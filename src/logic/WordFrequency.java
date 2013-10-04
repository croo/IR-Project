package logic;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import twitter4j.Status;


/**
 * 
 * This class gets a list of tweets and gives back varous types of statistics about it.
 * Eg.: word frequency, number of words.
 * 
 * @author croo
 *
 */
public class WordFrequency {

	private List<Status> tweets;
	
	private Integer numberOfWords = 0;

	private Map<String, Integer> wordFrequency;

	public WordFrequency(List<Status> tweets) {
		this.tweets = tweets;
		this.wordFrequency = generateWordFrequency();
	}

	private Map<String, Integer> generateWordFrequency() {
		Map<String, Integer> result = new HashMap<String,Integer>();
		
		for(Status tweet : tweets) {
			String[] tweet_words = tweet.getText().split(" ");
			for (int i = 0; i < tweet_words.length; i++) {
				numberOfWords++;
				String tweet_word = tweet_words[i].toLowerCase();
				if(result.containsKey(tweet_word)) {
					result.put(tweet_word, result.get(tweet_word) + 1);
				} else {
					result.put(tweet_word, 1);
				}
			}
		}
		
		return MapUtils.sortByValue(result);
	}
	
	public Map<String,Integer> getWordFrequency() {
		return wordFrequency;
	}
	
	public Integer getTotalNumberOfWords() {
		return numberOfWords;
	}

}
