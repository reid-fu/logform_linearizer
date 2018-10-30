package linearizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import feat_extract.LogicalForm;
import feat_extract.WordFeatures;
import linearizer.eng_heuristic.ChildOrderer;
import linearizer.eng_heuristic.ChildOrders;
import util.WordFeatUtil;

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
		WordFeatUtil.addChildren(current, new String[] {"Arg0","Mod"},
				new WordFeatures[] {WordFeatUtil.wordFeaturesWithID("w0"),
						WordFeatUtil.wordFeaturesWithID("w1")});
		
		uut.addChildrenBeforeParent(lf, order, beforeParent, current, new HashSet<>(Arrays.asList(new String[] {"w2"})), config);
		assertEquals(Arrays.asList(new String[] {"w0","w1","w2"}), order);
	}
	@Test
	public void testAddBeforeCycle() {
		List<String> order = new ArrayList<>();
		order.add("w2");
		List<String> beforeParent = ChildOrderer.DEFAULT_CHILD_ORDER.subList(0, ChildOrderer.DEFAULT_CHILD_ORDER.indexOf(ChildOrderer.PARENT_REL));
		WordFeatures current = WordFeatUtil.wordFeaturesWithID("w2");
		WordFeatures arg0 = WordFeatUtil.wordFeaturesWithID("w0");
		WordFeatUtil.addChildren(arg0, new String[] {"Det"}, new WordFeatures[] {current});
		WordFeatUtil.addChildren(current, new String[] {"Arg0","Mod"},
				new WordFeatures[] {arg0, WordFeatUtil.wordFeaturesWithID("w1")});
		
		uut.addChildrenBeforeParent(lf, order, beforeParent, current, new HashSet<>(Arrays.asList(new String[] {"w2"})), config);
		assertEquals(Arrays.asList(new String[] {"w0","w1","w2"}), order);
	}
	@Test
	public void testAddAfterNoCycle() {
		
	}
}
