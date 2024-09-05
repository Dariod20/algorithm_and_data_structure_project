package it.unibs.alg.pathfinding;

import java.util.ArrayList;
import java.util.List;

public class GridGenerator {
	
	private static final int AGGL_FACTOR_LIMIT = 10;
	private int[][] grid;
	private int numObst;
	private List<Integer> existingAgentsInit = new ArrayList<>();
	private List<Integer> existingAgentsGoal = new ArrayList<>();;
	
	/*
	 * n. rows (height) x n.columns (length)
	 */
	public GridGenerator(int h, int l, double obstacles_percentage, double agglomeration_factor) {
		grid = new int[h][l];
		numObst = (int) ((int) (grid.length * grid[0].length) * obstacles_percentage);
		
		inizializeGrid();
		insertObstacles(agglomeration_factor);
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
	 * Insert the obstacles in grid based on their percentage and based on the fact
	 * if the probability of agglomerates is high or not
	 */
	private void insertObstacles(double agglFact) {
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
			if(Math.random() < agglFact) {
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
	 * Insert a new obstacle based on the agglomeration factor and to make sure to not
	 * overlap the new obstacles with an existing one
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
		} while(count < AGGL_FACTOR_LIMIT);
		return null;
	}
	
	public int getRandomInit() {
		int i = (int) (grid.length * Math.random());
		int j = (int) (grid[0].length * Math.random());
		while(grid[i][j] == 0 || existingAgentsInit.contains(grid[i][j])) {
			i = (int) (grid.length * Math.random());
			j = (int) (grid[0].length * Math.random());
		}
		
		existingAgentsInit.add(grid[i][j]);
		return grid[i][j];
	}
	
	public int getRandomGoal(int currentInit) {
		int i = (int) (grid.length * Math.random());
		int j = (int) (grid[0].length * Math.random());
		while(grid[i][j] == 0 || existingAgentsGoal.contains(grid[i][j]) || grid[i][j] == currentInit ) {
			i = (int) (grid.length * Math.random());
			j = (int) (grid[0].length * Math.random());
		}
		
		existingAgentsGoal.add(grid[i][j]);
		return grid[i][j];
		}
	
	public int getMax() {
		if((grid.length * grid[0].length) < 2500 ) {
			return (grid.length * grid[0].length) - numObst;
		}
		return Math.round((grid.length * grid[0].length - numObst) / 3);
	}
	

}
