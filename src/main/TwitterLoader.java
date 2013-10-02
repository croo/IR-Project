package main;

import java.util.List;
import java.util.Map;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterLoader {
	 public static void main(String[] args) {
		 	
		 	args = new String[2];
		 	args[0] = "#NASDAQ";
	        if (args.length < 1) {
	            System.out.println("java twitter4j.examples.search.SearchTweets [query]");
	            System.exit(-1);
	        }
	        
	        ConfigurationBuilder cb = new ConfigurationBuilder();
	        cb.setDebugEnabled(true)
	          .setOAuthConsumerKey("JFm75nHCpTV8STSmSuFQ")
	          .setOAuthConsumerSecret("9nHk3iCawNaqP0mGfeRlBvxqwRT2XXBYgcGv1Z8wSmA")
	          .setOAuthAccessToken("1886475672-6HkkiT0Sm5LkGfwXLhMj574PEnWyBHfGx5RG9wi")
	          .setOAuthAccessTokenSecret("S9hLN3ogFG9LQX7sq41pvdWotTDYmu9L5WpbSQTl7y0");
	        TwitterFactory tf = new TwitterFactory(cb.build());
	        Twitter twitter = tf.getInstance();
	        int numberOfTweets = 0;
	        
	        //Analyst emo = new Analyst();
	        
	        try {
	            Query query = new Query(args[0]);
	            query.setCount(100);
	            query.setLang("en");
	            query.setResultType(Query.MIXED);
	            QueryResult result;
	            
//	            do {
	                result = twitter.search(query);
	                List<Status> tweets = result.getTweets();
	                for (Status tweet : tweets) {
	                	numberOfTweets++;
	                	
	                    System.out.println(tweet.getCreatedAt() + " - @" + tweet.getUser().getScreenName() + " - " + tweet.getText());
	                    //double emotion = emo.getEmotionOfTweet(tweet.getText());
	                    //System.out.println("The emotional value of this tweet is: " + emotion);
	                }
//	            } while ((query = result.nextQuery()) != null);
	                
	                WordFrequency wf = new WordFrequency(tweets);
	                Map<String,Integer> wf_map = wf.getWordFrequency();
	                System.out.println("Total number of words: " + wf.getTotalNumberOfWords());
	                System.out.println("" + wf_map.entrySet().iterator().next().getKey() + " - " + wf_map.entrySet().iterator().next().getValue());
	                System.out.println("" + wf_map.entrySet().iterator().next().getKey() + " - " + wf_map.entrySet().iterator().next().getValue());
	                System.out.println("" + wf_map.entrySet().iterator().next().getKey() + " - " + wf_map.entrySet().iterator().next().getValue());
	                System.out.println("" + wf_map.entrySet().iterator().next().getKey() + " - " + wf_map.entrySet().iterator().next().getValue());
	                System.out.println("" + wf_map.entrySet().iterator().next().getKey() + " - " + wf_map.entrySet().iterator().next().getValue());
	                System.out.println("" + wf_map.entrySet().iterator().next().getKey() + " - " + wf_map.entrySet().iterator().next().getValue());
	                System.out.println("" + wf_map.entrySet().iterator().next().getKey() + " - " + wf_map.entrySet().iterator().next().getValue());
	                System.out.println("" + wf_map.entrySet().iterator().next().getKey() + " - " + wf_map.entrySet().iterator().next().getValue());
	                
	                
	                System.out.println("Number of tweets is: " + numberOfTweets);
	            System.exit(0);
	        } catch (TwitterException te) {
	            te.printStackTrace();
	            System.out.println("Failed to search tweets: " + te.getMessage());
	            System.exit(-1);
	        }
	    }
}
