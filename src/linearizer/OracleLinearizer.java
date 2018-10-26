package linearizer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import feat_extract.LogicalForm;
import feat_extract.WordFeatures;

public class OracleLinearizer extends Linearizer {
	//TODO: Can't add parentheses in properly yet
	@Override
	public List<String> order(LogicalForm lf, WordFeatures current, Set<String> visited, LinConfig config) {
		List<String> wordIDs = new ArrayList<>(lf.getWordIds());
		// Remove all x# and h# word ID's
		for(int i = 0;i < wordIDs.size();i++) {
			if(!wordIDs.get(i).matches("w[0-9]*")) {
				wordIDs.remove(i);
				i--;
			}
		}
		
		Collections.sort(wordIDs, new Comparator<String>() {
			@Override
			public int compare(String arg0, String arg1) {
				int num0 = Integer.parseInt(arg0.substring(1));
				int num1 = Integer.parseInt(arg1.substring(1));
				return num0 - num1;
			}
		});
		return wordIDs;
	}
}
