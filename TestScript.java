import assets.*;

import java.util.*;

public class TestScript {

	public static void main (String args[]) {
		
		// Set up the map and gps
		CityMap mapA = new CityMap();
		GPS gps = new GPS();
		// List in java.util and java.awt conflicts
		ArrayList<java.util.List<Station>> partialNetworks =  mapA.processPartialNetwork(true);
		ArrayList<java.util.List<Station>> fullPaths = mapA.processFullNetwork(partialNetworks, 5, false);
		

		// Restrictions
		// 1. No loops!
		// 2. All selected stations must be based on the map, fooling around will cause the program to fail/give strange output
		
		// Rules
		// 1. station names(start and destination) specified in stationNames.add() and mapA.showAvailablePaths() must match ie the same! 
		ArrayList<Station> stationNames = new ArrayList<Station>(); // this part is based on the user input, hard-code for the moment :)
		stationNames.add( mapA.stationDictionary.get("C") );
		stationNames.add( mapA.stationDictionary.get("W2") );
		stationNames.add( mapA.stationDictionary.get("H3") );
		stationNames.add( mapA.stationDictionary.get("F1") ); // these 4 are actual valid paths
		//stationNames.add( mapA.stationDictionary.get("C") );
		
		ArrayList<java.util.List<Station>> availablePaths;

		// CSP
		availablePaths = mapA.showAvailablePaths("C", "F1", fullPaths, false, 10, false);
		Station[] chosenPath_CSP = gps.findChosenPath(availablePaths, stationNames, 10);

		if (chosenPath_CSP != null) {
			System.out.println("Showing the path to use (CSP)...");
			for (int i=0; i <= chosenPath_CSP.length - 1; i++) {
				System.out.print(chosenPath_CSP[i]);
				if (i != chosenPath_CSP.length - 1) {
					System.out.print(", ");
				}
			}
		}
		System.out.println("\n");
		
		// COP, 
		// limitations: 1. One way only, 
		// 				2. Cannot be a path that intersects C, it will end up with a reverse and a normal path, will give error
		// Reason: we're using a HashMap for speed, after all everything will start from C :) Unless the bridges are created manually
		availablePaths = mapA.showAvailablePaths("H4", "A1", fullPaths, false, 10, false);
		Station[] chosenPath_COP = gps.findChosenPath(availablePaths, "H4", "A1");
		
		System.out.println("Showing the shortest path(COP):");
		for (int i=0; i<chosenPath_COP.length; i++) {
			System.out.print(chosenPath_COP[i].name);
			if (i != chosenPath_COP.length - 1) {
				System.out.print(", ");
			}
		}
		System.out.println("");
		
		availablePaths = mapA.showAvailablePaths("C", "A1", fullPaths, false, 10, false);
		chosenPath_COP = gps.findChosenPath(availablePaths, "C", "A1");
		
		System.out.println("Showing the shortest path(COP):");
		for (int i=0; i<chosenPath_COP.length; i++) {
			System.out.print(chosenPath_COP[i].name);
			if (i != chosenPath_COP.length - 1) {
				System.out.print(", ");
			}
		}
		System.out.println("");
		
		// GUI
		Screen GPS_Screen = new Screen();
		GPS_Screen.sendMap(mapA);
		GPS_Screen.renderGraphics(300, 100, 690, 530); // only stations connected using CityMap::connectBridges will be drawn
		
	}
	
	// these 2 tests can be written in a more efficient way :)
	private static void test_stationDictionary(CityMap map, String lookup_name) {
		System.out.println("Testing . . .");
		// will give null if the name is wrong
		System.out.println(lookup_name + ": " + map.stationDictionary.get(lookup_name) + "\n");
	}
	
	private static void test_stationDictionary(CityMap map, String[] lookup_names) {
		System.out.println("Testing . . .");
		// will give null if the name is wrong
		for (int i=0; i<lookup_names.length; i++) {
			System.out.println(lookup_names[i] + ": " + map.stationDictionary.get(lookup_names[i]));
		}
		System.out.println("Done! null values indicate non-existing station names");
	}
}
