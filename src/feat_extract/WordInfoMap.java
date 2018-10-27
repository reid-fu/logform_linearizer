package feat_extract;
import java.util.HashMap;
import java.util.TreeSet;
import java.util.Map;
import java.util.Set;
import org.jdom2.Element;

import main.Exceptions.*;

/** Contains lemmas, POS tags, and gold supertags for each predicate word in sentence.
 * Predicate words are those present in logical form, as opposed to certain function words
 * and punctuation, which are omitted from logical form. This data structure uses word ID
 * in logical form to map words. */
public class WordInfoMap {
	private String sentence;
	/** Mapping from word's ID in logical form to its lemma, POS, and gold supertag */
	private Map<String, WordInfo> wordPreds = new HashMap<>();
	
	/** @param item XML element representing sentence, which includes "lf" and "pred-info" children 
	 * @throws NoPredicatesException */
	public WordInfoMap(Element item) throws NoPredicatesException {
		if(item != null) {
			Element predInfo = item.getChild("pred-info");
			sentence = item.getAttributeValue("string");
			parse(predInfo);
		}
	}
	
	public void parse(Element predInfo) throws NoPredicatesException {
		String predInfoStr = predInfo.getAttributeValue("data");
		parse(predInfoStr);
	}
	public void parse(String predInfo) throws NoPredicatesException {
		if(predInfo==null || predInfo.equals("")) {
			throw new NoPredicatesException(sentence);
		}
		String[] words = predInfo.split(" ");
		
		for(String word : words) {
			String[] predInfoArr = word.split(":");
			String wordText = predInfoArr[0];
			WordInfo fullWord = new WordInfo(predInfoArr[1], predInfoArr[2], predInfoArr[3]);
			wordPreds.put(wordText, fullWord);
		}
	}
	
	public void addWordInfo(String wordID, WordInfo wordInfo) {
		wordPreds.put(wordID, wordInfo);
	}
	public boolean containsWordId(String wordID) {
		return wordPreds.containsKey(wordID);
	}
	public String getSentence() {
		return sentence;
	}
	public WordInfo getWordInfo(String wordID) throws InvalidWordIDException {
		WordInfo wordInfo = wordPreds.get(wordID);
		if(wordInfo == null) {
			throw new InvalidWordIDException(sentence, wordID);
		} else {
			return wordInfo;
		}
	}
	public String getSupertag(String wordID) throws InvalidWordIDException {
		return getWordInfo(wordID).getSupertag();
	}
	public String getPOS(String wordID) throws InvalidWordIDException {
		return getWordInfo(wordID).getPos();
	}
	public String getLemma(String wordID) throws InvalidWordIDException {
		return getWordInfo(wordID).getLemma();
	}
	/** @return word ID's in lexical order
	 * Mainly used for debugging */
	public Set<String> sortedKeys(){
		Set<String> sortedKeys = new TreeSet<>(wordPreds.keySet());
		return sortedKeys;
	}
	public String toString() {
		return wordPreds.toString();
	}
}
