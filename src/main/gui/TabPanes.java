package main.gui;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;


public class TabPanes {
	protected GUI gui;
	protected JPanel topPanel;
	
	protected JTabbedPane tabs;
	protected JPanel hashPanel;
	private String[] hashComboExample =  {"Hash1", "Sports", "Cake"};
	
	protected JPanel userPanel;
	
	protected JButton searchHashButton;
	protected JButton searchUserButton;
	
	protected JRadioButton hashTweetAPIButton, userTweetAPIButton, hashCsvButton, userCsvButton;
	protected ButtonGroup hashGroup, userGroup;
	protected JTextField hashCsvFileField, userCsvFileField;
	
	public TabPanes (JPanel topPanel) {
		this.topPanel = topPanel;
		tabs = new JTabbedPane();
		tabInit();
	}
	
	private void tabInit () {
		generateHashPanel();
		tabs.addTab("Analysis on hash", hashPanel);
		generateUserPanel();
		tabs.addTab("Analysis on user", userPanel);
		topPanel.add(tabs);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void generateHashPanel () {
		hashPanel = new JPanel();
		hashPanel.setLayout(null);
		
		JLabel hashLabel = new JLabel("#Hashtag");
		hashLabel.setBounds(40, 20, 70, 20);
		hashPanel.add(hashLabel);
		
		JComboBox hashComboBox = new JComboBox(hashComboExample);
		hashComboBox.setBounds(140, 20, 140, 20);
		hashPanel.add(hashComboBox);
		
		searchHashButton = new JButton ("Search");
		searchHashButton.setBounds(110, 70, 100, 30);
		hashPanel.add(searchHashButton);
		
		hashCsvFileField = new JTextField();
		hashCsvFileField.setBounds(160, 172, 120, 20);
		hashPanel.add(hashCsvFileField); 
		
		ButtonGroup group = new ButtonGroup();
		
		hashTweetAPIButton = new JRadioButton("Use Twitter API Database");
		hashTweetAPIButton.setBounds(0, 120, 180, 40);
		hashTweetAPIButton.addActionListener(new ActionListener() {
			public void actionPerformed (ActionEvent ae) {
				hashCsvFileField.setText("");
				hashCsvFileField.disable();
				hashCsvFileField.setBackground(Color.LIGHT_GRAY);
			}
		});
		hashTweetAPIButton.doClick();
		hashPanel.add(hashTweetAPIButton); 
		
		hashCsvButton = new JRadioButton("Use cvs file Database");
		hashCsvButton.setBounds(0, 160, 160, 40);
		hashCsvButton.addActionListener(new ActionListener() {
			public void actionPerformed (ActionEvent ae) {
				hashCsvFileField.setBackground(Color.white);
				hashCsvFileField.enable();
			}
		});
		hashPanel.add(hashCsvButton);
		
		group.add(hashTweetAPIButton); group.add(hashCsvButton);
	}
	
	private void generateUserPanel () {
		userPanel = new JPanel();
		userPanel.setLayout(null);
		
		JLabel userLabel = new JLabel("Username");
		userLabel.setBounds(40, 20, 70, 20);
		//userLabel.setBorder(BorderFactory.createLineBorder(Color.black));
		userPanel.add(userLabel);
		
		JTextField userTfield = new JTextField();
		userTfield.setBounds(130, 20, 120, 20);
		userPanel.add(userTfield);
		
		searchUserButton = new JButton ("Search");
		searchUserButton.setBounds(110, 70, 75, 30);
		userPanel.add(searchUserButton);
		
		userCsvFileField = new JTextField();
		userCsvFileField.setBounds(160, 172, 120, 20);
		userPanel.add(userCsvFileField); 
		
		ButtonGroup group = new ButtonGroup();
		
		userTweetAPIButton = new JRadioButton("Use Twitter API Database");
		userTweetAPIButton.setBounds(0, 120, 180, 40);
		userTweetAPIButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				userCsvFileField.setText("");
				userCsvFileField.disable();
				userCsvFileField.setBackground(Color.LIGHT_GRAY);
			}
		});
		userTweetAPIButton.doClick();
		userPanel.add(userTweetAPIButton); 
		
		userCsvButton = new JRadioButton("Use cvs file Database");
		userCsvButton.setBounds(0, 160, 160, 40);
		userCsvButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				userCsvFileField.setBackground(Color.white);
				userCsvFileField.enable();
			}
		});
		userPanel.add(userCsvButton); 
		
		group.add(userTweetAPIButton); group.add(userCsvButton);
		

		
	}
	
}
