package database.csv;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import logic.Classification;
import logic.Utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import twitter4j.Status;
import database.Database;

public class CSVDatabase implements Database {


	private static final String CSV_DELIMITER = "\t";
	private static final int DATE_COLUMN = 0;
	private static final int NAME_COLUMN = 1;
	private static final int LABEL_COLUMN = 2;
	private static final int TEXT_COLUMN = 3;

	private Logger log = LoggerFactory.getLogger(CSVDatabase.class);
	
	private List<String> csvLines;

	public CSVDatabase(String fileName) throws IOException {
		csvLines = Utils.readAllLines(fileName);
	}

	@Override
	public List<Status> getTweets(String query) {
		
		List<Status> result = new ArrayList<Status>();
		
		for (String line : csvLines) {
			log.trace("Read line from csv: " + line);
			String[] row = line.split(CSV_DELIMITER);
			String date = row[DATE_COLUMN];
			String name = row[NAME_COLUMN];
			String label = row[LABEL_COLUMN];
			String text = row[TEXT_COLUMN];
			
			if(text.contains(query)) {
				StatusCSVImpl tweet = createTweet(date, name, label, text);
				result.add(tweet);
			}
		}
		return result;
	}

	public List<Status> getAllTweets() {
		List<Status> result = new ArrayList<Status>();
		for (String line : csvLines) {
			log.trace("Read line from csv: " + line);
			String[] row = line.split(CSV_DELIMITER);
			String date = row[DATE_COLUMN];
			String name = row[NAME_COLUMN];
			String label = row[LABEL_COLUMN];
			String text = row[TEXT_COLUMN];
			
			StatusCSVImpl tweet = createTweet(date, name, label, text);
			result.add(tweet);
		}
		return result;
	}
	
	private StatusCSVImpl createTweet(String date, String name, String label,
			String text) {
		StatusCSVImpl tweet = new StatusCSVImpl();
		tweet.setUser(name);
		tweet.setText(text);
		tweet.setLabel(label.equals("1") ? Classification.POSITIVE : Classification.NEGATIVE);
		//tweet.setDate(getParsedDate(date));
		return tweet;
	}
	
	private Date getParsedDate(String date) {
		try {
			return new SimpleDateFormat("\"yyyy-dd-MM H:m:s:S\"").parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
