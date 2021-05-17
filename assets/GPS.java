package assets;

import java.util.*;
import java.awt.*;

public class GPS {
	
	public Station[] 
	findChosenPath(ArrayList<java.util.List<Station>> paths_2_filter, String check_start_name, String check_destination_name) {

		Station[] shortestPath = null;
		
		// Schwartzian transform: "map"-sort-"map"
		shortestPath = schwartzianTransform(paths_2_filter);
		
		// check if it is the reverse route and directly return result
		return correctedDirection(shortestPath, check_start_name, check_destination_name);
	}
	
	public Station[] 
	findChosenPath(ArrayList<java.util.List<Station>> paths_2_filter, ArrayList<Station> stationNames, int capacity) {

		java.util.List<Station> stationList;
		for (int i = 0; i <= paths_2_filter.size() -1; i++) {
			stationList = paths_2_filter.get(i);
			if (stationList.containsAll(stationNames) && stationList.size() <= capacity && stationList.indexOf(stationNames.get(0)) == 0) {
				// if stationNames is included in the valid paths, then that means itself is the valid path too :)
				return stationNames.toArray(new Station[stationNames.size()-1]);
			}
		}
		
		return null;
	}

	private Station[] schwartzianTransform(ArrayList<java.util.List<Station>> paths_2_filter) {
		Map<Integer, java.util.List<Station>> schwartzian = new HashMap<Integer, java.util.List<Station>>();
		// Schwartzian transform: map-sort-map
		// map
		Collections.shuffle(paths_2_filter); 
		// this is to be fair, we're taking data out from a hashmap, there's a specific arrangement in the hashmap
		
		for (int i=0; i<paths_2_filter.size(); i++) {
			schwartzian.put(paths_2_filter.get(i).size(), paths_2_filter.get(i));
		}
		//System.out.println(schwartzian);
		//System.out.println(schwartzian.get(3));
		
		// sort
		ArrayList<Integer> indexes = new ArrayList<Integer>();
		Iterator<Integer> it = schwartzian.keySet().iterator();
		
		while (it.hasNext()) {
			indexes.add(it.next());
		}
		
		// System.out.println(indexes);
		
		// map
		int shortestPathIndex = Collections.min(indexes);
		//System.out.println("The shortest path index is " + shortestPathIndex);
		//System.out.println(schwartzian.get( shortestPathIndex ));
		
		//System.out.println( schwartzian.get( shortestPathIndex ).toArray(shortestPath) );
		return schwartzian.get( shortestPathIndex ).toArray(new Station[schwartzian.get( shortestPathIndex ).size()-1]);
	}
	
	private Station[] correctedDirection(Station[] shortestPath, String check_start_name, String check_destination_name) {
	// Go to https://www.geeksforgeeks.org/reverse-an-array-in-java/ to see how to use Collections + Arrays.asList to reverse the array
	// this part is working fine already
		// System.out.println("Start: " + shortestPath[0]);
		// System.out.println("End: " + shortestPath[2]);
		if (check_start_name == shortestPath[ shortestPath.length-1 ].name) {
			// reverse the path
			Station[] reversedShortestPath = new Station[shortestPath.length]; // cannot -1, will cause problem
			// System.out.println("Reversed length" + " " + reversedShortestPath.length);
			System.out.println("A reversed path found, it seems like you're trying to return to the company");
			for (int i=shortestPath.length-1; i>=0; i--) {
				// System.out.println(i + ", " + (shortestPath.length - 1 - i) );
				reversedShortestPath[shortestPath.length - 1 - i] = shortestPath[i];
			}
			return reversedShortestPath;
		} else {
			return shortestPath;
		}
	}
	
	public void drawMap(Station[] stations) {
		new Screen();
	}
	
}

