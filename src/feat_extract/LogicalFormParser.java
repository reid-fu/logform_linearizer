package feat_extract;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jdom2.Element;

import main.Exceptions.*;

public class LogicalFormParser {
	private WordFeatParser wordFeatParser = new WordFeatParser();
	private ParentChildParser parentChildParser = new ParentChildParser();
	
	public LogicalForm parse(String sentence, Element lf, WordInfoMap wordInfoMap) throws CCGXMLParseException, InvalidWordIDException {
		assert lf.getName().equals("lf");		
		LogicalForm parse = new LogicalForm(sentence);
		List<Relation> queue = new ArrayList<>();
		addChildrenToQueue(queue, lf, parse.getHead());
		
		while(!queue.isEmpty()) {
			Relation r = queue.remove(0);
			boolean ref = r.getChildXML().getAttributeValue("idref") != null;
			String wordID = ref ? r.getChildXML().getAttributeValue("idref") : r.getChildXML().getAttributeValue("id");
			
			WordFeatures wordFeats = wordFeatParser.getWordFeatures(parse, wordID, r.getChildXML(), wordInfoMap);
			wordID = wordID.contains(":") ? wordID.substring(0, wordID.indexOf(':')) : wordID;
			parse.addWordFeatures(wordID, wordFeats);
			
			if(r.getParentFeats() != WordFeatures.CCONJ) {
				parentChildParser.exchangeFeatures(parse, r.getParentFeats(), wordFeats, wordInfoMap, r.getName());
				String relName = ref ? r.getName() + LogicalForm.REF_MARKER : r.getName();
				r.getParentFeats().addChild(relName, wordFeats);
				wordFeats.addParent(relName, r.getParentFeats());
			}
			addChildrenToQueue(queue, r.getChildXML(), wordFeats);
		}
		updateSubtreeSizes(parse);
		return parse;
	}
	/** If parent has "node" children, then parent is either HEAD node or coordinating conjunction.
	 * If parent is "lf" node, add nameless relations between dummy HEAD node and root words of sentence.
	 * If parent word is coordinating conjunction and child is "node" node (representing shared argument),
	 * add nameless relation between coordinating conjunction and shared argument.
	 * Otherwise, add named relations between parent and child words. */
	public void addChildrenToQueue(List<Relation> queue, Element parent, WordFeatures parentFeatures) {
		// Process <node> children
		List<Element> nodeChildren = parent.getChildren("node");
		for(Element child : nodeChildren) {
			if(parent.getName().equals("lf")) {
				assert parentFeatures.equals(WordFeatures.HEAD);
				queue.add(new Relation(child, parentFeatures, "None"));
			} else { // Parent word is coordinating conjunction and child word represents shared argument
				queue.add(new Relation(child, WordFeatures.CCONJ, "None"));
			}
		}
		
		// Process <rel> children
		List<Element> relChildren = parent.getChildren();
		for(Element rel : relChildren) {
			if(rel.getName().equals("node"))
				continue;
			Element child = rel.getChild("node");
			String relName = rel.getAttributeValue("name");
			queue.add(new Relation(child, parentFeatures, relName));
		}
	}
	public void updateSubtreeSizes(LogicalForm lf) {
		updateSubtreeSize(lf.getHead(), new HashSet<String>());
	}
	public void updateSubtreeSize(WordFeatures feats, Set<String> visited) {
		for(WordFeatures child : feats.getChildList()) {
			String wordID = child.getUniqueFeature("id");
			if(wordID != null && !visited.contains(wordID)) {
				visited.add(wordID);
				updateSubtreeSize(child, visited);
			}
			feats.updateSubtreeCount(child.getSubtreeSize());
		}
	}
}
