/**
 * 
 */
package com.crowd.streetbuzzalgo.lingpipe;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.SortedSet;

import com.aliasi.lm.TokenizedLM;
import com.aliasi.tokenizer.IndoEuropeanTokenizerFactory;
import com.aliasi.tokenizer.TokenizerFactory;
import com.aliasi.util.Files;
import com.aliasi.util.ScoredObject;
import com.crowd.streetbuzzalgo.utils.DataClean;
import com.crowd.streetbuzzalgo.utils.FileUtils;

/**
 * @author Atrijit
 * 
 */
public class InterestingPhrases {
	private static int NGRAM = 5;

	private static int MIN_COUNT = 5;

	private static int MAX_NGRAM_REPORTING_LENGTH = 2;

	private static int NGRAM_REPORTING_LENGTH = 2;

	private static int MAX_COUNT = 100;

	private static File BACKGROUND_DIR = new File(
			"C:/Mywork/StreetBuzz/Lingpipe/Keyphrase/train/");

	private static File FOREGROUND_DIR = new File(
			"C:/Mywork/StreetBuzz/Lingpipe/Keyphrase/test/");

	/**
	 * 
	 */
	public InterestingPhrases() {
		// TODO Auto-generated constructor stub
	}

	public SortedSet trainFromList(List list) {
		StringBuffer sbfr = new StringBuffer();
		for (int i = 0; i < list.size(); i++) {
			String temp = (String) list.get(i);
			sbfr.append(temp);
		}
		String text = sbfr.toString();
		text = DataClean.clean(text);
		TokenizerFactory tokenizerFactory = IndoEuropeanTokenizerFactory.INSTANCE;
		TokenizedLM backgroundModel = null;
		try {
			backgroundModel = buildModelFromString(tokenizerFactory, NGRAM,
					text);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		backgroundModel.sequenceCounter().prune(3);

		System.out.println("Assembling collocations in Training");
		SortedSet<ScoredObject<String[]>> coll = backgroundModel
				.collocationSet(NGRAM_REPORTING_LENGTH, MIN_COUNT, MAX_COUNT);
		return coll;
		
	}

	public SortedSet trainFromString(String text) {
		TokenizerFactory tokenizerFactory = IndoEuropeanTokenizerFactory.INSTANCE;

		System.out.println("Training background model");
		TokenizedLM backgroundModel = null;
		try {
			backgroundModel = buildModelFromString(tokenizerFactory, NGRAM,
					text);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		backgroundModel.sequenceCounter().prune(3);

		System.out.println("\nAssembling collocations in Training");
		SortedSet<ScoredObject<String[]>> coll = backgroundModel
				.collocationSet(NGRAM_REPORTING_LENGTH, MIN_COUNT, MAX_COUNT);

		/*
		 * System.out.println("\nCollocations in Order of Significance:");
		 * report(coll);
		 */
		return coll;

		// test(backgroundModel,tokenizerFactory);
	}

	public void train() {
		TokenizerFactory tokenizerFactory = IndoEuropeanTokenizerFactory.INSTANCE;

		System.out.println("Training background model");
		TokenizedLM backgroundModel = null;
		try {
			backgroundModel = buildModel(tokenizerFactory, NGRAM,
					BACKGROUND_DIR);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		backgroundModel.sequenceCounter().prune(3);

		System.out.println("\nAssembling collocations in Training");
		SortedSet<ScoredObject<String[]>> coll = backgroundModel
				.collocationSet(NGRAM_REPORTING_LENGTH, MIN_COUNT, MAX_COUNT);

		System.out.println("\nCollocations in Order of Significance:");
		report(coll);

		// test(backgroundModel,tokenizerFactory);
	}

	public void test(TokenizedLM backgroundModel,
			TokenizerFactory tokenizerFactory) {
		System.out.println("Training foreground model");
		TokenizedLM foregroundModel = null;
		try {
			foregroundModel = buildModel(tokenizerFactory, NGRAM,
					FOREGROUND_DIR);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		foregroundModel.sequenceCounter().prune(3);

		System.out.println("\nAssembling New Terms in Test vs. Training");
		SortedSet<ScoredObject<String[]>> newTerms = foregroundModel
				.newTermSet(NGRAM_REPORTING_LENGTH, MIN_COUNT, MAX_COUNT,
						backgroundModel);

		System.out.println("\nNew Terms in Order of Signficance:");
		report(newTerms);

		System.out.println("\nDone.");
	}

	private static TokenizedLM buildModelFromString(
			TokenizerFactory tokenizerFactory, int ngram, String text)
			throws IOException {
		TokenizedLM model = new TokenizedLM(tokenizerFactory, ngram);
		model.handle(text);
		return model;
	}

	private static TokenizedLM buildModel(TokenizerFactory tokenizerFactory,
			int ngram, File directory) throws IOException {

		String[] trainingFiles = directory.list();
		TokenizedLM model = new TokenizedLM(tokenizerFactory, ngram);
		System.out.println("Training on " + directory);

		for (int j = 0; j < trainingFiles.length; ++j) {
			String text = Files.readFromFile(new File(directory,
					trainingFiles[j]), "ISO-8859-1");
			model.handle(text);
		}
		return model;
	}

	private static void report(SortedSet<ScoredObject<String[]>> nGrams) {
		for (ScoredObject<String[]> nGram : nGrams) {
			double score = nGram.score();
			String[] toks = nGram.getObject();
			report_filter(score, toks);
		}
	}

	private static void report_filter(double score, String[] toks) {
		String accum = "";
		for (int j = 0; j < toks.length; ++j) {
			if (nonCapWord(toks[j]))
				return;
			accum += " " + toks[j];
		}
		System.out.println("Score: " + score + " with :" + accum);
	}

	public static boolean nonCapWord(String tok) {
		if (!Character.isUpperCase(tok.charAt(0)))
			return true;
		for (int i = 1; i < tok.length(); ++i)
			if (!Character.isLowerCase(tok.charAt(i)))
				return true;
		return false;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		InterestingPhrases obj = new InterestingPhrases();
		List list = FileUtils.readFile(new File(
				"C:/Mywork/StreetBuzz/Lingpipe/bing.txt"));
		StringBuffer sbfr = new StringBuffer();
		for (int i = 0; i < list.size(); i++) {
			String temp = (String) list.get(i);
			sbfr.append(temp);
		}
		String str = sbfr.toString();
		str = DataClean.clean(str);
		//System.out.println(str);
		SortedSet<ScoredObject<String[]>> nGrams = obj.trainFromString(str);
		for (ScoredObject<String[]> nGram : nGrams) {
			double score = nGram.score();
			String[] toks = nGram.getObject();
			String accum = "";
			for (int j = 0; j < toks.length; ++j) {
				accum += " " + toks[j];

			}

			System.out.println(accum + " :: " + score);
			// interestKeywordsDAO.addOrUpdateRecord(ik);
		}
		long end = System.currentTimeMillis();
		System.out.println("time: " + ((end - start)) + " milliseconds");
		// obj.train();

	}

}
