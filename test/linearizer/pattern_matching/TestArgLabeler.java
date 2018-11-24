package linearizer.pattern_matching;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import feat_extract.LogicalForm;
import feat_extract.WFUtil;
import feat_extract.WordFeatures;
import linearizer.pattern_matching.SentTypeDeterminer.SentType;
import util.LFTestUtil;

public class TestArgLabeler {
	private static ArgLabeler uut = new ArgLabeler();
	private static ArgSeparator argSep = new ArgSeparator();
	
	/** First set of tests, to see if found patterns have merit 
	 * @throws Exception */
	public static void main(String[] args) throws Exception {
		List<LogicalForm> batch = LFTestUtil.getLFBatch(3, 3);
		for(LogicalForm lf : batch) {
			SentType sentType = SentTypeDeterminer.sentType(lf);
			if(sentType == null) {
				System.out.println("Skipping multi-root sentence: " + lf.getSentence());
				System.out.println();
				continue;
			}
			Map<String,WordFeatures> verbAndArgs = argSep.verbAndArgs(lf, sentType);
			WordFeatures subj = uut.getSubj(verbAndArgs, sentType);
			
			if(subj == null) {
				System.out.println("Unexpected lf format: " + lf.getSentence());
				System.out.println();
				continue;
			}
			System.out.println("Subject: " + WFUtil.subtreeStr(subj, new HashSet<>()));
			System.out.println("Sentence: " + lf.getSentence());
			System.out.println();
		}
	}
}
