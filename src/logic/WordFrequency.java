package logic;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
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
		
		return sortByValue(result);
	}
	
	private <K, V extends Comparable<? super V>> Map<K, V> sortByValue( Map<K, V> map ) // this code is stolen from Stackoverflow
    {
        List<Map.Entry<K, V>> list =
            new LinkedList<Map.Entry<K, V>>( map.entrySet() );
        Collections.sort( list, new Comparator<Map.Entry<K, V>>()
        {
            public int compare( Map.Entry<K, V> o1, Map.Entry<K, V> o2 )
            {
                return (o1.getValue()).compareTo( o2.getValue() );
            }
        } );

        Map<K, V> result = new LinkedHashMap<K, V>();
        for (Map.Entry<K, V> entry : list)
        {
            result.put( entry.getKey(), entry.getValue() );
        }
        return result;
    }
	
	public Map<String,Integer> getWordFrequency() {
		return wordFrequency;
	}
	
	public Integer getTotalNumberOfWords() {
		return numberOfWords;
	}

}
