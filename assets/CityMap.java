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
	
	public void displayFullNetwork(ArrayList<List<Station>> _partialNetworks) {
		// System.out.println( _partialNetworks.get(0).toString() ); // will print out all elements.toString()
		// call linkAllConnectingStations() and print everything out, this is the solution, might need some optimizing though
		ArrayList<List<Station>> fullNetworkList = linkAllConnectingStations( _partialNetworks );
		
		// still need to process the output
		System.out.println( fullNetworkList ); 
		// need to call showSearchPath() for the [optimized] path to use
		
	}
	
	// this part will only fail if there are no partial connections
	private ArrayList<List<Station>> linkAllConnectingStations(ArrayList<List<Station>> _partialNetworks) {
		ArrayList<List<Station>> _fullNetworkList = new ArrayList<List<Station>>();
		try {
			// loop through to get the last element of List(n-1)
			// and then get the first element of List(n)
			for (int i=0; i<_partialNetworks.size(); i++) {
				// last station in first network
				List<Station> firstSingleNetwork = _partialNetworks.get(i);
				Station firstNetworkLastStation = firstSingleNetwork.get( firstSingleNetwork.size()-1 );
				
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
		
		Station company = new Station(5, 5, "Company");
		Station house_1 = new Station(10, 5, "House 1");
		Station house_2 = new Station(1, 23, "House 2");
		Station house_3 = new Station(1, 2, "House 3");
		Station house_4 = new Station(9, 22, "House 4");
		Station warehouse_1 = new Station(50, 50, "Warehouse 1");
		Station warehouse_2 = new Station(100, 50, "Warehouse 2");
		
		// no connectTo() == error :)
		
		house_1.connectTo(company, 17);
		mapA.createBridges(house_1, company);
		
		// house_3.connectTo(house_3, 0);
		// mapA.createBridges(house_3, house_3); 
		// this loop needs to be fixed in the future, unless the distance is not 0
		
		// 3 stations in one go

		company.connectTo(house_2, 15);	house_2.connectTo(house_3, 32);
		mapA.createBridges(company, house_2, house_3); 
		
		// one connection will be left out, this will be solved by fixing the stations in the ideal manner
		company.connectTo(house_4, 50);
		mapA.createBridges(company, house_4); 
		
		house_4.connectTo(warehouse_1, 39); warehouse_1.connectTo(warehouse_2, 40);
		mapA.createBridges(house_4, warehouse_1, warehouse_2);
		
		warehouse_2.connectTo(house_3, 999);
		mapA.createBridges(warehouse_2, house_3);
		// or the linkAllConnectingStations() private method can be set to public and called a few times 
		// and store them into s data file
		
		// it won't be in sequence, we're using a HashMap :)
		ArrayList<List<Station>> partialNetworks =  mapA.displayPartialNetwork();
		
		mapA.displayFullNetwork(partialNetworks);
		
		// System.out.println(house_1.name + " connected to " + house_3.name + "? " + house_1.isConnectedTo(house_3));
		// System.out.println(house_3.name + " connected to " + house_1.name + "? " + house_3.isConnectedTo(house_1));
		
		// this doesn't do much
		// if (house_1.isConnectedTo(company) && company.isConnectedTo(house_2) && house_2.isConnectedTo(house_3)) {
			// house_1.showDetails();
		// }
		
		// System.out.println("asdjodjoi");

	}
}