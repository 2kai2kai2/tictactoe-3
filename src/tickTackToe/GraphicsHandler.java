package tickTackToe;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferStrategy;

public class GraphicsHandler extends Canvas implements Runnable {
	private static final long serialVersionUID = 1L;

	private Board board;
	private Display display;

	private BufferStrategy buffer;

	public boolean needUpdate = true;
	private Thread thread;

	private final int FPSCAP = 120;

	private long lastMS = 0;

	public GraphicsHandler(Board displayBoard, Display display1) {
		board = displayBoard;
		display = display1;

		this.addMouseListener(display);
		this.addMouseMotionListener(display);
	}

	/**
	 * To be called only after being added to the display. Initializes values that
	 * need to be started after this object is added to the JFrame.
	 */
	public void initialize() {
		this.createBufferStrategy(3);
		buffer = this.getBufferStrategy();
		thread = new Thread(this);
		thread.start();
	}

	@Override
	public void run() {
		Graphics graphics;
		while (true) {
			if (lastMS + 1000 / FPSCAP < System.currentTimeMillis()) {
				try {
					graphics = buffer.getDrawGraphics();
					draw(graphics);
					if (!Display.running) {
						endscreen(graphics);
					}
					graphics.dispose();
				} finally {
					buffer.show();
				}
				lastMS = System.currentTimeMillis();
			}
		}
	}

	/**
	 * Draws the board and all contained objects & indicators.
	 * 
	 * @param g The Graphics object used for the display.
	 */
	public void draw(Graphics g) {
		this.setSize(display.getContentPane().getSize());

		int width = (int) this.getSize().getWidth();
		int height = (int) this.getSize().getHeight();

		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(0, 0, width, height);

		// Draw lines
		int lineWidth = 1;
		for (int i = 1; i < 27; i++) {
			if (i % 9 == 0) {
				g.setColor(Color.BLACK);
				lineWidth = 3;
			} else if (i % 3 == 0) {
				g.setColor(Color.WHITE);
				lineWidth = 2;
			} else {
				g.setColor(Color.DARK_GRAY);
				lineWidth = 1;
				g.drawLine(i * width / 27, 0, i * width / 27, height);
				g.drawLine(0, i * height / 27, width, i * height / 27);
				continue;
			}
			g.fillRect(i * width / 27 - lineWidth / 2, 0, lineWidth, height);
			g.fillRect(0, i * height / 27 - lineWidth / 2, width, lineWidth);
		}

		// Draw symbols on induvidual squares
		for (int i = 0; i < 27; i++) {
			for (int j = 0; j < 27; j++) {
				int out = Board.numBoardOuter(i, j);
				int mid = Board.numBoardMiddle(i, j);
				int in = Board.numBoardInner(i, j);

				int team = board.get(out, mid, in);
				if (team == board.X) {
					g.setColor(Color.RED);
					g.drawLine(i * width / 27, j * height / 27, (i + 1) * width / 27, (j + 1) * height / 27);
					g.drawLine((i + 1) * width / 27, j * height / 27, i * width / 27, (j + 1) * height / 27);
				} else if (team == board.O) {
					g.setColor(Color.GREEN);
					// Draw 2 circles to form a thick circle
					g.drawOval(i * width / 27, j * height / 27, width / 27, height / 27);
				} // else nothing
			}
		}
		// Draw symbols on lower boxes
		for (int i = 0; i < 27; i += 3) {
			for (int j = 0; j < 27; j += 3) {
				int out = Board.numBoardOuter(i, j);
				int mid = Board.numBoardMiddle(i, j);

				int team = board.getWinnerMiddle(out, mid);
				if (team == board.X) {
					g.setColor(Color.RED);
					g.drawLine(i * width / 27, j * height / 27, (i + 3) * width / 27, (j + 3) * height / 27);
					g.drawLine((i + 3) * width / 27, j * height / 27, i * width / 27, (j + 3) * height / 27);
				} else if (team == board.O) {
					g.setColor(Color.GREEN);
					g.drawOval(i * width / 27, j * height / 27, width / 9, height / 9);
				}
			}
		}
		// Draw symbols on larger boxes
		for (int i = 0; i < 27; i += 9) {
			for (int j = 0; j < 27; j += 9) {
				int out = Board.numBoardOuter(i, j);

				int team = board.getWinnerOuter(out);
				if (team == board.X) {
					g.setColor(Color.RED);
					g.drawLine(i * width / 27, j * height / 27, (i + 9) * width / 27, (j + 9) * height / 27);
					g.drawLine((i + 9) * width / 27, j * height / 27, i * width / 27, (j + 9) * height / 27);
				} else if (team == board.O) {
					g.setColor(Color.GREEN);
					g.drawOval(i * width / 27, j * height / 27, width / 3, height / 3);
				}
			}
		}

		if (board.lastInner() != -1 && board.lastMiddle() != -1 && board.lastOuter() != -1 && display.running) {
			int next1X = Board.displayBoardX(board.lastMiddle(), board.lastInner(), 0);
			int next1Y = Board.displayBoardY(board.lastMiddle(), board.lastInner(), 0);

			if (board.currentPlayer() == board.O) {
				g.setColor(Color.GREEN);
			} else if (board.currentPlayer() == board.X) {
				g.setColor(Color.RED);
			}
			g.drawRect((int) (next1X * (width / 27.0)), (int) (next1Y * (height / 27.0)), width / 9, height / 9);
			g.drawRect((int) (next1X * (width / 27.0)) + 1, (int) (next1Y * (height / 27.0)) + 1, width / 9 - 2,
					height / 9 - 2);

			int next2X = Board.displayBoardX(board.lastInner(), 0, 0);
			int next2Y = Board.displayBoardY(board.lastInner(), 0, 0);

			if (board.currentPlayer() == board.O) {
				g.setColor(Color.ORANGE);
			} else if (board.currentPlayer() == board.X) {
				g.setColor(new Color(120, 200, 120));
			}
			g.drawRect((int) (next2X * (width / 27.0)), (int) (next2Y * (width / 27.0) - next2Y), width / 3,
					height / 3);
			g.drawRect((int) (next2X * (width / 27.0)) + 1, (int) (next2Y * (width / 27.0) - next2Y) + 1, width / 3 - 2,
					height / 3 - 2);
		}

		if (display.isFocused() && this.getMousePosition() != null && display.running) {
			try {
				Point mouse = this.getMousePosition();
				double mouseX = display.displayBoardX((int) mouse.getX());
				double mouseY = display.displayBoardY((int) mouse.getY());
				// So that it only shows up if the mouse is in the current select-able space, or
				// if there is no selection
				if (((board.lastInner() == -1 && board.lastMiddle() == -1 && board.lastOuter() == -1) || (Board
						.displayBoardX(board.lastMiddle(), board.lastInner(), 0) == mouseX - mouseX % 3
						&& Board.displayBoardY(board.lastMiddle(), board.lastInner(), 0) == mouseY - mouseY % 3))
						&& board.getWinnerMiddle(Board.numBoardOuter((int) mouseX, (int) mouseY),
								Board.numBoardMiddle((int) mouseX, (int) mouseY)) == 0
						&& board.getWinnerOuter(Board.numBoardOuter((int) mouseX, (int) mouseY)) == 0) {
					double next1X = mouseX % 9 * 3;
					double next1Y = mouseY % 9 * 3;
					double next2X = mouseX % 3 * 9;
					double next2Y = mouseY % 3 * 9;

					g.setColor(Color.CYAN);
					g.drawRect((int) (mouseX * (width / 27.0)), (int) (mouseY * (height / 27.0)), width / 27,
							height / 27);
					g.drawRect((int) (mouseX * (width / 27.0)) + 1, (int) (mouseY * (height / 27.0)) + 1,
							width / 27 - 2, height / 27 - 2);
					g.setColor(new Color(50, 50, 255));
					g.drawRect((int) (next1X * (width / 27.0)), (int) (next1Y * (height / 27.0)), width / 9,
							height / 9);
					g.drawRect((int) (next1X * (width / 27.0)) + 1, (int) (next1Y * (height / 27.0)) + 1, width / 9 - 2,
							height / 9 - 2);
					g.setColor(Color.BLUE);
					g.drawRect((int) (next2X * (width / 27.0)), (int) (next2Y * (width / 27.0) - next2Y), width / 3,
							height / 3);
					g.drawRect((int) (next2X * (width / 27.0)) + 1, (int) (next2Y * (width / 27.0) - next2Y) + 1,
							width / 3 - 2, height / 3 - 2);
				}
			} catch (NullPointerException e) {
				// Eh.
			}
		}
	}

