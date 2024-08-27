package it.unibs.alg.pathfinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class HeuristicAlgorithm {
	
//	private List<Integer> allStates;
	private Map<String, Double> wG;
	private String goal;
	
	private Map<Integer, Double> h = new HashMap<>();
	
	public HeuristicAlgorithm(Map<String, Double> wG, String goal) {
		this.wG = wG;
		this.goal = goal;
		
//		for(int i=0; i < G.length; i++) {
//			allStates.add(G[i]);
//		}
//		allStates.remove(Integer.parseInt(goal));
		
		computeHeuristic(goal);
	}
	
	private void computeHeuristic(String state) {
		List<String> adjacentEdges = wG.keySet().stream()
	            .filter(key -> key.endsWith(state))
	            .collect(Collectors.toList());
		
		List<String> adjacentStates = new ArrayList<>();
		
		for(String e: adjacentEdges) {
			double cost = 0;
			if(!state.equals(goal)) {
				cost = h.get(Integer.parseInt(state)) + wG.get(e);
			} else {
				cost = wG.get(e);
			}
			//da sistemare
			String adjCurrentState = e.split(state)[0];
			int currentState = Integer.parseInt(adjCurrentState);
			if(!h.containsKey(currentState)) {
				adjacentStates.add(adjCurrentState);
				h.put(currentState, cost);
			} else {
				if(h.get(currentState) > cost) {
					adjacentStates.add(adjCurrentState);
					h.put(currentState, cost);
				}
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
