package tickTackToe;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Scanner;

import javax.swing.JFrame;

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

		graphics.initialize();

	}

	public int displayBoardX(int pixX) {
		return (int) (pixX / ((double) graphics.getWidth() / 27));

	}

	public int displayBoardY(int pixY) {
		return (int) (pixY / ((double) graphics.getHeight() / 27));

	}

	@Override
	public void mouseClicked(MouseEvent e) {

	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
		int x = displayBoardX(e.getX());
		int y = displayBoardY(e.getY());

		int out = Board.numBoardOuter(x, y);
		int mid = Board.numBoardMiddle(x, y);
		int in = Board.numBoardInner(x, y);
		// TODO: Make this be just inner except when necessary
		if (board.get(out, mid, in) == board.BLANK && ((out == board.lastMiddle() && mid == board.lastInner())
				|| (board.lastOuter() == -1 && board.lastMiddle() == -1 && board.lastInner() == -1))) {
			board.place(out, mid, in);
		}
		board.checkWins();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseDragged(MouseEvent e) {
	}

	@Override
	public void mouseMoved(MouseEvent e) {
	}

}
