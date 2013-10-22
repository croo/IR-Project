package main;

import java.util.ArrayList;
import java.util.List;

import logic.HashTag;
import logic.Tweet;
import logic.TweetAnalyzer;
import twitter4j.Status;
import database.Database;
import database.csv.CSVDatabase;
import database.sentimental.BoostWords;
import database.sentimental.Emoticons;
import database.sentimental.SentiWordNet;

public class CommandLineTool {
	
	 public static void main(String[] args) {
		 
		 	System.setProperty(org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "warn");
		 	System.setProperty(org.slf4j.impl.SimpleLogger.LOG_FILE_KEY, "System.out");
		 	
		 	String hashTagQuery = "#happy";
		 	String csvDatabaseFile = "happy.txt";
		 	Double lowConfidenceLevel = 0.2;
		 	List<Tweet> lowConfidenceTweets = new ArrayList<>();
		 	
		 	if(args.length != 0) {
		 		hashTagQuery = args[0];
		 		csvDatabaseFile = args[1];
		 	}

		 	Database database = new CSVDatabase(csvDatabaseFile);
		 	List<Status> tweets = database.getTweets(hashTagQuery);
		 	
		 	SentiWordNet sentiWordNet = SentiWordNet.getInstance();
		 	Emoticons emoticons = Emoticons.getInstance();
		 	BoostWords boostWords = BoostWords.getInstance();
		 	
		 	TweetAnalyzer analyzer = new TweetAnalyzer(sentiWordNet,emoticons,boostWords);
		 	HashTag hashTag = new HashTag(hashTagQuery);
		 	for (Status tweet : tweets) {
		 		//System.out.println("\n");
		 		Tweet analyzedTweet = analyzer.getAnalyzedTweet(tweet);
		 		hashTag.add(analyzedTweet);
		 		//System.out.println(analyzedTweet.toString() +"\t"+analyzedTweet.getBayesianPositiveWeight() +"\t"+analyzedTweet.getBayesianNegativeWeight());
		 		System.out.println("Positive : " + analyzedTweet.getBayesianPositiveWeight());
		 		System.out.println("Negative : " + analyzedTweet.getBayesianNegativeWeight());
		 		
		 		if(analyzedTweet.getConfidenceLevel() < lowConfidenceLevel) {
		 			lowConfidenceTweets.add(analyzedTweet);
		 		}
			}
		 	
		 	System.out.println("------------------------------");
		 	System.out.println("This hashtag's positive weight: " + hashTag.getBayesianPositiveWeight());
		 	System.out.println("This hashtag's negative weight: " + hashTag.getBayesianNegativeWeight());
		 	System.out.flush();
		 	
		 	System.out.println("------------------------------");
		 	System.out.println("Tweets with low confidence levels: ");
		 	
		 	
		 	
	    }
}
