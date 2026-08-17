package compilador;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;

public class CompilerInterface extends JFrame {

	private static final long serialVersionUID = 1L;

	private static final String[] EQUIPE = {
			"Arthur Nascimento Pereira",
			"Ryan Henrique Vieira"
	};

	private static final String MSG_COMPILAR = "compilação de programas ainda não foi implementada";

	private final JTextArea editor;
	private final JTextArea areaMensagens;
	private final JLabel barraStatus;

	private File arquivoAtual = null;

	public CompilerInterface() {
		super("Compilador");

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1500, 800);
		setResizable(false);
		setLocationRelativeTo(null);

		editor = new JTextArea();
		editor.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
		editor.setLineWrap(false);
		editor.setBorder(BorderFactory.createCompoundBorder(
				new NumberedBorder(),
				BorderFactory.createEmptyBorder(0, 4, 0, 0)));

		JScrollPane scrollEditor = new JScrollPane(editor,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

		areaMensagens = new JTextArea();
		areaMensagens.setEditable(false);
		areaMensagens.setLineWrap(false);
		areaMensagens.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));

		JScrollPane scrollMensagens = new JScrollPane(areaMensagens,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

		JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, scrollEditor, scrollMensagens);
		splitPane.setResizeWeight(0.75);
		splitPane.setOneTouchExpandable(false);
		splitPane.setContinuousLayout(true);

		barraStatus = new JLabel(" ");
		barraStatus.setBorder(BorderFactory.createEmptyBorder(2, 6, 2, 6));
		barraStatus.setPreferredSize(new Dimension(10, 25));
		barraStatus.setHorizontalAlignment(SwingConstants.LEFT);

		JPanel barraFerramentas = criarBarraFerramentas();

		JPanel content = new JPanel(new BorderLayout());
		content.add(barraFerramentas, BorderLayout.WEST);
		content.add(splitPane, BorderLayout.CENTER);
		content.add(barraStatus, BorderLayout.SOUTH);

		setContentPane(content);
		configurarAtalhosGlobais();
	}

	private JPanel criarBarraFerramentas() {
		JPanel painel = new JPanel();
		painel.setLayout(new BoxLayout(painel, BoxLayout.Y_AXIS));
		painel.setPreferredSize(new Dimension(150, 10));
		painel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));

		Dimension tamanhoBotao = new Dimension(140, 56);

		painel.add(criarBotao("novo", "novo [ctrl-n]", tamanhoBotao, e -> acaoNovo()));
		painel.add(espaco());
		painel.add(criarBotao("abrir", "abrir [ctrl-o]", tamanhoBotao, e -> acaoAbrir()));
		painel.add(espaco());
		painel.add(criarBotao("salvar", "salvar [ctrl-s]", tamanhoBotao, e -> acaoSalvar()));
		painel.add(espaco());
		painel.add(criarBotao("copiar", "copiar [ctrl-c]", tamanhoBotao, e -> editor.copy()));
		painel.add(espaco());
		painel.add(criarBotao("colar", "colar [ctrl-v]", tamanhoBotao, e -> editor.paste()));
		painel.add(espaco());
		painel.add(criarBotao("recortar", "recortar [ctrl-x]", tamanhoBotao, e -> editor.cut()));
		painel.add(espaco());
		painel.add(criarBotao("compilar", "compilar [F7]", tamanhoBotao, e -> acaoCompilar()));
		painel.add(espaco());
		painel.add(criarBotao("equipe", "equipe [F1]", tamanhoBotao, e -> acaoEquipe()));

		painel.add(javax.swing.Box.createVerticalGlue());
		return painel;
	}

	private java.awt.Component espaco() {
		return javax.swing.Box.createRigidArea(new Dimension(0, 6));
	}

	private JButton criarBotao(String icone, String texto, Dimension tamanho, java.awt.event.ActionListener acao) {
		JButton botao = new JButton(texto, IconFactory.get(icone));
		botao.setVerticalTextPosition(SwingConstants.BOTTOM);
		botao.setHorizontalTextPosition(SwingConstants.CENTER);
		botao.setMaximumSize(tamanho);
		botao.setPreferredSize(tamanho);
		botao.setMinimumSize(tamanho);
		botao.setAlignmentX(JButton.CENTER_ALIGNMENT);
		botao.setFont(botao.getFont().deriveFont(11f));
		botao.addActionListener(acao);
		return botao;
	}

	private void configurarAtalhosGlobais() {
		bindAtalho(KeyStroke.getKeyStroke("control N"), "novo", e -> acaoNovo());
		bindAtalho(KeyStroke.getKeyStroke("control O"), "abrir", e -> acaoAbrir());
		bindAtalho(KeyStroke.getKeyStroke("control S"), "salvar", e -> acaoSalvar());
		bindAtalho(KeyStroke.getKeyStroke("control C"), "copiar", e -> editor.copy());
		bindAtalho(KeyStroke.getKeyStroke("control V"), "colar", e -> editor.paste());
		bindAtalho(KeyStroke.getKeyStroke("control X"), "recortar", e -> editor.cut());
		bindAtalho(KeyStroke.getKeyStroke("F7"), "compilar", e -> acaoCompilar());
		bindAtalho(KeyStroke.getKeyStroke("F1"), "equipe", e -> acaoEquipe());
	}

	private void bindAtalho(KeyStroke tecla, String nome, java.awt.event.ActionListener listener) {
		getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(tecla, nome);
		getRootPane().getActionMap().put(nome, new AbstractAction() {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				listener.actionPerformed(e);
			}
		});
	}

	private void acaoNovo() {
		editor.setText("");
		areaMensagens.setText("");
		arquivoAtual = null;
		barraStatus.setText(" ");
	}

	private void acaoAbrir() {
		JFileChooser chooser = new JFileChooser();
		chooser.setFileFilter(new FileNameExtensionFilter("Arquivos de texto (*.txt)", "txt"));
		int resultado = chooser.showOpenDialog(this);

		if (resultado != JFileChooser.APPROVE_OPTION) {
			return;
		}

		File arquivo = chooser.getSelectedFile();
		try {
			String conteudo = new String(Files.readAllBytes(arquivo.toPath()));
			editor.setText(conteudo);
			editor.setCaretPosition(0);
			areaMensagens.setText("");
			arquivoAtual = arquivo;
			atualizarBarraStatus();
		} catch (IOException ex) {
			JOptionPane.showMessageDialog(this,
					"Não foi possível abrir o arquivo: " + ex.getMessage(),
					"Erro", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void acaoSalvar() {
		File destino = arquivoAtual;

		if (destino == null) {
			JFileChooser chooser = new JFileChooser();
			chooser.setFileFilter(new FileNameExtensionFilter("Arquivos de texto (*.txt)", "txt"));
			int resultado = chooser.showSaveDialog(this);
			if (resultado != JFileChooser.APPROVE_OPTION) {
				return;
			}
			destino = chooser.getSelectedFile();
			if (!destino.getName().toLowerCase().endsWith(".txt")) {
				destino = new File(destino.getParentFile(), destino.getName() + ".txt");
			}
		}

		try (FileWriter writer = new FileWriter(destino)) {
			writer.write(editor.getText());
		} catch (IOException ex) {
			JOptionPane.showMessageDialog(this,
					"Não foi possível salvar o arquivo: " + ex.getMessage(),
					"Erro", JOptionPane.ERROR_MESSAGE);
			return;
		}

		boolean eraArquivoNovo = (arquivoAtual == null);
		arquivoAtual = destino;
		areaMensagens.setText("");

		if (eraArquivoNovo) {
			atualizarBarraStatus();
		}
	}

	private void acaoCompilar() {
		areaMensagens.setText(MSG_COMPILAR);
	}

	private void acaoEquipe() {
		StringBuilder sb = new StringBuilder();
		for (String nome : EQUIPE) {
			if (nome != null && !nome.isBlank()) {
				if (sb.length() > 0) {
					sb.append("\n");
				}
				sb.append(nome);
			}
		}
		areaMensagens.setText(sb.toString());
	}

	private void atualizarBarraStatus() {
		if (arquivoAtual == null) {
			barraStatus.setText(" ");
			return;
		}
		String pasta = arquivoAtual.getParent() != null ? arquivoAtual.getParent() : "";
		barraStatus.setText(" " + pasta + File.separator + arquivoAtual.getName());
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			CompilerInterface frame = new CompilerInterface();
			frame.setVisible(true);
		});
	}
}