package main.gui;

import java.awt.BorderLayout;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

import logic.CoTrainer;
import logic.SpellChecker;
import logic.naivebayes.NaiveBayesAnalyzer;
import logic.simplelinear.SimpleLinearAnalyzer;

import org.slf4j.impl.SimpleLogger;

import database.sentimental.BoostWords;
import database.sentimental.Emoticons;
import database.sentimental.SentiWordNet;

public class GUI {

	private static final String FRAME_DESCR = "Twitter emotion analysis";

	private static JFrame mainframe;
	private static JPanel mainPanel;

	public static void main(String[] args) {
		System.setProperty(SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "error");
		System.setProperty(SimpleLogger.LOG_FILE_KEY, "System.out");

		mainframe = new JFrame(FRAME_DESCR);
		mainframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainframe.setSize(650, 520);
		mainframe.setIconImage(new ImageIcon("./images/twitter.png").getImage());
		mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());

		SentiWordNet sentiWordNet = SentiWordNet.getInstance();
		Emoticons emoticons = Emoticons.getInstance();
		BoostWords boostWords = BoostWords.getInstance();
	 	SpellChecker spellChecker = SpellChecker.getInstance();


		SimpleLinearAnalyzer slaClassifier = new SimpleLinearAnalyzer(sentiWordNet, emoticons, boostWords, spellChecker);
		NaiveBayesAnalyzer nbClassifier = new NaiveBayesAnalyzer();
		CoTrainer coTrainer = new CoTrainer(slaClassifier,nbClassifier);

		new TabPanes(mainframe, mainPanel, slaClassifier, nbClassifier, coTrainer);
		
		mainframe.getContentPane().add(mainPanel);
		mainframe.setResizable(false);
		mainframe.setLocationRelativeTo(null);
		mainframe.setVisible(true);
	}
}
