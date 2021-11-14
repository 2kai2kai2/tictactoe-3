package tictactoe;

public class Board {

	public static final int BLANK = 0;
	public static final int X = 1;
	public static final int O = 2;
	public static final int TIE = 3;

	private final int[][][] board = new int[9][9][9]; // board[out][mid][in]

	private final int[][] wonMiddles = new int[9][9];
	private final int[] wonOuters = new int[9];
	public int winner = 0;

	private int[] lastMove = new int[] { -1, -1, -1 };
	// [0] = outermost grid, [1] = middle grid, and [2] = inner grid locations

	private int lastPlayer = 2; // Start with x

	/**
	 * Places a marker on the board at any location.
	 * 
	 * @param locOuter  The outer location (0-8)
	 * @param locMiddle The middle location (0-8) within the outer
	 * @param locInner  The inner location (0-8) within the middle
	 */
	public void place(int locOuter, int locMiddle, int locInner) {
		set(locOuter, locMiddle, locInner, nextPlayer());
		setLastMove(locOuter, locMiddle, locInner);
		checkWins();
		if (wonMiddles[locMiddle][locInner] != 0 || wonOuters[locMiddle] != 0) {
			setLastMove(-1, -1, -1);
		}
	}

	/**
	 * Evaluates the wins on every level for the last move.
	 */
	public void checkWins() {
		if (wonMiddles[lastOuter()][lastMiddle()] == BLANK) {
			wonMiddles[lastOuter()][lastMiddle()] = evaluateWinner(board[lastOuter()][lastMiddle()]);
		}

		if (wonOuters[lastOuter()] == BLANK) {
			wonOuters[lastOuter()] = evaluateWinner(wonMiddles[lastOuter()]);
		}

		if (evaluateWinner(wonOuters) != 0) {
			Display.running = false;
			winner = evaluateWinner(wonOuters);
		}
	}

	/**
	 * Evaluates an int array [9] (one box) to find the winner of the box.
	 * 
	 * @param locs The int array to be evaluated.
	 * @return 0=none, 1=X, 2=O, 3=tie of the winner of the array as a box.
	 */
	public int evaluateWinner(int[] locs) {
		for (int i = 0; i < 3; i++) {
			// Each Horizontal
			if (locs[3 * i] == locs[1 + 3 * i] && locs[1 + 3 * i] == locs[2 + 3 * i]) {
				if (locs[3 * i] != BLANK) {
					return locs[3 * i];
				}
			}
			// Each Vertical
			if (locs[i] == locs[i + 3] && locs[i + 3] == locs[i + 6]) {
				if (locs[i] != BLANK) {
					return locs[i];
				}
			}
		}
		// Each diagonal
		if ((locs[0] == locs[4] && locs[4] == locs[8]) || (locs[2] == locs[4] && locs[4] == locs[6])) {
			if (locs[4] != BLANK) {
				return locs[4];
			}
		}

		// Check if any spaces are open (no tie)
		for (int i : locs) {
			if (i == BLANK) {
				return BLANK;
			}
		}
		// If there are no more empty spots and no winner
		return TIE;
	}

	/**
	 * Gets the winner of a middle-level box.
	 * 
	 * @param out The outer box (0-8) containing the middle box.
	 * @param mid The middle box (0-8) within the selected outer box.
	 * @return The int value of the winner of the selected middle level box.
	 *
	 * @throws ArrayIndexOutOfBoundsException If out or mid are invalid indexes (0-8).
	 */
	public int getWinnerMiddle(int out, int mid) {
		return wonMiddles[out][mid];
	}

	/**
	 * Gets the winner of an outer-level box.
	 * 
	 * @param out The outer box (0-8) to get the winner of.
	 * @return The int value of the winner of the selected box.
	 *
	 * @throws ArrayIndexOutOfBoundsException If out or mid are invalid indexes (0-8).
	 */
	public int getWinnerOuter(int out) {
		return wonOuters[out];
	}

	/**
	 * Gets the next player for a turn and switches to the other.
	 * 
	 * @return The next player turn int value.
	 *
	 * @throws IllegalStateException If the previous value of lastPlayer was invalid.
	 */
	public int nextPlayer() {
		int a = lastPlayer;
		lastPlayer = switch (lastPlayer) {
			case X -> O;
			case O -> X;
			default -> throw new IllegalStateException("Cannot switch lastPlayer because the current value is invalid.");
		};
		return a;
	}

	/**
	 * Getter for the player int value.
	 * 
	 * @return The current player int value.
	 */
	public int currentPlayer() {
		return lastPlayer;
	}

	/**
	 * Getter for the last outer box move array location.
	 * 
	 * @return The array location of the last outer box placed in.
	 */
	public int lastOuter() {
		return lastMove[0];
	}

	/**
	 * Getter for the last middle box move array location.
	 * 
	 * @return The array location of the last middle box placed in within the last
	 *         outer box.
	 */
	public int lastMiddle() {
		return lastMove[1];
	}

	/**
	 * Getter for the last inner box move array location.
	 * 
	 * @return The array location of the last inner box placed in within the last
	 *         middle box.
	 */
	public int lastInner() {
		return lastMove[2];
	}

	/**
	 * Setter for the last move.
	 * 
	 * @param move The int[] {outer, middle, inner} locations to set the last move.
	 *
	 * @throws AssertionError If move is not three ints between 0 and 8.
	 */
	public void setLastMove(int[] move) {
		assert(move.length == 3);
		assert(0 <= move[0] && move[0] < 9);
		assert(0 <= move[1] && move[1] < 9);
		assert(0 <= move[2] && move[2] < 9);
		lastMove = move;
	}

