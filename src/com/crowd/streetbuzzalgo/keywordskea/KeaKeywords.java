/**
 * 
 */
package com.crowd.streetbuzzalgo.keywordskea;

import kea.main.KEAKeyphraseExtractor;
import kea.main.KEAModelBuilder;
import kea.stemmers.PorterStemmer;
import kea.stopwords.StopwordsEnglish;

/**
 * @author Atrijit
 * 
 */
public class KeaKeywords {
	private KEAModelBuilder km;

	private KEAKeyphraseExtractor ke;

	/**
	 * 
	 */
	public KeaKeywords() {
		// TODO Auto-generated constructor stub
	}

	private void setOptionsTraining() {

		km = new KEAModelBuilder();

		// A. required arguments (no defaults):

		// 1. Name of the directory -- give the path to your directory with
		// documents and keyphrases
		// documents should be in txt format with an extention "txt"
		// keyphrases with the same name as documents, but extension "key"
		// one keyphrase per line!
		km.setDirName("testdocs/en/train");

		// 2. Name of the model -- give the path to where the model is to be
		// stored and its name
		km.setModelName("testdocs/en/model");

		// 3. Name of the vocabulary -- name of the file (without extension)
		// that is stored in VOCABULARIES
		// or "none" if no Vocabulary is used (free keyphrase extraction).
		km.setVocabulary("agrovoc");

		// 4. Format of the vocabulary in 3. Leave empty if vocabulary = "none",
		// use "skos" or "txt" otherwise.
		km.setVocabularyFormat("skos");

		// B. optional arguments if you want to change the defaults
		// 5. Encoding of the document
		km.setEncoding("UTF-8");

		// 6. Language of the document -- use "es" for Spanish, "fr" for French
		// or other languages as specified in your "skos" vocabulary
		km.setDocumentLanguage("en"); // es for Spanish, fr for French

		// 7. Stemmer -- adjust if you use a different language than English or
		// if you want to alterate results
		// (We have obtained better results for Spanish and French with
		// NoStemmer)
		km.setStemmer(new PorterStemmer());

		// 8. Stopwords -- adjust if you use a different language than English!
		km.setStopwords(new StopwordsEnglish());

		// 9. Maximum length of a keyphrase
		km.setMaxPhraseLength(5);

		// 10. Minimum length of a keyphrase
		km.setMinPhraseLength(1);

		// 11. Minumum occurrence of a phrase in the document -- use 2 for long
		// documents!
		km.setMinNumOccur(2);

		// Optional: turn off the keyphrase frequency feature
		// km.setUseKFrequency(false);

	}
	
	private void extractKeyphrases() {
		try {
			ke.loadModel();
			ke.extractKeyphrases(ke.collectStems());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void createModel() {
		try {
			km.buildModel(km.collectStems());
			km.saveModel();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void setOptionsTesting(String m_testdir) {

		ke = new KEAKeyphraseExtractor();

		// A. required arguments (no defaults):

		// 1. Name of the directory -- give the path to your directory with
		// documents
		// documents should be in txt format with an extention "txt".
		// Note: keyphrases with the same name as documents, but extension "key"
		// one keyphrase per line!

		ke.setDirName(m_testdir);

		// 2. Name of the model -- give the path to the model
		ke.setModelName("testdocs/en/model");

		// 3. Name of the vocabulary -- name of the file (without extension)
		// that is stored in VOCABULARIES
		// or "none" if no Vocabulary is used (free keyphrase extraction).
		ke.setVocabulary("agrovoc");

		// 4. Format of the vocabulary in 3. Leave empty if vocabulary = "none",
		// use "skos" or "txt" otherwise.
		ke.setVocabularyFormat("skos");

		// B. optional arguments if you want to change the defaults
		// 5. Encoding of the document
		ke.setEncoding("UTF-8");

		// 6. Language of the document -- use "es" for Spanish, "fr" for French
		// or other languages as specified in your "skos" vocabulary
		ke.setDocumentLanguage("en"); // es for Spanish, fr for French

		// 7. Stemmer -- adjust if you use a different language than English or
		// want to alterate results
		// (We have obtained better results for Spanish and French with
		// NoStemmer)
		ke.setStemmer(new PorterStemmer());

		// 8. Stopwords
		ke.setStopwords(new StopwordsEnglish());

		// 9. Number of Keyphrases to extract
		ke.setNumPhrases(10);

		// 10. Set to true, if you want to compute global dictionaries from the
		// test collection
		ke.setBuildGlobal(false);

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		KeaKeywords test = new KeaKeywords();
		test.setOptionsTraining();
		test.createModel();
		String temp = "";
		test.setOptionsTesting(temp);
		test.extractKeyphrases();
		
	}

}
