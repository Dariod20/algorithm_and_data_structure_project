package it.unibs.alg.pathfinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReachGoal {
	
	private int[] G;
	private Map<String, Double> wG;
	private String init;
	private String goal;
	private int max;
	
	private int t = 0;
	
	private int[] stateIstant = new int[2];
	private List<int[]> Open = new ArrayList<>();
	private List<int[]> Closed = new ArrayList<>();
	private Map<int[], Integer> g = new HashMap<>();
	private Map<int[], Integer> P = new HashMap<>();
	private Map<Integer, Integer> h = new HashMap<>();
	
	public ReachGoal(int[] G, Map<String, Double> wG, /*percorsi agenti preesistenti,*/ String init, String goal, int max) {
		this.G = G;
		this.wG = wG;
		this.init = init;
		this.goal = goal;
		this.max = max;
		
		inizializeData();
	}
	
	private void inizializeData() {
		stateIstant[0] = Integer.parseInt(init);
		stateIstant[1] = t;
		Open.add(stateIstant);
		
		for(int t=0; t<=max; t++) {
			for(int i=0; i < G.length; i++) {
				stateIstant[0] = G[i];
				stateIstant[1] = t;
				g.put(stateIstant, 10000);
				P.put(null, null);
			}
		}
		
		stateIstant[0] = Integer.parseInt(init);
		stateIstant[1] = t;
		g.put(stateIstant, 0);
//		valore f da init a goal, ma serve davvero??
		
	}
	
	
	
	

}
