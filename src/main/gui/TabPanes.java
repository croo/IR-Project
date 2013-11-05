package main.gui;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
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
	
	protected JTabbedPane tabs;
	protected JPanel hashPanel;
	
	private final Font FONT = new Font("Courier", Font.PLAIN, 18);
	//Hashtag tabpane
	protected JButton searchHashButton;
	protected JRadioButton hashTweetAPIButton, hashCsvButton;
	private ButtonGroup hashButtonGroup;
	protected JTextField hashCsvFileField, hashQueryField;
	private TweetAnalyzer analyzer;
	private final String[] CLASSIFIER_DESCRIPTION = {" Simple Linear Classifier", " Naïve Bayes"};
	protected JCheckBox[] classifier;
	//Result tabpane
	protected JPanel resultPanel;
	protected JButton clearButton;
	
	
	public TabPanes (JPanel topPanel, TweetAnalyzer analyzer) {
		this.topPanel = topPanel;
		this.analyzer = analyzer;
		classifier = new JCheckBox[CLASSIFIER_DESCRIPTION.length];
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
					if(classifier[0].isSelected() || classifier[1].isSelected()) {
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
						generateResultPanel(query, hashtag.getBayesianPositiveWeight(), hashtag.getBayesianNegativeWeight());
						tabs.setSelectedIndex(1);
						tabs.setEnabledAt(0, false);
					} else
						JOptionPane.showMessageDialog(null, "Select at least one CLASSIFIER SELECTION");
				}
			}
		});
		
		
	}
	
	private void generateHashPanel () {
		hashPanel = new JPanel();
		hashPanel.setLayout(null);
		
		JLabel hashLabel = new JLabel("#Hashtag or term");
		hashLabel.setFont(FONT);
		hashLabel.setBounds(25, 25, 180, 30);
		hashPanel.add(hashLabel);
		
		hashQueryField = new JTextField();
		hashQueryField.setFont(FONT);
		hashQueryField.setBounds(330, 30, 180, 25);
		hashPanel.add(hashQueryField);
		
		JLabel databaseLabel = new JLabel("DATABASE SELECTION");
		databaseLabel.setFont(new Font("Courier", Font.BOLD, 17));
		databaseLabel.setBounds(20, 85, 230, 30);
		hashPanel.add(databaseLabel);
		
		hashCsvFileField = new JTextField();
		hashCsvFileField.setBounds(330, 130, 180, 25);
		hashCsvFileField.setFont(FONT);
		hashPanel.add(hashCsvFileField); 
		
		hashButtonGroup = new ButtonGroup();
		
		hashCsvButton = new JRadioButton("Use CSV file Database");
		hashCsvButton.setBounds(20, 120, 280, 40);
		hashCsvButton.setFont(FONT);
		hashCsvButton.addActionListener(new ActionListener() {
			public void actionPerformed (ActionEvent ae) {
				hashCsvFileField.setBackground(Color.white);
				hashCsvFileField.enable();
			}
		});
		
		hashTweetAPIButton = new JRadioButton("Use Twitter API Database");
		hashTweetAPIButton.setBounds(20, 155, 300, 40);
		hashTweetAPIButton.setFont(FONT);
		hashTweetAPIButton.addActionListener(new ActionListener() {
			public void actionPerformed (ActionEvent ae) {
				hashCsvFileField.setText("");
				hashCsvFileField.disable();
				hashCsvFileField.setBackground(Color.LIGHT_GRAY);
			}
		});
		hashTweetAPIButton.doClick();
		hashPanel.add(hashTweetAPIButton); 
		
		hashPanel.add(hashCsvButton);
		hashButtonGroup.add(hashTweetAPIButton); hashButtonGroup.add(hashCsvButton);
		
		JLabel classifierLabel = new JLabel("CLASSIFIER SELECTION");
		classifierLabel.setFont(new Font("Courier", Font.BOLD, 17));
		classifierLabel.setBounds(20, 235, 400, 30);
		hashPanel.add(classifierLabel);
		
		for(int i = 0; i < classifier.length; i++) {
			classifier[i] = new JCheckBox(CLASSIFIER_DESCRIPTION[i]);
			classifier[i].setFont(FONT);
			classifier[i].setBounds(20, 270+(40*i), 400, 30);
			hashPanel.add(classifier[i]);
		}
		
		searchHashButton = new JButton ("Search");
		searchHashButton.setFont(FONT);
		searchHashButton.setBounds(230, 380, 100, 40);
		hashPanel.add(searchHashButton);
		
	}
	
	private void generateResultPanel(String query, double positive, double negative) {
		resultPanel = new JPanel();
		
		JLabel resultLabel = new JLabel("Result for '"+query+"' is +"+positive+" -"+negative);
		resultLabel.setFont(new Font("Courier", Font.PLAIN, 18));
		resultPanel.add(resultLabel);
		
		clearButton = new JButton ("Clear");
		clearButton.setFont(FONT);
		clearButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				hashQueryField.setText("");
				hashCsvFileField.setText("");
				for(JCheckBox box: classifier)
					box.setSelected(false);
				tabs.setEnabledAt(0, true);
				tabs.remove(1);
			}
		});
		resultPanel.add(clearButton);
		tabs.addTab("Result for "+query.toUpperCase(), resultPanel);
	}
}
