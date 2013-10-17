package logic;

import database.sentimental.SentiWordNet;


/**
 * 
 * This class holds a word and all it's related data.
 * Like it's positive and negative semantic score,
 * possibly meaning or it's context.
 * 
 * @author croo
 *
 */
public class Word {
	
	private String word;
	private Double positiveWeight; 
	private Double negativeWeight;

	public Word(String word) {
		this.word = word;
	}
	
	public Double getPositiveWeight() {
		return positiveWeight;
	}
	public Double getNegativeWeight() {
		return negativeWeight;
	}
	
	@Override
	public String toString() {
		return word;
	}

	public void setPositiveWeight(Double average) {
		positiveWeight = average;
	}

	public void setNegativeWeight(Double average) {
		negativeWeight = average;
	}
}
