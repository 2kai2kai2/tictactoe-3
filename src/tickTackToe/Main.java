package tickTackToe;

import java.util.Scanner;

public class Main {
	public static Board board;
	public static Scanner s;
	
	public static void main(String[] args) {
		board = new Board();
		s = new Scanner(System.in);
		boolean running = true;
		System.out.println(board.out());
		board.place(inputGlobalCoords());
		while (running) {
			System.out.println(board.out());
			board.place(inputLocalCoords());
		}
	}
	
	public static int[] inputGlobalCoords() {
		System.out.println("o, m, i");
		String input = s.nextLine().trim();
		String out = input.substring(0, input.indexOf(",")).trim();
		String mid = input.substring(input.indexOf(",") + 2, input.indexOf(",", input.indexOf(",") + 1)).trim();
		String in = input.substring(input.indexOf(",", input.indexOf(",") + 1) + 2).trim();
		return new int[] {Integer.parseInt(out), Integer.parseInt(mid), Integer.parseInt(in)};
	}
	public static int inputLocalCoords() {
		System.out.println("i");
		return Integer.parseInt(s.nextLine().trim());
	}
}
