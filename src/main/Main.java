package main;

import java.util.List;
import java.util.Map;

import logic.WordFrequency;
import twitter4j.Status;
import database.Database;
import database.csv.CSVDatabase;

public class Main {
	 public static void main(String[] args) {
		 	Database database = new CSVDatabase(args[0]);
		 	List<Status> tweets = database.getTweets("#happy");
		 	
		 	//for(Status tweet: tweets) {
		 	//	System.out.println( tweet.getUser().getName() + " - " + tweet.getText());
		 	//}
		 	
		 	WordFrequency wf = new WordFrequency(tweets);
		 	wf.getTotalNumberOfWords();
		 	Map<String,Integer> wf_map = wf.getWordFrequency();
		 	
		 	for(Map.Entry<String, Integer> entry : wf_map.entrySet()) {
		 		System.out.println(entry.getKey()+"§ "+entry.getValue());
		 	}
		 	
		 	
	    }
}
