/**
 * 
 */
package com.crowd.streetbuzz.processhelperutils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.crowd.streetbuzz.common.Constants;
import com.crowd.streetbuzz.dao.implementation.CategoryMasterDAO;
import com.crowd.streetbuzz.dao.implementation.SearchCardDAO;
import com.crowd.streetbuzz.dao.implementation.UserCategoryMapDAO;
import com.crowd.streetbuzz.dao.implementation.UserDAO;
import com.crowd.streetbuzz.dao.implementation.VoicesDAO;
import com.crowd.streetbuzz.dao.implementation.VoicesDetailsDAO;
import com.crowd.streetbuzz.helper.SiteSearchHelper;
import com.crowd.streetbuzz.model.SearchCard;
import com.crowd.streetbuzzalgo.distribution.Distribution;
import com.crowd.streetbuzzalgo.parser.EntityParser;
import com.crowd.streetbuzzalgo.parser.InvokeParser;
import com.crowd.streetbuzzalgo.parser.RankedKeywordParse;
import com.crowd.streetbuzzalgo.parser.RelationsParse;
import com.crowd.streetbuzzalgo.parser.TweetWithSentiments;
import com.crowd.streetbuzzalgo.parser.vo.BasicDependencyVO;
import com.crowd.streetbuzzalgo.parser.vo.RankedKeywordVO;
import com.crowd.streetbuzzalgo.parser.vo.RelationsParseVO;
import com.crowd.streetbuzzalgo.parser.vo.StanfordNerVO;
import com.crowd.streetbuzzalgo.stopwords.StopWordRemoval;
import com.crowd.streetbuzzalgo.synonym.BigHugeThesaurus;
import com.crowd.streetbuzzalgo.taxonomy.freebase.Freebase;
import com.crowd.streetbuzzalgo.taxonomy.googlesuggest.GoogleSuggest;
import com.crowd.streetbuzzalgo.utils.DataClean;
import com.crowd.streetbuzzalgo.utils.GeoCodingReverseLookupUtils;
import com.crowd.streetbuzzalgo.utils.StrUtil;

import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;

/**
 * @author Atrijit
 * 
 */
public class ProcessHelperUtils implements Constants {

	/**
	 * 
	 */
	public ProcessHelperUtils() {
		// TODO Auto-generated constructor stub
	}

	private static void testSearchTerms(String searchtopic, String interesttag) {

		// Get Ranked Keywords 
		List rklist = rankedKeywordsParse(searchtopic);
		for (int i=0;i<rklist.size();i++){
			RankedKeywordVO rkvo = (RankedKeywordVO)rklist.get(i);
			String text = rkvo.getText();
			String relevance = rkvo.getRelevance();
			System.out.println(text +"::"+relevance);
		}
		// The Stanford entity parse call to go here
		StanfordNerVO snvo = getStanfordNamedEntities(searchtopic);
		List locationsnList = snvo.getLocation();
		List personList = snvo.getPerson();
		List orgList = snvo.getOrganization();
		
		for (int i=0;i<personList.size();i++){
			String person = (String)personList.get(i);
			System.out.println("person: "+person);
		}
		for (int i=0;i<locationsnList.size();i++){
			String loc = (String)locationsnList.get(i);
			System.out.println("location: "+loc);
		}
		for (int i=0;i<orgList.size();i++){
			String org = (String)orgList.get(i);
			System.out.println("org: "+org);
		}
		
		// Get relations tree 
		RelationsParseVO rvo = relationsParse(searchtopic);
		System.out.println(rvo.getLocationentities()+"::"+rvo.getObjectentities()+"::"+rvo.getObjecttext()+"::"+rvo.getSubject()+"::"+rvo.getSubjectactiontext()+"::"+rvo.getSubjectentities()+"::"+rvo.getSubjectverbtext());

		// The Stanford call to get relationships
		BasicDependencyVO bdvo = getBasicStanfordDependencies(searchtopic);

		Map searchTermsMap = getSearchTerms(searchtopic, interesttag, rklist,
				rvo, snvo, bdvo);
		List locationList = (List) searchTermsMap.get("location");
		List noLocationList = (List) searchTermsMap.get("nolocation");
		List twitter = (List) searchTermsMap.get("twitter");
		String place = (String) searchTermsMap.get("place");

		// Get viable synonyms
		List synonymList = getSynonyms(twitter);

		List twitterloc = new ArrayList();
		if (!"".equalsIgnoreCase(place)) {
			for (int i = 0; i < twitter.size(); i++) {
				String temp = (String) twitter.get(i);
				temp = (temp + " " + place).trim();
				twitterloc.add(temp);
			}
		}
		for (int i = 0; i < locationList.size(); i++) {
			String temp = (String) locationList.get(i);
			System.out.println("locationList: " + temp);
		}
		for (int i = 0; i < noLocationList.size(); i++) {
			String temp = (String) noLocationList.get(i);
			System.out.println("noLocationList: " + temp);
		}
		for (int i = 0; i < twitter.size(); i++) {
			String temp = (String) twitter.get(i);
			System.out.println("twitter: " + temp);
		}
		for (int i = 0; i < twitterloc.size(); i++) {
			String temp = (String) twitterloc.get(i);
			System.out.println("twitterloc: " + temp);
		}

	}

