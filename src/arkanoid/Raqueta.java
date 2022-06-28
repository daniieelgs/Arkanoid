package arkanoid;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

public class Raqueta extends arkanoidComponentAdapter{

	private final int HEIGHT = 5;
	private final int WIDTH = 60;
	
	private double y = 0, x = 0, xa = 0;
	
	private arkanoid content;
	
	public Raqueta(arkanoid content) {
		
		y = content.getHeight() - 50 - HEIGHT;
		
		setSpeed(3);
		
		this.content = content;
		
	}
	
	public void paint(Graphics2D g2) {
		Color preColor = g2.getColor();
		g2.setColor(Color.BLUE);
		//g2.fillOval((int)x, (int)y, WIDTH, HEIGHT);
		g2.fillRoundRect((int)x, (int)y, WIDTH, HEIGHT, 10, 10);
		g2.setColor(preColor);
	}

	public void move() {
		
		if(x + xa > 0 && x + xa < content.getWidth() - WIDTH) x = x + xa;
		
	}
	
	public void stopMove() {
		xa = 0;
	}
	
	public void moveLeft() {
		xa = -getSpeed();
	}
	
	public void moveRight() {
		xa = getSpeed();
	}
	
	public boolean movingLeft() {
		return xa < 0;
	}
	
	public boolean movingRight() {
		return xa > 0;
	}

	public Rectangle getBounds() {
		return new Rectangle((int)x, (int)y, WIDTH, HEIGHT);
	}


	public void setPoint(Point p) {
		x = p.getX();
		y = p.getY();
	}

	public Point getPoint() {
		return new Point((int)x, (int)y);
	}

}
