# MapSearching
Codes and assets for search algorithms

Under assets is the codes for the map
1. Station.java

## Classes & Methods
### 1. Station
#### Station(int x, int y, String name)
#### public void connectTo(Station s, int distance)
Connects the current *Station* to another *Station*. This eliminates the need of an edge. *Station* is the vertex :)
#### public boolean isConnectedTo(Station s)
#### public int distanceFrom(Station s)
#### public void verbose_distanceFrom(Station s)
More text :)
#### public Map<Station, Integer> allConnections()
#### public String toString()
This method is overridden
#### public void show_detials()
Call *toString()* and adds on more details
