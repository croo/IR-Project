package database.csv;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import database.Database;
import twitter4j.Status;
import au.com.bytecode.opencsv.CSVReader;

public class CSVDatabase implements Database {

	private static final String CSV_DIRECTORY = "csv_data/";
	
	private CSVReader reader;

	public CSVDatabase(String fileName) {
		try {
			reader = new CSVReader(new FileReader(CSV_DIRECTORY + fileName));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.err.println("\n I could not find the test database file.\n");
		}
	}

	@Override
	public List<Status> getTweets(String query) {
		List<Status> tweets = new ArrayList<Status>();
		String[] nextLine;
		try {
			while ((nextLine = reader.readNext()) != null) {
				StatusCSVImpl tweet = new StatusCSVImpl();
				
				String dateString = nextLine[0];
				String name = nextLine[1];
				String text = nextLine[2];
				
				if(name.contains(query) || text.contains(query)){
					Date date = getDateFromString(dateString);
					tweet.setDate(date);
					tweet.setUser(name);
					tweet.setText(text);
					tweets.add(tweet);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Something bad happened during the reading of the database csv file.");
		}
		return tweets;
	}

	public Date getDateFromString(String dateString) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM d HH:mm:ss z yyyy");
			return sdf.parse(dateString);
		} catch (ParseException e) {
			e.printStackTrace();
			throw new RuntimeException(
					"CSVDatabase date parsing just gone crazy.");
		}
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		reader.close();
	}
}
