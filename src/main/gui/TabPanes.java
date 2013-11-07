package main.gui;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import logic.CoTrainer;
import logic.HashTag;
import logic.Tweet;
import logic.naivebayes.NaiveBayesAnalyzer;
import logic.simplelinear.SimpleLinearAnalyzer;
import twitter4j.Status;
import database.Database;
import database.csv.CSVDatabase;
import database.twitter.TwitterAPIDatabase;


@SuppressWarnings("deprecation")
public class TabPanes {
	protected JFrame mainFrame;
	protected JPanel topPanel;
	
	protected JTabbedPane tabs;
	protected JPanel hashPanel;
	
	private final Font FONT = new Font("Courier", Font.PLAIN, 18);
	//Hashtag tabpane
	protected JButton searchHashButton;
	protected JRadioButton hashTweetAPIButton, hashCsvButton;
	private ButtonGroup hashButtonGroup;

	protected JTextField hashCsvFileField, hashQueryField;
	private SimpleLinearAnalyzer slaClassifier;
	private final String[] CLASSIFIER_DESCRIPTION = {" Simple Linear Classifier", " Naive Bayes"};
	protected JCheckBox[] classifier;
	//Result tabpane
	protected JPanel resultPanel;
	protected ButtonListener buttonListener;
	protected JButton clearButton;
	private NaiveBayesAnalyzer nbClassifier;
	
	private List<Tweet> slaAnalyzedTweets;
	private List<Tweet> nbAnalyzedTweets;
	private CoTrainer coTrainer;
	
	public TabPanes (JFrame mainFrame, JPanel topPanel, SimpleLinearAnalyzer slaClassifier, NaiveBayesAnalyzer nbClassifier, CoTrainer coTrainer) {
		this.mainFrame = mainFrame;
		this.topPanel = topPanel;
		this.slaClassifier = slaClassifier;
		this.nbClassifier = nbClassifier;
		this.coTrainer = coTrainer;
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
				if(hashQueryField.getText().equals("")) 
					JOptionPane.showOptionDialog(null, "Query text is empty", "ALERT", JOptionPane.WARNING_MESSAGE, JOptionPane.WARNING_MESSAGE, null, new Object[]{"Ok"}, 0);

				else {
					if(classifier[0].isSelected() || classifier[1].isSelected()) {
						String query = hashQueryField.getText();
						Database db = createDatabase();
						if(db != null) {
							List<Status> tweets = db.getTweets(query);
							HashTag hashtag = new HashTag(query);
							coTrainer.setRawTweets(tweets);
							if(classifier[0].isSelected()) {
								slaAnalyzedTweets = slaClassifier.getAnalyzedTweets(tweets);
								hashtag.addAll(slaAnalyzedTweets);
							}
							if(classifier[1].isSelected()) {
								nbAnalyzedTweets = nbClassifier.getAnalyzedTweets(tweets);
								hashtag.addAll(nbAnalyzedTweets);
							}
							
							generateResultPanel(query, getClassifierName(), hashtag);
							tabs.setSelectedIndex(tabs.getTabCount()-1);
						}
					} else
						JOptionPane.showOptionDialog(null, "Please select at least one option from CLASSIFIER SELECTION", "ALERT", JOptionPane.WARNING_MESSAGE, JOptionPane.WARNING_MESSAGE, null, new Object[]{"Ok"}, 0);
				}
			}

			private String getClassifierName() {
				if(classifier[0].isSelected() && classifier[1].isSelected()) {
					return "Both";
				} else if(classifier[0].isSelected()) {
					return "Simple Linear";
				} else {
					return "Naive Bayes";
				}
			}

