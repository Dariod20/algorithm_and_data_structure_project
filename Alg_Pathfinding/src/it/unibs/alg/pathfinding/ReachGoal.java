package it.unibs.alg.pathfinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class ReachGoal {
	
	private int[][] grid;
	private List<List<IntArrayState>> existingAgentsPaths;
	private int init;
	private int goal;
	private int max;
	
	private int t = 0;
	private int step = 0;
	private boolean successful = true;
	private List<IntArrayState> Open = new ArrayList<>();
	private List<IntArrayState> Closed = new ArrayList<>();
	private Map<IntArrayState, Double> g = new HashMap<>();
	private Map<IntArrayState, Double> f = new HashMap<>();
	private Map<IntArrayState, IntArrayState> P = new HashMap<>();
	private Map<Integer, Double> h = new HashMap<>();
	
	public ReachGoal(int[][] grid, List<List<IntArrayState>> existingAgentsPaths,
			int init, int goal, int max) {
		this.grid = grid;
		this.existingAgentsPaths = existingAgentsPaths;
		this.init = init;
		this.goal = goal;
		this.max = max;
	}
	
	public void runReachGoal() {
		executeFirstTwoSteps(init);
		Integer chosenNextCell = extractStateWithMinCostFromOpen();
		
		while(!Open.isEmpty()) {
			if(t <= max) {
				if(chosenNextCell == 0) {
					Utility.writeOnFile("\nDallo stato iniziale " + init + " � impossibile raggiungere il goal in " + goal + "\n");
					successful = false;
					break;
				}
				if(chosenNextCell != goal) {
					step++;
//					System.out.println("\nOpen in step " + step++);
					addStatesToOpenAndComputeTheirCost(chosenNextCell);
//					for(IntArrayState state: Open) {
//						double cost = f.get(new IntArrayState (new int[] {state.getCell(), state.getIstant()}));
//						System.out.printf("(%d, %d) -> %.2f\n", state.getCell(), state.getIstant(), cost);
//					}
					chosenNextCell = extractStateWithMinCostFromOpen();
				} else {
					t--;
					
//					System.out.print("\nClosed list: ");
//					for(IntArrayState state: Closed) {
//						System.out.print("(" + state.getCell() + ", " + state.getIstant() + "), ");
//					}
					Utility.writeOnFile("Numero di stati espansi: " + Closed.size()/*step*/);
					Utility.writeOnFile("\nNumero totale di stati inseriti nella Open: " + (P.size()+1));
					Utility.writeOnFile("\nLunghezza del percorso: " + t);
					Utility.writeOnFile("\nCosto del percorso: " + String.format("%.2f", f.get(new IntArrayState(new int[] {chosenNextCell, t}))));
					Utility.writeOnFile("\nNumero mosse wait: ???\n");
					break;
				}
			} else {
				Utility.writeOnFile("\nCi vogliono troppe iterazioni per cercare di raggiungere il goal: lunghezza_percorso = " + t + " supera max = " + max + "\n");
				successful = false;
				break;
			}
		}
		
		if(Open.isEmpty()) {
			Utility.writeOnFile("\nLa lista Open � vuota.\n");
			successful = false;
		}
		
	}
	
	private void executeFirstTwoSteps(int initial) {
		IntArrayState initState = new IntArrayState (new int[] {initial, 0});
		
		HeuristicAlgorithm heuristic = new HeuristicAlgorithm(grid, goal);
		heuristic.runHeuristic();
		h = heuristic.getH();
		
//		for(Entry<Integer, Double> entry : h.entrySet()) {
//            System.out.println("State: " + entry.getKey() + ", Cost: " + entry.getValue());
//		}
		
//		f.put(Open.get(0), h.get(initial));
//		System.out.println("Open in step " + step++);
//		System.out.printf("(%d, %d) -> %.2f\n", initial, t, h.get(initial));
		
		t++;
		step++;
//		System.out.println("\nOpen in step " + step++);
		addFirstStatesToOpenAndComputeTheirCost(initial, initState);
		Closed.add(initState);
	}
	
	private void addFirstStatesToOpenAndComputeTheirCost(int initial, IntArrayState initState) {
		int[] neighborhood = getNeighborhood(initial);
		double[] neighborhoodCost = getNeighborhoodCost(initial);
		
		List<IntArrayState> collisionStates = getCollisionStates(initial, neighborhood);
		
		for(int i=0; i < neighborhood.length; i++) {
			int nextPossibleCell = initial + neighborhood[i];
			int numCols = grid[0].length;
			int nextRowIndex = 0;
			int nextColIndex = 0;
			if(nextPossibleCell % numCols != 0) {
				nextRowIndex = nextPossibleCell / numCols;
				nextColIndex = nextPossibleCell % numCols-1;
			} else {
				nextRowIndex = nextPossibleCell / numCols-1;
				nextColIndex = numCols-1;
			}
			if(grid[nextRowIndex][nextColIndex] != 0) {
				IntArrayState nextPossibleState = new IntArrayState (new int[] {nextPossibleCell, t});
				if(!collisionStates.contains(nextPossibleState)) {
					P.put(nextPossibleState, initState);
					Open.add(nextPossibleState);
					g.put(nextPossibleState, neighborhoodCost[i]);
					double cost = neighborhoodCost[i] + h.get(nextPossibleCell);
					f.put(nextPossibleState, cost);
//					System.out.printf("(%d, %d) -> %.2f\n", nextPossibleCell, t, cost);
				} 
//				else {
//					System.out.printf("\nStato (%d, %d) non aggiunto a Open perch� in conflitto con "
//							+ "un altro agente\n", nextPossibleCell, t);
//				}
			}
		}
	}
	
	private void addStatesToOpenAndComputeTheirCost(int cell) {
		int[] neighborhood = getNeighborhood(cell);
		double[] neighborhoodCost = getNeighborhoodCost(cell);
		
		List<IntArrayState> collisionStates = getCollisionStates(cell, neighborhood);
		
		for(int i=0; i < neighborhood.length; i++) {
			int nextPossibleCell = cell + neighborhood[i];
			int numCols = grid[0].length;
			int nextRowIndex = 0;
			int nextColIndex = 0;
			if(nextPossibleCell % numCols != 0) {
				nextRowIndex = nextPossibleCell / numCols;
				nextColIndex = nextPossibleCell % numCols-1;
			} else {
				nextRowIndex = nextPossibleCell / numCols-1;
				nextColIndex = numCols-1;
			}
			
			if(grid[nextRowIndex][ nextColIndex] != 0) {
				IntArrayState nextPossibleState = new IntArrayState (new int[] {nextPossibleCell, t});
				IntArrayState parentState = new IntArrayState (new int[] {cell, t-1});
				
				/*
				 * Insert the best father of a state, checking if there is already inserted one with
				 * an heuristic worse then the current one
				 */
				if(P.containsKey(nextPossibleState)) {
					int insertedParentCell = P.get(nextPossibleState).getCell();
					int typeOfCost = Math.abs(insertedParentCell - nextPossibleCell);
					double insertedParentCost = 0;
					if(typeOfCost == 1 || typeOfCost == numCols) {
						insertedParentCost = h.get(nextPossibleCell) + 1;
					} else {
						insertedParentCost = h.get(nextPossibleCell) + Math.sqrt(2);
					}
					double newParentCost = h.get(nextPossibleCell) + neighborhoodCost[i];
					if(newParentCost < insertedParentCost) {
						P.put(nextPossibleState, parentState);
					}
				}
				
				if(!collisionStates.contains(nextPossibleState)) {
					double new_g = g.get(parentState) + neighborhoodCost[i];
					if(!g.containsKey(nextPossibleState)) {
						if(!Open.contains(nextPossibleState)) {
							P.put(nextPossibleState, parentState);
							Open.add(nextPossibleState);
						}
						g.put(nextPossibleState, new_g);
						double cost = new_g + h.get(nextPossibleCell);
						f.put(nextPossibleState, cost);
					} else {
						double existing_g = g.get(nextPossibleState);
						if(new_g < existing_g) {
							if(!Open.contains(nextPossibleState)) {
								P.put(nextPossibleState, parentState);
								Open.add(nextPossibleState);
							}
							g.put(nextPossibleState, new_g);
							f.put(nextPossibleState, new_g + h.get(nextPossibleCell));
						}
					}
				} 
//				else {
//					System.out.printf("\nStato (%d, %d) non aggiunto a Open perch� in conflitto con "
//							+ "un altro agente\n", nextPossibleCell, t);
//				}
			}
		}
	}
	
	private int[] getNeighborhood(int cell) {
		int numRows = grid.length;
		int numCols = grid[0].length;
		int rowIndex = 0;
		int colIndex = 0;
		if(cell % numCols != 0) {
			rowIndex = cell / numCols;
			colIndex = cell % numCols-1;
		} else {
			rowIndex = cell / grid[0].length-1;
			colIndex = grid[0].length-1;
		}
		if(rowIndex == 0 && colIndex == 0) {
			return new int[] {0, 1, numCols, (numCols+1)};
			
		} else if(rowIndex == 0 && colIndex == (numCols-1)) {
			return new int[] {0, -1, numCols, (numCols-1)};
			
		} else if(rowIndex == (numRows-1) && colIndex == 0) {
			return new int[] {0, -numCols, 1, -(numCols-1)};
			
		} else if(rowIndex == (numRows-1) && colIndex == (numCols-1)) {
			return new int[] {0, -numCols, -1, -(numCols+1)};
			
		} else if(rowIndex == 0) {
			return new int[] {0, -1, 1, numCols, (numCols-1), (numCols+1)};
			
		} else if(rowIndex == (numRows-1)) {
			return new int[] {0, -1, 1, -numCols, -(numCols+1), -(numCols-1)};
			
		} else if(colIndex == 0) {
			return new int[] {0, 1, -numCols, numCols, -(numCols-1), (numCols+1)};
			
		} else if(colIndex == (numCols-1)) {
			return new int[] {0, -1, -numCols, numCols, -(numCols+1), (numCols-1)};
			
		}
		return new int[] {0, -1, 1, -(numCols+1), -numCols, -(numCols-1), (numCols-1), numCols, (numCols+1)};
	}
	
	private double[] getNeighborhoodCost(int cell) {
		int numRows = grid.length;
		int numCols = grid[0].length;
		int rowIndex = 0;
		int colIndex = 0;
		if(cell % numCols != 0) {
			rowIndex = cell / numCols;
			colIndex = cell % numCols-1;
		} else {
			rowIndex = cell / numCols-1;
			colIndex = numCols-1;
		}
		if(rowIndex == 0 && colIndex == 0) {
			return new double[] {Utility.COST, Utility.COST, Utility.COST, Utility.SQRT_COST};
			
		} else if(rowIndex == 0 && colIndex == (numCols-1)) {
			return new double[] {Utility.COST, Utility.COST, Utility.COST, Utility.SQRT_COST};
			
		} else if(rowIndex == (numRows-1) && colIndex == 0) {
			return new double[] {Utility.COST, Utility.COST, Utility.COST, Utility.SQRT_COST};
			
		} else if(rowIndex == (numRows-1) && colIndex == (numCols-1)) {
			return new double[] {Utility.COST, Utility.COST, Utility.COST, Utility.SQRT_COST};
			
		} else if(rowIndex == 0) {
			return new double[] {Utility.COST, Utility.COST, Utility.COST, Utility.COST, Utility.SQRT_COST, Utility.SQRT_COST};
			
		} else if(rowIndex == (numRows-1)) {
			return new double[] {Utility.COST, Utility.COST, Utility.COST, Utility.COST, Utility.SQRT_COST, Utility.SQRT_COST};
			
		} else if(colIndex == 0) {
			return new double[] {Utility.COST, Utility.COST, Utility.COST, Utility.COST, Utility.SQRT_COST, Utility.SQRT_COST};
			
		} else if(colIndex == (numCols-1)) {
			return new double[] {Utility.COST, Utility.COST, Utility.COST, Utility.COST, Utility.SQRT_COST, Utility.SQRT_COST};
			
		}
		
		return new double[] {Utility.COST, Utility.COST, Utility.COST, Utility.SQRT_COST, Utility.COST, Utility.SQRT_COST, Utility.SQRT_COST, Utility.COST, Utility.SQRT_COST};
	}
	
	private List<IntArrayState> getCollisionStates(int cell, int[] neighborhood) {
		int numCols = grid[0].length;
		List<IntArrayState> collisionStates = new ArrayList<>();
		IntArrayState sameNextState = new IntArrayState (new int[] {cell, t});
		IntArrayState state = new IntArrayState(new int[2]);
		IntArrayState state1 = new IntArrayState(new int[2]);
		IntArrayState state2 = new IntArrayState(new int[2]);
		IntArrayState state3 = new IntArrayState(new int[2]);
		
		for(List<IntArrayState> agentPath: existingAgentsPaths) {
			for(int i=0; i < neighborhood.length; i++) {
				if(neighborhood[i] == 0) {
					if(agentPath.contains(sameNextState)) {
						collisionStates.add(sameNextState);
					}
					
				} else if(neighborhood[i] == -1) {
					state.setState(new int[] {cell-1, t});
					state1.setState(new int[] {cell-1, t-1});
					if(agentPath.contains(state) || (agentPath.contains(state1) && agentPath.contains(sameNextState))) {
						collisionStates.add(state);
					}
					
				} else if(neighborhood[i] == 1) {
					state.setState(new int[] {cell+1, t});
					state1.setState(new int[] {cell+1, t-1});
					if(agentPath.contains(state) || (agentPath.contains(state1) && agentPath.contains(sameNextState))) {
						collisionStates.add(state);
					}
					
				} else if(neighborhood[i] == -numCols) {
					state.setState(new int[] {cell-numCols, t});
					state1.setState(new int[] {cell-numCols, t-1});
					if(agentPath.contains(state) || (agentPath.contains(state1) && agentPath.contains(sameNextState))) {
						collisionStates.add(state);
					}
					
				} else if(neighborhood[i] == numCols) {
					state.setState(new int[] {cell+numCols, t});
					state.setState(new int[] {cell+numCols, t-1});
					if(agentPath.contains(state) || (agentPath.contains(state1) && agentPath.contains(sameNextState))) {
						collisionStates.add(state);
					}
					
				} else if(neighborhood[i] == -(numCols+1)) {
					state.setState(new int[] {cell-(numCols+1), t});
					state1.setState(new int[] {cell-(numCols+1), t-1});
					state2.setState(new int[] {cell-1, t-1});
					state3.setState(new int[] {cell-numCols, t});
					if(agentPath.contains(state) || 
						(agentPath.contains(state1) && agentPath.contains(sameNextState)) ||
						(agentPath.contains(state2) && agentPath.contains(state3))) {
							collisionStates.add(state);
					}
					
				} else if(neighborhood[i] == -(numCols-1)) {
					state.setState(new int[] {cell-(numCols-1), t});
					state1.setState(new int[] {cell-(numCols-1), t-1});
					state2.setState(new int[] {cell-numCols, t-1});
					state3.setState(new int[] {cell+1, t});
					
					if(agentPath.contains(state) || 
						(agentPath.contains(state1) && agentPath.contains(sameNextState)) ||
						(agentPath.contains(state2) && agentPath.contains(state3))) {
							collisionStates.add(state);
					}
					
				} else if(neighborhood[i] == (numCols-1)) {
					state.setState(new int[] {cell+(numCols-1), t});
					state1.setState(new int[] {cell+(numCols-1), t-1});
					state2.setState(new int[] {cell-1, t-1});
					state3.setState(new int[] {cell+numCols, t});
					if(agentPath.contains(state) || 
						(agentPath.contains(state1) && agentPath.contains(sameNextState)) ||
						(agentPath.contains(state2) && agentPath.contains(state3))) {
							collisionStates.add(state);
					}
					
				} else /*(numCols+1)*/ {
					state.setState(new int[] {cell+(numCols+1), t});
					state1.setState(new int[] {cell+(numCols+1), t-1});
					state2.setState(new int[] {cell+numCols, t-1});
					state3.setState(new int[] {cell+1, t});
					if(agentPath.contains(state) || 
						(agentPath.contains(state1) && agentPath.contains(sameNextState)) ||
						(agentPath.contains(state2) && agentPath.contains(state3))) {
							collisionStates.add(state);
					}
				}
			}
			
			if(t >= agentPath.get(0).getIstant()) {
				h.put(agentPath.get(0).getCell(), Double.MAX_VALUE);
			}
		}
		return collisionStates;
	}
	
	private int extractStateWithMinCostFromOpen() {
		IntArrayState minCostState = new IntArrayState(new int[2]);
	    double minValue = Double.MAX_VALUE;
		
		for (IntArrayState state : Open) {
            if (f.get(state) < minValue) {
                minValue = f.get(state);
                minCostState = state;
            }
        }
		
		if(minValue == Double.MAX_VALUE) {
			return 0;
		}
		
//		System.out.println("Min Cost State in Open: (" + minCostState.getCell() + ", " + minCostState.getIstant() + ")");
		t = minCostState.getIstant() + 1;
		Open.remove(minCostState);
		Closed.add(minCostState);
		
		return minCostState.getCell();
	}
	
	public List<IntArrayState> reconstructPath() {
		List<IntArrayState> path = new ArrayList<>();
		IntArrayState state = new IntArrayState (new int[] {goal, t});
		IntArrayState initial = new IntArrayState (new int[] {init, 0});
		path.add(state);
		
		while(!state.equals(initial)) {
			state = P.get(state);
			path.add(state);
		}
		return path;
	}
	
	public boolean getSuccessful() {
		return successful;
	}
	

}
