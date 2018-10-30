package linearizer.eng_heuristic;
import java.util.List;

import feat_extract.WordFeatures;

public class VerbChildOrderer extends ChildOrderer {
	public static boolean isVerb(WordFeatures current) {
		return current.hasFeature("mood") || current.hasFeature("tense") || current.hasFeature("partic");
	}
	public List<String> relOrder(WordFeatures current) {
		List<String> relOrder = super.relOrder(current);
		if(UtilLin.childrenWithinSizeRange(current, "Mod", "3-100").size() > 0)
			relOrder.add("Mod:3-100");
		return relOrder;
	}
}
