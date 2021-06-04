# JADE-Cars MapSearching

## Name
assets - for creating maps, gps and screen (some sort of a simulation)

## Version
1.0.0

## Description
Codes and assets for search algorithms. The codes here are the refined and extended version of the original one. The codes here are also meant to suite the project based on the Eclipse IDE. For the stand-alone i.e. original codes, see [MapSearching](https://github.com/Ellednera/MapSearching) (the original codes were written and compilled manually)

Structure of the files:
 - ./assets/Station.java
 - ./assets/CityMap.java
 - ./assets/GPS.java
 - ./assets/Screen.java
 - ./TestScript.java - can be run independently, this is used to test the GUI and includes examples of how to use the algorithms
 - ./CSPBenchmark.java - benchmark for CSP
 - ./COPBenchmark.java - benchmark for COP

## Synopsis
Below are the codes to make everything work, most of the methods not shown here can be called manually (they are shown in each individual class' synopsis, if any)
```java
import assets.*;

import java.util.*;

public class Synopsis {
	
	public static void main (String args[]) {
		
		// Set up the map and gps
		CityMap mapA = new CityMap();
		GPS gps = new GPS();
		
		// process and get ready all the paths
		// "List" in java.util and java.awt conflicts
		ArrayList<java.util.List<Station>> partialNetworks =  mapA.processPartialNetwork(true);
		ArrayList<java.util.List<Station>> fullPaths = mapA.processFullNetwork(partialNetworks, 5, false); // don't changed the number 5, allowed to increase it but not too much
	
		// Show GUI after processing the full paths
		Screen GPS_Screen = new Screen();
		GPS_Screen.sendMap(mapA);
		GPS_Screen.renderGraphics(); // only stations connected using CityMap::connectBridges will be drawn
				
		System.out.println("Screen rendering complete!");
		
		// wait for the details in gui to be filled
		// the screen is instance use only, so we need to terminate everything for every search :)
		while (!GPS_Screen.infoIsAssumedComplete) {
			try {
				System.out.println("Screen info is not complete, waiting for another 5 seconds...");
				Thread.sleep(5000);
			} catch (Exception e) {
				break;
			}
		}
		
		System.out.println("Screen info is complete, procedding to the algorithm\n"); // remove if necessary
		
		// get data from the gui
		int constraintData = GPS_Screen.getConstraintData(); // actual capacity or the weight 
		String constraintChoice = GPS_Screen.getConstraintChoice().toLowerCase(); // Either "Capacity" or "Weight"
		String selectedAlgorithm = GPS_Screen.getSelectedAlgorithm(); // Either "CSP" or "COP"
	
		// For COP, choose any station that seem farthest away to get some correct fascinating results
		// getting everything ready
		ArrayList<Station> stationNames = GPS_Screen.getSelectedStations();
		
		ArrayList<java.util.List<Station>> availablePaths;
	
		String start = stationNames.get(0).name;
		String destination = stationNames.get( stationNames.size()-1 ).name;
		availablePaths = mapA.showAvailablePaths(start, destination, fullPaths, false, constraintData, false); // 10 is the capacity
		
		// start routing based on the algorithm chosen
		if (selectedAlgorithm == "CSP") {
			
			Station[] chosenPath_CSP = gps.findChosenPath(availablePaths, stationNames, constraintData);
			
			if (chosenPath_CSP != null) {
				
				// to draw the chosen path on the screen
				GPS_Screen.setChosenPathToHighlight(chosenPath_CSP);
				
				System.out.println("Showing the path to use (CSP)...");
				for (int i=0; i <= chosenPath_CSP.length - 1; i++) {
					System.out.print(chosenPath_CSP[i]);
					if (i != chosenPath_CSP.length - 1) {
						System.out.print(", ");
					}
				}
			} else {
				System.out.println("No path found, make sure your " + constraintChoice +" is big enough");
				System.out.println("And also make sure that you're not returning back to CW or intersecting it");
				System.out.println("Or it's just that the system encountered a loop and died :)");
			}
			System.out.println("\n");
			
		} else if (selectedAlgorithm == "COP") {
	
			Station[] chosenPath_COP = gps.findChosenPath(availablePaths, start, destination);
			
			if (chosenPath_COP != null && constraintData > 0) {
				
				// to draw the chosen path on the screen
				GPS_Screen.setChosenPathToHighlight(chosenPath_COP);
				
				System.out.println("Showing the shortest path(COP):");
				for (int i=0; i<chosenPath_COP.length; i++) {
					System.out.print(chosenPath_COP[i].name);
					if (i != chosenPath_COP.length - 1) {
						System.out.print(", ");
					}
				}
				System.out.println("");	
			} else {
				System.out.println("No path found, make sure your " + constraintChoice +" is big enough");
				System.out.println("And also make sure that you only select the starting and destination stations only");
			}
			System.out.println("\n");
			
		}
		
	}
}
```

## GUI on Different Platforms
### On Windows 10
![GUI under Windows 10](https://github.com/JADE-Cars/JADE-Cars-MapSearching/blob/main/images/gui_windows10.png)

### On Linux Mint 20
![GUI under Linux Mint 20](https://github.com/JADE-Cars/JADE-Cars-MapSearching/blob/main/images/gui_linuxMint20.png)

### How it Works?
- THE GUI IS INSTANCE USE. SO, EVERY TIME A SEARCH IS DONE, YOU'LL NEED TO TERMINATE EVERYTHING AND START AGAIN :)
- The \~\~STATION LIST\~\~ is the list of stations that you can choose. By default, CW is already selected. The selected station names will be displayed on the console.
- After selecting all your desired stations, click "Register Route", this will display all the stations selected according to sequence at the bottom of the screen (with purple background). You can also click the "Register Route" button every time you select a station.
- The "Clear Route" button will remove all selected stations, but the default CW station will remain. CW is assummed to be the company+warehouse. The W of CW has nothing to do with other W* stations :)
- The constraint select box is for you to specify either to use the capacity or weight.
  - At the right side of the constraint box is an input field, make sure that you specify a big number to allow all the possible routes to be processed. **Remember to hit the ENTER button after entering  the value**, it will be disabled after that (the color becomes lighter)
- The algorithm part is for you to select either CSP or COP for the search
- The "Route" button will finally process the information entered and the search starts.
### Do
- Enter every field and chose every option that can be chosen
- Enter an appropriate value (not too big or too small) for the capacity or the weight to allow the searching algorithms to work correctly :)
- Make sure that you hit the ENTER key after entering the value for the constraint (either capacity or weight). The constraint value field will be disabled after the ENTER is hit.
- Click on the screen (anywhere in the pink area) if the original map (with green stations) disappear after the chosen route is highlighted or when the GUI is minimized and maximized again. 

### Don't
- Don't ever ever ever ever press the SPACEBAR right after clicking any buttons. The buttons include "Register Route", "Clear Route", and "Route" buttons.
- Don't try to press the "X" button of the GUI, that part was not implemented on purpose :)

### Other things to take note of
- It is advisable to make sure that the number of stations (green dots) chosen follows the following:
  - For **CSP**, make sure **not to create a loop**. This algorithm should be able to take in any number of stations.
    - The station roads might be bi-directional, but the routing algorithms don't care about that :)
  - For **COP**, make sure you **only** choose the **starting and destination stations**.
  - If it is obvious that your chosen path is correct and the searching algorithm should work, but the system terminates or can't produce any route, try again, it has something to do with the way the algorithms are coded
- Entering a huge value for the constraint might cause the algorithm to go wrong :)
- Not following the above instructions will most likely end up with strange or fascinating routes :) Sometimes the results are still correct
- The two algorithms have some randomness in them.

