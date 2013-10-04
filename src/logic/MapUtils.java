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
public class MapUtils
{
    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue( Map<K, V> map )
    {
        List<Map.Entry<K, V>> list =
            new LinkedList<Map.Entry<K, V>>( map.entrySet() );
        Collections.sort( list, new Comparator<Map.Entry<K, V>>()
        {
            public int compare( Map.Entry<K, V> o1, Map.Entry<K, V> o2 )
            {
                return (o1.getValue()).compareTo( o2.getValue() );
            }
        } );

        Map<K, V> result = new LinkedHashMap<K, V>();
        for (Map.Entry<K, V> entry : list)
        {
            result.put( entry.getKey(), entry.getValue() );
        }
        return result;
    }
    
    public static List<String> readAllLines(String filename){  
    	try {
	        Path file = Paths.get(filename);  
	        return Files.readAllLines(file, Charset.defaultCharset());  // with out loop we can read all the contents
    	}catch (InvalidPathException e) {
    		e.printStackTrace();
    		System.err.println("Reading-all-lines util couldn't find the given file.");
    	} catch (IOException e) {
    		e.printStackTrace();
    		System.err.println("Reading-all-lines util crashed with an IOException while reading " + filename + " file.");
    	}
		return null;
    }
}