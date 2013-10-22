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
	
	protected JRadioButton hashTweetAPI, userTweetAPI, hashCvsFile, userCvsFile;
	protected ButtonGroup hashGroup, userGroup;
	protected JTextField hashFileField, userFileField;
	
	public TabPanes (JPanel topPanel) {
		this.topPanel = topPanel;
		tabs = new JTabbedPane();
	}
	
	public void tabInit () {
		generateHashPanel();
		tabs.addTab("Analysis on hash", hashPanel);
		generateUserPanel();
		tabs.addTab("Analysis on user", userPanel);
		topPanel.add(tabs);
	}
	
	private void generateHashPanel () {
		hashPanel = new JPanel();
		hashPanel.setLayout(null);
		
		JLabel hashLabel = new JLabel("#Hashtag");
		hashLabel.setBounds(40, 20, 70, 20);
		hashPanel.add(hashLabel);
		
		JTextField hashComboBox = new JTextField("");
		hashComboBox.setBounds(140, 20, 140, 20);
		hashPanel.add(hashComboBox);
		
		searchHashButton = new JButton ("Search");
		searchHashButton.setBounds(110, 70, 100, 30);
		hashPanel.add(searchHashButton);
		
		hashFileField = new JTextField();
		hashFileField.setBounds(160, 172, 120, 20);
		hashPanel.add(hashFileField); 
		
		ButtonGroup group = new ButtonGroup();
		
		hashTweetAPI = new JRadioButton("Use Twitter API Database");
		hashTweetAPI.setBounds(0, 120, 180, 40);
		hashTweetAPI.addActionListener(new ActionListener() {
			public void actionPerformed (ActionEvent ae) {
				hashFileField.setText("");
				hashFileField.setEnabled(false);
				hashFileField.setBackground(Color.LIGHT_GRAY);
			}
		});
		hashTweetAPI.doClick();
		hashPanel.add(hashTweetAPI); 
		
		hashCvsFile = new JRadioButton("Use cvs file Database");
		hashCvsFile.setBounds(0, 160, 160, 40);
		hashCvsFile.addActionListener(new ActionListener() {
			public void actionPerformed (ActionEvent ae) {
				hashFileField.setBackground(Color.white);
				hashFileField.setEnabled(true);
			}
		});
		hashPanel.add(hashCvsFile);
		
		group.add(hashTweetAPI); group.add(hashCvsFile);
		

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
		
		userFileField = new JTextField();
		userFileField.setBounds(160, 172, 120, 20);
		userPanel.add(userFileField); 
		
		ButtonGroup group = new ButtonGroup();
		
		userTweetAPI = new JRadioButton("Use Twitter API Database");
		userTweetAPI.setBounds(0, 120, 180, 40);
		userTweetAPI.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				userFileField.setText("");
				userFileField.disable();
				userFileField.setBackground(Color.LIGHT_GRAY);
			}
		});
		userTweetAPI.doClick();
		userPanel.add(userTweetAPI); 
		
		userCvsFile = new JRadioButton("Use cvs file Database");
		userCvsFile.setBounds(0, 160, 160, 40);
		userCvsFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				userFileField.setBackground(Color.white);
				userFileField.enable();
			}
		});
		userPanel.add(userCvsFile); 
		
		group.add(userTweetAPI); group.add(userCvsFile);
		

		
	}
	
}
