package main;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.List;

import logic.SpellChecker;
import logic.Tweet;
import logic.naivebayes.NaiveBayesAnalyzer;
import logic.simplelinear.SimpleLinearAnalyzer;
import twitter4j.Status;
import database.Database;
import database.csv.CSVDatabase;
import database.csv.StatusCSVImpl;
import database.sentimental.BoostWords;
import database.sentimental.Emoticons;
import database.sentimental.SentiWordNet;

public class CommandLineTool {

	
	 public static void main(String[] args) {
		 
		 	System.setProperty(org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "error");
		 	System.setProperty(org.slf4j.impl.SimpleLogger.LOG_FILE_KEY, "System.out");
		 	System.setProperty(org.slf4j.impl.SimpleLogger.SHOW_SHORT_LOG_NAME_KEY, "true");
		 	
		 	//Database database = new CSVDatabase("training_data/merged_tabbed_training_data.csv");
		 	Database database = new CSVDatabase("training_data/testing_data_01.csv");
		 	List<Status> tweets = database.getTweets("");
		 	
		 	SentiWordNet sentiWordNet = SentiWordNet.getInstance();
		 	Emoticons emoticons = Emoticons.getInstance();
		 	BoostWords boostWords = BoostWords.getInstance();
		 	SpellChecker spellChecker = SpellChecker.getInstance();
		 	
		 	SimpleLinearAnalyzer slnAnalyzer = new SimpleLinearAnalyzer(sentiWordNet,emoticons,boostWords,spellChecker);
		 	NaiveBayesAnalyzer nbAnalyzer = new NaiveBayesAnalyzer();
		 	
		 	BufferedWriter writer = null;

			try {
				writer = new BufferedWriter(new OutputStreamWriter(
				        new FileOutputStream("first.txt", true), "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				System.err.println("Output file not found.(what the hell, i just created it. This is not possible.)");
				e.printStackTrace();
			}
			//SimpleDateFormat formatter = new SimpleDateFormat("\"yyyy-dd-MM H:m:s:S\"");
			int nbGood = 0;
			int nbBad = 0;
			int slnGood = 0;
			int slnBad = 0;
			int agrees = 0;
			int disagrees = 0;
			
			int bothGood = 0;
			int oneGood = 0;
			int bothWrong = 0;
		 	for (Status tweet : tweets) {
		 		Tweet slnTweet = slnAnalyzer.getAnalyzedTweet(tweet);
		 		Tweet nbTweet = nbAnalyzer.getAnalyzedTweet(tweet);
		 		
		 		if(((StatusCSVImpl)nbTweet.getRawTweet()).getLabel() == nbTweet.getClassification()) {
		 			nbGood++;
		 		} else {
		 			nbBad++;
		 		}
		 		
		 		if(((StatusCSVImpl)slnTweet.getRawTweet()).getLabel() == slnTweet.getClassification()) {
		 			slnGood++;
		 		} else {
		 			slnBad++;
		 		}
		 		
		 		if(nbTweet.getClassification() == slnTweet.getClassification()) {
		 			agrees++;
		 		} else {
		 			disagrees++;
		 		}
		 		
		 		if (((StatusCSVImpl)nbTweet.getRawTweet()).getLabel() == nbTweet.getClassification() && ((StatusCSVImpl)slnTweet.getRawTweet()).getLabel() == slnTweet.getClassification()) {
		 			bothGood++;
		 		} else if ((((StatusCSVImpl)nbTweet.getRawTweet()).getLabel() == nbTweet.getClassification()) != (((StatusCSVImpl)slnTweet.getRawTweet()).getLabel() == slnTweet.getClassification())) {
		 			oneGood++;
		 		} else {
		 			bothWrong++;
		 		}
//		 		try {
//					writer.append(formatter.format(t.getRawTweet().getCreatedAt()) + 
//							"\t" + t.getRawTweet().getUser().getName() + 
//							"\t" + "\""+t.getBayesianPositiveWeight() +"\"" + 
//							"\t" +"\""+ t.getBayesianNegativeWeight() +"\""+
//							"\t" +"\""+ ((t.getBayesianPositiveWeight() > t.getBayesianNegativeWeight()) ? "1":"0")+"\"");
//		 			writer.append(((slnTweet.getClassification() == Classification.POSITIVE) ? "1" :"0") + "\t" +
//		 					  ((nbTweet.getClassification() == Classification.POSITIVE) ? "1" : "0") + "\t" +
//		 						((StatusCSVImpl)nbTweet.getRawTweet()).getLabel() + "\t" +tweet.getText());
//					writer.newLine();
					/*System.out.println(((slnTweet.getClassification() == Classification.POSITIVE) ? "1" :"0") + "\t" +
		 					  ((nbTweet.getClassification() == Classification.POSITIVE) ? "1" : "0") + "\t" +
		 						((StatusCSVImpl)nbTweet.getRawTweet()).getLabel() + "\t" +tweet.getText());*/
//					System.out.println("+:" + nbTweet.getNaiveBayesPositiveProbability() + "\t-:" + nbTweet.getNaiveBayesNegativeProbability() + "\t" + nbTweet.getClassification() + "\t" +
//		 						((StatusCSVImpl)nbTweet.getRawTweet()).getLabel() + "\t" +tweet.getText());
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
		 	}	
		 	System.out.println("Naive bayes on test data:");
		 	System.out.println((new Double(nbGood) / new Double(nbGood + nbBad))*100.0 + "% good");
		 	System.out.println((new Double(nbBad) / new Double(nbGood + nbBad))*100.0 + "% bad");
		 	System.out.println();
		 	
		 	System.out.println("Simple Linear on test data:");
		 	System.out.println((new Double(slnGood) / new Double(slnGood + slnBad))*100.0 + "% good");
		 	System.out.println((new Double(slnBad) / new Double(slnGood + slnBad))*100.0 + "% bad");
		 	System.out.println();
		 	
		 	System.out.println("How much of the tweets they are agree on:");
		 	System.out.println((new Double(agrees) / new Double(agrees + disagrees))*100.0 + "% agree");
		 	System.out.println((new Double(disagrees) / new Double(agrees + disagrees))*100.0 + "% disagree");
		 	System.out.println();
		 	
		 	System.out.println("Combined they are:");
		 	System.out.println((new Double(bothGood) / new Double(bothGood + oneGood + bothWrong))*100.0 + "% both give good answer (OK for co-training)");
		 	System.out.println((new Double(oneGood) / new Double(bothGood + oneGood + bothWrong))*100.0 + "% one of them gives good answer(we should show this to the user)");
		 	System.out.println((new Double(bothWrong) / new Double(bothGood + oneGood + bothWrong))*100.0 + "% none of them gives good answer (completely wrong)");
		 	
	    }
}
