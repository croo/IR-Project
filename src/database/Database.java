package database;

import java.util.List;

import twitter4j.Status;

public interface Database {
	
	List<Status> getTweets(String query);
	
}
