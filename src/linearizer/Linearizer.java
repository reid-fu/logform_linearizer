package linearizer;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import feat_extract.LogicalForm;
import feat_extract.WordFeatures;

public abstract class Linearizer {
	/** @return Order of word ID's based on linearization */
	public List<String> getOrder(LogicalForm lf, WordFeatures current, Set<String> visited, LinConfig config) {
		List<String> order = order(lf, current, visited, config);
		if(config.relSeq()) {
			putRelationInSequence(lf, order);
		}
		return order;
	}
	public abstract List<String> order(LogicalForm lf, WordFeatures current, Set<String> visited, LinConfig config);
	
	/** @return list with current, if current has word ID in format w# and hasn't been visited<br/>
	 * null if current has word ID in format w# and has been visited<br/>
	 * empty list otherwise */
	public List<String> addCurrentToOrderAndVisited(WordFeatures current, Set<String> visited) {
		List<String> order = new LinkedList<>();
		String wordID = current.getUniqueFeature("id");
		if(wordID != null && wordID.charAt(0) == 'w' && !visited.contains(wordID)) {
			order.add(wordID);
			visited.add(wordID);
		} else if(wordID != null && wordID.charAt(0) == 'w') {
			return null;
		}
		return order;
	}
	public void putRelationInSequence(LogicalForm lf, List<String> order) {
		for(int i = 0;i < order.size();i++) {
			String item = order.get(i);
			if(item.matches("w[0-9]*")) { //item is word ID
				WordFeatures feats = lf.getWordFeatures(item); //should not be null
				List<String> parentRels = feats.getFeature("PR");
				if(parentRels != null && parentRels.size() > 0) {
					order.add(i, parentRels.get(0));
					i++;
				}
			}
		}
	}
	/** Puts parentheses around linearization sequences that represent subtrees of size k or more, where k depends on config */
	public void maybeAddParens(WordFeatures current, LinConfig config, List<String> order) {
		int parenSubtreeSize = config.parenSubtreeSize();
		if(parenSubtreeSize > 0 && current.getSubtreeSize() >= parenSubtreeSize) {
			order.add(0, "(");
			order.add(")");
		}
	}
}
