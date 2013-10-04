package logic;

import database.sentiwordnet.SentiWordNet;


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
	
	public String getWord() {
		return word;
	}
	public Double getPositiveWeight() {
		return positiveWeight;
	}
	public Double getNegativeWeight() {
		return negativeWeight;
	}
}
