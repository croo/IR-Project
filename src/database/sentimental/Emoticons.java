package database.sentimental;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import logic.Utils;
import logic.Word;

public class Emoticons {

	private static final int POSITIVE_WEIGHT_COLUMN = 0;
	private static final int NEGATIVE_WEIGHT_COLUMN = 1;
	private static final int WORDS_COLUMN = 2;
	private static final String EMOTICON_SCORE_PATH = "sentiwordnet_data/emoticon_scoring_utf-8.txt";
	private static Emoticons INSTANCE;

	private Map<String, Weight> emoticons;

	public static Emoticons getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new Emoticons();
		}
		return INSTANCE;
	}

	private List<String[]> data = new ArrayList<>();

	private Emoticons() {
		System.out.println("Initializing emoticons database...");
		Integer lineNo = 0;
		List<String> csvLines = null;
		try {
			csvLines = Utils.readAllLines(EMOTICON_SCORE_PATH,Charset.forName("utf-8"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (String line : csvLines) {
			lineNo++;
			data.add(line.split("\t"));
		}
		createDictionary();
		System.out.println("Done.");
	}

	private void createDictionary() {
		emoticons = new HashMap<>();
		for (String[] line : data) {
			String[] wordsFromData = line[WORDS_COLUMN].split(" ");
			Double positiveWeight = Double.valueOf(line[POSITIVE_WEIGHT_COLUMN]);
			Double negativeWeight = Double.valueOf(line[NEGATIVE_WEIGHT_COLUMN]);
			for (int i = 0; i < wordsFromData.length; i++) {
				String word = wordsFromData[i].split("#")[0].trim();
				if (!emoticons.containsKey(word)) {
					emoticons.put(word, new Weight());
				}
				emoticons.get(word).positive.add(positiveWeight);
				emoticons.get(word).negative.add(negativeWeight);
			}
		}
	}

	public Weight getWeight(Word emoticon) {
		return emoticons.get(emoticon.toString());
	}

	public boolean containsWord(Word word) {
		return emoticons.containsKey(word.toString());
	}
}
