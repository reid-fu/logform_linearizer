package feat_extract;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import util.WFTestUtil;

public class TestWFUtil {
	//Method getChildren
	@Test
	public void testGetChildrenNull() {
		WordFeatures word = WFTestUtil.wordFeaturesWithID("w0");
		List<WordFeatures> children = WFUtil.getChildren(word, "Arg0", true);
		assertEquals(null, children);
	}
	@Test
	public void testGetChildrenNoRefs() {
		WordFeatures parent = WFTestUtil.wordFeaturesWithID("w0");
		WordFeatures refChild = WFTestUtil.wordFeaturesWithID("w1");
		WordFeatures norefChild = WFTestUtil.wordFeaturesWithID("w2");
		WFTestUtil.addChildren(parent, new String[] {"Arg" + LogicalForm.REF_MARKER, "Arg"}, new WordFeatures[] {refChild, norefChild});
		
		List<WordFeatures> children = WFUtil.getChildren(parent, "Arg", false);
		assertEquals(Arrays.asList(new WordFeatures[] {norefChild}), children);
	}
	@Test
	public void testGetChildrenRefs() {
		WordFeatures parent = WFTestUtil.wordFeaturesWithID("w0");
		WordFeatures refChild = WFTestUtil.wordFeaturesWithID("w1");
		WordFeatures norefChild = WFTestUtil.wordFeaturesWithID("w2");
		WFTestUtil.addChildren(parent, new String[] {"Arg" + LogicalForm.REF_MARKER, "Arg0"}, new WordFeatures[] {refChild, norefChild});
		
		List<WordFeatures> children = WFUtil.getChildren(parent, "Arg", true);
		assertEquals(Arrays.asList(new WordFeatures[] {refChild}), children);
	}
	
	//Method getChildRels
	@Test
	public void testGetChildRelsNoRefs() {
		WordFeatures parent = WFTestUtil.wordFeaturesWithID("w0");
		WordFeatures refChild = WFTestUtil.wordFeaturesWithID("w1");
		WordFeatures norefChild = WFTestUtil.wordFeaturesWithID("w2");
		WFTestUtil.addChildren(parent, new String[] {"Arg" + LogicalForm.REF_MARKER, "Arg1"}, new WordFeatures[] {refChild, norefChild});
		
		Set<String> childRels = WFUtil.getChildRels(parent, false);
		assertEquals(WFTestUtil.strSet("Arg1"), childRels);
	}
	@Test
	public void testGetChildRelsRefs() {
		WordFeatures parent = WFTestUtil.wordFeaturesWithID("w0");
		WordFeatures refChild = WFTestUtil.wordFeaturesWithID("w1");
		WordFeatures norefChild = WFTestUtil.wordFeaturesWithID("w2");
		WFTestUtil.addChildren(parent, new String[] {"Arg" + LogicalForm.REF_MARKER, "Arg1"}, new WordFeatures[] {refChild, norefChild});
		
		Set<String> childRels = WFUtil.getChildRels(parent, true);
		assertEquals(WFTestUtil.strSet("Arg" + LogicalForm.REF_MARKER, "Arg1"), childRels);
	}
	
	//Method getChildList
	@Test
	public void testGetChildListNoRefs() {
		WordFeatures parent = WFTestUtil.wordFeaturesWithID("w0");
		WordFeatures refChild = WFTestUtil.wordFeaturesWithID("w1");
		WordFeatures norefChild = WFTestUtil.wordFeaturesWithID("w2");
		WFTestUtil.addChildren(parent, new String[] {"Arg" + LogicalForm.REF_MARKER, "Arg1"}, new WordFeatures[] {refChild, norefChild});
		
		List<WordFeatures> children = WFUtil.getChildList(parent, false);
		assertEquals(Arrays.asList(new WordFeatures[] {norefChild}), children);
	}
	@Test
	public void testGetChildListRefs() {
		WordFeatures parent = WFTestUtil.wordFeaturesWithID("w0");
		WordFeatures refChild = WFTestUtil.wordFeaturesWithID("w1");
		WordFeatures norefChild = WFTestUtil.wordFeaturesWithID("w2");
		WFTestUtil.addChildren(parent, new String[] {"Arg" + LogicalForm.REF_MARKER, "Arg1"}, new WordFeatures[] {refChild, norefChild});
		
		List<WordFeatures> children = WFUtil.getChildList(parent, true);
		//Note: Order of children doesn't matter, but test may fail when method works correctly if order is not expected order
		assertEquals(Arrays.asList(new WordFeatures[] {norefChild, refChild}), children);
	}
}
