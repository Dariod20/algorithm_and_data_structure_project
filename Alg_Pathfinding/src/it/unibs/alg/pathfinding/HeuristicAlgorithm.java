package it.unibs.alg.pathfinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Stack;

public class HeuristicAlgorithm {
	
	private int[][] grid;
	private int goal;
	
	private Map<Integer, Double> h = new HashMap<>();
	
	public HeuristicAlgorithm(int[][] grid, int goal) {
		this.grid = grid;
		this.goal = goal;
	}
	
	public void runHeuristic() {
		
		for(int i=0; i < grid.length; i++) {
			for(int j=0; j < grid[0].length; j++) {
				if(grid[i][j] != 0) {
					h.put(grid[i][j], 0.0);
				}
			}
		}
		
		computeHeuristic(goal);
		
		for(Entry<Integer, Double> entry : h.entrySet()) {
			if(entry.getValue() == 0.0) {
				h.put(entry.getKey(), Double.MAX_VALUE);
			}
		}
		h.put(goal, 0.0);
	}
	
//	private void computeHeuristic(int cell) {
//	    Stack<Integer> stackNextCells = new Stack<>();
//	    stackNextCells.push(cell);
//
//	    while (!stackNextCells.isEmpty()) {
//	        int currentCell = stackNextCells.pop();
//	        int[] neighborhood = getNeighborhood(currentCell);
//	        double[] neighborhoodCost = getNeighborhoodCost(currentCell);
//
//	        for (int i = 0; i < neighborhood.length; i++) {
//	            int nextPossibleCell = currentCell + neighborhood[i];
//	            int numCols = grid[0].length;
//	            int nextRowIndex = 0;
//	            int nextColIndex = 0;
//
//	            if (nextPossibleCell % numCols != 0) {
//	                nextRowIndex = nextPossibleCell / numCols;
//	                nextColIndex = nextPossibleCell % numCols - 1;
//	            } else {
//	                nextRowIndex = nextPossibleCell / numCols - 1;
//	                nextColIndex = numCols - 1;
//	            }
//
//	            if (grid[nextRowIndex][nextColIndex] != 0) {
//	                double cost = h.get(currentCell) + neighborhoodCost[i];
//	                double insertedCost = h.get(nextPossibleCell);
//
//	                if (insertedCost == 0 || insertedCost > cost) {
//	                	stackNextCells.push(nextPossibleCell);
//	                    h.put(nextPossibleCell, cost);
//	                }
//	            }
//	        }
//	    }
//	}
	
	private void computeHeuristic(int cell) {
	    List<Integer> listNextCells = new ArrayList<>();
	    listNextCells.add(cell);

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

	
	private int[] getNeighborhood(int cell) {
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
			
		} else if(rowIndex == (numCols-1) && colIndex == 0) {
			return new int[] {-numCols, 1, -(numCols+1)};
			
		} else if(rowIndex == (numCols-1) && colIndex == (numCols-1)) {
			return new int[] {-numCols, -1, -(numCols+1)};
			
		} else if(rowIndex == 0) {
			return new int[] {-1, 1, numCols, (numCols-1), (numCols+1)};
			
		} else if(rowIndex == (numCols-1)) {
			return new int[] {-1, 1, -numCols, -(numCols+1), -(numCols-1)};
			
		} else if(colIndex == 0) {
			return new int[] {1, -numCols, numCols, -(numCols-1), (numCols+1)};
			
		} else if(colIndex == (numCols-1)) {
			return new int[] {-1, -numCols, numCols, -(numCols+1), (numCols-1)};
			
		}
		return new int[] {-1, 1, -(numCols+1), -numCols, -(numCols-1), (numCols-1), numCols, (numCols+1)};
	}
	
	private double[] getNeighborhoodCost(int cell) {
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
			return new double[] {1, 1, Math.sqrt(2)};
			
		} else if(rowIndex == 0 && colIndex == (numCols-1)) {
			return new double[] {1, 1, Math.sqrt(2)};
			
		} else if(rowIndex == (numCols-1) && colIndex == 0) {
			return new double[] {1, 1, Math.sqrt(2)};
			
		} else if(rowIndex == (numCols-1) && colIndex == (numCols-1)) {
			return new double[] {1, 1, Math.sqrt(2)};
			
		} else if(rowIndex == 0) {
			return new double[] {1, 1, 1, Math.sqrt(2), Math.sqrt(2)};
			
		} else if(rowIndex == (numCols-1)) {
			return new double[] {1, 1, 1, Math.sqrt(2), Math.sqrt(2)};
			
		} else if(colIndex == 0) {
			return new double[] {1, 1, 1, Math.sqrt(2), Math.sqrt(2)};
			
		} else if(colIndex == (numCols-1)) {
			return new double[] {1, 1, 1, Math.sqrt(2), Math.sqrt(2)};
			
		}
		
		return new double[] {1, 1, Math.sqrt(2), 1, Math.sqrt(2), Math.sqrt(2), 1, Math.sqrt(2)};
	}
	
	public Map<Integer, Double> getH() {
		return h;
	}

}
