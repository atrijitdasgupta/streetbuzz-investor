/**
 * 
 */
package com.crowd.streetbuzzalgo.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.crowd.streetbuzzalgo.parser.InvokeRPParser;
import com.crowd.streetbuzzalgo.parser.MahoutCaller;

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
public class SentimentTest {

	/**
	 * 
	 */
	public SentimentTest() {
		// TODO Auto-generated constructor stub
	}
	
	private static void stanfordSentiment(String line){
		Properties props = new Properties();
		props.setProperty("annotators", "tokenize, ssplit, parse, sentiment");
		StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
		Annotation annotation = pipeline.process(line);
		for (CoreMap sentence : annotation
				.get(CoreAnnotations.SentencesAnnotation.class)) {
			Tree tree = sentence
					.get(SentimentCoreAnnotations.AnnotatedTree.class);
			int sentiment = RNNCoreAnnotations.getPredictedClass(tree);
			String sentimentStr = sentence
					.get(SentimentCoreAnnotations.ClassName.class);
			System.out.println("sentiment numeric value: " + sentiment
					+ ", sentiment absolute value: " + sentimentStr);
			
		}
	}
	
	private static void textalyticsSentiment(String line){
		try {
			String sentimentstr = MahoutCaller.getSentiments(line);
			System.out.println(sentimentstr);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static void mahoutSentiment(String line){
		try {
			InvokeRPParser.parseTargetedSentiment(line,"and");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		List list = new ArrayList();
		list.add ("Rahul Gandhi deserve to be India next P.M. At a young age he join INC and few years later he got post of vice-president in Congress party all because of his hard work and dedication to serve for betterment of Indian people and development of India");
		list.add ("Yes much good reason to hate Italian slut who had exploited country since (specially)last 10 years by huge scams. Who are you Mr. CHUGH to preached me? I am not belongs to congi culture with double standard....|");
		list.add ("It feels disgusted to read people still pinning faith on the dynasty rule. This man has no credentials, doesnt even know which line was written on which paper, and whatever he speaks are not his own thoughts. They are borrowed from PR agencies as they are the ones who write for him. Anyways speech is not what I am trying to put here, come to think of it India is becoming hollow and hollow with each passing time. I dont know what Congress is bringing to India, what culture are they bringing in the name of secularism. Wherever you look, the country seems to be in dismal state of affairs, corruption, appeasement of minority (though they no longer are minority), inflation, neglecting the Indian History, teaching the British concocted theories, and not respecting our own freedom fighters. The worst part is the media during the Congress rule...I appeal to all to bring in the change, a new vision");
		list.add ("Easy weight loss comes from cardio exercise: This time of year, everyone wants a quick answer to the magic quest... http://t.co/SoHK2EGF");
		list.add ("Must watch Misko's clarifying talk at @ngconf on reasons behind the Angular2 binding syntax and semantics. http://t.co/1ebuhtaoP2 ");
		list.add ("Theodore Roosevelt: No man is justified in doing evil on the ground of expediency. http://bit.ly/gUyNzM");
		list.add ("Hello members and non-members and Perumselva Pandiyan Saar! We have received some requests and some threats and some messages that are almost arm-twisting with regards to the group being changed into a closed one.We and our Supremer Architecture Saar PP Saar have had a procrastinated, proactive and protracted and not in the least, lewd discussion and we have decided to keep the group open.");
		list.add ("Seems like Karnataka HC made some error in calculating Jaya ammas inappropriate assets case... And subbu uncle planning to appeal with the apex court... What's u r opinion");
		list.add ("All false allegations against AMMalda Marcos! Her assets are not disproportionate to her size. Its all because she was famous for her thunder thighs once upon a time, the only disproportionate assets she has ever had.");
		
		for (int i=0;i<list.size();i++){
			String line = (String)list.get(i);
			//stanfordSentiment(line);
			mahoutSentiment(line);
			//textalyticsSentiment(line);
		}
	}

}
