package arkanoid;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class scoresPanel extends JPanel{
		
	private static final long serialVersionUID = 1L;
	private String highestUser, lowestUser;
	
	public scoresPanel(JSONReader data) {
		
		super(new BorderLayout());
		
		setBackground(Color.BLACK);
		
		JLabel title = new JLabel("Ranking Scores", JLabel.CENTER);
		
		title.setFont(new Font("Verdana", Font.BOLD, 24));
		title.setForeground(Color.GREEN);
		
		add(title, BorderLayout.NORTH);
		
		
		String[] names = data.getUsersName();
		
		Object[][] tabla = new Object[names.length][2];
		
		int maxScore = 0, minScore = 0;
		
		for(int i = 0; i < names.length; i++) {
			tabla[i][0] = names[i];
			tabla[i][1] = Integer.parseInt(String.valueOf(data.getScoreUser(names[i])));
			
			int sc = Integer.parseInt(tabla[i][1].toString());
			
			if(i == 0) {
								
				maxScore = sc;
				minScore = sc;
				highestUser = tabla[i][0].toString();
				lowestUser = tabla[i][0].toString();
				
			}else if(sc > maxScore) {
				
				maxScore = sc;
				highestUser = tabla[i][0].toString();
				
			}else if(sc < minScore) {
				
				minScore = sc;
				lowestUser = tabla[i][0].toString();
				
			}
			
		}
		
		JTable puntuacions = new table(tabla, new String[] {"Jugador", "Puntuacio"});
	
		puntuacions.setBackground(getBackground());
		
		puntuacions.setRowHeight(25);
		
		cellRenderer render = new cellRenderer();
		
		puntuacions.getColumnModel().getColumn(0).setPreferredWidth(100);
		puntuacions.getColumnModel().getColumn(1).setPreferredWidth(120);
		
		puntuacions.getColumnModel().getColumn(0).setCellRenderer(render);
		puntuacions.getColumnModel().getColumn(1).setCellRenderer(render);

								
		puntuacions.setAutoCreateRowSorter(true);
		
		puntuacions.getTableHeader().setForeground(Color.BLUE);
		puntuacions.getTableHeader().setFont(new Font("Times New Roman", Font.BOLD, 22));
		puntuacions.getTableHeader().setPreferredSize(new Dimension(0, 25));
		puntuacions.getTableHeader().setBackground(Color.ORANGE);
		
		puntuacions.setFont(new Font("Sans Serif", Font.PLAIN, 20));
		
		//puntuacions.setDefaultRenderer(Object.class, new cellRenderer());
				
		//puntuacions.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		
		JScrollPane scroll=new JScrollPane(puntuacions);
		
		scroll.getVerticalScrollBar().setUnitIncrement(20);
		scroll.getHorizontalScrollBar().setUnitIncrement(15);
		
		scroll.setOpaque(false);
		scroll.getViewport().setOpaque(false);
		scroll.setBorder(null);
		
		add(scroll, BorderLayout.CENTER);
		
	}
	
	private class table extends JTable{
		
		private static final long serialVersionUID = 1L;

		public table(Object[][] _tabla, String[] _titColumnas) {
			
			super(new DefaultTableModel() {

				private static final long serialVersionUID = 1L;

				{
					setDataVector(_tabla, _titColumnas);

				}
				
				public Class<?> getColumnClass(int c){
					
					if(c == 1) return Integer.class;
					
					return Object.class;
					
				}
				
			});
				
			
		}
		
		public boolean isCellEditable(int tow, int column) {
			return false;
		}
		

		
	}
	
	private class cellRenderer extends DefaultTableCellRenderer {

		private static final long serialVersionUID = 1L;

		{this.setHorizontalAlignment(SwingConstants.CENTER);}
		
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
	        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
	        component.setBackground(Color.BLACK);
	        component.setForeground(Color.WHITE);
	        
	        if(!highestUser.equals(lowestUser) && String.valueOf(table.getValueAt(row, 0)).equals(highestUser))
	        	setForeground(Color.GREEN);
	        else if(!highestUser.equals(lowestUser) && String.valueOf(table.getValueAt(row, 0)).equals(lowestUser))
	        	setForeground(Color.RED);
	        else if(column == 1) setForeground(Color.YELLOW);
	        
	        component.setSize(new Dimension(200, 200));
	        component.setPreferredSize(new Dimension(200, 200));
	        return component;
	    }
	}
	
}
