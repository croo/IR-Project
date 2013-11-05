package logic;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.tumba.spell.SpellChecker;
import twitter4j.Status;
import database.sentimental.BoostWords;
import database.sentimental.Emoticons;
import database.sentimental.SentiWordNet;
import database.sentimental.Weight;

public class SimpleLinearAnalyzer {

	Logger log = LoggerFactory.getLogger(SimpleLinearAnalyzer.class);
	private static final double UPPERCASE_BONUS = 1.1;
	private static final double EXCLAMATION_BONUS = 1.1;
	private SentiWordNet sentiWordNet;
	private Emoticons emoticons;
	private BoostWords boostWords;
	
	public static int numberOfUnknownWords = 0;
	private SpellChecker spellChecker;

	public SimpleLinearAnalyzer(SentiWordNet sentiWordNet, Emoticons emoticons, BoostWords boostWords,SpellChecker spellChecker) {
		this.sentiWordNet = sentiWordNet;
		this.emoticons = emoticons;
		this.boostWords = boostWords; // TODO: Not working yet, because the database isn't working yet.
		this.spellChecker = spellChecker;
	}

	public Tweet getAnalyzedTweet(Status rawTweet) {
		String text = rawTweet.getText();

		log.info("Analyzing the following tweet: {}",text);

		Tweet result = analyzeTweet(text);
		result.setRawTweet(rawTweet);
		return result;
	}

	private Tweet analyzeTweet(String text) {
		Tweet result = new Tweet(text);
		List<Word> words = filterTweet(text);
		
		logCleanTweet(words);
		
		List<Word> noValueFound = new ArrayList<>();
		for (Word word : words) {
			setWeightOfWord(word,noValueFound);
		}
		
		logNoValueWords(noValueFound);
		
		result.setWords(words);
		return result;
	}

	private void logNoValueWords(List<Word> noValueFound) {
		for (Word word : noValueFound) {
			log.warn("Couldn't find any value for word: '" + word.toString()+"'");
		}
	}

	private void logCleanTweet(List<Word> words) {
		Tweet tweet = new Tweet("");
		tweet.setWords(words);
		log.info("filtered tweet: {}", tweet.toString());
	}

	private void setWeightOfWord(/*in*/ Word word, /*out*/ List<Word> noValueFound) {
		Weight weight = new Weight(0.0,0.0);
		if (!important(word)) {
			/* don't do anything */;
		} else if(sentiWordNet.containsWord(word)){
			weight = sentiWordNet.getWeight(word);
		} else if (emoticons.containsWord(word)) {
			weight = emoticons.getWeight(word);
		} else {
			List<Word> fixedWords = sanitizeAndSpellcheck(word);
			logFixedWords(word, fixedWords);
			for (Word fixedWord : fixedWords) {
				if(fixedWord.equals("happy")) {
					System.out.println("FUK");
				}
				if (sentiWordNet.containsWord(fixedWord)) {
					weight.add(sentiWordNet.getWeight(fixedWord));
					log.warn("SUCCESS: spellcheck and sanitize solved the problem - {},{}",word.toString(),fixedWord.toString());
				} else {
					numberOfUnknownWords++;
					noValueFound.add(word);
					if(!word.equals(fixedWord)) {
						numberOfUnknownWords++;
						noValueFound.add(fixedWord);
					}
				}
			}
		}
		word.setPositiveWeight(Utils.getAverage(weight.positive));
		word.setNegativeWeight(Utils.getAverage(weight.negative));
		
		postprocessBonuses(word);
		//System.out.println(""+word +"\t" + word.getPositiveBayesianWeight() + "\t" + word.getNegativeBayesianWeight());
		log.trace(word + " : ( +{}; -{})",word.getPositiveBayesianWeight(),word.getNegativeBayesianWeight());
	}

	private void logFixedWords(Word word, List<Word> fixedWords) {
		StringBuilder wordList = new StringBuilder();
		for (Word w : fixedWords) {
			wordList.append(w).append(" ");
		}
		log.trace("Repaired '{}' to '{}'.",word.toString(), wordList.toString());
	}

	private List<Word> sanitizeAndSpellcheck(Word word) {
		 return spellCheck(sanitize(word));
	}

	private List<Word> spellCheck(Word sanitizedWord) {
		String mostSimilarWords = spellChecker.findMostSimilar(sanitizedWord.toString());
		List<Word> possibleWords = extractWords(mostSimilarWords);
		logPossibleWords(sanitizedWord, possibleWords);
		return possibleWords;
	}

	private void logPossibleWords(Word sanitizedWord, List<Word> possibleWords) {
		log.info("After spellcheck:");
		for (Word word : possibleWords) {
			log.info("Found '{}' as most similar word to '{}' in dictionary.",word.toString(),sanitizedWord.toString());
		}
	}

	private List<Word> extractWords(String mostSimilarWords) {
		List<Word> result = new ArrayList<>();
		if (mostSimilarWords != null) {
			String[] words = mostSimilarWords.split(" ");
			for (int i = 0; i < words.length; i++) {
				Word w = new Word(words[i]);
				result.add(w);
			}
		}
		return result;
	}

	private Word sanitize(Word word) {
		String sameCharacterMultipleTimes = "(.)(\\1)(\\1)+";
		String nonAlphabetCharacters = "[^a-zA-Z]";
		word = new Word(word.toString().replaceAll(nonAlphabetCharacters,""));
		word = new Word(word.toString().replaceAll(sameCharacterMultipleTimes,"$1"));
		return word;
	}

	private void postprocessBonuses(Word word) {
		if(isUpperCase(word)) {
			word.setPositiveWeight(word.getPositiveBayesianWeight()*UPPERCASE_BONUS);
			word.setNegativeWeight(word.getNegativeBayesianWeight()*UPPERCASE_BONUS);
		}
		
		if(haveExclamationMark(word)) {
			word.setPositiveWeight(word.getPositiveBayesianWeight()*EXCLAMATION_BONUS);
			word.setNegativeWeight(word.getNegativeBayesianWeight()*EXCLAMATION_BONUS);
		}
	}
	
	private boolean haveExclamationMark(Word word) {
		return word.toString().endsWith("!");
	}

	private boolean important(Word word) {
		if(word.toString().equals("I") || word.toString().equals("\"")) {
			return false;
		}
		return true;
	}

	private boolean isUpperCase(Word word) {
		return word.toString().matches("[A-Z]");
	}

	private List<Word> filterTweet(String tweet) {
		List<Word> filteredTweet = new ArrayList<>();
		String links = "(http://.*[.].*/.*[ ]|http://.*[.].*/.*$)";
		String names = "@[A-Za-z0-9_]*";
		String multipleSpaces = "[ ]+";
		
 		tweet = tweet.replaceAll(names,"");
 		tweet = tweet.replaceAll("#","");
 		tweet = tweet.replaceAll(links,"");
 		tweet = tweet.replaceAll(multipleSpaces," ");
 		
 		String[] wordArray = tweet.split(" ");
 		for (int i = 0; i < wordArray.length; i++) {
 			filteredTweet.add(new Word(wordArray[i]));
		}
 		return filteredTweet;
	}

	public Tweet getAnalyzedTweet(String text) {
		log.info("Analyzing the following tweet: {}",text);

		Tweet result = analyzeTweet(text);
		return result;
	}

	public List<Tweet> getAnalyzedTweets(List<Status> tweets) {
		List<Tweet> analyzedTweets = new ArrayList<>();
		for (Status tweet : tweets) {
			analyzedTweets.add(getAnalyzedTweet(tweet));
		}
		return analyzedTweets;
	}
}
