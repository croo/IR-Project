package database.twitter;

import java.util.List;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;
import database.Database;

public class TwitterAPIDatabase implements Database{

	private Query query;
	
	public TwitterAPIDatabase() {
		query = new Query();
		query.setCount(100);
		query.setLang("en");
		query.setResultType(Query.MIXED);
		
	}
	
	public void setQuery(Query query) {
		this.query = query;
	}
	
	@Override
	public List<Status> getTweets(String queryString) {
        Twitter twitter = getTwitterApi();
        List<Status> tweets = null;
        try {
        	query.setQuery(queryString);
            QueryResult result;
//            do {
                result = twitter.search(query);
                tweets = result.getTweets();
//                for (Status tweet : tweets) {
//                    System.out.println(tweet.getCreatedAt() + " - @" + tweet.getUser().getScreenName() + " - " + tweet.getText());
//                }
//            } while ((query = result.nextQuery()) != null);
            removeDuplicates(tweets);
        } catch (TwitterException e) {
            e.printStackTrace();
            System.out.println("Failed to search tweets: " + e.getMessage());
        }
        return tweets;
	}

	private void removeDuplicates(List<Status> tweets) {
		for (int i = 0; i < tweets.size(); i++) {
			for (int j = i; j < tweets.size(); j++) {
				if(i != j) {
					if(tweets.get(i).getText().equals(tweets.get(j).getText())) {
						System.err.println("Removed :" + tweets.get(j).getText());
						tweets.remove(j);
					}
				}
			}
		}
	}

	private Twitter getTwitterApi() {
		ConfigurationBuilder cb = new ConfigurationBuilder();
        setupOAuth(cb);
        TwitterFactory tf = new TwitterFactory(cb.build());
        Twitter twitter = tf.getInstance();
		return twitter;
	}

	private void setupOAuth(ConfigurationBuilder cb) {
		cb.setDebugEnabled(true)
          .setOAuthConsumerKey("JFm75nHCpTV8STSmSuFQ")
          .setOAuthConsumerSecret("9nHk3iCawNaqP0mGfeRlBvxqwRT2XXBYgcGv1Z8wSmA")
          .setOAuthAccessToken("1886475672-6HkkiT0Sm5LkGfwXLhMj574PEnWyBHfGx5RG9wi")
          .setOAuthAccessTokenSecret("S9hLN3ogFG9LQX7sq41pvdWotTDYmu9L5WpbSQTl7y0");
	}

}
