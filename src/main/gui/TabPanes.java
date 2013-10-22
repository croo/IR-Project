package main.gui;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import logic.HashTag;
import logic.Tweet;
import logic.TweetAnalyzer;
import twitter4j.Status;
import database.Database;
import database.csv.CSVDatabase;
import database.twitter.TwitterAPIDatabase;


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
	private JTextField hashQueryField;
	private ButtonGroup hashButtonGroup;
	private TweetAnalyzer analyzer;
	
	public TabPanes (JPanel topPanel, TweetAnalyzer analyzer) {
		this.topPanel = topPanel;
		this.analyzer = analyzer;
		tabs = new JTabbedPane();
		tabInit();
	}
	
	private void tabInit () {
		generateHashPanel();
		initHashSearchButton();
		tabs.addTab("Analysis on hash", hashPanel);
		generateUserPanel();
		tabs.addTab("Analysis on user", userPanel);
		topPanel.add(tabs);
	}
	
	private void initHashSearchButton() {
		searchHashButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String query = hashQueryField.getText();
				Database db = null;
				if(hashCsvButton.isSelected()) {
					db = new CSVDatabase(hashCsvFileField.getText().trim());
				} else if (hashTweetAPIButton.isSelected()) {
					db = new TwitterAPIDatabase();
				}
				
				List<Status> tweets = db.getTweets(query);
				HashTag hashtag = new HashTag(query);
				List<Tweet> analyzedTweets = analyzer.getAnalyzedTweets(tweets);
				hashtag.addAll(analyzedTweets);
				JOptionPane.showMessageDialog(null, "The query '" + query + "' have the following semantical value: \n +" + hashtag.getBayesianPositiveWeight() +"; -" + hashtag.getBayesianNegativeWeight());
			}
		});
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void generateHashPanel () {
		hashPanel = new JPanel();
		hashPanel.setLayout(null);
		
		JLabel hashLabel = new JLabel("#Hashtag");
		hashLabel.setBounds(40, 20, 70, 20);
		hashPanel.add(hashLabel);
		
		hashQueryField = new JTextField();
		hashQueryField.setBounds(140, 20, 140, 20);
		hashPanel.add(hashQueryField);
	
		searchHashButton = new JButton ("Search");
		searchHashButton.setBounds(110, 70, 100, 30);
		hashPanel.add(searchHashButton);
		
		hashCsvFileField = new JTextField();
		hashCsvFileField.setBounds(160, 172, 120, 20);
		hashPanel.add(hashCsvFileField); 
		
		hashButtonGroup = new ButtonGroup();
		
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
		
		hashButtonGroup.add(hashTweetAPIButton); hashButtonGroup.add(hashCsvButton);
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