			private Database createDatabase() {
				Database db = null;
				if(hashCsvButton.isSelected()) {
					try {
					db = new CSVDatabase(hashCsvFileField.getText().trim());
					} catch(IOException nsfe) {
						JOptionPane.showOptionDialog(null, "No such file found\n Type another one", "ALERT", JOptionPane.WARNING_MESSAGE, JOptionPane.WARNING_MESSAGE, null, new Object[]{"OK"}, 0);
						return null;
					}
				} else if (hashTweetAPIButton.isSelected()) {
					db = new TwitterAPIDatabase();
				}
				return db;
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
		hashQueryField.setBounds(320, 30, 300, 25);
		hashQueryField.addKeyListener(new KeyListener() {
			public void keyPressed(KeyEvent arg0) { }
			public void keyReleased(KeyEvent arg0) {
				if(hashQueryField.getText().length() >= 20)
					hashQueryField.setText(hashQueryField.getText().substring(0, 20));
			}
			public void keyTyped(KeyEvent arg0) { }
			
		});
		hashPanel.add(hashQueryField);
		
		JLabel databaseLabel = new JLabel("DATABASE SELECTION");
		databaseLabel.setFont(new Font("Courier", Font.BOLD, 17));
		databaseLabel.setBounds(20, 85, 230, 30);
		hashPanel.add(databaseLabel);
		
		hashCsvFileField = new JTextField();
		hashCsvFileField.setBounds(320, 130, 300, 25);
		hashCsvFileField.setFont(FONT);
		hashCsvFileField.addKeyListener(new KeyListener() {
			public void keyPressed(KeyEvent arg0) { }
			public void keyReleased(KeyEvent arg0) {
				if(hashCsvFileField.getText().length() >= 25)
					hashCsvFileField.setText(hashCsvFileField.getText().substring(0, 25));
			}
			public void keyTyped(KeyEvent arg0) { }
			
		});
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
			classifier[i].setBounds(20, 270+(40*i), 300, 30);
			hashPanel.add(classifier[i]);
		}
		
		searchHashButton = new JButton ("Search");
		searchHashButton.setFont(FONT);
		searchHashButton.setBounds(250, 380, 100, 40);
		hashPanel.add(searchHashButton);
		
		JLabel logoLabel = new JLabel(new ImageIcon("./images/twitter.png"));
		logoLabel.setBounds(340, 180, 209, 160);
		hashPanel.add(logoLabel);
		
