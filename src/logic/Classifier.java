package logic;

import twitter4j.Status;

public interface Classifier {

	Tweet getAnalyzedTweet(Status rawTweet);
}
