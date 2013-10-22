package main.gui;
import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;


public class GUI {

	private final String FRAME_DESCR = "Twitter emotion analysis";
	
	protected JFrame mainframe;
	protected JPanel mainPanel;
	protected TabPanes tabPanes;
	
	public GUI () {
		mainframe = new JFrame(FRAME_DESCR);
		mainframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainframe.setSize(300, 300);
		
		mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		//mainPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		
		tabPanes = new TabPanes (mainPanel);
		tabPanes.tabInit();
		
		mainframe.getContentPane().add(mainPanel);

		mainframe.setLocationRelativeTo(null);
		mainframe.setResizable(false);
		mainframe.setVisible(true);
	}
}
