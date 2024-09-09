package it.unibs.alg.pathfinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class HeuristicAlgorithm {
	
	private int[][] grid;
	private int goal;
	private Map<Integer, Double> h = new HashMap<>();
	
	public HeuristicAlgorithm(int[][] grid, int goal) {
		this.grid = grid;
		this.goal = goal;
	}
	
	/*
	 * - Initialize h with zeros for each free cell
	 * - Compute the heuristic of the relaxed path
	 * - If for a cell the heuristic is still 0, it means that from that cell is impossible to reach the goal
	 */
	public void runHeuristic() {
		for(int i=0; i < grid.length; i++) {
			for(int j=0; j < grid[0].length; j++) {
				if(grid[i][j] != 0) {
					h.put(grid[i][j], 0.0);
				}
			}
		}
		computeHeuristic();
		for(Entry<Integer, Double> entry : h.entrySet()) {
			if(entry.getValue() == 0.0) {
				h.put(entry.getKey(), Double.MAX_VALUE);
			}
		}
		h.put(goal, 0.0);
	}
	
	/*
	 * Calculate the best path from each cell in the grid to reach the goal, 
	 * starting from the neighborhood of the goal
	 */
	private void computeHeuristic() {
	    List<Integer> listNextCells = new ArrayList<>();
	    listNextCells.add(goal);

	    while (!listNextCells.isEmpty()) {
	        int currentCell = listNextCells.get(0);
	        int[] neighborhood = getNeighborhood(currentCell);
	        double[] neighborhoodCost = getNeighborhoodCost(currentCell);

	        for (int i = 0; i < neighborhood.length; i++) {
	            int nextPossibleCell = currentCell + neighborhood[i];
	            int numCols = grid[0].length;
	            int nextRowIndex = 0;
	            int nextColIndex = 0;

	            if (nextPossibleCell % numCols != 0) {
	                nextRowIndex = nextPossibleCell / numCols;
	                nextColIndex = nextPossibleCell % numCols - 1;
	            } else {
	                nextRowIndex = nextPossibleCell / numCols - 1;
	                nextColIndex = numCols - 1;
	            }

	            if (grid[nextRowIndex][nextColIndex] != 0) {
	                double cost = h.get(currentCell) + neighborhoodCost[i];
	                double insertedCost = h.get(nextPossibleCell);

	                if(insertedCost == 0 || !listNextCells.contains(currentCell)) {
	                	listNextCells.add(nextPossibleCell);
	                    h.put(nextPossibleCell, cost);
	                }
	            }
	        }
	        listNextCells.remove(0);
	    }
	}

	/*
	 * Get the neighborhood of a cell, excluding the cell itself and checking its position
	 */
	private int[] getNeighborhood(int cell) {
		int numRows = grid.length;
		int numCols = grid[0].length;
		int rowIndex = 0;
		int colIndex = 0;
		if(cell % numCols != 0) {
			rowIndex = cell / numCols;
			colIndex = cell % numCols-1;
		} else {
			rowIndex = cell / grid[0].length-1;
			colIndex = grid[0].length-1;
		}
		if(rowIndex == 0 && colIndex == 0) {
			return new int[] {1, numCols, (numCols+1)};
			
		} else if(rowIndex == 0 && colIndex == (numCols-1)) {
			return new int[] {-1, numCols, (numCols-1)};
			
		} else if(rowIndex == (numRows-1) && colIndex == 0) {
			return new int[] {-numCols, 1, -(numCols-1)};
			
		} else if(rowIndex == (numRows-1) && colIndex == (numCols-1)) {
			return new int[] {-numCols, -1, -(numCols+1)};
			
		} else if(rowIndex == 0) {
			return new int[] {-1, 1, numCols, (numCols-1), (numCols+1)};
			
		} else if(rowIndex == (numRows-1)) {
			return new int[] {-1, 1, -numCols, -(numCols+1), -(numCols-1)};
			
		} else if(colIndex == 0) {
			return new int[] {1, -numCols, numCols, -(numCols-1), (numCols+1)};
			
		} else if(colIndex == (numCols-1)) {
			return new int[] {-1, -numCols, numCols, -(numCols+1), (numCols-1)};
			
		}
		return new int[] {-1, 1, -(numCols+1), -numCols, -(numCols-1), (numCols-1), numCols, (numCols+1)};
	}
	
	/*
	 * Get all costs of the neighborhood of a cell, excluding the cost of staying in cell itself 
	 * and checking its position
	 */
	private double[] getNeighborhoodCost(int cell) {
		int numRows = grid.length;
		int numCols = grid[0].length;
		int rowIndex = 0;
		int colIndex = 0;
		if(cell % numCols != 0) {
			rowIndex = cell / numCols;
			colIndex = cell % numCols-1;
		} else {
			rowIndex = cell / numCols-1;
			colIndex = numCols-1;
		}
		if(rowIndex == 0 && colIndex == 0) {
			return new double[] {Utility.COST, Utility.COST, Utility.SQRT_COST};
			
		} else if(rowIndex == 0 && colIndex == (numCols-1)) {
			return new double[] {Utility.COST, Utility.COST, Utility.SQRT_COST};
			
		} else if(rowIndex == (numRows-1) && colIndex == 0) {
			return new double[] {Utility.COST, Utility.COST, Utility.SQRT_COST};
			
		} else if(rowIndex == (numRows-1) && colIndex == (numCols-1)) {
			return new double[] {Utility.COST, Utility.COST, Utility.SQRT_COST};
			
		} else if(rowIndex == 0) {
			return new double[] {Utility.COST, Utility.COST, Utility.COST, Utility.SQRT_COST, Utility.SQRT_COST};
			
		} else if(rowIndex == (numRows-1)) {
			return new double[] {Utility.COST, Utility.COST, Utility.COST, Utility.SQRT_COST, Utility.SQRT_COST};
			
		} else if(colIndex == 0) {
			return new double[] {Utility.COST, Utility.COST, Utility.COST, Utility.SQRT_COST, Utility.SQRT_COST};
			
		} else if(colIndex == (numCols-1)) {
			return new double[] {Utility.COST, Utility.COST, Utility.COST, Utility.SQRT_COST, Utility.SQRT_COST};
			
		}
		return new double[] {Utility.COST, Utility.COST, Utility.SQRT_COST, Utility.COST, Utility.SQRT_COST, Utility.SQRT_COST, Utility.COST, Utility.SQRT_COST};
	}
	
	
	public Map<Integer, Double> getH() {
		return h;
	}

}
