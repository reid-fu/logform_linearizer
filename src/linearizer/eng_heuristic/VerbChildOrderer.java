package linearizer.eng_heuristic;
import java.util.List;

import feat_extract.WordFeatures;

public class VerbChildOrderer extends ChildOrderer {
	public static boolean isVerb(WordFeatures current) {
		boolean normalVerb = current.hasFeature("mood") || current.hasFeature("tense") || current.hasFeature("partic");
		// PASS represents auxiliary verb that's form of "be", e.g. was said
		// PERF represents auxiliary verb that's form of "have", e.g. has said
		boolean auxBeHave = current.hasFeature("PN") 
				&& (current.getUniqueFeature("PN").equals("PASS") 
						|| current.getUniqueFeature("PN").equals("PERF")
						|| current.getUniqueFeature("PN").equals("PROG"));
		return normalVerb || auxBeHave;
	}
	public List<String> relOrder(WordFeatures current) {
		List<String> relOrder = super.relOrder(current);
		if(UtilLin.childrenWithinSizeRange(current, "Mod", "3-100").size() > 0)
			relOrder.add("Mod:3-100");
		return relOrder;
	}
}
