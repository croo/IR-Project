package main;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.List;

import logic.SpellChecker;
import logic.Tweet;
import logic.simplelinear.SimpleLinearAnalyzer;
import twitter4j.Status;
import database.Database;
import database.csv.CSVDatabase;
import database.sentimental.BoostWords;
import database.sentimental.Emoticons;
import database.sentimental.SentiWordNet;

public class CommandLineTool {

	
	 public static void main(String[] args) {
		 
		 	System.setProperty(org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "error");
		 	System.setProperty(org.slf4j.impl.SimpleLogger.LOG_FILE_KEY, "System.out");
		 	System.setProperty(org.slf4j.impl.SimpleLogger.SHOW_SHORT_LOG_NAME_KEY, "true");
		 	
		 	String hashTagQuery = "#happy";
		 	String csvDatabaseFile = "happy.txt";
		 	
		 	if(args.length != 0) {
		 		hashTagQuery = args[0];
		 		csvDatabaseFile = args[1];
		 	}

		 	Database database = new CSVDatabase("tabbed_training_data.csv");//new CSVDatabase(csvDatabaseFile);
		 	List<Status> tweets = database.getTweets(hashTagQuery);
		 	
		 	SentiWordNet sentiWordNet = SentiWordNet.getInstance();
		 	Emoticons emoticons = Emoticons.getInstance();
		 	BoostWords boostWords = BoostWords.getInstance();
		 	SpellChecker spellChecker = SpellChecker.getInstance();
		 	
		 	SimpleLinearAnalyzer analyzer = new SimpleLinearAnalyzer(sentiWordNet,emoticons,boostWords,spellChecker);
		 	
		 	BufferedWriter writer = null;

			try {
				writer = new BufferedWriter(new OutputStreamWriter(
				        new FileOutputStream("sample_data.txt", true), "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				System.err.println("Output file not found.(what the hell, i just created it. This is not possible.)");
				e.printStackTrace();
			}
			SimpleDateFormat formatter = new SimpleDateFormat("\"yyyy-dd-MM H:m:s:S\"");
		 	for (Status tweet : tweets) {
		 		Tweet t = analyzer.getAnalyzedTweet(tweet);
		 		try {
					writer.append(formatter.format(t.getRawTweet().getCreatedAt()) + 
							"\t" + t.getRawTweet().getUser().getName() + 
							"\t" + "\""+t.getBayesianPositiveWeight() +"\"" + 
							"\t" +"\""+ t.getBayesianNegativeWeight() +"\""+
							"\t" +"\""+ ((t.getBayesianPositiveWeight() > t.getBayesianNegativeWeight()) ? "1":"0")+"\"");
					writer.newLine();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		 	}		 	
		 	
		 	
	    }
}
