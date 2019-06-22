package tickTackToe;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
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
	 * To be called only after being added to the display
	 */
	public void initialize() {
		this.createBufferStrategy(3);
		buffer = this.getBufferStrategy();
		thread = new Thread(this);
		thread.start();
	}
	
	@Override
	public void run() {
		while (true) {
			if (lastMS + 1000 / FPSCAP < System.currentTimeMillis()) {
				Graphics graphics;
				try {
					graphics = buffer.getDrawGraphics();
					draw(graphics);
					graphics.dispose();
				} finally {
					buffer.show();
				}
				lastMS = System.currentTimeMillis();
			}
		}
	}

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
				g.setColor(Color.red);
				lineWidth = 3;
			} else if (i % 3 == 0) {
				g.setColor(Color.blue);
				lineWidth = 2;
			} else {
				g.setColor(Color.white);
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
				int out = board.numBoardOuter(i, j);
				int mid = board.numBoardMiddle(i, j);
				int in = board.numBoardInner(i, j);

				int team = board.get(out, mid, in);
				if (team == board.X) {
					g.setColor(Color.cyan);
					g.drawLine(i * width / 27, j * height / 27, (i + 1) * width / 27, (j + 1) * height / 27);
					g.drawLine((i + 1) * width / 27, j * height / 27, i * width / 27, (j + 1) * height / 27);
				} else if (team == board.O) {
					g.setColor(Color.cyan);
					// Draw 2 circles to form a thick circle
					g.drawOval(i * width / 27, j * height / 27, width / 27, height / 27);
				} // else nothing
			}
		}
		// Draw symbols on lower boxes
		for (int i = 0; i < 27; i += 3) {
			for (int j = 0; j < 27; j += 3) {
				int out = board.numBoardOuter(i, j);
				int mid = board.numBoardMiddle(i, j);

				int team = board.getWinnerMiddle(out, mid);
				if (team == board.X) {
					g.setColor(Color.cyan);
					g.drawLine(i * width / 27, j * height / 27, (i + 3) * width / 27, (j + 3) * height / 27);
					g.drawLine((i + 3) * width / 27, j * height / 27, i * width / 27, (j + 3) * height / 27);
				} else if (team == board.O) {
					g.setColor(Color.cyan);
					g.drawOval(i * width / 27, j * height / 27, width / 9, height / 9);
				}
			}
		}
		// Draw symbols on larger boxes
		for (int i = 0; i < 27; i += 9) {
			for (int j = 0; j < 27; j += 9) {
				int out = board.numBoardOuter(i, j);

				int team = board.getWinnerOuter(out);
				if (team == board.X) {
					g.setColor(Color.cyan);
					g.drawLine(i * width / 27, j * height / 27, (i + 9) * width / 27, (j + 9) * height / 27);
					g.drawLine((i + 9) * width / 27, j * height / 27, i * width / 27, (j + 9) * height / 27);
				} else if (team == board.O) {
					g.setColor(Color.cyan);
					g.drawOval(i * width / 27, j * height / 27, width / 3, height / 3);
				}
			}
		}

		if (display.isFocused() && this.getMousePosition() != null) {
			double mouseX = display.displayBoardX((int) this.getMousePosition().getX());
			double mouseY = display.displayBoardY((int) this.getMousePosition().getY());

			double next1X = mouseX % 9 * 3;
			double next1Y = mouseY % 9 * 3;
			double next2X = mouseX % 3 * 9;
			double next2Y = mouseY % 3 * 9;

			g.setColor(Color.red);
			g.drawRect((int) (mouseX * (width / 27.0)), (int) (mouseY * (height / 27.0)), width / 27, height / 27);
			g.drawRect((int) (mouseX * (width / 27.0)) + 1, (int) (mouseY * (height / 27.0)) + 1, width / 27 - 2,
					height / 27 - 2);
			g.setColor(Color.MAGENTA);
			g.drawRect((int) (next1X * (width / 27.0)), (int) (next1Y * (height / 27.0)), width / 9, height / 9);
			g.drawRect((int) (next1X * (width / 27.0)) + 1, (int) (next1Y * (height / 27.0)) + 1, width / 9 - 2,
					height / 9 - 2);
			g.setColor(Color.ORANGE);
			g.drawRect((int) (next2X * (width / 27.0)), (int) (next2Y * (width / 27.0) - next2Y), width / 3,
					height / 3);
			g.drawRect((int) (next2X * (width / 27.0)) + 1, (int) (next2Y * (width / 27.0) - next2Y) + 1, width / 3 - 2,
					height / 3 - 2);
		}
	}
}