	/**
	 * Displays the end screen showing the winner or tie. NOTE: The Width and Height
	 * of this object are used.
	 * 
	 * @param g The Graphics object used as a display.
	 */
	private void endscreen(Graphics g) {
		if (board.winner == board.O) {
			g.setColor(Color.GREEN);
			for (int x = 0; x < this.getWidth(); x++) {
				for (int y = 0; y < this.getHeight(); y++) {
					int OX = x - this.getWidth() / 2;
					int OY = y - this.getHeight() / 2;
					if (Math.sqrt(OX * OX + OY * OY) < Math.min(this.getHeight(), this.getWidth()) / 2
							&& Math.sqrt(OX * OX + OY * OY) > Math.min(this.getHeight(), this.getWidth()) / 2 - 20) {
						g.drawRect(x, y, 1, 1);
					}
				}
			}
			g.setColor(Color.WHITE);
		} else if (board.winner == board.X) {
			int size = Math.min(this.getWidth(), this.getHeight());
			int offsetX = (this.getWidth() - size) / 2;
			int offsetY = (this.getHeight() - size) / 2;
			g.setColor(Color.RED);
			g.fillPolygon(
					new int[] { offsetX + 0, offsetX + 10, offsetX + size / 2, offsetX + size - 10, offsetX + size,
							offsetX + size / 2 + 10, offsetX + size, offsetX + size - 10, offsetX + size / 2,
							offsetX + 10, offsetX + 0, offsetX + size / 2 - 10 },
					new int[] { offsetY + 10, offsetY + 0, offsetY + size / 2 - 10, offsetY + 0, offsetY + 10,
							offsetY + size / 2, offsetY + size - 10, offsetY + size, offsetY + size / 2 + 10,
							offsetY + size, offsetY + size - 10, offsetY + size / 2 },
					12);

		} else if (board.winner == board.TIE) {
			// All the numbers in this are kinda finnicky
			g.setColor(Color.YELLOW);
			int sizeH = this.getHeight() / 2 * 3;
			int sizeW = this.getWidth() / 13 * 7;
			g.setFont(new Font(Font.MONOSPACED, Font.BOLD, Math.min(sizeH, sizeW)));
			g.drawString("TIE", 0, Math.min(sizeH, sizeW) * 2 / 3);
		}
	}
}
