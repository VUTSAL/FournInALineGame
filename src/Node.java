public class Node {
	
	public Node(int[][] init, int blanks) { // Constructor
		this.blanks = blanks;
		board = init;
	}

	public int[][] board; //The board
	public int blanks; // Available spaces

	public boolean move(String input, boolean player) { // Player input and agent input
		int a = input.charAt(0) - 97;
		if (a < 0)
			a = input.charAt(0) - 65;
		int b = input.charAt(1) - 49;
		return move(a, b, player);
	}

	public boolean move(int a, int b, boolean player) { //Test the edge cases
		if (a > board.length || b > board.length || a < 0 || b < 0
				|| board[a][b] != 0)
			return false;
		--blanks;
		if (player)
			board[a][b] = -1;
		else
			board[a][b] = 1;
		return true;
	}
	public boolean undo(int a, int b) { //Undoes a move
		if (board[a][b] == 0)
			return false;
		else {
			++blanks;
			board[a][b] = 0;
			return true;
		}
	}
	
	private boolean checkRow(int i, int j, int type) { //Checks rows for four in a line
		for (int n = j; n < j + 4; ++n)
			if (board[i][n] != type)
				return false;
		return true;
	}

	private boolean checkClm(int i, int j, int type) { // Checks columns for four in a line
		for (int n = i; n < i + 4; ++n)
			if (board[n][j] != type)
				return false;
		return true;
	}

	private boolean checkForFour(int i, int j, int type) { // Checks for four in a line 
		if (j + 4 <= board.length && checkRow(i, j, type))
			return true;
		if (i + 4 <= board.length && checkClm(i, j, type))
			return true;
		return false;
	}

	public int checkWin() { // Checks if either the player or the agent has won
		for (int i = 0; i < board.length; i++)
			for (int j = 0; j < board[i].length; j++)
				if (board[i][j] == 1 || board[i][j] == -1)
					if (checkForFour(i, j, board[i][j]))
						return board[i][j];
		return 0;
	}

	public void printState() { //Prints the given state
		System.out.println("  1 2 3 4 5 6 7 8");
		char c = 'a';
		for (int i = 0; i < board.length; i++) {
			System.out.print("" + (c++) + " ");
			for (int j = 0; j < board.length; j++)
				if (board[i][j] == 0)
					System.out.print("- ");
				else if (board[i][j] == 1)
					System.out.print("X ");
				else
					System.out.print("O ");
			System.out.println("");
		}
	}
}
