import assets.*;

import java.util.*;

public class TestScript {

	public static void main (String args[]) {
		// new Screen().renderGraphics(300, 300, 400, 400);
		// everything is based on TestScript.java
		CityMap mapA = new CityMap();
		GPS gps = new GPS();
		// List in java.util and java.awt confilcts
		// we might deal with this later as java.awt.List might not be used
		ArrayList<java.util.List<Station>> partialNetworks =  mapA.processPartialNetwork(true);
		ArrayList<java.util.List<Station>> fullPaths = mapA.processFullNetwork(partialNetworks, 3, false);
		
		ArrayList<java.util.List<Station>> availablePaths = mapA.showAvailablePaths("W1", "W2", fullPaths, false);
		// W2 -> W1 successful, both partial netowrks have only 2 stations each, that's why it works
		// for a long list of stations, trimming might be needed
		
		// this part is problematic, it only displays null
		// findChosenPath(availablePaths);
		// toArray(<T>[] a) a is a new T[], if not it won't work
		
		Station[] chosenPath = gps.findChosenPath(availablePaths, "W1", "W2");
		System.out.println("The shortest path(according to sequence):");
		//System.out.println(chosenPath); // this is only a reference, because it's retruned from an array
		for (int i=0; i<chosenPath.length; i++) {
			System.out.print(chosenPath[i].name + ",");
		}
		System.out.println("");
		
		Screen GPS_Screen = new Screen();
		GPS_Screen.sendMap(mapA);
		GPS_Screen.renderGraphics(300, 100, 500, 500); // only stations connected using CityMap::connectBridges will be drawn

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
