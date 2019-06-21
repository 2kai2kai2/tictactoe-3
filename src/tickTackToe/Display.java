package tickTackToe;

import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JTextArea;

public class Display extends JFrame implements MouseListener, MouseMotionListener {

	private static final long serialVersionUID = 1L;

	private Board board;
	private GraphicsHandler graphics;
	public static Scanner s = new Scanner(System.in);

	public int headerHeight;

	public Display() {
		board = new Board();
		boolean running = true;

		headerHeight = this.getHeight();
		this.setTitle("Tic-Tac-Toe");
		this.setSize(600, 600 + headerHeight);
		graphics = new GraphicsHandler(board, this);
		this.add(graphics);

		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);

	}

	public int displayBoardX(int pixX) {
		return (int)(pixX / ((double)graphics.getWidth() / 27));

	}

	public int displayBoardY(int pixY) {
		return (int)(pixY / ((double)graphics.getHeight() / 27));

	}

	@Override
	public void mouseClicked(MouseEvent e) {
		int x = displayBoardX(e.getX());
		int y = displayBoardY(e.getY());
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseDragged(MouseEvent e) {
	}
	
	private int prevMouseX = 0;
	private int prevMouseY = 0;
	
	@Override
	public void mouseMoved(MouseEvent e) {
		int x = displayBoardX(e.getX());
		int y = displayBoardY(e.getY());
		if (x != prevMouseX || y != prevMouseY) {
			prevMouseX = x;
			prevMouseY = y;
			graphics.repaint();
		}
	}

}
