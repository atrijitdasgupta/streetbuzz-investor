/**
 * 
 */
package com.crowd.streetbuzzalgo.parser;

import java.io.FileInputStream;
import java.io.InputStream;

import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.Span;

/**
 * @author Atrijit
 *
 */
public class InvokeOpenNLPParser {

	/**
	 * 
	 */
	public InvokeOpenNLPParser() {
		// TODO Auto-generated constructor stub
	}
	
	private static void parse(String paragraph)throws Exception{
		InputStream is1 = new FileInputStream("opennlpparsermodels/en-sent.bin");
		SentenceModel model = new SentenceModel(is1);
		SentenceDetectorME sdetector = new SentenceDetectorME(model);
	 	String sentences[] = sdetector.sentDetect(paragraph);
	 	for (int i=0;i<sentences.length;i++){
	 		System.out.println("1. "+sentences[i]);
	 	}
	 	is1.close();
	 	
	 	InputStream is2 = new FileInputStream("opennlpparsermodels/en-token.bin");
	 	TokenizerModel tmodel = new TokenizerModel(is2);
	 	Tokenizer tokenizer = new TokenizerME(tmodel);
	 	String tokens[] = tokenizer.tokenize(paragraph);
	 	for (int j=0;j<tokens.length;j++){
	 		System.out.println("2. "+tokens[j]);
	 	}
	 	is2.close();
	 	
	 	InputStream is3 = new FileInputStream("opennlpparsermodels/en-token.bin");
	 	TokenizerModel tokenModel = new TokenizerModel(is3);
	 	Tokenizer loctokenizer = new TokenizerME(tokenModel);
	 	NameFinderME nameFinder =
	 	      new NameFinderME(
	 	         new TokenNameFinderModel(new FileInputStream("opennlpparsermodels/en-ner-location.bin")));
	 	String loctokens[] = tokenizer.tokenize(paragraph);
	 	Span nameSpans[] = nameFinder.find(loctokens);
	 	 for( int i = 0; i<nameSpans.length; i++) {
	 	      System.out.println("Span: "+nameSpans[i].toString());
	 	   }
			
		is3.close();
	
		
	//	is = new FileInputStream("opennlpparsermodels/en-ner-person.bin");
		 
		
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String input ="who has better chance in Delhi, Modi or Kejriwal?";
		try {
			parse(input);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
