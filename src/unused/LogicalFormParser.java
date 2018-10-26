package unused;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.jdom2.Element;

import feat_extract.WordFeatures;
import feat_extract.WordInfoMap;
import main.Exceptions.*;

public class LogicalFormParser {
	private String sentence;
	private Map<String, WordFeatures> featureMap = new HashMap<>();
	private Map<String, String> supertags = new HashMap<>();
	private WordFeatures head;
	public String getSentence() {
		return sentence;
	}
	public Set<String> getWordIds(){
		return featureMap.keySet();
	}
	public WordFeatures getWordFeatures(String wordID) {
		return featureMap.get(wordID);
	}
	public String getSupertag(String wordID) {
		return supertags.get(wordID);
	}
	
	// Methods to change data
	public void parseFromXML(String sentence, Element lf, WordInfoMap wordInfoMap) throws CCGXMLParseException {
		this.sentence = sentence;
		parseHeadFeatures(lf, wordInfoMap);
		
		List<TriEntry<Element, String, WordFeatures>> queue = new ArrayList<>();
		addChildrenToQueue(queue, lf.getChild("node"), head);
		while(!queue.isEmpty()) {
			TriEntry<Element, String, WordFeatures> current = queue.remove(0);
			Element node = current.getFirst();
			String relation = current.getSecond();
			WordFeatures parentFeatures = current.getThird();
			
			String wordID = node.getAttributeValue("idref");
			WordFeatures wordFeatures = null;
			if(wordID != null) { //node has idref attribute
				wordFeatures = featureMap.get(wordID);
				//If wordID is not in featureMap, add relation feature and skip to next iteration
				if(wordFeatures == null) {
					parentFeatures.addFeature("CT", relation);
					continue;
				}
			} else if(node.getAttributeValue("pred") != null) { //node has pred attribute
				wordID = node.getAttributeValue("id");
				wordFeatures = new WordFeatures();
				parseWordFeatures(wordFeatures, node, wordID, wordInfoMap);
			} else {
				addChildrenToQueue(queue, node, WordFeatures.HEAD);
				continue;
			}
			
			exchangeFeatures(parentFeatures, wordFeatures, wordInfoMap, relation);
			wordID = wordID.contains(":") ? wordID.substring(0, wordID.indexOf(':')) : wordID;
			featureMap.put(wordID, wordFeatures);
			addChildrenToQueue(queue, node, wordFeatures);
		}
	}
	private void parseHeadFeatures(Element lf, WordInfoMap wordInfo) throws CCGXMLParseException {
		Element root = lf.getChild("node");
		String headID = root.getAttributeValue("id");
		//TODO: Determine how to handle case in which head word is not predicate
		if(headID.charAt(0) != 'w') {
			throw new HeadNotPredicateException(sentence);
		}
		String headSupertag = (headID.indexOf(':') == -1) ? wordInfo.getSupertag(headID)
				: wordInfo.getSupertag(headID.substring(0, headID.indexOf(':')));
		
		head = new WordFeatures();
		parseWordFeatures(head, root, headID, wordInfo);
		head.addFeature("RN", "NULL");
		featureMap.put(headID, head);
		supertags.put(headID, headSupertag);
	}
	private void exchangeFeatures(WordFeatures parent, WordFeatures child, WordInfoMap wordInfo, String relation) {
		if(parent == WordFeatures.HEAD) {
			child.addFeature("RN", "NULL");
		} else {
			String parentID = parent.getUniqueFeature("id");
			String childID = child.getUniqueFeature("id");
			String parentSupertag = wordInfo.getSupertag(parentID);
			String childSupertag = wordInfo.getSupertag(childID);
			supertags.put(parentID, parentSupertag);
			supertags.put(childID, childSupertag);
			addParentFeatures(parent, child, parentSupertag);
			addChildFeatures(parent, child, childSupertag, relation);
		}
	}
	private void addChildrenToQueue(List<TriEntry<Element, String, WordFeatures>> queue, Element parent, WordFeatures parentFeatures) {
		List<Element> rootRels = parent.getChildren();
		for(Element rel : rootRels) {
			Element child = rel.getChild("node");
			String relName = rel.getAttributeValue("name");
			queue.add(new TriEntry<>(child, relName, parentFeatures));
		}
	}
	
