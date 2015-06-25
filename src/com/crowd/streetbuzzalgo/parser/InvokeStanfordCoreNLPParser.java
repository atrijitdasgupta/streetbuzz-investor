/**
 * 
 */
package com.crowd.streetbuzzalgo.parser;

import java.util.List;
import java.util.Properties;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.CoreAnnotations.NamedEntityTagAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations.CollapsedCCProcessedDependenciesAnnotation;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.trees.LabeledScoredTreeNode;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations.TreeAnnotation;
import edu.stanford.nlp.util.CoreMap;

/**
 * @author Atrijit
 *
 */
public class InvokeStanfordCoreNLPParser {

	/**
	 * 
	 */
	public InvokeStanfordCoreNLPParser() {
		// TODO Auto-generated constructor stub
		String demo = "I don't know half of you half as well as I should like, and I like less than half of you half as well as you deserve";
		try {
			parse(demo);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void parse(String text)throws Exception{
		Properties props = new Properties();
		props.put("annotators", "tokenize, ssplit, pos, lemma, parse, sentiment");
		StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
		Annotation annotation = pipeline.process(text);
		List<CoreMap> sentences = annotation.get(CoreAnnotations.SentencesAnnotation.class);
		for (CoreMap sentence : sentences) {
		  String sentiment = sentence.get(SentimentCoreAnnotations.ClassName.class);
		  System.out.println(sentiment + "\t" + sentence);
		}
	}
	
	private void parseOld(String text)throws Exception{
		Properties props = new Properties();
		Tree tree = null;
		SemanticGraph dependencies = null;
	    //props.put("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref");
		props.put("annotators", "tokenize, ssplit, pos, lemma, parse, sentiment");
	    StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
	    Annotation document = new Annotation(text);
	    pipeline.annotate(document);
	    List<CoreMap> sentences = document.get(SentencesAnnotation.class);
	    for(CoreMap sentence: sentences) {
	    	for (CoreLabel token: sentence.get(TokensAnnotation.class)) {
	    		 String word = token.get(TextAnnotation.class);
	    		 String pos = token.get(PartOfSpeechAnnotation.class);
	    		 String ne = token.get(NamedEntityTagAnnotation.class);
	    	}
	    	tree = sentence.get(TreeAnnotation.class);
	    	dependencies = sentence.get(CollapsedCCProcessedDependenciesAnnotation.class);
	    	
	    }
	    List v = tree.getChildrenAsList();
	    for(int i=0;i<v.size();i++){
	    	LabeledScoredTreeNode o = (LabeledScoredTreeNode)v.get(i);
	    	System.out.println("jj "+o.pennString());
	    	List k = o.getLeaves();
	    	for(int j=0;j<k.size();j++){
	    		LabeledScoredTreeNode ob = (LabeledScoredTreeNode)k.get(j);
	    		System.out.println(ob.pennString());
	    	}
	    }
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		InvokeStanfordCoreNLPParser invokeStanfordCoreNLPParser = new InvokeStanfordCoreNLPParser();

	}

}
