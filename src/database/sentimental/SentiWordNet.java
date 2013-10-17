package database.sentimental;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import logic.Utils;
import logic.Word;

/**
 * 
 * A Singleton class of SentiWordNet data se
public class Weights {

}
t.
 * 
 * Usage
 * 
 * @author croo
 * 
 */
public class SentiWordNet {

	private static final int WORDS_COLUMN = 4;

	private static final String SENTIWORDNET_PATH = "sentiwordnet_data/SentiWordNet_3.0.0_20130122.txt";

	private static SentiWordNet INSTANCE = null;

	private List<String[]> data = new LinkedList<String[]>();

	private Map<String, Weight> words;

	public static SentiWordNet getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new SentiWordNet();
		}
		return INSTANCE;
	}

	private SentiWordNet() {
		System.out.println("Initializing sentiWordNet database...");
		Integer lineNo = 0;
		List<String> csvLines = Utils.readAllLines(SENTIWORDNET_PATH,Charset.defaultCharset());
		for (String line : csvLines) {
			lineNo++;
			data.add(line.split("\t"));
		}
		createDictionary();
		System.out.println("Done.");
	}
	
	public Boolean containsWord(Word word) {
		return words.containsKey(getKey(word));
	}
	
	private String getKey(Word word) {
		return word.toString().toLowerCase().replaceAll("[.! ,\"]","");
	}

	private void createDictionary() {
		words = new HashMap<>();
		for (String[] line : data) {
			String[] wordsFromData = line[WORDS_COLUMN].split(" ");
			Double positiveWeight = Double.parseDouble(line[2]);
			Double negativeWeight = Double.parseDouble(line[3]);
			for (int i = 0; i < wordsFromData.length; i++) {
				String word = wordsFromData[i].split("#")[0].trim();
				if (!words.containsKey(word)) {
					words.put(word, new Weight());
				}
				words.get(word).positive.add(positiveWeight);
				words.get(word).negative.add(negativeWeight);
			}
		}
	}

	public Weight getWeight(Word word) {
		return words.get(getKey(word));
	}

	@Deprecated
	public Double getWeightByLists(String word) {
		List<Double> positiveWeights = new ArrayList<Double>();
		List<Double> negativeWeights = new ArrayList<Double>();
		for (String[] line : data) {
			String sentinetWord = line[WORDS_COLUMN];
			Double positiveWeight = Double.parseDouble(line[2]);
			Double negativeWeight = Double.parseDouble(line[3]);
			if (sentinetWord.matches(".*([\t ]" + word + "#[1-9]*|^" + word
					+ "#[1-9]*).*")) {
				positiveWeights.add(positiveWeight);
				negativeWeights.add(negativeWeight);
			}
		}
		Double posAvg = getAverage(positiveWeights);
		Double negAvg = getAverage(negativeWeights);
		return posAvg - negAvg;
	}

	private Double getAverage(List<Double> numbers) {
		Double result = 0.0;
		for (Double number : numbers) {
			result += number;
		}
		if (numbers.size() > 0)
			return result / numbers.size();
		else
			return 0.0;
	}

}
