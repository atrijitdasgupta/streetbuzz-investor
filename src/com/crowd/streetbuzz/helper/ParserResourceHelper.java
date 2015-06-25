/**
 * 
 */
package com.crowd.streetbuzz.helper;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.crowd.streetbuzz.common.Constants;

import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;

/**
 * @author Atrijit
 * 
 */
public class ParserResourceHelper implements Constants {
	private static ParserResourceHelper parserResourceHelper = new ParserResourceHelper();

	private static HashMap resourcesMap = new HashMap();

	/**
	 * 
	 */
	private ParserResourceHelper() {
		if(resourcesMap==null){
			System.out.println("null map");
			resourcesMap = new HashMap();
		}
		loadResources(resourcesMap);
	}

	public static synchronized ParserResourceHelper getInstance(){
		if(resourcesMap==null){
			System.out.println("Found null HashMap in ParserResourceHelper le gasp!!");
			resourcesMap = new HashMap();
			loadResources(resourcesMap);
		}
		return parserResourceHelper;
	}

	private static void loadResources(Map resourcesMap) {
		
		// For sentiment analysis
		Properties props = new Properties();
		props.setProperty("annotators", "tokenize, ssplit, parse, sentiment");
		StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
		System.out.println("pipeline: "+pipeline);
		System.out.println("resourcesMap: "+resourcesMap);
		resourcesMap.put("stanfordpipe", pipeline);

		// For named entities
		String serializedClassifier = BASEEXTLIBPATH
				+ "stanfordcorelib/english.all.3class.distsim.crf.ser.gz";
		AbstractSequenceClassifier<CoreLabel> classifier = CRFClassifier
				.getClassifierNoExceptions(serializedClassifier);
		resourcesMap.put("classifier", classifier);
		
		// For basic dependency parsing
		String grammar = BASEEXTLIBPATH+"stanfordnlp/englishPCFG.ser.gz";
		String[] options = { "-maxLength", "80", "-retainTmpSubcategories" };
		LexicalizedParser lp = LexicalizedParser.loadModel(grammar, options);
		resourcesMap.put("lexical", lp);

	}

	public static HashMap getResourcesMap() {
		return resourcesMap;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
