package assets;

import java.util.*;

public class CityMap {
	public Map<Station, Map<Station, Station>> cityMap = new HashMap<Station, Map<Station, Station>>();
	// set max 3 connections in one go :)
	
	// for searching the path, this will be initialized in the createBridges() method
	public Map<String, Station> stationDictionary = new HashMap<String, Station>();
	
	// constructor - directly initialize the map
	public CityMap() {
		Station cw = new Station(255, 230, "CW"); // company
		Station s1 = new Station(320, 165, "S1"); // station
		Station h1 = new Station(150, 150, "H1"); // houses
		Station h2 = new Station(50, 50, "H2");
		Station h3 = new Station(70, 160, "H3");
		Station h4 = new Station(335, 100, "H4");
		Station h5 = new Station(250, 50, "H5");
		Station h6 = new Station(430, 220, "H6"); 
		Station f1 = new Station(190, 45, "F1"); // factory
		Station w1 = new Station(0, 325, "W1"); // warehouse
		Station w2 = new Station(350, 350, "W2"); // warehouse
		Station a1 = new Station(460, 100, "A1"); // airport
		Station p1 = new Station(380, 50, "P1"); // port
		Station r1 = new Station(350, 250, "R1"); // river ?
		Station r2 = new Station(435, 367, "R2");
		Station r3 = new Station(300, 445, "R3");
		Station m1 = new Station(100, 330, "M1"); // mall
		Station m2 = new Station(145, 430, "M2");
		Station q1 = new Station(25, 460, "Q1"); // ...
		
		// assume that all connections are bi-directional
		// however the searching algorightms don't care about it :)
		
		// top right corner
		cw.connectTo(s1, 2); createBridges(cw, s1);
		  s1.connectTo(h4, 1); createBridges(s1, h4);
					   h4.connectTo(h5, 2); createBridges(h4, h5);
					   h4.connectTo(h6, 4); createBridges(h4, h6);
					   				h6.connectTo(p1, 5); createBridges(h6, p1);
					   							 p1.connectTo(a1, 3); createBridges(p1, a1);
		// top left corner
		cw.connectTo(h1, 3); createBridges(cw, h1);
				     h1.connectTo(h2, 3); createBridges(h1, h2);
				  			      h2.connectTo(w1, 6); createBridges(h2, w1);
				     h1.connectTo(h3, 8); createBridges(h1, h3);
				  			      h3.connectTo(f1, 8); createBridges(h3, f1);

		cw.connectTo(w2, 4); createBridges(cw, w2);
				     w2.connectTo(h3, 15); createBridges(w2, h3);
				  			      h3.connectTo(w1, 5); createBridges(h3, w1);
				  			      h3.connectTo(f1, 9); createBridges(h3, f1);
		
		cw.connectTo(w1, 11); createBridges(cw, w1);
			         w1.connectTo(h2, 6); createBridges(w1, h2);

		cw.connectTo(h3, 4); createBridges(cw, h3);
				     h3.connectTo(w1, 9);

		// bottom part		
		cw.connectTo(r1, 5); createBridges(cw, r1);
					 r1.connectTo(r2, 9); createBridges(r1, r2);
					 			  r2.connectTo(r3, 10); createBridges(r2, r3);
		cw.connectTo(r3, 12); createBridges(cw, r3);
					 r3.connectTo(m1, 16); createBridges(r3, m1);
					 			  m1.connectTo(q1, 8); createBridges(m1, q1);
					 r3.connectTo(m2, 11); createBridges(r3, m2);
					 			  m2.connectTo(q1, 5); createBridges(m2, q1);
		cw.connectTo(q1, 20); createBridges(cw, q1);
		
	}
	/*
	public void createBridges(Station s1, final Station s2, final Station s3) {
		// cityMap.put(s1, new HashMap<Station, Station>()); // this line is causing the same key's value to be overwritten
		// there's no autovivification in Java, this is not Perl :^)
		// cityMap.get(s1).put(s2, s3);
		if (cityMap.containsKey(s1)) {
			cityMap.get(s1).put(s2, s3);
		} else {
			cityMap.put(s1, new HashMap<Station, Station>() {{ put(s2, s3); }});
		}
		updateStationRecord(s1, s2, s3);
	}
	*/
	
	public void createBridges(Station s1, final Station s2) {
		// cityMap.put(s1, new HashMap<Station, Station>());
		
		if (cityMap.containsKey(s1)) {
			cityMap.get(s1).put(s2, null); // value is null
		} else {
			cityMap.put(s1, new HashMap<Station, Station>() {{ put(s2, null); }});
		}
		updateStationRecord(s1, s2);
	}
	
	private void updateStationRecord(Station s1, Station s2) {
		stationDictionary.put(s1.name, s1);
		stationDictionary.put(s2.name, s2);
	}
	/*
	private void updateStationRecord(Station s1, Station s2, Station s3) {
		stationDictionary.put(s1.name, s1);
		stationDictionary.put(s2.name, s2);
		stationDictionary.put(s3.name, s3);
	}
	*/
	
