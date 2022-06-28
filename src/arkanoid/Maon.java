package arkanoid;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

public abstract class Maon extends arkanoidComponentAdapter{

	private int live;
	private int x, y, width, heigh;
	private Color c;
	private boolean die;
	private int points;
	protected arkanoid arkanoid;
	
	public Maon(Rectangle r, int live, Color c, arkanoid arkanoid) {
		
		x = (int)r.getX();
		y = (int)r.getY();
		width = (int)r.getWidth();
		heigh = (int)r.getHeight();
		this.c = c;
		this.live = live;
		this.arkanoid = arkanoid;
		die = false;
		points = live * 10;
		
	}
	
	public void hit() {
		
		live--;
				
		setColor(getColor().darker());
		
		if(live == 0) {
			arkanoid.addScore(points);
			breakIt();
		}
		
	}
	
	public int lives() {
		return live;
	}
	
	public Color getColor() {
		return c;
	}
	
	public void setColor(Color c) {
		this.c = c;
	}
	
	public void die() {
		die = true;
	}
	
	public boolean isDie() {
		return die;
	}
	
	public abstract void breakIt();
	
	public void paint(Graphics2D g2) {
		
		Color preColor = g2.getColor();
		g2.setColor(c);
		g2.fillRect(x, y, width, heigh);
		g2.setColor(preColor);
		
	}
	
	public abstract void move();
	
	public Rectangle getBounds() {
		return new Rectangle(x, y, width, heigh);
	}
	
	public void setPoint(Point p) {
		x = (int)p.getX();
		y = (int)p.getY();
	}

	public Point getPoint() {
		return new Point(x, y);
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public boolean intersects(arkanoidComponent c) {
		
		return getBounds().intersects(c.getBounds());
		
	}
	
	
}
