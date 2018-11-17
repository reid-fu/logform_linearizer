package feat_extract;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class WordFeatures {
	public static final WordFeatures HEAD = new WordFeatures();
	public static final WordFeatures CCONJ = new WordFeatures();
	static {
		HEAD.addFeature("id", "HEAD");
		HEAD.addFeature("PN", "NULL");
	}	
	private Map<String, List<String>> features = new HashMap<>();
	private Map<String, List<WordFeatures>> parents = new HashMap<>();
	private Map<String, List<WordFeatures>> children = new HashMap<>();
	private Set<String> childRels = new HashSet<>();
	private Set<String> childRelsNoRefs = new HashSet<>();
	private int subtreeSize = 1;
	private boolean isSharedArg = false;
	
	// FEATURE METHODS
	public void addFeature(String feature, String value) {
		if(features.containsKey(feature)) {
			features.get(feature).add(value);
		} else {
			List<String> vals = new ArrayList<>();
			vals.add(value);
			features.put(feature, vals);
		}
	}
	public boolean hasFeature(String feature) {
		return features.containsKey(feature);
	}
	public List<String> getFeature(String feature) {
		return features.get(feature);
	}
	public String getUniqueFeature(String feature) {
		List<String> featList = features.get(feature);
		return (featList != null && featList.size() > 0) ? featList.get(0) : null;
	}	
	public Map<String, List<String>> getFeatures() {
		return features;
	}
	
	// PARENT AND CHILDREN METHODS
	public void addParent(String relation, WordFeatures parent) {
		if(parents.containsKey(relation)) {
			parents.get(relation).add(parent);
		} else {
			List<WordFeatures> vals = new ArrayList<>();
			vals.add(parent);
			parents.put(relation, vals);
		}
	}
	public Map<String, List<WordFeatures>> getParents() {
		return parents;
	}
	public void addChild(String relation, WordFeatures child) {
		if(children.containsKey(relation)) {
			children.get(relation).add(child);
		} else {
			List<WordFeatures> vals = new ArrayList<>();
			vals.add(child);
			children.put(relation, vals);
			childRels.add(relation);
			if(!relation.contains(LogicalForm.REF_MARKER))
				childRelsNoRefs.add(relation);
		}
	}
	public Map<String, List<WordFeatures>> getChildren() {
		return children;
	}
	public Set<String> getChildRels() {
		return childRels;
	}
	public Set<String> getChildRelsNoRefs() {
		return childRelsNoRefs;
	}
	//TODO Replace this method with WFUtil.getChildList at some point
	/** @return list of children not backed up by children map */
	public List<WordFeatures> getChildList() {
		List<WordFeatures> childList = new ArrayList<>();
		for(List<WordFeatures> list : children.values()) {
			childList.addAll(list);
		}
		return childList;
	}
	//TODO Move this method to WFUtil at some point
	public List<String> getArgumentChildNames() {
		List<String> argNames = new ArrayList<>();
		for(int i = 0;i <= 5;i++) {
			List<String> argINames = features.get("A" + i + "N");
			if(argINames != null)
				argNames.addAll(argINames);
		}
		return argNames;
	}
	
	// METHODS FOR OTHER FIELDS
	/** Adds specified amount to subtreeSize */
	public void updateSubtreeCount(int increase) {
		subtreeSize += increase;
	}
	public int getSubtreeSize() {
		return subtreeSize;
	}
	public void markAsSharedArg() {
		isSharedArg = true;
	}
	public boolean isSharedArg() {
		return isSharedArg;
	}
	
	// METHODS INHERITED FROM OBJECT
	public boolean equals(Object obj) {
		if(!(obj instanceof WordFeatures)) {
			return false;
		} else if(this == obj) {
			return true;
		}
		
		WordFeatures other = (WordFeatures) obj;
		String id = getUniqueFeature("id");
		String otherID = other.getUniqueFeature("id");
		
		if(id == null || otherID == null) {
			return false;
		}
		return id.equals(otherID);
	}
	public Object clone() {
		WordFeatures clone = new WordFeatures();
		clone.features = new HashMap<>(features);
		clone.parents = new HashMap<>(parents);
		clone.children = new HashMap<>(children);
		clone.childRels = new HashSet<>(childRels);
		clone.childRelsNoRefs = new HashSet<>(childRelsNoRefs);
		return clone;
	}
	public String toString() {
		String id = getUniqueFeature("id");
		id = (id == null) ? "" : id;
		String pred = getUniqueFeature("PN");
		pred = (pred == null) ? "X" : pred;
		return "feats(" + id + ":" + pred + ")";
	}
}
