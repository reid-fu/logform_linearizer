package util;
import java.io.File;
import java.util.List;
import org.jdom2.Element;

import feat_extract.LogicalForm;
import feat_extract.LogicalFormParser;
import feat_extract.WordInfoMap;
import feat_extract.XMLFileLoader;

public class LFTestUtil {
	private static final String FILE_PREFIX = "/home/reid/projects/research/ccg/openccg/ccgbank/extract/test/00/wsj_00";
	private static final XMLFileLoader loader = new XMLFileLoader();
	private static LogicalFormParser parser = new LogicalFormParser();
	
	public static Element loadLF(int fileNum, int id) throws Exception {
		String numStr = (fileNum < 10) ? "0" + fileNum : "" + fileNum;
		List<Element> lfs = loader.parse(new File(FILE_PREFIX + numStr + ".xml"));
		return lfs.get(id - 1);
	}
	public static LogicalForm getLF(int fileNum, int id) throws Exception {
		Element item = loadLF(fileNum, id);
		Element lf = item.getChild("lf");
		String sentence = item.getAttributeValue("string");
		WordInfoMap wordInfo = new WordInfoMap(item);
		return parser.parse(sentence, lf, wordInfo);
	}
}
