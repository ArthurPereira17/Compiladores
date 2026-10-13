# Compilador 2026.2 — Parte 3: Analisador Sintático

Trabalho final de Compiladores. Editor com análise **léxica** e **sintática** (LL(1)) para a linguagem 2026.2, em Java, usando o GALS para gerar o scanner e o parser.

**Equipe 4:** Arthur Nascimento Pereira, Ryan Henrique Vieira

## Executar

Requer Java 17 ou superior (Windows 8+).

```
java -jar dist/compilador.jar
```

| Ação | Atalho |
|---|---|
| Novo / Abrir / Salvar | `Ctrl+N` / `Ctrl+O` / `Ctrl+S` |
| Copiar / Colar / Recortar | `Ctrl+C` / `Ctrl+V` / `Ctrl+X` |
| **Compilar** | `F7` |
| Equipe | `F1` |

## Compilar o projeto

É preciso um JDK 17+ (`javac` e `jar` no PATH).

```
build.bat      # Windows
./build.sh     # Linux / macOS
```

Gera `dist/compilador.jar`.

## Saída do botão Compilar

A área de mensagens é limpa e mostra **uma** destas saídas:

| Situação | Mensagem |
|---|---|
| Sem erros | `programa compilado com sucesso` |
| Erro léxico | `linha N: erro léxico` |
| Erro sintático | `linha N: encontrado X esperado Y` |

Regras da mensagem sintática:

- `X` é o lexema encontrado. Para constante string aparece `constante_string`, e para fim do programa `EOF`.
- `Y` vem do `PARSER_ERROR`, montado a partir da tabela LL(1).
  - Identificadores (`i_`, `f_`, `s_`, `b_`) aparecem como `identificador`, e as constantes como `constante_int`, `constante_float` e `constante_string`.
  - Não-terminais de expressão (`<expressao>`, `<relacional>`, `<aritmetica>`, `<fator>`, `<vetor>` etc.) e `<lista_expressoes>` dão `expressao`.
  - Os demais listam os terminais com entrada na linha da tabela, por exemplo `identificador if in out while`.
- Vale o **primeiro** erro encontrado, léxico ou sintático.

Exemplos:

```
linha 4: erro léxico
linha 5: encontrado i_lado esperado (
linha 1: encontrado EOF esperado expressao
```

## Estrutura

```
sintatico4/
├── sintatico4.gals            especificação léxica + BNF (LL(1)) — fonte oficial
├── dist/compilador.jar        executável
├── build.bat / build.sh       geram o .jar
├── src/compilador/
│   ├── Lexico, Sintatico, Semantico, Token, *Error,
│   │   Constants, ScannerConstants, ParserConstants    gerados pelo GALS
│   ├── LexicoCompilador.java  estende o Lexico (ver abaixo)
│   ├── Analisador.java        roda léxico + sintático e monta a mensagem
│   └── CompilerInterface.java interface (Swing)
└── tools/
    ├── gerar_tabela_ll1.py    recalcula PARSER_TABLE / PRODUCTIONS a partir do .gals
    ├── gerar_mensagens.py     reescreve o PARSER_ERROR a partir da tabela
    └── Tabela_Analise_LL1.html
```

### Classes geradas pelo GALS

A única alteração feita nelas, além da linha `package compilador;`, é a constante `PARSER_ERROR` em `ParserConstants.java`. Cada mensagem original do GALS foi mantida em comentário de linha ao lado da nova:

```java
"module", // "<forma_geral> inválido"
```

Não há alteração em `Lexico.java` nem em `Sintatico.java`. O comportamento extra fica em `LexicoCompilador`, que estende o `Lexico`:

- palavra com formato de palavra reservada que não existe na linguagem (`foo`, `While`) vira **erro léxico**;
- guarda o último token entregue ao parser, usado como "encontrado" nos erros sintáticos.

## Regenerar o parser

Se a gramática mudar:

1. Abra `sintatico4.gals` no GALS e gere os arquivos (scanner e parser, Java).
2. Copie-os para `src/compilador/` (com `package compilador;` no topo).
3. Refaça as mensagens de erro:
   ```
   python3 tools/gerar_mensagens.py src/compilador/ParserConstants.java
   ```
4. Rode `build.bat` ou `./build.sh`.

`tools/gerar_tabela_ll1.py ... --check` confere se a tabela e as produções do `ParserConstants.java` batem com o `.gals`.

## Observação sobre a vírgula

O float usa vírgula como separador decimal (`12,3`). Por isso uma constante inteira colada numa vírgula é lida como float:

| Código | Resultado |
|---|---|
| `out (1,2)` | um único float `1,2`, compila |
| `out (1, 2)` | erro léxico (float incompleto `1,`) |
| `out (1 , 2)` | dois argumentos, compila |

Isso vem da especificação léxica da linguagem, não do analisador sintático.

## Linguagem (resumo)

```
module [declarações] { comandos }
```

- **Identificadores:** `i_` (int), `f_` (float), `s_` (string), `b_` (bool), seguidos de letra e depois letras ou dígitos. Vetores são declarados com `i_v[10]` e usados com `i_v[expressão]`.
- **Constantes:** `123`, `12,3`, `"texto"`.
- **Comentário de bloco:** `$_ ... _$`.
- **Comandos:**
  - atribuição: `i_a <- expressão`, `i_v[0] <- expressão`
  - entrada e saída: `in (i_a, i_v[1])`, `out ("a = ", i_a)`
  - repetição: `while (expr) isTrueDo { ... }` ou `isFalseDo { ... }`
  - seleção: `if (expr) isTrueDo { ... } [isFalseDo { ... }]`, ou na ordem inversa (`isFalseDo` antes de `isTrueDo`)
- **Operadores:** `+ - * /`, relacionais `= <> < >`, lógicos `and or not`, constantes `true` e `false`.

## Testes

`testes_sintatico4.zip` traz 47 programas (corretos, com erro léxico e com erro sintático) com a saída esperada de cada um.

## Entrega

Arquivo compactado `sintatico4` com o código-fonte, o `.jar`, o `.gals` e demais recursos, postado no AVA até 23h de 23/10/2026.
