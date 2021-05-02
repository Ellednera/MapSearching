import java.util.*;
import java.awt.*;

public class GPS {
	public static void main (String args[]) {
		// new Screen().renderGraphics(300, 300, 400, 400);
		// everything is based on TestScript.java
		CityMap mapA = new CityMap();
		
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
		
		Station[] chosenPath = findChosenPath(availablePaths, "W1", "W2");
		System.out.println("The shortest path(according to sequence):");
		//System.out.println(chosenPath); // this is only a reference, because it's retruned from an array
		for (int i=0; i<chosenPath.length; i++) {
			System.out.print(chosenPath[i].name + ",");
		}
		System.out.println("");
		
		Screen GPS_Screen = new Screen();
		GPS_Screen.sendStationList(mapA.stationDictionary.values().iterator());
		GPS_Screen.renderGraphics(300, 100, 500, 500); // only stations connected using CityMap::connectBridges will be drawn

	}
	
	
	private static Station[] 
	findChosenPath(ArrayList<java.util.List<Station>> paths_2_filter, String check_start_name, String check_destination_name) {

		Station[] shortestPath = null;
		
		// Schwartzian transform: "map"-sort-"map"
		shortestPath = schwartzianTransform(paths_2_filter);
		
		// check if it is the reverse route and directly return result
		return correctedDirection(shortestPath, check_start_name, check_destination_name);
	}
	
	private static Station[] schwartzianTransform(ArrayList<java.util.List<Station>> paths_2_filter) {
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
		
		System.out.println(indexes);
		// map
		int shortestPathIndex = Collections.min(indexes);
		//System.out.println("The shortest path index is " + shortestPathIndex);
		//System.out.println(schwartzian.get( shortestPathIndex ));
		
		//System.out.println( schwartzian.get( shortestPathIndex ).toArray(shortestPath) );
		return schwartzian.get( shortestPathIndex ).toArray(new Station[schwartzian.get( shortestPathIndex ).size()-1]);
	}
	
	private static Station[] correctedDirection(Station[] shortestPath, String check_start_name, String check_destination_name) {
	// Go to https://www.geeksforgeeks.org/reverse-an-array-in-java/ to see how to use Collections + Arrays.asList to reverse the array
	// this part is working fine already
		// System.out.println("Start: " + shortestPath[0]);
		// System.out.println("End: " + shortestPath[2]);
		if (check_start_name == shortestPath[ shortestPath.length-1 ].name) {
			// reverse the path
			Station[] reversedShortestPath = new Station[shortestPath.length]; // cannot -1, will cause problem
			// System.out.println("Reversed length" + " " + reversedShortestPath.length);
			for (int i=shortestPath.length-1; i>=0; i--) {
				System.out.println(i + ", " + (shortestPath.length - 1 - i) );
				reversedShortestPath[shortestPath.length - 1 - i] = shortestPath[i];
			}
			return reversedShortestPath;
		} else {
			return shortestPath;
		}
	}
	
	public static void drawMap(Station[] stations) {
		new Screen();
	}
}

class Screen extends Frame {
	private Iterator<Station> stations_it = null;
	private int stationDiameter = 20;
	private int stationRadius = stationDiameter / 2;
	Screen () {
		super("COS30019 Vehicle Routing System - GPS");
	}
	
	public void sendStationList(Iterator<Station> stations_it) {
		this.stations_it = stations_it;
	}
	
	public void renderGraphics(int x, int y, int w, int h) {
		setBounds(x, y, w, h);
		setBackground(Color.PINK);
		setLayout(new GridLayout(2, 1));

		setVisible(true);		

	}
	
	public void paint(Graphics g) {
		Color c = g.getColor();
		
		Station station = null;
		while (stations_it.hasNext()) {
			station = stations_it.next();

			// circles
			g.setColor(new Color(110, 200, 90));
			g.fillOval(station.x, station.y, stationDiameter, stationDiameter);
			
			// station names
			g.setColor(Color.BLACK);
			g.drawString(station.name, station.x + 20, station.y + 10);
			
			// draw roads
			g.setColor(Color.WHITE);
			Iterator<Station> connected_it  = station.allConnections().keySet().iterator();
			while (connected_it.hasNext()) {
				Station connectedStation = connected_it.next();
				g.drawLine(station.x + stationRadius, station.y +stationRadius, 	
							connectedStation.x +stationRadius , connectedStation.y +stationRadius);
			}

		}
		
		g.setColor(c);
		
	}

}
