import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Solver {
    
    private Stack<Board> sol = null;
    private int moves = -1;
    
    private static class SearchNode implements Comparable<SearchNode> {
        int moves;
        Board board;
        SearchNode prev;
        int priority;
        int priority1;
        
        SearchNode(Board board, int moves, SearchNode prev) {
            this.moves = moves;
            this.board = board;
            this.prev = prev;
            this.priority = board.manhattan() + moves;
            this.priority1 = board.hamming() + moves;
        }
        
        @Override
        public int compareTo(SearchNode that) {
            return Integer.compare(this.priority, that.priority);
        }
    }
    
    public Solver(Board initial) {
        if (initial == null)
            throw new IllegalArgumentException("Argument to Solver1() is null!");

        Board twin = initial.twin();
        MinPQ<SearchNode> pq = new MinPQ<>();
        MinPQ<SearchNode> twinPQ = new MinPQ<>();
        SearchNode start = new SearchNode(initial, 0, null);
        SearchNode twinStart = new SearchNode(twin, 0, null);
        pq.insert(start);
        twinPQ.insert(twinStart);
        while (!pq.isEmpty()) {
            SearchNode current = pq.delMin();

            if (current.board.isGoal()) {
                moves = current.moves;
                sol = new Stack<>();
                while (current != null) {
                    sol.push(current.board);
                    current = current.prev;
                }
                break;
            } else {
                for (Board neighbor : current.board.neighbors()) {
                    if (current.prev == null || !neighbor.equals(current.prev.board)) {
                        pq.insert(new SearchNode(neighbor, current.moves + 1, current));
                    }
                }
            }

            current = twinPQ.delMin();

            if (current.board.isGoal()) {
                break;
            }
            else {
                for (Board neighbor : current.board.neighbors()) {
                    if (current.prev == null || !neighbor.equals(current.prev.board)) {
                        twinPQ.insert(new SearchNode(neighbor, current.moves + 1, current));
                    }
                }
            }
        }
    }
    
    public boolean isSolvable() {
        return sol != null;
    }
    
    public int moves() {
        return moves;
    }
    
    public Iterable<Board> solution() {
        return sol;
    }
    
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);
        
        // solve the puzzle
        Solver solver = new Solver(initial);
        
        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}
