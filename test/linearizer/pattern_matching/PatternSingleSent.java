package linearizer.pattern_matching;
import java.io.File;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.jdom2.Element;

import feat_extract.LogicalForm;
import feat_extract.LogicalFormParser;
import feat_extract.WFUtil;
import feat_extract.WordFeatures;
import feat_extract.WordInfoMap;
import feat_extract.XMLFileLoader;
import linearizer.arg_separate.ArgSeparator;
import linearizer.pattern_matching.SentTypeDeterminer.SentType;
import main.Exceptions;

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
		SentType sentType = SentTypeDeterminer.sentType(sentence);
		if(sentType == null)
			throw new Exceptions.NoMoodException(sent_text);
		Map<String,WordFeatures> verbArgs = uut.verbAndArgs(sentence);
		Set<WordFeatures> visited = new HashSet<>();
		
		for(String role : verbArgs.keySet()) {
			if(role.equals(ArgSeparator.VERB_KEY) || role.equals(ArgSeparator.AUX_VERB_KEY)) {
				System.out.println(role + ": " + verbArgs.get(role).getUniqueFeature("PN"));
				visited.add(verbArgs.get(role));
			} else {
				System.out.println(role + ": " + WFUtil.subtreeStr(verbArgs.get(role), visited));
			}
		}
	}
}
