package feat_extract;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.jdom2.*;
import org.jdom2.input.SAXBuilder;

public class XMLFileLoader {
	private SAXBuilder saxBuilder = new SAXBuilder();
	
	public List<Element> parse(File file) throws JDOMException, IOException {
		Document document = saxBuilder.build(file);
		return document.getRootElement().getChildren();
	}
	/** @return Logical forms in files in dir 
	 * @throws IOException 
	 * @throws JDOMException */
	public List<Element> logicalForms(String dir) throws JDOMException, IOException {
		File[] files = new File(dir).listFiles();
		Arrays.sort(files);
		List<Element> logForms = new ArrayList<>();
		
		for(File f : files) {
			if(f.getName().endsWith(".xml")) {
				logForms.addAll(parse(f));
			}
		}
		return logForms;
	}
}
