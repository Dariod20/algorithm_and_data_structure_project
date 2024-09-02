package it.unibs.alg.pathfinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Main {
	
	public static void main(String[] args) {
		
		long start = System.currentTimeMillis();
		
//		se pct ostacoli e fattAggl troppo alti, la griglia non si crea
		
		GridGenerator gridGenerator = new GridGenerator(5, 5, 0.2, 0);
		String[][] grid = gridGenerator.getGrid();
		
		String init = grid[0][2];
		String goal = grid[3][2];
		int max = 21;
		
//		genero griglia in modo manuale
		
		grid[1][3] = grid[2][1] = grid[2][2] = grid[2][3] = "x";
//		grid[0][3] = grid[1][3] = grid[2][3] = grid[3][3]= grid[4][3] = "x";
		gridGenerator.instantiatewG();
		
		Map<String, Double> weightedGraph = gridGenerator.getwG();
		
		for(int i=0; i < grid.length; i++) {
			for(int j=0; j < grid[0].length; j++) {
				if(i==0 && j==2) 
					System.out.print("i" + "   ");
				else if(i==3 && j==2)
					System.out.print("g" + "   ");
				else if(grid[i][j].equals("x"))
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
		
//		for(int i=0; i < grid.length; i++) {
//			for(int j=0; j < grid[0].length; j++) {
//				if(!grid[i][j].equals("x")) 
//					System.out.print("0 ");
//				else
//					System.out.print("1 ");
//			}
//			System.out.println();
//		}
//		
//		System.out.println();
		
//		for(Map.Entry<String, Double> entry : weightedGraph.entrySet()) {
//	            System.out.println("Key: " + entry.getKey() + ", Value: " + entry.getValue());
//	    }
		
		System.out.println();
		System.out.println();
		
		
		//G da calcolare in GridGenerator
		//dim G = numero celle - numero ostacoli
		int[] G = new int[max];
		gridGenerator.setG(G);
		
		//Per ottimizzare si potrebbe fare una lista semplice, ma così non si distinguono più 
		//i percorsi degli agenti
		List<List<IntArrayKey>> existingAgentsPaths = new ArrayList<>();
		
		List<IntArrayKey> a1Path = new ArrayList<>();
		a1Path.add(new IntArrayKey(new int[] {17, 3}));
		existingAgentsPaths.add(a1Path);
		
		List<IntArrayKey> a2Path = new ArrayList<>();
		a2Path.add(new IntArrayKey(new int[] {2, 0}));
		a2Path.add(new IntArrayKey(new int[] {7, 1}));
		existingAgentsPaths.add(a2Path);
		
		
		/*
		 * Rimane da controllare quando l'agente si ferma su una cella: idea di considerarlo come un 
		 * ostacolo (x) permanente o far diventare la sua euristica al massimo
		 */
		ReachGoal reachGoal = new ReachGoal(G, weightedGraph, existingAgentsPaths, init, goal, max);
		reachGoal.runReachGoal();
		reachGoal.reconstructPath();
		
		long end = System.currentTimeMillis();
		
		System.out.println("\n\nDurata: " + (end-start) + " millisecondi");

	}

}
