package logic;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


/**
 * 
 * This class contains the all-around common utilities of the code.
 * Not very coherent and full of static methods. Anyway, its useful...
 * 
 * @author croo
 *
 */
public class Utils
{
    
    public static List<String> readAllLines(String filename, Charset charset){  
    	try {
	        Path file = Paths.get(filename);  
	        return Files.readAllLines(file,charset);  // with out loop we can read all the contents
    	}catch (InvalidPathException e) {
    		e.printStackTrace();
    		System.err.println("Reading-all-lines util couldn't find the given file with charset:" + Charset.defaultCharset().name());
    	} catch (IOException e) {
    		e.printStackTrace();
    		System.err.println("Reading-all-lines util crashed with an IOException while reading " + filename + " file.");
    	}
		return null;
    }
    
	public static Double getAverage(List<Double> numbers) {
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