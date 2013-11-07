package logic;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import logic.naivebayes.NaiveBayesAnalyzer;
import logic.simplelinear.SimpleLinearAnalyzer;
import twitter4j.Status;

public class CoTrainer {

	private SimpleLinearAnalyzer slaClassifier;
	private NaiveBayesAnalyzer nbClassifier;
	private List<Status> tweets;
	
	private HashTag slaHashtag = new HashTag("");
	private HashTag nbHashtag = new HashTag("");

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
	
	public void run() {
		for (Status tweet : tweets) {
			Tweet t1 = slaClassifier.getAnalyzedTweet(tweet);
			slaHashtag.add(t1);
			Tweet t2 = nbClassifier.getAnalyzedTweet(tweet);
			nbHashtag.add(t2);
			
			if(t1.getClassification() == t2.getClassification()) {
				int label = t1.getClassification() == Classification.POSITIVE ? 1 : 0;
				
				List<Word> t = Tokenizer.getTokens(t1.getRawTweet().getText());
				StringBuilder b = new StringBuilder();
				for (Word word : t) {
					b.append(word + " ");
				}
				save(b.toString().trim(),label);
			}
		}
	}

	private void save(String tweet, int label) {
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new OutputStreamWriter(
			        new FileOutputStream("training_data/active_learning.csv", true), "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			System.err.println("Output file not found.");
			e.printStackTrace();
		}
		SimpleDateFormat formatter = new SimpleDateFormat("\"yyyy-dd-MM H:m:s:S\"");
		try {
			writer.append(formatter.format(new Date()) + "\t" +
							"\"@user\"" + "\t" +
							label + "\t" +
							"\""+tweet+"\"");
			writer.newLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void setRawTweets(List<Status> tweets) {
		this.tweets = tweets;
	}

	public Double getPositiveWeight() {
		return (slaHashtag.getNormalizedPositiveWeight() + nbHashtag.getPositivePercentage()) / 2.0;
	}

	public Double getNegativeWeight() {
		return (slaHashtag.getNormalizedNegativeWeight() + nbHashtag.getNegativePercentage()) / 2.0;
	}
	
}
