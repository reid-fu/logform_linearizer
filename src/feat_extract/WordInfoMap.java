package feat_extract;
import java.util.HashMap;
import java.util.TreeSet;
import java.util.Map;
import java.util.Set;
import org.jdom2.Element;

import main.Exceptions.NoPredicatesException;

public class WordInfoMap {
	private String sentence;
	private Map<String, WordInfo> wordPreds = new HashMap<>();
	
	public WordInfoMap(String sentence) {
		this.sentence = sentence;
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
	public String getSupertag(String wordID) {
		if(wordPreds.get(wordID) == null) {
			System.err.println("No supertag for word ID " + wordID);
		}
		return wordPreds.get(wordID).getSupertag();
	}
	public String getPOS(String wordID) {
		return wordPreds.get(wordID).getPos();
	}
	public String getLemma(String wordID) {
		return wordPreds.get(wordID).getLemma();
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
