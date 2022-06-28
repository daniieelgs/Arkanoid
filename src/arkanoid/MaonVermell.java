package arkanoid;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;

public class MaonVermell extends Maon{

	private Raqueta racquet;
	private boolean broken;
		
	private Color secondColor;
		
	private long initTime;
	
	public MaonVermell(Rectangle r, Raqueta racquet, arkanoid arkanoid) {
		super(r, 2, Color.RED, arkanoid);
		this.racquet = racquet;
		setSpeed(2);
		secondColor = new Color(Color.RED.getRed(), Color.RED.getGreen(), Color.RED.getBlue(), Color.RED.getAlpha()/2);
		broken = false;
	}

	public void breakIt() {
		broken = true;
		setColor(Color.RED);
		initTime = System.nanoTime();
	}

	public boolean intersects(arkanoidComponent c) {
		
		if(broken) return false;
		
		return super.intersects(c);
		
	}
	
	public void move() {
				
		if(broken) {
						
			setPoint(new Point(getX(), (int)(getY()+getSpeed())));

			if((System.nanoTime() - initTime)/1000000 >= 200) {
				setColor(getColor().equals(Color.RED) ? secondColor : Color.RED);
				initTime = System.nanoTime();
			}
			
			if(getY() > racquet.getBounds().getY()+racquet.getBounds().getHeight()+50) die();
			else if(getBounds().intersects(racquet.getBounds())) {
				die();
				arkanoid.gameOver();
			}

		}
	}
	
}
