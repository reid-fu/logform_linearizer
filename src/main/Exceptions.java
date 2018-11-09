package main;

@SuppressWarnings("serial")
public class Exceptions {
	public static class InvalidWordIDException extends Exception {
		public InvalidWordIDException(String sentence, String wordID) {
			super("Word ID " + wordID + " not found for sentence \"" + sentence + "\"");
		}
	}
	public static class NoMoodException extends Exception {
		public NoMoodException(String sentence) {
			super("\"" + sentence + "\" has no mood attribute");
		}
	}
	
	public static abstract class CCGXMLParseException extends Exception {
		private String sentence;
		private String reason;
		
		public CCGXMLParseException(String sentence, String reason) {
			this.sentence = sentence;
			this.reason = reason;
		}
		public void printSkipMessage() {
			System.err.println("Skipping sentence \"" + sentence + "\": " + reason);
		}
	}
	public static class NoPredicatesException extends CCGXMLParseException {
		public NoPredicatesException(String sentence) {
			super(sentence, "No predicates in sentence");
		}
	}
	public static class HeadNotPredicateException extends CCGXMLParseException {
		public HeadNotPredicateException(String sentence) {
			super(sentence, "Head word is not predicate");
		}
	}
	public static class NoRelationNameException extends CCGXMLParseException {
		public NoRelationNameException(String sentence, String relation) {
			super(sentence, "No relation name for " + relation);
		}
	}
}
