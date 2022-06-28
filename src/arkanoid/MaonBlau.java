package arkanoid;

import java.awt.Color;
import java.awt.Rectangle;

public class MaonBlau extends Maon{
	
	private Raqueta racquet;
	public static Thread speedRun;
	public static long sleepInit;
	private static boolean pause = false;
	private static boolean stop = false;
	
	public MaonBlau(Rectangle r, Raqueta racquet, arkanoid arkanoid) {
		super(r, 1, Color.BLUE, arkanoid);
		this.racquet = racquet;
	}

	public void breakIt() {
		die();
		
		sleepInit = System.nanoTime();
		
		if(speedRun == null || !speedRun.isAlive()) {	
			
			speedRun = new Thread(new Runnable() {
				
				public void run() {
	
					racquet.setSpeed(racquet.getSpeed()*2);
										
					while((System.nanoTime() - sleepInit) / 1000000000 < 10 && !stop) {
						try {
							Thread.sleep(1);
						} catch (InterruptedException e) {
							Thread.currentThread().interrupt();
						}
						if(pause)	
							sleepInit = System.nanoTime() - (10 - (int)((System.nanoTime() - sleepInit) / 1000000000))*1000000000;
												
					}
															
					racquet.setSpeed(racquet.getSpeed()/2);
					
				}
			});
		
			speedRun.start();
			
		}
		
	}
	
	public static void pause() {
		pause = true;
	}
	
	public static void stop() {
		
		if(speedRun != null && speedRun.isAlive()) {
			
			stop = true;
			
			try {
				speedRun.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			stop = false;
			
		}
		
	}
	
	public static void resume() {
		pause = false;
	}

	public void move() {}
	
}
