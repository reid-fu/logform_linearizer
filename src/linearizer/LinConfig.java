package linearizer;

import linearizer.eng_heuristic.ChildOrders;

/** Linearization configuration object */
public class LinConfig {
	private String featOrderName;
	private int parenSubtreeSize;
	private boolean relSeq;
	private String childOrderName;
	
	public LinConfig(String featOrderName, int parenSubtreeSize, boolean relSeq, String childOrderName) {
		this.featOrderName = featOrderName;
		this.parenSubtreeSize = parenSubtreeSize;
		this.relSeq = relSeq;
		this.childOrderName = childOrderName;
	}
	public String featOrderName() {
		return featOrderName;
	}
	public String[] featOrder() {
		return FeatOrders.getFeatOrder(featOrderName);
	}
	public int parenSubtreeSize() {
		return parenSubtreeSize;
	}
	public boolean relSeq() {
		return relSeq;
	}
	public String childOrderName() {
		if(parenSubtreeSize > 0) {
			return childOrderName + "paren";
		}
		return childOrderName;
	}
	public String[] childOrder() {
		return ChildOrders.getChildOrder(childOrderName);
	}
	public Linearizer getLinearizer() {
		if(childOrderName.equals(ChildOrders.ENG)) {
			return new EngLinearizer();
		} else if(childOrderName.equals(ChildOrders.ORACLE)) {
			return new OracleLinearizer();
		}
		return null;
	}
	public String toString() {
		return "(" + featOrderName + ", " + parenSubtreeSize + ", " + relSeq + ", " + childOrderName + ")";
	}
}
