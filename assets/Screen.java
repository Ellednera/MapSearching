package assets;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class Screen extends Frame {
	//private Station dummy = new Station(300, 300, "Dummy");
	private Iterator<Station> stations_it = null;
	private CityMap localMap = null;
	// private Station[] chosenPathToHighlight;
	
	private int stationDiameter = 20;
	private int stationRadius = stationDiameter / 2;
	
	private java.awt.List stationList = new java.awt.List();
	private Button routeButton = new Button("Route");
	private TextField routeTF = new TextField();
	private ArrayList<String> selectedStationNames = new ArrayList<String>();
	private Button registerRouteButton = new Button("Register Route");
	private Button clearRouteButton = new Button("Clear route");
	
	public Screen() {
		
		super("COS30018 Vehicle Routing System - GPS");
		// listeners		
		this.addMouseListener( new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				Screen gps_screen = (Screen) e.getSource();
				gps_screen.sendMap(localMap);
				gps_screen.repaint();
			}
		} );
		
		// add listeners
		routeButton.addActionListener( new ButtonListener() );
		registerRouteButton.addActionListener( new ButtonListener() );
		clearRouteButton.addActionListener( new ButtonListener() );
		// stationList.addItemListener( new ListListener() ); // this must go into renderStationList()
		selectedStationNames.add("C");

	}
	
	public void sendMap(CityMap map) {
		this.stations_it = map.stationDictionary.values().iterator();
		this.localMap = map;
	}
	
	public void renderGraphics(int x, int y, int w, int h) {
		setBounds(x, y, w, h);
		setBackground(Color.PINK);
		setLayout(null);

		Panel stationListPanel = renderStationList();
		Panel details = renderDetails();
		Panel routeDisplay = renderRouteDisplay(this.routeTF);
		Panel routeButton = renderRouteButton(this.routeButton);
		Panel registerRoute = renderRegisterRouteButton(this.registerRouteButton, this.clearRouteButton);
		
		add(stationListPanel); add(details); add(routeDisplay); add(routeButton); add(registerRoute);
		
		setVisible(true);
	}
	
	private Panel renderStationList() {
		Panel stationListPanel = new Panel();
		stationListPanel.setBounds(500, 30, 180, 250);
		stationListPanel.setLayout(new GridLayout(1, 1));
		stationListPanel.setVisible(true);
		stationListPanel.setBackground(Color.BLUE);
		
		this.stationList = poolList();
		this.stationList.setMultipleMode(true);
		this.stationList.addItemListener( new ListListener() );
		stationListPanel.add(this.stationList);
		
		return stationListPanel;
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
	
	private Panel renderDetails() {
		Panel details = new Panel(); 
		details.setBounds(500, 345, 180, 100);
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
	
	private Panel renderRouteDisplay(TextField routeTF) {
		Panel routeDisplay = new Panel();
		routeDisplay.setBounds(8, 500, 680, 25);
		routeDisplay.setLayout(new GridLayout(1, 1));
		routeDisplay.setVisible(true);
		routeDisplay.setBackground(Color.YELLOW);
		
		// TextField
		routeTF.setBackground(new Color(250, 150, 250));
		routeTF.setEnabled(false);
		routeTF.setText("Select the stations on the top right panel and click ''Register Route");
		
		routeDisplay.add(routeTF);
		
		return routeDisplay;
	}
	
	private Panel renderRouteButton(Button routeButton) {
		
		Panel routeButtonPanel = new Panel();
		routeButtonPanel.setBounds(500, 450, 180, 50);
		routeButtonPanel.setLayout(new BorderLayout());
		routeButtonPanel.setVisible(true);
		routeButtonPanel.setBackground(Color.ORANGE);
		
		routeButton.setActionCommand("route");
		routeButtonPanel.add(routeButton, BorderLayout.CENTER);
		
		return routeButtonPanel;
	}
	
	private Panel renderRegisterRouteButton(Button registerRouteButton, Button clearRouteButton) {
		Panel registerRouteButtonPanel = new Panel();
		registerRouteButtonPanel.setBounds(500, 280, 180, 50);
		registerRouteButtonPanel.setLayout(new GridLayout(1, 2));
		registerRouteButtonPanel.setVisible(true);
		registerRouteButtonPanel.setBackground(Color.GREEN);
		
		registerRouteButton.setActionCommand("register route");
		clearRouteButton.setActionCommand("clear route");
		
		registerRouteButtonPanel.add(registerRouteButton);
		registerRouteButtonPanel.add(clearRouteButton);
		
		return registerRouteButtonPanel;
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
	
	private class ButtonListener implements ActionListener {
		
		public void actionPerformed(ActionEvent e) {
			//System.out.println("Button pressed!");
			Button someButton = (Button) e.getSource();
			
			// we might still have some more buttons
			if (someButton.getActionCommand() == "route") {
				
				System.out.println("Route button pressed");
				
			} else if (someButton.getActionCommand() == "register route") {
				
				System.out.println("Register route button pressed");
				System.out.println("Selected stations: " + selectedStationNames);
				
				routeTF.setText(selectedStationNames.toString());
				
			} else if (someButton.getActionCommand() == "clear route") {
				
				System.out.println("Route cleared");
				
				selectedStationNames = new ArrayList<String>();
				selectedStationNames.add("C");
				routeTF.setText(selectedStationNames.toString());
				
				System.out.println("Selected stations: " + selectedStationNames);
				
			}
		}
	}
	
	private class ListListener implements ItemListener {
		public void itemStateChanged(ItemEvent e) {
			java.awt.List selection = (java.awt.List) e.getSource();
			System.out.println(selection.getSelectedItem() + " was selected");
			
			if (selection.getSelectedItem() != null) {
				selectedStationNames.add(selection.getSelectedItem());
			}
			
			selection.deselect(selection.getSelectedIndex());
		}
	}
	
}