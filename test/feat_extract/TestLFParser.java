package feat_extract;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import java.util.ArrayList;
import java.util.List;
import org.jdom2.Element;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import util.LFTestUtil;

@RunWith(MockitoJUnitRunner.class)
public class TestLFParser {
	private LogicalFormParser uut = new LogicalFormParser();
	@Mock
	private Element lf;
	@Mock
	private WordInfoMap wordInfoMap;
	
	//Method parse
	@Test
	public void testParse1p1() throws Exception {
		LogicalForm lf = LFTestUtil.getLF(1, 1);
		WordFeatures root = lf.getHead();
		assertEquals(1, root.getChildRels().size());
		assertEquals(1, root.getChildRelsNoRefs().size());
		
		assertEquals(1, root.getChildList().size());
		WordFeatures aux = root.getChildList().get(0);
		assertEquals("will", aux.getUniqueFeature("PN"));
		
		assertEquals(2, aux.getChildList().size());
		WordFeatures subj = WFUtil.getChildren(aux, "Arg0", false).get(0);
		assertEquals("Pierre_Vinken", subj.getUniqueFeature("PN"));
		WordFeatures desc = WFUtil.getChildren(subj, "ApposRel", false).get(0);
		assertEquals("old", desc.getUniqueFeature("PN"));
		assertEquals("[Arg1, Arg0" + LogicalForm.REF_MARKER + "]", WFUtil.getChildRels(desc, true).toString());
		
		WordFeatures verb = WFUtil.getChildren(aux, "Arg1", false).get(0);
		assertEquals("join.01", verb.getUniqueFeature("PN"));
		assertEquals("[Mod, Arg1, Arg0" + LogicalForm.REF_MARKER + "]", WFUtil.getChildRels(verb, true).toString());
		assertEquals(2, WFUtil.getChildren(verb, "Mod", false).size());
		assertEquals("board", WFUtil.getChildren(verb, "Arg1", false).get(0).getUniqueFeature("PN"));
	}
	
	//Method addChildrenToQueue
	@Test
	public void testAddChildrenToQueueLFSingleRoot() {
		List<Relation> queue = new ArrayList<>();
		List<Element> rootWords = new ArrayList<>();
		rootWords.add(mock(Element.class));
		when(lf.getChildren("node")).thenReturn(rootWords);
		when(lf.getName()).thenReturn("lf");
		WordFeatures parentFeats = new LogicalForm("test").getHead();
		
		uut.addChildrenToQueue(queue, lf, parentFeats);		
		assertEquals(queue.size(), 1);
		Relation rel = queue.get(0);
		assertEquals(rel.getParentFeats(), parentFeats);
	}
	@Test
	public void testAddChildrenToQueueLFMultiRoot() {
		List<Relation> queue = new ArrayList<>();
		List<Element> rootWords = new ArrayList<>();
		rootWords.add(mock(Element.class));
		rootWords.add(mock(Element.class));
		when(lf.getChildren("node")).thenReturn(rootWords);
		when(lf.getName()).thenReturn("lf");
		WordFeatures parentFeats = new LogicalForm("test").getHead();
		
		uut.addChildrenToQueue(queue, lf, parentFeats);		
		assertEquals(queue.size(), 2);
		Relation rel = queue.get(0);
		assertEquals(rel.getParentFeats(), parentFeats);
	}
	@Test
	public void testAddChildrenToQueueCCONJ() {
		List<Relation> queue = new ArrayList<>();
		WordFeatures parentFeats = new WordFeatures();
		
		// Set up node children
		List<Element> nodeChildren = new ArrayList<>();
		Element nodeChild = mock(Element.class);
		when(nodeChild.getName()).thenReturn("node");
		nodeChildren.add(nodeChild);
		when(lf.getChildren("node")).thenReturn(nodeChildren);		
		when(lf.getName()).thenReturn("node");
		
		// Set up rel children
		List<Element> children = new ArrayList<>();
		Element relChild = mock(Element.class);
		when(relChild.getName()).thenReturn("rel");
		when(relChild.getAttributeValue("name")).thenReturn("First");
		children.add(relChild);
		children.addAll(nodeChildren);
		when(lf.getChildren()).thenReturn(children);
		
		uut.addChildrenToQueue(queue, lf, parentFeats);		
		assertEquals(queue.size(), 2);
		Relation rel = queue.get(0);
		assertEquals(rel.getParentFeats(), WordFeatures.CCONJ);
		rel = queue.get(1);
		assertEquals(rel.getName(), "First");
		assertEquals(rel.getParentFeats(), parentFeats);
	}
	
	// EXCHANGE FEATURES TESTS TODO Move elsewhere
//	@Test
//	public void testExchangeFeaturesHeadParent() {
//		LogicalForm lf = new LogicalForm("test");
//		WordFeatures parent = lf.getHead();
//		WordFeatures child = new WordFeatures();
//		uut.exchangeFeatures(lf, parent, child, wordInfoMap, null);
//		
//		WordFeatures expChild = new WordFeatures();
//		addFeatures(expChild, "RN", "NULL");
//		assertEquals(child.getFeatures(), expChild.getFeatures());
//	}
//	@Test
//	public void testExchangeFeaturesXParent() {
//		LogicalForm lf = new LogicalForm("test");
//		WordFeatures parent = new WordFeatures();
//		addFeatures(parent, "id", "x1");
//		WordFeatures child = new WordFeatures();
//		uut.exchangeFeatures(lf, parent, child, wordInfoMap, null);
//		
//		WordFeatures expChild = new WordFeatures();
//		addFeatures(expChild, "RN", "NULL");
//		assertEquals(child.getFeatures(), expChild.getFeatures());
//	}
//	@Test
//	public void testExchangeFeaturesNormal() {
//		LogicalForm lf = new LogicalForm("test");
//		WordFeatures parent = new WordFeatures();
//		addFeatures(parent, "id", "w0", "PN", "something", "PT", "NN");
//		WordFeatures child = new WordFeatures();
//		addFeatures(child, "id", "w1", "PN", "else", "PT", "NN");
//		when(wordInfoMap.getSupertag("w0")).thenReturn("np");
//		when(wordInfoMap.getSupertag("w1")).thenReturn("np");
//		uut.exchangeFeatures(lf, parent, child, wordInfoMap, "GenRel");
//		
//		WordFeatures expParent = new WordFeatures();
//		addFeatures(expParent, "id", "w0", "PN", "something", "PT", "NN", "CS", "np", "CT", "GenRel", "MS", "np", "CN", "else");
//		WordFeatures expChild = new WordFeatures();
//		addFeatures(expChild, "id", "w1", "PN", "else", "PT", "NN", "PP", "NN", "PS", "np", "RN", "something");
//		assertEquals(parent.getFeatures(), expParent.getFeatures());
//		assertEquals(child.getFeatures(), expChild.getFeatures());
//	}
}
