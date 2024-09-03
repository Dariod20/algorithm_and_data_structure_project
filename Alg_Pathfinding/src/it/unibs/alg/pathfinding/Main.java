package it.unibs.alg.pathfinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Main {
	
	public static void main(String[] args) {
		
		long start = System.currentTimeMillis();
		
//		se pct ostacoli e fattAggl troppo alti, la griglia non si crea
		GridGenerator gridGenerator = new GridGenerator(10, 8, 0.15, 0);
		String[][] grid = gridGenerator.getGrid();
		
		String init = grid[0][3];
		String goal = grid[4][3];
		
//		genero griglia in modo manuale
		grid[2][2] = grid[2][3] = grid[2][4] = 
		grid[4][1] = grid[5][1] = grid[6][1] = grid[7][1] = 
		grid[4][5] = grid[5][5] = grid[6][5] = 
		grid[8][6] = grid[8][7] ="x";
		gridGenerator.instantiatewG();
		
		Map<String, Double> weightedGraph = gridGenerator.getwG();
		
		for(int i=0; i < grid.length; i++) {
			for(int j=0; j < grid[0].length; j++) {
				if(i==0 && j==3) 
					System.out.print("i" + "   ");
				else if(i==4 && j==3)
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
		
//		for(Map.Entry<String, Double> entry : weightedGraph.entrySet()) {
//	            System.out.println("Key: " + entry.getKey() + ", Value: " + entry.getValue());
//	    }
		
		System.out.println();
		System.out.println();
		
		
		//G da calcolare in GridGenerator
		//dim G = numero celle - numero ostacoli
		int[] G = gridGenerator.getG();
		
		List<List<IntArrayKey>> existingAgentsPaths = new ArrayList<>();
		
		List<IntArrayKey> a1Path = new ArrayList<>();
		a1Path.add(new IntArrayKey(new int[] {9, 0}));
		a1Path.add(new IntArrayKey(new int[] {18, 1}));
		a1Path.add(new IntArrayKey(new int[] {27, 2}));
		a1Path.add(new IntArrayKey(new int[] {35, 3}));
		a1Path.add(new IntArrayKey(new int[] {43, 4}));
		existingAgentsPaths.add(a1Path);
		
		List<IntArrayKey> a2Path = new ArrayList<>();
		a2Path.add(new IntArrayKey(new int[] {1, 0}));
		a2Path.add(new IntArrayKey(new int[] {10, 1}));
		existingAgentsPaths.add(a2Path);
		
		List<IntArrayKey> a3Path = new ArrayList<>();
		a3Path.add(new IntArrayKey(new int[] {66, 0}));
		a3Path.add(new IntArrayKey(new int[] {59, 1}));
		a3Path.add(new IntArrayKey(new int[] {52, 2}));
		a3Path.add(new IntArrayKey(new int[] {53, 3}));
		existingAgentsPaths.add(a3Path);
		
		ReachGoal reachGoal = new ReachGoal(G, weightedGraph, existingAgentsPaths, init, goal, gridGenerator.getMax());
		reachGoal.runReachGoal();
		List<IntArrayKey> entryAgentPath = reachGoal.reconstructPath();
		existingAgentsPaths.add(entryAgentPath);
		
		System.out.print("\n\n\n");
		System.out.println("Simulazione dello spostamento di tutti gli agenti dalla loro cella di partenza a quella di "
				+ "arrivo sulla griglia.");
		System.out.println("Il percorso di alcuni agenti sembra incompleto, ma in realtà non si vede perchè si "
				+ "sovrappone a quello \ndegli altri agenti in istanti temporali distinti.\n");
		
		for(int i=0; i < grid.length; i++) {
			for(int j=0; j < grid[0].length; j++) {
				boolean emptyCell = true;
				if(grid[i][j].equals("x")) {
					System.out.print("XX | ");
				} else {
					for(int k=0; k < existingAgentsPaths.size(); k++) {
						for(IntArrayKey cell: existingAgentsPaths.get(k)) {
							if(cell.getKey()[0] == Integer.parseInt(grid[i][j])) {
								if(!emptyCell) {
									System.out.print((k+1));
								} else {
									System.out.print("A" + (k+1) + " | ");
									emptyCell = false;
									break;
								}
							}
							if(!emptyCell) {
								break;
							}
						}
					}
					if(emptyCell) {
						System.out.print("   | ");
					}
				}
			}
			System.out.println();
			for(int l=0; l < grid[0].length; l++) {
				System.out.print("-----");
			}
			System.out.println();
		}
		
		long end = System.currentTimeMillis();
		
		System.out.println("\n\nDurata: " + (end-start) + " millisecondi");

	}

}
