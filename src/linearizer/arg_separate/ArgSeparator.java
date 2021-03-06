package linearizer.arg_separate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import feat_extract.LogicalForm;
import feat_extract.WFUtil;
import feat_extract.WordFeatures;
import linearizer.pattern_matching.WFPatternUtil;
import main.Exceptions.NoMoodException;

public class ArgSeparator {
	public static Set<String> WH_WORDS = new HashSet<>(Arrays.asList("who","what","where","when","how","which"));
	public static final String VERB_KEY = "verb";
	public static final String AUX_VERB_KEY = "aux";
	
	/** @param lf LogicalForm representing sentence
	 * @return Verb and verb arguments in sentence
	 * @throws NoMoodException If root of lf has no "mood" attribute
	 */
	public Map<String,WordFeatures> verbAndArgs(LogicalForm lf) {
		Map<String,WordFeatures> verbAndArgs = new HashMap<>();
		WordFeatures root = WFUtil.getChildList(lf.getHead(), true).get(0);
		
		boolean auxVerb = WFPatternUtil.isAuxVerb(root);
		if(auxVerb) {
			verbAndArgs.put(AUX_VERB_KEY, root);
		} else {
			verbAndArgs.put(VERB_KEY, root);
		}
		
		addVerbArguments(verbAndArgs, root);
		if(auxVerb) {
			WordFeatures mainVerb = WFPatternUtil.mainVerb(root);
			verbAndArgs.values().remove(mainVerb);
			verbAndArgs.put(VERB_KEY, mainVerb);
			addVerbArguments(verbAndArgs, mainVerb);
		}
		return verbAndArgs;
	}
	/** Adds child subtrees of verb to verbAndArgs */
	private void addVerbArguments(Map<String,WordFeatures> verbAndArgs, WordFeatures verb) {
		for(String rel : WFUtil.getChildRels(verb, false)) {
			int childCounter = 1;
			for(WordFeatures child : WFUtil.getChildren(verb, rel, false)) {
				if(verbAndArgs.containsKey(rel)) {
					while(verbAndArgs.containsKey(rel + "." + childCounter))
						childCounter++;
					verbAndArgs.put(rel + "." + childCounter, child);
				} else {
					verbAndArgs.put(rel, child);
				}
			}
		}
	}
}
