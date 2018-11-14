package feat_extract;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

import feat_extract.WordFeatures;

public class TestWordFeatures {
	@Test
	public void headStateTest() {
		assertEquals("[]", WordFeatures.HEAD.getChildRels().toString());
	}
	@Test
	public void usageTest() {
		WordFeatures headClone = (WordFeatures) WordFeatures.HEAD.clone();
		assertEquals(WordFeatures.HEAD, headClone);
	}
}
