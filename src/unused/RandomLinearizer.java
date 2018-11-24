package unused;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import feat_extract.LogicalForm;
import feat_print.AnnotSeqPrinter;

public class RandomLinearizer extends AnnotSeqPrinter {	
	public RandomLinearizer(String featFile, String sentFile) throws FileNotFoundException {
		super(featFile, sentFile);
	}
	public void writeLF(LogicalForm sentence, String[] featOrder) {
		List<String> wordIDs = new ArrayList<>(sentence.getWordIds());
		Collections.shuffle(wordIDs);
		super.writeLF(sentence, featOrder, wordIDs);
	}
}
