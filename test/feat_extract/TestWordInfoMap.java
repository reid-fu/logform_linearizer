package feat_extract;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

import main.Exceptions.InvalidWordIDException;
import main.Exceptions.NoPredicatesException;

public class TestWordInfoMap {
	@Test
	public void testNullConstruct() throws NoPredicatesException, InvalidWordIDException {
		WordInfoMap wordInfoMap = new WordInfoMap(null);
		wordInfoMap.addWordInfo("w0", new WordInfo("np", "PRP", "he"));
		assertEquals(wordInfoMap.getLemma("w0"), "he");
		assertEquals(wordInfoMap.getPOS("w0"), "PRP");
		assertEquals(wordInfoMap.getSupertag("w0"), "np");
	}
	@Test
	public void testNonnullConstruct() {
		
	}
	@Test(expected = InvalidWordIDException.class)
	public void testInvalidWordID() throws NoPredicatesException, InvalidWordIDException {
		WordInfoMap wordInfoMap = new WordInfoMap(null);
		wordInfoMap.getLemma("w0");
	}
}
