public class ABSearch {
	
	private final long limit;
	private final int depth;
	private boolean first;
	
	public ABSearch(int lim, boolean b) { //Construct
		this.limit = (long) (lim * 1000);
		this.depth = lim*100;
		this.first = b;
	}
	
	private int alpha;
	private int beta;
	private long startTime;
	private static final boolean Blocker = false;
	protected int a;
	protected int b;

	public Actions absearch(Node node) { // alpha beta search with depth limit
		return Search(node, depth);
	}

	public Actions Search(Node node, int d) { // Perform search for best possible action using Alpha Beta pruning and iterative deepening 
		int score;
		int mi = 0;
		int mj = 0;
		alpha = Integer.MIN_VALUE; //Value evaluated by min function
		beta = Integer.MAX_VALUE; // Value evaluated by max function
		int best = alpha; // Set best available value to Alpha initially

		startTime = System.currentTimeMillis(); // Start timer
		for (int i = 0; i < node.board.length; i++) {
			for (int j = 0; j < node.board.length; j++) {
				if (node.board[i][j] == 0) {
					node.move(i, j, false);
					score = Min(node, d - 1);
					node.undo(i, j);
					if (score > best) {
						mi = i;
						mj = j;
						best = score;
					}
				}
			}
		}
		return new Actions(mi, mj);
	}
	private int evalScore(Node s) { // Evaluate score of the board at a state s
		int score = 0;
		int x = 0;
		int o = 0;
		if (!Blocker) {
			for (int i = 0; i < s.board.length; i++)
				for (int j = 0; j < s.board.length; j++)
					score += evalScore(s, i, j);
			return score;
		}
		for (int i = 0; i < s.board.length; ++i)
			for (int j = 0; j < s.board.length - 3; ++j) {
				for (int k = 0; k < 4; ++k) {
					if (s.board[i][j + k] == 1)
						x++;
					if (s.board[i][j + k] == -1)
						o++;
				}
				score += x * x;
				score -= o * o;
				x = 0;
				o = 0;
				for (int k = 0; k < 4; ++k) {
					if (s.board[j + k][i] == 1)
						x++;
					if (s.board[j + k][i] == -1)
						o++;
				}
				score += x * x;
				score -= o * o;
				x = 0;
				o = 0;
			}
		return score;
	}
	private int evalScore(Node s, int i, int j) { //Score evaluation function for each block on the board
		int score = 0;
		int tmp = 0;
		int chk = s.board[i][j];

		if (!first && chk > 0) {
			chk = -1;
			s.board[i][j] = -1;
			if (s.checkWin() == -1) {
				score += 10000;
			}
			s.board[i][j] = 1;
		}
		
		if (!first) // When not first, makes the agent think it's first and make relevant moves to block
			chk = -1;
		
		if (i >= 3) {
			for (int c = 1; c < 4; c++)
				if (s.board[i - c][j] == chk)
					tmp += 5 - c;
				else if (s.board[i - c][j] != 0) {
					tmp = -1;
					c = 10;
				}
			if (i < s.board.length - 1)//Checks for XX and XXX format with blanks in the adjacent blocks
				if (tmp == 7 && s.board[i + 1][j] == 0)
					tmp = 10000;
			if (!first && tmp == 9)
				tmp = 9990;
			score += tmp;
			tmp = 0;
		} else
			score--;
		
		if (i < s.board.length - 3) {
			for (int c = 1; c < 4; c++)
				if (s.board[i + c][j] == chk)
					tmp += 5 - c;
				else if (s.board[i + c][j] != 0) {
					tmp = -1;
					c = 10;
				}
			if (i > 0)//Checks for XX and XXX format with blanks in the adjacent blocks
				if (tmp == 7 && s.board[i - 1][j] == 0)
					tmp = 10000;
			if (!first && tmp == 9)
				tmp = 9990;
			score += tmp;
			tmp = 0;
		} else
			score--;

		if (j >= 3) {
			for (int c = 1; c < 4; c++)
				if (s.board[i][j - c] == chk)
					tmp += 5 - c;
				else if (s.board[i][j - c] != 0) {
					tmp = -1;
					c = 10;
				}
			if (j < s.board.length - 1)//Checks for XX and XXX format with blanks in the adjacent blocks
				if (tmp == 7 && s.board[i][j + 1] == 0)
					tmp = 10000;
			if (!first && tmp == 9)
				tmp = 9990;
			score += tmp;
			tmp = 0;
		} else
			score--;

		if (j < s.board.length - 3) {
			for (int c = 1; c < 4; c++)
				if (s.board[i][j + c] == chk)
					tmp += 5 - c;
				else if (s.board[i][j + c] != 0) {
					tmp = -1;
					c = 10;
				}
			if (j > 0) //Checks for XX and XXX format with blanks in the adjacent blocks
				if (tmp == 7 && s.board[i][j - 1] == 0)
					tmp = 10000;
			if (!first && tmp == 9)
				tmp = 9990;
			score += tmp;
			tmp = 0;
		} else
			score--;
		if (!first)// Checks diagonals
			if (i >= 1 && i < s.board.length - 1 && j >= 1
					&& j < s.board.length - 1) {
				if (s.board[i + 1][j + 1] == chk
						|| s.board[i + 1][j - 1] == chk
						|| s.board[i - 1][j + 1] == chk
						|| s.board[i - 1][j - 1] == chk)
					score++;
			}

		if (!first)
			chk = s.board[i][j];
		return score * chk;
	}
	private boolean stopThinking() {// stops generating nodes and chooses best possible action
		if (System.currentTimeMillis() - startTime > limit)
			return true;
		return false;
	}
	
	private int Max(Node node, int d) {
		if (node.checkWin() == 1)
			return Integer.MAX_VALUE / 2;
		if (node.checkWin() == -1)
			return Integer.MIN_VALUE / 2;
		if (node.blanks == 0)
			return 0;
		if (stopThinking() || d <= 0)
			return evalScore(node);
		
		int best = alpha;
		for (int i = 0; i < node.board.length; i++) {
			for (int j = 0; j < node.board.length; j++) {
				if (node.board[i][j] == 0) {
					node.move(i, j, true);
					best = Integer.max(best, Min(node, d - 1));
					node.undo(i, j);
				}
			}
		}
		return best;
	}

	private int Min(Node node, int d) {
		if (node.checkWin() == 1)
			return Integer.MAX_VALUE / 2;
		if (node.checkWin() == -1)
			return Integer.MIN_VALUE / 2;
		if (node.blanks == 0)
			return 0;
		if (stopThinking() || d <= 0)
			return evalScore(node);
		
		int best = beta;
		for (int i = 0; i < node.board.length; i++) {
			for (int j = 0; j < node.board.length; j++) {
				if (node.board[i][j] == 0) {
					node.move(i, j, false);
					best = Integer.min(best, Max(node, d - 1));
					node.undo(i, j);
				}
			}
		}
		return best;
	}
}