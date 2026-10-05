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
 * Generates high-visibility 22x22 pixel icons for all toolbar buttons.
 */
final class IconFactory {

	private static final int SIZE = 22;
	private static final int PADDING = 2; // Padding inside the icon
	private static final int INNER_SIZE = SIZE - (2 * PADDING); // 18x18 drawing area

	private IconFactory() {
	}

	static ImageIcon get(String tipo) {
		BufferedImage img = new BufferedImage(SIZE, SIZE, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = img.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setStroke(new BasicStroke(2.0f)); // Bolder stroke for better visibility

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
			// Default icon - a question mark in a box with high contrast
			g.setColor(Color.BLACK);
			g.draw(new Rectangle2D.Double(PADDING, PADDING, INNER_SIZE, INNER_SIZE));
			g.setColor(Color.WHITE);
			g.fill(new Rectangle2D.Double(PADDING + 1, PADDING + 1, INNER_SIZE - 2, INNER_SIZE - 2));
			g.setColor(Color.BLACK);
			g.drawString("?", PADDING + 6, PADDING + 12);
		}
		g.dispose();
		return new ImageIcon(img);
	}

	/**
	 * Draws a "new file" icon: a document with a prominent plus sign.
	 */
	private static void drawNewIcon(Graphics2D g) {
		// White document with thick blue border
		g.setColor(Color.WHITE);
		g.fill(new RoundRectangle2D.Double(PADDING, PADDING, INNER_SIZE, INNER_SIZE, 3, 3));
		g.setColor(new Color(0, 80, 180)); // Darker blue for better contrast
		g.draw(new RoundRectangle2D.Double(PADDING, PADDING, INNER_SIZE, INNER_SIZE, 3, 3));

		// Plus sign - thicker and more prominent
		g.setColor(new Color(0, 80, 180));
		int centerX = PADDING + INNER_SIZE / 2;
		int centerY = PADDING + INNER_SIZE / 2;
		int plusSize = 4;
		g.draw(new Line2D.Double(centerX, centerY - plusSize, centerX, centerY + plusSize));   // Vertical
		g.draw(new Line2D.Double(centerX - plusSize, centerY, centerX + plusSize, centerY)); // Horizontal
	}

	/**
	 * Draws an "open file" icon: a folder with clear tab and document.
	 */
	private static void drawOpenIcon(Graphics2D g) {
		// Folder tab - more prominent
		g.setColor(new Color(220, 140, 40)); // Brighter orange
		g.fill(new Rectangle2D.Double(PADDING + 2, PADDING + 2, INNER_SIZE - 4, 5));

		// Folder body
		g.setColor(new Color(220, 140, 40));
		g.fill(new RoundRectangle2D.Double(PADDING + 2, PADDING + 6, INNER_SIZE - 4, INNER_SIZE - 8, 2, 2));

		// Folder border
		g.setColor(new Color(180, 100, 0)); // Darker orange for border
		g.draw(new Rectangle2D.Double(PADDING + 2, PADDING + 2, INNER_SIZE - 4, 5));
		g.draw(new RoundRectangle2D.Double(PADDING + 2, PADDING + 6, INNER_SIZE - 4, INNER_SIZE - 8, 2, 2));

		// Document inside - more visible
		g.setColor(Color.WHITE);
		g.fill(new Rectangle2D.Double(PADDING + 5, PADDING + 9, INNER_SIZE - 8, INNER_SIZE - 11));
		g.setColor(new Color(180, 100, 0));
		g.draw(new Rectangle2D.Double(PADDING + 5, PADDING + 9, INNER_SIZE - 8, INNER_SIZE - 11));
		g.drawLine(PADDING + 7, PADDING + 11, PADDING + INNER_SIZE - 7, PADDING + 11);
		g.drawLine(PADDING + 7, PADDING + 13, PADDING + INNER_SIZE - 7, PADDING + 13);
	}

