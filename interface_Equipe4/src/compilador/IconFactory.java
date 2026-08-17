package compilador;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.geom.Path2D;

import javax.swing.ImageIcon;

/**
 * Factory for creating toolbar icons programmatically.
 * Generates 22x22 pixel icons with high visibility.
 */
final class IconFactory {

	private static final int SIZE = 22;

	private IconFactory() {
	}

	static ImageIcon get(String tipo) {
		BufferedImage img = new BufferedImage(SIZE, SIZE, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = img.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setStroke(new BasicStroke(1.8f));

		String chave = tipo == null ? "" : tipo.trim().toLowerCase();

		switch (chave) {
		case "novo":
			drawNewIcon(g);
			break;
		case "abrir":
			drawOpenIcon(g);
			break;
		case "salvar":
			drawSaveIcon(g);
			break;
		case "copiar":
			drawCopyIcon(g);
			break;
		case "colar":
			drawPasteIcon(g);
			break;
		case "recortar":
			drawCutIcon(g);
			break;
		case "compilar":
			drawCompileIcon(g);
			break;
		case "equipe":
			drawTeamIcon(g);
			break;
		default:
			// Default icon - a question mark in a box
			g.setColor(Color.DARK_GRAY);
			g.draw(new Rectangle2D.Double(2, 2, 18, 18));
			g.setColor(Color.BLACK);
			g.drawString("?", 10, 16);
		}
		g.dispose();
		return new ImageIcon(img);
	}

	/**
	 * Draws a "new file" icon: a simple document with a plus.
	 */
	private static void drawNewIcon(Graphics2D g) {
		// White document with blue border
		g.setColor(Color.WHITE);
		g.fill(new RoundRectangle2D.Double(3, 3, 16, 16, 2, 2));
		g.setColor(new Color(0, 100, 200));
		g.draw(new RoundRectangle2D.Double(3, 3, 16, 16, 2, 2));

		// Plus sign
		g.setColor(new Color(0, 100, 200));
		g.draw(new Line2D.Double(9, 7, 9, 15));   // Vertical
		g.draw(new Line2D.Double(7, 11, 11, 11)); // Horizontal
	}

	/**
	 * Draws an "open file" icon: a simple folder.
	 */
	private static void drawOpenIcon(Graphics2D g) {
		// Folder tab
		g.setColor(new Color(200, 150, 50));
		g.fill(new Rectangle2D.Double(4, 4, 12, 6));

		// Folder body
		g.setColor(new Color(200, 150, 50));
		g.fill(new RoundRectangle2D.Double(4, 8, 14, 12, 2, 2));

		// Folder border
		g.setColor(new Color(150, 100, 0));
		g.draw(new Rectangle2D.Double(4, 4, 12, 6));
		g.draw(new RoundRectangle2D.Double(4, 8, 14, 12, 2, 2));

		// Document inside
		g.setColor(Color.WHITE);
		g.fill(new Rectangle2D.Double(7, 10, 8, 8));
		g.setColor(new Color(150, 100, 0));
		g.draw(new Rectangle2D.Double(7, 10, 8, 8));
		g.drawLine(9, 12, 11, 12);
		g.drawLine(9, 14, 11, 14);
	}

	/**
	 * Draws a "save file" icon: a floppy disk.
	 */
	private static void drawSaveIcon(Graphics2D g) {
		// Disk body
		g.setColor(Color.WHITE);
		g.fill(new RoundRectangle2D.Double(4, 4, 14, 14, 2, 2));
		g.setColor(new Color(100, 100, 100));
		g.draw(new RoundRectangle2D.Double(4, 4, 14, 14, 2, 2));

		// Disk details
		g.setColor(new Color(100, 100, 100));
		g.fill(new Rectangle2D.Double(7, 5, 8, 4)); // Upper bar
		g.fill(new Rectangle2D.Double(6, 10, 10, 6)); // Lower bar

		// Disk hole
		g.setColor(Color.WHITE);
		g.fill(new Ellipse2D.Double(9, 8, 4, 4));

		// Save arrow
		g.setColor(new Color(100, 100, 100));
		g.draw(new Line2D.Double(10, 7, 10, 11)); // Vertical
		g.draw(new Line2D.Double(10, 11, 12, 11)); // Horizontal right
		g.draw(new Line2D.Double(10, 11, 8, 11));  // Horizontal left
		g.draw(new Line2D.Double(10, 11, 9, 12));  // Diagonal up-left
		g.draw(new Line2D.Double(10, 11, 11, 12)); // Diagonal up-right
	}

	/**
	 * Draws a "copy" icon: two overlapping documents.
	 */
	private static void drawCopyIcon(Graphics2D g) {
		// Shadow document (behind)
		g.setColor(new Color(240, 240, 240));
		g.fill(new RoundRectangle2D.Double(4, 6, 12, 12, 2, 2));

		// Main document (front)
		g.setColor(Color.WHITE);
		g.fill(new RoundRectangle2D.Double(6, 4, 12, 12, 2, 2));

		// Borders
		g.setColor(new Color(80, 80, 80));
		g.draw(new RoundRectangle2D.Double(4, 6, 12, 12, 2, 2));
		g.draw(new RoundRectangle2D.Double(6, 4, 12, 12, 2, 2));

		// Document lines
		g.setColor(new Color(80, 80, 80));
		g.drawLine(8, 6, 12, 6);
		g.drawLine(8, 8, 12, 8);
		g.drawLine(8, 10, 12, 10);
		g.drawLine(8, 12, 12, 12);
		g.drawLine(8, 14, 12, 14);

		g.drawLine(6, 8, 10, 8);
		g.drawLine(6, 10, 10, 10);
		g.drawLine(6, 12, 10, 12);
		g.drawLine(6, 14, 10, 14);
	}

	/**
	 * Draws a "paste" icon: a clipboard with document.
	 */
	private static void drawPasteIcon(Graphics2D g) {
		// Clipboard base
		g.setColor(new Color(220, 200, 160));
		g.fill(new RoundRectangle2D.Double(4, 4, 14, 14, 2, 2));

		// Clipboard grip
		g.setColor(new Color(180, 160, 120));
		g.fill(new Rectangle2D.Double(8, 2, 6, 2));

		// Clipboard border
		g.setColor(new Color(120, 100, 60));
		g.draw(new RoundRectangle2D.Double(4, 4, 14, 14, 2, 2));
		g.draw(new Rectangle2D.Double(8, 2, 6, 2));

		// Document on clipboard
		g.setColor(Color.WHITE);
		g.fill(new Rectangle2D.Double(6, 6, 10, 10));
		g.setColor(new Color(160, 140, 100));
		g.draw(new Rectangle2D.Double(6, 6, 10, 10));
		g.drawLine(8, 8, 12, 8);
		g.drawLine(8, 10, 12, 10);
		g.drawLine(8, 12, 12, 12);
	}

	/**
	 * Draws a "cut" icon: scissors.
	 */
	private static void drawCutIcon(Graphics2D g) {
		// Left scissor handle
		g.setColor(new Color(200, 50, 50));
		g.draw(new Line2D.Double(5, 5, 9, 9));
		g.draw(new Line2D.Double(9, 9, 13, 5));

		// Right scissor handle
		g.draw(new Line2D.Double(9, 5, 13, 9));
		g.draw(new Line2D.Double(13, 9, 17, 5));

		// Pivot point
		g.setColor(new Color(180, 40, 40));
		g.fill(new Ellipse2D.Double(10, 8, 4, 4));

		// Blades
		g.setColor(new Color(200, 50, 50));
		g.draw(new Line2D.Double(8, 9, 6, 13));
		g.draw(new Line2D.Double(6, 13, 4, 15));
		g.draw(new Line2D.Double(12, 9, 14, 13));
		g.draw(new Line2D.Double(14, 13, 16, 15));

		// Blade details
		g.setColor(new Color(180, 40, 40));
		g.draw(new Line2D.Double(5, 12, 7, 12));
		g.draw(new Line2D.Double(15, 12, 17, 12));
	}

	/**
	 * Draws a "compile" icon: gear/cog with play triangle.
	 */
	private static void drawCompileIcon(Graphics2D g) {
		// Gear outer circle
		g.setColor(new Color(50, 180, 50));
		g.fill(new Ellipse2D.Double(4, 4, 14, 14));

		// Gear inner circle (hole)
		g.setColor(Color.WHITE);
		g.fill(new Ellipse2D.Double(8, 8, 6, 6));

		// Gear teeth (simple version)
		g.setColor(new Color(50, 180, 50));
		g.fill(new Rectangle2D.Double(6, 3, 2, 4));   // Top
		g.fill(new Rectangle2D.Double(14, 6, 2, 2));  // Right-top
		g.fill(new Rectangle2D.Double(14, 10, 2, 2)); // Right-bottom
		g.fill(new Rectangle2D.Double(6, 15, 2, 4));  // Bottom
		g.fill(new Rectangle2D.Double(2, 6, 2, 2));   // Left-top
		g.fill(new Rectangle2D.Double(2, 10, 2, 2));  // Left-bottom

		// Play triangle
		g.setColor(Color.WHITE);
		Path2D triangle = new Path2D.Double();
		triangle.moveTo(9, 7);
		triangle.lineTo(9, 13);
		triangle.lineTo(13, 10);
		triangle.closePath();
		g.fill(triangle);

		// Gear border
		g.setColor(new Color(30, 140, 30));
		g.draw(new Ellipse2D.Double(4, 4, 14, 14));
		g.draw(new Ellipse2D.Double(8, 8, 6, 6));
	}

	/**
	 * Draws a "team" icon: two people silhouettes.
	 */
	private static void drawTeamIcon(Graphics2D g) {
		// First person (left)
		g.setColor(new Color(100, 100, 200));
		g.fill(new Ellipse2D.Double(4, 4, 6, 6)); // Head
		g.fill(new Rectangle2D.Double(6, 10, 2, 8)); // Body

		// Second person (right)
		g.fill(new Ellipse2D.Double(12, 4, 6, 6)); // Head
		g.fill(new Rectangle2D.Double(14, 10, 2, 8)); // Body

		// Connection line (representing teamwork)
		g.setColor(new Color(80, 80, 180));
		g.draw(new Line2D.Double(8, 14, 16, 14));

		// Outlines
		g.setColor(new Color(60, 60, 160));
		g.draw(new Ellipse2D.Double(4, 4, 6, 6));
		g.draw(new Rectangle2D.Double(6, 10, 2, 8));
		g.draw(new Ellipse2D.Double(12, 4, 6, 6));
		g.draw(new Rectangle2D.Double(14, 10, 2, 8));
	}
}