/**
 * 
 */
package com.crowd.streetbuzzalgo.stopwords;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.didion.jwnl.JWNL;
import net.didion.jwnl.JWNLException;
import net.didion.jwnl.data.IndexWord;
import net.didion.jwnl.data.POS;
import net.didion.jwnl.dictionary.Dictionary;
import net.didion.jwnl.dictionary.MorphologicalProcessor;

import com.crowd.streetbuzz.common.Constants;

/**
 * @author Atrijit
 * 
 */
public class StopWordRemoval implements Constants{
	private int MaxWordLength = 50;

	private Dictionary dic;

	private MorphologicalProcessor morph;

	private boolean IsInitialized = false;

	public HashMap AllWords = null;

	/**
	 * 
	 */
	public StopWordRemoval() {
		// TODO Auto-generated constructor stub
		AllWords = new HashMap();

		try {
			JWNL
					.initialize(new FileInputStream(
							(BASEEXTLIBPATH+"wordnet/JWNLproperties.xml")));
			dic = Dictionary.getInstance();
			morph = dic.getMorphologicalProcessor();
			// ((AbstractCachingDictionary)dic).
			// setCacheCapacity (10000);
			IsInitialized = true;
		} catch (FileNotFoundException e) {
			System.out
					.println("Error initializing Stemmer: JWNLproperties.xml not found");
			e.printStackTrace();
		} catch (JWNLException e) {
			System.out.println("Error initializing Stemmer: " + e.toString());
			e.printStackTrace();
		}
	}

	public static String removeStopwords(String key) {
		key = key.toLowerCase();
		String[] array = key.split(" ");
		List temp = new ArrayList();
		for (int i = 0; i < array.length; i++) {
			String stop = array[i];
			if (!temp.contains(stop)) {
				temp.add(stop);
			}
		}
		List stopwords = new ArrayList();
		stopwords.add("the");
		stopwords.add("good");
		stopwords.add("best");
		for (int h = 0; h < stopwords.size(); h++) {
			String stopper = (String) stopwords.get(h);
			if (temp.contains(stopper)) {
				temp.remove(stopper);
			}
		}
		StringBuffer sbfr = new StringBuffer();
		for (int k = 0; k < temp.size(); k++) {
			String fin = (String) temp.get(k);
			sbfr.append(fin + " ");
		}
		key = sbfr.toString();
		key = key.trim();
		return key;
	}

	public void Unload() {
		dic.close();
		Dictionary.uninstall();
		JWNL.shutdown();
	}

	public String stem(String word) {
		if (!IsInitialized)
			return word;
		if (word == null)
			return null;
		if (morph == null)
			morph = dic.getMorphologicalProcessor();

		IndexWord w;
		try {
			w = morph.lookupBaseForm(POS.VERB, word);
			if (w != null)
				return w.getLemma().toString();
			w = morph.lookupBaseForm(POS.NOUN, word);
			if (w != null)
				return w.getLemma().toString();
			w = morph.lookupBaseForm(POS.ADJECTIVE, word);
			if (w != null)
				return w.getLemma().toString();
			w = morph.lookupBaseForm(POS.ADVERB, word);
			if (w != null)
				return w.getLemma().toString();
		} catch (JWNLException e) {
		}
		return null;
	}
	
	public String StemSmart( String word )
	{
		// check if we already know the word
		String stemmedword = (String)AllWords.get( word );
		if ( stemmedword != null )
			return stemmedword; // return it if we already know it
		
		// don't check words with digits in them
		if ( containsNumbers (word))
			stemmedword = null;
		else{
			// unknown word: try to stem it
			stemmedword = stem (word);
		}
			
		
		if ( stemmedword != null )
		{
			// word was recognized and stemmed with wordnet:
			// add it to hashmap and return the stemmed word
			AllWords.put( word, stemmedword );
			return stemmedword;
		}
		// word could not be stemmed by wordnet, 
		// thus it is no correct english word
		// just add it to the list of known words so 
		// we won't have to look it up again
		AllWords.put( word, word );
		return word;
	}
	private boolean containsNumbers(String input){
		
		String regex = "(.)*(\\d)(.)*";      
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        boolean isMatched = matcher.matches();
		return isMatched;
		
	}

	/*
	 * public static String stem(String word){ word = word.toLowerCase();
	 * PorterStemmer obj = new PorterStemmer(); obj.setCurrent(word);
	 * obj.stem(); return obj.getCurrent(); }
	 */

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//System.out.println(removeStopwords("the best coffee"));
		// System.out.println(stem("the best coffee"));
		StopWordRemoval swr = new StopWordRemoval();
		String text = "is Ice Cream";
		String temp = removeStopwords(text);
		System.out.println(temp);
		/*String [] textArr = text.split(" ");
		for (int i=0;i<textArr.length;i++){
			String temp = textArr[i];
			System.out.println(temp+": "+swr.stem(temp));
		}
		System.out.println(swr.containsNumbers("6"));*/

	}

}
