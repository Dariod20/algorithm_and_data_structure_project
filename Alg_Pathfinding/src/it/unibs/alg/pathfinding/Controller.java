package it.unibs.alg.pathfinding;

import java.util.ArrayList;
import java.util.List;

public class Controller {
	
	/*
	 * Valori massimi consentiti per pct ostacoli e fattAggl:
	 * 0.9 e 0
	 * 0.6 e 0.1
	 * 0.4 e 0.2
	 * 0.3 e 0.4
	 * 0.2 e 0.6
	 * 0.1 e 0.9
	 * Input e output da file
	 */
	
	private GridGenerator gridGenerator;
	private int numAgents = 4;
	private List<List<IntArrayState>> existingAgentsPaths = new ArrayList<>();
	
	public void startAlgorithm() {
		//da migliorare il fattore di agglomerazione !!!
		gridGenerator = new GridGenerator(15, 15, 0.2, 0.6);
		int[][] grid = gridGenerator.getGrid();
		
//		genero griglia in modo manuale
//		grid[2][0] = grid[2][1] = grid[2][2] = 
//		grid[4][0] = grid[4][1] = grid[4][2] =
//		grid[1][6] = grid[1][7] = grid[2][6] = grid[2][7] = 
//		grid[8][1] = grid[7][1] = grid[7][2] = grid[6][3] =
//		grid[7][5] = grid[6][5] = grid[5][5] = grid[5][6] = grid[5][7] = grid[6][7] = grid[7][7] = 0;
		
		System.out.println("\nGRIGLIA " + grid.length + " x " + grid[0].length + "\n");
		printGrid(grid);
		findPath(grid);
	}
	
	private void printGrid(int[][] grid) {
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
		System.out.println();
	}
	
	
	/*
	 * Due alternative possibili se per un agente è impossibile da init andare in goal:
	 * - Ignorarlo e passare al prossimo
	 * - Cambiare il suo goal (o il suo init)
	 */
	private void findPath(int[][] grid) {
		for(int n = 0; n < numAgents; n++) {
//			int init = -1;
//			int goal = -1;
			
//			if(n == 0) {
//				init = grid[5][3];
//				goal = grid[1][1];
//			} else if(n == 1) {
//				init = grid[3][1];
//				goal = grid[3][3];
//			} else {
//				init = grid[0][0];
//				goal = grid[9][9];
//			}
			
			int init = gridGenerator.getRandomInit();
			int goal = gridGenerator.getRandomGoal(init);
			
			System.out.println("Agent " + (n+1));
			System.out.println("Initial state: " + init);
			System.out.println("Goal state: " + goal + "\n");
			
			ReachGoal reachGoal = new ReachGoal(grid, existingAgentsPaths, init, goal, gridGenerator.getMax());
			reachGoal.runReachGoal();
			if(reachGoal.getSuccessful()) {
				existingAgentsPaths.add(reachGoal.reconstructPath());
			} else {
				List<IntArrayState> onlyInitAndGoal = new ArrayList<>();
				onlyInitAndGoal.add(new IntArrayState(new int[] {init, 0}));
				onlyInitAndGoal.add(new IntArrayState(new int[] {goal, 0}));
				existingAgentsPaths.add(onlyInitAndGoal);
			}
			
			if(grid.length < 100) {
				System.out.print("\n\n\n");
				System.out.println("Simulazione dello spostamento degli agenti finora presi in considerazione "
						+ "\ndalla loro cella di partenza a quella di arrivo sulla griglia.\n");
				
				int maxNumAgents = getMaxNumAgentsOnTheSameCell(grid);
				
				for(int i=0; i < grid.length; i++) {
					for(int j=0; j < grid[0].length; j++) {
						boolean emptyCell = true;
						int counter = 0;
						if(grid[i][j] == 0) {
							for(int k=0; k < maxNumAgents; k++) {
								System.out.print("X");
							}
							System.out.print(" | ");
						} else {
							for(int k=0; k < existingAgentsPaths.size(); k++) {
								for(IntArrayState cell: existingAgentsPaths.get(k)) {
									if(cell.getState()[0] == grid[i][j]) {
										counter++;
										System.out.print((k+1));
										emptyCell = false;
										break;
									}
								}
							}
							if(emptyCell) {
								for(int k=0; k < maxNumAgents; k++) {
									System.out.print(" ");
								}
							} else {
								for(int k=0; k < (maxNumAgents - counter); k++) {
									System.out.print(" ");
								}
							}
							System.out.print(" | ");
						}
					}
					System.out.println();
					for(int l=0; l < grid[0].length; l++) {
						System.out.print("---");
						for(int k=0; k < maxNumAgents; k++) {
							System.out.print("-");
						}
					}
					System.out.println();
				}
			}
			System.out.println("\n");
		}
	}
	
	private int getMaxNumAgentsOnTheSameCell(int[][] grid) {
		int maxNumAgents = 0;
		for(int i=0; i < grid.length; i++) {
			for(int j=0; j < grid[0].length; j++) {
				int counter = 0;
				if(grid[i][j] != 0) {
					for(List<IntArrayState> existingPath: existingAgentsPaths) {
						for(IntArrayState cell: existingPath) {
							if(cell.getState()[0] == grid[i][j]) {
								counter++;
							}
						}
					}
					if(counter > maxNumAgents) {
						maxNumAgents = counter;
					}
				} 
			}
		}
		return maxNumAgents;
	}
	

}