	/**
	 * Draws a "save file" icon: a classic floppy disk with clear details.
	 */
	private static void drawSaveIcon(Graphics2D g) {
		// Disk body
		g.setColor(Color.WHITE);
		g.fill(new RoundRectangle2D.Double(PADDING + 2, PADDING + 2, INNER_SIZE - 4, INNER_SIZE - 4, 3, 3));
		g.setColor(new Color(80, 80, 80)); // Darker gray for better contrast
		g.draw(new RoundRectangle2D.Double(PADDING + 2, PADDING + 2, INNER_SIZE - 4, INNER_SIZE - 4, 3, 3));

		// Disk details - more prominent
		g.setColor(new Color(80, 80, 80));
		g.fill(new Rectangle2D.Double(PADDING + 5, PADDING + 5, INNER_SIZE - 8, 3)); // Upper bar
		g.fill(new Rectangle2D.Double(PADDING + 4, PADDING + 11, INNER_SIZE - 6, 5)); // Lower bar

		// Disk hole
		g.setColor(Color.WHITE);
		g.fill(new Ellipse2D.Double(PADDING + 8, PADDING + 8, 4, 4));

		// Save arrow - more prominent
		g.setColor(new Color(80, 80, 80));
		int centerX = PADDING + INNER_SIZE / 2;
		int arrowBaseY = PADDING + 7;
		int arrowTipY = PADDING + 4;
		g.draw(new Line2D.Double(centerX, arrowBaseY, centerX, arrowTipY)); // Vertical
		g.draw(new Line2D.Double(centerX, arrowTipY, centerX + 3, arrowTipY + 3)); // Right diagonal
		g.draw(new Line2D.Double(centerX, arrowTipY, centerX - 3, arrowTipY + 3)); // Left diagonal
		g.draw(new Line2D.Double(centerX - 2, arrowBaseY, centerX + 2, arrowBaseY)); // Base
	}

	/**
	 * Draws a "copy" icon: two clearly overlapping documents.
	 */
	private static void drawCopyIcon(Graphics2D g) {
		// Shadow document (behind) - offset and lighter
		g.setColor(new Color(245, 245, 245));
		g.fill(new RoundRectangle2D.Double(PADDING + 2, PADDING + 5, INNER_SIZE - 4, INNER_SIZE - 4, 2, 2));

		// Main document (front)
		g.setColor(Color.WHITE);
		g.fill(new RoundRectangle2D.Double(PADDING + 4, PADDING + 2, INNER_SIZE - 4, INNER_SIZE - 4, 2, 2));

		// Borders
		g.setColor(new Color(80, 80, 80));
		g.draw(new RoundRectangle2D.Double(PADDING + 2, PADDING + 5, INNER_SIZE - 4, INNER_SIZE - 4, 2, 2));
		g.draw(new RoundRectangle2D.Double(PADDING + 4, PADDING + 2, INNER_SIZE - 4, INNER_SIZE - 4, 2, 2));

		// Document lines - more visible
		g.setColor(new Color(80, 80, 80));
		int docLeft = PADDING + 6;
		int docWidth = INNER_SIZE - 10;
		g.drawLine(docLeft, PADDING + 4, docLeft + docWidth, PADDING + 4);
		g.drawLine(docLeft, PADDING + 7, docLeft + docWidth, PADDING + 7);
		g.drawLine(docLeft, PADDING + 10, docLeft + docWidth, PADDING + 10);
		g.drawLine(docLeft, PADDING + 13, docLeft + docWidth, PADDING + 13);

		g.drawLine(PADDING + 5, PADDING + 6, PADDING + 5 + docWidth/3, PADDING + 6);
		g.drawLine(PADDING + 5, PADDING + 9, PADDING + 5 + docWidth/3, PADDING + 9);
		g.drawLine(PADDING + 5, PADDING + 12, PADDING + 5 + docWidth/3, PADDING + 12);
	}

