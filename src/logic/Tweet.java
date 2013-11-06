package logic;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import twitter4j.Status;


public class Tweet {
	
	Logger log = LoggerFactory.getLogger(Tweet.class);
	
	private Status rawTweet;
	private List<Word> words = new ArrayList<>();
	private Classification classification;

	private Double naiveBayesPositiveProbability;
	private Double naiveBayesNegativeProbability;

	public void setWords(List<Word> cleanTweet) {
		this.words = cleanTweet;
	}

	private Double getAveragePositiveWeight() {
		List<Double> weights = new ArrayList<Double>();
		for (Word w : words) {
			weights.add(w.getPositiveNormalizedWeight());
		}
		return Utils.getAverage(weights);
	}

	private Double getAverageNegativeWeight() {
		List<Double> weights = new ArrayList<Double>();
		for (Word w : words) {
			weights.add(w.getNegativeNormalizedWeight());
		}
		return Utils.getAverage(weights);
	}
	
	public Double getNormalizedPositiveWeight() {
		Double avgPosWeight = getAveragePositiveWeight();
		Double avgNegWeight = getAverageNegativeWeight();
		if((avgPosWeight + avgNegWeight) == 0.0) {
			log.warn("The tweet:{} have zero average positive and negative weight.",words.toString());
			return 0.0; 
		} else {
			return avgPosWeight / (avgPosWeight + avgNegWeight); 
		}
	}
	
	public Double getNormalizedNegativeWeight() {
		Double avgPosWeight = getAveragePositiveWeight();
		Double avgNegWeight = getAverageNegativeWeight();
		if((avgPosWeight + avgNegWeight) == 0.0) {
			log.warn("The tweet:{} have zero average positive and negative weight.",words.toString());
			return 0.0;
		}else {
			return avgNegWeight / (avgNegWeight + avgPosWeight); 
		}
	}
	
	public void setClassification(Classification c)	{
		this.classification = c;
	}
	
	public Classification getClassification() {
		return classification;
	}
	
	@Override
	public String toString() {
		StringBuilder out = new StringBuilder();
		for (Word w : words) {
			out.append(w + " ");
		}
		return out.toString();
	}

	public void setRawTweet(Status rawTweet) {
		this.rawTweet = rawTweet;
	}
	
	public Status getRawTweet() {
		return rawTweet;
	}

	public void setNaiveBayesPositiveProbability(Double probability) {
		this.naiveBayesPositiveProbability = probability;
	}
	
	public Double getNaiveBayesPositiveProbability() {
		return naiveBayesPositiveProbability;
	}
	
	public void setNaiveBayesNegativeProbability(Double probability) {
		this.naiveBayesNegativeProbability = probability;
	}
	
	public Double getNaiveBayesNegativeProbability() {
		return naiveBayesNegativeProbability;
	}
}
