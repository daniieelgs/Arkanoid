package arkanoid;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.List;

public class Pilota extends arkanoidComponentAdapter{
	
	private arkanoid content;
	private Raqueta racquet; 
	
	private final int VERTICAL = 4, HORIZONTAL = 5, ESQUINA = 6;
	
	public static final int DIAMETER = 15;
	private double x = 0, y = 0, xa = 1, ya = 1;
	private Color colorBall;
		
	public Pilota(Raqueta racquet, arkanoid content) {
		
		this.racquet = racquet;
		this.content = content;
				
		setSpeed(2);
		
		colorBall = Color.WHITE.darker();
				
		x = racquet.getBounds().getX() + (racquet.getBounds().getWidth()/2 - DIAMETER/2);
		y = racquet.getBounds().getY() - DIAMETER;
		ya = -getSpeed();
		xa = getSpeed();
		
	}
	
	public void paint(Graphics2D g2) {
		Color preColor = g2.getColor();
		g2.setColor(colorBall);
		g2.fillOval((int)x, (int)y, DIAMETER, DIAMETER);
		g2.setColor(preColor);
	}

	public void move() {
				
		if(x + xa > content.getWidth() - DIAMETER) xa = -getSpeed();
		else if(y + ya > content.getHeight() - DIAMETER) content.die();//ya = -getSpeed();
		else if (x + xa < 0) xa = getSpeed();
		else if (y + ya < 0) ya = getSpeed();
		else if(collisionRacqueta()) {

			y = racquet.getBounds().y - DIAMETER;
			ya = -getSpeed();
			
			if(racquet.movingLeft()) xa = -getSpeed();
			else if(racquet.movingRight()) xa = getSpeed();


		}else {
			
			int index = collisionMaons();
			
			if(index != -1) {
				
				Maon brick = content.getMaons().get(index);
				
				int typeCollision = collisionType(brick);

				if(typeCollision == VERTICAL) {
					ya = -ya;
				}else if(typeCollision == HORIZONTAL) {
					xa = -xa;
				}else if(typeCollision == ESQUINA) {
					xa = -xa;
					ya = -ya;
				}								
				
											
				brick.hit();
				
				if(brick.lives() == 0) content.incrementSpeed();
				
				
			}
			
		}
		
		x = x + xa;
		y = y + ya;
		
		
	}

	private int collisionMaons() {
		
		int index = 0;
		
		List<Maon> bricks = content.getMaons();
		
		while(index < bricks.size() && !bricks.get(index).intersects(this))
			index++;
		
		return index == bricks.size() ? -1 : index;
		
	}
	
	private int collisionType(arkanoidComponent c) {
		
		Rectangle r = c.getBounds();
		
		//final double error = getSpeed() + 0.5;
		
		//System.out.println(x + " - " + y + " : " + getSpeed() + " : "  + r.getX() + " - " + r.getY());
		//System.out.println((x+DIAMETER) + " - " + (y+DIAMETER) + " : " + error + " : "  + (r.getX()+r.getWidth()) + " - " + (r.getY()+r.getHeight()));

		int type = -1;
		
		x -= getSpeed();
		y -= getSpeed();
			
		int componentX = (int) Math.abs(x - r.getX());
		int componentY = (int) Math.abs(y - r.getY());
		
		if((componentX != 0 ? componentY / componentX : (componentY != 0 ? componentX / componentY : 0)) == 1)
			type = ESQUINA;
		else if(x > r.getX() && x + DIAMETER < r.getX()+r.getWidth())
			type = VERTICAL;
		else if(y < r.getY() && y + DIAMETER > r.getY()+r.getHeight())
			type = HORIZONTAL;
		else {
			
			if(xa < 0 && ya > 0) {
				
				if(r.getY() - y < DIAMETER) type = HORIZONTAL;
				else type = VERTICAL;
				
				
			}else if(xa < 0 && ya < 0) {
				
				if(y + DIAMETER - r.getY() + r.getHeight() < DIAMETER) type = HORIZONTAL;
				else type = VERTICAL;
				
				
			}else if(xa > 0 && ya < 0) {
				
				if(r.getX() - x < DIAMETER) type = VERTICAL;
				else type = HORIZONTAL;
				
			}else {
				
				if(r.getX() - x < DIAMETER) type = VERTICAL;
				else type = HORIZONTAL;		
				
			}
			
		}
		
		System.out.println("Type: " + (type == VERTICAL ? "Vertical" : (type == HORIZONTAL ? "Horizontal" : "Esquina")) + " - " + c.getClass().getName() +  " - ya: " + ya + " xa: " + xa + "{X: " + x + " Y: " + y + " Diametro: " + DIAMETER + "} / {X: " + r.getX() + " Y: " + r.getY() + " W: " + r.getWidth() + " H: " + r.getHeight() + "}");
				
		//TODO
		
		/*if(x + DIAMETER > r.getX() && x < r.getX()+r.getWidth() && y + DIAMETER > r.getY()-error && y < r.getY()+error) 
			type = TOP;
		else if(x + DIAMETER > r.getX() && x < r.getX()+r.getWidth() && y < r.getY()+r.getHeight()+error && y + DIAMETER > r.getY()+r.getHeight()-error)
			type = BOTTOM;
		else if(y + DIAMETER > r.getY() && y < r.getY()+r.getHeight() && x+DIAMETER > r.getX()-error && x <= r.getX()+error)
			type = LEFT;
		else if(y + DIAMETER > r.getY() && y < r.getY()+r.getHeight() && x < r.getX()+r.getWidth()+error && x+DIAMETER > r.getX()+r.getWidth()-error)
			type = RIGHT;*/
		
		/*if(x > r.getX() - DIAMETER + 0 &&
			    x < r.getX() + r.getWidth() - 0)
			type = VERTICAL;
		else
			type = HORIZONTAL;*/
		
		x += getSpeed();
		y += getSpeed();
		
		return type;
		
	}
	
	private boolean collisionRacqueta() {
		return racquet.getBounds().intersects(getBounds());
	}
	
	public Rectangle getBounds() {
		return new Rectangle((int)x, (int)y, DIAMETER, DIAMETER);
	}
	
	public void setDirection(double xa, double ya) {
		
		this.xa = xa;
		this.ya = ya;
		
	}
	
	public void setPoint(Point p) {
		x = p.getX();
		y = p.getY();
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	public void setX(int x) {
		this.x = x;
	}

	public Point getPoint() {
		return new Point((int)x, (int)y);
	}
	
	public double getDirectionX() {
		return xa;
	}
	
	public double getDirectionY() {
		return ya;
	}
	
}