	// this will fail due to Station's connectTo() method, minimum is 2 stations per connection
	/*
	public void createBridges(Station s1) {
		cityMap.put(s1, new HashMap<Station, Station>());
		cityMap.get(s1).put(null, null); // key and value both null
	}
	*/
	
	// if verbose, it will show the linking station(the one with stations)
	public ArrayList<List<Station>> processPartialNetwork(boolean verbose) {
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
						if (verbose) {
							System.out.println
								(station_level_1.name + "--"+ station_level_1.distanceFrom(station_level_2) +"->" + station_level_2.name);	
						}
						
						continue;
					} else {
						Station final_station = cityMap.get(station_level_1).get(station_level_2);
						partialNetworks.add( Arrays.asList(station_level_1, station_level_2, final_station) ); // collect data
						if (verbose) {
							System.out.print(station_level_1.name + "--"+ station_level_1.distanceFrom(station_level_2) +"->");
							System.out.println
								(station_level_2.name + "--"+ station_level_2.distanceFrom(final_station) +"->" + final_station.name);
						}
						
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
	
	// returns the full network list
	// if verbose, it will notify if there is a partial connection that can be connected, and
	// it will display all the final results
	public ArrayList<List<Station>> processFullNetwork(ArrayList<List<Station>> _partialNetworks, int maxLayers, boolean verbose) {
		// System.out.println( _partialNetworks.get(0).toString() ); // will print out all elements.toString()
		// call linkAllConnectingStations() and print everything out, this is the solution, might need some optimizing though
		
		ArrayList<List<Station>> fullNetworkList;
		if (maxLayers <= 2) {
			fullNetworkList = linkAllConnectingStations( _partialNetworks, verbose );
		} else {
			
			for (int i=1; i <= maxLayers-1; i++) {
				_partialNetworks = linkAllConnectingStations( _partialNetworks, verbose );
			}
			fullNetworkList = _partialNetworks;
		}
		
		// still need to process the output
		if (verbose) {
			System.out.println( fullNetworkList );
		}
		
		return fullNetworkList;
		// need to call showSearchPath() for the [optimized] path to use
		
	}
	
	// this part will only fail if there are no partial connections
	private ArrayList<List<Station>> linkAllConnectingStations(ArrayList<List<Station>> _partialNetworks, boolean verbose) {
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
				
				// add every original partial network by default :)
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
					
						if (verbose) {
							System.out.println("Found a partial connection and connecting them . . .");
						}
						
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
	
	// the parameters must be string, so that we can get the actual stations from the stationDictionary after the user input
	// the name will always be unique, since everything is stored in a HashMap in the very beginning
	public ArrayList<List<Station>> 
		showAvailablePaths(String start_name, String destination_name, ArrayList<List<Station>> processedPaths, boolean verbose, int capacity, boolean analyseCapacity) {
			// it is impossible for stationDictionary to contain anything that is not registered! 
			// if so, then it means there's a programming error :) 
				// The nullPointerException will only appear when we call the Station's properties

			Station start = stationDictionary.get(start_name);
			Station destination = stationDictionary.get(destination_name);
			// catch NullPointerException, which means the programmer set the wrong name in the GUI or something
			try {
				String check_name;
				check_name = start.name;
				check_name = destination.name;
			} catch (NullPointerException e) {
				System.out.println("\nOops! I can't look-up the stations you specified.");
				System.out.println("->Reason: Programming error. The station name(s) you specified couldn't be found.");
				System.out.println("-->Verify that the station names '"+ start_name +"' and '"+ destination_name 
					+"' are correct and that they exist.");
				System.out.println("   Then, edit them in the GUI input, buttons or something.\n");
				System.out.println("!! More details over here:");
				e.printStackTrace(); System.exit(-1);
			}
			
			if (verbose) {
				System.out.println(start.name + "-->" + destination.name);
			}
			
			ArrayList<List<Station>> validPaths = new ArrayList<List<Station>>();
			
			// loop through every path
			// then check if start & destination is within each row or not
			Iterator<List<Station>> it = processedPaths.iterator();
			List<Station> path = null;
			while (it.hasNext()) {
				path = it.next();
				
				// check against capacity
				if (path.size() - 1 > capacity) { 
					// path.size() - 1 is to exclude the company, that is the starting point
					if (analyseCapacity) {
						System.out.println("Ignoring path " + path);
						System.out.println("  Total stations(excluding company): "+ (path.size()-1) + ", Capacity: " + capacity);
					}
					continue;
				}
				
				// we might assume that the lowest number of links is actually shorter in the future
				// we need to make this work first
				if (path.contains(start) && path.contains(destination)) {
					validPaths.add(path);
				}
			}
			
			// error is caught in the front part
			if (verbose) {
				System.out.println("All valid paths from " + start.name + " to " + destination.name + ":");
				System.out.println(validPaths);
			}
			
			return validPaths;
			// and then it's up to the gps to decide which path to take, the gps will conclude the searching concept (CSP, COP, etc)
	}

}
