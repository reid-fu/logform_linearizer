package main;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.jdom2.*;

import feat_extract.*;
import unused.LogicalFormParser;

public class ConvertMain {
	public static final String DATA_DIR = "/home/reid/projects/research/ccg/openccg/ccgbank/extract/test/test";
	public static final String OUTPUT_FILE = "/home/reid/projects/research/ccg/openccg/ccgbank/extract/test/test/test.feats";
	
	public static void main(String[] args) throws IOException, JDOMException {
		List<Element> data = new LinkedList<>();
		// Load data from files
		File dir = new File(DATA_DIR);
		File[] subdirs = dir.listFiles();
		XMLFileLoader parser = new XMLFileLoader();
		for(File subdir : subdirs) {
			if(subdir.isDirectory()) {
				addDataInDir(parser, subdir, data);
			} else if(subdir.getAbsolutePath().endsWith("xml")) {
				List<Element> fileData = parser.parse(subdir);
				data.addAll(fileData);
			}
		}
		// Convert data to features and output
		List<LogicalFormParser> cnvt = convertDataToFeatures(data);
		writeConvertedData(cnvt);
	}
	public static void addDataInDir(XMLFileLoader parser, File dir, List<Element> data) throws JDOMException, IOException {
		for(File file : dir.listFiles()) {
			List<Element> fileData = parser.parse(file);
			data.addAll(fileData);
		}
	}
	public static List<LogicalFormParser> convertDataToFeatures(List<Element> data) {
		List<LogicalFormParser> cnvt = new LinkedList<>();
		for(Element item : data) {
			try {
				Element predInfo = item.getChild("pred-info");
				Element lf = item.getChild("lf");
				String sent_text = item.getAttributeValue("string");
				WordInfoMap wordInfo = new WordInfoMap(sent_text);
				
				wordInfo.parse(predInfo);
				LogicalFormParser sentence = new LogicalFormParser();
				sentence.parseFromXML(sent_text, lf, wordInfo);
				cnvt.add(sentence);
			} catch (Exception e) {
				//Skip sentence and print error
				e.printStackTrace();
			}
		}
		return cnvt;
	}
	public static void writeConvertedData(List<LogicalFormParser> cnvt) throws IOException {
		PrintWriter writer = new PrintWriter(OUTPUT_FILE);
		for(LogicalFormParser lf : cnvt) {
			Set<String> wordIds = lf.getWordIds();
			for(String id : wordIds) {
				String supertag = lf.getSupertag(id);
				Map<String, List<String>> features = lf.getWordFeatures(id).getFeatures();
				writer.println(supertag + " " + features);
			}
		}
		writer.close();
	}
	
	// Unused methods
	public static void writeData(List<Element> data) throws IOException {
		PrintWriter writer = new PrintWriter(OUTPUT_FILE);
		int i = 0;
		for(Element sentence : data) {
			i++;
			writer.println("SENTENCE " + i);
			writeNodeAndChildren(writer, sentence, 0);
		}
		writer.close();
	}
	public static void writeNodeAndChildren(PrintWriter writer, Element node, int numTabs) {
		writeTabs(writer, numTabs);
		writer.print(node.getName());
		String attrStr = "{";
		for(Attribute attr : node.getAttributes()) {
			attrStr += attr.getName() + "=" + attr.getValue() + ", ";
		}
		if(!attrStr.equals("{")) {
			attrStr = attrStr.substring(0, attrStr.length()-2) + "}";
			writer.println(attrStr);
		}
		
		for(Element child : node.getChildren()) {
			writeNodeAndChildren(writer, child, numTabs+1);
		}
		writer.println();
	}
	public static void writeTabs(PrintWriter writer, int numTabs) {
		for(int i = 0;i < numTabs;i++) {
			writer.print("  ");
		}
	}
}
