package linearizer.pattern_matching;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import feat_extract.WordFeatures;

public class WFPatternUtil {
	/** Lemmas used to explicitly mark certain types of auxiliary verbs:<br/>
	 * PASS marks auxiliary form of "be" used in passive voice<br/>
	 * PERF marks auxiliary form of "have" used in perfect tense<br/>
	 * PROG marks auxiliary form of "be" used in progressive tense */
	public static Set<String> MARKED_AUX = new HashSet<>(Arrays.asList("PASS","PERF","PROG"));
	
	/** @return True iff word's lemma is in MARKED_AUX, or word has verb as "Arg1" child */
	public static boolean isAuxVerb(WordFeatures word) {
		String lemma = word.getUniqueFeature("PN");
		if(MARKED_AUX.contains(lemma))
			return true;
		
		List<WordFeatures> arg1 = word.getChildren().get("Arg1");
		if(arg1 == null)
			return false;
		
		for(WordFeatures arg : arg1) {
			String argLemma = arg.getUniqueFeature("PN");
			if(argLemma != null && argLemma.matches("[a-z]+\\.[0-9]+"))
				return true;
		}
		return false;
	}
	/** @return Main verb, given auxiliary verb */
	public static WordFeatures mainVerb(WordFeatures aux) {
		List<WordFeatures> arg1 = aux.getChildren().get("Arg1");
		if(arg1 == null)
			return null;
		
		for(WordFeatures arg : arg1) {
			String argLemma = arg.getUniqueFeature("PN");
			if(argLemma != null && argLemma.matches("[a-z]+\\.[0-9]+"))
				return arg;
		}
		return null;
	}
}
