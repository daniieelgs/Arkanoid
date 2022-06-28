package arkanoid;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.font.TextAttribute;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.function.Function;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

import org.json.simple.parser.ParseException;

public class loginPanel extends JPanel{
	
	private static final long serialVersionUID = 1L;
	private boolean modeInputUser;
	private File jsonFile = null;
	private JSONReader data = null;
	private JComboBox<String> users;
	private JTextField inputUser;
	private int recordScore;
	
	private Function<ActionEvent, Object> playEvent;
	private Function<ActionEvent, Object> exitEvent;
	
	private Box vertical;
	private JLabel newUserLabel, score, allScores;
	
	public loginPanel() {
		
		setLayout(new BorderLayout());
		
		setOpaque(true);
		setBackground(new Color(102, 113, 172));
				
		try {			
			jsonFile = createJSONFile();
			data = new JSONReader(jsonFile);
		} catch (IOException | ParseException e1) {
			e1.printStackTrace();
			JOptionPane.showMessageDialog(null, e1.getMessage(), "Error on JSON File", JOptionPane.ERROR_MESSAGE);
		}
		
		JPanel userScore = new JPanel(new GridLayout(2, 1));
		
		userScore.setOpaque(isOpaque());
		userScore.setBackground(getBackground());
		
		JPanel bottomButtons = new JPanel(new GridLayout(1, 2));
		
		Font buttonsFont = new Font("Verdana", Font.BOLD, 15);
		
		JButton exit = new JButton("Salir");
		
		exit.setBackground(new Color(226, 10, 10));
		exit.setForeground(Color.WHITE);
		exit.setFont(buttonsFont);
		exit.setFocusPainted(false);
		exit.setBorderPainted(false); 
		
		AbstractAction abstractButton = new AbstractAction() {

			private static final long serialVersionUID = 1L;
						
			public void actionPerformed(ActionEvent e) {
				playEvent.apply(e);
			}
			
		};
		
		abstractButton.putValue(Action.NAME, "Jugar");	
		getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ENTER"), "play");		
		getActionMap().put("play", abstractButton);
		
		JButton jugar = new JButton(abstractButton);
		
		jugar.setBackground(new Color(04, 156, 31));
		jugar.setForeground(Color.WHITE);
		jugar.setFont(buttonsFont);
		jugar.setFocusPainted(false);
		jugar.setBorderPainted(false); 
		
		MouseListener buttonsMouseEvent = new MouseAdapter() {
			
			public void mouseEntered(MouseEvent e) {
				
				JButton button = (JButton) e.getSource();
												
				button.setBackground(button.getBackground().darker());
								
			}
			
			public void mouseExited(MouseEvent e) {
				
				JButton button = (JButton) e.getSource();
				
				button.setBackground(button.getBackground().brighter());
				
			}
			
		};
		
		exit.addMouseListener(buttonsMouseEvent);
		jugar.addMouseListener(buttonsMouseEvent);
		
		bottomButtons.add(exit);
		bottomButtons.add(jugar);
		
		
		modeInputUser = false;
		
		JPanel userPanel = new JPanel();
		
		userPanel.setOpaque(isOpaque());
		userPanel.setBackground(getBackground());
		
		vertical = Box.createVerticalBox();
		
		users = new JComboBox<String>(data.getUsersName());
		
		inputUser = new JTextField();
		
		vertical.add(users);
		
		newUserLabel = new JLabel("Crear nuevo usuario", JLabel.CENTER);
		
		newUserLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
		
		Font labelClick = new Font("Verdana", Font.PLAIN, 12);
		
		newUserLabel.setFont(labelClick);
		newUserLabel.setForeground(Color.BLACK);
		
		vertical.add(newUserLabel);
				
		userPanel.add(vertical);
		
		userScore.add(userPanel);
		
		JPanel userScorePanel = new JPanel();
		
		userScorePanel.setOpaque(isOpaque());
		userScorePanel.setBackground(getBackground());
		
		Box verticalScore = Box.createVerticalBox();
		
		recordScore = (int) data.getScoreUser(String.valueOf(users.getSelectedItem()));
		
		score = new JLabel("Score: " + recordScore, JLabel.CENTER);
		score.setFont(new Font("Sans Serif", Font.BOLD, 40));
		score.setForeground(Color.GREEN);
		
		verticalScore.add(score);
		
		allScores = new JLabel("Totes les puntuacions", JLabel.CENTER);
		allScores.setFont(labelClick);
		allScores.setForeground(Color.BLACK);
		allScores.setCursor(new Cursor(Cursor.HAND_CURSOR));
		verticalScore.add(allScores);
		
		userScorePanel.add(verticalScore);
		
		userScore.add(userScorePanel);
		
		add(userScore, BorderLayout.CENTER);
		add(bottomButtons, BorderLayout.SOUTH);
		
		if(users.getSelectedItem() == null) {
			
			changeMode();
			
			newUserLabel.setEnabled(false);
			
			jugar.setEnabled(false);
			
		}
				
		/*jugar.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				playEvent.apply(e);
			}
		});*/
		
		exit.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				exitEvent.apply(e);
			}
			
		});
		
		users.addItemListener(new ItemListener() {
			
			public void itemStateChanged(ItemEvent e) {
				recordScore = (int) data.getScoreUser(String.valueOf(users.getSelectedItem()));

				score.setText("Score: " + recordScore);
				
				jugar.setEnabled(users.getSelectedItem() != null);
			}
		});
		
		inputUser.addCaretListener(new CaretListener() {
			
			public void caretUpdate(CaretEvent e) {
				jugar.setEnabled(!inputUser.getText().trim().equals(""));
				if(data.userExist(inputUser.getText()))
					recordScore = (int) data.getScoreUser(inputUser.getText());
				else
					recordScore = 0;
				
				score.setText("Score: " + recordScore);
			}
		});
		
				
		newUserLabel.addMouseListener(new MouseEventLabelClick() {
			
			public void mouseClicked(MouseEvent e) {
				
				if(newUserLabel.isEnabled()) changeMode();
				
			}
			
		});
		
		allScores.addMouseListener(new MouseEventLabelClick() {
			
			public void mouseClicked(MouseEvent e) {
				
				JDialog dialogScores = new JDialog();
				
				dialogScores.setTitle("Ranking Scores");
				
				dialogScores.setIconImage(new ImageIcon(loginPanel.class.getResource("images/icon.png")).getImage());
				
				int width=Toolkit.getDefaultToolkit().getScreenSize().width;
				int height=Toolkit.getDefaultToolkit().getScreenSize().height;
				
				dialogScores.setBounds(width/2 - 200, height/2 - 250, 239, 500);
				
				dialogScores.setResizable(false);
				
				dialogScores.add(new scoresPanel(data));
				
				dialogScores.setModal(true);
				dialogScores.setVisible(true);
				
			}
			
		});
		
	}
	
	private void changeMode() {
		modeInputUser = !modeInputUser;
		
		if(modeInputUser) {
			vertical.remove(users);
			vertical.add(inputUser);
			
			newUserLabel.setText("Seleccionar usuario");
			recordScore = 0;
		}else {
			vertical.remove(inputUser);
			vertical.add(users);
			
			newUserLabel.setText("Crear nuevo usuario");
			recordScore = (int) data.getScoreUser(String.valueOf(users.getSelectedItem()));
		}
						
		vertical.remove(newUserLabel);
		vertical.add(newUserLabel);

		score.setText("Score: " + recordScore);			
		
		updateUI();
	}
	
	public JSONReader getJSONReader() {
		return data;
	}
	
	private File createJSONFile() throws IOException {
		
		File source = new File("userData");
		
		if(!source.exists()) source.mkdir();
		
		File jsonFile = new File(source, "data.json");
		
		if(!jsonFile.exists()) {
			
			FileWriter newJSONFile = new FileWriter(jsonFile);
			
			newJSONFile.write("{\"userData\":[], \"userList\":[]}");
			
			newJSONFile.close();
			
		}
		
		return jsonFile;
		
	}
	
	public String getUserSelected() {
		
		return modeInputUser ? inputUser.getText() : String.valueOf(users.getSelectedItem());
		
	}
	
	public int getScoreUser() {
		
		return recordScore;
		
	}
	
	public void addPlayListener(Function<ActionEvent, Object> playEvent) {
		
		this.playEvent = playEvent;
		
	}
	
	public void addExitListener(Function<ActionEvent, Object> exitEvent) {
		
		this.exitEvent = exitEvent;
		
	}
	
	private abstract class MouseEventLabelClick extends MouseAdapter{
		
		private Font font;
		
		@SuppressWarnings({ "rawtypes", "unchecked" })
		public void mouseEntered(MouseEvent e) {
			
			JLabel label = (JLabel) e.getSource();
			
			if(label.isEnabled()) {
				
				font = label.getFont();
		        Map attributes=font.getAttributes();
		        attributes.put(TextAttribute.UNDERLINE,TextAttribute.UNDERLINE_ON);
		        label.setFont(font.deriveFont(attributes));
		        
		        label.setForeground(Color.BLUE.darker());
				
			}
			
		}
		
		public void mouseExited(MouseEvent e) {
			
			JLabel label = (JLabel) e.getSource();
			
			label.setFont(font);
			
			label.setForeground(Color.BLACK);
			
		}
		
		public abstract void mouseClicked(MouseEvent e);
		
		
	}
	
}
