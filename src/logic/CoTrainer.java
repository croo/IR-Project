package logic;

import java.util.List;

import logic.naivebayes.NaiveBayesAnalyzer;
import logic.simplelinear.SimpleLinearAnalyzer;
import twitter4j.Status;

public class CoTrainer {

	private SimpleLinearAnalyzer slaClassifier;
	private NaiveBayesAnalyzer nbClassifier;
	private List<Status> tweets;

	public CoTrainer(SimpleLinearAnalyzer slaClassifier, NaiveBayesAnalyzer nbClassifier) {
		this.slaClassifier = slaClassifier;
		this.nbClassifier = nbClassifier;
	}

	public String[] getUncertainTweets() {
		String[] uncertainTweets = new String[5];
		int i = 0;
		for (Status tweet : tweets) {
			Tweet t1 = slaClassifier.getAnalyzedTweet(tweet);
			Tweet t2 = nbClassifier.getAnalyzedTweet(tweet);
			if(t1.getClassification() != t2.getClassification() && !nbClassifier.trainingSetContains(tweet)) {
				uncertainTweets[i] = tweet.getText();
				i++;
			}
			if(i > 4) break;
		}
		return uncertainTweets;
	}

	public void setRawTweets(List<Status> tweets) {
		this.tweets = tweets;
	}
	
}
