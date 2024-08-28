package it.unibs.alg.pathfinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Main {

	public static void main(String[] args) {
		
		long start = System.currentTimeMillis();
		
//		se pct ostacoli e fattAggl troppo alti, la griglia non si crea
		
		GridGenerator gridGenerator = new GridGenerator(5, 5, 0.2, 0);
		String[][] grid = gridGenerator.getGrid();
		
//		genero griglia in modo manuale
		
		grid[1][3] = grid[2][1] = grid[2][2] = grid[2][3] = "x";
		gridGenerator.instantiatewG();
		
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
		
		System.out.println();
		System.out.println();
		System.out.println();
		
		
		List<String> allStates = new ArrayList<>();
		
		for(int i=0; i < grid.length; i++) {
			for(int j=0; j < grid[0].length; j++) {
				if(!grid[i][j].equals("x")) {
					allStates.add(grid[i][j]);
				}
			}
		}
		
		HeuristicAlgorithm heuritic = new HeuristicAlgorithm(allStates, weightedGraph, grid[3][2]);
		Map<Integer, Double> h = heuritic.getH();
		
		for(Entry<Integer, Double> entry : h.entrySet()) {
            System.out.println("State: " + entry.getKey() + ", Cost: " + entry.getValue());
		}
		
		long end = System.currentTimeMillis();
		
		System.out.println("\n\nDurata: " + (end-start) + " millisecondi");

	}

}
