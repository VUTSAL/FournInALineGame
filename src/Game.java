import java.util.Scanner;

public class Game {
	
	private static Scanner sc = new Scanner(System.in);//scanner for our inputs
	final static int N = 8; // Size of the Board

	public static void main(String[] args) throws InterruptedException { // Main start - Creates a new board and time limit and who goes first is set
		
		String input;
		ABSearch ai;
		Actions a;
		boolean player;
		Node node = new Node(new int[N][N], N * N);

		System.out.println("State the time limit for the agent to think?");
		int limit = sc.nextInt();
		sc.nextLine();

		System.out.println("Are you going first?(Y/N)");
		if (sc.nextLine().toUpperCase().charAt(0)!= 'Y')
			player = false;
		else 
			player = true;
		node.printState();

		ai = new ABSearch(limit, !player);

		while (node.blanks > 0) {
			if (player) {//If it's player's move, it takes the move as input and checks if it is a valid move
				System.out.print("Next move: ");
				input = sc.nextLine();
				while (!node.move(input, player)) {
					System.out.print("Invalid input, please try again: ");
					input = sc.nextLine();
				}
			} else { //When it's the programs move, it makes the best move using alpha beta search 
				a = ai.absearch(node);
				node.move(a.a, a.b, player);
			}
			if (player)
				player = false;
			else
				player = true;
			node.printState();
			if (node.checkWin() != 0)
				break;
		}
		switch (node.checkWin()) { // Checks if any of these cases are true and break 
		case 0:
			System.out.println("It's a DRAW");
			break;
		case 1:
			System.out.println("Agent Has Won");
			break;
		case -1:
			System.out.println("You Have Won");
		}

	}
}
