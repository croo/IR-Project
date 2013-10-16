package database.sentiwordnet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import logic.MapUtils;

/**
 * 
 * A Singleton class of SentiWordNet data set.
 * 
 * Usage
 * 
 * @author croo
 * 
 */
public class SentiWordNet {

	private static final int WORDS = 4;

	private static final String SENTIWORDNET_PATH = "sentiwordnet_data/SentiWordNet_3.0.0_20130122.txt";

	private static SentiWordNet INSTANCE = null;

	private List<String[]> data = new LinkedList<String[]>();

	private Map<String, Pair> sentiWords;

	public static SentiWordNet getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new SentiWordNet();
		}
		return INSTANCE;
	}

	private SentiWordNet() {
		System.out.println("Initializing sentiWordNet database...");
		Integer lineNo = 0;
		List<String> csvLines = MapUtils.readAllLines(SENTIWORDNET_PATH);
		for (String line : csvLines) {
			lineNo++;
			data.add(line.split("\t"));
		}
		createWordList();
		System.out.println("Done.");
	}

	private void createWordList() {
		sentiWords = new HashMap<>();
		for (String[] line : data) {
			String[] words = line[WORDS].split(" ");
			Double positiveWeight = Double.parseDouble(line[2]);
			Double negativeWeight = Double.parseDouble(line[3]);
			for (int i = 0; i < words.length; i++) {
				String word = words[i].split("#")[0].trim();
				if (!sentiWords.containsKey(word)) {
					sentiWords.put(word, new Pair());
				}
				sentiWords.get(word).positive.add(positiveWeight);
				sentiWords.get(word).negative.add(negativeWeight);
			}
		}
	}

	public Double getWeight(String word) {
		Pair weights = sentiWords.get(word);
		if (weights != null) {
			return getAverage(weights.positive) - getAverage(weights.negative);
		} else {
			return null;
		}
	}

	@Deprecated
	public Double getWeightByLists(String word) {
		List<Double> positiveWeights = new ArrayList<Double>();
		List<Double> negativeWeights = new ArrayList<Double>();
		for (String[] line : data) {
			String sentinetWord = line[WORDS];
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

	private class Pair {
		
		public List<Double> positive = new ArrayList<>();
		public List<Double> negative = new ArrayList<>();

	}
}
