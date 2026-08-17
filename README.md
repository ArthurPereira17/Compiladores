Interface do Compilador - Trabalho Final parte 1
==================================================

COMO RODAR:
  1. Certifique-se de ter o Java (JRE 11+) instalado.
  2. No terminal, dentro desta pasta: java -jar dist/compilador.jar
     (ou dê duplo clique em dist/compilador.jar no Windows, se a
     associação de arquivos .jar estiver configurada)

COMO COMPILAR A PARTIR DO CÓDIGO-FONTE:
  javac -d build src/compilador/CompilerInterface.java
  jar cfe compilador.jar compilador.CompilerInterface -C build .

ESTRUTURA:
  src/compilador/NumberedBorder.java   -> borda com numeração de linhas (fornecida)
  src/compilador/IconFactory.java      -> ícones dos botões, gerados via código
  src/compilador/CompilerInterface.java-> janela principal (toda a lógica da interface)
  dist/compilador.jar                  -> executável pronto

IMPORTANTE ANTES DE ENTREGAR:
  - Edite a lista EQUIPE em CompilerInterface.java com os nomes reais
    dos integrantes da equipe (usada no botão "equipe").
  - Renomeie a pasta/arquivo compactado para "interface" + número da equipe,
    conforme pede o enunciado.