package linearizer.pattern_matching;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jdom2.Element;

import feat_extract.LogicalForm;
import feat_extract.LogicalFormParser;
import feat_extract.WordFeatures;
import feat_extract.WordInfoMap;
import feat_extract.XMLFileLoader;

public class PatternSingleSent {
	public static final String TEST_FILE = "/home/reid/projects/research/ccg/openccg/ccgbank/extract/test/test.xml";
	private static XMLFileLoader loader = new XMLFileLoader();
	private static ArgSeparator uut = new ArgSeparator();
	
	public static void main(String[] args) throws Exception {
		LogicalFormParser parser = new LogicalFormParser();
		Element item = loader.parse(new File(TEST_FILE)).get(0);
		
		WordInfoMap wordInfo = new WordInfoMap(item);
		Element lf = item.getChild("lf");
		String sent_text = item.getAttributeValue("string");
		
		LogicalForm sentence = parser.parse(sent_text, lf, wordInfo);
		Map<String,WordFeatures> verbArgs = uut.verbAndArgs(sentence);
		Set<WordFeatures> visited = new HashSet<>();
		
		for(String role : verbArgs.keySet()) {
			if(role.equals(ArgSeparator.VERB_KEY) || role.equals(ArgSeparator.AUX_VERB_KEY)) {
				System.out.println(role + ": " + verbArgs.get(role).getUniqueFeature("PN"));
				visited.add(verbArgs.get(role));
			} else {
				System.out.println(role + ": " + argStr(verbArgs.get(role), visited));
			}
		}
	}
	public static String argStr(WordFeatures arg, Set<WordFeatures> visited) {
		String str = arg.getUniqueFeature("PN");
		List<WordFeatures> queue = new ArrayList<>(arg.getChildList());
		
		while(!queue.isEmpty()) {
			WordFeatures descendant = queue.remove(0);
			if(visited.contains(descendant)) {
				continue;
			} else {
				visited.add(descendant);
			}
			
			str += ", " + descendant.getUniqueFeature("PN");
			queue.addAll(descendant.getChildList());
		}
		return str;
	}
}
