# MapSearching
Codes and assets for search algorithms

Under assets is the codes for the map
1. Station.java
2. CityMap.java

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
  
#### public void displayFullNetwork(ArrayList<List\<Station>> _partialNetworks)
Display the combined-once partial network. This method calls the private method *linkAllConnectingStations()* to combine the bridges. See *linkAllConnectingStations()* for more details.


The resulting partial connection combined (ie. returned value) will contain at least 4 (ie. 2+2) or at most 6 (ie. 3+3) stations. There might still be some connections that can be combined into a single connection (full path with more than 6 stations connected together)

#### private ArrayList<List\<Station>> linkAllConnectingStations(ArrayList<List\<Station>> _partialNetworks)
This method combines the partial networks by looping through the ArrayList ONCE only and each *List<Station>* within will be checked against another *List<Station>*. Therefore, only 2 linking partial networks will be combined together each time. If there are more than 2 partial networks that shuold be linked toegther, only the first 2 (that the JVM happen to see) will be processed. This methods returns the same list as the *displayFullNetwork()* method. This means that the return value can be passed through *displayFullNetwork()* again to connect the remaining partial network or a recurssion can be done within this method.

This method might be made public in the future depending on how it will be used.
  
#### public void showSearchPath(Station start, Station destination)
(*Unimplemented*) Displays the path of the starting location to the destination.
