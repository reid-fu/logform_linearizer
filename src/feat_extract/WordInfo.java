package feat_extract;

/** Contains supertag, POS, and lemma for some word */
public class WordInfo {
	private String supertag;
	private String pos;
	private String lemma;
	
	public WordInfo(String supertag, String pos, String lemma) {
		this.supertag = supertag;
		this.pos = pos;
		this.lemma = lemma;
	}
	public String getSupertag() {
		return supertag;
	}
	public String getPos() {
		return pos;
	}
	public String getLemma() {
		return lemma;
	}
	public String toString() {
		return "(" + lemma + ", " + pos + ", " + supertag + ")";
	}
}
