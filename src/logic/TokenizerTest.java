package logic;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class TokenizerTest {

	@Test
	public void testOneWord() {
		List<Word> expected = new ArrayList<Word>();
		expected.add(new Word("sad"));
		List<Word> actual = Tokenizer.getTokens("sad");
		assertEquals(expected,actual);
	}
	
	@Test
	public void testStopWordsShouldGoAway() {
		List<Word> expected = new ArrayList<Word>();
		expected.add(new Word("sad"));
		List<Word> actual = Tokenizer.getTokens("i am sad");
		assertEquals(expected,actual);
	}

}
