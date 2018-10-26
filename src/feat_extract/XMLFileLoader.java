package feat_extract;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom2.*;
import org.jdom2.input.SAXBuilder;

public class XMLFileLoader {
	private SAXBuilder saxBuilder = new SAXBuilder();
	
	public List<Element> parse(File file) throws JDOMException, IOException {
		Document document = saxBuilder.build(file);
		return document.getRootElement().getChildren();
	}
	public static Map<String,Element> getIdNodeMap(Element item) {
		Map<String,Element> idMap = new HashMap<>();
		//TODO Implement later if necessary
		return idMap;
	}
}
