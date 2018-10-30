package linearizer.eng_heuristic;
import java.util.List;
import feat_extract.WordFeatures;

public class ChildOrdererFactory {
	private ChildOrderer DEFAULT_ORDERER = new ChildOrderer() {};
	private VerbChildOrderer VERB_ORDERER = new VerbChildOrderer();
	
	/** @return ChildOrderer based on current word's part of speech */
	public ChildOrderer childOrderer(WordFeatures current) {
		if(VerbChildOrderer.isVerb(current)) {
			return VERB_ORDERER;
		} else {
			return DEFAULT_ORDERER;
		}
	}
	/** @return "Best" order of child words of current word, represented as list of relation names */
	public List<String> relOrder(WordFeatures current) {
		return childOrderer(current).relOrder(current);
	}
}
