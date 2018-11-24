package linearizer.pattern_matching;
import java.util.Map;

import feat_extract.WordFeatures;

public class LabelPatternUtil {
	public static WordFeatures getMinArg(Map<String, WordFeatures> verbAndArgs) {
		int minArgNum = 9;
		WordFeatures minArg = null;
		String matchedRel = null;
		
		for(String rel : verbAndArgs.keySet()) {
			if(rel.matches("Arg[0-9][a-z]?") && rel.charAt(3) - '0' < minArgNum) {
				minArgNum = rel.charAt(3) - '0';
				minArg = verbAndArgs.get(rel);
				matchedRel = rel;
			}
		}
		
		if(matchedRel != null)
			verbAndArgs.remove(matchedRel); //Remove so that other args can't be matched to chosen subtree
		return minArg;
	}
}
