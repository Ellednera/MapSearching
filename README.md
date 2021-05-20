# JADE-Cars MapSearching
Codes and assets for search algorithms. The codes here are meant to suite the project. For the stand-alone codes, see [MapSearching](https://github.com/Ellednera/MapSearching)

Structure of the files:
 - ./assets/Station.java
 - ./assets/CityMap.java
 - ./assets/GPS.java
 - ./TestScript.java - can be run independently, this is used to test the GUI and includes examples of how to use the algorithms

## Classes & Methods
### 1. assets::Station
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
#### public CityMap()
The default constructor. This will set and connect all the Stations. You can also add-on connections if you want to :)

The number of layers by default is now 5 instaed of 3

#### public void createBridges(Station s1, Station s2)
Connects 2 *Stations* in one go. This is the minimum stations that can be connected.

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

#### public Screen ()
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
Create the panel for the label with the text "\~~STATION LIST\~~" at the top right corner of the screen

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

The main part includes the codes to use all the assets as well as displaying the graphics. For the algorithm part, just copy and past the codes indicated with **// COP** and **// CSP**

#### private static void test_stationDictionary(CityMap map, String lookup_name)
#### private static void test_stationDictionary(CityMap map, String[] lookup_names)
Checks if the specified *Station* name is valid or not. If the name is not valid, it will display "null". The output is pretty self-explanatory :)

These 2 methods can be written in a more efficient way by directly accessing the *CityMap* object's _stationDictionary_ static HashMap<String, Station> variable.