		JTextArea kopimi = new JTextArea();
		kopimi.setText("KOPIMI\nWeb Information Retrieval\nand Data Mining\nAll rights to copy");
		kopimi.setFont(new Font("Courier", Font.ITALIC, 14));
		kopimi.setLineWrap(true);
		kopimi.setBackground(hashPanel.getBackground());
		kopimi.setBounds(400, 340, 260, 100);
		kopimi.setEditable(false);
		hashPanel.add(kopimi);
		
		
	}
	
	private void generateResultPanel(String query, String classifierName, HashTag hashtag) {
		final int posValue = (int)Math.round(100*hashtag.getAveragePositiveWeight());
		final int negValue = (int)Math.round(100*hashtag.getAverageNegativeWeight());
		resultPanel = new JPanel();
		resultPanel.setLayout(null);
		
		JLabel resultLabel = new JLabel("Result on");
		resultLabel.setFont(FONT);
		resultLabel.setBounds(30, 10, 100, 30);
		resultPanel.add(resultLabel);
		
		JLabel queryLabel = new JLabel(query);
		queryLabel.setFont(new Font("Courier", Font.BOLD, 18));
		queryLabel.setBounds(140, 10, 260, 30);
		resultPanel.add(queryLabel);
		
		JLabel pos = new JLabel("+"+posValue+"%");
		pos.setFont(FONT); 
		pos.setForeground(Color.green);
		pos.setBounds(500, 10, 60, 30);
		resultPanel.add(pos);		
		
		JLabel neg = new JLabel("-"+negValue+"%");
		neg.setFont(FONT); 
		neg.setForeground(Color.red);
		neg.setBounds(560, 10, 60, 30);
		resultPanel.add(neg);
		
		if(posValue == negValue && posValue == 0) {
			JLabel noResultLabel = new JLabel("No result on "+query);
			noResultLabel.setFont(FONT);
			noResultLabel.setBounds(340, 40, 220, 40);
			resultPanel.add(noResultLabel);
		} else {
			JButton histogramButton = new JButton("Histogram");
			histogramButton.setFont(new Font("Courier", Font.PLAIN, 15));
			histogramButton.setBounds(480, 50, 140, 40);
			resultPanel.add(histogramButton);
			histogramButton.addActionListener(new ActionListener () {
				public void actionPerformed(ActionEvent ae) {
					Chart chart = new Chart(mainFrame);
					chart.addBar(Color.GREEN, posValue);
					chart.addBar(Color.RED, negValue);
					chart.addChart(chart);
				}
			});
		}

		if(classifierName.equals("Both")) {
			addUncertainTweets();
		}
		
		clearButton = new JButton ("Close");
		clearButton.setFont(FONT);
		clearButton.setBounds(250, 410, 100, 40);
		resultPanel.add(clearButton);
		clearButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				hashQueryField.setText("");
				hashCsvFileField.setText("");
				for(JCheckBox box: classifier)
					box.setSelected(false);
				tabs.setEnabledAt(0, true);
				tabs.remove(tabs.getSelectedIndex());
			}
		});
		tabs.addTab(classifierName + ": "+query, resultPanel);
	}

	private void addUncertainTweets() {
		final String[] uncertainTweets = coTrainer.getUncertainTweets();
		final int uncertainCount = uncertainTweets.length;
		if(uncertainCount == 0) {
			JLabel noUncertains = new JLabel("No uncertain tweets to rate");
			noUncertains.setFont(new Font("Courier", Font.BOLD, 17));
			noUncertains.setBounds(30, 90, 400, 30);
			resultPanel.add(noUncertains);
		} else {
			JLabel uncertainLabel = new JLabel("Rate these uncertain tweets");
			uncertainLabel.setFont(new Font("Courier", Font.BOLD, 17));
			uncertainLabel.setBounds(30, 90, 400, 30);
			resultPanel.add(uncertainLabel);
			
			JTextArea[] uncertainArea = new JTextArea[uncertainTweets.length];
			JButton[][] uncertainButton = new JButton[uncertainTweets.length][2];
			final String PATH = "./images/";
			JSeparator[] line = new JSeparator[uncertainCount];
			
			buttonListener = new ButtonListener(uncertainTweets);
			
			for(int i = 0; i < uncertainTweets.length; i++) {
				for(int j = 0; j < 2; j++) {
					uncertainButton[i][j] = new JButton(new ImageIcon(j==0? PATH+"green.png": PATH+"red.png"));
					uncertainButton[i][j].setName(i+""+j);
					uncertainButton[i][j].setBorder(BorderFactory.createLineBorder(Color.black));
					uncertainButton[i][j].setBounds(555+(40*j), 145+(50*i), 30, 30);
					resultPanel.add(uncertainButton[i][j]);
					uncertainButton[i][j].addActionListener(buttonListener);
				}
				uncertainArea[i] = new JTextArea();
				uncertainArea[i].setFont(new Font("Courier", Font.PLAIN, 14));
				uncertainArea[i].setBackground(resultPanel.getBackground());
				uncertainArea[i].setText(uncertainTweets[i]);
				uncertainArea[i].setLineWrap(true);
				uncertainArea[i].setBounds(10, 140+(48*i), 520, 40);
				uncertainArea[i].setEditable(false);
				resultPanel.add(uncertainArea[i]);
				
				line[i] = new JSeparator(JSeparator.HORIZONTAL);
				line[i].setForeground(Color.black);
				line[i].setBounds(5, 185+(50*i), 625, 5);
				resultPanel.add(line[i]);
			}
			resultPanel.remove(line[line.length-1]);
		}
	}
}
