package main.gui;
import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;


public class GUI {

	private static final String FRAME_DESCR = "Twitter emotion analysis";
	
	private static JFrame mainframe;
	private static JPanel mainPanel;
	private static TabPanes tabPanes;
	
	public static void main (String[] args) {
		mainframe = new JFrame(FRAME_DESCR);
		mainframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainframe.setSize(300, 300);
		
		mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		//mainPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		
		tabPanes = new TabPanes (mainPanel);
		
		mainframe.getContentPane().add(mainPanel);

		mainframe.setLocationRelativeTo(null);
		mainframe.setResizable(false);
		mainframe.setVisible(true);
	}
}
