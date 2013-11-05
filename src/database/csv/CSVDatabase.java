package database.csv;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import logic.SimpleLinearAnalyzer;
import logic.Utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import twitter4j.Status;
import database.Database;

public class CSVDatabase implements Database {

	private Logger log = LoggerFactory.getLogger(SimpleLinearAnalyzer.class);
	
	private String fileName;
	private List<String> csvLines;

	
	public CSVDatabase(String fileName) {
		this.fileName = fileName;
		csvLines = Utils.readAllLines(fileName);
	}

	@Override
	public List<Status> getTweets(String query) {
		List<Status> result = new ArrayList<Status>();
		for (String line : csvLines) {
			log.trace("Read line from csv: " + line);
			
			String[] row = line.split("\t");
			String date = row[0];
			String name = row[1];
			String text = row[3];
			StatusCSVImpl tweet = new StatusCSVImpl();
			tweet.setUser(name);
			tweet.setText(text);
			tweet.setDate(getParsedDate(date));
			result.add(tweet);
		}
		
		return result;
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

	@Override
	public List<String> getUsers(String namePrefix) {
		// TODO Auto-generated method stub
		return null;
	}
}
