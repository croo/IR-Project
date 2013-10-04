package database.sentiwordnet;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import logic.MapUtils;
import au.com.bytecode.opencsv.CSVReader;


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
private static final String SENTIWORDNET_PATH = "sentiwordnet_data/SentiWordNet_3.0.0_20130122.txt";

	private static SentiWordNet INSTANCE = null;
	
	private CSVReader reader;

	private List<String[]> data = new LinkedList<String[]>();
	
	
	public static SentiWordNet getInstance () {
		if(INSTANCE == null) {
			INSTANCE = new SentiWordNet();
		}
		return INSTANCE;
	}

	private SentiWordNet() {
		Integer lineNo = 0;
		List<String> csvLines = MapUtils.readAllLines(SENTIWORDNET_PATH);
		for (String line : csvLines) {
			lineNo++;
			data.add(line.split("\t"));
		}
	}

	public Double getWeight(String word) {
		List<Double> positiveWeights = new ArrayList<Double>();
		List<Double> negativeWeights = new ArrayList<Double>();
		for(String[] line: data) {
			String sentinetWord = line[4];
			Double positiveWeight = Double.parseDouble(line[2]);
			Double negativeWeight = Double.parseDouble(line[3]);
			if(sentinetWord.matches(".*([\t ]"+word+"#[1-9]*|^"+word+"#[1-9]*).*")) {
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
		for(Double number:numbers) {
			result += number;
		}
		if(numbers.size() > 0)
			return result / numbers.size();
		else
			return 0.0;
	}
	
	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		reader.close();
	}
}
