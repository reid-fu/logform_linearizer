package linearizer.pattern_matching;
import java.util.List;
import main.Exceptions.NoMoodException;
import feat_extract.LogicalForm;
import feat_extract.WFUtil;
import feat_extract.WordFeatures;

public class SentTypeDeterminer {
	/** DECLARATIVE stands for declarative statement;
	 * WH_QUESTION stands for question that starts with wh-phrase such as what or how;
	 * YN_QUESTION stands for yes-no question;
	 * IMPERATIVE stands for imperative statement */
	public static enum SentType {DECLARATIVE, QUESTION, IMPERATIVE};
	
	/** @param lf LogicalForm representing sentence
	 * @return Type of sentence (declarative, question, or imperative). Null if lf has multiple roots.
	 * @throws NoMoodException if lf root has no "mood" attribute
	 */
	public static SentType sentType(LogicalForm lf) throws NoMoodException {
		List<WordFeatures> roots = WFUtil.getChildList(lf.getHead(), true);
		if(roots.size() != 1)
			return null;
		WordFeatures root = roots.get(0);
		
		String mood = root.getUniqueFeature("ZM");
		return sentType(mood, lf.getSentence());
	}
	private static SentType sentType(String mood, String sentence) throws NoMoodException {
		if(mood == null) {
			throw new NoMoodException(sentence);
		} else if(mood.equals("dcl")) {
			return SentType.DECLARATIVE;
		} else if(mood.equals("imp")) {
			return SentType.IMPERATIVE;
		} else {
			return SentType.QUESTION;
		}
	}
}
