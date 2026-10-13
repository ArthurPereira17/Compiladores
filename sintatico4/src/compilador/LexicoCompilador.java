package compilador;

/**
 * Extensao do Lexico gerado pelo GALS (que nao pode ser alterado).
 *
 * 1) Palavra que segue o padrao de palavra reservada, mas nao e nenhuma das
 *    palavras da linguagem, volta do Lexico com o id t_Preservada. Aqui ela
 *    passa a ser tratada como erro lexico.
 * 2) Guarda o ultimo token entregue ao parser, que e o token "encontrado"
 *    quando ocorre um erro sintatico (null = fim do programa).
 */
public class LexicoCompilador extends Lexico {

    private Token ultimoToken = null;

    public LexicoCompilador(String input) {
        super(input);
    }

    @Override
    public Token nextToken() throws LexicalError {
        Token t = super.nextToken();
        if (t != null && t.getId() == Constants.t_Preservada) {
            throw new LexicalError("palavra reservada invalida", t.getPosition());
        }
        ultimoToken = t;
        return t;
    }

    public Token getUltimoToken() {
        return ultimoToken;
    }
}
