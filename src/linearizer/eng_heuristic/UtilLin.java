package linearizer.eng_heuristic;
import java.util.ArrayList;
import java.util.List;
import feat_extract.LogicalForm;
import feat_extract.WordFeatures;

public class UtilLin {
	/** @param current WordFeatures object for current word
	 * @param rel Relation name by which to filter returned child words. Should not be null.
	 * @param size_range Size range by which to filter returned child words. If not null, should match regex "[0-9]+-[0-9]+".
	 * @return List of child words with specified relation name and within specified size range. Empty list if no child words match criteria.
	 */
	public static List<WordFeatures> childrenWithinSizeRange(WordFeatures current, String rel, String size_range) {
		List<WordFeatures> children = current.getChildren().get(rel);
		if(children == null)
			return new ArrayList<>();
		if(size_range == null)
			return children;
		
		int lowBound = Integer.parseInt(size_range.substring(0, size_range.indexOf("-")));
		int highBound = Integer.parseInt(size_range.substring(size_range.indexOf("-") + 1));
		List<WordFeatures> children2 = new ArrayList<>(children);
		children2.removeIf(feats -> feats.getSubtreeSize() < lowBound || feats.getSubtreeSize() > highBound);
		return children2;
	}
	/** Removes child words with specified relation name and within specified size range
	 * @param current WordFeatures object for current word
	 * @param rel Relation name by which to filter returned child words. Should not be null.
	 * @param size_range Size range by which to filter returned child words. If not null, should match regex "[0-9]+-[0-9]+".
	 */
	public static void removeChildrenWithinSizeRange(WordFeatures current, String rel, String size_range) {
		List<WordFeatures> children = current.getChildren().get(rel);
		if(children == null)
			return;
		
		if(size_range == null) {
			current.getChildren().remove(rel);
		} else {
			int lowBound = Integer.parseInt(size_range.substring(0, size_range.indexOf("-")));
			int highBound = Integer.parseInt(size_range.substring(size_range.indexOf("-") + 1));
			children.removeIf(feats -> feats.getSubtreeSize() >= lowBound && feats.getSubtreeSize() <= highBound);
		}
	}
	/** @param lf Logical form
	 * @param wordIDs ID's used to look up words in logical form
	 * @return Sum of number of characters in words represented in wordIDs
	 */
	public static int getTotalPredicateLength(LogicalForm lf, List<String> wordIDs) {
		int len = 0;
		for(String wordID : wordIDs) {
			if(wordID.equals("(") || wordID.equals(")"))
				continue;
			WordFeatures feats = lf.getWordFeatures(wordID);
			String pn = feats.getUniqueFeature("PN");
			if(pn != null)
				len += pn.length();
		}
		return len;
	}
}