## Classes & Methods
### 1. assets::Station

#### Synopsis
```java
import assets.Station;
import java.util.*;

...

Station home = new Station(30, 50, "Home");
Station mall = new Station(100, 100, "Mall");
Station airport = new Station(500, 500, "Airport");

home.connectTo(mall, 50);
mall.connectTo(home, 50); // an explicit loop, be careful
mall.connectTo(airport, 25);

// connections are bi-directional by default
System.out.println("Home<->mall: " + home.isConnectedTo(mall));
System.out.println("Mall<->Home: " + mall.isConnectedTo(home));

System.out.println("Airport<->Mall: " + airport.isConnectedTo(mall));
System.out.println("Mall<->Airport: " + mall.isConnectedTo(airport));

// indirect connections can't be processed, see CityMap
System.out.println("Home<->Airoport: " + home.isConnectedTo(airport));

System.out.println("d(Home<->Mall)=" + home.distanceFrom(mall));
System.out.println("d(Mall<->Home)=" + mall.distanceFrom(home));
System.out.println("d(Airport->Mall)=" + airport.distanceFrom(mall));

// indirect connections can't be processed, see CityMap
try {
	System.out.println("d(Home->Airport)=" + home.distanceFrom(airport));
} catch (NullPointerException e) {
	System.out.println("Home is not directly connect to the airport.");
}

System.out.println("");

home.verbose_distanceFrom(mall);
airport.verbose_distanceFrom(mall);

// indirect connections can't be processed, see CityMap
try {
	home.verbose_distanceFrom(airport); // error
} catch (NullPointerException e) {
	System.out.println("Home is not directly connect to the airport.");
}

System.out.println("");

// show the details of each station's connections
home.showDetails();
mall.showDetails();
airport.showDetails();
```

