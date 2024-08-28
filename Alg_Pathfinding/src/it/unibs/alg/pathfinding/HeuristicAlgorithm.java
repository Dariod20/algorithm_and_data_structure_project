package it.unibs.alg.pathfinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class HeuristicAlgorithm {
	
	private List<String> allStates;
	private Map<String, Double> wG;
	private String goal;
	
	private Map<Integer, Double> h = new HashMap<>();
	
	public HeuristicAlgorithm(List<String> allStates, Map<String, Double> wG, String goal) {
		this.allStates = allStates;
		this.wG = wG;
		this.goal = goal;
		
		this.allStates.remove(goal);
		
		computeHeuristic(goal);
	}
	
	private void computeHeuristic(String state) {
		List<String> adjacentEdges = wG.keySet().stream()
	            .filter(key -> key.endsWith("_" + state))
	            .collect(Collectors.toList());
		
		adjacentEdges.remove(state + "_" + state); 
		
		List<String> adjacentStates = new ArrayList<>();
		
		for(String e: adjacentEdges) {
			double cost = 0;
			if(!state.equals(goal)) {
				cost = h.get(Integer.parseInt(state)) + wG.get(e);
			} else {
				cost = wG.get(e);
			}
			
			String adjCurrentState = e.split("_")[0];
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
			
//			String[] splitting = e.split(state);
//			String adjCurrentState = splitting[0];
//			if(splitting.length == 1 && allStates.contains(adjCurrentState)) {
//				int currentState = Integer.parseInt(adjCurrentState);
//				if(!h.containsKey(currentState)) {
//					adjacentStates.add(adjCurrentState);
//					h.put(currentState, cost);
//				} else {
//					if(h.get(currentState) > cost) {
//						adjacentStates.add(adjCurrentState);
//						h.put(currentState, cost);
//					}
//				}
//			}
		}
		
		for(String s: adjacentStates) {
			computeHeuristic(s);
		}
	}
	
	public Map<Integer, Double> getH() {
		return h;
	}

}
