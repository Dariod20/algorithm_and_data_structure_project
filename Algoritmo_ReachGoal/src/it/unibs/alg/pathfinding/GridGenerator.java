package it.unibs.alg.pathfinding;

import java.util.ArrayList;
import java.util.List;

public class GridGenerator {
	
	private static final int BREAK_LIMIT = 10;
	private int[][] grid;
	private double pct_obst;
	private double aggl_fact;
	private int initRowIndex = 0;
	private int initColIndex = 0;
	private List<Integer> existingAgentsInit = new ArrayList<>();
	private List<Integer> existingAgentsGoal = new ArrayList<>();;
	
	/*
	 * n. rows (height) x n.columns (length)
	 */
	public GridGenerator(int height, int length, double pct_obst, double aggl_fact) {
		grid = new int[height][length];
		this.pct_obst = pct_obst;
		this.aggl_fact = aggl_fact;
		
		inizializeGrid();
	}

	public int[][] getGrid() {
		return grid;
	}
	
	/*
	 * Initialize grid with ordinal numbers
	 */
	private void inizializeGrid() {
		int value = 1;
		for(int i=0; i < grid.length; i++) {
			for(int j=0; j < grid[0].length; j++) {
				grid[i][j] = value;
				value++;
			}
		}
	}
	
	/*
	 * If the entry init or goal is an obstacle, then assign it the init or goal value
	 * and insert a new random obstacle
	 */
	public void checkEntryInitGoalNotObstacles(int entryInit, int entryGoal) {
		int numCols = grid[0].length;
		int rowIndex = 0;
		int colIndex = 0;
		
		if(entryInit % numCols != 0) {
			rowIndex = entryInit / numCols;
			colIndex = entryInit % numCols-1;
		} else {
			rowIndex = entryInit / numCols-1;
			colIndex = numCols-1;
		}
		if(grid[rowIndex][colIndex] == 0) {
			grid[rowIndex][colIndex] = entryInit;
			int [] rndObst = new int[2];
			do {
				rndObst = getRandomObstPos();
			} while(rowIndex == rndObst[0] && colIndex == rndObst[1]);
		}
		existingAgentsInit.add(entryInit);
		
		if(entryGoal % numCols != 0) {
			rowIndex = entryGoal / numCols;
			colIndex = entryGoal % numCols-1;
		} else {
			rowIndex = entryGoal / numCols-1;
			colIndex = numCols-1;
		}
		if(grid[rowIndex][colIndex] == 0) {
			grid[rowIndex][colIndex] = entryGoal;
			int [] rndObst = new int[2];
			do {
				rndObst = getRandomObstPos();
			} while(rowIndex == rndObst[0] && colIndex == rndObst[1]);
		}
		existingAgentsGoal.add(entryGoal);
	}
	
	/*
	 * Insert obstacles in the positions decided by the user in the 'Input.txt' file
	 */
	public int insertManuallyObstacles(List<String> inputs, int start) {
		int i = start;
		int numCols = grid[0].length;
		int rowIndex = 0;
		int colIndex = 0;
		String data = inputs.get(i++);
		while(!data.equals("true")) {
			int cell = 0;
			try {
				cell = Integer.parseInt(data);
			} catch(NumberFormatException e) {
				System.out.println("\nATTENZIONE: la posizione dell'ostacolo non è un intero.\n ");
				System.exit(1);
			}
			if(cell % numCols != 0) {
				rowIndex = cell / numCols;
				colIndex = cell % numCols-1;
			} else {
				rowIndex = cell / numCols-1;
				colIndex = numCols-1;
			}
			grid[rowIndex][colIndex] = 0;
			data = inputs.get(i++);
		}
		return i++;
	}
	
