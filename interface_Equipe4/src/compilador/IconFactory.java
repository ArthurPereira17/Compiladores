import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.geom.Path2D;

import javax.swing.ImageIcon;

final class IconFactory {

	private static final int SIZE = 22;

	private IconFactory() {
	}

	static ImageIcon get(String tipo) {
		BufferedImage img = new BufferedImage(SIZE, SIZE, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = img.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setStroke(new BasicStroke(1.6f));

		switch (tipo) {
			case "novo":
				g.setColor(new Color(70, 130, 200));
				g.fillRoundRect(4, 2, 12, 18, 3, 3);
				g.setColor(Color.WHITE);
				g.fillPolygon(new int[] { 12, 16, 16 }, new int[] { 2, 2, 6 }, 3);
				g.setColor(new Color(30, 70, 120));
				g.drawRoundRect(4, 2, 12, 18, 3, 3);
				g.drawLine(7, 9, 13, 9);
				g.drawLine(7, 13, 13, 13);
				break;
			case "abrir":
				g.setColor(new Color(230, 175, 60));
				g.fillRoundRect(2, 8, 18, 10, 2, 2);
				Path2D tab = new Path2D.Double();
				tab.moveTo(3, 8);
				tab.lineTo(6, 4);
				tab.lineTo(14, 4);
				tab.lineTo(16, 8);
				tab.closePath();
				g.fill(tab);
				g.setColor(new Color(160, 115, 30));
				g.drawRoundRect(2, 8, 18, 10, 2, 2);
				break;
			case "salvar":
				g.setColor(new Color(90, 90, 100));
				g.fillRoundRect(3, 2, 16, 18, 2, 2);
				g.setColor(Color.WHITE);
				g.fillRect(6, 3, 8, 6);
				g.fillRect(5, 12, 12, 7);
				g.setColor(new Color(60, 60, 70));
				g.drawRoundRect(3, 2, 16, 18, 2, 2);
				break;
			case "copiar":
				g.setColor(new Color(250, 250, 250));
				g.fillRect(7, 3, 11, 14);
				g.setColor(new Color(90, 90, 90));
				g.drawRect(7, 3, 11, 14);
				g.setColor(new Color(230, 230, 230));
				g.fillRect(3, 6, 11, 14);
				g.setColor(new Color(60, 60, 60));
				g.drawRect(3, 6, 11, 14);
				break;
			case "colar":
				g.setColor(new Color(210, 190, 150));
				g.fillRoundRect(4, 3, 14, 17, 2, 2);
				g.setColor(new Color(150, 130, 90));
				g.drawRoundRect(4, 3, 14, 17, 2, 2);
				g.setColor(new Color(120, 100, 60));
				g.fillRoundRect(8, 1, 6, 3, 1, 1);
				g.setColor(Color.WHITE);
				g.drawLine(7, 8, 15, 8);
				g.drawLine(7, 11, 15, 11);
				g.drawLine(7, 14, 12, 14);
				break;
			case "recortar":
				g.setColor(new Color(200, 70, 70));
				g.drawLine(3, 3, 17, 17);
				g.drawLine(3, 17, 17, 3);
				g.setColor(new Color(200, 70, 70));
				g.drawOval(2, 14, 5, 5);
				g.drawOval(13, 14, 5, 5);
				break;
			case "compilar":
				g.setColor(new Color(60, 160, 90));
				int[] px = { 5, 5, 17 };
				int[] py = { 3, 19, 11 };
				g.fillPolygon(px, py, 3);
				g.setColor(new Color(30, 110, 60));
				g.drawPolygon(px, py, 3);
				break;
			case "equipe":
				g.setColor(new Color(120, 110, 200));
				g.fillOval(2, 6, 8, 8);
				g.fillOval(11, 6, 8, 8);
				g.fillArc(0, 12, 10, 10, 0, 180);
				g.fillArc(9, 12, 10, 10, 0, 180);
				break;
			default:
				g.setColor(Color.GRAY);
				g.fillRect(2, 2, 16, 16);
		}
		g.dispose();
		return new ImageIcon(img);
	}
}
