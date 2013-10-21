package main;

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

public class Main {
	 public static void main(String[] args) {
		 
		 	System.setProperty(org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "ERROR");
		 	System.setProperty(org.slf4j.impl.SimpleLogger.LOG_FILE_KEY, "System.out");
		 	
		 	String hashTagQuery = "#happy";
		 	String csvDatabaseFile = "happy.txt";
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
		 		//System.out.println("Positive : " + analyzedTweet.getBayesianPositiveWeight());
		 		//System.out.println("Negative : " + analyzedTweet.getBayesianNegativeWeight());
			}
		 	
		 	//System.out.println("------------------------------");
		 	//System.out.println("This hashtag's positive weight: " + hashTag.getBayesianPositiveWeight());
		 	//System.out.println("This hashtag's negative weight: " + hashTag.getBayesianNegativeWeight());
		 	
		 	
		 	
		 	/*String searchString = tweets.get(0).getUser().getName();
		 	List<String> users = database.getUsers("@a");
		 	for (String user : users) {
				System.out.println(user);
			}*/
		 	
//		 	WordFrequency wf = new WordFrequency(tweets);
//		 	wf.getTotalNumberOfWords();
//		 	Map<String,Integer> wf_map = wf.getWordFrequency();
//		 	
//		 	for(Map.Entry<String, Integer> entry : wf_map.entrySet()) {
//		 		System.out.println(entry.getKey()+ args[1] + " "+entry.getValue() + "\r");
//		 	}
		 
		 
		 
//		 	SentiWordNet s = SentiWordNet.getInstance();
//		 	System.out.println("Sad: " + s.getWeight("sad"));
//		 	System.out.println("Happy: " + s.getWeight("happy"));
//		 	System.out.println("cake: " + s.getWeight("cake"));
//		 	System.out.println("orange: " + s.getWeight("orange"));
//		 	System.out.println("disaster: " + s.getWeight("disaster"));

//		 	System.out.println("earthquake: " + s.getWeight("earthquake"));
	    }
}
