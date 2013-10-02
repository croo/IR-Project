package main;

import java.util.ArrayList;
import java.util.List;

public class Analyst {

	List<String> emoticons = new ArrayList<String>();
	
	/**
	 * 
	 * @return value should be between -1.0 and 1.0
	 */
	public double getEmotionOfTweet(String tweet) {
		
		String[] tweetWords = tweet.split("[ ,.;]");
		return 0.0;
	}

}
