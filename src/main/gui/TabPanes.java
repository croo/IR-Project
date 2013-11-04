package main.gui;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
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


@SuppressWarnings("deprecation")
public class TabPanes {
	protected JPanel topPanel;
	protected JPanel botPanel;
	
	protected JTabbedPane tabs;
	protected JPanel hashPanel;
	
	
	protected JButton searchHashButton;
	protected JRadioButton hashTweetAPIButton, hashCsvButton;
	protected JTextField hashCsvFileField;
	private JTextField hashQueryField;
	private TweetAnalyzer analyzer;
	
	
	protected JPanel userPanel;
	protected JButton searchUserButton;
	protected JRadioButton userTweetAPIButton, userCsvButton;
	private ButtonGroup hashButtonGroup;
	protected ButtonGroup userGroup;
	protected JTextField userCsvFileField;
	
	
	public TabPanes (JPanel topPanel, JPanel botPanel, TweetAnalyzer analyzer) {
		this.topPanel = topPanel;
		this.botPanel = botPanel;
		this.analyzer = analyzer;
		tabs = new JTabbedPane();
		tabInit();
	}
	
	private void tabInit () {
		generateHashPanel();
		initHashSearchButton();
		tabs.addTab("Analysis on hash", hashPanel);
		topPanel.add(tabs);
	}
	
	private void initHashSearchButton() {
		searchHashButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(hashQueryField.getText().equals("")) {
					JOptionPane.showMessageDialog(null, "Query empty");
				} else {
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
					//New class where Peter's code should go
					JOptionPane.showMessageDialog(null, "The query '" + query + "' have the following semantical value: \n +" + hashtag.getBayesianPositiveWeight() +"; -" + hashtag.getBayesianNegativeWeight());
				}
			}
		});
	}
	
	private void generateHashPanel () {
		hashPanel = new JPanel();
		hashPanel.setLayout(null);
		
		JLabel hashLabel = new JLabel("#Hashtag");
		hashLabel.setFont(new Font(null, Font.PLAIN, 18));
		hashLabel.setBounds(60, 40, 100, 30);
		hashPanel.add(hashLabel);
		
		hashQueryField = new JTextField();
		hashQueryField.setFont(new Font("Courier", Font.PLAIN, 18));
		hashQueryField.setBounds(180, 45, 160, 25);
		hashPanel.add(hashQueryField);
	
		searchHashButton = new JButton ("Search");
		searchHashButton.setFont(new Font(null, Font.PLAIN, 18));
		searchHashButton.setBounds(313, 160, 100, 50);
		hashPanel.add(searchHashButton);
		
		hashCsvFileField = new JTextField();
		hashCsvFileField.setBounds(600, 60, 120, 25);
		hashCsvFileField.setFont(new Font("Courier", Font.PLAIN, 18));
		hashPanel.add(hashCsvFileField); 
		
		hashButtonGroup = new ButtonGroup();
		
		hashTweetAPIButton = new JRadioButton("Use Twitter API Database");
		hashTweetAPIButton.setBounds(400, 20, 180, 40);
		hashTweetAPIButton.addActionListener(new ActionListener() {
			public void actionPerformed (ActionEvent ae) {
				hashCsvFileField.setText("");
				hashCsvFileField.disable();
				hashCsvFileField.setBackground(Color.LIGHT_GRAY);
			}
		});
		hashTweetAPIButton.doClick();
		hashPanel.add(hashTweetAPIButton); 
		
		hashCsvButton = new JRadioButton("Use CSV file Database");
		hashCsvButton.setBounds(400, 50, 160, 40);
		hashCsvButton.addActionListener(new ActionListener() {
			public void actionPerformed (ActionEvent ae) {
				hashCsvFileField.setBackground(Color.white);
				hashCsvFileField.enable();
			}
		});
		hashPanel.add(hashCsvButton);
		
		hashButtonGroup.add(hashTweetAPIButton); hashButtonGroup.add(hashCsvButton);
	}
	
/*	private void generateUserPanel () {
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
	}*/
	
}
