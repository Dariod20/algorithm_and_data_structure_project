package it.unibs.alg.pathfinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GridGenerator {
	
	private int[][] grid;
	private Map<String, Double> wG;
	private int numObst;
	
	/*
	 * n. rows (height) x n.columns (length)
	 */
	public GridGenerator(int h, int l, double obstacles_percentage, double agglomeration_factor) {
		grid = new int[h][l];
		wG = new HashMap<>();
		numObst = 21; //(int) ((int) (grid.length * grid[0].length) * obstacles_percentage);
		
		inizializeGridAndNooseNodes();
//		insertObstacles(agglomeration_factor);
//		instantiatewG();
	}

	public int[][] getGrid() {
		return grid;
	}

	public Map<String, Double> getwG() {
		return wG;
	}
	
	/*
	 * Initialize grid with ordinal numbers and then add the noose nodes to the map
	 */
	private void inizializeGridAndNooseNodes() {
		int value = 1;
		for(int i=0; i < grid.length; i++) {
			for(int j=0; j < grid[0].length; j++) {
				grid[i][j] = value;
				wG.put(value + "_" + value, 1.0);
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
			wG.remove(grid[i][j] + "_" + grid[i][j]);
			grid[i][j] = 0;
			obstPos[0] = i;
			obstPos[1] = j;
			existingObstPos.add(obstPos);
		}
		int count = 2;
		
		while(count <= numObst) {
			if(Math.random() < agglFact) {
				int [] newObst = getNewObstPos(existingObstPos);
				i = newObst[0];
				j = newObst[1];
				obstPos = newObst;
			} else {
				do {
					i = (int) (grid.length * Math.random());
					j = (int) (grid[0].length * Math.random());
					obstPos[0] = i;
					obstPos[1] = j;
				} while(grid[i][j] == 0);
			}
			
			wG.remove(grid[i][j] + "_" + grid[i][j]);
			grid[i][j] = 0;
			existingObstPos.add(obstPos);
			
			count++;
		}
	}
	
	/*
	 * Insert a new obstacle based on the agglomeration factor and to make sure to not
	 * overlap the new obstacles with an existing one
	 */
	private int[] getNewObstPos(ArrayList<int[]> existingObstPos) {
		int elem = (int) (existingObstPos.size() * Math.random());
		int[] obst = existingObstPos.get(elem);
		do {
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
			
		} while(true);
	}
	
	/*
	 * Create the map with all arcs with their weights
	 */
//	/*private*/public void instantiatewG() {
//		double square2 = Math.sqrt(2);
//		
//		for(int i=0; i < grid.length; i++) {
//			for(int j=0; j < grid[0].length; j++) {
//				if(isAnObstacle(i, j)) {
//					continue;
//				}
//				int currentValue = grid[i][j];
//				if(i-1 >= 0) {
//					if(!isAnObstacle(i-1, j)) 
//						wG.put(currentValue + "_" + grid[i-1][j], 1.0);
//					if(j+1 < grid[0].length && !isAnObstacle(i-1, j+1))
//						wG.put(currentValue + "_" + grid[i-1][j+1], square2);
//					if(j-1 >= 0 && !isAnObstacle(i-1, j-1))
//						wG.put(currentValue + "_" + grid[i-1][j-1], square2);
//				}
//				if(j+1 < grid[0].length) {
//					if(!isAnObstacle(i, j+1))
//						wG.put(currentValue + "_" + grid[i][j+1], 1.0);
//					if(i+1 < grid.length && !isAnObstacle(i+1, j+1))
//						wG.put(currentValue + "_" + grid[i+1][j+1], square2);
//				}
//				if(i+1 < grid.length) {
//					if(!isAnObstacle(i+1, j))
//						wG.put(currentValue + "_" + grid[i+1][j], 1.0);
//					if(j-1 >= 0 && !isAnObstacle(i+1, j-1))
//						wG.put(currentValue + "_" + grid[i+1][j-1], square2);
//				}
//				if(j-1 >= 0 && !isAnObstacle(i, j-1))
//					wG.put(currentValue + "_" + grid[i][j-1], 1.0);
//			}
//		}
//	}
//	
//	private boolean isAnObstacle(int i, int j) {
//		if(grid[i][j] == 0) {
//			return true;
//		}
//		return false;
//	}
	
	public int getMax() {
		return Math.round((grid.length * grid[0].length - numObst) / 3);
	}
	

}
