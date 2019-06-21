package tickTackToe;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;

public class GraphicsHandler extends Canvas implements Runnable {
	private static final long serialVersionUID = 1L;

	Board board;
	Display display;

	public boolean needUpdate = true;

	public GraphicsHandler(Board displayBoard, Display display1) {
		board = displayBoard;
		display = display1;

		this.addMouseListener(display);
		this.addMouseMotionListener(display);

		/*Thread thread = new Thread(this);
		thread.start();*/
	}

	@Override
	public void run() {
		/*while (true) {
			if (needUpdate) {
				this.repaint();
			}
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}*/
	}

	public void paint(Graphics g) {
		this.setSize(display.getContentPane().getSize());
		System.out.println(this.getSize());
		int width = (int) this.getSize().getWidth();
		int height = (int) this.getSize().getHeight();

		int lineWidth = 1;
		for (int i = 1; i < 27; i++) {
			if (i % 9 == 0) {
				g.setColor(Color.red);
				lineWidth = 3;
			} else if (i % 3 == 0) {
				g.setColor(Color.blue);
				lineWidth = 2;
			} else {
				g.setColor(Color.black);
				lineWidth = 1;
				g.drawLine(i * width / 27, 0, i * width / 27, height);
				g.drawLine(0, i * height / 27, width, i * height / 27);
				continue;
			}
			g.fillRect(i * width / 27 - lineWidth / 2, 0, lineWidth, height);
			g.fillRect(0, i * height / 27 - lineWidth / 2, width, lineWidth);
		}

		if (display.hasFocus() && this.getMousePosition() != null) {
			double mouseX = display.displayBoardX((int) this.getMousePosition().getX());
			double mouseY = display.displayBoardY((int) this.getMousePosition().getY());

			double next1X = mouseX % 9 * 3;
			double next1Y = mouseY % 9 * 3;
			double next2X = mouseX % 3 * 9;
			double next2Y = mouseY % 3 * 9;

			System.out
					.println(mouseX + ", " + mouseY + " | " + next1X + ", " + next1Y + " | " + next2X + ", " + next2Y);
			g.setColor(Color.red);
			g.drawRect((int) (mouseX * (width / 27.0)), (int) (mouseY * (height / 27.0)), width / 27, height / 27);
			g.setColor(Color.MAGENTA);
			g.drawRect((int) (next1X * (width / 27.0)), (int) (next1Y * (height / 27.0)), width / 9, height / 9);
			g.setColor(Color.ORANGE);
			g.drawRect((int) (next2X * (width / 27.0)), (int) (next2Y * (width / 27.0) - next2Y), width / 3,
					height / 3);
		}
	}

}
