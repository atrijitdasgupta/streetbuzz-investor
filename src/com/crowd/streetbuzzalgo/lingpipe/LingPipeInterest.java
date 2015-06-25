/**
 * 
 */
package com.crowd.streetbuzzalgo.lingpipe;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.aliasi.classify.ConditionalClassification;
import com.aliasi.classify.LMClassifier;
import com.aliasi.util.AbstractExternalizable;
import com.crowd.streetbuzz.common.Constants;
import com.crowd.streetbuzzalgo.utils.StrUtil;

/**
 * @author Atrijit
 *
 */
public class LingPipeInterest implements Constants{
	String[] categories;

	LMClassifier lmclass;
	/**
	 * 
	 */
	public LingPipeInterest() {
		try {
			lmclass = (LMClassifier) AbstractExternalizable
					.readObject(new File(BASELINGPIPEPATH + INTERESTCLASSIFIERPATH + CLASSIFIERFILENAME));
			categories = lmclass.categories();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	public String classify(String text) {
		ConditionalClassification classification = lmclass.classify(text);
		return classification.bestCategory();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println(new LingPipeInterest().classify("Antonio Banderas"));

	}

}
