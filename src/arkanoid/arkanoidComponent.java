package arkanoid;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Point;

public interface arkanoidComponent {

	
	public void paint(Graphics2D g2);
	
	public void move();
	
	public Rectangle getBounds();
	
	public void setPoint(Point p);
	public Point getPoint();
	
	public void incrementSpeed();
	public void decrementSpeed();
	public void setSpeed(double speed);
	public double getSpeed();
	
	
}
