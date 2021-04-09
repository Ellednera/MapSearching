# MapSearching
Codes and assets for search algorithms

Under assets is the codes for the map
1. Station.java

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
