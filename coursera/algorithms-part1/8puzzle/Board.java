import edu.princeton.cs.algs4.Stack;

public class Board {
    private static final int[] dx = {-1, 0, 1, 0};
    private static final int[] dy = { 0, 1, 0,-1};

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

    @Override
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
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i == n - 1 && j == n - 1) {
                    return (tiles[i][j] == 0);
                } else if (tiles[i][j] != (i * n + j + 1)) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean equals(Object y) {
        if (y == null) return false;
        if (!y.getClass().equals(this.getClass()))
            return false;
        if (this == y) return true;

        Board that = (Board) y;
        if (this.n != that.n) return false;
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                if (this.tiles[i][j] != that.tiles[i][j])
                    return false;

        return true;
    }

    public Iterable<Board> neighbors() {
        Stack<Board> stack = new Stack<>();
        int x = -1, y = -1;
        for (int i = 0; i < n && x == -1; i++) {
            for (int j = 0; j < n && x == -1; j++) {
                if (tiles[i][j] == 0) {
                    x = i;
                    y = j;
                }
            }
        }

        for (int k = 0; k < dx.length; k++) {
            int i = x + dx[k];
            int j = y + dy[k];
            if (i < 0 || i >= n || j < 0 || j >= n)
                continue;
            int[][] copy = copyTiles();
            copy[x][y] = copy[i][j];
            copy[i][j] = 0;
            stack.push(new Board(copy));
        }

        return stack;
    }

    public Board twin() {
        int[][] copy = copyTiles();

        int one = copy[0][0];
        int two = copy[0][1];
        int three = copy[1][0];

        if (one == 0) {
            copy[1][0] = two;
            copy[0][1] = three;
        } else if (two == 0) {
            copy[0][0] = three;
            copy[1][0] = one;
        } else {
            copy[0][0] = two;
            copy[0][1] = one;
        }

        return new Board(copy);
    }

    private int[][] copyTiles() {
        int[][] copy = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                copy[i][j] = tiles[i][j];
            }
        }
        return copy;
    }

    public static void main(String[] args) {
        int[][] tiles = {{1, 2}, {3, 0}};
        Board b = new Board(tiles);
        System.out.println(b);
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
