package sudoku;

import static sudoku.Interface.N;

import java.util.ArrayList;

public class SudokuSolver {

	private static int[][] values;	// board of values that are going to be solved
	private static boolean solved = false;
	
	// NOTE: I use x to denote the row and y to denote the column
	
	public static int[][] solve(int[][] values){
		SudokuSolver.solved = false;
		SudokuSolver.values = values;
		
		solve(0, 0);
		return SudokuSolver.values;
	}
	
	private static void solve(int x, int y){
		if (solved || isSolved()) return;
		
		if (values[x][y] != -1) {
			solve( getNextX(x, y), getNextY(x, y) );
			return;
		}
		
		for (int val : validNumbers(x, y)) {
			values[x][y] = val;
			solve( getNextX(x, y), getNextY(x, y) );
			
			if (solved) return;
		}
		
		values[x][y] = -1;
		return;
	}
	
	/**
	 * Returns true if board is
	 * full, assuming every value
	 * is valid. If true, sets
	 * solved flag to true
	 * 
	 * @return true if board is full, false otherwise
	 */
	private static boolean isSolved() {
		for (int i = 0; i < N*N; i++)
			for (int j = 0; j < N*N; j++)
				if (values[i][j] == -1) return false;
		
		return solved = true;	// returns true and sets solved to true
	}
	
	/**
	 * Returns array of valid numbers
	 * to place at cell x, y.
	 * NOTE: this function should only
	 * be called on empty cells
	 * 
	 * @param x row of the cell
	 * @param y column of the cell
	 * @return array of valid numbers to place at cell x, y
	 */
	private static ArrayList<Integer> validNumbers(int x, int y) {
		boolean[] lockedNumbers = new boolean[N*N + 1];	// Initialized as false
		ArrayList<Integer> validNums = new ArrayList<Integer>();
		
		// Adds cells in same row and column
		for (int i = 0; i < N*N; i++) {
			int colVal = values[x][i];
			int rowVal = values[i][y];
			
			if (colVal != -1 && colVal <= N*N) lockedNumbers[colVal] = true;
			if (rowVal != -1 && rowVal <= N*N) lockedNumbers[rowVal] = true;
		}
		
		// The following lines are quite ugly, but make it so that an arbitrary N is allowed
		// Adds all cells in the same N x N set
		Pair<Pair<Integer, Integer>, Pair<Integer, Integer>> extremes = determineExtremes(x, y);
		for (int i = extremes.getFirst().getFirst(); i < extremes.getSecond().getFirst(); i++)
			for (int j = extremes.getFirst().getSecond(); j < extremes.getSecond().getSecond(); j++)
				if (values[i][j] != -1 && values[i][j] <= N*N) lockedNumbers[ values[i][j] ] = true;
			
		// Adds valid numbers to returned array
		for (int i = 1; i <= N*N; i++)
			if (lockedNumbers[i] == false) validNums.add(i);
		
		return validNums;
	}
	
	private static int getNextX(int x, int y) {
		if (getNextY(x, y) == 0) return x + 1;		
		return x;
	}
	
	private static int getNextY(int x, int y) {
		return (y + 1)%(N*N);
	}
	
	/**
	 * Checks whether the cell at
	 * x, y is valid
	 * @param values
	 * @param x
	 * @param y
	 * @return
	 */
	public static boolean isValid(int[][] values, int x, int y) {
		int originalVal = values[x][y];	// Stores value of the desired cell
		values[x][y] = -1;				// Temporarily removes it
		SudokuSolver.values = values;
		
		ArrayList<Integer> validNums = validNumbers(x, y);
		values[x][y] = originalVal;		// Sets the original value back
		
		return validNums.contains(originalVal);
	}
	
	/**
	 * Determines extremes of
	 * the N x N set that the
	 * cell at (x, y) belongs
	 * to.
	 * @param x
	 * @param y
	 * @return top-left and bottom-right coordinates of the cell set
	 */
	private static Pair<Pair<Integer, Integer>, Pair<Integer, Integer>> determineExtremes(int x, int y){
		Pair<Integer, Integer> topLeft = new Pair<>((int)(x/N)*N, (int)(y/N)*N);
		Pair<Integer, Integer> bottomRight = new Pair<>(N + (int)(x/N)*N, N + (int)(y/N)*N);
		
		return new Pair<Pair<Integer, Integer>, Pair<Integer, Integer>>(topLeft, bottomRight);
	}
}