	public void parseWordFeatures(WordFeatures word, Element wordNode, String id, WordInfoMap wordInfoMap)
			throws NoRelationNameException {
		String wordID = id, wordNE = null; //wordNE is word's named entity category
		if(wordID.indexOf(':') != -1) {
			int colonIndex = wordID.indexOf(':');
			wordNE = wordID.substring(colonIndex + 1);
			wordID = wordID.substring(0, colonIndex);
		}
		
		word.addFeature("id", wordID);
		word.addFeature("PN", wordInfoMap.getLemma(wordID));
		word.addFeature("PT", wordInfoMap.getPOS(wordID));
		if(wordNE != null) {
			word.addFeature("XC", wordNE);
		}
		addOptionalFeatures(word, wordNode);
		addNumChildrenFeatures(word, wordNode);
	}
	private void addOptionalFeatures(WordFeatures word, Element wordNode) {
		String[] otherAttrs = {"mood", "tense", "det", "num", "partic"}; //Attributes may or may not be present
		String[] corrFeatures = {"ZM", "ZT", "ZD", "ZN", "ZP"}; //Corresponding features, matched by index
		for(int i = 0; i < otherAttrs.length;i++) {
			String attrValue = wordNode.getAttributeValue(otherAttrs[i]);
			if(attrValue != null) {
				word.addFeature(corrFeatures[i], attrValue);
			}
		}
	}
	private void addNumChildrenFeatures(WordFeatures word, Element wordNode) throws NoRelationNameException {
		word.addFeature("FO", "" + wordNode.getChildren().size());
		int numArgChildren = 0;
		for(Element rel : wordNode.getChildren()) {
			String relName = rel.getAttributeValue("name");
			//TODO: Determine what this case is and how to handle it
			if(relName == null) {
				throw new NoRelationNameException(sentence, rel.toString());
			}
			if(relName.startsWith("Arg")) {
				numArgChildren++;
			}
		}
		word.addFeature("NA", "" + numArgChildren);
	}
	
	public void addParentFeatures(WordFeatures parent, WordFeatures child, String parentSupertag) {
		String parentLemma = parent.getUniqueFeature("PN");
		child.addFeature("RN", parentLemma);
		String parentPOS = parent.getUniqueFeature("PT");
		child.addFeature("PP", parentPOS);
		child.addFeature("PS", parentSupertag);
	}
	
	public void addChildFeatures(WordFeatures parent, WordFeatures child, String childSupertag, String relation) {
		parent.addFeature("CT", relation);
		parent.addFeature("CS", childSupertag);
		String childName = child.getUniqueFeature("PN");
		parent.addFeature("CN", childName);
		String childPOS = child.getUniqueFeature("PT");
		String childNE = child.getUniqueFeature("XC"); //named entity class
		
		if(relation.startsWith("Arg")) {
			//TODO: Find out what to do with Arg relations without numbers
			char argNum = (relation.length() > 3) ? relation.charAt(3) : '0';
			parent.addFeature("A" + argNum + "N", childName);
			parent.addFeature("A" + argNum + "P", childPOS);
			parent.addFeature("AS", childSupertag);
			if(childNE != null) {
				parent.addFeature("X" + argNum + "D", childNE);
			}
		} else {
			parent.addFeature("MS", childSupertag);
			if(relation.equals("Mod")) {
				parent.addFeature("MP", childPOS);
			}
			if(childNE != null) {
				parent.addFeature("XM", childNE);
			}
		}
	}
}
