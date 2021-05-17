package assets;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class Screen extends Frame {
	//private Station dummy = new Station(300, 300, "Dummy");
	private Iterator<Station> stations_it = null;
	private CityMap localMap = null;
	
	private int stationDiameter = 20;
	private int stationRadius = stationDiameter / 2;
	
	public Screen() {
		super("COS30018 Vehicle Routing System - GPS");
		// listeners
		// test minimixing and maximixing
		
		/*
		// doesn't do much, just click the screen and the graphic will come back
		this.addWindowListener(new WindowAdapter() {
			public void windowIconified(WindowEvent e) {
				// repaint
				Screen gps_screen = (Screen) e.getSource();
				gps_screen.sendMap(localMap);
				gps_screen.repaint();
				//System.exit(-1);
			}
		});
		*/
		
		this.addMouseListener( new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				Screen gps_screen = (Screen) e.getSource();
				gps_screen.sendMap(localMap);
				gps_screen.repaint();
			}
		} );
		
		
		// this will cause everything to terminate
		/*this.addWindowListener( new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				// setVisible(false);
				System.exit(-1);
			}
		} );*/
	}
	
	public void sendMap(CityMap map) {
		this.stations_it = map.stationDictionary.values().iterator();
		this.localMap = map;
	}
	
	public void renderGraphics(int x, int y, int w, int h) {
		setBounds(x, y, w, h);
		setBackground(Color.PINK);
		setLayout(null);

		Panel stationList = renderStationList();
		Panel details = renderDetails();
		Panel routeDisplay = renderRouteDisplay();
		Panel routeButton = renderRouteButton();
		
		add(stationList); add(details); add(routeDisplay); add(routeButton);
		
		setVisible(true);
	}
	
	private Panel renderStationList() {
		Panel stationList = new Panel();
		stationList.setBounds(500, 30, 180, 250);
		stationList.setLayout(new GridLayout(1, 1));
		stationList.setVisible(true);
		stationList.setBackground(Color.BLUE);
		
		java.awt.List stations = poolList();
		stations.setMultipleMode(true);
		
		stationList.add(stations);
		
		return stationList;
	}
	
	private java.awt.List poolList() {
		
		java.awt.List stations = new java.awt.List();
		ArrayList<String> stationNames2sort = new ArrayList<String>();;
		
		Iterator<String> it =  localMap.stationDictionary.keySet().iterator();
		
		for (int i=0; it.hasNext(); i++) {
			stationNames2sort.add( it.next() );
		}
		Collections.sort(stationNames2sort);
		
		for (int i=0; i <= stationNames2sort.size()-1; i++) {
			stations.add(stationNames2sort.get(i));
		}
		
		return stations;
	}
	
	// need to change the algorithm to select box
	private Panel renderDetails() {
		Panel details = new Panel(); 
		details.setBounds(500, 300, 180, 100);
		details.setLayout(new GridLayout(3, 2));
		details.setVisible(true);
		details.setBackground(Color.CYAN);
		
		Label capacity = new Label();
		capacity.setText("Capacity: ");
		TextField capacityTF = new TextField();
		
		Label weight = new Label();
		weight.setText("Weight (kg): ");
		TextField weightTF = new TextField();
		
		Label algorithm = new Label();
		algorithm.setText("Algorithm: ");
		Choice algorithmMenu = new Choice();
		algorithmMenu.add("CSP");
		algorithmMenu.add("COP");
		algorithmMenu.add("ACO");
		
		details.add(capacity); details.add(capacityTF);
		details.add(weight); details.add(weightTF);
		details.add(algorithm); details.add(algorithmMenu);
		
		return details;
	}
	
	private Panel renderRouteDisplay() {
		Panel routeDisplay = new Panel();
		routeDisplay.setBounds(0, 500, 680, 100);
		routeDisplay.setLayout(new GridLayout(1, 1));
		routeDisplay.setVisible(true);
		routeDisplay.setBackground(Color.YELLOW);
		
		// TextField
		TextField routeTF = new TextField();
		routeTF.setBackground(new Color(250, 150, 250));
		routeTF.setEnabled(false);
		routeTF.setText("Company->");
		
		routeDisplay.add(routeTF);
		
		return routeDisplay;
	}
	
	private Panel renderRouteButton() {
		
		Panel routeButtonPanel = new Panel();
		routeButtonPanel.setBounds(500, 450, 180, 50);
		routeButtonPanel.setLayout(new BorderLayout());
		routeButtonPanel.setVisible(true);
		routeButtonPanel.setBackground(Color.ORANGE);
		
		Button routeButton = new Button("Route");
		routeButton.setActionCommand("route");
		routeButtonPanel.add(routeButton, BorderLayout.CENTER);
		
		return routeButtonPanel;
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
			Iterator<Station> connected_it  = station.allConnections().keySet().iterator();
			while (connected_it.hasNext()) {
				Station connectedStation = connected_it.next();
				
				// road
				g.setColor(Color.WHITE);
				g.drawLine(station.x + stationRadius, station.y +stationRadius, 	
							connectedStation.x +stationRadius , connectedStation.y +stationRadius);
				
				// distance
				g.setColor(Color.MAGENTA);
				g.drawString(String.valueOf(station.distanceFrom(connectedStation)), 
						(station.x + connectedStation.x)/2, (station.y + connectedStation.y)/2);
			}
		}
		
		// to test repaint() logic
		/*
		g.setColor(Color.WHITE);
		g.fillOval(dummy.x, (int)(Math.random()*500 + 1), stationDiameter, stationDiameter);
		g.drawString(dummy.name, dummy.x + 20, dummy.y + 10);
		*/
		
		g.setColor(c);		
	}
}