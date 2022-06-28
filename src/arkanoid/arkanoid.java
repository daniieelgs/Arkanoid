package arkanoid;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

public class arkanoid extends JPanel{

	private static final long serialVersionUID = 1L;
	
	private Pilota ball;
	private Raqueta racquet;
	
	private JFrame frame;
	
	private boolean play, pause, starting, die, win, finalScreen;
	private List<Maon> bricks;
	
	private int refresh;
	private int refreshNano;
	
	private int currentKeyCode;
	private int counterStarting;
	
	private JLabel score, lifes, user, recordScore;
	
	private int points, countLifes;
	
	private JButton resume, exit;
		
	private String userName;
	private int recordUser;
	
	private JSONReader data;
	
	private boolean efectRecord;
	
	private String txtDie, txtWin;
	
	public arkanoid() {
		
		startGame();
				
	}
	
	public List<Maon> getMaons(){
		return bricks;
	}
	
	public void restart() {
		
		countLifes = 3;
		points = 0;
		
		play = false;
		pause = false;
		starting = false;
		die = false;
		win = false;
		finalScreen = false;
		
		efectRecord = true;
		
		refresh = 15;
		refreshNano = 0;
		
		bricks = createMaons();
		
		racquet.setPoint(new Point(0, racquet.getPoint().y));
		
		Rectangle pointRacquet = racquet.getBounds();
		
		ball.setPoint(new Point(pointRacquet.x + pointRacquet.width/2  - Pilota.DIAMETER/2, pointRacquet.y - Pilota.DIAMETER));
		ball.setDirection(Math.abs(ball.getDirectionX()), -Math.abs(ball.getDirectionY()));
		
		lifes.setText(countLifes + " ♥");
		score.setText(points + " pt");
		
		lifes.setForeground(Color.GREEN);
		score.setForeground(Color.WHITE);
		
		user.setVisible(false);
		recordScore.setVisible(false);
		
		MaonBlau.stop();
		
	}
	
