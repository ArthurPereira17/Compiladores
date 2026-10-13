#!/usr/bin/env python3
"""
Recalcula PARSER_TABLE e PRODUCTIONS de ParserConstants.java a partir da
gramatica do arquivo .gals (mesma numeracao do GALS: producoes numeradas na
ordem em que aparecem; nao-terminais na ordem de #NonTerminals).

Uso:
  python3 gerar_tabela_ll1.py ../sintatico4.gals ../src/compilador/Constants.java \
          ../src/compilador/ParserConstants.java [--check]

--check : nao grava; apenas compara o resultado com o que ja esta no arquivo.

IMPORTANTE: o caminho oficial e abrir o .gals no GALS e gerar os arquivos.
Este script existe para conferir/reproduzir essa geracao (foi validado
contra a saida do GALS da BNF anterior). Depois de gerar, rode
gerar_mensagens.py para refazer o PARSER_ERROR.
"""
import re
import sys

EPS = "\u00ee"  # 'i' com circunflexo, simbolo de epsilon usado no .gals


def carregar(path_gals, path_consts):
    with open(path_gals, encoding="latin-1") as f:
        gals = f.read().replace("\r", "")
    with open(path_consts, encoding="utf-8") as f:
        consts = f.read()

    # terminais
    term = {}
    for nome, i in re.findall(r"int t_(\w+) = (\d+);", consts):
        if not nome.startswith("TOKEN_"):
            term[nome] = int(i)
    for i, lex in re.findall(r"int t_TOKEN_\d+ = (\d+); //\"(.*)\"", consts):
        term['"%s"' % lex] = int(i)
    term["$"] = 1

    # nao-terminais (ordem de declaracao)
    sec_nt = gals.split("#NonTerminals")[1].split("#Grammar")[0]
    nts = re.findall(r"<(\w+)>", sec_nt)
    first_nt = max(term.values()) + 1
    nt_id = {n: first_nt + i for i, n in enumerate(nts)}

    # gramatica
    sec_g = gals.split("#Grammar")[1]
    linhas = [l for l in sec_g.split("\n") if not l.strip().startswith("//")]
    texto = " ".join(linhas)
    regras = [r.strip() for r in texto.split(";") if r.strip()]

    producoes = []          # lista de (nt, [simbolos ids])
    for r in regras:
        m = re.match(r"<(\w+)>\s*::=\s*(.*)$", r, re.S)
        nome, rhs = m.group(1), m.group(2)
        alt = []
        for tok in re.findall(r'"[^"]*"|<\w+>|\||' + EPS + r"|\w+", rhs):
            if tok == "|":
                producoes.append((nome, alt))
                alt = []
            elif tok == EPS:
                pass
            elif tok.startswith("<"):
                alt.append(nt_id[tok[1:-1]])
            elif tok in term:
                alt.append(term[tok])
            else:
                raise SystemExit("simbolo desconhecido: " + tok)
        producoes.append((nome, alt))
    return nts, nt_id, first_nt, term, producoes


def tabela(nts, nt_id, first_nt, term, producoes):
    n_term = max(term.values())
    prods = [(nt_id[n], a) for n, a in producoes]

    nullable = {i: False for i in nt_id.values()}
    first = {i: set() for i in nt_id.values()}
    mudou = True
    while mudou:
        mudou = False
        for a, rhs in prods:
            todos_nul = True
            for s in rhs:
                if s < first_nt:
                    if s not in first[a]:
                        first[a].add(s); mudou = True
                    todos_nul = False
                    break
                if not first[s] <= first[a]:
                    first[a] |= first[s]; mudou = True
                if not nullable[s]:
                    todos_nul = False
                    break
            if todos_nul and not nullable[a]:
                nullable[a] = True; mudou = True

    follow = {i: set() for i in nt_id.values()}
    follow[first_nt].add(1)
    mudou = True
    while mudou:
        mudou = False
        for a, rhs in prods:
            for k, s in enumerate(rhs):
                if s < first_nt:
                    continue
                resto_nul = True
                for t in rhs[k + 1:]:
                    if t < first_nt:
                        if t not in follow[s]:
                            follow[s].add(t); mudou = True
                        resto_nul = False
                        break
                    if not first[t] <= follow[s]:
                        follow[s] |= first[t]; mudou = True
                    if not nullable[t]:
                        resto_nul = False
                        break
                if resto_nul and not follow[a] <= follow[s]:
                    follow[s] |= follow[a]; mudou = True

    tab = [[-1] * n_term for _ in nts]
    conflitos = []
    for p, (a, rhs) in enumerate(prods):
        cols, nul = set(), True
        for s in rhs:
            if s < first_nt:
                cols.add(s); nul = False
                break
            cols |= first[s]
            if not nullable[s]:
                nul = False
                break
        if nul:
            cols |= follow[a]
        for t in cols:
            cel = tab[a - first_nt][t - 1]
            if cel not in (-1, p):
                conflitos.append((nts[a - first_nt], t, cel, p))
            tab[a - first_nt][t - 1] = p
    return tab, [r for _, r in prods], conflitos


def fmt_tabela(tab):
    return ",\n".join(
        "        { " + ", ".join("%2d" % v for v in l) + " }" for l in tab)


def fmt_prods(ps):
    return ",\n".join(
        "        { " + (", ".join("%2d" % v for v in p) if p else " 0") + " }"
        for p in ps)


def main():
    gals, consts, pc = sys.argv[1:4]
    check = "--check" in sys.argv
    nts, nt_id, first_nt, term, producoes = carregar(gals, consts)
    tab, ps, conf = tabela(nts, nt_id, first_nt, term, producoes)
    if conf:
        print("CONFLITOS LL(1) (gramatica nao e LL(1)):")
        for c in conf:
            print("  <%s> terminal %d: producoes %d e %d" % c)
        sys.exit(1)

    with open(pc, encoding="utf-8") as f:
        src = f.read()
    re_t = re.compile(r"(int\[\]\[\] PARSER_TABLE =\s*\{\n)(.*?)(\n    \};)", re.S)
    re_p = re.compile(r"(int\[\]\[\] PRODUCTIONS =\s*\{\n)(.*?)(\n    \};)", re.S)
    novo_t, novo_p = fmt_tabela(tab), fmt_prods(ps)
    if check:
        ok_t = re_t.search(src).group(2) == novo_t
        ok_p = re_p.search(src).group(2) == novo_p
        print("tabela igual:", ok_t, "| producoes iguais:", ok_p,
              "| %d producoes, %d nao-terminais" % (len(ps), len(nts)))
        sys.exit(0 if ok_t and ok_p else 2)

    src = re_t.sub(lambda m: m.group(1) + novo_t + m.group(3), src)
    src = re_p.sub(lambda m: m.group(1) + novo_p + m.group(3), src)
    with open(pc, "w", encoding="utf-8", newline="\n") as f:
        f.write(src)
    print("PARSER_TABLE e PRODUCTIONS gravados (%d producoes)" % len(ps))


if __name__ == "__main__":
    main()
