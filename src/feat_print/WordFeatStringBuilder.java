package feat_print;
import java.util.List;
import feat_extract.WordFeatures;

public class WordFeatStringBuilder {
	public static final String FEAT_SEP = "|";
	
	/** @return word feature string in format pred_name|supertag|feature1|...; order of features specified by featOrder */
	public String wordFeatString(WordFeatures features, String supertag, String[] featOrder) {
		String predName = features.getUniqueFeature("PN");
		if(predName != null && (predName.matches("[A-Z]?[a-z]+\\.[0-9]+") || predName.matches("[A-Z]?[a-z]+\\.XX"))) {
			predName = predName.substring(0, predName.indexOf("."));
		}
//		String wordID = features.getUniqueFeature("id");
		supertag = (supertag == null) ? "" : supertag;
		String wordFeatStr = predName + FEAT_SEP + supertag;
		
		for(String feat : featOrder) {
			if(feat.contains("#")) {
				wordFeatStr += poundFeatString(feat, features);
			} else {
				String featVal = features.getUniqueFeature(feat);
				wordFeatStr += FEAT_SEP;
				if(featVal != null) {
					wordFeatStr += featVal;
				}
			}
		}
		return wordFeatStr;
	}
	
	public String poundFeatString(String feat, WordFeatures features) {
		String[] vals = getInitVals(feat);
		feat = feat.substring(0, feat.indexOf("#"));
		
		if(feat.equals("AT")) {
			List<String> childRels = features.getFeature("CT");
			fillVals(vals, childRels, new StringCondition() {
				@Override
				public boolean match(String str) {
					return str.matches("Arg[0-9][a-z]?"); // Arg, ArgA, ArgM do not count as argument
				}
			});
		} else if(feat.equals("AN")) {
			List<String> argNames = features.getArgumentChildNames();
			fillVals(vals, argNames, null);	
		} else {
			List<String> children = features.getFeature(feat);
			fillVals(vals, children, null);
		}
		
		return resultString(vals);
	}
	private String[] getInitVals(String feat) {
		int numVals = Integer.parseInt(feat.substring(feat.indexOf("#") + 1));
		String[] vals = new String[numVals];
		for(int i = 0;i < vals.length;i++)
			vals[i] = "";
		return vals;
	}
	private String resultString(String[] vals) {
		String result = "";
		for(String val : vals) {
			result += FEAT_SEP + val;
		}
		return result;
	}
	
	public void fillVals(String vals[], List<String> valSrc, StringCondition condition) {
		if(valSrc == null)
			return;
		int valsI = 0;
		for(String val : valSrc) {
			if(val != null && (val.matches("[A-Z]?[a-z]+\\.[0-9]+") || val.matches("[A-Z]?[a-z]+\\.XX"))) {
				val = val.substring(0, val.indexOf("."));
			}
			if(condition == null || condition.match(val)) {
				vals[valsI] = val;
				valsI++;
				if(valsI >= vals.length)
					break;
			}
		}
	}
}
