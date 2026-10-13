package compilador;

/**
 * Executa as analises lexica e sintatica e devolve a mensagem para a area de
 * mensagens do compilador.
 */
public class Analisador {

    public static final String SUCESSO = "programa compilado com sucesso";

    public static String analisar(String fonte) {
        LexicoCompilador lexico = new LexicoCompilador(fonte);
        Sintatico sintatico = new Sintatico();
        Semantico semantico = new Semantico();

        try {
            sintatico.parse(lexico, semantico);
            return SUCESSO;
        } catch (LexicalError e) {
            return "linha " + calcularLinha(fonte, e.getPosition()) + ": erro léxico";
        } catch (SyntaticError e) {
            return "linha " + calcularLinha(fonte, e.getPosition())
                    + ": encontrado " + lexemaEncontrado(lexico.getUltimoToken())
                    + " esperado " + e.getMessage();
        } catch (SemanticError e) {
            return "linha " + calcularLinha(fonte, e.getPosition()) + ": " + e.getMessage();
        }
    }

    private static String lexemaEncontrado(Token t) {
        if (t == null || t.getId() == Constants.DOLLAR) {
            return "EOF";
        }
        if (t.getId() == Constants.t_constanteString) {
            return "constante_string";
        }
        return t.getLexeme();
    }

    static int calcularLinha(String fonte, int posicao) {
        int linha = 1;
        int limite = Math.min(Math.max(posicao, 0), fonte.length());
        for (int i = 0; i < limite; i++) {
            if (fonte.charAt(i) == '\n') {
                linha++;
            }
        }
        return linha;
    }
}
