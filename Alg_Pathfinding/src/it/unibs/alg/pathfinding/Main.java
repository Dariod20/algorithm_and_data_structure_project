package it.unibs.alg.pathfinding;

public class Main {

	public static void main(String[] args) {
		
		long start = System.currentTimeMillis();
		
		Controller controller = new Controller();
		controller.startAlgorithm();
        
		long end = System.currentTimeMillis();
		long milliDuration = end - start;

		Utility.writeOnFile("\nTempo di esecuzione: " + milliDuration + " millisecondi\n\n");
		
		System.out.println("\nDurata: " + milliDuration + " millisecondi");
		System.out.println("\nGli output prodotti sono stati salvati sul file 'Output.txt'");

	}

}
