package logic.naivebayes;

import java.util.List;

import logic.Classification;
import logic.Tokenizer;
import logic.Tweet;
import logic.Word;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import twitter4j.Status;


public class NaiveBayesAnalyzer {
	
	Logger log = LoggerFactory.getLogger(NaiveBayesAnalyzer.class);

	private TrainingDataset trainingSet = new TrainingDataset("training_data/merged_tabbed_training_data.csv");
	
	public Tweet getTAnalyzedTweet(Status rawTweet) {
		String text = rawTweet.getText();
		
		log.info("Analyzing the following tweet: {}",text);

		Tweet result = analyzeTweet(text);
		result.setRawTweet(rawTweet);
		return result;
	}

	private Tweet analyzeTweet(String text) {
		Tweet tweet = new Tweet();
		List<Word> words = Tokenizer.getTokens(text);
		
		setWeightOfWords(tweet, words);
		tweet.setWords(words);
		return tweet;
	}
	
	private void setWeightOfWords(Tweet tweet, /*in*/ List<Word> words) {
			Double positiveProbability = getProbability(words,Classification.POSITIVE);
			tweet.setNaiveBayesPositiveProbability(positiveProbability);
			
			Double negativeProbability = getProbability(words,Classification.NEGATIVE);
			tweet.setNaiveBayesNegativeProbability(negativeProbability);

			tweet.setClassification(positiveProbability > negativeProbability ? Classification.POSITIVE : Classification.NEGATIVE);
	}

	private Double getProbability(List<Word> words, Classification label) {
		
		Double probabilityOfLabel = trainingSet.probabilityOf(label);
		Double probabilityOfWordsBeingInClass = trainingSet.probabilityOf(words,label);
		//Double probabilityOfWords = trainingSet.probabilityOf(words);
		
		double probability = (probabilityOfLabel * probabilityOfWordsBeingInClass); 
		//			/ (probabilityOfWords);
		return probability;
	}

}
