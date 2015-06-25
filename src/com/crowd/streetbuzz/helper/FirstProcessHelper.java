/**
 * 
 */
package com.crowd.streetbuzz.helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.crowd.streetbuzz.common.Constants;
import com.crowd.streetbuzzalgo.parser.EntityParser;
import com.crowd.streetbuzzalgo.parser.InvokeParser;
import com.crowd.streetbuzzalgo.parser.RankedKeywordParse;
import com.crowd.streetbuzzalgo.parser.RelationsParse;
import com.crowd.streetbuzzalgo.parser.vo.BasicDependencyVO;
import com.crowd.streetbuzzalgo.parser.vo.RankedKeywordVO;
import com.crowd.streetbuzzalgo.parser.vo.RelationsParseVO;
import com.crowd.streetbuzzalgo.parser.vo.StanfordNerVO;
import com.crowd.streetbuzzalgo.stopwords.StopWordRemoval;
import com.crowd.streetbuzzalgo.synonym.WikiSynonymAPICaller;
import com.crowd.streetbuzzalgo.taxonomy.freebase.Freebase;
import com.crowd.streetbuzzalgo.taxonomy.googlesuggest.GoogleSuggest;
import com.crowd.streetbuzzalgo.utils.DataClean;
import com.crowd.streetbuzzalgo.utils.StrUtil;
import com.crowd.streetbuzzalgo.utils.TemporaryCategoryUtils;

import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;

/**
 * @author Atrijit
 * 
 */
public class FirstProcessHelper implements Constants {

	/**
	 * 
	 */
	public FirstProcessHelper() {
		// TODO Auto-generated constructor stub
	}

	public HttpSession process(String entry, String category,
			HttpServletRequest request) {
		HttpSession session = request.getSession(true);
		// First clean the String
		entry = this.clean(entry);

		// Check out the category value (page passes numeric value)
		String categoryStr = getCategory(category);

		// Get Ranked Keywords 
		List rklist = this.rankedKeywordsParse(entry);

		// Get relations tree 
		RelationsParseVO rvo = this.relationsParse(entry);

		// The Stanford entity parse call to go here
		StanfordNerVO snvo = this.getStanfordNamedEntities(entry);

		// The Stanford call to get relationships
		BasicDependencyVO bdvo = this.getBasicStanfordDependencies(entry);
		
		Map map = getSearchTerms(entry, categoryStr, rklist, rvo, snvo, bdvo);
		List locationList = (List)map.get("location");
		List noLocationList = (List)map.get("nolocation");
		List twitter = (List)map.get("twitter");
		
		String place = (String)map.get("place");
		
		session.setAttribute("entry", entry);
		session.setAttribute("categoryStr", categoryStr);
		session.setAttribute("nolocation", noLocationList);
		session.setAttribute("location", locationList);
		session.setAttribute("twitter", twitter);
		session.setAttribute("place", place);
		

		return session;
	}

	private String getVerb(RelationsParseVO rvo) {
		List subjectverbtextlist = rvo.getSubjectverbtext();
		List subjectverbactionlist = rvo.getSubjectactiontext();
		String verb = "";
		if (subjectverbtextlist != null && subjectverbtextlist.size() > 0) {
			verb = StrUtil.nonNull((String) subjectverbtextlist.get(0));
		}
		if (!"".equalsIgnoreCase(verb)) {
			return verb;
		} else if (subjectverbactionlist != null
				&& subjectverbactionlist.size() > 0) {
			verb = StrUtil.nonNull((String) subjectverbactionlist.get(0));
			return verb;
		} else {
			return verb;
		}
	}

