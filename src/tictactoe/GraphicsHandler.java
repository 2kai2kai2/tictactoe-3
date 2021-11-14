package tictactoe;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.io.Serial;

public class GraphicsHandler extends Canvas implements Runnable {
    @Serial
    private static final long serialVersionUID = 1L;
    private static final int FPSCAP = 120;


    private final Board board;
    private final Display display;

    private BufferStrategy buffer;


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
        Thread thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        Graphics2D graphics;
        while (true) {
            if (lastMS + 1000 / FPSCAP < System.currentTimeMillis()) {
                try {
                    graphics = (Graphics2D) buffer.getDrawGraphics();
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
    public void draw(Graphics2D g) {
        this.setSize(display.getContentPane().getSize());

        int width = this.getWidth();
        int height = this.getHeight();

        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(0, 0, width, height);

        // Draw lines
        for (int i = 1; i < 27; i++) {
            if (i % 9 == 0) {
                g.setColor(Color.BLACK);
                g.setStroke(new BasicStroke(3));
            } else if (i % 3 == 0) {
                g.setColor(Color.WHITE);
                g.setStroke(new BasicStroke(2));
            } else {
                g.setColor(Color.DARK_GRAY);
                g.setStroke(new BasicStroke(1));
            }
            g.drawLine(display.displayPixelX(i), 0, display.displayPixelX(i), height);
            g.drawLine(0, display.displayPixelY(i), width, display.displayPixelY(i));
        }

        // Draw symbols on individual squares
        for (int x = 0; x < 27; x++) {
            for (int y = 0; y < 27; y++) {
                int out = Board.numBoardOuter(x, y);
                int mid = Board.numBoardMiddle(x, y);
                int in = Board.numBoardInner(x, y);

                int team = board.get(out, mid, in);

                int top = display.displayPixelY(y);
                int bottom = display.displayPixelY(y + 1);
                int left = display.displayPixelX(x);
                int right = display.displayPixelX(x + 1);

                switch (team) {
                    case Board.X:
                        g.setColor(Color.RED);
                        g.drawLine(left, top, right, bottom);
                        g.drawLine(right, top, left, bottom);
                        break;
                    case Board.O:
                        g.setColor(Color.GREEN);
                        g.drawOval(left, top, display.displayPixelX(1), display.displayPixelY(1));
                        break;
                    case Board.BLANK:
                        break;
                    default:
                        throw new IllegalStateException("Invalid tile owner at (" + x + ", " + y + "): " + team);
                }
            }
        }
        // Draw symbols on lower boxes
        for (int x = 0; x < 27; x += 3) {
            for (int y = 0; y < 27; y += 3) {
                int out = Board.numBoardOuter(x, y);
                int mid = Board.numBoardMiddle(x, y);

                int team = board.getWinnerMiddle(out, mid);

                int top = display.displayPixelY(y);
                int bottom = display.displayPixelY(y + 3);
                int left = display.displayPixelX(x);
                int right = display.displayPixelX(x + 3);

                switch (team) {
                    case Board.X:
                        g.setColor(Color.RED);
                        g.drawLine(left, top, right, bottom);
                        g.drawLine(right, top, left, bottom);
                        break;
                    case Board.O:
                        g.setColor(Color.GREEN);
                        g.drawOval(left, top, display.displayPixelX(3), display.displayPixelY(3));
                        break;
                    case Board.BLANK:
                        break;
                    default:
                        throw new IllegalStateException("Invalid mid tile owner at (" + x / 3 + ", " + y / 3 + "): " + team);
                }
            }
        }
        // Draw symbols on larger boxes
        for (int x = 0; x < 27; x += 9) {
            for (int y = 0; y < 27; y += 9) {
                int out = Board.numBoardOuter(x, y);

                int team = board.getWinnerOuter(out);

                int top = display.displayPixelY(y);
                int bottom = display.displayPixelY(y + 9);
                int left = display.displayPixelX(x);
                int right = display.displayPixelX(x + 9);

                switch (team) {
                    case Board.X:
                        g.setColor(Color.RED);
                        g.drawLine(left, top, right, bottom);
                        g.drawLine(right, top, left, bottom);
                        break;
                    case Board.O:
                        g.setColor(Color.GREEN);
                        g.drawOval(left, top, display.displayPixelX(9), display.displayPixelY(9));
                        break;
                    case Board.BLANK:
                        break;
                    default:
                        throw new IllegalStateException("Invalid outer tile owner at (" + x / 9 + ", " + y / 9 + "): " + team);
                }
            }
        }

        // Shows the highlight of the upcoming required selection boxes
        if (board.lastInner() >= 0 && board.lastMiddle() >= 0 && board.lastOuter() >= 0 && Display.running) {
            int next1X = Board.displayBoardX(board.lastMiddle(), board.lastInner(), 0);
            int next1Y = Board.displayBoardY(board.lastMiddle(), board.lastInner(), 0);

            if (board.currentPlayer() == Board.O) {
                g.setColor(Color.GREEN);
            } else if (board.currentPlayer() == Board.X) {
                g.setColor(Color.RED);
            }
            g.setStroke(new BasicStroke(2));
            g.drawRect(display.displayPixelX(next1X), display.displayPixelY(next1Y), display.displayPixelX(3), display.displayPixelY(3));

            int next2X = Board.displayBoardX(board.lastInner(), 0, 0);
            int next2Y = Board.displayBoardY(board.lastInner(), 0, 0);

            if (board.currentPlayer() == Board.O) {
                g.setColor(Color.ORANGE);
            } else if (board.currentPlayer() == Board.X) {
                g.setColor(new Color(80, 200, 80)); // Dark green
            }
            g.drawRect(display.displayPixelX(next2X), display.displayPixelY(next2Y), display.displayPixelX(9), display.displayPixelY(9));
        }

        // Shows the highlight of the mouse-selected upcoming boxes.
        Point mouse = this.getMousePosition();
        if (display.isFocused() && mouse != null && Display.running) {
            int mouseX = display.displayBoardX(mouse.getX());
            int mouseY = display.displayBoardY(mouse.getY());

            int indexOuter = Board.numBoardOuter(mouseX, mouseY);
            int indexMiddle = Board.numBoardMiddle(mouseX, mouseY);

            boolean isUnbounded = board.lastInner() < 0 || board.lastMiddle() < 0 || board.lastOuter() < 0;
            boolean noTileWinner = board.getWinnerMiddle(indexOuter, indexMiddle) == 0 && board.getWinnerOuter(indexOuter) == 0;
            boolean isInBounds = Board.displayBoardX(board.lastMiddle(), board.lastInner(), 0) / 3 == mouseX / 3
                    && Board.displayBoardY(board.lastMiddle(), board.lastInner(), 0) / 3 == mouseY / 3;
            // So that it only shows up if the mouse is in the current select-able space, or
            // if there is no selection
            if ((isUnbounded || isInBounds) && noTileWinner) {
                int next1X = mouseX % 9 * 3;
                int next1Y = mouseY % 9 * 3;
                int next2X = mouseX % 3 * 9;
                int next2Y = mouseY % 3 * 9;

                g.setStroke(new BasicStroke((2)));
                // Cursor
                g.setColor(Color.CYAN);
                g.drawRect(display.displayPixelX(mouseX), display.displayPixelY(mouseY), display.displayPixelX(1), display.displayPixelY(1));
                // Box of the next placeable mid-board
                g.setColor(new Color(50, 50, 255));
                g.drawRect(display.displayPixelX(next1X), display.displayPixelY(next1Y), display.displayPixelX(3), display.displayPixelY(3));
                // Box of the next-next placeable outer-board
                g.setColor(Color.BLUE);
                g.drawRect(display.displayPixelX(next2X), display.displayPixelY(next2Y) /*- next2Y*/, display.displayPixelX(9), display.displayPixelY(9));
            }
        }
    }

    /**
     * Displays the end screen showing the winner or tie. NOTE: The Width and Height
     * of this object are used.
     *
     * @param g The Graphics2D object used as a display.
     */
    private void endscreen(Graphics2D g) {
        int size = Math.min(this.getWidth(), this.getHeight());
        switch (board.winner) {
            case Board.O -> {
                g.setColor(Color.GREEN);
                g.setStroke(new BasicStroke(20));
                g.drawOval(20, 20, size - 40, size - 40);
                g.setColor(Color.WHITE);
            }
            case Board.X -> {
                int offsetX = (this.getWidth() - size) / 2;
                int offsetY = (this.getHeight() - size) / 2;
                g.setColor(Color.RED);
                g.setStroke(new BasicStroke(20));
                g.drawLine(offsetX, offsetY, size + offsetX, size + offsetY);
                g.drawLine(offsetX, size + offsetY, size + offsetX, offsetY);
            }
            case Board.TIE -> {
                // All the numbers in this are kinda finicky
                g.setColor(Color.YELLOW);
                int sizeH = this.getHeight() / 2 * 3;
                int sizeW = this.getWidth() / 13 * 7;
                g.setFont(new Font(Font.MONOSPACED, Font.BOLD, Math.min(sizeH, sizeW)));
                g.drawString("TIE", 0, Math.min(sizeH, sizeW) * 2 / 3);
            }
            default ->
                    // This method should not have been called.
                    throw new IllegalStateException("GraphicsHandler.endscreen should not be called if board.winner is not 1=X, 2=O, or 3=TIE.");
        }
    }
}