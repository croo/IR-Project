package main;

import java.util.List;

import twitter4j.Status;
import database.Database;
import database.csv.CSVDatabase;

public class Main {
	 public static void main(String[] args) {
		 	Database database = new CSVDatabase("sad.txt");
		 	List<Status> tweets = database.getTweets("#happy");
		 	
		 	for(Status tweet: tweets) {
		 		System.out.println( tweet.getUser().getName() + " - " + tweet.getText());
		 	}
		 	
	    }
}