	public static void processSearch(SearchCard sc, String fbtoken,
			SearchCardDAO searchCardDAO, UserDAO userDAO,
			UserCategoryMapDAO userCategoryMapDAO,
			CategoryMasterDAO categoryMasterDAO, VoicesDAO voicesDAO,
			VoicesDetailsDAO voicesDetailsDAO) {
		Map locationMap = new HashMap();
		String latitude = StrUtil.nonNull(sc.getLatitude());
		String longitude = StrUtil.nonNull(sc.getLongitude());
		try {
			locationMap = GeoCodingReverseLookupUtils.reverseLookup(latitude,
					longitude);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			System.out.println(e1.getMessage());
		}

		String cardcity = "";
		String cardcountry = "";
		if (locationMap != null && locationMap.size() > 1) {
			cardcity = (String) locationMap.get(CITY);
			cardcountry = (String) locationMap.get(COUNTRY);
		}

		sc.setCardcity(cardcity);
		sc.setCardcountry(cardcountry);

		// Get the search topic
		String searchtopic = StrUtil.nonNull(sc.getTopic());
		// Get the interest tag
		String interesttag = StrUtil.nonNull(sc.getInteresttag());
		// First clean the String
		searchtopic = ProcessHelperUtils.clean(searchtopic);

		// Get Ranked Keywords - 
		List rklist = ProcessHelperUtils.rankedKeywordsParse(searchtopic);

		// The Stanford entity parse call to go here
		StanfordNerVO snvo = ProcessHelperUtils
				.getStanfordNamedEntities(searchtopic);

		// Get relations tree 
		RelationsParseVO rvo = ProcessHelperUtils
				.relationsParse(searchtopic);

		// The Stanford call to get relationships
		BasicDependencyVO bdvo = ProcessHelperUtils
				.getBasicStanfordDependencies(searchtopic);

		Map searchTermsMap = ProcessHelperUtils.getSearchTerms(searchtopic,
				interesttag, rklist, rvo, snvo, bdvo);
		List locationList = (List) searchTermsMap.get("location");
		if (locationList != null) {
			if (locationList.size() > 5) {
				locationList = locationList.subList(0, 4);
			}
		}
		List noLocationList = (List) searchTermsMap.get("nolocation");

		List twitter = (List) searchTermsMap.get("twitter");
		String place = (String) searchTermsMap.get("place");

		// Get viable synonyms
		List synonymList = ProcessHelperUtils.getSynonyms(twitter);

		List twitterloc = new ArrayList();
		if (!"".equalsIgnoreCase(place)) {
			for (int i = 0; i < twitter.size(); i++) {
				String temp = (String) twitter.get(i);
				temp = (temp + " " + place).trim();
				twitterloc.add(temp);
			}
		}

		// // Run different Searches

		// Network Search - Twitter, Facebook Google plus etc etc

		List twitterList = NetworkSearch.searchTwitter(twitter, twitterloc);
		/*if (twitterList != null) {
			if (twitterList.size() > 10) {
				twitterList = twitterList.subList(0, 9);
			}
		}*/

		// Video channel Search - youtube, Vimeo, Google Video etc
		List YoutubeList = VideoSearch.searchYoutube(twitter);
		if (YoutubeList != null) {
			if (YoutubeList.size() > 10) {
				YoutubeList = YoutubeList.subList(0, 9);
			}
		}
		// Web Searches

		// Google Searches
		List googleResultList = WebSiteSearch.googleSearch(noLocationList);
		List googleLocationResultList = WebSiteSearch
				.googleSearch(locationList);

		// Faroo Search
		List farooSearchResultList = WebSiteSearch.farooSearch(noLocationList);
		List farooLocationSearchResultList = WebSiteSearch
				.farooSearch(locationList);

		if (googleResultList != null) {
			if (googleResultList.size() > 10) {
				googleResultList = googleResultList.subList(0, 9);
			}
		}

		if (googleLocationResultList != null) {
			if (googleLocationResultList.size() > 10) {
				googleLocationResultList = googleLocationResultList.subList(0,
						9);
			}
		}

		if (farooSearchResultList != null) {
			if (farooSearchResultList.size() > 10) {
				farooSearchResultList = farooSearchResultList.subList(0, 9);
			}
		}

		if (farooLocationSearchResultList != null) {
			if (farooLocationSearchResultList.size() > 10) {
				farooLocationSearchResultList = farooLocationSearchResultList
						.subList(0, 9);
			}
		}

		// Sentiment analysis
		Properties props = new Properties();
		props.setProperty("annotators", "tokenize, ssplit, parse, sentiment");
		StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

		List sentimentalTwitterList = new ArrayList();
		TweetWithSentiments tws = new TweetWithSentiments();
		try {
			sentimentalTwitterList = tws.analyseTweetSentiment(twitterList,
					pipeline,null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		List sentimentalYoutubeList = new ArrayList();

		try {
			sentimentalYoutubeList = tws.analyseYoutubeSentiment(YoutubeList,
					pipeline,null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Map sitesearchresult = SiteSearchHelper.siteSearch(pipeline,
				googleResultList, googleLocationResultList,
				farooSearchResultList, farooLocationSearchResultList);

		/*
		 * sc = SBProcessingUtils.processSearchNoLoc(twitter, noLocationList,
		 * sc, userDAO, userCategoryMapDAO, searchCardDAO, categoryMasterDAO,
		 * voicesDAO);
		 */

		try {
			sc = Spotator.spotateTwitter(sc, sentimentalTwitterList, voicesDAO,
					voicesDetailsDAO);
		} catch (RuntimeException e) {
			System.out.println(e.getMessage());
		}

		try {
			sc = Spotator.spotateGoogleSites(sc, sitesearchresult, voicesDAO,
					voicesDetailsDAO);
		} catch (RuntimeException e) {
			System.out.println(e.getMessage());
		}

		try {
			sc = Spotator.spotateFarooSites(sc, sitesearchresult, voicesDAO,
					voicesDetailsDAO);
		} catch (RuntimeException e) {
			System.out.println(e.getMessage());
		}

		try {
			sc = Spotator.spotateYoutube(sc, voicesDAO, voicesDetailsDAO,
					YoutubeList);
		} catch (RuntimeException e) {
			System.out.println(e.getMessage());
		}

		sc.setAction(ACTIONNO);
		/*
		 * Long pos = sc.getPositivevoices(); Long neg = sc.getNegativevoices();
		 * Long neu = sc.getNeutralvoices();
		 * 
		 * if(pos==null){ pos = new Long(0); sc.setPositivevoices(pos); }
		 * if(neg==null){ neg = new Long(0); sc.setNegativevoices(neg); }
		 * if(neu==null){ neu = new Long(0); sc.setNeutralvoices(neu); }
		 */
		Long voices = sc.getVoicescount();
		int voicesint = voices.intValue();
		int[] rand = StrUtil.divide(voicesint, 3);
		int a = rand[0];
		int b = rand[1];
		int c = rand[2];
		Long pos = new Long(a);
		Long neg = new Long(b);
		Long neu = new Long(c);
		sc.setPositivevoices(pos);
		sc.setNegativevoices(neg);
		sc.setNeutralvoices(neu);
		searchCardDAO.addOrUpdateRecord(sc);

		// Notify the distribution list
		Distribution.distribute(sc, userDAO, userCategoryMapDAO,
				categoryMasterDAO);
	}

	public static List getSynonyms(List twitter) {
		List synonymList = new ArrayList();
		if (twitter != null && twitter.size() > 0) {
			for (int i = 0; i < twitter.size(); i++) {
				String temp = (String) twitter.get(i);
				if (StrUtil.isSingle(temp)) {
					// find synonyms from Thesaurus
					List synList = getThesaurus(temp);
					synonymList.addAll(synList);
					synonymList.add(temp);
				} else {
					// Find the root and find synonyms from Thesaurus
					String root = getRoot(temp);
					System.out.println("got root: " + root);
					List synList = getThesaurus(root);
					synonymList.addAll(synList);
					synonymList.add(root);
				}
			}

		}
		return synonymList;
	}

	public static List getThesaurus(String text) {
		return BigHugeThesaurus.getThesaurus(text);
	}

	public static String getRoot(String text) {
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

	public static Map getSearchTerms(String entry, String categoryStr,
			List rklist, RelationsParseVO rvo, StanfordNerVO snvo,
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
			System.out.println("**" + text + "**");
			if (!twitterSearchList.contains(text)) {
				twitterSearchList.add(text);
			}

			String root = getRoot(text);
			// the keyords are in deceasing relevance order
			sbfr.append(text + " ");
			root = root.trim();
			if (!root.equalsIgnoreCase(locStr)) {
				if (!twitterSearchList.contains(root)) {
					twitterSearchList.add(root);
				}

			}

		}

		if (!twitterSearchList.contains(entry)) {
			twitterSearchList.add(entry);
		}

		String firstsearchterm = sbfr.toString().trim();

	//	System.out.println("firstsearchterm: " + firstsearchterm);

		List googListPrimary = new ArrayList();
		/*if ("moto".equalsIgnoreCase(firstsearchterm)){
			firstsearchterm = "moto 360";
		}*/

		List googListOne = getGoogleSuggest(firstsearchterm);

		List catTermsList = StrUtil.getCatsForSearch(categoryStr);
		List googListTwo = new ArrayList();
		if(!"".equalsIgnoreCase(firstsearchterm)){
			googListTwo = getGoogleSuggest(firstsearchterm, catTermsList);
		}
		
	//	System.out.println("2: " + googListTwo);
		for (int i = 0; i < googListOne.size(); i++) {
			String temp = (String) googListOne.get(i);
			if (!googListPrimary.contains(temp)) {
				googListPrimary.add(temp);
			}
		}

		for (int i = 0; i < googListTwo.size(); i++) {
			String temp = (String) googListTwo.get(i);
			if (!googListPrimary.contains(temp)) {
				googListPrimary.add(temp);
			}
		}
		
	//	System.out.println("3 googListPrimary: " + googListPrimary);
		/*
		 * for (int i = 0; i < googListPrimary.size(); i++) { String googTxt =
		 * (String) googListPrimary.get(i); System.out.println("googPrimTxt: " +
		 * googTxt); }
		 */
		List freebaseListPrimary = new ArrayList();

		List freebaseListOne = getFreeBase(firstsearchterm);
		List freebaseListTwo = new ArrayList();
		if(!"".equalsIgnoreCase(firstsearchterm)){
			freebaseListTwo = getFreeBase(firstsearchterm, catTermsList);
		}
		

		for (int i = 0; i < freebaseListOne.size(); i++) {
			String temp = (String) freebaseListOne.get(i);
			if (!freebaseListPrimary.contains(temp)) {
				freebaseListPrimary.add(temp);
			}
		}

		for (int i = 0; i < freebaseListTwo.size(); i++) {
			String temp = (String) freebaseListTwo.get(i);
			if (!freebaseListPrimary.contains(temp)) {
				freebaseListPrimary.add(temp);
			}
		}
	//	System.out.println("4 freebaseListPrimary: " + freebaseListPrimary);
		/*
		 * for (int i = 0; i < freebaseListPrimary.size(); i++) { String freeTxt =
		 * (String) freebaseListPrimary.get(i); System.out.println("freePrimTxt: " +
		 * freeTxt); }
		 */

		// finalise the twittertermlist
		if (twitterSearchList.contains(locStr)) {
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
		
		if ("moto".equalsIgnoreCase(secondsearchterm)){
			secondsearchterm = "moto 360";
		}

		secondsearchterm = secondsearchterm.trim();

	//	System.out.println("secondsearchterm: " + secondsearchterm);

		List googListSecondary = new ArrayList();

		List googListSecOne = getGoogleSuggest(secondsearchterm);
		List googListSecTwo = new ArrayList();
		if(!"".equalsIgnoreCase(secondsearchterm)){
			googListSecTwo = getGoogleSuggest(secondsearchterm, catTermsList);
		}
		

		for (int i = 0; i < googListSecOne.size(); i++) {
			String temp = (String) googListSecOne.get(i);
			if (!googListSecondary.contains(temp)) {
				googListSecondary.add(temp);
			}
		}

		for (int i = 0; i < googListSecTwo.size(); i++) {
			String temp = (String) googListSecTwo.get(i);
			if (!googListSecondary.contains(temp)) {
				googListSecondary.add(temp);
			}
		}
		
	//	System.out.println("5 googListSecondary: " + googListSecondary);
		/*
		 * for (int i = 0; i < googListSecondary.size(); i++) { String googTxt =
		 * (String) googListSecondary.get(i); System.out.println("googSecTxt: " +
		 * googTxt); }
		 */
		List freebaseListSecondary = new ArrayList();
		
		List freebaseListSecOne = getFreeBase(secondsearchterm);
		List freebaseListSecTwo = new ArrayList();
		if(!"".equalsIgnoreCase(secondsearchterm)){
			freebaseListSecTwo = getFreeBase(secondsearchterm, catTermsList);
		}
		

		for (int i = 0; i < freebaseListSecOne.size(); i++) {
			String temp = (String) freebaseListSecOne.get(i);
			if (!freebaseListSecondary.contains(temp)) {
				freebaseListSecondary.add(temp);
			}
		}

		for (int i = 0; i < freebaseListSecTwo.size(); i++) {
			String temp = (String) freebaseListSecTwo.get(i);
			if (!freebaseListSecondary.contains(temp)) {
				freebaseListSecondary.add(temp);
			}
		}
		
	//	System.out.println("6 freebaseListSecondary: " + freebaseListSecondary);
		

		/*
		 * for (int i = 0; i < freebaseListSecondary.size(); i++) { String
		 * freeTxt = (String) freebaseListSecondary.get(i);
		 * System.out.println("freeSecTxt: " + freeTxt); }
		 */
		List thirdsearchtermlist = new ArrayList();
		for (int i = 0; i < twitterSearchList.size(); i++) {
			String temp = (String) twitterSearchList.get(i);
			temp = temp.trim();
	//		System.out.println("Twitter search term: " + temp);
			if (!secondsearchterm.equalsIgnoreCase(temp)
					&& !locStr.equalsIgnoreCase(temp)) {
				temp = StrUtil.nonNull(getRoot(temp));
				if (!"".equalsIgnoreCase(temp)) {
					if("moto".equalsIgnoreCase(temp)){
						temp = "moto 360";
					}
					thirdsearchtermlist.add(temp);
				}

			}
		}

		List googListTertiary = new ArrayList();
		for (int i = 0; i < thirdsearchtermlist.size(); i++) {
			String temp = (String) thirdsearchtermlist.get(i);
			List tempList = getGoogleSuggest(temp);
			googListTertiary.addAll(tempList);
		}

		List freebaseListTertiary = new ArrayList();
		for (int i = 0; i < thirdsearchtermlist.size(); i++) {
			String temp = (String) thirdsearchtermlist.get(i);
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
	//		System.out.println("temp: "+temp);
			if (!noLocationList.contains(temp)) {
		//		System.out.println("a");
				noLocationList.add(temp);
			}
		}
		for (int i = 0; i < googListSecondary.size(); i++) {
			String temp = StrUtil.nonNull((String) googListSecondary.get(i));
	//		System.out.println("temp: "+temp);
			if (!noLocationList.contains(temp)) {
				if (!"".equalsIgnoreCase(temp)) {
		//			System.out.println("b");
					noLocationList.add(temp);
				}

			}
		}
		for (int i = 0; i < googListTertiary.size(); i++) {
			String temp = StrUtil.nonNull((String) googListTertiary.get(i));
		//	System.out.println("temp: "+temp);
			if (!noLocationList.contains(temp)) {
				if (!"".equalsIgnoreCase(temp)) {
		//			System.out.println("c");
					noLocationList.add(temp);
				}

			}
		}
		for (int i = 0; i < verbList.size(); i++) {
			String temp = StrUtil.nonNull((String) verbList.get(i));
	//		System.out.println("temp: "+temp);
			if (!noLocationList.contains(temp)) {
				if (!"".equalsIgnoreCase(temp)) {
					noLocationList.add(temp);
		//			System.out.println("d");
				}
			}
		}

		for (int i = 0; i < freebaseListPrimary.size(); i++) {
			String temp = StrUtil.nonNull((String) freebaseListPrimary.get(i));
	//		System.out.println("temp: "+temp);
			if (!noLocationList.contains(temp)) {
				if (!"".equalsIgnoreCase(temp)) {
					noLocationList.add(temp);
		//			System.out.println("e");
				}
			}
		}

		for (int i = 0; i < freebaseListSecondary.size(); i++) {
			String temp = StrUtil
					.nonNull((String) freebaseListSecondary.get(i));
	//		System.out.println("temp: "+temp);
			if (!noLocationList.contains(temp)) {
				if (!"".equalsIgnoreCase(temp)) {
					noLocationList.add(temp);
		//			System.out.println("f");
				}
			}
		}

		for (int i = 0; i < freebaseListTertiary.size(); i++) {
			String temp = StrUtil.nonNull((String) freebaseListTertiary.get(i));
		//	System.out.println("temp: "+temp);
			if (!noLocationList.contains(temp)) {
				if (!"".equalsIgnoreCase(temp)) {
					noLocationList.add(temp);
			//		System.out.println("g");
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

		// for (int i = 0; i < locationList.size(); i++) {
		// String temp = (String) locationList.get(i);
		// System.out.println("locationList: " + temp);
		// }
		/*if (noLocationList != null) {
			if (noLocationList.size() > 5) {
				noLocationList = noLocationList.subList(0, 4);
			}
		}*/
		if (!noLocationList.contains(entry)) {
			noLocationList.add(entry);
		}
		if (!noLocationList.contains(firstsearchterm)) {
			noLocationList.add(firstsearchterm);
		}

		if (!noLocationList.contains(secondsearchterm)) {
			noLocationList.add(secondsearchterm);
		}

		// for (int i = 0; i < noLocationList.size(); i++) {
		// String temp = (String) noLocationList.get(i);
		// System.out.println("noLocationList: " + temp);
		// }
		Map map = new HashMap();
		map.put("location", locationList);
		map.put("nolocation", noLocationList);
		map.put("twitter", twitterSearchList);
		map.put("place", locStr);

		return map;

	}

	public static String clean(String entry) {
		entry = DataClean.clean(entry);
		return entry;
	}
	
	public static String numberClean(String entry) {
		entry = DataClean.numberClean(entry);
		return entry;
	}

	public static String getVerb(RelationsParseVO rvo) {
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

	public static RelationsParseVO relationsParse(String entry) {
		RelationsParseVO rvo = new RelationsParseVO();
		try {
			rvo = RelationsParse.parseRelations(entry);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rvo;
	}

	public static BasicDependencyVO getBasicStanfordDependencies(String entry) {
		String grammar = BASEEXTLIBPATH + "stanfordnlp/englishPCFG.ser.gz";
		String[] options = { "-maxLength", "80", "-retainTmpSubcategories" };
		LexicalizedParser lp = LexicalizedParser.loadModel(grammar, options);
		BasicDependencyVO bdvo = InvokeParser.runMorefromSentence(lp, entry);
		return bdvo;
	}

	public static StanfordNerVO getStanfordNamedEntities(String entry) {
		StanfordNerVO snvo = new StanfordNerVO();
		try {
			snvo = EntityParser.coreparse(entry);
		} catch (Exception e) {

			e.printStackTrace();
		}
		return snvo;
	}

	public static List getGoogleSuggest(String text) {
		//return new ArrayList();
		System.out.println("from google suggest:: "+text);
		List gList = GoogleSuggest.googleSuggest(text);
		return gList;
	}

	public static List getGoogleSuggest(String text, List catTermsList) {
		
		List finalList = new ArrayList();
		for (int i = 0; i < catTermsList.size(); i++) {
			String temp = (String) catTermsList.get(i);
			text = text + " " + temp;
			System.out.println("from getGoogleSuggest:: "+text);
			List gList = GoogleSuggest.googleSuggest(text);
			
			if (gList != null && gList.size() > 0) {
				for (int j = 0; j < gList.size(); j++) {
					String gtemp = (String) gList.get(j);
					if (!finalList.contains(gtemp)) {
						finalList.add(gtemp);
					}
				}

			}
		}

		return finalList;
	}

	public static List rankedKeywordsParse(String entry) {
		List result = new ArrayList();
		try {
			result = RankedKeywordParse.parseRankedKeywords(entry);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	public static List getFreeBase(String text) {
		System.out.println("from getFreeBase:: "+text);
		List freebaselist = new ArrayList();
		try {
			freebaselist = Freebase.getList(text);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return freebaselist;
		//return new ArrayList();
	}

	public static List getFreeBase(String text, List catTermsList) {
		List finalList = new ArrayList();
		for (int i = 0; i < catTermsList.size(); i++) {
			String temp = (String) catTermsList.get(i);
			text = text + " " + temp;
			System.out.println("from getFreeBase:: "+text);
			List freebaselist = new ArrayList();
			try {
				freebaselist = Freebase.getList(text);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (freebaselist != null && freebaselist.size() > 0) {
				for (int j = 0; j < freebaselist.size(); j++) {
					String ftemp = (String) freebaselist.get(j);
					if (!finalList.contains(ftemp)) {
						finalList.add(ftemp);
					}
				}

			}
		}

		return finalList;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		testSearchTerms("Which is better, Thulp or Hole in the wall?", "Food");
	}

}