	/**
	 * Draws a "paste" icon: a clipboard with clear document.
	 */
	private static void drawPasteIcon(Graphics2D g) {
		// Clipboard base
		g.setColor(new Color(230, 200, 150)); // Brighter beige
		g.fill(new RoundRectangle2D.Double(PADDING + 2, PADDING + 2, INNER_SIZE - 4, INNER_SIZE - 4, 2, 2));

		// Clipboard grip
		g.setColor(new Color(200, 160, 100));
		g.fill(new Rectangle2D.Double(PADDING + 8, PADDING, INNER_SIZE - 14, 3));

		// Clipboard border
		g.setColor(new Color(150, 100, 50)); // Darker for border
		g.draw(new RoundRectangle2D.Double(PADDING + 2, PADDING + 2, INNER_SIZE - 4, INNER_SIZE - 4, 2, 2));
		g.draw(new Rectangle2D.Double(PADDING + 8, PADDING, INNER_SIZE - 14, 3));

		// Document on clipboard
		g.setColor(Color.WHITE);
		g.fill(new Rectangle2D.Double(PADDING + 4, PADDING + 4, INNER_SIZE - 8, INNER_SIZE - 8));
		g.setColor(new Color(180, 150, 100));
		g.draw(new Rectangle2D.Double(PADDING + 4, PADDING + 4, INNER_SIZE - 8, INNER_SIZE - 8));
		g.drawLine(PADDING + 6, PADDING + 6, PADDING + INNER_SIZE - 6, PADDING + 6);
		g.drawLine(PADDING + 6, PADDING + 9, PADDING + INNER_SIZE - 6, PADDING + 9);
		g.drawLine(PADDING + 6, PADDING + 12, PADDING + INNER_SIZE - 6, PADDING + 12);
	}

	/**
	 * Draws a "cut" icon: clear scissors.
	 */
	private static void drawCutIcon(Graphics2D g) {
		// Left scissor handle
		g.setColor(new Color(180, 30, 30)); // Brighter red
		g.draw(new Line2D.Double(PADDING + 3, PADDING + 3, PADDING + 7, PADDING + 7));
		g.draw(new Line2D.Double(PADDING + 7, PADDING + 7, PADDING + 11, PADDING + 3));

		// Right scissor handle
		g.draw(new Line2D.Double(PADDING + 7, PADDING + 3, PADDING + 11, PADDING + 7));
		g.draw(new Line2D.Double(PADDING + 11, PADDING + 7, PADDING + 15, PADDING + 3));

		// Pivot point
		g.setColor(new Color(150, 20, 20)); // Darker red
		g.fill(new Ellipse2D.Double(PADDING + 8, PADDING + 6, 4, 4));

		// Blades
		g.setColor(new Color(180, 30, 30));
		g.draw(new Line2D.Double(PADDING + 6, PADDING + 7, PADDING + 4, PADDING + 11));
		g.draw(new Line2D.Double(PADDING + 4, PADDING + 11, PADDING + 3, PADDING + 13));
		g.draw(new Line2D.Double(PADDING + 10, PADDING + 7, PADDING + 12, PADDING + 11));
		g.draw(new Line2D.Double(PADDING + 12, PADDING + 11, PADDING + 13, PADDING + 13));

		// Blade details
		g.setColor(new Color(150, 20, 20));
		g.draw(new Line2D.Double(PADDING + 4, PADDING + 10, PADDING + 6, PADDING + 10));
		g.draw(new Line2D.Double(PADDING + 12, PADDING + 10, PADDING + 14, PADDING + 10));
	}

