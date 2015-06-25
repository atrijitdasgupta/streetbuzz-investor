/**
 * 
 */
package com.crowd.streetbuzzalgo.lingpipe;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.aliasi.classify.ConditionalClassification;
import com.aliasi.classify.LMClassifier;
import com.aliasi.util.AbstractExternalizable;
import com.crowd.streetbuzz.common.Constants;
import com.crowd.streetbuzzalgo.utils.StrUtil;

/**
 * @author Atrijit
 * 
 */
public class LingPipe implements Constants {
	String[] categories;

	LMClassifier lmclass;

	/**
	 * 
	 */
	public LingPipe() {
		try {
			lmclass = (LMClassifier) AbstractExternalizable
					.readObject(new File(BASEEXTLIBPATH + CLASSIFIERPATH
							+ "/classifier.lingPipe"));
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
		List list = StrUtil.breakPara(text);
		//System.out.println(list);
		List val = new ArrayList();
		for (int i = 0; i < list.size(); i++) {
			String sentence = StrUtil.nonNull((String) list.get(i));
			if("".equalsIgnoreCase(sentence)){
				continue;
			}
		//	System.out.println(sentence);
			String sentiment = this.classifySingle(sentence);
			val.add(sentiment);
			if(val.size()==1){
				break;
			}
		}
		//System.out.println(val);
		String sentiment = getSentiment(val);
		//System.out.println(sentiment);
		return sentiment;
	}

	public int getSentimentScore(String sentiment) {
		if (POSITIVELING.equalsIgnoreCase(sentiment)) {
			return 3;
		} else if (NEUTRALLING.equalsIgnoreCase(sentiment)) {
			return 2;
		} else if (NEGATIVELING.equalsIgnoreCase(sentiment)) {
			return 1;
		}
		return 0;
	}

	private String getSentiment(List list) {
		int pos = 0;
		int neg = 0;
		int neu = 0;
		for (int i = 0; i < list.size(); i++) {
			String sentiment = (String) list.get(i);
			sentiment = sentiment.trim();
			if (POSITIVELING.equalsIgnoreCase(sentiment)) {
				pos = pos + 1;
			} else if (NEGATIVELING.equalsIgnoreCase(sentiment)) {
				neg = neg + 1;
			} else if (NEUTRALLING.equalsIgnoreCase(sentiment)) {
				neu = neu + 1;
			}
		}
		if (pos > neg && pos > neu) {
			return POSITIVELING;
		} else if (neg > pos && neg > neu) {
			return NEGATIVELING;
		} else if (neu > pos && neu > neg) {
			return NEUTRALLING;
		} else if (pos == 0) {
			if (neg > neu) {
				return NEGATIVELING;
			} else if (neu > neg) {
				return NEUTRALLING;
			} else if (neg == neu) {
				return NEUTRALLING;
			}
		} else if (neg == 0) {
			if (pos > neu) {
				return POSITIVELING;
			} else if (neu > pos) {
				return NEUTRALLING;
			} else if (neu == pos) {
				return POSITIVELING;
			}
		} else if (neu == 0) {
			if (pos > neg) {
				return POSITIVELING;
			} else if (neg > pos) {
				return NEGATIVELING;
			} else if (neg == pos) {
				return POSITIVELING;
			}
		} else if (pos == 0 && neg == 0 && neu > 0) {
			return NEUTRALLING;
		} else if (pos == 0 && neg > 0 && neu == 0) {
			return NEGATIVELING;
		} else if (pos > 0 && neg > 0 && neu > 0) {
			return POSITIVELING;
		}

		return NEUTRALLING;
	}

	private String classifySingle(String text) {
		ConditionalClassification classification = lmclass.classify(text);
		return classification.bestCategory();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println(new LingPipe().classify("Antonio Banderas"));
	
	}

}
