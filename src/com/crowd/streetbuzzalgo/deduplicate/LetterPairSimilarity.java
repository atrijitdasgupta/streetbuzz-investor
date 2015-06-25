/**
 * 
 */
package com.crowd.streetbuzzalgo.deduplicate;

import java.util.ArrayList;

/**
 * @author Atrijit
 * 
 */
public class LetterPairSimilarity {

	/**
	 * 
	 */
	public LetterPairSimilarity() {
		// TODO Auto-generated constructor stub
	}
	public static boolean stringCompare(String str1, String str2) {
		str1 = str1.replaceAll(" ", "");
		str2 = str2.replaceAll(" ", "");
		str1 = str1.trim();
		str2 = str2.trim();
		if(str1.equalsIgnoreCase(str2)){
			return true;
		}
		return false;
	}
	public static double compareStrings(String str1, String str2) {
		ArrayList pairs1 = wordLetterPairs(str1.toUpperCase());
		ArrayList pairs2 = wordLetterPairs(str2.toUpperCase());
		int intersection = 0;
		int union = pairs1.size() + pairs2.size();
		for (int i = 0; i < pairs1.size(); i++) {
			Object pair1 = pairs1.get(i);
			for (int j = 0; j < pairs2.size(); j++) {
				Object pair2 = pairs2.get(j);
				if (pair1.equals(pair2)) {
					intersection++;
					pairs2.remove(j);
					break;
				}
			}
		}
		return (2.0 * intersection) / union;
	}

	private static ArrayList wordLetterPairs(String str) {
		ArrayList allPairs = new ArrayList();
		// Tokenize the string and put the tokens/words into an array
		String[] words = str.split("\\s");
		// For each word
		for (int w = 0; w < words.length; w++) {
			// Find the pairs of characters
			String[] pairsInWord = letterPairs(words[w]);
			for (int p = 0; p < pairsInWord.length; p++) {
				allPairs.add(pairsInWord[p]);
			}
		}
		return allPairs;
	}

	private static String[] letterPairs(String str) {
		int numPairs = str.length() - 1;
		String[] pairs = new String[numPairs];
		for (int i = 0; i < numPairs; i++) {
			pairs[i] = str.substring(i, i + 2);
		}
		return pairs;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println(stringCompare("Moto E", "MotoE"));
		

	}

}
