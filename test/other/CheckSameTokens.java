package other;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

/** Dev should have 36247 tokens
 * Train should have 680705 tokens
 * Test should have 42844 tokens
 */
public class CheckSameTokens {
	static String file1 = "output/dev_hypertagger";
	static String file2 = "output/allrel_oracle/dev_hypertagger";
	
	public static void main(String[] args) throws FileNotFoundException {
		Scanner s1 = new Scanner(new File(file1));
		Scanner s2 = new Scanner(new File(file2));
		
		if(file1.contains("sents") && file2.contains("sents")) {
			checkSentsFiles(s1, s2);
		} else {
			checkDataFiles(s1, s2);
		}
		
		s1.close();
		s2.close();
	}
	public static void checkSentsFiles(Scanner s1, Scanner s2) {		
		int lineNum = 0, tokenCount1 = 0, tokenCount2 = 0;
		while(s1.hasNext() && s2.hasNext()) {
			lineNum++;
			String line1 = s1.nextLine();
			String line2 = s2.nextLine();
			
			if(lineNum % 2 != 0) {
				line1 = line1.substring(1, line1.length()-1);
				line2 = line2.substring(1, line2.length()-1);
				Set<String> set1 = new HashSet<>(Arrays.asList(line1.split(", ")));
				Set<String> set2 = new HashSet<>(Arrays.asList(line2.split(", ")));
				
				if(!set1.equals(set2)) {
					System.out.println("Line " + lineNum + " different tokens");
				}
				tokenCount1 += set1.size();
				tokenCount2 += set2.size();
			}
		}		
		System.out.println("Token count for file1: " + tokenCount1);
		System.out.println("Token count for file2: " + tokenCount2);		
	}
	public static void checkDataFiles(Scanner s1, Scanner s2) {
		int lineNum = 0, tokenCount1 = 0, tokenCount2 = 0;
		while(s1.hasNext() && s2.hasNext()) {
			lineNum++;
			String line1 = s1.nextLine();
			String line2 = s2.nextLine();
			List<String> list1 = tokens(line1);
			List<String> list2 = tokens(line2);
			Set<String> set1 = new HashSet<>(list1);
			Set<String> set2 = new HashSet<>(list2);
			
			if(!set1.equals(set2) || list1.size() != list2.size()) {
				System.out.println("Line " + lineNum + " different tokens");
			}
			tokenCount1 += list1.size();
			tokenCount2 += list2.size();
		}
		System.out.println("Token count for file1: " + tokenCount1);
		System.out.println("Token count for file2: " + tokenCount2);
	}
	private static List<String> tokens(String line) {
		List<String> tokens = new ArrayList<>();
		String[] groups = line.split(" ");
		for(String group : groups) {
			String token = group.split("\\|")[0];
			if(!token.equals("(") && !token.equals(")"))
				tokens.add(token);
		}
		return tokens;
	}
}
