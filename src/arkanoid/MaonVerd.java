package arkanoid;

import java.awt.Color;
import java.awt.Rectangle;

public class MaonVerd extends Maon{

	public MaonVerd(Rectangle r, arkanoid arkanoid) {
		super(r, 3, Color.GREEN, arkanoid);
	}

	public void breakIt() {
		die();
	}

	public void move() {}
	
}