	private void startGame() {
		
		setLayout(null);
		
		frame = createFrame();

		racquet = new Raqueta(this);
		ball = new Pilota(racquet, this);
		
		restart();
										
		resume = new JButton("Resume");
		exit = new JButton("Exit");
		
		resume.setBounds((int)frame.getSize().getWidth()/2 - 90, (int)frame.getSize().getHeight()/2 + 24, 180, 50);
		resume.setFont(new Font("Verdana", Font.PLAIN, 30));
		resume.setOpaque(false);
		resume.setContentAreaFilled(false); 	
		resume.setCursor(new Cursor(Cursor.HAND_CURSOR));
		resume.setForeground(Color.GREEN.brighter());
		
		resume.setFocusable(false);
		
		exit.setBounds((int)frame.getSize().getWidth()/2 - 90, (int)frame.getSize().getHeight()/2 + 30 + resume.getHeight(), 180, 50);
		exit.setFont(new Font("Verdana", Font.PLAIN, 30));
		exit.setOpaque(false);
		exit.setContentAreaFilled(false); 	
		exit.setCursor(new Cursor(Cursor.HAND_CURSOR));
		exit.setForeground(Color.RED.brighter());
		
		resume.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				resume();
				loop();
			}
			
		});
		
		exit.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				save();
				restart();
				resume();
				repaint();
				login();
				
			}
			
		});
		
		frame.addFocusListener(new FocusListener() {
			
			public void focusGained(FocusEvent e) {
				if(pause && play) {
					resume();
					loop();
				}
			}

			public void focusLost(FocusEvent e) {
				if(play) pause();
			}
			
		});
		
		frame.addWindowListener(new WindowAdapter() {
		
			public void windowClosing(WindowEvent e) {
				
				save();
				
			}
		
		});
		
		frame.addKeyListener(new KeyAdapter() {
						
			public void keyReleased(KeyEvent e) {
				if(currentKeyCode == e.getKeyCode()) racquet.stopMove();
			}
			
			public void keyPressed(KeyEvent e) {
				currentKeyCode = e.getKeyCode();
								
				if(play && !starting && !pause) {
					if(currentKeyCode == KeyEvent.VK_LEFT || currentKeyCode == KeyEvent.VK_A)
						racquet.moveLeft();
					else if(currentKeyCode == KeyEvent.VK_RIGHT || currentKeyCode == KeyEvent.VK_D)
						racquet.moveRight();
					
				}
								
				if(e.getKeyCode() == 27 && play && !starting) {
											
					if(pause) resume(); else pause();
					
					if(!pause) loop();
					
				}
					

			}
			
		});
		
		login();
		
		//loop();
		
		this.updateUI();

	}
	
	
	
	private List<Maon> createMaons() {
		
		final int filas = 10;
		final int col = 6;
		final int space = 10;
		
		int[] maxBricks = {21, 21, 23}; //VERMELL - BLAU - VERD
		//int[] maxBricks = {0, 65, 0}; //VERMELL - BLAU - VERD
		int[] numBricks = {0, 0, 0};
		
		List<Maon> maons = new ArrayList<>();
		
		//maons.add(new MaonVermell(new Rectangle(100, 250, 60, 20), racquet, this));
		
		for(int i = 0; i < col; i++)
			for(int j = 0; j < filas; j++) {
				
				int k = 0;
				
				do k = (int)(Math.random()*3); while(numBricks[k] == maxBricks[k]);
								
				int width = (this.getWidth() / filas) - space,
				height = width/2,
				x = (j * (width+space) + space),
				y = (i * (height+space) + space);
				
				Rectangle r = new Rectangle(x, y, width, height);
				
				if(k == 0) maons.add(new MaonVermell(r, racquet, this));
				else if (k == 1) maons.add(new MaonBlau(r, racquet, this));
				else if (k == 2) maons.add(new MaonVerd(r, this));
				
				if(k < maxBricks.length) numBricks[k]++;
				
			}
		
		//maons.add(new MaonVerd(new Rectangle(230, 0, 50, 50), this));
		
		return maons;
		
	}
	
	public void gameOver() {
		
		save();
		
			
		countLifes = 0;
			
		lifes.setText(countLifes + " ♥");
		
		lifes.setForeground(countLifes == 2 ? Color.YELLOW : Color.RED);
			
		play = false;
			
		die = true;
			
		efectDie();
			
		die = false;		
		
		finalScreen();
		
	}
	
	public void win() {
		play = false;
		
		win = true;
		
		efectWin();
		
		win = false;
		
		save();
		
		finalScreen();
	}
	
	private void finalScreen() {
		add(exit);
		
		finalScreen = true;
						
		repaint();
		
		//finalScreen = false;
	}
	
	public void save() {
		
		if(userName != null)		
			try {
				data.saveUser(userName, recordUser);
			} catch (IOException e) {
				e.printStackTrace();
			}
		
	}
	
	private Thread loop() {
		Thread t = new Thread(new Runnable() {			
			public void run() {
								
				Thread.currentThread().setName("game-loop-thread");
				
				start();
				
				while(play && !pause) {
									
					move();
					repaint();
					
					if(bricks.size() == 0) win();
					else {
						Iterator<Maon> it = bricks.iterator();
						
						while(it.hasNext()) {
							Maon m = it.next();
							if(m.isDie()) it.remove();
						}
						
						if(die) {
									
							efectDie();
							
							die = false;
							
							start();
							
						}
					}
						
					try {Thread.sleep(refresh, refreshNano);} catch (InterruptedException e) {e.printStackTrace();}
					
				}
								
				repaint();
				
			}
			
		});
		
		t.start();
		
		return t;
	}
	
	private void efectDie() {
				
		txtDie = "";
		
		String ex = "";
		
		if(countLifes == 0) ex = "DIE";
		else
			for(int i = 0; i < 3 - countLifes; i++) ex += "!";
		
		txtDie = ex;
				
		for(int i = 0; i < 6; i++) {
			
			repaint();
			
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			txtDie = txtDie.equals("") ? ex : "";
			
		}
		
		
	}
	
	private void efectWin() {
				
		txtWin = "";
		
		String ex = "WIN";
		
		txtWin = ex;
				
		for(int i = 0; i < 6; i++) {
			
			repaint();
			
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			txtWin = txtWin.equals("") ? ex : "";
			
		}
		
		
	}
	
	
	private void start() {
				
		starting = true;
		
		for(counterStarting = 3; counterStarting > 0 && !pause; counterStarting--) 
			try {
				repaint();
				Thread.sleep(1000);
			} catch (InterruptedException e) {e.printStackTrace();}
		
		starting = false;
		
		
	}
	
	private void move() {
		
		ball.move();
		racquet.move();
		bricks.forEach(b -> b.move());
	}
	
	private void login() {
		
		setLayout(new BorderLayout());
		
		JPanel bottomButtons = new JPanel(new GridLayout(1, 2));
		loginPanel login = new loginPanel();
		
		data = login.getJSONReader();
				
		login.addPlayListener(e -> {
			
			remove(bottomButtons);
			remove(login);
			setLayout(null);
			updateUI();
			play = true;
			resume();
			frame.requestFocus();
			
			userName = login.getUserSelected();
			recordUser = login.getScoreUser();
			
			user.setText("Jugador: " + userName);
			user.setToolTipText(userName);
			recordScore.setText("Record: " + recordUser + " pt");
			recordScore.setToolTipText(recordUser + " pt");
			
			user.setVisible(true);
			recordScore.setVisible(true);
			
			loop();

			return null;
		});
		login.addExitListener(e -> {
			frame.dispose();
			System.exit(ABORT);
			
			return null;
		});

		
		add(login, BorderLayout.CENTER);
		add(bottomButtons, BorderLayout.SOUTH);
		
	}
	
	public void paint(Graphics g) {
		super.paint(g);
		
		Graphics2D g2 = (Graphics2D) g;
		
		if(starting) {
			
			g2.setFont(new Font("Verdana", Font.BOLD, 60));
			g2.setColor(Color.LIGHT_GRAY);
			g2.drawString(String.valueOf(counterStarting), (int)frame.getSize().getWidth()/2 - 30, (int)frame.getSize().getHeight()/2);
			
		}else if(pause) {
			g2.setFont(new Font("Verdana", Font.BOLD, 60));
			g2.setColor(Color.LIGHT_GRAY);
			g2.drawString("Pause", (int)frame.getSize().getWidth()/2 - 100, (int)frame.getSize().getHeight()/2);
			this.updateUI();
		}else if(die) {
			g2.setFont(new Font("Verdana", Font.BOLD, 80));
			g2.setColor(new Color(196, 11, 11, 150));
			g2.drawString(txtDie, (int)frame.getSize().getWidth()/2 - (txtDie.length()*20), (int)frame.getSize().getHeight()/2);
			this.updateUI();
		}else if(win) {
			g2.setFont(new Font("Verdana", Font.BOLD, 80));
			g2.setColor(new Color(65, 229, 8));
			g2.drawString(txtWin, (int)frame.getSize().getWidth()/2 - (txtWin.length()*30), (int)frame.getSize().getHeight()/2);
			this.updateUI();
		}
				
		if(finalScreen) {
						
			if(!efectRecord) {
				g2.setFont(new Font("Verdana", Font.BOLD, 50));
				g2.setColor(new Color(65, 229, 8));
				g2.drawString("¡New Record!", (int)frame.getSize().getWidth()/2 - 200, (int) 80);				
			}
			
			g2.setFont(new Font("Verdana", Font.BOLD, 20));
			g2.setColor(Color.RED);
			g2.drawString("Name: " + userName, (int)frame.getSize().getWidth()/2 - 90, (int) 180);
			
			
			g2.setFont(new Font("Verdana", Font.BOLD, 20));
			g2.setColor(Color.WHITE);
			g2.drawString("Score: " + points, (int)frame.getSize().getWidth()/2 - 90, (int) 210);
			
			g2.setFont(new Font("Verdana", Font.BOLD, 20));
			g2.setColor(Color.GREEN.darker());
			g2.drawString("Personal Record Score: " + recordUser, (int)frame.getSize().getWidth()/2 - 160, (int) 240);
			
			
			String userHighest = data.getUserHighestScore();
			
			g2.setFont(new Font("Verdana", Font.BOLD, 15));
			g2.setColor(Color.WHITE);
			g2.drawString("Global Record Score: " + data.getScoreUser(userHighest), (int)frame.getSize().getWidth()/2 - 110, (int) 280);
						
			g2.setFont(new Font("Verdana", Font.BOLD, 15));
			g2.setColor(Color.WHITE);
			g2.drawString("By: " + userHighest, (int)frame.getSize().getWidth()/2 - 90 + 40, (int) 300);

		}
		
		if(play) {
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			
			ball.paint(g2);
			racquet.paint(g2);
			
			bricks.forEach(b -> b.paint(g2));
			
		}
		
	}
	
	private JFrame createFrame() {
		JFrame marc = new JFrame("Arkanoid");

		marc.setIconImage(new ImageIcon(arkanoid.class.getResource("images/icon.png")).getImage());
		
		setBackground(Color.BLACK);
		
		int width=Toolkit.getDefaultToolkit().getScreenSize().width;
		int height=Toolkit.getDefaultToolkit().getScreenSize().height;
		
		marc.setBounds(width/2 - width/3/2, (int)(height/2 - (height/1.5 + height/1.5*0.1)/2), width/3, (int)(height/1.5 + height/1.5*0.1));
		
		JPanel bar = new JPanel(new GridLayout(1, 3));
		bar.setBackground(Color.BLACK);
		bar.setBorder(BorderFactory.createLineBorder(Color.GREEN, 2));
		bar.setPreferredSize(new Dimension((int)marc.getSize().getWidth(), (int)(marc.getSize().getHeight()*0.1)));
		
		score = new JLabel(points + " pt", JLabel.RIGHT);
		lifes = new JLabel(countLifes + " ♥");
		
		user = new JLabel("Jugador:", JLabel.CENTER);
		recordScore = new JLabel("Record:", JLabel.CENTER);
		
		Font userFont = new Font("Sans Serif", Font.PLAIN, (int)(marc.getSize().getHeight()*0.03));
		
		user.setFont(userFont);
		user.setForeground(Color.RED);
		user.setVisible(false);
		
		recordScore.setFont(userFont);
		recordScore.setForeground(Color.GREEN.darker());
		recordScore.setVisible(false);
		
		Box userData = Box.createVerticalBox();
				
		userData.add(user);
		userData.add(recordScore);
		
		Border margin = BorderFactory.createEmptyBorder(10, 10, 10, 10);
		
		score.setBorder(margin);
		lifes.setBorder(margin);
		
		Font f = new Font("Arial", Font.PLAIN, (int)(marc.getSize().getHeight()*0.05));
		
		score.setFont(f);
		lifes.setFont(f);
		
		lifes.setForeground(Color.GREEN);
		score.setForeground(Color.WHITE);
		
		bar.add(lifes);
		bar.add(userData);
		bar.add(score);
		
		marc.add(this, BorderLayout.CENTER);
		marc.add(bar, BorderLayout.NORTH);
		marc.setResizable(false);
		marc.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		marc.setVisible(true);
		
		return marc;
	}
	
	public void incrementSpeed() {
				
		refreshNano -= 30000;
		
		if(refreshNano <= 0) {
			refreshNano = 100000;
			refresh--;
			
			if(refresh == 0) refresh = 1;
		}
						
	}
	
	public void addScore(int points) {
		
		this.points += points;
		
		score.setText(this.points + " pt");
		
		if(this.points > recordUser) efectRecordScore();
				
	}
	
	private void efectRecordScore() {
		
		recordUser = points;
		
		recordScore.setText("Record: " + recordUser);
		
		if(efectRecord) new Thread(new Runnable() {

			public void run() {
				
				efectRecord = false;
				
				for(int i = 0; i < 5; i++) {
					
					score.setForeground(score.getForeground().equals(Color.WHITE) ? Color.GREEN : Color.WHITE);
					
					try {
						Thread.sleep(200);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
				}
				
				score.setForeground(Color.WHITE);
				
			}
			
		}).start();
		
	}
	
	public void die() {
		
		countLifes--;
		
		if(countLifes == 0) gameOver();
		else {
						
			Rectangle pointRacquet = racquet.getBounds();
			
			ball.setPoint(new Point(pointRacquet.x + pointRacquet.width/2  - Pilota.DIAMETER/2, pointRacquet.y - Pilota.DIAMETER));
			ball.setDirection(ball.getDirectionX(), -ball.getDirectionY());
			
			die = true;
			
			lifes.setText(countLifes + " ♥");
			
			lifes.setForeground(countLifes == 2 ? Color.YELLOW : Color.RED);
			
		}
		
	}
	
	private void pause() {
		
		pause = true;
		MaonBlau.pause();
		add(resume);
		add(exit);
		
	}
	
	private void resume() {
		
		pause = false;
		MaonBlau.resume();
		remove(resume);
		remove(exit);
		
	}
	
	public int getLifes() {
		return countLifes;
	}
	
	public void setLifes(int lifes) {
		countLifes = lifes;
	}
	
	public static void main(String[] args) {
		
		new arkanoid();
		
	}
	
}
