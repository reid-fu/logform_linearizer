package feat_extract;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class WFUtil {
	/** @return List of child words related to parent word through relation rel. Null if no such children. */
	public static List<WordFeatures> getChildren(WordFeatures word, String rel, boolean includeRefs) {
		List<WordFeatures> nonrefs = word.getChildren().get(rel);
		if(nonrefs == null)
			return null;
		
		List<WordFeatures> list = new ArrayList<>(nonrefs);
		List<WordFeatures> refs = word.getChildren().get(rel + LogicalForm.REF_MARKER);
		
		if(includeRefs && refs != null)
			list.addAll(refs);
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
}
