import java.util.*;

public class CityMap {
	public Map<Station, Map<Station, Station>> cityMap = new HashMap<Station, Map<Station, Station>>();
	// set max 3 connections in one go :)
	
	public void createBridges(Station s1, Station s2, Station s3) {
		// cityMap.put(s1, new HashMap<Station, Station>()); // this line is causing the same key's value to be overwritten
		// there's no autovivification in Java, this is not Perl :^)
		// cityMap.get(s1).put(s2, s3);
		if (cityMap.containsKey(s1)) {
			cityMap.get(s1).put(s2, s3);
		} else {
			cityMap.put(s1, new HashMap<Station, Station>() {{ put(s2, s3); }});
		}
	}
	
	public void createBridges(Station s1, Station s2) {
		// cityMap.put(s1, new HashMap<Station, Station>());
		
		if (cityMap.containsKey(s1)) {
			cityMap.get(s1).put(s2, null); // value is null
		} else {
			cityMap.put(s1, new HashMap<Station, Station>() {{ put(s2, null); }});
		}
	}
	
	// this will fail due to Station's connectTo() method, minimum is 2 stations per connection
	/*
	public void createBridges(Station s1) {
		cityMap.put(s1, new HashMap<Station, Station>());
		cityMap.get(s1).put(null, null); // key and value both null
	}
	*/
	
	public ArrayList<List<Station>> displayPartialNetwork() {
		// if any error occurs, it means that the stations are not connected correctly
		// if there is only one station with no connection, it will be ignored, we're using a while loop, that's why we don't need to check
		// plus, you cannot connect something to nothing :) Up till this point, this shouldn't be the problem
		
		// for collecting data
		ArrayList<List<Station>> partialNetworks = new ArrayList<List<Station>>(); 
		// actual map iteration
		Iterator<Station> sit_1 = cityMap.keySet().iterator();
		try {

			while (sit_1.hasNext()) {
				Station station_level_1 = sit_1.next();
				
				Iterator<Station> sit_2 = cityMap.get(station_level_1).keySet().iterator();
				while (sit_2.hasNext()) {
					Station station_level_2 = sit_2.next();
							
					if(cityMap.get(station_level_1).get(station_level_2) == null) { 
						partialNetworks.add( Arrays.asList(station_level_1, station_level_2) ); // collect data
						System.out.println
							(station_level_1.name + "--"+ station_level_1.distanceFrom(station_level_2) +"->" + station_level_2.name);
						continue;
					} else {
						Station final_station = cityMap.get(station_level_1).get(station_level_2);
						partialNetworks.add( Arrays.asList(station_level_1, station_level_2, final_station) ); // collect data
						System.out.print(station_level_1.name + "--"+ station_level_1.distanceFrom(station_level_2) +"->");
						System.out.println
							(station_level_2.name + "--"+ station_level_2.distanceFrom(final_station) +"->" + final_station.name);
					}
					
				}
			}

		} catch (NullPointerException e) {
			System.out.println("Oops! I can't generate the station network.");
			System.out.println("->Reason: Programming error. The stations are not connected correctly.");
			System.out.println("-->Check the part with connectTo() for the station you created, good luck\n");
			System.out.println("More details over here:");
			e.printStackTrace(); System.exit(-1);
		}
		
		return partialNetworks;
	}
	
	public void displayFullNetwork(ArrayList<List<Station>> _partialNetworks, int maxLayers) {
		// System.out.println( _partialNetworks.get(0).toString() ); // will print out all elements.toString()
		// call linkAllConnectingStations() and print everything out, this is the solution, might need some optimizing though
		
		ArrayList<List<Station>> fullNetworkList;
		if (maxLayers <= 2) {
			fullNetworkList = linkAllConnectingStations( _partialNetworks );
		} else {
			
			for (int i=1; i <= maxLayers-1; i++) {
				_partialNetworks = linkAllConnectingStations( _partialNetworks );
			}
			fullNetworkList = _partialNetworks;
		}
		
		// still need to process the output
		System.out.println( fullNetworkList ); 
		// need to call showSearchPath() for the [optimized] path to use
		
	}
	
	// this part will only fail if there are no partial connections
	private ArrayList<List<Station>> linkAllConnectingStations(ArrayList<List<Station>> _partialNetworks) {
		ArrayList<List<Station>> _fullNetworkList = new ArrayList<List<Station>>();
		try {
			// add all the partial connections into the list too, so that we will have more available connections to look up :)
				// see the first for loop
			// loop through to get the last element of List(n-1)
			// and then get the first element of List(n)
			for (int i=0; i<_partialNetworks.size(); i++) {
				// last station in first network
				List<Station> firstSingleNetwork = _partialNetworks.get(i);
				Station firstNetworkLastStation = firstSingleNetwork.get( firstSingleNetwork.size()-1 );
				
				// add every partial network by default :)
				_fullNetworkList.add( firstSingleNetwork );
				
				// loop through the rest of the paths
				for (int j=0; j<_partialNetworks.size(); j++) {
					// System.out.println("i=" + i + " j=" + j);
					if (i == j) { continue; } // same path, skip it
					
					// first station in second network
					List<Station> secondSingleNetwork = _partialNetworks.get(j); // get jay (j) not i
					Iterator<Station> secondNIT = secondSingleNetwork.iterator();
					Station secondNetworkFirstStation = secondNIT.next();
					
					// if the 2 stations are the same, then the 2 lists are linked
					// System.out.println( firstNetworkLastStation.equals( secondNetworkFirstStation ) );
					// System.out.println( firstNetworkLastStation + "\n" + secondNetworkFirstStation );
					if (  firstNetworkLastStation.equals( secondNetworkFirstStation )  ) {
						System.out.println("Found a partial connection and connecting them . . .");
						List<Station> singleFullPath = new ArrayList<Station>();
						singleFullPath.addAll(firstSingleNetwork);
						
						// remove intersection = 2nd network's first station, it's the same as the 1st network last station
						singleFullPath.addAll(secondSingleNetwork);
						
						singleFullPath.remove(secondNetworkFirstStation);
						
						_fullNetworkList.add( singleFullPath );
						
					}
				}
				
			}

		} catch (IndexOutOfBoundsException e) {
			// Make this a fatal error, because we will be creating the map manually (hard code)
			// it cannot be that there are no partial connections, unless we want the map to be an easy-to-search map :)
			System.out.println("Oops! I can't generate the full station network.");
			
			System.out.println("->Reason 1: Programming error. The partial connections are not connected " + 
				"\n    correctly or there is no partial connection.");
			System.out.println("-->Please don't fool around with the map system\n");
			
			System.out.println("->Reason 2: Programming error. The index used for iterating is off by 1");
			System.out.println("-->Please check the for loop at the part i<_partialNetworks.size()\n");
			System.out.println("More details over here:");
			e.printStackTrace(); System.out.println("");
			System.exit(-1);
		}
		return _fullNetworkList;
	}
	
	public void showSearchPath(Station start, Station destination) {
		
	}
	
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
		ArrayList<List<Station>> partialNetworks =  mapA.displayPartialNetwork();
		
		mapA.displayFullNetwork(partialNetworks, 3);
		
		// System.out.println(house_1.name + " connected to " + house_3.name + "? " + house_1.isConnectedTo(house_3));
		// System.out.println(house_3.name + " connected to " + house_1.name + "? " + house_3.isConnectedTo(house_1));
		
		// this doesn't do much
		// if (house_1.isConnectedTo(company) && company.isConnectedTo(house_2) && house_2.isConnectedTo(house_3)) {
			// house_1.showDetails();
		// }
		
		// System.out.println("asdjodjoi");

	}
}
