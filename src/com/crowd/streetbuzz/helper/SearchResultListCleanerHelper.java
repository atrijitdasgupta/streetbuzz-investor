/**
 * 
 */
package com.crowd.streetbuzz.helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import com.crowd.streetbuzz.common.Constants;
import com.crowd.streetbuzzalgo.parser.EntityParser;
import com.crowd.streetbuzzalgo.utils.StrUtil;
import com.crowd.streetbuzzalgo.vo.SearchVO;

import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreLabel;

/**
 * @author Atrijit
 * 
 */
public class SearchResultListCleanerHelper implements Constants{

	/**
	 * 
	 */
	public SearchResultListCleanerHelper() {
		// TODO Auto-generated constructor stub
	}

	public static HashMap cleanMap(HashMap maptoclean, List synonyms, String mode) {
		Pattern patton = getPattern(synonyms);
		if ("facebook".equalsIgnoreCase(mode)) {
			return cleanFacebookMap(maptoclean, patton);
		} else {
			return maptoclean;
		}

	}

	private static HashMap cleanFacebookMap(HashMap facebookMap, Pattern patton) {

		return facebookMap;
	}

	public static Pattern getPattern(List synonyms) {
		StringBuffer regexBuf = new StringBuffer();
		for (int i = 0; i < synonyms.size(); i++) {
			String syn = (String) synonyms.get(i);
			syn = syn.toLowerCase();
			regexBuf.append(syn + "|");

		}
		String regex = regexBuf.toString();
		if(regex.endsWith("|")){
			regex = regex.substring(0, regex.lastIndexOf("|"));
		}
		
		Pattern patton = Pattern.compile(regex);
		return patton;
	}

	public static List cleanList(List listtoclean, List synonyms, String mode) {

		Pattern patton = getPattern(synonyms);

		if ("twitter".equalsIgnoreCase(mode)) {
			return cleanTwitterList(listtoclean, patton);
		} else if ("google".equalsIgnoreCase(mode)) {
			return cleanGoogleList(listtoclean, patton);
		} else if ("facebook".equalsIgnoreCase(mode)) {
			return cleanFacebookList(listtoclean, patton);
		} else if ("faroo".equalsIgnoreCase(mode)) {
			return cleanFarooList(listtoclean, patton);
		} else {
			return listtoclean;
		}
	}
	
	private static List cleanTwitterList(List twitterList, List synonyms) {
		List newtwitterlist = new ArrayList();
		for (int i = 0; i < twitterList.size(); i++) {
			SearchVO svo = (SearchVO) twitterList.get(i);
			String text = svo.getText();
			text = text.toLowerCase();
			if(StrUtil.containsOrNot(text,synonyms)){
				newtwitterlist.add(svo);
			}
		}
		return newtwitterlist;
	}

	private static List cleanTwitterList(List twitterList, Pattern patton) {
		List newtwitterlist = new ArrayList();
		for (int i = 0; i < twitterList.size(); i++) {
			SearchVO svo = (SearchVO) twitterList.get(i);
			String text = svo.getText();
			text = text.toLowerCase();
			if(StrUtil.containsOld(text,patton)){
				newtwitterlist.add(svo);
			}
		}
		return newtwitterlist;
	}

	private static List cleanGoogleList(List googleList, Pattern patton) {
		
		return googleList;
	}

	private static List cleanFacebookList(List facebookList, Pattern patton) {
		
		return facebookList;
	}

	private static List cleanFarooList(List farooList, Pattern patton) {
		return farooList;
	}
	public static List removeIrrelevancy(List listtoclean, String entry, String action){
		List finalList = new ArrayList();
		String serializedClassifier = BASEEXTLIBPATH+"stanfordcorelib/english.all.3class.distsim.crf.ser.gz";
		AbstractSequenceClassifier<CoreLabel> classifier = CRFClassifier.getClassifierNoExceptions(serializedClassifier);
		for (int i=0;i<listtoclean.size();i++){
			String searchterm = (String)listtoclean.get(i);
			if(stripAway(searchterm, entry, classifier)){
				if(!finalList.contains(searchterm)){
					String searchtermlow = searchterm.toLowerCase();
					String actionlow = action.toLowerCase();
					if(searchtermlow.indexOf(actionlow)<0){
						searchterm = searchterm+" "+action;
					}
					searchterm = searchterm.trim();
					if(!"".equalsIgnoreCase(searchterm)){
						finalList.add(searchterm);
					}
					
				}
			}
		}
		
		return finalList;
	}
	
	private static boolean stripAway(String searchterm, String entry, AbstractSequenceClassifier<CoreLabel> classifier){
		
		String[] entretemps = searchterm.split(" ");
		StringBuffer sbfr = new StringBuffer();
		for (int k = 0; k < entretemps.length; k++) {
			String etemp = entretemps[k];
			try {
				if (EntityParser.coreparseCheck(etemp, classifier)) {
					sbfr.append(" " + etemp);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		searchterm = sbfr.toString();
		System.out.println(searchterm);
		System.out.println(entry);
		String searchtermArr[] = searchterm.split(" ");
		for (int i=0;i<searchtermArr.length;i++){
			String temp = searchtermArr[i];
			if(entry.indexOf(temp)>-1){
				return true;
			}
		}
		return false;
	}


	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String searchterm = "in club Modi Delhi";
		String entry = "Whether Narendra Modi would win in Delhi?";
		String serializedClassifier = BASEEXTLIBPATH+"stanfordcorelib/english.all.3class.distsim.crf.ser.gz";
		AbstractSequenceClassifier<CoreLabel> classifier = CRFClassifier.getClassifierNoExceptions(serializedClassifier);
		System.out.println(stripAway(searchterm, entry, classifier));

	}

}
