package database.twitter;

import java.util.LinkedList;
import java.util.List;

import twitter4j.Status;

public class UsernameFinder {

	// Don't use getUser from the twitterDatabase - it would probably cause a stack overflow as that function uses this class.
	// Sorry to have it exposed like this...
	private TwitterAPIDatabase twitterDatabase;
	
	private String previousQuery = "";
	private List<String> usernameList = new LinkedList<String>();

	public UsernameFinder(TwitterAPIDatabase twitterAPIDatabase) {
		this.twitterDatabase = twitterAPIDatabase;
	}

	public List<String> getUser(String namePrefix) {
		if(namePrefix.length() < 3) {
			return new LinkedList<String>();
		} else if(namePrefix.startsWith(previousQuery)){
			narrowUsernameList(namePrefix);
			return usernameList;
		}  else {
			List<Status> tweets = twitterDatabase.getTweets("@"+namePrefix);
			usernameList = getUsernames(tweets);
			return usernameList;
		}
	}

	private void narrowUsernameList(String namePrefix) {
		List<String> removable = new LinkedList<String>();
		for (String name : usernameList) {
			if(!name.startsWith(namePrefix)) removable.add(name);
		}
		usernameList.removeAll(removable);
	}

	private List<String> getUsernames(List<Status> tweets) {
		List<String> usernames = new LinkedList<String>();
		for (Status tweet : tweets) {
			usernames.add(tweet.getUser().getName());
		}
		return usernames;
	}

}