	/*
	 * Insert the required number of obstacles in the grid in a random way.
	 * Group them based on the agglomeration factor percentage
	 */
	public void insertRandomObstacles() {
		int numObst = (int) Math.round((grid.length * grid[0].length) * pct_obst);
		ArrayList<int[]> existingObstPos = new ArrayList<>();
		int i = 0, j = 0;
		int[] obstPos = new int[2];
		if(numObst > 0) {
			i = (int) (grid.length * Math.random());
			j = (int) (grid[0].length * Math.random());
			grid[i][j] = 0;
			obstPos[0] = i;
			obstPos[1] = j;
			existingObstPos.add(obstPos);
		}
		int count = 2;
		
		while(count <= numObst) {
			if(Math.random() < aggl_fact) {
				obstPos = getNewObstPos(existingObstPos);
				if(obstPos != null) {
					i = obstPos[0];
					j = obstPos[1];
					grid[i][j] = 0;
				} else {
					obstPos = getRandomObstPos();
				}
				existingObstPos.add(obstPos);
			} else {
				obstPos = getRandomObstPos();
				existingObstPos.add(obstPos);
			}
			
			count++;
		}
	}
	
	/*
	 * Get randomly the position of an obstacle
	 */
	private int[] getRandomObstPos() {
		int[] obstPos = new int[2];
		int i = 0;
		int j = 0;
		do {
			i = (int) (grid.length * Math.random());
			j = (int) (grid[0].length * Math.random());
		} while(grid[i][j] == 0);

		obstPos[0] = i;
		obstPos[1] = j;
		grid[i][j] = 0;
		
		return obstPos;
	}
	
	/*
	 * Insert a new obstacle near to an existing one in horizontal or vertical direction
	 */
	private int[] getNewObstPos(ArrayList<int[]> existingObstPos) {
		int count = 0;
		int[] obst = new int[2];
		do {
			int elem = (int) (existingObstPos.size() * Math.random());
			obst = existingObstPos.get(elem);
			double r = Math.random();
			if(r < 0.25 && obst[0]-1 >= 0 && grid[obst[0]-1][obst[1]] != 0) {
				obst[0] = obst[0]-1;
				return obst;
			}
			if(r < 0.5 && obst[0]+1 < grid.length && grid[obst[0]+1][obst[1]] != 0) {
				obst[0] = obst[0]+1;
				return obst;
			}
			if(r < 0.75 && obst[1]-1 >= 0 && grid[obst[0]][obst[1]-1] != 0) {
				obst[1] = obst[1]-1;
				return obst;
			}
			if(obst[1]+1 < grid[0].length && grid[obst[0]][obst[1]+1] != 0) {
				obst[1] = obst[1]+1;
				return obst;
			}
			count++;
		} while(count < BREAK_LIMIT);
		return null;
	}
	
	/*
	 * Get randomly the cardinal number that identify the starting cell of an agent
	 */
	public int getRandomInit() {
		int i = (int) (grid.length * Math.random());
		int j = (int) (grid[0].length * Math.random());
		while(grid[i][j] == 0 || existingAgentsInit.contains(grid[i][j])) {
			i = (int) (grid.length * Math.random());
			j = (int) (grid[0].length * Math.random());
		}
		
		existingAgentsInit.add(grid[i][j]);
		initRowIndex = i;
		initColIndex = j;
		return grid[i][j];
	}
	
	/*
	 * Get randomly the cardinal number that identify the goal cell of an agent
	 */
	public int getRandomGoal(int minPathLength) {
		int i = (int) (grid.length * Math.random());
		int j = (int) (grid[0].length * Math.random());
		while(grid[i][j] == 0 || existingAgentsGoal.contains(grid[i][j]) || 
				(i == initRowIndex && j == initColIndex) || 
				(Math.abs(initRowIndex-i) < minPathLength && Math.abs(initColIndex-j) < minPathLength) ) {
			i = (int) (grid.length * Math.random());
			j = (int) (grid[0].length * Math.random());
		}
		
		existingAgentsGoal.add(grid[i][j]);
		return grid[i][j];
	}
	
}
