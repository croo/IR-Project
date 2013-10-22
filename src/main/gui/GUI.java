package main.gui;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

import logic.Tweet;
import logic.TweetAnalyzer;

import org.slf4j.impl.SimpleLogger;

import twitter4j.Status;
import database.Database;
import database.csv.CSVDatabase;
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
		mainframe.setSize(300, 300);

		mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());

		SentiWordNet sentiWordNet = SentiWordNet.getInstance();
		Emoticons emoticons = Emoticons.getInstance();
		BoostWords boostWords = BoostWords.getInstance();

		TweetAnalyzer analyzer = new TweetAnalyzer(sentiWordNet, emoticons, boostWords);

		new TabPanes(mainPanel,analyzer);

		mainframe.getContentPane().add(mainPanel);

		mainframe.setLocationRelativeTo(null);
		mainframe.setResizable(false);
		mainframe.setVisible(true);
	}
}
