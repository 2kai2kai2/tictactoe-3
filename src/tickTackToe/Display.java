package tickTackToe;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Scanner;

import javax.swing.JFrame;

public class Display extends JFrame implements MouseListener, MouseMotionListener {

	private static final long serialVersionUID = 1L;
	
	public static boolean running;

	private Board board;
	private GraphicsHandler graphics;
	public static Scanner s = new Scanner(System.in);

	public int headerHeight;

	public Display() {
		board = new Board();

		running = true;

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
		// This space is not taken
		if (board.get(out, mid, in) == board.BLANK
				// This space is allowed based on previous placement
				&& ((out == board.lastMiddle() && mid == board.lastInner())
						// Or, there is no requirements by previous placement
						|| (board.lastOuter() == -1 && board.lastMiddle() == -1 && board.lastInner() == -1))
				// This box is not won on any level
				&& (board.getWinnerMiddle(out, mid) == 0 && board.getWinnerOuter(out) == 0)) {
			board.place(out, mid, in);
		}
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
