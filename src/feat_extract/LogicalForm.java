package feat_extract;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class LogicalForm {
	private String sentence;
	private Map<String, WordFeatures> featureMap;
	private Map<String, String> supertags;
	private WordFeatures head;
	
	public LogicalForm(String sentence) {
		this.sentence = sentence;
		featureMap = new HashMap<>();
		supertags = new HashMap<>();
		head = (WordFeatures) WordFeatures.HEAD.clone();
	}
	public String getSentence() {
		return sentence;
	}
	public Set<String> getWordIds(){
		return featureMap.keySet();
	}
	public void addWordFeatures(String wordID, WordFeatures wordFeats) {
		featureMap.put(wordID, wordFeats);
	}
	public WordFeatures getWordFeatures(String wordID) {
		return featureMap.get(wordID);
	}
	public void addSupertag(String wordID, String supertag) {
		supertags.put(wordID, supertag);
	}
	public String getSupertag(String wordID) {
		return supertags.get(wordID);
	}
	public WordFeatures getHead() {
		return head;
	}
	public WordFeatures addXNode(String wordID) {
		if(!wordID.startsWith("x")) {
			System.err.println("Calling addXNode with wordID that doesn't start with x");
		}
		WordFeatures x = new WordFeatures();
		x.addFeature("id", wordID);
		addWordFeatures(wordID, x);
		return x;
	}
}
