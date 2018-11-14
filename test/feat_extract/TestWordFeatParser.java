package feat_extract;
import org.jdom2.Element;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.mockito.runners.MockitoJUnitRunner;
import util.WFTestUtil;

@RunWith(MockitoJUnitRunner.class)
public class TestWordFeatParser {
	private WordFeatParser uut = new WordFeatParser();
	
	//Method getWordFeatures
	@Test
	public void testAddFeaturesToExisting() {
		//TODO Finish test case for WordFeatParser
	}
	@Test
	public void testAddNewWNodeNoNE() {
		
	}
	@Test
	public void testAddNewWNodeNE() {
		
	}
	@Test
	public void testAddNewXNode() {
		
	}
	
	//Method parseWordFeatures
	@Test
	public void testParseWordFeatures() throws Exception {
		WordFeatures word = new WordFeatures();
		Element wordNode = mock(Element.class);
		String wordNE = "PERSON";
		WordInfoMap wordInfoMap = mock(WordInfoMap.class);
		
		when(wordInfoMap.getLemma("w0")).thenReturn("Henry");
		when(wordInfoMap.getPOS("w0")).thenReturn("NNP");
		when(wordInfoMap.getSupertag("w0")).thenReturn(null);
		
		uut.parseWordFeatures(null, word, wordNode, "w0", wordNE, wordInfoMap);
		assertEquals("w0", word.getUniqueFeature("id"));
		assertEquals("Henry", word.getUniqueFeature("PN"));
		assertEquals("NNP", word.getUniqueFeature("PT"));
		assertEquals("PERSON", word.getUniqueFeature("XC"));
	}
	
	//Method addMorphologicalFeatures
	@Test
	public void testAddMorphologicalFeaturesNoun() {
		WordFeatures word = WFTestUtil.wordFeaturesWithID("w0");
		Element wordNode = mock(Element.class);
		
		when(wordNode.getAttributeValue("det")).thenReturn("nil");
		when(wordNode.getAttributeValue("num")).thenReturn("sg");
		
		uut.addMorphologicalFeatures(word, wordNode);
		assertEquals("nil", word.getUniqueFeature("ZD"));
		assertEquals("sg", word.getUniqueFeature("ZN"));
	}
	@Test
	public void testAddMorphologicalFeaturesVerb() {
		WordFeatures word = WFTestUtil.wordFeaturesWithID("w1");
		Element wordNode = mock(Element.class);
		
		when(wordNode.getAttributeValue("mood")).thenReturn("dcl");
		when(wordNode.getAttributeValue("num")).thenReturn("sg");
		when(wordNode.getAttributeValue("tense")).thenReturn("past");
		
		uut.addMorphologicalFeatures(word, wordNode);
		assertEquals("dcl", word.getUniqueFeature("ZM"));
		assertEquals("sg", word.getUniqueFeature("ZN"));
		assertEquals("past", word.getUniqueFeature("ZT"));
	}
	
	//Method addNumChildrenFeatures
	@Test
	public void testAddNumChildrenFeatures() {
		WordFeatures word = WFTestUtil.wordFeaturesWithID("w0");
		Element wordNode = mock(Element.class), arg0Node = mock(Element.class), arg1aNode = mock(Element.class);
		Element argNode = mock(Element.class), genRelNode = mock(Element.class);
		
		when(arg0Node.getAttributeValue("name")).thenReturn("Arg0");
		when(arg1aNode.getAttributeValue("name")).thenReturn("Arg1a");
		when(argNode.getAttributeValue("name")).thenReturn("Arg");
		when(genRelNode.getAttributeValue("name")).thenReturn("GenRel");
		List<Element> rels = Arrays.asList(arg0Node, arg1aNode, argNode, genRelNode);
		when(wordNode.getChildren("rel")).thenReturn(rels);
		
		uut.addNumChildrenFeatures(word, wordNode);
		assertEquals("4", word.getUniqueFeature("FO"));
		assertEquals("2", word.getUniqueFeature("NA"));
	}
}
