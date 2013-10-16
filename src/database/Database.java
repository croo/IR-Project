package database;

import java.util.List;

import twitter4j.Status;
import twitter4j.User;

public interface Database {
	
	List<Status> getTweets(String query);
	
	List<String> getUsers(String namePrefix);
}
