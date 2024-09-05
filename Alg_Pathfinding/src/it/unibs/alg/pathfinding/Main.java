package it.unibs.alg.pathfinding;

public class Main {
	
	public static void main(String[] args) {
		
		long start = System.currentTimeMillis();
		
		Controller controller = new Controller();
		controller.startAlgorithm();
		
		long end = System.currentTimeMillis();
		long milliDuration = end - start;
//		int duration = Math.round(milliDuration/1000);
//		System.out.println("\nDurata: " + duration + " secondi\n\n\n");
		System.out.println("\nDurata: " + milliDuration + " millisecondi\n\n\n");

	}

}
