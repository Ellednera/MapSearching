import java.util.*;
import assets.*;

public class COPBenchmark {

	public static void main(String[] args) {
		// Set up the map and gps
		CityMap mapA = new CityMap();
		GPS gps = new GPS();
		// List in java.util and java.awt conflicts
		ArrayList<java.util.List<Station>> partialNetworks =  mapA.processPartialNetwork(false);
		ArrayList<java.util.List<Station>> fullPaths = mapA.processFullNetwork(partialNetworks, 5, false); // don't changed the number 5, allowed to increase it but not too much
		
		// get data from the gui
		int constraintData = 100; // actual capacity or the weight
		
		ArrayList<Station> stationNames = new ArrayList<Station>(); // this part is based on the user input, hard-code for the moment :)
		stationNames.add( mapA.stationDictionary.get("C") );
		stationNames.add( mapA.stationDictionary.get("A1") );
		
		ArrayList<java.util.List<Station>> availablePaths;

		String start = "CW";
		String destination = "A1";
		availablePaths = mapA.showAvailablePaths(start, destination, fullPaths, false, constraintData, false);

		// https://stackoverflow.com/questions/2572868/how-to-time-java-program-execution-speed
		System.out.println("COP Benchmarking started...");
		final long startTime = System.currentTimeMillis();
				
		for (int i=1; i<=5000000; i++) { // 5 million times 
			gps.findChosenPath(availablePaths, start, destination);
		}
		
		final long endTime = System.currentTimeMillis();

		System.out.println("COP Benchmarking done!");
		System.out.println("Total execution time: " + (endTime - startTime) + " miliseconds");

	}

}
