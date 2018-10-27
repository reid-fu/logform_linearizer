package feat_extract;
import org.jdom2.Element;

import main.Exceptions.InvalidWordIDException;

public class WordFeatParser {
	/** @return Feature set for word with passed ID. If word doesn't yet have feature set, creates one for it and parses its features. 
	 * @throws InvalidWordIDException */
	public WordFeatures getWordFeatures(LogicalForm lf, String wordID, Element wordXML, WordInfoMap wordInfoMap) throws InvalidWordIDException {
		String wordNE = wordID.contains(":") ? wordID.substring(wordID.indexOf(':') + 1) : null;
		wordID = wordID.contains(":") ? wordID.substring(0, wordID.indexOf(':')) : wordID;
		WordFeatures featSet = lf.getWordFeatures(wordID);
		
		if(featSet != null) {
			if(wordID.matches("w[0-9]+") && featSet.getUniqueFeature("PN") == null) {
				parseWordFeatures(lf, featSet, wordXML, wordID, wordNE, wordInfoMap);
			}
			return featSet;
		} else if(wordXML.getAttributeValue("pred") != null) {
			featSet = new WordFeatures();
			parseWordFeatures(lf, featSet, wordXML, wordID, wordNE, wordInfoMap);
			return featSet;
		} /* Executed if wordID follows regex "x[0-9]+" or wordXML has "idref" attribute */
		else {
			featSet = new WordFeatures();
			featSet.addFeature("id", wordID);
			return featSet;
		}
	}
	/** Handles word ID's following regex "w[0-9]+(:[A-Z]+)?" or "h[0-9]+"<br/>
	* Adds at least following features: PN, PT, FO, NA 
	 * @throws InvalidWordIDException */
	public void parseWordFeatures(LogicalForm lf, WordFeatures wordFeats, Element xmlNode,
			String id, String wordNE, WordInfoMap wordInfoMap) throws InvalidWordIDException {
		String wordID = id; //wordNE is word's named entity category		
		wordFeats.addFeature("id", wordID);
		wordFeats.addFeature("PN", getLemma(xmlNode, wordID, wordInfoMap));
		wordFeats.addFeature("PT", wordInfoMap.getPOS(wordID));
		if(wordInfoMap.getSupertag(wordID) != null) {
			lf.addSupertag(wordID, wordInfoMap.getSupertag(wordID));
		}
		if(wordNE != null) {
			wordFeats.addFeature("XC", wordNE);
		}
		addMorphologicalFeatures(wordFeats, xmlNode);
		addNumChildrenFeatures(wordFeats, xmlNode);
	}
	public String getLemma(Element xmlNode, String id, WordInfoMap wordInfoMap) throws InvalidWordIDException {
		String lemma = wordInfoMap.getLemma(id);
		if(lemma == null || lemma.equals("null")) {
			lemma = xmlNode.getAttributeValue("pred");
		}
		return lemma;
	}
	/** Adds ZM, ZT, ZD, ZN, and ZP features to current word's feature set */
	public void addMorphologicalFeatures(WordFeatures wordFeats, Element xmlNode) {
		String[] otherAttrs = {"mood", "tense", "det", "num", "partic"}; //Attributes may or may not be present
		String[] corrFeatures = {"ZM", "ZT", "ZD", "ZN", "ZP"}; //Corresponding features, matched by index
		
		for(int i = 0; i < otherAttrs.length;i++) {
			String attrValue = xmlNode.getAttributeValue(otherAttrs[i]);
			if(attrValue != null) {
				wordFeats.addFeature(corrFeatures[i], attrValue);
			}
		}
	}
	/** Adds FO and NA features to current word's feature set */
	public void addNumChildrenFeatures(WordFeatures wordFeats, Element xmlNode) {
		wordFeats.addFeature("FO", "" + xmlNode.getChildren("rel").size());
		int numArgChildren = 0;
		
		for(Element rel : xmlNode.getChildren()) {
			if(!rel.getName().equals("rel"))
				continue;
			String relName = rel.getAttributeValue("name");
			if(relName.matches("Arg[0-9][a-z]?")) // Arg, ArgA, ArgM do not count as argument
				numArgChildren++;
		}
		wordFeats.addFeature("NA", "" + numArgChildren);
	}
}
