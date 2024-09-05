package it.unibs.alg.pathfinding;

import java.util.ArrayList;
import java.util.List;

public class Controller {
	
	private static final int MAX_DIM_TO_PRINT_GRID = 30;
	private GridGenerator gridGenerator;
	private int numAgents = 0;
	private List<List<IntArrayState>> existingAgentsPaths = new ArrayList<>();
        
    private int init = 0;
    private int goal = 0;
	
	public void startAlgorithm() {
		double[] inputs = Utility.readInputs();
		int numRows = (int) inputs[0];
		int numCols = (int) inputs[1];
		double pctObst = inputs[2];
		double agglFact = inputs[3];
		numAgents = (int) inputs[4];
		init = (int) inputs[5];
		goal = (int) inputs[6];
		
		gridGenerator = new GridGenerator(numRows, numCols, pctObst, agglFact);
		int[][] grid = gridGenerator.getGrid();
		
//		genero griglia in modo manuale
//		grid[2][0] = grid[2][1] = grid[2][2] = 
//		grid[4][0] = grid[4][1] = grid[4][2] =
//		grid[1][6] = grid[1][7] = grid[2][6] = grid[2][7] = 
//		grid[8][1] = grid[7][1] = grid[7][2] = grid[6][3] =
//		grid[7][5] = grid[6][5] = grid[5][5] = grid[5][6] = grid[5][7] = grid[6][7] = grid[7][7] = 0;
		
		if(Utility.fileExists()) {
			System.out.println("\nFILE OUTPUT ALREADY EXISTS: delete or rename it!\n");
			return;
		} 
		
		Utility.writeOnFile("Numero righe: " + numRows + "\n");
		Utility.writeOnFile("Numero colonne: " + numCols + "\n");
		Utility.writeOnFile("Celle attraversabili: " + (1-pctObst) + " (" + (int)((numRows*numCols)*(1-pctObst)) + "/" + (numRows*numCols) + ")\n");
		Utility.writeOnFile("Fattore agglomerazione: " + agglFact + "\n");
		Utility.writeOnFile("Max iterazioni: " + gridGenerator.getMax() + "\n");
		Utility.writeOnFile("Stato iniziale: " + init + "\n");
		Utility.writeOnFile("Stato goal: " + goal + "\n");
		Utility.writeOnFile("Numero agenti: " + numAgents + "\n");
		if(numAgents > 1) {
			Utility.writeOnFile("\nPercorsi agenti preesistenti calcolati con chiamata ricorsiva di ReachGoal() con Init e Goal casuali\n");
		} else {
			Utility.writeOnFile("\nNessun agente preesistente\n");
		}
		
		if(grid.length <= MAX_DIM_TO_PRINT_GRID) {
			Utility.writeOnFile("\nGRIGLIA " + grid.length + " x " + grid[0].length + "\n\n");
			printGrid(grid);
		}
		findPath(grid);
	}
	
	private void printGrid(int[][] grid) {
		for(int i=0; i < grid.length; i++) {
			for(int j=0; j < grid[0].length; j++) {
				if(grid[i][j] == 0)
					Utility.writeOnFile("X    ");
				else if(grid[i][j] < 10) 
					Utility.writeOnFile(grid[i][j] + "    ");
				else if(grid[i][j] < 100) 
					Utility.writeOnFile(grid[i][j] + "   ");
				else if(grid[i][j] < 1000) 
					Utility.writeOnFile(grid[i][j] + "  ");
				else 
					Utility.writeOnFile(grid[i][j] + " ");
			}
			Utility.writeOnFile("\n");
		}
		
		Utility.writeOnFile("\n");
	}
	
	private void findPath(int[][] grid) {
		for(int n = 0; n < numAgents; n++) {
			
//			int init = -1;
//			int goal = -1;
//			if(n == 0) {
//				init = grid[2][0];
//				goal = grid[0][2];
//			} else if(n == 1) {
//				init = grid[1][0];
//				goal = grid[3][2];
//			} else if(n == 2) {
//				init = grid[1][6];
//				goal = grid[1][4];
//			} else {
//				init = grid[3][4];
//				goal = grid[0][4];
//			}
			
//			int init = gridGenerator.getRandomInit();
//			int goal = gridGenerator.getRandomGoal(init);
			
			Utility.writeOnFile("\nAgente " + (n+1) + "\n");
			Utility.writeOnFile("Suo stato iniziale: " + init + "\n");
			Utility.writeOnFile("Suo stato goal: " + goal + "\n\n");
			
			ReachGoal reachGoal = new ReachGoal(grid, existingAgentsPaths, init, goal, gridGenerator.getMax());
			reachGoal.runReachGoal();
			if(reachGoal.getSuccessful()) {
				List<IntArrayState> path = reachGoal.reconstructPath();
				existingAgentsPaths.add(path);
				
				Utility.writeOnFile("\nPath: \n");
				for(int i=path.size()-1; i >= 0; i--) {
					if(i==0) {
						Utility.writeOnFile("(" + path.get(i).getCell() + ", " + path.get(i).getIstant() + ")");
					} else {
						Utility.writeOnFile("(" + path.get(i).getCell() + ", " + path.get(i).getIstant() + ") -> ");
					}
				}
				
			} else {
				List<IntArrayState> onlyInitAndGoal = new ArrayList<>();
				onlyInitAndGoal.add(new IntArrayState(new int[] {init, 0}));
				onlyInitAndGoal.add(new IntArrayState(new int[] {goal, 0}));
				existingAgentsPaths.add(onlyInitAndGoal);
			}
			
			if(grid.length <= MAX_DIM_TO_PRINT_GRID) {
				Utility.writeOnFile("\n\nSimulazione dello spostamento degli agenti finora presi in considerazione dal loro stato iniziale al goal.\n\n");
				
				int maxNumAgents = getMaxNumAgentsOnTheSameCell(grid);
				
				for(int i=0; i < grid.length; i++) {
					for(int j=0; j < grid[0].length; j++) {
						boolean emptyCell = true;
						int counter = 0;
						if(grid[i][j] == 0) {
							for(int k=0; k < maxNumAgents; k++) {
								Utility.writeOnFile("X");
							}
							Utility.writeOnFile(" | ");
						} else {
							for(int k=0; k < existingAgentsPaths.size(); k++) {
								for(IntArrayState cell: existingAgentsPaths.get(k)) {
									if(cell.getCell() == grid[i][j]) {
										counter++;
										Utility.writeOnFile(""+(k+1));
										emptyCell = false;
										break;
									}
								}
							}
							if(emptyCell) {
								for(int k=0; k < maxNumAgents; k++) {
									Utility.writeOnFile(" ");
								}
							} else {
								for(int k=0; k < (maxNumAgents - counter); k++) {
									Utility.writeOnFile(" ");
								}
							}
							Utility.writeOnFile(" | ");
						}
					}
					Utility.writeOnFile("\n");
					for(int l=0; l < grid[0].length; l++) {
						Utility.writeOnFile("---");
						for(int k=0; k < maxNumAgents; k++) {
							Utility.writeOnFile("-");
						}
					}
					Utility.writeOnFile("\n");
				}
			}
			Utility.writeOnFile("\n\n");
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
							if(cell.getCell() == grid[i][j]) {
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
