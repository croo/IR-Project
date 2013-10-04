package database.sentiwordnet;

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
		for(String[] line: data) {
			System.out.println(line[2]+","+line[3]+","+line[4]);
		}
		
		return 0.0;
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		reader.close();
	}
}
