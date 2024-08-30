package it.unibs.alg.pathfinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class ReachGoal {
	
	private int[] G;
	private Map<String, Double> wG;
	private String init;
	private String goal;
	private int max;
	
	private int t = 0;
	private int step = 0;
	
	private List<IntArrayKey> Open = new ArrayList<>();
	private List<IntArrayKey> Closed = new ArrayList<>();
	private Map<IntArrayKey, Double> g = new HashMap<>();
	private Map<IntArrayKey, Double> f = new HashMap<>();
	private Map<IntArrayKey, Integer> P = new HashMap<>();
	private Map<Integer, Double> h = new HashMap<>();
	
	public ReachGoal(int[] G, Map<String, Double> wG, /*percorsi agenti preesistenti,*/ String init, String goal, int max) {
		this.G = G;
		this.wG = wG;
		this.init = init;
		this.goal = goal;
		this.max = max;
	}
	
	public void runReachGoal() {
		int initial = Integer.parseInt(init);
		System.out.println("Open in step " + step++);
		inizializeData(initial);
		t++;
		System.out.println("\nOpen in step " + step++);
		addFirstStatesToOpenAndComputeTheirCost(initial);
		Integer nextNearState = extractStateWithMinCostFromOpen();
		boolean isGoal = false;
		
		while(!Open.isEmpty() && !isGoal) {
			t++;
			String nextState = nextNearState.toString();
			if(!nextState.equals(goal)) {
				System.out.println("\nOpen in step " + step++);
				addStatesToOpenAndComputeTheirCost(nextState);
				nextNearState = extractStateWithMinCostFromOpen();
			} else {
				isGoal = true;
			}
		}
		t--;
		System.out.println("\nNumero di mosse impiegato: " + t);
		System.out.println("Costo totale: " + f.get(new IntArrayKey(new int[] {nextNearState, t})));
	}
	
	private void inizializeData(int initial) {
		Open.add(new IntArrayKey (new int[] {initial, t}));
		
		for(int t=0; t<=max; t++) {
			for(int i=0; i < G.length; i++) {
				g.put(new IntArrayKey (new int[] {G[i], t}), Double.MAX_VALUE);
				f.put(new IntArrayKey (new int[] {G[i], t}), Double.MAX_VALUE);
				P.put(null, null);
			}
		}
		
		HeuristicAlgorithm heuritic = new HeuristicAlgorithm(G, wG, goal);
		h = heuritic.getH();
		
//		f.put(Open.get(0), h.get(initial));
		System.out.println("(" + initial + ", " + t + ") -> " + h.get(initial));
		
//		for(Entry<Integer, Double> entry : h.entrySet()) {
//            System.out.println("State: " + entry.getKey() + ", Cost: " + entry.getValue());
//		}
	}
	
	private void addFirstStatesToOpenAndComputeTheirCost(int initial) {
		for(Entry<String, Double> entry : wG.entrySet()) {
			if(entry.getKey().endsWith("_" + init)) {
				int nearState = Integer.parseInt(entry.getKey().split("_")[0]);
				Open.add(new IntArrayKey (new int[] {nearState, t}));
				g.put(new IntArrayKey(new int[] {nearState, t}), entry.getValue());
				double cost = entry.getValue() + h.get(nearState);
				f.put(new IntArrayKey (new int[] {nearState, t}), cost);
				System.out.println("(" + nearState + ", " + t + ") -> " + cost);
			}
		}
		Open.remove(new IntArrayKey (new int[] {initial, 0}));
		Closed.add(new IntArrayKey (new int[] {initial, 0}));
	}
	
	private void addStatesToOpenAndComputeTheirCost(String state) {
		for(Entry<String, Double> entry : wG.entrySet()) {
			if(entry.getKey().endsWith("_" + state)) {
				int nearState = Integer.parseInt(entry.getKey().split("_")[0]);
				
				// se l'euristica è 0, stato goal non raggiungibile
				
				int s = Integer.parseInt(state);
				double existing_g = g.get(new IntArrayKey (new int[] {nearState, t}));
				double new_g = g.get(new IntArrayKey (new int[] {s, t-1})) + entry.getValue();
				if(new_g < existing_g) {
					Open.add(new IntArrayKey (new int[] {nearState, t}));
					g.put(new IntArrayKey (new int[] {nearState, t}), new_g);
					double cost = new_g + h.get(nearState);
					f.put(new IntArrayKey (new int[] {nearState, t}), cost);
					System.out.println("(" + nearState + ", " + t + ") -> " + cost);
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
		
		System.out.println("Min Cost State in Open: (" + minCostState.getKey()[0] + ", " + minCostState.getKey()[1] + ")");
		Open.remove(minCostState);
		Closed.add(minCostState);
		
		return minCostState.getKey()[0];
	}
	
	

}
