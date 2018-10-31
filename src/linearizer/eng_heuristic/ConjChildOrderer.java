package linearizer.eng_heuristic;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import feat_extract.WordFeatures;

public class ConjChildOrderer extends ChildOrderer {
	public static final String CONJ_ARG0 = "First";
	public static final String CONJ_ARG1 = "Next";
	public static Set<String> CONJUNCTIONS = new HashSet<>(
			Arrays.asList("and"));
	
	public static boolean isConj(WordFeatures current) {
		String word = current.getUniqueFeature("PN");
		return CONJUNCTIONS.contains(word);
	}
	public List<String> relOrder(WordFeatures current) {
		List<String> relOrder = new ArrayList<>(3);
		relOrder.add(CONJ_ARG0);
		relOrder.add(ChildOrderer.PARENT_REL);
		relOrder.add(CONJ_ARG1);
		return relOrder;
	}
}
