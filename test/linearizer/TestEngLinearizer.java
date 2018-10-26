package linearizer;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import feat_extract.LogicalForm;
import feat_extract.WordFeatures;

public class TestEngLinearizer {
	@SuppressWarnings("unused")
	private EngLinearizer uut;
	@Mock
	private LogicalForm lf;
	
	// HELPER METHODS
	public void addFeatures(WordFeatures wordFeats, String...feats) {
		assert feats.length % 2 == 0;
		for(int i = 0; i < feats.length; i+=2) {
			wordFeats.addFeature(feats[i], feats[i+1]);
		}
	}
	public void addParents(WordFeatures child, String rels[], WordFeatures parents[]) {
		assert rels.length == parents.length;
		for(int i = 0; i < rels.length; i++) {
			child.addParent(rels[i], parents[i]);
		}
	}
	public void addChildren(WordFeatures parent, String rels[], WordFeatures children[]) {
		assert rels.length == children.length;
		for(int i = 0; i < rels.length; i++) {
			parent.addChild(rels[i], children[i]);
		}
	}
	@Before
	public void setup() {
		uut = new EngLinearizer();
	}
	
	// TEST ADD CHILD ORDER CHILDREN
	@SuppressWarnings("unused")
	@Test
	public void testGetRemainingChildren() {
		String[] wordIDs = {"x0","x1"};
	}
}
