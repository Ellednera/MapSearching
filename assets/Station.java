package assets;

import java.util.*;

public class Station {
	public int x;
	public int y;
	public String name;
	private Map<Station, Integer> connections = new HashMap<Station, Integer>();
	
	Station(int x, int y, String name) {
		this.x = x;
		this.y = y;
		this.name = name;
	}
	
	public void connectTo(Station s, int distance) {
		connections.put(s, distance);
		s.allConnections().put(this, distance);
	}
	
	public boolean isConnectedTo(Station s) {
		return connections.containsKey(s);
	}
	
	public int distanceFrom(Station s) {
		return (int) connections.get(s);
	}
	
	public void verbose_distanceFrom(Station s) {
		System.out.println
			( "Distance from " + this.name + " to " + s.name + " is " 
				+ this.distanceFrom(s) + " cm :)" );
	}
	
	// this might not be needed, as we already can check the details
	public Map<Station, Integer> allConnections() {
		return connections;
	}
	
	// override toString(), might be useful
	public String toString() {
		return "This is " + name + " at (" + x + "," + y + ")";
	}
	
	public void showDetails() {
		System.out.println(this.toString());
		if(this.connections.isEmpty()) {
			System.out.println("  '" + this.name + "' has no friends, it's sad :(");
		} else {
			System.out.println(this.name +" is connected to: ");
			Iterator<Station> i = connections.keySet().iterator();

			while(i.hasNext()) {
				Station s = i.next();
				System.out.println( "|--> " + s.name + " at " + connections.get(s) + " cm away" );
			}
			System.out.println("");
		}
		
	}
	
	// testing purposes, it's up to the city map to do this part
	public static void main(String args[]) {
		Station company = new Station(5, 5, "Company");
		Station house_1 = new Station(10, 5, "House 1");
		Station house_2 = new Station(1, 23, "House 2");
		company.connectTo(house_1, 15);
		house_1.connectTo(house_2, 32);
		System.out.println( company.isConnectedTo(house_1) );
		System.out.println( company.distanceFrom(house_1) );
		company.verbose_distanceFrom(house_1);
	
		System.out.println("");
		System.out.println( company );
		System.out.println( house_1 );
		
		System.out.println("");
		company.showDetails();
		house_1.showDetails();
		house_2.showDetails();
		
	}
}
