package linearizer.pattern_matching;
import java.util.Map;

import feat_extract.WordFeatures;
import linearizer.pattern_matching.SentTypeDeterminer.SentType;

public class ArgLabeler {
	public WordFeatures getSubj(Map<String, WordFeatures> verbAndArgs, SentType sentType) {
		return LabelPatternUtil.getMinArg(verbAndArgs);
	}
}