	/**
	 * Setter for the last move.
	 * 
	 * @param outer  The outer box array location of the last move.
	 * @param middle The middle box array location of the last move.
	 * @param inner  The inner box array location of the last move.
	 */
	public void setLastMove(int outer, int middle, int inner) {
		setLastMove(new int[] { outer, middle, inner });
	}

	/**
	 * Sets the value of a spot on the board.
	 * 
	 * @param out The area of the outermost board
	 * @param mid The area on the middle-size board within out
	 * @param in  The spot on the smallest board within mid
	 * @param team The team (final int X or O) to give the tile to.
	 */
	public void set(int out, int mid, int in, int team) {
		board[out][mid][in] = team;
	}

	/**
	 * Gets the value of a spot on the board.
	 * 
	 * @param out The index locating the outer box (0-8)
	 * @param mid The index locating the middle box (0-8)
	 * @param in  The index locating the inner box (0-8)
	 * @return The int value of the team that has the box.
	 *
	 * @throws AssertionError If the board location is invalid.
	 */
	public int get(int out, int mid, int in) {
		assert(0 <= out && out < 9);
		assert(0 <= mid && mid < 9);
		assert(0 <= in && in < 9);
		return board[out][mid][in];
	}

	/**
	 * Gets the grid X index of the given array location.
	 * 
	 * @param out The index of the outer box.
	 * @param mid The index of the middle box in the outer box.
	 * @param in  The index of the inner box in the middle box.
	 * @return The corresponding grid X index.
	 *
	 * @throws AssertionError If the board location is invalid.
	 */
	public static int displayBoardX(int out, int mid, int in) {
		assert(0 <= out && out < 9);
		assert(0 <= mid && mid < 9);
		assert(0 <= in && in < 9);
		return out % 3 * 9 + mid % 3 * 3 + in % 3;
	}

	/**
	 * Gets the grid Y index of the given array location.
	 * 
	 * @param out The index of the outer box.
	 * @param mid The index of the middle box in the outer box.
	 * @param in  The index of the inner box in the middle box.
	 * @return The corresponding grid Y index.
	 *
	 * @throws AssertionError If the board location is invalid.
	 */
	public static int displayBoardY(int out, int mid, int in) {
		assert(0 <= out && out < 9);
		assert(0 <= mid && mid < 9);
		assert(0 <= in && in < 9);
		return (out / 3) * 9 + (mid / 3) * 3 + in / 3;
	}

	/**
	 * Gets the outer box array location from the grid indexes
	 * 
	 * @param x The X grid index (0-26).
	 * @param y The Y grid index (0-26).
	 * @return The outer box array location (0-8).
	 *
	 * @throws AssertionError If either grid index is invalid.
	 */
	public static int numBoardOuter(int x, int y) {
		assert(0 <= x && x < 27);
		assert(0 <= y && y < 27);
		return x / 9 + (y / 9) * 3;
	}

	/**
	 * Gets the middle box array location from the grid indexes
	 * 
	 * @param x The X grid index (0-26).
	 * @param y The Y grid index (0-26).
	 * @return The middle box array location (0-8).
	 *
	 * @throws AssertionError If either grid index is invalid.
	 */
	public static int numBoardMiddle(int x, int y) {
		assert(0 <= x && x < 27);
		assert(0 <= y && y < 27);
		return ((x % 9) / 3) + ((y % 9) / 3) * 3;
	}

	/**
	 * Gets the inner box array location from the grid indexes
	 * 
	 * @param x The X grid index (0-26).
	 * @param y The Y grid index (0-26).
	 * @return The inner box array location (0-8).
	 *
	 * @throws AssertionError If either grid index is invalid.
	 */
	public static int numBoardInner(int x, int y) {
		assert(0 <= x && x < 27);
		assert(0 <= y && y < 27);
		return x % 3 + y % 3 * 3;
	}

	/**
	 * Gets the String representation of a team.
	 * 
	 * @param value The int value of the team.
	 * @return The String (length 1) value of the team.
	 *
	 * @throws IllegalStateException If the value is not 0=BLANK, 1=X, or 2=O.
	 */
	public String symbolOf(int value) {
		return switch (value) {
			case X -> "X";
			case O -> "O";
			case BLANK -> "-";
			default -> throw new IllegalStateException("Team " + value + " is invalid (must be 0=BLANK, 1=X, 2=O).");
		};
	}

	/**
	 * Creates a text-based representation of the board.
	 * 
	 * @deprecated
	 * @return A multiline string containing a text-graphic of the board.
	 */
	@Deprecated
	public String out() {
		StringBuilder out = new StringBuilder("=========================================================================\n");
		String[] rows = new String[3 * 3 * 3];
		for (int i = 0; i < 3 * 3 * 3; i++) {
			rows[i] = "[ ";
			for (int j = 0; j < 3 * 3 * 3; j++) {
				rows[i] += symbolOf(get(numBoardOuter(j, i), numBoardMiddle(j, i), numBoardInner(j, i)));
				if (j % 3 == 2 && numBoardOuter(j, i) == lastMiddle() && numBoardMiddle(j, i) == lastInner()) {
					rows[i] += " <";
				} else if (j % 9 == 8) {
					rows[i] += " [";
				} else if (j % 3 == 2) {
					rows[i] += " |";
				}
				rows[i] += " ";
			}
		}
		for (int i = 0; i < 3 * 3 * 3; i++) {
			out.append(rows[i]).append("\n");
			if (i % 9 == 8) {
				out.append("=========================================================================\n");
			} else if (i % 3 == 2) {
				out.append("-------------------------------------------------------------------------\n");
			}
		}
		return out.toString();
	}
}