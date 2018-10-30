package linearizer.eng_heuristic;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import feat_extract.WordFeatures;

public abstract class ChildOrderer {
	public static final String PARENT_REL = "Parent";
	/** {"Det","GenOwn","Arg0","Mod:1-2", "Parent","Arg1","Arg2","Arg3","Arg4","Arg5"}, where "Mod:1-2" means one or two-word modifiers */
	public static List<String> DEFAULT_CHILD_ORDER = 
			new ArrayList<>(Arrays.asList(new String[]{"Det","GenOwn","Arg0","Mod:1-2", PARENT_REL,"Arg1","Arg2","Arg3","Arg4","Arg5"}));
	
	/** @return "Best" order of child words of current word, represented as list of relation names */
	public List<String> relOrder(WordFeatures current) {
//		List<String> relOrder = new ArrayList<>();
//		for(String rel : DEFAULT_CHILD_ORDER) {
//			if(rel.equals(PARENT_REL)) {
//				relOrder.add(rel);
//			} else {
//				rel = rel.contains(":") ? rel.substring(0, rel.indexOf(":")) : rel;
//				String size_range = rel.contains(":") ? rel.substring(rel.indexOf(":") + 1) : null;
//				if(LinUtil.childrenWithinSizeRange(current, rel, size_range).size() > 0) {
//					relOrder.add(rel);
//				}
//			}
//		}
		return new ArrayList<>(DEFAULT_CHILD_ORDER);
	}
}
