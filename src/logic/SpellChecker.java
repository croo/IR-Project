package logic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SpellChecker {
	
	private static Logger log = LoggerFactory.getLogger(SpellChecker.class);
	
	private static pt.tumba.spell.SpellChecker spellChecker;
	
	private static void initSpellChecker() {
		System.out.println("Initializing JaSpell checker...");
		spellChecker = new pt.tumba.spell.SpellChecker();
		String dictionary = "english_dict/english.txt";
		String commonMisspells = "english_dict/common-misspells.txt";
		try {
			spellChecker.initialize(dictionary, commonMisspells);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Initialization of the spellchecker failed with the following parameters:\n {}, {}, {}",dictionary,commonMisspells);
		}
		System.out.println("Done.");
	}
	
	public static pt.tumba.spell.SpellChecker getInstance() {
		if(spellChecker == null) {
			initSpellChecker();
		}
		return spellChecker;
	}
}
