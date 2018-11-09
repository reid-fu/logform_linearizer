package linearizer.eng_heuristic;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import feat_extract.WordFeatures;

public class CompChildOrderer extends ChildOrderer {
	public static final String COMP_ARG = "Arg0";
	public static Set<String> COMPLEMENTIZERS = new HashSet<>(
			Arrays.asList("whether"));
	
	public static boolean isComp(WordFeatures current) {
		String word = current.getUniqueFeature("PN");
		return COMPLEMENTIZERS.contains(word);
	}
	public List<String> relOrder(WordFeatures current) {
		List<String> relOrder = new ArrayList<>(2);
		relOrder.add(ChildOrderer.PARENT_REL);
		relOrder.add(COMP_ARG);
		return relOrder;
	}
}