#### Station(int x, int y, String name)
The constructor to create a new *Station*, x and y are the location to be drawn on the screen and have nothing to do with the distance in *CityMap::createBridges*

#### public void connectTo(Station s, int distance)
Connects the current *Station* to another *Station*. This eliminates the need of an edge. *Station* is the vertex :)

Whenever a *Station* is connected to this current station, both *Station*'s connections list will be updated. This works because a *Station* must be passed in and therefore both station's infomation can be obatin in one go

#### public boolean isConnectedTo(Station s)
Checks if this current station is connected to another *Station* s

#### public int distanceFrom(Station s)
The distance from this current station to another *Station* s

#### public void verbose_distanceFrom(Station s)
More text :)

#### public Map<Station, Integer> allConnections()
Returns the whole list of connections from this station to all other stations, if any

This method is called by *showDetails()*

#### public String toString()
This method is overridden to return a string that resembles the Cartesian coordinates as much as possible for readability

#### public void showDetials()
Calls *toString()* and adds on more details


### 2. assets::CityMap

#### Synopsis
```java
import assets.Station;
import asets.CityMap;
import java.util.*;

...

// create maps
CityMap firstMap = new CityMap();
CityMap secondMap = new CityMap();

// add on stations to second map
Station home = new Station(30, 50, "Home");
Station mall = new Station(100, 100, "Mall");
Station airport = new Station(500, 500, "Airport");

home.connectTo(mall, 50);    secondMap.createBridges(home, mall);
mall.connectTo(home, 5);     secondMap.createBridges(mall, home); // this will cause a loop
mall.connectTo(airport, 25); secondMap.createBridges(mall, airport);

// link all the connections
System.out.println("First map's partial network:");
ArrayList<List<Station>> firstMapPartialNetwork = firstMap.processPartialNetwork(true);

System.out.println("");

System.out.println("Second map's partial network:");
ArrayList<List<Station>> secondMapPartialNetwork = secondMap.processPartialNetwork(true);

// the number 5 is the minimum iterations needed to link all the default stations
ArrayList<List<Station>> secondMapFullNetwork 
	= secondMap.processFullNetwork(secondMapPartialNetwork, 5, false);

// capacity of 3 will exclude the starting destination (Home)
ArrayList<List<Station>> pathsFromHomeToAirport = secondMap.showAvailablePaths("Home", "Airport", secondMapFullNetwork, false, 4, false);
System.out.println(pathsFromHomeToAirport); // a lot of redundant paths
```

#### CityMap()
The default constructor. This will set and connect all the Stations. You can also add-on connections if you want to :)

The number of layers to pass to asset::CityMap::processFullNetwork is 5

#### public void createBridges(Station s1, Station s2)
Connects 2 *Stations* in one go. This is the minimum stations that can be connected.

This means that the stations are bi-directional. However, this is not taken into consideration during the route searching process :)

#### private void updateStationRecord(Station s1, Station s2)
This method will be called by the *createBridges* method. The record is needed for the actual searching based on user input.

