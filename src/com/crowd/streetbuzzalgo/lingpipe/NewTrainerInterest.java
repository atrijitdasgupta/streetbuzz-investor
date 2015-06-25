/**
 * 
 */
package com.crowd.streetbuzzalgo.lingpipe;

import java.io.File;
import java.io.IOException;

import com.aliasi.classify.Classification;
import com.aliasi.classify.Classified;
import com.aliasi.classify.ConfusionMatrix;
import com.aliasi.classify.DynamicLMClassifier;
import com.aliasi.classify.JointClassification;
import com.aliasi.classify.JointClassifier;
import com.aliasi.classify.JointClassifierEvaluator;
import com.aliasi.lm.NGramProcessLM;
import com.aliasi.util.AbstractExternalizable;
import com.aliasi.util.Files;

/**
 * @author Atrijit
 * 
 */
public class NewTrainerInterest {
	private static File TRAINING_DIR = new File(
			"C:/Mywork/StreetBuzz/Lingpipe/newinteresttrainall/");

	private static File TESTING_DIR = new File(
			"C:/Mywork/StreetBuzz/Lingpipe/newinteresttest/");

	private static String[] CATEGORIES = { "1", "2", "3", "4", "5", "6", "7",
			"8", "9", "10", "11", "12", "13", "14", "15", "16" };

	private static int NGRAM_SIZE = 16;

	/**
	 * 
	 */
	public NewTrainerInterest() {
		// TODO Auto-generated constructor stub
	}

	private static void train() throws Exception {
	/*	String [] CATEGORIES = new String[16];
		for (int i=0;i<16;i++){
			String str = new Integer(i+1).toString();
			CATEGORIES[i] = str;
		}*/
		
		DynamicLMClassifier<NGramProcessLM> classifier = DynamicLMClassifier
				.createNGramProcess(CATEGORIES, NGRAM_SIZE);
		for (int i = 0; i < CATEGORIES.length; ++i) {
			File classDir = new File(TRAINING_DIR, CATEGORIES[i]);
			String[] trainingFiles = classDir.list();
			for (int j = 0; j < trainingFiles.length; ++j) {
				File file = new File(classDir, trainingFiles[j]);
				String text = "";
				try {
					text = Files.readFromFile(file, "ISO-8859-1");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("Training on " + CATEGORIES[i] + "/"
						+ trainingFiles[j]);
				Classification classification = new Classification(
						CATEGORIES[i]);
				Classified<CharSequence> classified = new Classified<CharSequence>(
						text, classification);
				classifier.handle(classified);
			}
		}
		System.out.println("Compiling");
		JointClassifier<CharSequence> compiledClassifier = (JointClassifier<CharSequence>) AbstractExternalizable
				.compile(classifier);
		

		boolean storeCategories = true;
		JointClassifierEvaluator<CharSequence> evaluator = new JointClassifierEvaluator<CharSequence>(
				compiledClassifier, CATEGORIES, storeCategories);
		
		String[] testingFiles = TESTING_DIR.list();
		
		for (int j = 0; j < testingFiles.length; ++j) {
			String text = Files.readFromFile(new File(TESTING_DIR,
					testingFiles[j]), "ISO-8859-1");
			System.out.print("Testing on " 	+ testingFiles[j] + " ");
			/*Classification classification = new Classification(
					CATEGORIES[i]);
			Classified<CharSequence> classified = new Classified<CharSequence>(
					text, classification);
			evaluator.handle(classified);*/
			JointClassification jc = compiledClassifier.classify(text);
			String bestCategory = jc.bestCategory();
			String details = jc.toString();
			System.out.println("Got best category of: " + bestCategory);
			//System.out.println(jc.toString());
			//System.out.println("---------------");
		}
		
		/*for (int i = 0; i < CATEGORIES.length; ++i) {
			File classDir = new File(TESTING_DIR, CATEGORIES[i]);
			String[] testingFiles = classDir.list();
			for (int j = 0; j < testingFiles.length; ++j) {
				String text = Files.readFromFile(new File(classDir,
						testingFiles[j]), "ISO-8859-1");
				System.out.print("Testing on " + CATEGORIES[i] + "/"
						+ testingFiles[j] + " ");
				Classification classification = new Classification(
						CATEGORIES[i]);
				Classified<CharSequence> classified = new Classified<CharSequence>(
						text, classification);
				evaluator.handle(classified);
				JointClassification jc = compiledClassifier.classify(text);
				String bestCategory = jc.bestCategory();
				String details = jc.toString();
				System.out.println("Got best category of: " + bestCategory);
				System.out.println(jc.toString());
				System.out.println("---------------");
			}
		}*/
		/*ConfusionMatrix confMatrix = evaluator.confusionMatrix();
        System.out.println("Total Accuracy: " + confMatrix.totalAccuracy());

        System.out.println("\nFULL EVAL");
        System.out.println(evaluator);*/
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			train();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
