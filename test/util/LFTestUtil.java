package util;
import java.io.File;
import java.util.Arrays;
import java.util.LinkedList;
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
		return getLF(item);
	}
	public static LogicalForm getLF(Element item) throws Exception {
		Element lf = item.getChild("lf");
		String sentence = item.getAttributeValue("string");
		WordInfoMap wordInfo = new WordInfoMap(item);
		return parser.parse(sentence, lf, wordInfo);
	}
	public static List<Element> getXMLBatch(int startFileNum, int endFileNum) throws Exception {
		String dir = FILE_PREFIX.substring(0, FILE_PREFIX.lastIndexOf('/'));
		List<Element> batch = new LinkedList<>();
		File[] files = new File(dir).listFiles();
		Arrays.sort(files);
		
		for(int i = startFileNum-1;i < endFileNum;i++) {
			File f = files[2*i + 1];
			List<Element> lfs = loader.parse(f);
			batch.addAll(lfs);
		}
		return batch;
	}
	public static List<LogicalForm> getLFBatch(int startFileNum, int endFileNum) throws Exception {
		List<Element> xmlBatch = getXMLBatch(startFileNum, endFileNum);
		List<LogicalForm> batch = new LinkedList<>();
		
		for(Element item : xmlBatch) {
			LogicalForm lf = getLF(item);
			batch.add(lf);
		}
		return batch;
	}
}
