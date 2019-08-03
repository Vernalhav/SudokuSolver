package sudoku;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * A GUI that lets you insert a sudoku board
 * and solves it for you. It does so using
 * backtracking - NOT graph colouring :/ - so
 * it is pretty slow for N > 4 (more than 10
 * minutes). If we ever solve graph colouring,
 * then it will become fast for most Ns with
 * a different implementation.
 * 
 * @author Victor Giovannoni
 *
 */

public class Interface {
	
	public static final int N = 3;

	private static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	
	private static final int SCR_WIDTH = (int) (screenSize.getWidth()*900/1920);
	private static final int SCR_HEIGHT = SCR_WIDTH + 100;

	JFrame mainFrame;
	JTextField[][] cells;
	JButton confirm;
	
	JPanel[][] threeByThrees;
	JPanel mainGrid;

	private boolean[][] isInvalid = new boolean[N*N][N*N];
	
	public Interface() {
		mainFrame = new JFrame("Sudoku Solver");
		
		mainFrame.setSize(SCR_WIDTH, SCR_HEIGHT);
		mainFrame.setLocationRelativeTo(null);
		mainFrame.setResizable(false);	
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel mainPanel = new JPanel(new BorderLayout());
		mainFrame.add(mainPanel);
		
		confirm = new JButton("Solve!");
		confirm.setHorizontalAlignment(SwingConstants.CENTER);

		confirm.setActionCommand("Solve");
		confirm.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (arg0.getActionCommand() == "Solve") handleSolvePress();
				else handleNewGamePress();
			}
			
		});
		
		setupMainGrid();
		mainPanel.add(mainGrid, BorderLayout.CENTER);
		mainPanel.add(confirm, BorderLayout.SOUTH);
		
		mainFrame.setVisible(true);
	}
	
	private void handleSolvePress() {
		for (int i = 0; i < N*N; i++)
			for (int j = 0; j < N*N; j++)
				if (cells[i][j].getText().length() != 0) cells[i][j].setForeground(Color.RED);
		
		int[][] answer = SudokuSolver.solve( getValueMatrix() );
		
		for (int i = 0; i < N*N; i++)
			for (int j = 0; j < N*N; j++)
				cells[i][j].setText("" + answer[i][j]);
		
		confirm.setText("Continue");
		confirm.setActionCommand("Continue");
	}
	
	private void handleNewGamePress() {
		for (int i = 0; i < N*N; i++) {
			for (int j = 0; j < N*N; j++) {
				cells[i][j].setText("");
				cells[i][j].setForeground(Color.BLACK);
			}			
		}
		
		confirm.setText("Solve!");
		confirm.setActionCommand("Solve");
	}
	
	private void setupMainGrid() {
		threeByThrees = new JPanel[N][N];
		cells = new JTextField[N*N][N*N];
		
		mainGrid = new JPanel(new GridLayout(N, N, 0, 0));
		
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				threeByThrees[i][j] = new JPanel(new GridLayout(N, N, 0, 0));
				setUpSingleCellSet(i, j);
			}
		}
	}

	private void setUpSingleCellSet(int x, int y) {
		JPanel current = threeByThrees[x][y];
		
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				JTextField cell = createCell(N*x + i, N*y + j);
				
				current.add(cell);
				cells[N*x + i][N*y + j] = cell;
			}
		}
		
		current.setBorder(BorderFactory.createMatteBorder(4, 4, 4, 4, Color.BLACK));
		mainGrid.add(current);
	}
	
	private JTextField createCell(int x, int y) {
		JTextField cell = new JTextField();
		
		cell.setHorizontalAlignment(SwingConstants.CENTER);
		Font f = new Font("SansSerif", Font.BOLD, SCR_WIDTH/(2*N*N));
		cell.setFont(f);

		cell.getDocument().putProperty("coords", new int[] {x, y});
		
		cell.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void changedUpdate(DocumentEvent e) {
				
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				int[] coords = (int[]) e.getDocument().getProperty("coords");
				isInvalid[coords[0]][coords[1]] = !isValid(cell.getText());
				confirm.setEnabled(allCellsValid());	// Only enables solve button if all cells are OK
			
				updateFontSize(cell, cell.getText());
			}
			
			@Override
			public void removeUpdate(DocumentEvent e) {
				int[] coords = (int[]) e.getDocument().getProperty("coords");
				isInvalid[coords[0]][coords[1]] = !isValid(cell.getText());
				confirm.setEnabled(allCellsValid());	// Only enables solve button if all cells are OK				
			
				updateFontSize(cell, cell.getText());
			}
			
		});	
		
		return cell;
	}
	
	private void updateFontSize(JTextField cell, String s) {
		int len = s.length();
		if (len == 0) return;
		
		Font f = cell.getFont().deriveFont((float) SCR_WIDTH/(N*N*(len + 1)));
		cell.setFont(f);
	}
	
	private boolean allCellsValid() {
		for (int i = 0; i < N*N; i++) {
			for (int j = 0; j < N*N; j++) {
				if (isInvalid[i][j]) return false;
			}
		}
		return true;
	}
	
	private boolean isValid(String s) {
		if ("".equals(s)) return true;
		
		try {
			int n = Integer.parseInt(s);
			return 0 < n && n <= N*N;
			
		} catch(NumberFormatException e) {
			return false;
		}
	}
	
	private int[][] getValueMatrix(){
		int[][] matrix = new int[N*N][N*N];
		
		for (int i = 0; i < N*N; i++) {
			for (int j = 0; j < N*N; j++) {
				// matrix[i][j] = value or -1 if empty
				matrix[i][j] = "".equals(cells[i][j].getText()) ? -1 : Integer.parseInt(cells[i][j].getText());
			}
		}
		
		return matrix;
	}
	
	
	public static void main(String[] args) {
		new Interface();
	}
	
}
