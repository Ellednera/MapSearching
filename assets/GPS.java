import java.awt.*;

public class GPS {
	public static void main (String args[]) {
		new Screen().renderGraphics(300, 300, 400, 400);
	}
}

class Screen extends Frame {
	Screen () {
		super("COS30019 Vehicle Routing System - GPS");
	}
	
	public void renderGraphics(int x, int y, int w, int h) {	
		setBounds(x, y, w, h);
		setBackground(Color.PINK);
		setLayout(new GridLayout(2, 1));

		setVisible(true);		

	}
	
	public void paint(Graphics g) {
		Color c = g.getColor();
		
		g.setColor(Color.WHITE);
		for (int i=0; i<10; i++) {
			g.fillOval((int)(Math.random() * 370), (int)(Math.random() * 370), 30, 30);
		}
		
		g.setColor(c);
		
	}

}
