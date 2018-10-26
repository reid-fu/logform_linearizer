package linearizer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import feat_extract.LogicalForm;
import feat_extract.WordFeatures;

public class DFSLinearizer extends Linearizer {
//	public List<String> order(Element lfSubtree, boolean parens) {
//		List<String> order = new LinkedList<>();
//		String wordID = lfSubtree.getAttributeValue("id");
//		if(wordID != null) {
//			wordID = wordID.contains(":") ? wordID.substring(0, wordID.indexOf(':')) : wordID;
//			if(wordID.charAt(0) == 'w' && !order.contains(wordID)) {
//				if(parens)
//					order.add("(");
//				order.add(wordID);
//			}
//		}
//		
//		for(Element child : lfSubtree.getChildren()) {
//			List<String> childOrder = order(child, parens);
//			order.addAll(childOrder);
//		}
//		if(parens) {
//			order.add(")");
//		}
//		return order;
//	}

	@Override
	public List<String> order(LogicalForm lf, WordFeatures current, Set<String> visited, LinConfig config) {
		List<String> order = addCurrentToOrderAndVisited(current, visited);
		if(order == null)
			return new ArrayList<String>();
		List<WordFeatures> children = current.getChildList();
		Collections.shuffle(children);
		
		for(WordFeatures child : children) {
			List<String> childOrder = order(lf, child, visited, config);
			order.addAll(childOrder);
		}
		maybeAddParens(current, config, order);
		return order;
	}
}
