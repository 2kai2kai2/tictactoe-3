package tickTackToe;

public class Main {
	public static int[][][] board = new int[9][9][9];
	// board[out][mid][in]
	
	public static final int BLANK = 0;
	public static final int X = 1;
	public static final int O = 2;
	
	public static void main(String[] args) {
		board[1][2][3] = 1;
		for (int[][] i : board) {
			for(int[] j : i) {
				for(int k : j) {
					System.out.print(k);
				}
				System.out.print(" ");
			}
			System.out.println();
		}
	}
	
	public static void printBoard() {
		
	}
	
	public static int get(int outer, int middle, int inner) {
		return 0;
	}
	
	public static int get(int[][] outer, int[][] middle, int[][] inner) {
		return 0;
	}
}
