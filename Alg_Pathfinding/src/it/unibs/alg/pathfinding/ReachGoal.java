package it.unibs.alg.pathfinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class ReachGoal {
	
	private int[] G;
	private Map<String, Double> wG;
	private List<List<IntArrayKey>> existingAgentsPaths;
	private String init;
	private String goal;
	private int max;
	
	private int t = 0;
	private int step = 0;
	private List<IntArrayKey> Open = new ArrayList<>();
	private List<IntArrayKey> Closed = new ArrayList<>();
	private Map<IntArrayKey, Double> g = new HashMap<>();
	private Map<IntArrayKey, Double> f = new HashMap<>();
	private Map<IntArrayKey, IntArrayKey> P = new HashMap<>();
	private Map<Integer, Double> h = new HashMap<>();
	
	public ReachGoal(int[] G, Map<String, Double> wG, List<List<IntArrayKey>> existingAgentsPaths,
			String init, String goal, int max) {
		this.G = G;
		this.wG = wG;
		this.existingAgentsPaths = existingAgentsPaths;
		this.init = init;
		this.goal = goal;
		this.max = max;
	}
	
	public void runReachGoal() {
		int initial = Integer.parseInt(init);
		executeFirstTwoSteps(initial);
		Integer nextNearState = extractStateWithMinCostFromOpen();
		
		while(!Open.isEmpty()) {
			if(t <= max) {
				if(nextNearState == 0) {
					System.out.println("\nFrom initial position " + init + " is impossible to reach the goal in position " + goal);
					break;
				}
				String nextState = nextNearState.toString();
				if(!nextState.equals(goal)) {
					System.out.println("\nOpen in step " + step++);
					addStatesToOpenAndComputeTheirCost(nextState);
					for(IntArrayKey state: Open) {
						double cost = f.get(new IntArrayKey (new int[] {state.getKey()[0], state.getKey()[1]}));
						System.out.printf("(%d, %d) -> %.2f\n", state.getKey()[0], state.getKey()[1], cost);
					}
					nextNearState = extractStateWithMinCostFromOpen();
				} else {
					t--;
					System.out.println("\n\nGOAL REACHED!!");
					System.out.print("\nClosed list: ");
					for(IntArrayKey state: Closed) {
						System.out.print("(" + state.getKey()[0] + ", " + state.getKey()[1] + "), ");
					}
					System.out.println("\n\nNumero di mosse impiegato: " + t);
					System.out.printf("\nCosto totale: %.2f\n", f.get(new IntArrayKey(new int[] {nextNearState, t})));
					break;
				}
			} else {
				System.out.println("\nIt takes too long to reach the goal: t = " + t + " has exceeded max = " + max);
				break;
			}
		}
		
		if(Open.isEmpty()) {
			System.out.println("\nOpen list is empty: impossibile to reach the goal!");
		}
		
	}
	
	private void executeFirstTwoSteps(int initial) {
		Open.add(new IntArrayKey (new int[] {initial, 0}));
		
		HeuristicAlgorithm heuritic = new HeuristicAlgorithm(G, wG, goal);
		h = heuritic.getH();
		
//		for(Entry<Integer, Double> entry : h.entrySet()) {
//            System.out.println("State: " + entry.getKey() + ", Cost: " + entry.getValue());
//		}
		
//		f.put(Open.get(0), h.get(initial));
		System.out.println("Open in step " + step++);
		System.out.printf("(%d, %d) -> %.2f\n", initial, t, h.get(initial));
		
		t++;
		System.out.println("\nOpen in step " + step++);
		addFirstStatesToOpenAndComputeTheirCost(initial);
	}
	
	private void addFirstStatesToOpenAndComputeTheirCost(int initial) {
		for(Entry<String, Double> entry : wG.entrySet()) {
			if(entry.getKey().endsWith("_" + init)) {
				int nearState = Integer.parseInt(entry.getKey().split("_")[0]);
				IntArrayKey stateIstant = new IntArrayKey (new int[] {nearState, t});
				boolean collision = false;
				for(List<IntArrayKey> otherAgentPath: existingAgentsPaths) {
					if(otherAgentPath.contains(stateIstant)) {
						collision = true;
					}
					if(t == otherAgentPath.get(otherAgentPath.size()-1).getKey()[1]) {
						h.put(otherAgentPath.get(otherAgentPath.size()-1).getKey()[0], Double.MAX_VALUE);
					}
				}
					
				if(!collision) {
					P.put(stateIstant, new IntArrayKey (new int[] {initial, 0}));
					Open.add(stateIstant);
					g.put(stateIstant, entry.getValue());
					double cost = entry.getValue() + h.get(nearState);
					f.put(stateIstant, cost);
					System.out.printf("(%d, %d) -> %.2f\n", nearState, t, cost);
				} else {
					System.out.printf("\nStato (%d, %d) non aggiunto a Open perchè in conflitto con "
							+ "un altro agente", nearState, t);
				}
			}
		}
		Open.remove(new IntArrayKey (new int[] {initial, 0}));
		Closed.add(new IntArrayKey (new int[] {initial, 0}));
	}
	
	private void addStatesToOpenAndComputeTheirCost(String state) {
		for(Entry<String, Double> entry : wG.entrySet()) {
			if(entry.getKey().endsWith("_" + state)) {
				int nearState = Integer.parseInt(entry.getKey().split("_")[0]);
				int s = Integer.parseInt(state);
				IntArrayKey stateIstant = new IntArrayKey (new int[] {nearState, t});
				
				/*
				 * Insert the best father of a state, checking if there is already inserted one with
				 * an heuristic worse then the current one
				 */
				if(P.containsKey(stateIstant) && h.get(nearState) < h.get(P.get(stateIstant).getKey()[0])) {
					P.put(stateIstant, new IntArrayKey (new int[] {s, t-1}));
				}
				
				boolean collision = false;
				for(List<IntArrayKey> otherAgentPath: existingAgentsPaths) {
					if(otherAgentPath.contains(stateIstant)) {
						collision = true;
					}
					if(t == otherAgentPath.get(otherAgentPath.size()-1).getKey()[1]) {
						h.put(otherAgentPath.get(otherAgentPath.size()-1).getKey()[0], Double.MAX_VALUE);
					}
				}
				
				if(!collision) {
					double new_g = g.get(new IntArrayKey (new int[] {s, t-1})) + entry.getValue();
					if(!g.containsKey(stateIstant)) {
						if(!Open.contains(stateIstant)) {
							P.put(stateIstant, new IntArrayKey (new int[] {s, t-1}));
							Open.add(stateIstant);
						}
						g.put(stateIstant, new_g);
						double cost = new_g + h.get(nearState);
						f.put(stateIstant, cost);
					} else {
						double existing_g = g.get(stateIstant);
						if(new_g < existing_g) {
							if(!Open.contains(stateIstant)) {
								P.put(stateIstant, new IntArrayKey (new int[] {s, t-1}));
								Open.add(stateIstant);
							}
							g.put(stateIstant, new_g);
							double cost = new_g + h.get(nearState);
							f.put(stateIstant, cost);
						}
					}
				} else {
					System.out.printf("\nStato (%d, %d) non aggiunto a Open perchè in conflitto con "
							+ "un altro agente", nearState, t);
				}
			}
		}
	}
	
	private int extractStateWithMinCostFromOpen() {
		IntArrayKey minCostState = new IntArrayKey(new int [2]);
	    double minValue = Double.MAX_VALUE;
		
		for (IntArrayKey arrayKey : Open) {
            if (f.get(arrayKey) < minValue) {
                minValue = f.get(arrayKey);
                minCostState = arrayKey;
            }
        }
		
		if(minValue == Double.MAX_VALUE) {
			return 0;
		}
		
		System.out.println("Min Cost State in Open: (" + minCostState.getKey()[0] + ", " + minCostState.getKey()[1] + ")");
		t = minCostState.getKey()[1] + 1;
		Open.remove(minCostState);
		Closed.add(minCostState);
		
		return minCostState.getKey()[0];
	}
	
	public List<IntArrayKey> reconstructPath() {
		List<IntArrayKey> path = new ArrayList<>();
		IntArrayKey stateIstant = new IntArrayKey (new int[] {Integer.parseInt(goal), t});
		IntArrayKey initial = new IntArrayKey (new int[] {Integer.parseInt(init), 0});
		path.add(stateIstant);
		
		while(!stateIstant.equals(initial)) {
			stateIstant = P.get(stateIstant);
			path.add(stateIstant);
		}
		
		System.out.print("\nPath: ");
		for(int i=path.size()-1; i >= 0; i--) {
			System.out.printf("(%d, %d) -> ", path.get(i).getKey()[0], path.get(i).getKey()[1]);
		}
		return path;
	}
	

}
