package linearizer.pattern_matching;
import static org.junit.Assert.assertEquals;

import java.util.Map;
import java.util.Set;

import org.junit.Test;
import feat_extract.LogicalForm;
import feat_extract.WordFeatures;
import linearizer.arg_separate.ArgSeparator;
import linearizer.pattern_matching.SentTypeDeterminer.SentType;
import main.Exceptions;
import util.LFTestUtil;
import util.WFTestUtil;

public class TestArgSeparator {
	private ArgSeparator uut = new ArgSeparator();
	
	@Test
	public void testSVO() throws Exception {
		LogicalForm lf = LFTestUtil.getLF(3, 2);
		SentType sentType = SentTypeDeterminer.sentType(lf);
		if(sentType == null)
			throw new Exceptions.NoMoodException(lf.getSentence());
		Map<String,WordFeatures> verbAndArgs = uut.verbAndArgs(lf);
		Set<String> expKeySet = WFTestUtil.strSet("Mod", "Mod.1", "Arg1", "Arg0", "verb", "ElabRel");
		assertEquals(expKeySet, verbAndArgs.keySet());
		assertEquals("fiber", verbAndArgs.get("Arg0").getUniqueFeature("PN"));
		assertEquals("resilient", verbAndArgs.get("Arg1").getUniqueFeature("PN"));
		assertEquals("be", verbAndArgs.get(ArgSeparator.VERB_KEY).getUniqueFeature("PN").substring(0, 2));
	}
	@Test
	public void testCompoundVerbSVO() throws Exception {
		LogicalForm lf = LFTestUtil.getLF(3, 3);
		SentType sentType = SentTypeDeterminer.sentType(lf);
		if(sentType == null)
			throw new Exceptions.NoMoodException(lf.getSentence());
		Map<String,WordFeatures> verbAndArgs = uut.verbAndArgs(lf);
		Set<String> expKeySet = WFTestUtil.strSet("Mod", "Mod.1", "Arg1", "Arg0", "verb", "aux");
		assertEquals(expKeySet, verbAndArgs.keySet());
		assertEquals("Lorillard_Inc.", verbAndArgs.get("Arg0").getUniqueFeature("PN"));
		assertEquals("crocidolite", verbAndArgs.get("Arg1").getUniqueFeature("PN"));
		assertEquals("use", verbAndArgs.get(ArgSeparator.VERB_KEY).getUniqueFeature("PN").substring(0, 3));
		assertEquals("stop", verbAndArgs.get(ArgSeparator.AUX_VERB_KEY).getUniqueFeature("PN").substring(0, 4));
	}
}
