package feat_extract;
import org.jdom2.Element;

public class Relation {
	private Element childXML;
	private WordFeatures parentFeats;
	private String name;
	
	public Relation(Element childXML, WordFeatures parentFeats, String name) {
		this.childXML = childXML;
		this.parentFeats = parentFeats;
		this.name = name;
	}
	public Element getChildXML() {
		return childXML;
	}
	public WordFeatures getParentFeats() {
		return parentFeats;
	}
	public String getName() {
		return name;
	}
	public String toString() {
		String parentPred = parentFeats.getUniqueFeature("PN");
		String childPred = childXML.getAttributeValue("pred");
		return "{" + name + "," + parentPred + "," + childPred + "}";
	}
}
