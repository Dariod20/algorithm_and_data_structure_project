package it.unibs.alg.pathfinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class HeuristicAlgorithm {
	
	private Map<String, Double> wG;	
	private String goal;
	private Map<Integer, Double> h = new HashMap<>();
	
	public HeuristicAlgorithm(int[] G, Map<String, Double> wG, String goal) {
		this.goal = goal;
		this.wG = wG;
		
//		allStates.remove(this.goal);
		
		for(int i=0; i < G.length; i++) {
			h.put(G[i], 0.0);
		}
		
		computeHeuristic(goal);
	}
	
	private void computeHeuristic(String state) {
		List<String> adjacentEdges = wG.keySet().stream()
				//se non si considera ilcosto del goal per capire che si è raggiunto il goal, 
				//il secondo controllo è rimovibile
	            .filter(key -> key.endsWith("_" + state) && !key.startsWith(goal))
	            .collect(Collectors.toList());
		
		adjacentEdges.remove(state + "_" + state); 
		
		List<String> adjacentStates = new ArrayList<>();
		
		for(String e: adjacentEdges) {
			double cost = h.get(Integer.parseInt(state)) + wG.get(e);
			String adjCurrentState = e.split("_")[0];
			int currentState = Integer.parseInt(adjCurrentState);
			double insertedCost = h.get(currentState);
			
			if(insertedCost == 0 || insertedCost > cost) {
				adjacentStates.add(adjCurrentState);
				h.put(currentState, cost);
			}
		}
		
		for(String s: adjacentStates) {
			computeHeuristic(s);
		}
	}
	
	public Map<Integer, Double> getH() {
		return h;
	}

}
