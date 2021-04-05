
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private static final int MIN_INDEX = 1;
    private final boolean[][] grid;
    private final int n;
    private int count;
    private final WeightedQuickUnionUF connections;
    private final WeightedQuickUnionUF con;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("Size of grid must be greater than 0");
        }
        this.n = n;
        grid = new boolean[n][n];
        connections = new WeightedQuickUnionUF(n*n + 2);
        con = new WeightedQuickUnionUF(n*n + 1);
        for (int i = 0; i < n; i ++) {
            for(int j = 0; j < n; j++) {
                grid[i][j] = false;
            }
        }
    }

    private void validate(int row , int col) {
        if ((row < MIN_INDEX|| row > n) || (col < MIN_INDEX || col > n)) {
            throw new IllegalArgumentException("Row or Column out of bounds");
        }
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        validate(row , col);
        if (isOpen(row , col)) {
            return;
        }
        row--;
        col--;
        grid[row][col] = true;
        count++;
        if (row - 1 >= 0 && grid[row-1][col]) {
            connections.union((row - 1) * n + col, row * n + col);
            con.union((row - 1) * n + col, row * n + col);
        }
        if (row + 1 < n && grid[row+1][col]) {
            connections.union((row + 1) * n + col, row * n + col);
            con.union((row + 1) * n + col, row * n + col);
        }
        if (col - 1 >= 0 && grid[row][col-1]) {
            connections.union((row) * n + col - 1, row * n + col);
            con.union((row) * n + col - 1, row * n + col);
        }
        if (col + 1 < n && grid[row][col+1]) {
            connections.union((row) * n + col + 1, row * n + col);
            con.union((row) * n + col + 1, row * n + col);
        }
        if (row == 0) {
            connections.union(n * n, row * n + col);
            con.union(n * n, row * n + col);
        }
        if(row == n-1) {
            connections.union(n * n + 1, row * n + col);
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        validate(row , col);
        return grid[row-1][col-1];
    }

    // is the site (row, col) full?
    public boolean isFull (int row, int col) {
        validate(row , col);
        if(!isOpen(row , col))
            return false;
        row--;
        col--;
        return con.find(row * n + col) == con.find(n*n);
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return count;
    }

    // does the system percolate?
    public boolean percolates() {
        return connections.find(n*n) == connections.find(n*n + 1);
    }
}
