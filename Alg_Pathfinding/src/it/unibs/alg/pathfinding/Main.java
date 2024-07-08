package it.unibs.alg.pathfinding;

import java.util.Map;

public class Main {

	public static void main(String[] args) {
		
		GridGenerator gridGenerator = new GridGenerator(2, 3);
		String[][] grid = gridGenerator.getGrid();
		Map<String, Double> weightedGraph = gridGenerator.getwG();
		
		
//		for(int i=0; i < grid.length; i++) {
//			for(int j=0; j < grid[0].length; j++) {
//				System.out.print("0 ");
//			}
//			System.out.println();
//		}
		
		for(int i=0; i < grid.length; i++) {
			for(int j=0; j < grid[0].length; j++) {
				System.out.print(grid[i][j] + " ");
			}
			System.out.println();
		}
		
		System.out.println();
		
		for(Map.Entry<String, Double> entry : weightedGraph.entrySet()) {
	            System.out.println("Key: " + entry.getKey() + ", Value: " + entry.getValue());
	    }

	}

}
