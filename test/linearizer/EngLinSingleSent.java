package linearizer;
import java.io.File;
import java.util.HashSet;
import java.util.List;

import org.jdom2.Element;

import feat_extract.LogicalForm;
import feat_extract.LogicalFormParser;
import feat_extract.WordInfoMap;
import feat_extract.XMLFileLoader;
import feat_print.AnnotSeqPrinter;
import linearizer.eng_heuristic.ChildOrders;

public class EngLinSingleSent {
	public static final String TEST_FILE = "/home/reid/projects/research/ccg/openccg/ccgbank/extract/test/test.xml";
	private static XMLFileLoader loader = new XMLFileLoader();
	private static EngLinearizer uut = new EngLinearizer();
	private static LinConfig config = new LinConfig(FeatOrders.ALL_REL, 0, false, ChildOrders.ENG);
	
	public static void main(String[] args) throws Exception {
		LogicalFormParser parser = new LogicalFormParser();
		AnnotSeqPrinter printer = new AnnotSeqPrinter();
		Element item = loader.parse(new File(TEST_FILE)).get(0);
		
		WordInfoMap wordInfo = new WordInfoMap(item);
		Element lf = item.getChild("lf");
		String sent_text = item.getAttributeValue("string");
		
		LogicalForm sentence = parser.parse(sent_text, lf, wordInfo);
		List<String> engOrder = uut.getOrder(sentence, sentence.getHead(), new HashSet<String>(), config);
		System.out.println(sent_text);
		System.out.println(printer.wordsInOrderOfID(sentence, engOrder));
	}
}
