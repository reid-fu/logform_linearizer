package linearizer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import org.jdom2.Element;
import org.junit.jupiter.api.Test;

import feat_extract.LogicalForm;
import feat_extract.LogicalFormParser;
import feat_extract.WordInfoMap;
import feat_extract.XMLFileLoader;
import feat_print.AnnotSeqPrinter;
import linearizer.eng_heuristic.ChildOrders;
import main.Exceptions.CCGXMLParseException;

public class EngLinIntegTest {
	public static final String TEST_DIR = "/home/reid/projects/research/ccg/openccg/ccgbank/extract/test/00";
	private XMLFileLoader loader = new XMLFileLoader();
	private EngLinearizer uut = new EngLinearizer();
	private OracleLinearizer ref = new OracleLinearizer();
	private LinConfig config = new LinConfig(FeatOrders.ALL_REL, 0, false, ChildOrders.ENG);
	
	@Test
	public void numMatches() throws Exception {
		List<Element> logForms = loader.logicalForms(TEST_DIR);
		LogicalFormParser parser = new LogicalFormParser();
		AnnotSeqPrinter printer = new AnnotSeqPrinter();
		int exactMatches = 0;
		List<String> nonmatches = new ArrayList<>();
		
		for(Element item : logForms) {
			try {
				WordInfoMap wordInfo = new WordInfoMap(item);
				Element lf = item.getChild("lf");
				String sent_text = item.getAttributeValue("string");
				
				LogicalForm sentence = parser.parse(sent_text, lf, wordInfo);
				List<String> engOrder = uut.getOrder(sentence, sentence.getHead(), new HashSet<String>(), config);
				List<String> oracleOrder = ref.getOrder(sentence, sentence.getHead(), new HashSet<String>(), config);
				
				if(engOrder.equals(oracleOrder)) {
					exactMatches++;
				} else if(nonmatches.size() < 50) {
					nonmatches.add(sent_text + "\t\t" + printer.wordsInOrderOfID(sentence, engOrder));
				}
			} catch (CCGXMLParseException e) {
				e.printSkipMessage();
			}
		}
		System.out.println("Exact matches: " + exactMatches);
		@SuppressWarnings("unused")
		int numPrinted = 0;
		for(String nonmatch : nonmatches.subList(30, 50)) {
			System.out.println(nonmatch);
			numPrinted++;
		}
	}
}
