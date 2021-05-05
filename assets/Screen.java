package assets;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class Screen extends Frame {
	private Station dummy = new Station(300, 300, "Dummy");
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
		setLayout(new GridLayout(2, 1));

		setVisible(true);
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
			g.setColor(Color.WHITE);
			Iterator<Station> connected_it  = station.allConnections().keySet().iterator();
			while (connected_it.hasNext()) {
				Station connectedStation = connected_it.next();
				g.drawLine(station.x + stationRadius, station.y +stationRadius, 	
							connectedStation.x +stationRadius , connectedStation.y +stationRadius);
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