	private List getWikiSynonym(String text) {
		List wikilist = new ArrayList();
		try {
			wikilist = WikiSynonymAPICaller.getSynonymsList(text);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return wikilist;
	}

	private List googleSuggest(String key) {
		return GoogleSuggest.googleSuggest(key);

	}

	

	private List getFreebaseSynonym(String text) {
		List freebaselist = new ArrayList();
		try {
			freebaselist = Freebase.getList(text);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return freebaselist;
	}

	private List getGoogleSuggest(String text) {
		List gList = GoogleSuggest.googleSuggest(text);
		return gList;
	}

	private List getFreeBase(String text) {
		List freebaselist = new ArrayList();
		try {
			freebaselist = Freebase.getList(text);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return freebaselist;
	}

	private String coreparseCheck(String temp) {
		String serializedClassifier = BASEEXTLIBPATH
				+ "stanfordcorelib/english.all.3class.distsim.crf.ser.gz";
		AbstractSequenceClassifier<CoreLabel> classifier = CRFClassifier
				.getClassifierNoExceptions(serializedClassifier);
		String[] entretemps = temp.split(" ");
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
		temp = sbfr.toString();
		temp = temp.trim();
		return temp;
	}

	public void testprocess(String entry, String category) {

		// First clean the String
		entry = this.clean(entry);

		// Check out the category value (page passes numeric value)
		String categoryStr = getCategory(category);

		// Get Ranked Keywords 
		List rklist = this.rankedKeywordsParse(entry);

		// Get relations tree 
		RelationsParseVO rvo = this.relationsParse(entry);

		// The Stanford entity parse call to go here
		StanfordNerVO snvo = this.getStanfordNamedEntities(entry);

		// The Stanford call to get relationships
		BasicDependencyVO bdvo = this.getBasicStanfordDependencies(entry);
		// test(entry,categoryStr,rklist,rvo,snvo,bdvo);
		getSearchTerms(entry, categoryStr, rklist, rvo, snvo, bdvo);
	}
	
	private String getRoot(String text){
		StopWordRemoval swr = new StopWordRemoval();
		String temp = swr.removeStopwords(text);
		return temp;
	}

	/*
	 * 1.Take full sentence 2.Check if there are organization and person - if so
	 * run synonym searches on them. 3.Check keywords returned by RP call -
	 * run synonym searches on them 4.Check verb / action from Stanford call
	 * (root) or from RP call 5.Create combinations - replace in actual
	 * sentence 6.Create combinations - make shorter versions - for Twitter -
	 * just take unique keywords and run search. For Twitter / Youtube shorter 
	 * versions and less keyword.
	 */
	
	private Map getSearchTerms(String entry, String categoryStr, List rklist,
			RelationsParseVO rvo, StanfordNerVO snvo,
			BasicDependencyVO bdvo) {
		// Get Verb
		String verb = getVerb(rvo);
		System.out
				.println("******************************************************************");
		System.out.println(verb);
		
		List location = snvo.getLocation();
		String locStr = "";
		if (location != null && location.size() > 0) {
			for (int i = 0; i < location.size(); i++) {
				locStr = locStr + " "
						+ StrUtil.nonNull((String) location.get(i));
				locStr = locStr.trim();
			}

		}

		// Get the Keywords from rklist and put them together to create one
		// search term
		StringBuffer sbfr = new StringBuffer();
		List twitterSearchList = new ArrayList();
		for (int i = 0; i < rklist.size(); i++) {
			RankedKeywordVO rkvo = (RankedKeywordVO) rklist.get(i);
			String text = rkvo.getText().trim();
			System.out.println("**"+text+"**");
			if(!twitterSearchList.contains(text)){
				twitterSearchList.add(text);
			}
			
			String root = getRoot(text);
			// the keyords are in deceasing relevance order
			sbfr.append(text + " ");
			root = root.trim();
			if(!root.equalsIgnoreCase(locStr)){
				if(!twitterSearchList.contains(root)){
					twitterSearchList.add(root);
				}
				
			}

		}
		
		String firstsearchterm = sbfr.toString().trim();

		System.out.println("firstsearchterm: " + firstsearchterm);

		List googListPrimary = getGoogleSuggest(firstsearchterm);
		/*
		 * for (int i = 0; i < googListPrimary.size(); i++) { String googTxt =
		 * (String) googListPrimary.get(i); System.out.println("googPrimTxt: " +
		 * googTxt);
		 *  }
		 */
		List freebaseListPrimary = getFreeBase(firstsearchterm);
		
		/*for (int i = 0; i < freebaseListPrimary.size(); i++) {
			String freeTxt = (String) freebaseListPrimary.get(i);
			System.out.println("freePrimTxt: " + freeTxt);

		}*/

		
		//finalise the twittertermlist
		if(twitterSearchList.contains(locStr)){
			twitterSearchList.remove(locStr);
		}
		
		
		
		
		

		// Get the search terms that would fetch results from Google Suggest,
		// Twitter etc.
		// Take the top relevance keyword, append location to it if needed and
		// use that.
		String topPriorityString = "";
		for (int i = 0; i < rklist.size(); i++) {
			RankedKeywordVO rkvo = (RankedKeywordVO) rklist.get(i);
			String text = rkvo.getText().trim();
			System.out.println("locStr: " + locStr);
			System.out.println("text: " + text);
			if (!locStr.equalsIgnoreCase(text)) {
				topPriorityString = text;
				break;
			}
		}
		String secondsearchterm = topPriorityString;// + " " + verb;
		
		secondsearchterm = secondsearchterm.trim();
		
		System.out.println("secondsearchterm: "+secondsearchterm);

		List googListSecondary = getGoogleSuggest(secondsearchterm);
		/*
		 * for (int i = 0; i < googListSecondary.size(); i++) { String googTxt =
		 * (String) googListSecondary.get(i); System.out.println("googSecTxt: " +
		 * googTxt);
		 *  }
		 */
		List freebaseListSecondary = getFreeBase(secondsearchterm);
		
		/*for (int i = 0; i < freebaseListSecondary.size(); i++) {
			String freeTxt = (String) freebaseListSecondary.get(i);
			System.out.println("freeSecTxt: " + freeTxt);

		}*/
		List thirdsearchtermlist = new ArrayList();
		for (int i=0;i<twitterSearchList.size();i++){
			String temp = (String)twitterSearchList.get(i);
			temp = temp.trim();
			System.out.println("Twitter search term: "+temp);
			if(!secondsearchterm.equalsIgnoreCase(temp)&&!locStr.equalsIgnoreCase(temp)){
				temp = StrUtil.nonNull(getRoot(temp));
				if(!"".equalsIgnoreCase(temp)){
					thirdsearchtermlist.add(temp);
				}
				
			}
		}
		
		List googListTertiary = new ArrayList();
		for(int i=0;i<thirdsearchtermlist.size();i++){
			String temp = (String)thirdsearchtermlist.get(i);
			List tempList = getGoogleSuggest(temp);
			googListTertiary.addAll(tempList);
		}
		
		List freebaseListTertiary = new ArrayList();
		for(int i=0;i<thirdsearchtermlist.size();i++){
			String temp = (String)thirdsearchtermlist.get(i);
			List tempList = getFreeBase(temp);
			freebaseListTertiary.addAll(tempList);
		}
		

		// Create one set with verbs added
		List verbList = new ArrayList();
		for (int i = 0; i < googListPrimary.size(); i++) {
			String temp = (String) googListPrimary.get(i);
			temp = temp.trim();
			temp = temp + " " + verb;
			if (!verbList.contains(temp)) {
				verbList.add(temp);
			}
		}

		for (int i = 0; i < googListSecondary.size(); i++) {
			String temp = (String) googListSecondary.get(i);
			temp = temp.trim();
			temp = temp + " " + verb;
			if (!verbList.contains(temp)) {
				verbList.add(temp);
			}
		}
		/*
		 * for (int i=0;i<verbList.size();i++){ String temp =
		 * (String)verbList.get(i); System.out.println("verbList: "+temp); }
		 */

		// To final List add:
		// Original search term
		// Verbless ones
		// Verb ones
		List noLocationList = new ArrayList();

		for (int i = 0; i < googListPrimary.size(); i++) {
			String temp = StrUtil.nonNull((String) googListPrimary.get(i));
			if (!noLocationList.contains(temp)) {
				
				noLocationList.add(temp);
			}
		}
		for (int i = 0; i < googListSecondary.size(); i++) {
			String temp = StrUtil.nonNull((String) googListSecondary.get(i));
			if (!noLocationList.contains(temp)) {
				if(!"".equalsIgnoreCase(temp)){
					noLocationList.add(temp);
				}
				
			}
		}
		for (int i = 0; i < googListTertiary.size(); i++) {
			String temp = StrUtil.nonNull((String) googListTertiary.get(i));
			if (!noLocationList.contains(temp)) {
				if(!"".equalsIgnoreCase(temp)){
					noLocationList.add(temp);
				}
				
			}
		}
		for (int i = 0; i < verbList.size(); i++) {
			String temp = StrUtil.nonNull((String) verbList.get(i));
			if (!noLocationList.contains(temp)) {
				if(!"".equalsIgnoreCase(temp)){
					noLocationList.add(temp);
				}
			}
		}

		for (int i = 0; i < freebaseListPrimary.size(); i++) {
			String temp = StrUtil.nonNull((String) freebaseListPrimary.get(i));
			if (!noLocationList.contains(temp)) {
				if(!"".equalsIgnoreCase(temp)){
					noLocationList.add(temp);
				}
			}
		}

		for (int i = 0; i < freebaseListSecondary.size(); i++) {
			String temp = StrUtil.nonNull((String) freebaseListSecondary.get(i));
			if (!noLocationList.contains(temp)) {
				if(!"".equalsIgnoreCase(temp)){
					noLocationList.add(temp);
				}
			}
		}
		
		for (int i = 0; i < freebaseListTertiary.size(); i++) {
			String temp = StrUtil.nonNull((String) freebaseListTertiary.get(i));
			if (!noLocationList.contains(temp)) {
				if(!"".equalsIgnoreCase(temp)){
					noLocationList.add(temp);
				}
			}
		}

		// create one set with verb and locations added
		List locationList = new ArrayList();
		for (int j = 0; j < noLocationList.size(); j++) {
			String temp = (String) noLocationList.get(j);

			if (!temp.contains(locStr)) {
				temp = temp + " " + locStr;
				locationList.add(temp);
			}

		}

//		for (int i = 0; i < locationList.size(); i++) {
//			String temp = (String) locationList.get(i);
//			System.out.println("locationList: " + temp);
//		}

		noLocationList.add(entry);
		noLocationList.add(firstsearchterm);
		noLocationList.add(secondsearchterm);

//		for (int i = 0; i < noLocationList.size(); i++) {
//			String temp = (String) noLocationList.get(i);
//			System.out.println("noLocationList: " + temp);
//		}
		Map map = new HashMap();
		map.put("location", locationList);
		map.put("nolocation", noLocationList);
		map.put("twitter", twitterSearchList);
		map.put("place", locStr);
		

		return map;

	}

	

	private BasicDependencyVO getBasicStanfordDependencies(String entry) {
		String grammar = BASEEXTLIBPATH+"stanfordnlp/englishPCFG.ser.gz";
		String[] options = { "-maxLength", "80", "-retainTmpSubcategories" };
		LexicalizedParser lp = LexicalizedParser.loadModel(grammar, options);
		BasicDependencyVO bdvo = InvokeParser.runMorefromSentence(lp, entry);
		//BasicDependencyVO bdvo = InvokeParser.runMorefromSentence(entry);
		return bdvo;
	}

	private StanfordNerVO getStanfordNamedEntities(String entry) {
		StanfordNerVO snvo = new StanfordNerVO();
		try {
			snvo = EntityParser.coreparse(entry);
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		return snvo;
	}

	private String clean(String entry) {
		entry = DataClean.clean(entry);
		return entry;
	}

	private RelationsParseVO relationsParse(String entry) {
		RelationsParseVO rvo = new RelationsParseVO();
		try {
			rvo = RelationsParse.parseRelations(entry);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rvo;
	}

	private List rankedKeywordsParse(String entry) {
		List result = new ArrayList();
		try {
			result = RankedKeywordParse.parseRankedKeywords(entry);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	private String getCategory(String category) {
		return TemporaryCategoryUtils.getCategoryStr(category);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		FirstProcessHelper temp = new FirstProcessHelper();
		String entry = "Would Narendra Modi and BJP win in New Delhi?";
		// entry = "Should I buy a Samsung Galaxy or an iPhone?";
		//entry = "Where do I get good coffee in Pune to have for breakfast with a friend?";
		entry="Best cake and coffee in Pune";
		temp.testprocess(entry, "2");
		long end = System.currentTimeMillis();
		long gap = end - start;
		System.out.println("Time taken: "+(gap/1000));
		/*FirstProcessHelper temp = new FirstProcessHelper();
		System.out.println(temp.getRoot("good"));*/

	}

}
