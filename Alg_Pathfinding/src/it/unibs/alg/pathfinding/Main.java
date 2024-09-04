package it.unibs.alg.pathfinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Main {
	
	public static void main(String[] args) {
		
		long start = System.currentTimeMillis();
		
//		se pct ostacoli e fattAggl troppo alti, la griglia non si crea
		
//		Chiamata ricorsiva per l'euristica troppo dispendiosa
		
//		Input e output da file
		
		GridGenerator gridGenerator = new GridGenerator(70, 70, 0.02, 0);
		int[][] grid = gridGenerator.getGrid();
		
//		genero griglia in modo manuale
		grid[2][0] = grid[2][1] = grid[2][2] = 
		grid[4][0] = grid[4][1] = grid[4][2] =
		grid[1][6] = grid[1][7] = grid[2][6] = grid[2][7] = 
		grid[8][1] = grid[7][1] = grid[7][2] = grid[6][3] =
		grid[7][5] = grid[6][5] = grid[5][5] = grid[5][6] = grid[5][7] = grid[6][7] = grid[7][7] = 0;
//		gridGenerator.instantiatewG();
		
//		Map<String, Double> weightedGraph = gridGenerator.getwG();
		
		for(int i=0; i < grid.length; i++) {
			for(int j=0; j < grid[0].length; j++) {
				if(grid[i][j] == 0)
					System.out.print("X   ");
				else if(grid[i][j] < 10) 
					System.out.print(grid[i][j] + "   ");
				else if(grid[i][j] < 100) 
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
		
		int numAgents = 1;
		List<List<IntArrayState>> existingAgentsPaths = new ArrayList<>();
		
		/*
		 * Due alternative possibili se per un agente è impossibile da init andare in goal:
		 * - Ignorarlo e passare al prossimo
		 * - Cambiare il suo goal (o il suo init)
		 */
		
		for(int n = 0; n < numAgents; n++) {
			int init = -1;
			int goal = -1;
			
			if(n == 0) {
				init = grid[0][0];
				goal = grid[69][69];
			} else if(n == 1) {
				init = grid[0][9];
				goal = grid[6][6];
			} else {
				init = grid[0][0];
				goal = grid[9][9];
			}
			
			System.out.println("Initial state: " + init);
			System.out.println("Goal state: " + goal + "\n");
			
			ReachGoal reachGoal = new ReachGoal(grid, existingAgentsPaths, init, goal, gridGenerator.getMax());
			reachGoal.runReachGoal();
			if(reachGoal.getSuccessful()) {
				existingAgentsPaths.add(reachGoal.reconstructPath());
			} 
			
			System.out.print("\n\n\n");
			System.out.println("Simulazione dello spostamento degli agenti finora presi in considerazione "
					+ "\ndalla loro cella di partenza a quella di arrivo sulla griglia.");
			System.out.println("Il percorso di alcuni agenti sembra incompleto, ma in realtà non si vede perchè si "
					+ "sovrappone a quello \ndegli altri agenti in istanti temporali distinti.\n");
			
			for(int i=0; i < grid.length; i++) {
				for(int j=0; j < grid[0].length; j++) {
					boolean emptyCell = true;
					if(grid[i][j] == 0) {
						System.out.print("XX | ");
					} else {
						for(int k=0; k < existingAgentsPaths.size(); k++) {
							for(IntArrayState cell: existingAgentsPaths.get(k)) {
								if(cell.getState()[0] == grid[i][j]) {
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
			long milliDuration = end - start;
			int duration = Math.round(milliDuration/1000);
			System.out.println("\n\nDurata: " + duration + " secondi\n\n\n");
//			System.out.println("\n\nDurata: " + milliDuration + " millisecondi\n\n\n");
		}

	}

}
