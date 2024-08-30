package it.unibs.alg.pathfinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Main {
	
	public final static double INFINITE_COST = 1000000;

	
	public static void main(String[] args) {
		
		long start = System.currentTimeMillis();
		
//		se pct ostacoli e fattAggl troppo alti, la griglia non si crea
		
		GridGenerator gridGenerator = new GridGenerator(5, 5, 0.2, 0);
		String[][] grid = gridGenerator.getGrid();
		
//		genero griglia in modo manuale
		
		grid[1][3] = grid[2][1] = grid[2][2] = grid[2][3] = "x";
//		grid[0][3] = grid[1][3] = grid[2][3] = grid[3][3]= grid[4][3] = "x";
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
		
//		for(Map.Entry<String, Double> entry : weightedGraph.entrySet()) {
//	            System.out.println("Key: " + entry.getKey() + ", Value: " + entry.getValue());
//	    }
		
		System.out.println();
		System.out.println();
		
		
		//G da calcolare in GridGenerator
		//dim G = numero celle - numero ostacoli
		int[] G = new int[21];
		gridGenerator.setG(G);
		
		ReachGoal reachGoal = new ReachGoal(G, weightedGraph, grid[0][2], grid[3][2], 21);
		reachGoal.runReachGoal();
		
		long end = System.currentTimeMillis();
		
		System.out.println("\n\nDurata: " + (end-start) + " millisecondi");

	}

}
