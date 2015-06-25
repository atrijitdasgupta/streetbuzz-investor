/**
 * 
 */
package com.crowd.streetbuzz.helper;

import java.util.Map;

import edu.stanford.nlp.pipeline.StanfordCoreNLP;

/**
 * @author Atrijit
 *
 */
public class Test {

	/**
	 * 
	 */
	public Test() {
		// TODO Auto-generated constructor stub
	}
	
	private static StanfordCoreNLP getPipe(){
		ParserResourceHelper prh = ParserResourceHelper.getInstance();
		Map resourcesMap = prh.getResourcesMap();
		StanfordCoreNLP pipeline = (StanfordCoreNLP)resourcesMap.get("stanfordpipe");
		return pipeline;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		StanfordCoreNLP lp = getPipe();
		System.out.println("2. lp: "+lp);
	}

}