	/**
	 * Draws a "compile" icon: gear with clear play triangle.
	 */
	private static void drawCompileIcon(Graphics2D g) {
		// Gear outer circle
		g.setColor(new Color(40, 160, 40)); // Brighter green
		g.fill(new Ellipse2D.Double(PADDING + 2, PADDING + 2, INNER_SIZE - 4, INNER_SIZE - 4));

		// Gear inner circle (hole)
		g.setColor(Color.WHITE);
		g.fill(new Ellipse2D.Double(PADDING + 6, PADDING + 6, INNER_SIZE - 12, INNER_SIZE - 12));

		// Gear teeth - more prominent
		g.setColor(new Color(40, 160, 40));
		int gearCenterX = PADDING + INNER_SIZE / 2;
		int gearCenterY = PADDING + INNER_SIZE / 2;
		int gearRadius = (INNER_SIZE - 4) / 2;
		int innerRadius = (INNER_SIZE - 12) / 2;
		int toothLength = 2;

		// Draw 6 teeth
		for (int i = 0; i < 6; i++) {
			double angle = i * Math.PI / 3;
			int x1 = (int) (gearCenterX + (gearRadius - toothLength) * Math.cos(angle));
			int y1 = (int) (gearCenterY + (gearRadius - toothLength) * Math.sin(angle));
			int x2 = (int) (gearCenterX + gearRadius * Math.cos(angle));
			int y2 = (int) (gearCenterY + gearRadius * Math.sin(angle));
			g.draw(new Line2D.Double(x1, y1, x2, y2));
		}

		// Play triangle - more prominent
		g.setColor(Color.WHITE);
		int triangleX1 = PADDING + 7;
		int triangleY1 = PADDING + 5;
		int triangleX2 = PADDING + 7;
		int triangleY2 = PADDING + INNER_SIZE - 5;
		int triangleX3 = PADDING + INNER_SIZE - 5;
		int triangleY3 = PADDING + INNER_SIZE / 2;
		Path2D triangle = new Path2D.Double();
		triangle.moveTo(triangleX1, triangleY1);
		triangle.lineTo(triangleX2, triangleY2);
		triangle.lineTo(triangleX3, triangleY3);
		triangle.closePath();
		g.fill(triangle);

		// Gear border
		g.setColor(new Color(20, 120, 20)); // Darker green
		g.draw(new Ellipse2D.Double(PADDING + 2, PADDING + 2, INNER_SIZE - 4, INNER_SIZE - 4));
		g.draw(new Ellipse2D.Double(PADDING + 6, PADDING + 6, INNER_SIZE - 12, INNER_SIZE - 12));
	}

	/**
	 * Draws a "team" icon: two clear people silhouettes.
	 */
	private static void drawTeamIcon(Graphics2D g) {
		// First person (left)
		g.setColor(new Color(60, 60, 180)); // Brighter blue
		g.fill(new Ellipse2D.Double(PADDING + 2, PADDING + 2, 6, 6)); // Head
		g.fill(new Rectangle2D.Double(PADDING + 5, PADDING + 9, 3, 8)); // Body

		// Second person (right)
		g.fill(new Ellipse2D.Double(PADDING + INNER_SIZE - 8, PADDING + 2, 6, 6)); // Head
		g.fill(new Rectangle2D.Double(PADDING + INNER_SIZE - 5, PADDING + 9, 3, 8)); // Body

		// Connection line (representing teamwork) - more prominent
		g.setColor(new Color(40, 40, 150));
		g.setStroke(new BasicStroke(2.5f));
		g.draw(new Line2D.Double(PADDING + 5, PADDING + 13, PADDING + INNER_SIZE - 5, PADDING + 13));
		g.setStroke(new BasicStroke(2.0f)); // Reset to default stroke

		// Outlines
		g.setColor(new Color(30, 30, 120)); // Darker blue for outline
		g.draw(new Ellipse2D.Double(PADDING + 2, PADDING + 2, 6, 6));
		g.draw(new Rectangle2D.Double(PADDING + 5, PADDING + 9, 3, 8));
		g.draw(new Ellipse2D.Double(PADDING + INNER_SIZE - 8, PADDING + 2, 6, 6));
		g.draw(new Rectangle2D.Double(PADDING + INNER_SIZE - 5, PADDING + 9, 3, 8));
	}
}