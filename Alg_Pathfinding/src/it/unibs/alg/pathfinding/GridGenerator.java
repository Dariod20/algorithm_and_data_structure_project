package it.unibs.alg.pathfinding;

import java.util.HashMap;
import java.util.Map;

public class GridGenerator {
	
	private String[][] grid;
	private Map<String, Double> wG;
	
	/*
	 * n. rows (height) x n.columns (length)
	 */
	public GridGenerator(int h, int l) {
		grid = new String[h][l];
		wG = new HashMap<>();
		inizializeGridAndNooseNodes();
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
	
	/*
	 * Create the map with all arcs with their weights
	 */
	private void instantiatewG() {
		double square2 = Math.sqrt(2);
		
		for(int i=0; i < grid.length; i++) {
			for(int j=0; j < grid[0].length; j++) {
				String currentValue = grid[i][j];
				if(i-1 >= 0) {
					wG.put(currentValue + grid[i-1][j], 1.0);
					if(j+1 < grid[0].length)
						wG.put(currentValue + grid[i-1][j+1], square2);
					if(j-1 >= 0)
						wG.put(currentValue + grid[i-1][j-1], square2);
				}
				if(j+1 < grid[0].length) {
					wG.put(currentValue + grid[i][j+1], 1.0);
					if(i+1 < grid.length)
						wG.put(currentValue + grid[i+1][j+1], square2);
				}
				if(i+1 < grid.length) {
					wG.put(currentValue + grid[i+1][j], 1.0);
					if(j-1 >= 0)
						wG.put(currentValue + grid[i+1][j-1], square2);
				}
				if(j-1 >= 0)
					wG.put(currentValue + grid[i][j-1], 1.0);
			}
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
