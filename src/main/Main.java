package main;

import java.util.List;

import twitter4j.Status;
import database.Database;
import database.csv.CSVDatabase;

public class Main {
	 public static void main(String[] args) {
		 	Database database = new CSVDatabase("happy.txt");
		 	List<Status> tweets = database.getTweets("#happy");
		 	
		 	for(Status tweet: tweets) {
		 		System.out.println(tweet.getText());
		 		String message = tweet.getText();
		 		message = message.replaceAll("@[A-Za-z0-9_]*","");
		 		message = message.replaceAll("#","");
		 		message = message.replaceAll("(http://.*[.].*/.*[ ]|http://.*[.].*/.*$)","");
		 		message = message.replaceAll("[ ]+"," ");
		 		
		 		System.out.println(message);
		 		System.out.println("");
		 	}
		 	
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
