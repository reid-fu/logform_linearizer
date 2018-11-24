package linearizer;
import java.util.List;
import java.util.Set;

import feat_extract.LogicalForm;
import feat_extract.WordFeatures;
import linearizer.pattern_matching.*;

public class TopDownLinearizer extends Linearizer {
	private ArgSeparator argSep = new ArgSeparator();
	private ArgLabeler argLabel = new ArgLabeler();
	
	@Override
	public List<String> order(LogicalForm lf, WordFeatures current, Set<String> visited, LinConfig config) {
		// TODO Auto-generated method stub
		return null;
	}
}
