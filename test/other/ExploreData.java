package other;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import feat_extract.LogicalForm;
import feat_extract.WordFeatures;
import util.LFTestUtil;

public class ExploreData {
	public static final String DEV_DIR = "/home/reid/projects/research/ccg/openccg/ccgbank/extract/test/00/";
	public static final String[] AUX_VERBS = {"PROG", "PASS", "PERF", "would", "should", "might", "will", "could", "can", "may", "must"};
	public static final String[] PUNCTUATION = {"&#45;&#45;", ",", ";", "quote&#45;rel"};
	public static final String[] CONJUNCTIONS = {"and", "but", "or"};
	public static final String[] WH_WORDS = {"what", "who", "why"};
	
	public static void main(String[] args) {
		try {
			rootTypeStats();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void rootTypeStats() throws Exception {
		List<LogicalForm> lfs = LFTestUtil.getLFBatch(1, 3);
		Map<String,Integer> rootTypes = new TreeMap<>();
		rootTypes.put("*VERB*", 0);
		rootTypes.put("*AUX*", 0);
		rootTypes.put("*PUNCT*", 0);
		rootTypes.put("*CONJ*", 0);
		rootTypes.put("*WH-WORD*", 0);
		rootTypes.put("*MULTI*", 0);
		
		for(LogicalForm lf : lfs) {
			List<WordFeatures> roots = lf.getHead().getChildList();
			if(roots.size() > 1) {
				rootTypes.put("*MULTI*", rootTypes.get("*MULTI*") + 1);
				continue;
			}
			WordFeatures root = roots.get(0);
			String rootWord = root.getUniqueFeature("PN");
			
			if(rootWord.matches("[a-z]+\\.[0-9]+") || rootWord.matches("[a-z]+\\.XX")) { // Verb with sense tag
				rootTypes.put("*VERB*", rootTypes.get("*VERB*") + 1);
			} else if(Arrays.asList(AUX_VERBS).contains(rootWord)) {
				rootTypes.put("*AUX*", rootTypes.get("*AUX*") + 1);
			} else if(Arrays.asList(PUNCTUATION).contains(rootWord)) {
				rootTypes.put("*PUNCT*", rootTypes.get("*PUNCT*") + 1);
			} else if(Arrays.asList(CONJUNCTIONS).contains(rootWord)) {
				rootTypes.put("*CONJ*", rootTypes.get("*CONJ*") + 1);
			} else if(Arrays.asList(WH_WORDS).contains(rootWord)) {
				rootTypes.put("*WH-WORD*", rootTypes.get("*WH-WORD*") + 1);
			} else if(rootTypes.containsKey(rootWord)) {
				rootTypes.put(rootWord, rootTypes.get(rootWord) + 1);
			} else {
				rootTypes.put(rootWord, 1);
			}
		}
		System.out.println(rootTypes);
	}
}
