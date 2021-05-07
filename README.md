# JADE-Cars MapSearching
Codes and assets for search algorithms. The codes here are meant to suite the project. For the stand-alone codes, see [MapSearching](https://github.com/Ellednera/MapSearching)

Structure for the codes:
 - ./assets/Station.java
 - ./assets/CityMap.java
 - ./assets/GPS.java
 - ./TestScript.java - can be run independently, this is used to test the GUI

## Classes & Methods
### 1. assets::Station
#### Station(int x, int y, String name)
This is the only constructor
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
#### public CityMap()
The default constructor. This will set and connect all the Stations. This constructor **MUST** be called if you're using the *GPS*. You can still add on connections if you want to :)

#### public void createBridges(Station s1, Station s2, Station s3)
Connects 3 *Station*s in one go. This is the maximum stations that can be connected **through this method**. See *processFullNetwork()* method to link more than 3 *Station*s.

#### public void createBridges(Station s1, Station s2)
Connects 2 *Stations* in one go. This is the minimum stations that can be connected.

#### private void updateStationRecord(Station s1, Station s2)
#### private void updateStationRecord(Station s1, Station s2, Station s3)
These two methods will be called by the two *createBridges* methods. The record is needed for the actual searching based on user input.

#### public ArrayList<List\<Station>> processPartialNetwork(boolean verbose)
Displays all the connections created by the *creteBridges()* methods. This only displays the partial network. There might still be some connections that can be combined into a single connection. See also *processFullNetwork()*

If **verbose** is set to true, it will show all the partial networks that will go throught eh linking process.
  
#### public ArrayList<List\<Station>> processFullNetwork(ArrayList<List\<Station>> _partialNetworks, int maxLayers, boolean verbose)
Display the combined-once partial network. This method calls the private method *linkAllConnectingStations()* to combine the bridges. See *linkAllConnectingStations()* for more details.

*maxLayers* is the number of layers to be proccessed. Draw your map on a piece of papaer first and then determine how many layers it will be :) A branching network will caused an extra layer to exists.

If **verbose** is set to true, it will notify that there are 2 partial networks that can be link for each iteration.

This method will store every path in *_partialNetworks* into a new ArrayList<List<Station>>, any linking partial network will then be processed and added in. This means that there might be some "redundant" partial paths (before linking then together) included which might be useful depending on the use case.

#### private ArrayList<List\<Station>> linkAllConnectingStations(ArrayList<List\<Station>> _partialNetworks, boolean verbose)
This method combines the partial networks by looping through the ArrayList ONCE only and each *List<Station>* within will be checked against another *List<Station>*. Therefore, only 2 linking partial networks will be combined together each time. If there are more than 2 partial networks that shuold be linked toegther, only the first 2 (that the JVM happen to see) will be processed. This methods returns the same list as the *processFullNetwork()* method. This method can be called as many times as needed.

If **verbose** is set to true, it will show all the paths that meets the requirements ie. paths that include the specified start and destination stations.

*This method should be optimized further in the future (The starting station should be in the first record while the destination station should be in the last record and not somewhere in the middle due to some simple geometry concepts)

#### public ArrayList<List\<Station>> showAvailablePaths(String start_name, String destination_name, ArrayList<List\<Station>> processedPaths, boolean verbose, int capacity, boolean analyseCapacity)
Process the available paths for the specified starting and destination stations. If the number of stations exceed the *capacity*, it will be ignored. 

If **verbose** is set to true, it will display all the available paths. This output is only good for debugging. If you need to generate a nicer display, just override the Station.toString() method or manually change the display in this method.

Setting **analyseCapacity** to true will give details about the ignored paths.

### 3. assets::GPS
Just compile and run this script, and a gui will appear :)

#### private static Station[] findChosenPath(ArrayList<java.util.List\<Station>> paths_2_filter, String check_start_name, String check_destination_name)
This is the last processing step. This will chose the shortest path for the Delivery Agent.

*Search includes COP currently, CSP will be added in the future

#### private static Station[] schwartzianTransform(ArrayList<java.util.List\<Station>> paths_2_filter)
Schwartzian transformation used by *findChosenPath* method

#### private static Station[] correctedDirection(Station[] shortestPath, String check_start_name, String check_destination_name)
This method tries to correct the direction of the chosen path if the filtered path is not in the correct order. It's either in the correct order of the other way round.

#### public static void drawMap(Station[] stations)
Draws all the connected stations and the details on the screen. Uncomment the commented connections in CityMap's constructor to see more stations appear.

### 4. assets::Screen
This class extends the **Frame** class and is used by **GPS**.
This class should be made into a local variable inside assets::GPS, but anyway

#### public Screen ()
This sets the title/name of the program and overrides the following listeners:
 - mousePressed(MouseEvent e)
   - to cause the map to be redrawn if the gui window is minimized and open again
   - sendMap() and repaint() is called here
 
#### public void sendMap(CityMap map)
This method will process the iterator of all the values of CityMap's stationDictionary variable and pass the whole map to the class for repainting the screen

#### public void renderGraphics(int x, int y, int w, int h) 
(Under development) Renders the display. Some circles will be displayed.


x & y are the location of where the window should display


w & h are the width and height of the screen



#### public void paint(Graphics g)
Overriden to can draw the stations and the connecting lines as well as the name of the stations.

### 5. TestScript.java
This is the general script for testing the other classes. This program includes several testing features so that the user/programmer doesn't need to manually write codes to perform tests.
Some useful codes are also included here.

All you need to do is just edit, compile and run this file and add on stuff if you want to.

#### private static void test_stationDictionary(CityMap map, String lookup_name)
#### private static void test_stationDictionary(CityMap map, String[] lookup_names)
Checks if the specified *Station* name is valid or not. If the name is not valid, it will display "null". The output is pretty self-explanatory :)

These 2 methods can be written in a more efficient way by directly accessing the *CityMap* object's _stationDictionary_ static HashMap<String, Station> variable.
