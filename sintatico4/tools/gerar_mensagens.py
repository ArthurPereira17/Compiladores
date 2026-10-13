#!/usr/bin/env python3
"""
Reescreve a constante PARSER_ERROR de ParserConstants.java (unica alteracao
permitida nas classes geradas pelo GALS) a partir da propria PARSER_TABLE.

Uso:  python3 gerar_mensagens.py ../src/compilador/ParserConstants.java

Pode ser reexecutado apos regerar os arquivos no GALS: as mensagens originais
do GALS (ou as ja alteradas, que ficam em comentario) sao preservadas
em comentario de linha ao lado da mensagem nova.
"""
import re
import sys

# nomes dos nao-terminais cuja mensagem deve ser sempre "expressao" (enunciado)
EXPRESSAO = {
    "expressao", "expressao_", "termo_logico", "relacional", "relacional_",
    "aritmetica", "aritmetica_", "termo_aritmetico", "termo_aritmetico_",
    "fator", "vetor",
    "lista_expressoes",          # lista de expressoes do comando out
    "lista_identificadores1",    # "[" <expressao> "]" opcional
}

# terminais por id (ver Constants.java)
TERMINAIS = {
    1: "EOF", 2: "palavra reservada",
    3: "identificador", 4: "identificador", 5: "identificador", 6: "identificador",
    7: "constante_int", 8: "constante_float", 9: "constante_string",
    10: "and", 11: "false", 12: "if", 13: "in", 14: "isFalseDo", 15: "isTrueDo",
    16: "module", 17: "not", 18: "or", 19: "out", 20: "true", 21: "while",
    22: ",", 23: "[", 24: "]", 25: "(", 26: ")", 27: "{", 28: "}",
    29: "+", 30: "-", 31: "*", 32: "/", 33: "<-", 34: "=", 35: "<>", 36: "<", 37: ">",
}


def main(path):
    with open(path, encoding="utf-8") as f:
        src = f.read()

    first_nt = int(re.search(r"FIRST_NON_TERMINAL\s*=\s*(\d+)", src).group(1))
    first_sem = int(re.search(r"FIRST_SEMANTIC_ACTION\s*=\s*(\d+)", src).group(1))

    # tabela
    corpo = re.search(r"PARSER_TABLE\s*=\s*\{(.*?)\n    \};", src, re.S).group(1)
    linhas = re.findall(r"\{([^{}]*)\}", corpo)
    tabela = [[int(x) for x in l.split(",")] for l in linhas]

    # bloco PARSER_ERROR
    m = re.search(r"(String\[\] PARSER_ERROR\s*=\s*\{)(.*?)(\n    \};)", src, re.S)
    entradas = []
    for linha in m.group(2).strip("\n").split("\n"):
        linha = linha.strip()
        if not linha:
            continue
        # entrada: "texto" [,] [// original]
        mm = re.match(r'^("(?:[^"\\]|\\.)*")\s*,?\s*(?://\s*(.*))?$', linha)
        if not mm:
            raise SystemExit("linha de PARSER_ERROR nao reconhecida: " + linha)
        atual, original = mm.group(1), mm.group(2)
        entradas.append((original if original else atual))
    total = len(entradas)
    assert total == first_sem, (total, first_sem)

    # nomes dos nao-terminais, extraidos das mensagens originais "<nome> invalido"
    nomes = {}
    for i in range(first_nt, first_sem):
        mm = re.match(r'"<(\w+)>', entradas[i])
        nomes[i] = mm.group(1) if mm else "?"

    novas = []
    for i in range(total):
        if i == 0:
            novas.append('""')
        elif i < first_nt:                      # terminal
            novas.append('"%s"' % TERMINAIS[i])
        else:                                   # nao-terminal
            nome = nomes[i]
            if nome in EXPRESSAO:
                msg = "expressao"
            else:
                partes, vistos = [], set()
                for t, v in enumerate(tabela[i - first_nt], start=1):
                    if v < 0:
                        continue
                    s = TERMINAIS[t]
                    if s not in vistos:         # "identificador" so uma vez
                        vistos.add(s)
                        partes.append(s)
                msg = " ".join(partes)
            novas.append('"%s"' % msg.replace("\\", "\\\\"))

    saida = []
    for i, (n, o) in enumerate(zip(novas, entradas)):
        virg = "," if i < total - 1 else ""
        saida.append("        %s%s //%s" % (n, virg, " " + o if o else ""))
    novo_bloco = m.group(1) + "\n" + "\n".join(saida) + m.group(3)
    src = src[:m.start()] + novo_bloco + src[m.end():]
    with open(path, "w", encoding="utf-8", newline="\n") as f:
        f.write(src)
    print("PARSER_ERROR reescrito (%d entradas)" % total)


if __name__ == "__main__":
    main(sys.argv[1])
