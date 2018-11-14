package linearizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.assertEquals;
import feat_extract.LogicalForm;
import feat_extract.WordFeatures;
import linearizer.eng_heuristic.ChildOrderer;
import linearizer.eng_heuristic.ChildOrders;
import util.WFTestUtil;

public class EngLinUnitTest {
	private EngLinearizer uut;
	private LinConfig config;
	@Mock
	private LogicalForm lf;
	
	@Before
	public void setup() {
		uut = new EngLinearizer();
		config = new LinConfig(FeatOrders.ALL_REL, 5, false, ChildOrders.ENG);
	}
	@Test
	public void testAddBeforeNoCycle() {
		List<String> order = new ArrayList<>();
		order.add("w2");
		List<String> beforeParent = ChildOrderer.DEFAULT_CHILD_ORDER.subList(0, ChildOrderer.DEFAULT_CHILD_ORDER.indexOf(ChildOrderer.PARENT_REL));
		WordFeatures current = new WordFeatures();
		WFTestUtil.addChildren(current, new String[] {"Arg0","Mod","Arg1" + LogicalForm.REF_MARKER},
				new WordFeatures[] {WFTestUtil.wordFeaturesWithID("w0"),
						WFTestUtil.wordFeaturesWithID("w1"), WFTestUtil.wordFeaturesWithID("w3")});
		
		uut.addChildrenBeforeParent(lf, order, beforeParent, current, new HashSet<>(Arrays.asList(new String[] {"w2"})), config);
		assertEquals(Arrays.asList(new String[] {"w0","w1","w2"}), order);
		assertEquals(1, current.getChildren().size());
	}
	@Test
	public void testAddBeforeCycle() {
		List<String> order = new ArrayList<>();
		order.add("w2");
		List<String> beforeParent = ChildOrderer.DEFAULT_CHILD_ORDER.subList(0, ChildOrderer.DEFAULT_CHILD_ORDER.indexOf(ChildOrderer.PARENT_REL));
		WordFeatures current = WFTestUtil.wordFeaturesWithID("w2");
		WordFeatures arg0 = WFTestUtil.wordFeaturesWithID("w0");
		WFTestUtil.addChildren(arg0, new String[] {"Det" + LogicalForm.REF_MARKER}, new WordFeatures[] {current});
		WFTestUtil.addChildren(current, new String[] {"Arg0","Mod"},
				new WordFeatures[] {arg0, WFTestUtil.wordFeaturesWithID("w1")});
		
		uut.addChildrenBeforeParent(lf, order, beforeParent, current, new HashSet<>(Arrays.asList(new String[] {"w2"})), config);
		assertEquals(Arrays.asList(new String[] {"w0","w1","w2"}), order);
		assertEquals(0, current.getChildren().size());
	}
	@Test
	public void testAddAfterNoCycle() {
		List<String> order = new ArrayList<>();
		order.add("w1");
		List<String> afterParent = ChildOrderer.DEFAULT_CHILD_ORDER.subList(
				ChildOrderer.DEFAULT_CHILD_ORDER.indexOf(ChildOrderer.PARENT_REL) + 1, ChildOrderer.DEFAULT_CHILD_ORDER.size());
		WordFeatures current = new WordFeatures();
		WFTestUtil.addChildren(current, new String[] {"Arg1","Arg2","Arg1" + LogicalForm.REF_MARKER},
				new WordFeatures[] {WFTestUtil.wordFeaturesWithID("w2"),
						WFTestUtil.wordFeaturesWithID("w3"), WFTestUtil.wordFeaturesWithID("w4")});
		
		uut.addChildrenAfterParent(lf, order, afterParent, current, new HashSet<>(Arrays.asList(new String[] {"w1"})), config);
		assertEquals(Arrays.asList(new String[] {"w1","w2","w3"}), order);
		assertEquals(1, current.getChildren().size());
	}
	@Test
	public void testAddAfterCycle() {
		List<String> order = new ArrayList<>();
		order.add("w1");
		List<String> afterParent = ChildOrderer.DEFAULT_CHILD_ORDER.subList(
				ChildOrderer.DEFAULT_CHILD_ORDER.indexOf(ChildOrderer.PARENT_REL) + 1, ChildOrderer.DEFAULT_CHILD_ORDER.size());
		WordFeatures current = WFTestUtil.wordFeaturesWithID("w1");
		WordFeatures arg2 = WFTestUtil.wordFeaturesWithID("w3");
		WFTestUtil.addChildren(arg2, new String[] {"Arg0" + LogicalForm.REF_MARKER}, new WordFeatures[] {current});
		WFTestUtil.addChildren(current, new String[] {"Arg1","Arg2"},
				new WordFeatures[] {WFTestUtil.wordFeaturesWithID("w2"), arg2});
		
		uut.addChildrenAfterParent(lf, order, afterParent, current, new HashSet<>(Arrays.asList(new String[] {"w1"})), config);
		assertEquals(Arrays.asList(new String[] {"w1","w2","w3"}), order);
		assertEquals(0, current.getChildren().size());
	}
}
