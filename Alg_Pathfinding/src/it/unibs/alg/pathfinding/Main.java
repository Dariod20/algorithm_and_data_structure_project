package it.unibs.alg.pathfinding;

import java.util.Map;

public class Main {

	public static void main(String[] args) {
		
//		se pct ostacoli e fattAggl troppo alti, la griglia non si crea
		GridGenerator gridGenerator = new GridGenerator(10, 20, 0.8, 0);
		String[][] grid = gridGenerator.getGrid();
		Map<String, Double> weightedGraph = gridGenerator.getwG();
		
		for(int i=0; i < grid.length; i++) {
			for(int j=0; j < grid[0].length; j++) {
				if(grid[i][j].equals("x"))
					System.out.print(grid[i][j] + "   ");
				else if(Integer.parseInt(grid[i][j]) < 10) 
					System.out.print(grid[i][j] + "   ");
				else if(Integer.parseInt(grid[i][j]) < 100) 
					System.out.print(grid[i][j] + "  ");
				else
					System.out.print(grid[i][j] + " ");
			}
			System.out.println();
		}
		
		System.out.println();
		
		for(int i=0; i < grid.length; i++) {
			for(int j=0; j < grid[0].length; j++) {
				if(!grid[i][j].equals("x")) 
					System.out.print("0 ");
				else
					System.out.print("1 ");
			}
			System.out.println();
		}
		
		System.out.println();
		
		for(Map.Entry<String, Double> entry : weightedGraph.entrySet()) {
	            System.out.println("Key: " + entry.getKey() + ", Value: " + entry.getValue());
	    }

	}

}
