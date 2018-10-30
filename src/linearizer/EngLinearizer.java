package linearizer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import feat_extract.LogicalForm;
import feat_extract.WordFeatures;
import linearizer.eng_heuristic.ChildOrderer;
import linearizer.eng_heuristic.ChildOrdererFactory;
import linearizer.eng_heuristic.UtilLin;

public class EngLinearizer extends Linearizer {
	private ChildOrdererFactory ordererFactory = new ChildOrdererFactory();
	
	/** English-like linearizer that puts word ID's in following order:<br/>
	 * - Arg0<br/>
	 * - Parent<br/>
	 * - Remaining arguments in order<br/>
	 * - Other relations sorted by number of nodes in subtree */
	public List<String> order(LogicalForm lf, WordFeatures current, Set<String> visited, LinConfig config) {
		List<String> order = addCurrentToOrderAndVisited(current, visited);
		if(order == null)
			return new ArrayList<String>();
		WordFeatures clone = (WordFeatures) current.clone();
		List<String> relOrder = ordererFactory.relOrder(current);
		List<String> beforeParent = relOrder.subList(0, relOrder.indexOf(ChildOrderer.PARENT_REL));
		List<String> afterParent = relOrder.subList(relOrder.indexOf(ChildOrderer.PARENT_REL) + 1, relOrder.size());
		
		addChildrenBeforeParent(lf, order, beforeParent, clone, visited, config);
		addChildrenAfterParent(lf, order, afterParent, clone, visited, config);
		addRemainingChildren(lf, order, clone, visited, config);
		maybeAddParens(current, config, order);
		return order;
	}
	/** Inserts children with relations in beforeParent in front of parent */
	public void addChildrenBeforeParent(LogicalForm lf, List<String> order, List<String> beforeParent,
			WordFeatures current, Set<String> visited, LinConfig config) {
		for(int i = beforeParent.size()-1; i >= 0; i--) {
			String rel = beforeParent.get(i);
			String rel_type = rel.contains(":") ? rel.substring(0, rel.indexOf(":")) : rel;
			String size_range = rel.contains(":") ? rel.substring(rel.indexOf(":") + 1) : null;
			List<WordFeatures> children = UtilLin.childrenWithinSizeRange(current, rel_type, size_range);
			UtilLin.removeChildrenWithinSizeRange(current, rel_type, size_range);
			
			for(WordFeatures child : children) {
				List<String> childOrder = order(lf, child, visited, config);
				order.addAll(0, childOrder);
			}			
		}
	}
	/** Inserts children with relations in afterParent after parent */
	public void addChildrenAfterParent(LogicalForm lf, List<String> order, List<String> afterParent,
			WordFeatures current, Set<String> visited, LinConfig config) {
		for(int i = 0; i < afterParent.size(); i++) {
			String rel = afterParent.get(i);
			String rel_type = rel.contains(":") ? rel.substring(0, rel.indexOf(":")) : rel;
			String size_range = rel.contains(":") ? rel.substring(rel.indexOf(":") + 1) : null;
			List<WordFeatures> children = UtilLin.childrenWithinSizeRange(current, rel_type, size_range);
			UtilLin.removeChildrenWithinSizeRange(current, rel_type, size_range);
			
			for(WordFeatures child : children) {
				List<String> childOrder = order(lf, child, visited, config);
				order.addAll(childOrder);
			}
		}
	}
	/** Add remaining related words to order, sorted by subtree size. If subtree size is equal, break tie based on number of characters in predicates. */
	public void addRemainingChildren(LogicalForm lf, List<String> order, WordFeatures current, Set<String> visited, LinConfig config) {
		List<List<String>> remChildren = getRemainingChildren(lf, current, visited, config);
		Collections.sort(remChildren, new Comparator<List<String>>() {
			@Override
			public int compare(List<String> arg0, List<String> arg1) {
				if(arg0.size() - arg1.size() != 0)
					return arg0.size() - arg1.size();
				int predLen0 = UtilLin.getTotalPredicateLength(lf, arg0);
				int predLen1 = UtilLin.getTotalPredicateLength(lf, arg1);
				return predLen0 - predLen1;
			}
		});
		for(List<String> childOrder : remChildren) {
			order.addAll(childOrder);
		}
	}
	public List<List<String>> getRemainingChildren(LogicalForm lf, WordFeatures current, Set<String> visited, LinConfig config) {
		List<List<String>> remChildren = new LinkedList<>();
		for(String relation : current.getChildren().keySet()) {
			for(WordFeatures childFeats : current.getChildren().get(relation)) {					
				List<String> childOrder = order(lf, childFeats, visited, config);
				remChildren.add(childOrder);
			}
		}
		return remChildren;
	}
}
