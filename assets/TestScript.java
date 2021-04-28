import java.util.*;

public class TestScript {

	public static void main(String args[]) {
		CityMap mapA = new CityMap();
		
		Station company = new Station(5, 5, "C"); // company
		Station s1 = new Station(5, 5, "S1"); // station
		Station h1 = new Station(5, 5, "H1"); // houses
		Station h2 = new Station(5, 5, "H2");
		Station h3 = new Station(5, 5, "H3");
		Station h4 = new Station(5, 5, "H4");
		Station h5 = new Station(5, 5, "H5");
		Station h6 = new Station(5, 5, "H6"); 
		Station f1 = new Station(5, 5, "F1"); // factory
		Station w1 = new Station(5, 5, "W1"); // warehouse
		Station w2 = new Station(5, 5, "W2"); // warehouse
		Station a1 = new Station(5, 5, "A1"); // airport
		Station p1 = new Station(5, 5, "P1"); // port
		
		// no connectTo() == error :)
		/*
		// assume that all connections are bi-directional
		company.connectTo(s1, 2); s1.connectTo(h4, 1); mapA.createBridges(company, s1, h4);
												h4.connectTo(h5, 2); mapA.createBridges(h4, h5);
												h4.connectTo(h6, 4); mapA.createBridges(h4, h6);
		
		company.connectTo(h1, 3); mapA.createBridges(company, h1);
						  h1.connectTo(h2, 3); h2.connectTo(w1, 6); mapA.createBridges(h1, h2, w1);
						  h1.connectTo(h3, 8); h3.connectTo(f1, 8); mapA.createBridges(h1, h3, f1);
		*/
		
		// the challenging part :)
		company.connectTo(w2, 4); mapA.createBridges(company, w2);
						  w2.connectTo(h3, 10); mapA.createBridges(w2, h3);
						  			   h3.connectTo(w1, 5); mapA.createBridges(h3, w1);
						  			   h3.connectTo(f1, 9); mapA.createBridges(h3, f1);
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
		String[] station_names = {"Company", "H1", "W1", "A1", "C", "Company"};
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
