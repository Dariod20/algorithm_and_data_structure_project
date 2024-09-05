package it.unibs.alg.pathfinding;

public class Main {
	
	private static final int BYTE_ORDER = 1000000;
	private static final String BYTE_PREF = "mega";

	public static void main(String[] args) {
		
		long start = System.currentTimeMillis();
		
		Controller controller = new Controller();
		controller.startAlgorithm();
		
		Runtime runtime = Runtime.getRuntime();

        // Get the total heap size (allocated memory)
        long heapSize = runtime.totalMemory();

        // Get the maximum heap size (if set by the JVM)
        long maxHeapSize = runtime.maxMemory();

        // Get the free memory within the heap
        long freeMemory = runtime.freeMemory();
        
        Utility.writeOnFile("Heap Size corrente (memoria occupata): " + String.format("%.3f", ((double)heapSize/BYTE_ORDER))
        	+ " " + BYTE_PREF + "bytes\n");
        Utility.writeOnFile("Heap Size massimo (memoria allocabile totale): " + String.format("%.3f", ((double)maxHeapSize/BYTE_ORDER))
        	+ " " + BYTE_PREF + "bytes\n");
        Utility.writeOnFile("Memoria libera: " + String.format("%.3f", ((double)freeMemory/BYTE_ORDER)) 
        	+ " " + BYTE_PREF + "bytes\n");
		
		long end = System.currentTimeMillis();
		long milliDuration = end - start;

		Utility.writeOnFile("\nTempo di esecuzione: ");
		Utility.writeOnFile(milliDuration + " millisecondi\n\n");
		
		
		System.out.println("Heap Size corrente (memoria occupata): " + String.format("%.3f", ((double)heapSize/BYTE_ORDER))
			+ " " + BYTE_PREF + "bytes\n");
		System.out.println("Heap Size massimo (memoria allocabile totale): " + String.format("%.3f", ((double)maxHeapSize/BYTE_ORDER))
			+ " " + BYTE_PREF + "bytes\n");
		System.out.println("Memoria libera: " + String.format("%.3f", ((double)freeMemory/BYTE_ORDER))
			+ " " + BYTE_PREF + "bytes\n");
		
//		int duration = Math.round(milliDuration/1000);
//		System.out.println("\nDurata: " + duration + " secondi");
		System.out.println("\nDurata: " + milliDuration + " millisecondi");

	}

}
