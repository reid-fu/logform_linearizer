package unused;

public class FullWordDescriptor {
	private String lemma;
	private String pos;
	private String supertag;
	private String namedEntityCategory;
	
	public FullWordDescriptor(String lemma, String pos, String supertag, String namedEntityCategory) {
		this.lemma = lemma;
		this.pos = pos;
		this.supertag = supertag;
		this.namedEntityCategory = namedEntityCategory;
	}
	public String getLemma() {
		return lemma;
	}
	public String getPos() {
		return pos;
	}
	public String getSupertag() {
		return supertag;
	}
	public String getNamedEntityCategory() {
		return namedEntityCategory;
	}
}