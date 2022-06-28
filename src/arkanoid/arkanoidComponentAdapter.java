package arkanoid;



public abstract class arkanoidComponentAdapter implements arkanoidComponent{

	private double speed = 1;
	private double maxSpeed = 8;	
	
	public void setSpeed(double speed) {
		this.speed = speed;
	}
	
	public void setMaxSpeed(double maxSpeed) {
		this.maxSpeed = maxSpeed;
	}
	
	public double getSpeed() {
		return speed;
	}
	
	public double getMaxSpeed() {
		return maxSpeed;
	}
	
	public void incrementSpeed() {
		speed+=0.5;
		if(speed > maxSpeed) speed = maxSpeed;
	}
	
	public void decrementSpeed() {
		speed-=0.5;
		if(speed < 0) speed = 0;
	}

	
}
