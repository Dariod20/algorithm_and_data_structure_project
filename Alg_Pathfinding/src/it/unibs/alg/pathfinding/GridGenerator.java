package it.unibs.alg.pathfinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GridGenerator {
	
	private String[][] grid;
	private Map<String, Double> wG;
	
	/*
	 * n. rows (height) x n.columns (length)
	 */
	public GridGenerator(int h, int l, double obstacles_percentage, double agglomeration_factor) {
		grid = new String[h][l];
		wG = new HashMap<>();
		inizializeGridAndNooseNodes();
		insertObstacles(obstacles_percentage, agglomeration_factor);
		instantiatewG();
	}

	public String[][] getGrid() {
		return grid;
	}

	public Map<String, Double> getwG() {
		return wG;
	}
	
	/*
	 * Initialize grid with ordinal numbers and then add the noose nodes to the map
	 */
	private void inizializeGridAndNooseNodes() {
		Integer v = 1;
		for(int i=0; i < grid.length; i++) {
			for(int j=0; j < grid[0].length; j++) {
				String newValue = v.toString();
				grid[i][j] = newValue;
				wG.put(newValue + newValue, 1.0);
				v++;
			}
		}
	}
	
	private void insertObstacles(double pct, double agglFact) {
		int dim = grid.length * grid[0].length;
		int numObst = (int) ((int) dim * pct);
		ArrayList<int[]> existingObstPos = new ArrayList<>();
		
		int i = 0, j = 0;
		int[] obstPos = new int[2];
		if(numObst > 0) {
			i = (int) (grid.length * Math.random());
			j = (int) (grid[0].length * Math.random());
			wG.remove(grid[i][j] + grid[i][j]);
			grid[i][j] = "x";
			obstPos[0] = i;
			obstPos[1] = j;
			existingObstPos.add(obstPos);
		}
		int count = 2;
		
		while(count <= numObst) {
			if(Math.random() > agglFact) {
//				TODO
			} else {
				do {
					i = (int) (grid.length * Math.random());
					j = (int) (grid[0].length * Math.random());
				} while(grid[i][j].equals("x"));
			}
			
			wG.remove(grid[i][j] + grid[i][j]);
			grid[i][j] = "x";
			
			count++;
		}
	}
	
	
	/*
	 * Create the map with all arcs with their weights
	 */
	private void instantiatewG() {
		double square2 = Math.sqrt(2);
		
		for(int i=0; i < grid.length; i++) {
			for(int j=0; j < grid[0].length; j++) {
				if(isAnObstacle(i, j)) {
					continue;
				}
				String currentValue = grid[i][j];
				if(i-1 >= 0) {
					if(!isAnObstacle(i-1, j)) 
						wG.put(currentValue + grid[i-1][j], 1.0);
					if(j+1 < grid[0].length && !isAnObstacle(i-1, j+1))
						wG.put(currentValue + grid[i-1][j+1], square2);
					if(j-1 >= 0 && !isAnObstacle(i-1, j-1))
						wG.put(currentValue + grid[i-1][j-1], square2);
				}
				if(j+1 < grid[0].length) {
					if(!isAnObstacle(i, j+1))
						wG.put(currentValue + grid[i][j+1], 1.0);
					if(i+1 < grid.length && !isAnObstacle(i+1, j+1))
						wG.put(currentValue + grid[i+1][j+1], square2);
				}
				if(i+1 < grid.length) {
					if(!isAnObstacle(i+1, j))
						wG.put(currentValue + grid[i+1][j], 1.0);
					if(j-1 >= 0 && !isAnObstacle(i+1, j-1))
						wG.put(currentValue + grid[i+1][j-1], square2);
				}
				if(j-1 >= 0 && !isAnObstacle(i, j-1))
					wG.put(currentValue + grid[i][j-1], 1.0);
			}
		}
	}
	
	private boolean isAnObstacle(int i, int j) {
		if(grid[i][j].equals("x")) {
			return true;
		}
		return false;
	}
	
	
	
	
	
	
	
	
	
	
	
	

}
