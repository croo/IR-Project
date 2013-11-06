package logic.naivebayes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import logic.Classification;
import logic.Tokenizer;
import logic.Word;
import twitter4j.Status;
import database.csv.CSVDatabase;
import database.csv.StatusCSVImpl;

/*package*/ class TrainingDataset {
	
	Logger log = LoggerFactory.getLogger(TrainingDataset.class);

	
	private List<Status> tweets;
	private int numberOfPositiveTweets;
	private int numberOfNegativeTweets;
	
	private int vocabularySize;
	private int numberOfPositiveWords;
	private int numberOfNegativeWords;
	
	private Map<Word,Integer> positiveBagOfWords = new HashMap<>();
	private Map<Word,Integer> negativeBagOfWords = new HashMap<>();
	
	public TrainingDataset(String trainingsetFilename) {
		System.out.println("Loading training data for naive bayes...");
		CSVDatabase csv = new CSVDatabase(trainingsetFilename);
		tweets = csv.getAllTweets();
		
		for (Status s : tweets) {
			List<Word> tweet = Tokenizer.getTokens(s.getText());
			vocabularySize += tweet.size();
			if (((StatusCSVImpl)s).getLabel() == Classification.POSITIVE) {
				putInBagOfWords(tweet,positiveBagOfWords);
				numberOfPositiveWords += tweet.size();
				numberOfPositiveTweets++;
			} else {
				putInBagOfWords(tweet,negativeBagOfWords);
				numberOfNegativeWords += tweet.size();
				numberOfNegativeTweets++;
			}
		}
		System.out.println("Done.");
	}
	
	public Integer getVocabularySize() {
		return vocabularySize;
	}
	
	private void putInBagOfWords(List<Word> tweet, Map<Word,Integer> bagOfWords) {
		for (Word word : tweet) {
			if(!bagOfWords.containsKey(word)) {
				bagOfWords.put(word,1);
			} else {
				bagOfWords.put(word, bagOfWords.get(word) + 1);
			}
		}
	}

	public Double getNumberOfTweets(Classification label) {
		return label == Classification.POSITIVE ? new Double(numberOfPositiveTweets) : new Double(numberOfNegativeTweets);
	}

	public Double probabilityOf(Classification label) {
		return label == Classification.POSITIVE ? new Double(numberOfPositiveTweets) / new Double(tweets.size()) :
													new Double(numberOfNegativeTweets) / new Double(tweets.size()) ;
	}

	public Double probabilityOf(List<Word> words, Classification label) {
		Double probability = 1.0;
		
		for (Word word : words) {
			probability *= probabilityOf(word,label);
		}
		return probability;
	}

	public Double probabilityOf(List<Word> words) {
		Double probability = 1.0;
		for (Word word : words) {
			probability *= new Double(count(word))/new Double(vocabularySize);
		}
		return probability;
	}
	
	private Integer count(Word word) {
		Integer count = 0;
		count += positiveBagOfWords.get(word) == null ? 0 : positiveBagOfWords.get(word);
		count += negativeBagOfWords.get(word) == null ? 0 : negativeBagOfWords.get(word);
		return count;
	}

	private Double probabilityOf(Word word, Classification label) {
		Double numberOfWordInLabel = new Double(count(word, label));
		Double numberOfAllWordsInLabel = (label == Classification.POSITIVE ? new Double(numberOfPositiveWords) : 
			new Double(numberOfNegativeWords));
		Double probability = numberOfWordInLabel	/ numberOfAllWordsInLabel;
		if(probability.equals(0.0)) {
			return 1.0 / new Double(vocabularySize);
		}
		return probability;
	}
	
	private Integer count(Word word, Classification label) {
		if(label == Classification.POSITIVE) {
			return positiveBagOfWords.get(word) == null ? 0 : positiveBagOfWords.get(word);
		} else {
			return negativeBagOfWords.get(word) == null ? 0 : negativeBagOfWords.get(word);
		}
	}
}