package linearizer;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import feat_print.AnnotSeqPrinter;
import linearizer.eng_heuristic.ChildOrders;
import feat_extract.LogicalForm;
import util.LFTestUtil;

public class BLEUBatchTest {
	public static final String TEST_DIR = "/home/reid/projects/research/ccg/openccg/ccgbank/extract/test/00";
	public static final String SCRIPT_PATH = "scripts/bleu.py";
	private static AnnotSeqPrinter printer = new AnnotSeqPrinter();
	private static OracleLinearizer oracle = new OracleLinearizer();
	
	public static void main(String[] args) throws Exception {
		List<String>[] batch = refTestBatchEngLin();
		System.out.println("Num sentences: " + batch[0].size());
		System.out.println("Avg BLEU: " + batchBLEU(batch[0], batch[1]));
	}
	/** @return First element is list of reference sentences. Second element is list of candidate sentences. */
	@SuppressWarnings("unchecked")
	public static List<String>[] refTestBatchEngLin() throws Exception {
		List<LogicalForm> batch = LFTestUtil.getLFBatch(1, 3);
		EngLinearizer englin = new EngLinearizer();
		LinConfig config = new LinConfig(FeatOrders.ALL_REL, 0, false, ChildOrders.ENG);
		List<String> refBatch = new LinkedList<>();
		List<String> testBatch = new LinkedList<>();
		
		for(LogicalForm lf : batch) {
			List<String> oracleOrder = oracle.getOrder(lf, lf.getHead(), new HashSet<String>(), config);
			List<String> engOrder = englin.getOrder(lf, lf.getHead(), new HashSet<String>(), config);
			String refSent = printer.wordsInOrderOfID(lf, oracleOrder);
			String testSent = printer.wordsInOrderOfID(lf, engOrder);
			refBatch.add(refSent);
			testBatch.add(testSent);
		}
		return new List[] {refBatch, testBatch};
	}
	public static double sentenceBLEU(String refSent, String testSent) throws IOException {
		Process p = new ProcessBuilder().command("python3", SCRIPT_PATH, "\"" + refSent + "\"", "\"" + testSent + "\"").start();
		BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
		String line = br.readLine();
		printError(p);
		return Double.parseDouble(line);
	}
	public static void printError(Process proc) throws IOException {
		BufferedReader br2 = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
		String line2 = null;
		while((line2 = br2.readLine()) != null) {
			System.out.println(line2);
		}
	}
	public static double batchBLEU(List<String> refBatch, List<String> testBatch) throws Exception {
		double bleu = 0;
		for(int i = 0;i < refBatch.size();i++) {
			bleu += sentenceBLEU(refBatch.get(i), testBatch.get(i));
		}
		return bleu / refBatch.size();
	}
}
