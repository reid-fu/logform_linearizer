package util;
import feat_extract.WordFeatures;

public class WordFeatUtil {
	public static WordFeatures wordFeaturesWithID(String id) {
		WordFeatures wordFeats = new WordFeatures();
		wordFeats.addFeature("id", id);
		return wordFeats;
	}
	public static void addFeatures(WordFeatures wordFeats, String...feats) {
		assert feats.length % 2 == 0;
		for(int i = 0; i < feats.length; i+=2) {
			wordFeats.addFeature(feats[i], feats[i+1]);
		}
	}
	public static void addParents(WordFeatures child, String rels[], WordFeatures parents[]) {
		assert rels.length == parents.length;
		for(int i = 0; i < rels.length; i++) {
			child.addParent(rels[i], parents[i]);
		}
	}
	public static void addChildren(WordFeatures parent, String rels[], WordFeatures children[]) {
		assert rels.length == children.length;
		for(int i = 0; i < rels.length; i++) {
			parent.addChild(rels[i], children[i]);
		}
	}
}
