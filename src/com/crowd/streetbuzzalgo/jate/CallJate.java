/**
 * 
 */
package com.crowd.streetbuzzalgo.jate;

import uk.ac.shef.dcs.oak.jate.core.feature.indexer.GlobalIndexBuilderMem;
import uk.ac.shef.dcs.oak.jate.core.feature.indexer.GlobalIndexMem;
import uk.ac.shef.dcs.oak.jate.core.npextractor.CandidateTermExtractor;
import uk.ac.shef.dcs.oak.jate.core.npextractor.NounPhraseExtractorOpenNLP;
import uk.ac.shef.dcs.oak.jate.core.npextractor.WordExtractor;
import uk.ac.shef.dcs.oak.jate.model.CorpusImpl;
import uk.ac.shef.dcs.oak.jate.util.control.Lemmatizer;
import uk.ac.shef.dcs.oak.jate.util.control.StopList;

/**
 * @author Atrijit
 *
 */
public class CallJate {

	/**
	 * 
	 */
	public CallJate() {
		// TODO Auto-generated constructor stub
	}
	
	public static void call()throws Exception{
		StopList stop = new StopList(true);
        Lemmatizer lemmatizer = new Lemmatizer();
        CandidateTermExtractor npextractor = new NounPhraseExtractorOpenNLP(stop, lemmatizer);
        CandidateTermExtractor wordextractor = new WordExtractor(stop, lemmatizer, false, 1);
        GlobalIndexBuilderMem builder = new GlobalIndexBuilderMem();
        GlobalIndexMem wordDocIndex = builder.build(new CorpusImpl(""), wordextractor);
        GlobalIndexMem termDocIndex = builder.build(new CorpusImpl(""), npextractor);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
