package main.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;

import logic.TweetAnalyzer;

import org.slf4j.impl.SimpleLogger;

import database.sentimental.BoostWords;
import database.sentimental.Emoticons;
import database.sentimental.SentiWordNet;

public class GUI {

	private static final String FRAME_DESCR = "Twitter emotion analysis";

	private static JFrame mainframe;
	private static JPanel mainPanel;
	private static JPanel botPanel;

	public static void main(String[] args) {
		System.setProperty(SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "error");
		System.setProperty(SimpleLogger.LOG_FILE_KEY, "System.out");

		mainframe = new JFrame(FRAME_DESCR);
		mainframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainframe.setSize(800, 600);
		mainframe.setLayout(new GridLayout(2,1));
		mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		botPanel = new JPanel();
		botPanel.setLayout(new BorderLayout());

		SentiWordNet sentiWordNet = SentiWordNet.getInstance();
		Emoticons emoticons = Emoticons.getInstance();
		BoostWords boostWords = BoostWords.getInstance();

		TweetAnalyzer analyzer = new TweetAnalyzer(sentiWordNet, emoticons, boostWords);

		new TabPanes(mainPanel, botPanel, analyzer);
		
		mainframe.getContentPane().add(mainPanel);
		//botPanel.setBorder(BorderFactory.createLineBorder(Color.green));
		mainframe.getContentPane().add(botPanel);
		
		
		mainframe.setLocationRelativeTo(null);
		mainframe.setResizable(false);
		mainframe.setVisible(true);
	}
}
