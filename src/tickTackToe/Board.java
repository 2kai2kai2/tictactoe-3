package tickTackToe;

public class Board {

	public final int BLANK = 0;
	public final int X = 1;
	public final int O = 2;
	public final int TIE = 3;

	private int[][][] board = new int[9][9][9]; // board[out][mid][in]

	public int winner = 0;

	private int[][] wonMiddles = new int[9][9];
	private int[] wonOuters = new int[9];

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
	 * Places a marker on the board at any location.
	 * 
	 * @param locs An array of {outer, middle, inner} locations. Each 0-8.
	 */
	public void place(int[] locs) {
		if (locs.length == 3) {
			if (locs[0] < 9 && locs[1] < 9 && locs[2] < 9) {
				place(locs[0], locs[1], locs[2]);
			}
		}
	}

	/**
	 * Places a marker on the board at the next location in the board.
	 * 
	 * @param locInner The integer location (0-8) within the current middle square.
	 */
	public void place(int locInner) {
		place(lastMiddle(), lastInner(), locInner);
	}

	/**
	 * Evaluates the wins on every level for the last move.
	 */
	public void checkWins() {
		if (wonMiddles[lastOuter()][lastMiddle()] == BLANK) {
			wonMiddles[lastOuter()][lastMiddle()] = win(board[lastOuter()][lastMiddle()]);
		}

		if (wonOuters[lastOuter()] == BLANK) {
			wonOuters[lastOuter()] = win(wonMiddles[lastOuter()]);
		}

		if (win(wonOuters) != 0) {
			Display.running = false;
			winner = win(wonOuters);
		}
	}

