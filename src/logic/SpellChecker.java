package logic;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SpellChecker {
	
	private static Logger log = LoggerFactory.getLogger(SpellChecker.class);
	
	private static pt.tumba.spell.SpellChecker spellChecker;
	
	private static SpellChecker INSTANCE = null;
	
	private SpellChecker () {
		initSpellChecker();
	}
	
	private static void initSpellChecker() {
		System.out.println("Initializing JaSpell checker...");
		spellChecker = new pt.tumba.spell.SpellChecker();
		String dictionary = "english_dict/english.txt";
		String commonMisspells = "english_dict/common-misspells.txt";
		try {
			spellChecker.initialize(dictionary, commonMisspells);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Initialization of the spellchecker failed with the following parameters:\n {}, {}, {}",dictionary,commonMisspells);
		}
		System.out.println("Done.");
	}
	
	public static SpellChecker getInstance() {
		if(spellChecker == null) {
			INSTANCE = new SpellChecker();
		}
		return INSTANCE;
	}
	
	public List<String> getCorrections(String word) {
		String[] words = spellChecker.findMostSimilar(word).split(" ");
		List<String> result = new ArrayList<>();
		for (int i = 0; i < words.length; i ++) {
			result.add(words[i]);
		}
		return result;
	}
	
	public List<Word> getCorrections(Word word) {
		return spellCheck(word);
	}
	
	public Word sanitize(Word word) {
		String sameCharacterMultipleTimes = "(.)(\\1)(\\1)+";
		String nonAlphabetCharacters = "[^a-zA-Z]";
		word = new Word(word.toString().replaceAll(nonAlphabetCharacters,""));
		word = new Word(word.toString().replaceAll(sameCharacterMultipleTimes,"$1"));
		return word;
	}
	
	public String sanitize(String word) {
		return sanitize(new Word(word)).toString();
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
}
