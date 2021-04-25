# MapSearching
Codes and assets for search algorithms

Under assets is the codes for the map
1. Station.java
2. CityMap.java (compiling this file will generate more than one class file)
3. GPS.java (compiling this file will generate another class file "Screen")

##### Compile only CityMap.java, the Station class will be compiled together.

## Classes & Methods
### 1. Station
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
This method is overridden
#### public void showDetials()
Call *toString()* and adds on more details


### 2. CityMap
#### CityMap()
The default constructor

#### public void createBridges(Station s1, Station s2, Station s3)
Connects 3 *Station*s in one go. This is the maximum stations that can be connected **through this method**. See *displayFullNetwork()* method to link more than 3 *Station*s.

#### public void createBridges(Station s1, Station s2)
Connects 2 *Stations* in one go. This is the minimum stations that can be connected.

#### public ArrayList<List\<Station>> displayPartialNetwork()
Displays all the connections created by the *creteBridges()* methods. This only displays the partial network. There might still be some connections that can be combined into a single connection. See also *displayFullNetwork()*
  
#### public void displayFullNetwork(ArrayList<List\<Station>> _partialNetworks, int maxLayers)
Display the combined-once partial network. This method calls the private method *linkAllConnectingStations()* to combine the bridges. See *linkAllConnectingStations()* for more details.

*maxLayers* is the number of layers to be proccessed. Draw your map on a piece of papaer first and then determine how many layers it will be :) A branching network will caused an extra layer to exists.

This method will store every path in *_partialNetworks* into a new ArrayList<List<Station>>, any linking partial network will then be processed and added in. This means that there might be some "redundant" partial paths (before linking then together) included which might be useful depending on the use case.

#### private ArrayList<List\<Station>> linkAllConnectingStations(ArrayList<List\<Station>> _partialNetworks)
This method combines the partial networks by looping through the ArrayList ONCE only and each *List<Station>* within will be checked against another *List<Station>*. Therefore, only 2 linking partial networks will be combined together each time. If there are more than 2 partial networks that shuold be linked toegther, only the first 2 (that the JVM happen to see) will be processed. This methods returns the same list as the *displayFullNetwork()* method. This method can be called as many times as needed.
  
#### public void showSearchPath(Station start, Station destination)
(*Unimplemented*) Displays the path of the starting location to the destination.


### 3. GPS
Just compile and run this script :)

### 4. Screen
This class extends the **Frame** class and is used by **GPS**.
Currently, everything is hard-coded and this class is put in the same source file as GPS.java :)

**THIS CLASS SHOULD ONLY BE CALLED INSIDE OF GPS AND NOWHERE ELSE**
#### Screen ()
This will automatically set the window with dimension of 400px by 400px.

#### public void renderGraphics(int x, int y, int w, int h) 
(Under development) Renders the display. Some circles will be displayed.
x & y are the location of where the window should display
w & h are the width and height of the screen

#### public void paint(Graphics g)
Overriden to draw some circles at random location on the screen.
