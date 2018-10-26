package linearizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import feat_extract.LogicalForm;
import feat_extract.WordFeatures;

public class PreorderLinearizer extends Linearizer {
	private static final String[] CHILD_ORDER = ChildOrders.ENG_CHILD_ORDER;
	
	/** English-like linearizer that puts word ID's in following order:<br/>
	 * - Arg0<br/>
	 * - Parent<br/>
	 * - Remaining arguments in order<br/>
	 * - Other relations sorted by number of nodes in subtree */
	public List<String> order(LogicalForm lf, WordFeatures current, Set<String> visited, LinConfig config) {
		List<String> order = addCurrentToOrderAndVisited(current, visited);
		if(order == null)
			return new ArrayList<String>();
		
		addChildOrderChildren(lf, order, current, visited, config);
		addRemainingChildren(lf, order, current, visited, config);
		maybeAddParens(current, config, order);
		return order;
	}
	
	// METHODS FOR ADDING CHILD ORDER CHILDREN
	/** Adds children in CHILD_ORDER, with Det, GenOwn, Arg0, and size 1 or 2 modifiers going in front of parent */
	public void addChildOrderChildren(LogicalForm lf, List<String> order, WordFeatures current,
			Set<String> visited, LinConfig config) {
		addChildrenAfterParent(lf, order, current, visited, config);
	}
	public void addChildrenAfterParent(LogicalForm lf, List<String> order, WordFeatures current,
			Set<String> visited, LinConfig config) {
		for(int i = 0; i < CHILD_ORDER.length; i++) {
			String rel_type = CHILD_ORDER[i].contains(":") ? CHILD_ORDER[i].substring(0, CHILD_ORDER[i].indexOf(":")) : CHILD_ORDER[i];
			String size_range = CHILD_ORDER[i].contains(":") ? CHILD_ORDER[i].substring(CHILD_ORDER[i].indexOf(":") + 1) : null;
			List<WordFeatures> children = current.getChildren().get(rel_type);
			
			// Filter out children outside of size range, if size range is specified
			int lowBound = (size_range == null) ? 0 : Integer.parseInt(size_range.substring(0, size_range.indexOf("-")));
			int highBound = (size_range == null) ? 1000 : Integer.parseInt(size_range.substring(size_range.indexOf("-") + 1));
			if(children != null) {
				children = new ArrayList<>(children);
				children.removeIf(feats -> feats.getSubtreeSize() < lowBound || feats.getSubtreeSize() > highBound);
				
				// Add children to order
				for(WordFeatures child : children) {
					List<String> childOrder = order(lf, child, visited, config);
					order.addAll(childOrder);
				}
			}
		}
	}
	
	// METHODS FOR ADDING REMAINING CHILDREN
	/** Add remaining related words to order, sorted by subtree size. If subtree size is equal, break tie based on number of characters in predicates. */
	public void addRemainingChildren(LogicalForm lf, List<String> order, WordFeatures current,
			Set<String> visited, LinConfig config) {
		List<List<String>> remChildren = getRemainingChildren(lf, order, current, visited, config);
		Collections.sort(remChildren, new Comparator<List<String>>() {
			@Override
			public int compare(List<String> arg0, List<String> arg1) {
				if(arg0.size() - arg1.size() != 0)
					return arg0.size() - arg1.size();
				int predLen0 = getTotalPredicateLength(lf, arg0);
				int predLen1 = getTotalPredicateLength(lf, arg1);
				return predLen0 - predLen1;
			}
		});
		for(List<String> childOrder : remChildren) {
			order.addAll(childOrder);
		}
	}
	public List<List<String>> getRemainingChildren(LogicalForm lf, List<String> order, WordFeatures current,
			Set<String> visited, LinConfig config) {
		List<List<String>> remChildren = new LinkedList<>();
		for(String relation : current.getChildren().keySet()) {
			List<String> childOrderList = Arrays.asList(CHILD_ORDER);
			if(childOrderList.contains(relation))
				continue;
			String rangedRel = relation + ":";
			List<String> orderRangedRel = childOrderList.stream().filter(s -> s.startsWith(rangedRel)).collect(Collectors.toList());
			
			if(orderRangedRel.size() > 0) {
				String size_range = orderRangedRel.get(0);
				size_range = size_range.substring(size_range.indexOf(":") + 1);
				int lowBound = (size_range == null) ? 0 : Integer.parseInt(size_range.substring(0, size_range.indexOf("-")));
				int highBound = (size_range == null) ? 1000 : Integer.parseInt(size_range.substring(size_range.indexOf("-") + 1));
				
				for(WordFeatures childFeats : current.getChildren().get(relation)) {
					if(childFeats.getSubtreeSize() < lowBound || childFeats.getSubtreeSize() > highBound) {
						List<String> childOrder = order(lf, childFeats, visited, config);
						remChildren.add(childOrder);
					}
				}
			} else {
				for(WordFeatures childFeats : current.getChildren().get(relation)) {					
					List<String> childOrder = order(lf, childFeats, visited, config);
					remChildren.add(childOrder);
				}
			}
		}
		return remChildren;
	}
	public int getTotalPredicateLength(LogicalForm lf, List<String> wordIDs) {
		int len = 0;
		for(String wordID : wordIDs) {
			if(wordID.equals("(") || wordID.equals(")"))
				continue;
			WordFeatures feats = lf.getWordFeatures(wordID);
			String pn = feats.getUniqueFeature("PN");
			if(pn != null)
				len += pn.length();
		}
		return len;
	}
}
