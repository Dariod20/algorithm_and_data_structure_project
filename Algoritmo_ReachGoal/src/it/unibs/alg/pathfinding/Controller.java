package it.unibs.alg.pathfinding;

import java.util.ArrayList;
import java.util.List;

public class Controller {
	
	private static final int MAX_AGENTS_TO_PRINT_SIMULATION = 8;
	private static final int MAX_DIM_TO_PRINT_GRID = 30;
	private static final int MAX_PATH_STATES_ON_THE_SAME_LINE = 500;
	private static final int BYTE_ORDER = 1000000;
	private static final String BYTE_PREF = "mega";
	
	private List<String> inputs;
	private int max;
	private int numAgents;
	int numRows;
	int numCols;
	double pctObst;
	double agglFact;
    private int entryInit;
    private int entryGoal;
    private int minLengthAgentsPath;
    private boolean isGridManual;
	private int agentsStart = 0;
	private GridGenerator gridGenerator;
	private List<List<IntArrayState>> existingAgentsPaths = new ArrayList<>();
	
	
	public Controller() {
		inputs = Utility.readInputs();
		try {
			numRows = Integer.parseInt(inputs.get(0));
			numCols = Integer.parseInt(inputs.get(1));
			pctObst = Double.parseDouble(inputs.get(2));
			agglFact = Double.parseDouble(inputs.get(3));
			max = Integer.parseInt(inputs.get(4));
			numAgents = Integer.parseInt(inputs.get(5));
			entryInit = Integer.parseInt(inputs.get(6));
			entryGoal = Integer.parseInt(inputs.get(7));
			minLengthAgentsPath = Integer.parseInt(inputs.get(8));
		} catch(NumberFormatException e) {
			System.out.println("\nATTENZIONE: uno dei paremtri del file " + Utility.INPUT_FILE + " che si aspetta come valore "
					+ "un numero, ha assegnato invece una stringa\n");
			System.exit(1);
		}
		isGridManual = Boolean.parseBoolean(inputs.get(9));
	}
	
	/*
	 * Method that execute all the process
	 */
	public void startAlgorithm() {
		gridGenerator = new GridGenerator(numRows, numCols, pctObst, agglFact);
		if(isGridManual) {
			agentsStart = gridGenerator.insertManuallyObstacles(inputs, 10);
		} else {
			gridGenerator.insertRandomObstacles();
			gridGenerator.checkEntryInitGoalNotObstacles(entryInit, entryGoal);
		}
		int[][] grid = gridGenerator.getGrid();
		if(Utility.fileExists()) {
			Utility.deleteFile();
		} 
		writeFirstOutputs(isGridManual, agentsStart);
		if(grid.length <= MAX_DIM_TO_PRINT_GRID) {
			Utility.writeOnFile("\nGRIGLIA " + grid.length + " x " + grid[0].length + "\n\n");
			printGrid(grid);
		}
		findPath(grid);
		printMemory();
	}
	
	/*
	 * Write on file 'Output.txt' the instance information
	 */
	private void writeFirstOutputs(boolean isGridManual, int agentsStart) {
		int dim = numRows*numCols;
		Utility.writeOnFile("Numero righe: " + numRows + "\n");
		Utility.writeOnFile("Numero colonne: " + numCols + "\n");
		if(!isGridManual) {
			Utility.writeOnFile("Celle attraversabili: " + String.format("%.2f", (1-pctObst)) + " (" + (int)(dim*(1-pctObst)) + "/" + dim + ")\n");
			Utility.writeOnFile("Fattore agglomerazione: " + agglFact + "\n");
		} else {
			double pct_obstacles = ((double) (agentsStart-10)) / dim;
			Utility.writeOnFile("Celle attraversabili: " + String.format("%.2f", (1-pct_obstacles)) + " (" + (dim-(agentsStart-10)) + "/"+ dim + ")\n");
		}
		Utility.writeOnFile("Max iterazioni: " + max + "\n");
		Utility.writeOnFile("Stato iniziale: " + entryInit + "\n");
		Utility.writeOnFile("Stato goal: " + entryGoal + "\n");
		Utility.writeOnFile("Numero agenti preesistenti: " + numAgents + "\n");
		if(numAgents > 0) {
			Utility.writeOnFile("Lunghezza minima del percorso degli agenti preesistenti: " + minLengthAgentsPath + "\n");
			if(isGridManual) {
				Utility.writeOnFile("\nPercorsi agenti preesistenti calcolati con chiamata iterativa di ReachGoal() con Init e Goal decisi dll'utente\n");
			} else {
				Utility.writeOnFile("\nPercorsi agenti preesistenti calcolati con chiamata iterativa di ReachGoal() con Init e Goal casuali\n");
			}
		}
	}
	
