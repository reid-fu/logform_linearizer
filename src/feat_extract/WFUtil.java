package feat_extract;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class WFUtil {
	/** @return List of child words related to parent word through relation rel. Null if no such children. */
	public static List<WordFeatures> getChildren(WordFeatures word, String rel, boolean includeRefs) {
		List<WordFeatures> nonrefs = word.getChildren().get(rel);		
		List<WordFeatures> list = (nonrefs == null) ? new ArrayList<>() : new ArrayList<>(nonrefs);
		
		if(includeRefs) {
			List<WordFeatures> refs = word.getChildren().get(rel + LogicalForm.REF_MARKER);
			if(refs != null)
				list.addAll(refs);
		}
		if(list.size() == 0)
			return null;
		return list;
	}
	/** @param word Node representing parent word
	 * @param includeRefs Whether to include relations with REF_MARKER
	 * @return List of relations between parent word and child words
	 */
	public static Set<String> getChildRels(WordFeatures word, boolean includeRefs) {
		if(includeRefs) {
			return word.getChildRels();
		} else {
			return word.getChildRelsNoRefs();
		}
	}
	/** @return List of children not backed up by word's children map */
	public static List<WordFeatures> getChildList(WordFeatures word, boolean includeRefs) {
		List<WordFeatures> childList = new ArrayList<>();
		Set<String> rels = getChildRels(word, includeRefs);
		
		for(String rel : rels) {
			childList.addAll(word.getChildren().get(rel));
		}
		return childList;
	}
	/** @return String representation of values that word has for feature */
	public static String getFeatString(WordFeatures word, String feature) {
		List<String> vals = word.getFeature(feature);
		if(vals == null) {
			return null;
		}
		String featStr = vals.toString();
		return featStr.substring(1, featStr.length()-1);
	}
	/** Removes relation and associated children from word */
	public static void removeChildren(WordFeatures word, String rel, boolean includeRefs) {
		if(word.getChildren().containsKey(rel)) {
			word.getChildren().remove(rel);
			word.getChildRels().remove(rel);
			word.getChildRelsNoRefs().remove(rel);
		}
		if(includeRefs && word.getChildren().containsKey(rel + LogicalForm.REF_MARKER)) {
			word.getChildren().remove(rel + LogicalForm.REF_MARKER);
			word.getChildRels().remove(rel + LogicalForm.REF_MARKER);
		}
	}
}
