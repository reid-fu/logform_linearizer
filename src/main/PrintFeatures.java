package main;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.jdom2.Element;
import org.jdom2.JDOMException;

import feat_extract.*;
import feat_print.AnnotSeqPrinter;
import linearizer.*;
import main.Exceptions.CCGXMLParseException;

public class PrintFeatures {
	public static final String DATA_DIR = "/home/reid/projects/research/ccg/openccg/ccgbank/extract/test";
	public static final String[] OUTPUT_FILES = {"output/train_hypertagger", "output/dev_hypertagger", "output/test_hypertagger"};
	public static final String[] SENT_FILES = {"output/train_sents.txt", "output/dev_sents.txt", "output/test_sents.txt"};
	public static final String[] ORIG_SENT_FILES = {"output/orig_train_sents.txt", "output/orig_dev_sents.txt", "output/orig_test_sents.txt"};
	
	public static final String[] TRAIN_PART = {"02","03","04","05","06","07","08",
			"09","10","11","12","13","14","15","16","17","18","19","20","21"};
	public static final String[] DEV_PART = {"00"};
	public static final String[] TEST_PART = {"23"};
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void main(String[] args) throws JDOMException, IOException {
		List<Element> trainData = new LinkedList<>();
		List<Element> devData = new LinkedList<>();
		List<Element> testData = new LinkedList<>();
		
		File dir = new File(DATA_DIR);
		File[] subdirs = dir.listFiles();
		XMLFileLoader parser = new XMLFileLoader();
		
		for(File subdir : subdirs) {
			String dirName = subdir.getName();
			if(Arrays.asList(TRAIN_PART).contains(dirName)) {
				addDataInDir(parser, subdir, trainData);
			} else if(Arrays.asList(DEV_PART).contains(dirName)) {
				addDataInDir(parser, subdir, devData);
			} else if(Arrays.asList(TEST_PART).contains(dirName)) {
				addDataInDir(parser, subdir, testData);
			}
		}
		
		List[] data = {trainData, devData, testData};
		LinConfig config = new LinConfig(FeatOrders.ALL_REL, 5, false, ChildOrders.ENG);
		Linearizer linearizer = config.getLinearizer();
		writeFeatures(data, linearizer, config);
	}
	public static void addDataInDir(XMLFileLoader parser, File dir, List<Element> data) 
			throws JDOMException, IOException {
		for(File file : dir.listFiles()) {
			if(!file.getName().endsWith(".xml")) {
				continue;
			}
			List<Element> fileData = parser.parse(file);
			data.addAll(fileData);
		}
	}
	public static void writeFeatures(List<Element>[] data, Linearizer linearizer, LinConfig config) throws IOException {
		for(int i = 0;i < OUTPUT_FILES.length;i++) {
			AnnotSeqPrinter printer = new AnnotSeqPrinter(OUTPUT_FILES[i], SENT_FILES[i]);
			Set<String> uniqueSentences = new HashSet<>();
			int numSkipped = 0, numDuplicates = 0;
			PrintWriter writer = new PrintWriter(ORIG_SENT_FILES[i]);
			
			for(Element item : data[i]) {
				try {
					Element predInfo = item.getChild("pred-info");
					Element lf = item.getChild("lf");
					String sent_text = item.getAttributeValue("string");
					if(!uniqueSentences.add(sent_text))
						numDuplicates++;
					WordInfoMap wordInfo = new WordInfoMap(sent_text);					
					wordInfo.parse(predInfo);
					
					LogicalFormParser parser = new LogicalFormParser();
					LogicalForm sentence = parser.parse(sent_text, lf, wordInfo);
					List<String> linOrder = linearizer.getOrder(sentence, sentence.getHead(), new HashSet<String>(), config);
					printer.writeLF(sentence, config.featOrder(), linOrder);
					writer.println(sent_text);
				} catch (CCGXMLParseException e) {
					numSkipped++;
					e.printSkipMessage();
				}
			}
			
			System.out.println("Sentences skipped: " + numSkipped);
			System.out.println("Duplicate sentences: " + numDuplicates);
			System.out.println("Unique sentences: " + uniqueSentences.size());
			printer.close();
			writer.close();
		}
	}
}
