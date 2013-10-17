package logic;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Tweet {
	
	Logger log = LoggerFactory.getLogger(Tweet.class);
	
	private List<Word> words = new ArrayList<>();
	private String originalText;

	public Tweet(String text){
		this.originalText = text;
	}
	
	public void setWords(List<Word> cleanTweet) {
		this.words = cleanTweet;
	}

	public Double getAveragePositiveWeight() {
		List<Double> weights = new ArrayList<Double>();
		for (Word w : words) {
			weights.add(w.getPositiveWeight());
		}
		return Utils.getAverage(weights);
	}

	public Double getAverageNegativeWeight() {
		List<Double> weights = new ArrayList<Double>();
		for (Word w : words) {
			weights.add(w.getNegativeWeight());
		}
		return Utils.getAverage(weights);
	}
	
	
	/**
	 * tweet=    sad    happy cake. 
	   negative  0.625  0.125 0.05 = 0.2666
	   positive  0.0    0.75  0.45 = (0.0 + 0.75 + 0.45) / 3 = avg 0.4 
	
		P( tweet | positive )  = 0.4 / { 0.4 +  0.2666} = 0.6
		P( tweet | negative ) = (1 - P(t | +) ) = 1 - 0.6 = 0.4
	 * @return
	 */
	public Double getBayesianPositiveWeight() {
		Double avgPosWeight = getAveragePositiveWeight();
		Double avgNegWeight = getAverageNegativeWeight();
		if((avgPosWeight + avgNegWeight) == 0.0) {
			log.warn("The tweet:{} have zero average positive and negative weight.",words.toString());
			return 0.0; 
		} else {
			return avgPosWeight / (avgPosWeight + avgNegWeight); 
		}
	}
	
	public Double getBayesianNegativeWeight() {
		Double avgPosWeight = getAveragePositiveWeight();
		Double avgNegWeight = getAverageNegativeWeight();
		if((avgPosWeight + avgNegWeight) == 0.0) {
			log.warn("The tweet:{} have zero average positive and negative weight.",words.toString());
			return 0.0;
		}else {
			return avgNegWeight / (avgNegWeight + avgPosWeight); 
		}
		
	}
	
	@Override
	public String toString() {
		StringBuilder out = new StringBuilder();
		for (Word w : words) {
			out.append(w + " ");
		}
		return out.toString();
	}
	
}