#### public ArrayList<List\<Station>> processPartialNetwork(boolean verbose)
Stores all the connections created by *CityMap::creteBridges()*. At this stage, we only have all the partial network. There might still be some connections that can/needs to be combined into a single connection. See also *processFullNetwork()*

If **verbose** is set to true, it will show all the partial networks that will go through the linking process.
  
#### public ArrayList<List\<Station>> processFullNetwork(ArrayList<List\<Station>> _partialNetworks, int maxLayers, boolean verbose)
This method calls the private method *linkAllConnectingStations()* to combine the bridges. See *linkAllConnectingStations()* for more details.

*maxLayers* is the number of layers to be proccessed. Draw your map on a piece of papaer first and then determine how many layers it will be :) A branching network will caused an extra layer to exists.

If **verbose** is set to true, it will display the combined-once partial network and then notify if there are any partial networks that can be link for each iteration.

This method will store every path in *_partialNetworks* into a new ArrayList<List<Station>>, any linking partial network will then be processed and added in. This means that there might be some "redundant" partial paths (before linking then together) included which might be useful depending on the use case.

#### private ArrayList<List\<Station>> linkAllConnectingStations(ArrayList<List\<Station>> _partialNetworks, boolean verbose)
This method combines the partial networks by looping through the ArrayList ONCE only and each *List<Station>* within will be checked against another *List<Station>*. Therefore, only 2 linking partial networks will be combined together each time. If there are more than 2 partial networks that should be linked toegther, only the first 2 (that the JVM happen to see) will be processed. This methods returns the same list as the *processFullNetwork()* method. This method can be called as many times as needed.

If **verbose** is set to true, it will show all the paths that meets the requirements ie. paths that include the specified start and destination stations.

*This method should be optimized further in the future (The starting station should be in the first record while the destination station should be in the last record and not somewhere in the middle due to some simple geometry concepts)

#### public ArrayList<List\<Station>> showAvailablePaths(String start_name, String destination_name, ArrayList<List\<Station>> processedPaths, boolean verbose, int capacity, boolean analyseCapacity)
Process the available paths for the specified starting and destination stations. If the number of stations exceed the *capacity*, it will be ignored. 

If **verbose** is set to true, it will display all the available paths. This output is only good for debugging. If you need to generate a nicer display, just override the Station.toString() method or manually change the display in this method.

Setting **analyseCapacity** to true will give details about the ignored paths.

### 3. assets::GPS
#### public Station[] findChosenPath(ArrayList<java.util.List\<Station>> paths_2_filter, String check_start_name, String check_destination_name)
This is the last processing step. This will chose the shortest path for the Delivery Agent. This only checks for the starting and destination stations.

This method utilizes the **COP** concept.

#### public Station[] findChosenPath(ArrayList<java.util.List\<Station>> paths_2_filter, ArrayList\<Station> stationNames, int capacity)
This is the last processing step. Unlike the method above, this method will try to find a path that includes all the stations (according to sequence) specified in *stationNames*. Take note that this method doesn't call the above method eventhough it should :) If the path is invalid or no path can be determined for the specied stations, *null* is returned

This method utilizes the **CSP** concept.

This method was suppose to refine the above method, but it ended up an idependent method :)

*Take note that this algorithm doesn't guarantee correct results all the time, if any paths are situated in a looping network, it is very likely for the algorithm to fail. This has something to do with the way all the assets were coded.

#### private static Station[] schwartzianTransform(ArrayList<java.util.List\<Station>> paths_2_filter)
Schwartzian transformation used by *findChosenPath* (COP) method

#### private static Station[] correctedDirection(Station[] shortestPath, String check_start_name, String check_destination_name)
This method tries to correct the direction of the chosen path if the filtered path is not in the correct order. It's either in the correct order of the other way round.

#### public static void drawMap(Station[] stations)
Draws all the connected stations and the details on the screen. The map might be gone as soon as it leaves the actual computer screen. This can b esolved by clicking the background (pink area) to repaint the map.

### 4. assets::Screen
This class extends the **Frame** class and is used by **GPS**. The graphics is generated using java.awt.
This class should be made into a local variable inside assets::GPS, but anyway

