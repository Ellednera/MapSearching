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
		
		ArrayList<java.util.List<Station>> availablePaths = mapA.showAvailablePaths("C", "H3", fullPaths, false);
		
		// this part is problematic, it only displays null
		findChosenPath(availablePaths);
		// toArray(<T>[] a) a is a new T[], if not it won't work
		
		Station[] chosenPath = findChosenPath(availablePaths);
		System.out.println("The shortest path(according to sequence):");
		//System.out.println(chosenPath); // this is only a reference, because it's retruned from an array
		for (int i=0; i<chosenPath.length; i++) {
			System.out.print(chosenPath[i].name + ",");
		}
		System.out.println("");

	}
	
	private static Station[] findChosenPath(ArrayList<java.util.List<Station>> paths_2_filter) {

		Station[] shortestPath = null;
		Map<Integer, java.util.List<Station>> schwartzian = new HashMap<Integer, java.util.List<Station>>();
		// Schwartzian transform: map-sort-map
		// map
		Collections.shuffle(paths_2_filter);
		
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
		shortestPath = schwartzian.get( shortestPathIndex ).toArray(new Station[schwartzian.get( shortestPathIndex ).size()-1]);
		//System.out.println( schwartzian.get( shortestPathIndex ).toArray(shortestPath) );
		return shortestPath;
	}
}

class Screen extends Frame {
	Screen () {
		super("COS30019 Vehicle Routing System - GPS");
	}
	
	public void renderGraphics(int x, int y, int w, int h) {
		setBounds(x, y, w, h);
		setBackground(Color.PINK);
		setLayout(new GridLayout(2, 1));

		setVisible(true);		

	}
	
	public void paint(Graphics g) {
		Color c = g.getColor();
		
		g.setColor(Color.WHITE);
		for (int i=0; i<10; i++) {
			g.fillOval((int)(Math.random() * 370), (int)(Math.random() * 370), 30, 30);
		}
		
		g.setColor(c);
		
	}

}
