package logic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Tokenizer {

	private static Logger log = LoggerFactory.getLogger(Tokenizer.class);
	
	/**
	 *  This list was taken from http://www.ranks.nl/resources/stopwords.html
	 **/
	private static List<String> stopWords = Arrays.asList(
			new String[]{"a", "about", "above", "after", "again", "against", "all", "am", "an", "and",
			"any", "are", "aren't", "as", "at", "be", "because", "been", "before", "being",
			"below", "between", "both", "but", "by", "can't", "cannot", "could", "couldn't", "did",
			"didn't", "do", "does", "doesn't", "doing", "don't", "down", "during", "each", "few",
			"for", "from", "further", "had", "hadn't", "has", "hasn't", "have", "haven't", "having",
			"he", "he'd", "he'll", "he's", "her", "here", "here's", "hers", "herself", "him",
			"himself", "his", "how", "how's", "i", "i'd", "i'll", "i'm", "i've", "if",
			"in", "into", "is", "isn't", "it", "it's", "its", "itself", "let's", "me",
			"more", "most", "mustn't", "my", "myself", "no", "nor", "not", "of", "off",
			"on", "once", "only", "or", "other", "ought", "our", "ours ", "ourselves", "out",
			"over", "own", "same", "shan't", "she", "she'd", "she'll", "she's", "should", "shouldn't",
			"so", "some", "such", "than", "that", "that's", "the", "their", "theirs", "them",
			"themselves", "then", "there", "there's", "these", "they", "they'd", "they'll", "they're", "they've",
			"this", "those", "through", "to", "too", "under", "until", "up", "very", "was",
			"wasn't", "we", "we'd", "we'll", "we're", "we've", "were", "weren't", "what", "what's",
			"when", "when's", "where", "where's", "which", "while", "who", "who's", "whom", "why",
			"why's", "with", "won't", "would", "wouldn't", "you", "you'd", "you'll", "you're", "you've",
			"your", "yours", "yourself", "yourselves"}
);
	
	public static List<Word> getTokens(String tweet) {
		List<Word> filteredTweet = new ArrayList<>();
		String links = "(http://.*[.].*/.*[ ]|http://.*[.].*/.*$)";
		String names = "@[A-Za-z0-9_]*";
		String multipleSpaces = "[ ]+";
		
 		tweet = tweet.replaceAll(names,"");
 		tweet = tweet.replaceAll("#","");
 		tweet = tweet.replaceAll(links,"");
 		tweet = tweet.replaceAll("\"", " ");
 		tweet = tweet.replaceAll(multipleSpaces," ");
 		
 		String[] wordArray = tweet.trim().split(" ");
 		for (int i = 0; i < wordArray.length; i++) {
 			filteredTweet.add(new Word(wordArray[i]));
		}
 		
 		filteredTweet = removeStopWords(filteredTweet);
 		deleteEmptyWords(filteredTweet);
 		
 		logCleanTweet(filteredTweet);
 		return filteredTweet;
	}
	
	private static void deleteEmptyWords(List<Word> filteredTweet) {
		for (int i = 0; i < filteredTweet.size(); i++) {
			if(filteredTweet.get(i).equals("")) {
				filteredTweet.remove(i);
			}
		}
	}

	private static void logCleanTweet(List<Word> words) {
		Tweet tweet = new Tweet();
		tweet.setWords(words);
		log.info("filtered tweet: {}", tweet.toString());
	}
	
	private static List<Word> removeStopWords(List<Word> words) {
		List<Word> importantWords = new ArrayList<>();
		importantWords.addAll(words);
		for (Word w : words) {
			if(isStopWord(w)) {
				importantWords.remove(w);
			}
		}
		return importantWords;
	}
	
	public static Boolean isStopWord(Word word) {
		return stopWords.contains(word.toString().toLowerCase());
	}
	
	public static Boolean isStopWord(String word) {
		return stopWords.contains(word.toLowerCase());
	}
}
