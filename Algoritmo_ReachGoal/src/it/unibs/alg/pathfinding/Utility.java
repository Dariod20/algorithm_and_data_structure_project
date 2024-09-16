package it.unibs.alg.pathfinding;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Utility {
	
	public static final int COST = 1;
	public static final double SQRT_COST = Math.sqrt(2);
	public static final String INPUT_FILE = "Input.txt";
	public static final String OUTPUT_FILE = "Output.txt";

	/*
	 * Read the 'Input.txt' lines and return the list of the inputs
	 */
	public static List<String> readInputs() {
		BufferedReader input = null;
		List<String> inputs = new ArrayList<>();
		try {
			input = new BufferedReader(new FileReader(INPUT_FILE));
			List<String> lines = input.lines().collect(Collectors.toList());
			for(int i=0; i < lines.size(); i++) {
				 String dataLine[] = lines.get(i).split("\s+");
				 if(dataLine.length == 2) {
					 if(dataLine[0].equals("griglia_manuale") || dataLine[0].equals("agenti_manuali")) {
						 if(dataLine[1].equals("true") || dataLine[1].equals("false")) {
							 inputs.add(dataLine[1]);
						 } else {
							 System.out.println("\nATTENZIONE: i parametri 'griglia_manuale' e 'agenti_manuali' devono essere di tipo boolean.\n");
								System.exit(1);
						 }
					 } else {
							inputs.add(dataLine[1]);
						}
				 } else {
					 System.out.println("\nATTENZIONE: i parametri d'ingresso del file " + INPUT_FILE + " non rispettano "
					 		+ "il formato presatbilito 'TIPO_INPUT VALORE'. Effettuare un controllo e riprovare.\n");
					 System.exit(1);
				 }
			}
        }
		catch (FileNotFoundException e) {
			System.out.println("\nATTENZIONE: il file " + INPUT_FILE + " non esiste o il suo percorso è sbagliato.\n");
			System.exit(1);
		} finally {
        	try {
				input.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
		return inputs;
	}
	
	public static void writeOnFile(String text) {
		PrintWriter pw = null;
			try {
				pw = new PrintWriter(new FileOutputStream(new File(OUTPUT_FILE), true));
				pw.append(text);
			} catch (FileNotFoundException e) {
				System.out.println("\nATTENZIONE: il file " + OUTPUT_FILE +" non esiste.\n");
				System.exit(1);
			} finally {
				if(pw != null) {
					pw.close();
				}
			}
	}
	
	public static boolean fileExists() {
		File file = new File(OUTPUT_FILE);
		if(file.exists()) {
			return true;
		}
		return false;
	}
	
	public static void deleteFile() {
		File fileOld = new File(OUTPUT_FILE);
		fileOld.delete();
	}

}
