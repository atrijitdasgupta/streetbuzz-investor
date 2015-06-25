/**
 * 
 */
package com.crowd.streetbuzzalgo.parser;

import java.util.Properties;

import com.crowd.streetbuzz.model.Voices;
import com.crowd.streetbuzzalgo.crawler.vo.CrawlerVO;
import com.crowd.streetbuzzalgo.vo.SearchVO;
import com.crowd.streetbuzzalgo.vo.YoutubeVO;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.CoreMap;

/**
 * @author Atrijit
 * 
 */
public class PipeLineSingle {
	private static volatile PipeLineSingle instance = null;

	private static StanfordCoreNLP pipeline = null;

	/**
	 * 
	 */
	private PipeLineSingle() {

	}

	public static PipeLineSingle getInstance() {
		if (instance == null) {
			synchronized (PipeLineSingle.class) {
				// Double check
				if (instance == null) {
					instance = new PipeLineSingle();
					getPipe();
				}
			}
		}
		return instance;
	}

	private static StanfordCoreNLP getPipe() {
		Properties props = new Properties();
		props.setProperty("annotators", "tokenize, ssplit, parse, sentiment");
		pipeline = new StanfordCoreNLP(props);
		return pipeline;

	}
	
	public static void analyseStringSentiment(String line){
		Annotation annotation = pipeline.process(line);
		for (CoreMap sentence : annotation
				.get(CoreAnnotations.SentencesAnnotation.class)) {
			Tree tree = sentence
					.get(SentimentCoreAnnotations.AnnotatedTree.class);
			int sentimentscore = RNNCoreAnnotations.getPredictedClass(tree);
			String sentimentStr = sentence
					.get(SentimentCoreAnnotations.ClassName.class);
			

		}
	}
	public static SearchVO analyse(SearchVO svo, String line) {
		Annotation annotation = pipeline.process(line);
		for (CoreMap sentence : annotation
				.get(CoreAnnotations.SentencesAnnotation.class)) {
			Tree tree = sentence
					.get(SentimentCoreAnnotations.AnnotatedTree.class);
			int sentimentscore = RNNCoreAnnotations.getPredictedClass(tree);
			String sentimentStr = sentence
					.get(SentimentCoreAnnotations.ClassName.class);
			svo.setSentiment(sentimentStr);
			svo.setSentimentscore(sentimentscore);

		}
		return svo;
	}

	public static CrawlerVO analyse(CrawlerVO cvo, String line) {
		Annotation annotation = pipeline.process(line);
		for (CoreMap sentence : annotation
				.get(CoreAnnotations.SentencesAnnotation.class)) {
			Tree tree = sentence
					.get(SentimentCoreAnnotations.AnnotatedTree.class);
			int sentimentscore = RNNCoreAnnotations.getPredictedClass(tree);
			String sentimentStr = sentence
					.get(SentimentCoreAnnotations.ClassName.class);
			cvo.setSentimentstr(sentimentStr);
			cvo.setSentimentscore(sentimentscore);

		}
		return cvo;
	}

	public static YoutubeVO analyse(YoutubeVO ytv, String line) {
		Annotation annotation = pipeline.process(line);
		for (CoreMap sentence : annotation
				.get(CoreAnnotations.SentencesAnnotation.class)) {
			Tree tree = sentence
					.get(SentimentCoreAnnotations.AnnotatedTree.class);
			int sentimentscore = RNNCoreAnnotations.getPredictedClass(tree);
			String sentimentStr = sentence
					.get(SentimentCoreAnnotations.ClassName.class);
			ytv.setSentimentrating(sentimentStr);
			ytv.setSentimentscore(sentimentscore);

		}
		return ytv;
	}
	
	public static Voices analyse(Voices voices, String line) {
		Annotation annotation = pipeline.process(line);
		for (CoreMap sentence : annotation
				.get(CoreAnnotations.SentencesAnnotation.class)) {
			Tree tree = sentence
					.get(SentimentCoreAnnotations.AnnotatedTree.class);
			int sentimentscore = RNNCoreAnnotations.getPredictedClass(tree);
			String sentimentStr = sentence
					.get(SentimentCoreAnnotations.ClassName.class);
			voices.setSentimentrating(sentimentStr);
			voices.setSentimentscore(new Long(sentimentscore));

		}
		return voices;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		PipeLineSingle pls = PipeLineSingle.getInstance();
		CrawlerVO cvo = new CrawlerVO();
		cvo = analyse(cvo,"I am not going to stand up for you");
		System.out.println(cvo.getSentimentscore()+"::"+cvo.getSentimentstr());
	}

}