#### Screen ()
This sets the title/name of the program and overrides the following listeners:
 - mousePressed(MouseEvent e)
   - to cause the map to be redrawn if the gui window is minimized and open again
   - sendMap() and repaint() is called here

This method also creates all the necessary components for the GUI
 
#### public void sendMap(CityMap map)
This method will process the iterator of all the values of CityMap's stationDictionary variable and pass the whole map to the class for repainting the screen

#### public void renderGraphics() 
Renders the display. The city map will be drawn.

#### private Panel renderStationListLabel()
Create the panel for the label with the text \~\~STATION LIST\~\~ at the top right corner of the screen

#### private Panel renderStationList()
Creates the panel for the list of station names. This is a selection box

This method will be called by *renderGraphics()*

#### private java.awt.List poolList()
Called by *renderStationList()* mentioned above. This is the data/names of the stations

This method will be called by *renderStationList()* to populate the list.

*Take note that the return type might conflict with java.util.List

#### private Panel renderRegisterRouteButton(Button registerRouteButton, Button clearRouteButton)
Creates the panel for the 2 buttons--register route and clear route.

*registerRouteButton* and *clearRouteButton* correspond to the "Register Route" and "Clear Route" buttons respectivly. This passing of parameters is to make the action listener work.

This method will be called by *renderGraphics()*

#### private Panel renderDetails()
Creates the panel for entering the capacity, weight, and algorithm.

This method will be called by *renderGraphics()*

#### private Panel renderRouteDisplay(TextField routeTF)
Creates the text box on the bottom of the screen. This is to display the stations selected (in sequence) based on the selection list

*routeTF* is the texf field which must be set in the Screen class for the action listener to work.

This method will be called by *renderGraphics()*

#### private Panel renderRouteButton(Button routeButton)
Creates the button with the word "Route" to find the path based on the selected station names.

*routeButton* is the button with the word "Route" which must be set in the Screen class for the action listener to work.

This method will be called by *renderGraphics()*

#### public void setChosenPathToHighlight(Station[] chosenPath)
This method draw/paints the *chosenPath* on the screen with another color after being determined by any of the algorithm

#### public int getConstraintData()
This is the actual capacity or the weight

#### public String getConstraintChoice()
This is the type of constrains selected in the GUI. It's either "Capacity" or "Weight". You might need to do some text transformation (based on your preferences) when dealing with them in the *ConstraintItemListener* inner class

#### public String getSelectedAlgorithm()
This is the name of the algorithm selected in the GUI. It's either "COP" or "CSP". You might need to do some text transformation (based on your preferences) when dealing with them in the *SelectedAlgorithmItemListener* inner class

#### public ArrayList\<Station> getSelectedStations()
Returns the station selected in the GUI if the "Register Route" button is pressed. It is assumed that when this method is called, the user has actually selected the desired stations and registered them into the system.

#### public void paint(Graphics g)
Overriden to draw the stations and the connecting lines as well as the name of the stations.

#### private class ButtonListener
This inner class implements ActionListener. This is used for the buttons.

#### private class ListListener 
This inner classimplements ItemListener. This is used for the station list.

#### private class ConstraintItemListener
This inner class implements ItemListener. This is used for the constraint selection thingy (aka. Choice).

#### private class ConstraintTFActionListener
This inner class implements ActionListener. This is used to get the entered value for the capacity or weight

#### private class SelectedAlgorithmItemListener
This inner class implements ItemListener. This is used to get the selected algorithm.

### 5. TestScript.java
This is the general script for testing the other classes. This program includes several testing features so that the user/programmer doesn't need to manually write codes to perform tests.
Some useful codes are also included here.

All you need to do is just edit, compile and run this file and add on stuff if you want to.

The main part includes the codes to use all the assets as well as displaying the graphics.

#### private static void test_stationDictionary(CityMap map, String lookup_name)
#### private static void test_stationDictionary(CityMap map, String[] lookup_names)
Checks if the specified *Station* name is valid or not. If the name is not valid, it will display "null". The output is pretty self-explanatory :)

These 2 methods can be written in a more efficient way by directly accessing the *CityMap* object's _stationDictionary_ static HashMap<String, Station> variable.
