package tickTackToe;

public class Main {
	
	
	public static void main(String[] args) {
		Board board = new Board();
		board.set(2, 1, 0, board.O);
		board.set(7, 4, 5, board.X);
		System.out.println(board.out());
	}
}
