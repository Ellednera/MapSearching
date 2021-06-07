import assets.*;

import java.util.*;

public class TestScript {

	public static void main (String args[]) {
		
		// Set up the map and gps
		CityMap mapA = new CityMap();
		GPS gps = new GPS();
		// List in java.util and java.awt conflicts
		ArrayList<java.util.List<Station>> partialNetworks =  mapA.processPartialNetwork(true);
		ArrayList<java.util.List<Station>> fullPaths = mapA.processFullNetwork(partialNetworks, 5, false); // don't changed the number 5, allowed to increase it but not too much

		// Show GUI after processing the full paths
		Screen GPS_Screen = new Screen();
		GPS_Screen.sendMap(mapA);
		GPS_Screen.renderGraphics(); // only stations connected using CityMap::connectBridges will be drawn
				
		System.out.println("Screen rendering complete!"); // remove this if necessary
		
		// wait for the details in gui to be filled
		// the screen is instance use only, so we need to terminate everything for every search :)
		while (!GPS_Screen.infoIsAssumedComplete) {
			try {
				System.out.println("Screen info is not complete, waiting for another 5 seconds...");
				Thread.sleep(5000);
			} catch (Exception e) {
				break;
			}
		}
		
		System.out.println("Screen info is complete, procedding to the algorithm\n"); // remove if necessary
		
		// get data from the gui
		// do this
		Object[] guiData = GPS_Screen.getAllScreenInput();
		for (int i=0; i<guiData.length; i++) {
			System.out.println("Index: " + i + "=" + guiData[i]);
		}
		
		// or these 4 steps
		int constraintData = GPS_Screen.getConstraintData(); // actual capacity or the weight 
		String constraintChoice = GPS_Screen.getConstraintChoice().toLowerCase(); // Either "Capacity" or "Weight"
		String selectedAlgorithm = GPS_Screen.getSelectedAlgorithm(); // Either "CSP" or "COP"
		ArrayList<Station> stationNames = GPS_Screen.getSelectedStations();
		
		// System.out.println("Obtained constraint data without hitting the enter key: " + constraintData);
		// System.exit(0);
		
		// getting everything ready
		ArrayList<java.util.List<Station>> availablePaths;

		String start = stationNames.get(0).name;
		String destination = stationNames.get( stationNames.size()-1 ).name;
		availablePaths = mapA.showAvailablePaths(start, destination, fullPaths, false, constraintData, false); // 10 is the capacity
		
		// start routing based on the algorithm chosen
		if (selectedAlgorithm == "CSP") {
			// CSP
			
			Station[] chosenPath_CSP = gps.findChosenPath(availablePaths, stationNames, constraintData);
			
			if (chosenPath_CSP != null) {
				
				// to draw the chosen path on the screen
				GPS_Screen.setChosenPathToHighlight(chosenPath_CSP);
				
				System.out.println("Showing the path to use (CSP)...");
				for (int i=0; i <= chosenPath_CSP.length - 1; i++) {
					System.out.print(chosenPath_CSP[i]);
					if (i != chosenPath_CSP.length - 1) {
						System.out.print(", ");
					}
				}
			} else {
				System.out.println("No path found, make sure your " + constraintChoice +" is big enough");
				System.out.println("And also make sure that you're not returning back to CW or intersecting it");
				System.out.println("Or it's just that the system encountered a loop and died :)");
			}
			System.out.println("\n");
		} else if (selectedAlgorithm == "COP") {
			
			// to draw the chosen path on the screen
			Station[] chosenPath_COP = gps.findChosenPath(availablePaths, start, destination);
			
			if (chosenPath_COP != null && constraintData > 0) {
				
				GPS_Screen.setChosenPathToHighlight(chosenPath_COP);
				
				System.out.println("Showing the shortest path(COP):");
				for (int i=0; i<chosenPath_COP.length; i++) {
					System.out.print(chosenPath_COP[i].name);
					if (i != chosenPath_COP.length - 1) {
						System.out.print(", ");
					}
				}
				System.out.println("");	
			} else {
				System.out.println("No path found, make sure your " + constraintChoice +" is big enough");
				System.out.println("And also make sure that you only select the starting and destination stations only");
			}
			System.out.println("\n");
			
		}
		
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