	/**
	 * Evaluates an int array [9] (one box) to find the winner of the box.
	 * 
	 * @param locs The int array to be evaluated.
	 * @return 0=none, 1=X, 2=O, 3=tie of the winner of the array as a box.
	 */
	public int win(int[] locs) {
		for (int i = 0; i < 3; i++) {
			// Each Horizontal
			if (locs[3 * i] == locs[1 + 3 * i] && locs[1 + 3 * i] == locs[2 + 3 * i]) {
				if (locs[3 * i] != BLANK) {
					return locs[3 * i];
				}
				// Each Vertical
			} else if (locs[i] == locs[i + 3] && locs[i + 3] == locs[i + 6]) {
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
	 */
	public int getWinnerMiddle(int out, int mid) {
		return wonMiddles[out][mid];
	}

	/**
	 * Gets the winner of an outer-level box.
	 * 
	 * @param out The outer box (0-8) to get the winner of.
	 * @return The int value of the winner of the selected box.
	 */
	public int getWinnerOuter(int out) {
		return wonOuters[out];
	}

	/**
	 * Gets the next player for a turn and switches to the other.
	 * 
	 * @return The next player turn int value.
	 */
	public int nextPlayer() {
		int a = lastPlayer;
		if (lastPlayer == X) {
			lastPlayer = O;
		} else if (lastPlayer == O) {
			lastPlayer = X;
		}
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
	 *         middle box..
	 */
	public int lastInner() {
		return lastMove[2];
	}

	/**
	 * Setter for the last move.
	 * 
	 * @param move The int[] {outer, middle, inner} locations to set the last move.
	 */
	public void setLastMove(int[] move) {
		if (move.length == 3) {
			if (move[0] < 9 && move[1] < 9 && move[2] < 9) {
				lastMove = move;
			}
		}
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
	 * Sets all values on the board back to blank (0).
	 */
	public void reset() {
		for (int[][] i : board) {
			for (int[] j : i) {
				for (int k : j) {
					k = BLANK;
				}
			}
		}
	}

	/**
	 * Sets the value of a spot on the board.
	 * 
	 * @param out The area of the outermost board
	 * @param mid The area on the middle-size board within out
	 * @param in  The spot on the smallest board within mid
	 */
	public void set(int out, int mid, int in, int team) {
		board[out][mid][in] = team;
	}

	/**
	 * Sets the value of a spot on the board.
	 * 
	 * @param out The x, y values for the location of the middle board from the top
	 *            left of the outermost board
	 * @param mid The x, y values for the location of the smallest board from the
	 *            top left of the middle board
	 * @param in  The x, y values for the spot on the smallest board from the top
	 *            left
	 */
	public void set(int[] out, int[] mid, int[] in, int team) {
		// If any of the x, y pairs are larger than the allowed side, do nothing.
		if (out.length == 2 && mid.length == 2 && in.length == 2) {
			// If it is not an x, y pair, just do nothing
			if (out[0] > 2 || out[1] > 2 || mid[0] > 2 || mid[1] > 2 || in[0] > 2 || in[1] > 2) {
				set(out[0] + 3 * out[1], mid[0] + 3 * mid[1], in[0] + 3 * in[1], team);
			}
		}
	}

	/**
	 * Gets the value of a spot on the board.
	 * 
	 * @param out The index locating the outer box (0-8)
	 * @param mid The index locating the middle box (0-8)
	 * @param in  The index locating the inner box (0-8)
	 * @return The int value of the team that has the box.
	 */
	public int get(int out, int mid, int in) {
		if (out < 9 && mid < 9 && in < 9) {
			return board[out][mid][in];
		}
		return -1;
	}

	/**
	 * Gets the grid X index of the given array location.
	 * 
	 * @param out The index of the outer box.
	 * @param mid The index of the middle box in the outer box.
	 * @param in  The index of the inner box in the middle box.
	 * @return The corresponding grid X index.
	 */
	public static int displayBoardX(int out, int mid, int in) {
		if (out < 9 && mid < 9 && in < 9) {
			return out % 3 * 9 + mid % 3 * 3 + in % 3;
		}
		return -1;
	}

	/**
	 * Gets the grid Y index of the given array location.
	 * 
	 * @param out The index of the outer box.
	 * @param mid The index of the middle box in the outer box.
	 * @param in  The index of the inner box in the middle box.
	 * @return The corresponding grid Y index.
	 */
	public static int displayBoardY(int out, int mid, int in) {
		if (out < 9 && mid < 9 && in < 9) {
			return out / 3 * 9 + mid / 3 * 3 + in / 3;
		}
		return -1;
	}

	/**
	 * Gets the outer box array location from the grid indexes
	 * 
	 * @param x The X grid index (0-26).
	 * @param y The Y grid index (0-26).
	 * @return The outer box array location (0-8).
	 */
	public static int numBoardOuter(int x, int y) {
		if (x < 27 && y < 27) {
			return x / 9 + y / 9 * 3;
		}
		return -1;
	}

	/**
	 * Gets the middle box array location from the grid indexes
	 * 
	 * @param x The X grid index (0-26).
	 * @param y The Y grid index (0-26).
	 * @return The middle box array location (0-8).
	 */
	public static int numBoardMiddle(int x, int y) {
		if (x < 27 && y < 27) {
			return x % 9 / 3 + y % 9 / 3 * 3;
		}
		return -1;
	}

	/**
	 * Gets the inner box array location from the grid indexes
	 * 
	 * @param x The X grid index (0-26).
	 * @param y The Y grid index (0-26).
	 * @return The inner box array location (0-8).
	 */
	public static int numBoardInner(int x, int y) {
		if (x < 27 && y < 27) {
			return x % 3 + y % 3 * 3;
		}
		return -1;
	}

	/**
	 * Gets the String representation of a team.
	 * 
	 * @param value The int value of the team.
	 * @return The String (length 1) value of the team.
	 */
	public String symbolOf(int value) {
		if (value == X) {
			return "X";
		} else if (value == O) {
			return "O";
		} else if (value == BLANK) {
			return "-";
		} else {
			return "?";
		}
	}

	/**
	 * Creates a text-based representation of the board.
	 * 
	 * @deprecated
	 * @return A multiline string containing a text-graphic of the board.
	 */
	public String out() {
		String out = "=========================================================================\n";
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
			out += rows[i] + "\n";
			if (i % 9 == 8) {
				out += "=========================================================================\n";
			} else if (i % 3 == 2) {
				out += "-------------------------------------------------------------------------\n";
			}
		}
		return out;
	}
}