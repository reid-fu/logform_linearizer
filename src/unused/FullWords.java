package unused;
import java.util.HashMap;
import java.util.Map;
import org.jdom2.Element;

public class FullWords {
	private Map<String, FullWordDescriptor> fullWords = new HashMap<>();
	
	public void parse(Element fullWords) {
		String fullWordsStr = fullWords.getText();
		String[] words = fullWordsStr.split(" ");
		for(String word : words) {
			if(!word.equals("<s>") && !word.equals("</s>")) {
				String[] fullWordArr = word.split(":");
				String wordText = fullWordArr[0];
				FullWordDescriptor fullWord = new FullWordDescriptor(fullWordArr[1], fullWordArr[2], fullWordArr[3], fullWordArr[4]);
				this.fullWords.put(wordText, fullWord);
			}
		}
	}
	public String getLemma(String wordText) {
		return fullWords.get(wordText).getLemma();
	}
	public String getPOS(String wordText) {
		return fullWords.get(wordText).getPos();
	}
	public String getSupertag(String wordText) {
		return fullWords.get(wordText).getSupertag();
	}
	public String getNamedEntityCategory(String wordText) {
		return fullWords.get(wordText).getNamedEntityCategory();
	}
}
