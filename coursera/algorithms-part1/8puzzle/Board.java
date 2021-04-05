import edu.princeton.cs.algs4.Stack;

public class Board {
    private final int[][] tiles;
    private final int n;
    
    public Board(int[][] tiles) {
        if (tiles == null)
            throw new IllegalArgumentException("Array cannot be null!");
        n = tiles.length;
        this.tiles = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                this.tiles[i][j] = tiles[i][j];
            }
        }
    }
    
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(n).append("\n");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                s.append(String.format("%2d ", tiles[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }
    
    public int dimension() {
        return n;
    }
    
    public int hamming() {
        int count = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int x = tiles[i][j];
                if (x == 0)
                    continue;
                if (x != (i * n + j + 1))
                    count++;
            }
        }
        return count;
    }
    
    public int manhattan() {
        int total = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int x = tiles[i][j] - 1;
                if (x == -1)
                    continue;
                int dist = Math.abs((x / n) - i);
                dist += Math.abs((x % n) - j);
                total += dist;
            }
        }
        return total;
    }
    
    public boolean isGoal() {
        boolean goal = true;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i == n - 1 && j == n - 1) {
                    if (tiles[i][j] != 0)
                        goal = false;
                } else {
                    if (tiles[i][j] != (i * n + j + 1))
                        goal = false;
                }
            }
        }
        return goal;
    }
    
    public boolean equals(Object y) {
        if (y == null)
            return false;
        if (y.getClass() != this.getClass())
            return false;
        if (this == y)
            return true;
        String ys = y.toString();
        String ts = this.toString();
        return ts.equals(ys);
    }
    
    public Iterable<Board> neighbors() {
        Stack<Board> stack = new Stack<>();
        int x = -1, y = -1;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (tiles[i][j] == 0) {
                    x = i;
                    y = j;
                }
            }
        }
        if (y > 0) {
            int[][] ltile = copyTiles();
            ltile[x][y] = ltile[x][y - 1];
            ltile[x][y - 1] = 0;
            stack.push(new Board(ltile));
        }
        if (y < n - 1) {
            int[][] rtile = copyTiles();
            rtile[x][y] = rtile[x][y + 1];
            rtile[x][y + 1] = 0;
            stack.push(new Board(rtile));
        }
        if (x > 0) {
            int[][] utile = copyTiles();
            utile[x][y] = utile[x - 1][y];
            utile[x - 1][y] = 0;
            stack.push(new Board(utile));
        }
        if (x < n - 1) {
            int[][] dtile = copyTiles();
            dtile[x][y] = dtile[x + 1][y];
            dtile[x + 1][y] = 0;
            stack.push(new Board(dtile));
        }
        return stack;
    }
    
    public Board twin() {
        int[][] ct = copyTiles();
        int x = -1, y = -1;
        int x1 = -1, y1 = -1;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (tiles[i][j] == 0)
                    continue;
                if (x > -1 && x1 > -1)
                    break;
                else if (x > -1) {
                    x1 = i;
                    y1 = j;
                } else {
                    x = i;
                    y = j;
                }
            }
        }
        int temp = ct[x][y];
        ct[x][y] = ct[x1][y1];
        ct[x1][y1] = temp;
        return new Board(ct);
    }
    
    private int[][] copyTiles() {
        int[][] ct = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                ct[i][j] = tiles[i][j];
            }
        }
        return ct;
    }
    
    public static void main(String[] args) {
        int[][] tiles = {{1, 2}, {3, 0}};
        Board b = new Board(tiles);
        System.out.println(b.toString());
        System.out.println(b.isGoal());
        System.out.println(b.hamming());
        System.out.println(b.manhattan());
        System.out.println(b.equals(b));
        System.out.println(b.twin().toString());
        for (Board n : b.neighbors()) {
            System.out.println(n.toString());
        }
        
    }
}
