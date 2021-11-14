package tictactoe;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.Serial;

import javax.swing.JFrame;

public class Display extends JFrame implements MouseListener, MouseMotionListener {

	@Serial
	private static final long serialVersionUID = 1L;

	public static boolean running;

	private final Board board;
	private final GraphicsHandler graphics;

	public int headerHeight;

	public Display() {
		board = new Board();

		running = true;

		headerHeight = this.getHeight();
		this.setTitle("Tic-Tac-Toe^3");
		this.setSize(600, 600 + headerHeight);
		graphics = new GraphicsHandler(board, this);
		this.add(graphics);

		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);

		graphics.initialize();
	}

	/**
	 * Convert pixel to grid X coordinate.
	 * 
	 * @param pixX The X pixel on the display.
	 * @return The X value on the grid (number of inner boxes from the left, 0-26).
	 */
	public int displayBoardX(double pixX) {
		int calc = (int)(pixX / (graphics.getSize().getWidth() / 27.0));
		return Math.max(0, Math.min(calc, 26));
	}

	/**
	 * Convert pixel to grid Y coordinate.
	 * 
	 * @param pixY The Y pixel on the display.
	 * @return The Y value on the grid (number of inner boxes from the top, 0-26).
	 */
	public int displayBoardY(double pixY) {
		int calc = (int)(pixY / (graphics.getSize().getHeight() / 27.0));
		return Math.max(0, Math.min(calc, 26));
	}

	/**
	 * Convert grid X coordinate to pixel.
	 *
	 * @param displayX The X value on the grid (number of inner boxes from the top, 0-26).
	 * @return The X pixel on the display.
	 */
	public int displayPixelX(int displayX) {
		return (int)(displayX * graphics.getSize().getWidth() / 27.0);
	}

	/**
	 * Convert grid Y coordinate to pixel.
	 *
	 * @param displayY The Y value on the grid (number of inner boxes from the top, 0-26).
	 * @return The Y pixel on the display.
	 */
	public int displayPixelY(int displayY) {
		return (int)(displayY * graphics.getSize().getHeight() / 27.0);
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
		int x = displayBoardX(e.getPoint().getX());
		int y = displayBoardY(e.getPoint().getY());

		int out = Board.numBoardOuter(x, y);
		int mid = Board.numBoardMiddle(x, y);
		int in = Board.numBoardInner(x, y);

		// This space is not taken
		if (board.get(out, mid, in) == Board.BLANK
				// This space is allowed based on previous placement
				&& ((out == board.lastMiddle() && mid == board.lastInner())
						// Or, there is no requirements by previous placement
						|| (board.lastOuter() < 0 && board.lastMiddle() < 0 && board.lastInner() < 0))
				// This box is not won on any level
				&& (board.getWinnerMiddle(out, mid) == Board.BLANK && board.getWinnerOuter(out) == Board.BLANK
						&& board.winner == Board.BLANK)) {
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