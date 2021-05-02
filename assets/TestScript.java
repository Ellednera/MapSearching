import java.util.*;

public class TestScript {

	public static void main(String args[]) {
	
	/*
		int numbers[] = new int[5];
		numbers[1] = 5;
		System.out.println(numbers);
	*/
	
	// this whole part is the map's initialization process
		CityMap mapA = new CityMap();
		
		// house_3.connectTo(house_3, 0);
		// mapA.createBridges(house_3, house_3); 
		// this loop needs to be fixed in the future, unless the distance is not 0
		
		
		// or the linkAllConnectingStations() private method can be set to public and called a few times 
		// and store them into s data file
		
		// it won't be in sequence, we're using a HashMap :)
		
		// this 2 lines must be called like this, they're linked together, you can nest them if you want to :)
		ArrayList<List<Station>> partialNetworks =  mapA.processPartialNetwork(false);
		ArrayList<List<Station>> fullPaths = mapA.processFullNetwork(partialNetworks, 3, false);
		
		// System.out.println(house_1.name + " connected to " + house_3.name + "? " + house_1.isConnectedTo(house_3));
		// System.out.println(house_3.name + " connected to " + house_1.name + "? " + house_3.isConnectedTo(house_1));
		
		// this doesn't do much
		// if (house_1.isConnectedTo(company) && company.isConnectedTo(house_2) && house_2.isConnectedTo(house_3)) {
			// house_1.showDetails();
		// }
		
		// System.out.println("asdjodjoi");
		
		// Test company---->H3, shuold get some compound path(not necessarily end with H3, it could be in the middle)
		// it's up to the programmer to make sure that the spelling is correct
		mapA.showAvailablePaths("C", "H3", fullPaths, true);
		
		// tests
		String[] station_names = {"Company", "H1", "W1", "A1", "C", "Company"}; // use stationDictionary
		test_stationDictionary(mapA, "Company");
		test_stationDictionary(mapA, "H3");
		test_stationDictionary(mapA, station_names);
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