	/*
	 * Print the values contained in the grid, substituting the obstacle
	 * value 0 with an 'X'
	 */
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
	
	/*
	 * For each agent: get its init and goal, call reachGoal algorithm and, if it is successful,
	 * print the agent path on file and add it to the existing agents path list
	 */
	private void findPath(int[][] grid) {
		long start = 0;
		int init = -1;
		int goal = -1;
		int agentIndex = agentsStart;
		for(int n = 0; n < (numAgents+1); n++) {
			if(n == 2) {
				int a = 9;
			}
			if(n == numAgents) {
				start = System.currentTimeMillis();
				init = entryInit;
				goal = entryGoal;
			} else {
				if(agentsStart == 0) {
					init = gridGenerator.getRandomInit();
					goal = gridGenerator.getRandomGoal(minLengthAgentsPath);
				} else {
					init = Integer.parseInt(inputs.get(agentIndex++));
					goal = Integer.parseInt(inputs.get(agentIndex++));
				}
			}
			
			Utility.writeOnFile("\nAgente " + (n+1) + "\n");
			Utility.writeOnFile("Suo stato iniziale: " + init + "\n");
			Utility.writeOnFile("Suo stato goal: " + goal + "\n\n");
			
			ReachGoal reachGoal = new ReachGoal(grid, existingAgentsPaths, init, goal, max);
			reachGoal.runReachGoal();
			if(reachGoal.getSuccessful()) {
				int numWaitMoves = 0;
				List<IntArrayState> path = reachGoal.reconstructPath();
				existingAgentsPaths.add(path);
				Utility.writeOnFile("\nPath: \n");
				for(int i=path.size()-1; i >= 0; i--) {
					if(i==0) {
						Utility.writeOnFile("(" + path.get(i).getCell() + ", " + path.get(i).getIstant() + ")");
					} else {
						if(path.get(i).getCell() ==  path.get(i-1).getCell()) {
							numWaitMoves++;
						}
						Utility.writeOnFile("(" + path.get(i).getCell() + ", " + path.get(i).getIstant() + ") -> ");
					}
					if(i % MAX_PATH_STATES_ON_THE_SAME_LINE == 0) {
						Utility.writeOnFile("\n");
					}
				}
				Utility.writeOnFile("\nNumero mosse wait: " + numWaitMoves + "\n\n");
				
			} else {
				List<IntArrayState> onlyInitAndGoal = new ArrayList<>();
				onlyInitAndGoal.add(new IntArrayState(new int[] {goal, max}));
				onlyInitAndGoal.add(new IntArrayState(new int[] {init, 0}));
				existingAgentsPaths.add(onlyInitAndGoal);
			}
			
			printAgentsSimulation(grid);
		}
		long end = System.currentTimeMillis();
		Utility.writeOnFile("\nTempo speso per trovare percorso del nuovo agente: " + (end-start) + " millisecondi\n\n");
	}

	/*
	 * Print the agents movement simulation on the grid
	 */
	private void printAgentsSimulation(int[][] grid) {
		if(grid.length <= MAX_DIM_TO_PRINT_GRID && numAgents <= MAX_AGENTS_TO_PRINT_SIMULATION) {
			Utility.writeOnFile("Simulazione dello spostamento degli agenti finora presi in considerazione dal loro stato iniziale al goal.\n\n");
			
			int maxNumAgents = getMaxNumAgentsOnACell(grid);
			
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
			Utility.writeOnFile("\n\n");
		}
	}
	
	/*
	 * Get the maximum number of agents that cross a cell
	 */
	private int getMaxNumAgentsOnACell(int[][] grid) {
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
	
	/*
	 * Print information about:
	 * - the total heap size (allocated memory)
	 * - the maximum heap size (if set by the JVM)
	 * - the free memory within the heap
	 */
	private void printMemory() {
		Runtime runtime = Runtime.getRuntime();
        long heapSize = runtime.totalMemory();
        long maxHeapSize = runtime.maxMemory();
        long freeMemory = runtime.freeMemory();
        
        Utility.writeOnFile("Heap Size corrente (memoria occupata): " + String.format("%.3f", ((double)heapSize/BYTE_ORDER))
        	+ " " + BYTE_PREF + "bytes su ");
        Utility.writeOnFile("" + String.format("%.3f", ((double)maxHeapSize/BYTE_ORDER)) + " " + BYTE_PREF + "bytes\n");
        Utility.writeOnFile("\nMemoria dell'Heap Size corrente libera: " + String.format("%.3f", ((double)freeMemory/BYTE_ORDER)) 
        	+ " " + BYTE_PREF + "bytes\n");
	}

}
