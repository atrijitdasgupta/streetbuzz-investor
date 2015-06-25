/**
 * 
 */
package com.crowd.streetbuzzalgo.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import twitter4j.GeoLocation;
import twitter4j.Status;

import com.crowd.streetbuzz.common.Constants;
import com.crowd.streetbuzz.dao.implementation.CategoryMasterDAO;
import com.crowd.streetbuzz.dao.implementation.ConversationCardDAO;
import com.crowd.streetbuzz.dao.implementation.SearchCardDAO;
import com.crowd.streetbuzz.dao.implementation.UserCategoryMapDAO;
import com.crowd.streetbuzz.dao.implementation.UserDAO;
import com.crowd.streetbuzz.dao.implementation.VoicesDAO;
import com.crowd.streetbuzz.dao.implementation.VoicesDetailsDAO;
import com.crowd.streetbuzz.helper.FBHelper;
import com.crowd.streetbuzz.helper.SiteSearchHelper;
import com.crowd.streetbuzz.helper.TwitterHelper;
import com.crowd.streetbuzz.model.ConversationCard;
import com.crowd.streetbuzz.model.SearchCard;
import com.crowd.streetbuzzalgo.distribution.Distribution;
import com.crowd.streetbuzzalgo.faroosearch.FarooQueryPoint;
import com.crowd.streetbuzzalgo.faroosearch.vo.FarooResultSet;
import com.crowd.streetbuzzalgo.googlesearch.SearchGoogle;
import com.crowd.streetbuzzalgo.parser.EntityParser;
import com.crowd.streetbuzzalgo.parser.InvokeParser;
import com.crowd.streetbuzzalgo.parser.RankedKeywordParse;
import com.crowd.streetbuzzalgo.parser.RelationsParse;
import com.crowd.streetbuzzalgo.parser.TweetWithSentiments;
import com.crowd.streetbuzzalgo.parser.vo.BasicDependencyVO;
import com.crowd.streetbuzzalgo.parser.vo.RankedKeywordVO;
import com.crowd.streetbuzzalgo.parser.vo.RelationsParseVO;
import com.crowd.streetbuzzalgo.parser.vo.StanfordNerVO;
import com.crowd.streetbuzzalgo.socialplugin.facebook.FacebookCalls;
import com.crowd.streetbuzzalgo.socialplugin.youtube.YTSearch;
import com.crowd.streetbuzzalgo.stopwords.StopWordRemoval;
import com.crowd.streetbuzzalgo.synonym.BigHugeThesaurus;
import com.crowd.streetbuzzalgo.taxonomy.freebase.Freebase;
import com.crowd.streetbuzzalgo.taxonomy.googlesuggest.GoogleSuggest;
import com.crowd.streetbuzzalgo.vo.SearchVO;

import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;

/**
 * @author Atrijit
 * 
 */
public class ProcessingUtils implements Constants {

	/**
	 * 
	 */
	public ProcessingUtils() {
		// TODO Auto-generated constructor stub
	}

	private static String clean(String entry) {
		entry = DataClean.clean(entry);
		return entry;
	}

	private static List rankedKeywordsParse(String entry) {
		List result = new ArrayList();
		try {
			result = RankedKeywordParse.parseRankedKeywords(entry);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
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

	private static BasicDependencyVO getBasicStanfordDependencies(String entry) {
		String grammar = BASEEXTLIBPATH + "stanfordnlp/englishPCFG.ser.gz";
		String[] options = { "-maxLength", "80", "-retainTmpSubcategories" };
		LexicalizedParser lp = LexicalizedParser.loadModel(grammar, options);
		BasicDependencyVO bdvo = InvokeParser.runMorefromSentence(lp, entry);
		return bdvo;
	}

	private static RelationsParseVO relationsParse(String entry) {
		RelationsParseVO rvo = new RelationsParseVO();
		try {
			rvo = RelationsParse.parseRelations(entry);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rvo;
	}

	/*
	 * 1.Take full sentence 2.Check if there are organization and person - if so
	 * run synonym searches on them. 3.Check keywords returned by RP call -
	 * run synonym searches on them 4.Check verb / action from Stanford call
	 * (root) or from RP API call 5.Create combinations - replace in actual
	 * sentence 6.Create combinations - make shorter versions - for Twitter -
	 * just take unique keywords and run search. For Twitter / Youtube shorter
	 * versions and less keyword.
	 */

	private static Map getSearchTerms(String entry, String categoryStr,
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

		String firstsearchterm = sbfr.toString().trim();

		System.out.println("firstsearchterm: " + firstsearchterm);

		List googListPrimary = getGoogleSuggest(firstsearchterm);
		/*
		 * for (int i = 0; i < googListPrimary.size(); i++) { String googTxt =
		 * (String) googListPrimary.get(i); System.out.println("googPrimTxt: " +
		 * googTxt); }
		 */
		List freebaseListPrimary = getFreeBase(firstsearchterm);

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

		secondsearchterm = secondsearchterm.trim();

		System.out.println("secondsearchterm: " + secondsearchterm);

		List googListSecondary = getGoogleSuggest(secondsearchterm);
		/*
		 * for (int i = 0; i < googListSecondary.size(); i++) { String googTxt =
		 * (String) googListSecondary.get(i); System.out.println("googSecTxt: " +
		 * googTxt); }
		 */
		List freebaseListSecondary = getFreeBase(secondsearchterm);

		/*
		 * for (int i = 0; i < freebaseListSecondary.size(); i++) { String
		 * freeTxt = (String) freebaseListSecondary.get(i);
		 * System.out.println("freeSecTxt: " + freeTxt); }
		 */
		List thirdsearchtermlist = new ArrayList();
		for (int i = 0; i < twitterSearchList.size(); i++) {
			String temp = (String) twitterSearchList.get(i);
			temp = temp.trim();
			System.out.println("Twitter search term: " + temp);
			if (!secondsearchterm.equalsIgnoreCase(temp)
					&& !locStr.equalsIgnoreCase(temp)) {
				temp = StrUtil.nonNull(getRoot(temp));
				if (!"".equalsIgnoreCase(temp)) {
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
			if (!noLocationList.contains(temp)) {

				noLocationList.add(temp);
			}
		}
		for (int i = 0; i < googListSecondary.size(); i++) {
			String temp = StrUtil.nonNull((String) googListSecondary.get(i));
			if (!noLocationList.contains(temp)) {
				if (!"".equalsIgnoreCase(temp)) {
					noLocationList.add(temp);
				}

			}
		}
		for (int i = 0; i < googListTertiary.size(); i++) {
			String temp = StrUtil.nonNull((String) googListTertiary.get(i));
			if (!noLocationList.contains(temp)) {
				if (!"".equalsIgnoreCase(temp)) {
					noLocationList.add(temp);
				}

			}
		}
		for (int i = 0; i < verbList.size(); i++) {
			String temp = StrUtil.nonNull((String) verbList.get(i));
			if (!noLocationList.contains(temp)) {
				if (!"".equalsIgnoreCase(temp)) {
					noLocationList.add(temp);
				}
			}
		}

		for (int i = 0; i < freebaseListPrimary.size(); i++) {
			String temp = StrUtil.nonNull((String) freebaseListPrimary.get(i));
			if (!noLocationList.contains(temp)) {
				if (!"".equalsIgnoreCase(temp)) {
					noLocationList.add(temp);
				}
			}
		}

		for (int i = 0; i < freebaseListSecondary.size(); i++) {
			String temp = StrUtil
					.nonNull((String) freebaseListSecondary.get(i));
			if (!noLocationList.contains(temp)) {
				if (!"".equalsIgnoreCase(temp)) {
					noLocationList.add(temp);
				}
			}
		}

		for (int i = 0; i < freebaseListTertiary.size(); i++) {
			String temp = StrUtil.nonNull((String) freebaseListTertiary.get(i));
			if (!noLocationList.contains(temp)) {
				if (!"".equalsIgnoreCase(temp)) {
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

		// for (int i = 0; i < locationList.size(); i++) {
		// String temp = (String) locationList.get(i);
		// System.out.println("locationList: " + temp);
		// }

		noLocationList.add(entry);
		noLocationList.add(firstsearchterm);
		noLocationList.add(secondsearchterm);

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

	private static String getVerb(RelationsParseVO rvo) {
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

	private static String getRoot(String text) {
		StopWordRemoval swr = new StopWordRemoval();
		String temp = swr.removeStopwords(text);
		return temp;
	}

	private static List getGoogleSuggest(String text) {
		List gList = GoogleSuggest.googleSuggest(text);
		return gList;
	}

	private static List getFreeBase(String text) {
		List freebaselist = new ArrayList();
		try {
			freebaselist = Freebase.getList(text);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return freebaselist;
	}

	private static List getSynonyms(List twitter) {
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

	private static List getThesaurus(String text) {
		return BigHugeThesaurus.getThesaurus(text);
	}

	private static List searchTwitter(List searchTermsList,
			List searchTermsListLocation) {
		List retList = new ArrayList();
		TweetWithSentiments tWS = new TweetWithSentiments();
		int count = searchTermsList.size();
		int num = 50;
		if (count > 16) {
			count = 16;
		}
		for (int i = 0; i < count; i++) {
			String querystr = (String) searchTermsList.get(i);
			List result = new ArrayList();
			try {
				try {
					result = tWS.search(querystr);
				} catch (Exception e) {
					continue;
				}
				if (result != null) {
					num = result.size();
					if (num > 50) {
						num = 50;
					}
				}
				if (result != null) {
					for (int j = 0; j < num; j++) {
						Status status = (Status) result.get(j);
						String line = status.getText();
						GeoLocation gl = status.getGeoLocation();
						Date dt = status.getCreatedAt();
						SearchVO svo = new SearchVO();
						svo.setCommentdate(dt);
						svo.setLocation("");
						svo.setSearchterm(querystr);
						svo.setMode(TWITTER);
						svo.setText(line);
						String commenter = status.getUser().getDescription();
						svo.setCommenter(commenter);
						svo.setSearchwithlocation(false);
						if (!retList.contains(svo)) {
							retList.add(svo);
						}

					}
				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		count = searchTermsListLocation.size();
		if (count > 16) {
			count = 16;
		}
		for (int i = 0; i < count; i++) {
			String querystr = (String) searchTermsList.get(i);
			List result = new ArrayList();
			try {
				try {
					result = tWS.search(querystr);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					continue;
				}
				if (result != null) {
					num = result.size();
					if (num > 50) {
						num = 50;
					}
				}
				if (result != null) {
					for (int j = 0; j < num; j++) {
						Status status = (Status) result.get(j);
						String line = status.getText();
						GeoLocation gl = status.getGeoLocation();
						Date dt = status.getCreatedAt();
						SearchVO svo = new SearchVO();
						svo.setCommentdate(dt);
						svo.setLocation("");
						svo.setSearchterm(querystr);
						svo.setMode(TWITTER);
						svo.setText(line);
						String commenter = status.getUser().getDescription();
						svo.setCommenter(commenter);
						svo.setSearchwithlocation(true);
						if (!retList.contains(svo)) {
							retList.add(svo);
						}
					}
				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return retList;
	}

	private static List searchYoutube(List searchTermsList) {
		List finalList = new ArrayList();
		YTSearch ytSearch = new YTSearch();
		for (int i = 0; i < searchTermsList.size(); i++) {
			String querystr = (String) searchTermsList.get(i);
			List result = YTSearch.searchYoutube(querystr);
			finalList.addAll(result);
		}

		return finalList;
	}

	private static List googleSearch(List searchTermsList) {
		List finalList = new ArrayList();
		for (int i = 0; i < searchTermsList.size(); i++) {
			String querystr = (String) searchTermsList.get(i);
			List result = new ArrayList();
			try {
				result = SearchGoogle.search(querystr);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			finalList.addAll(result);
		}
		return finalList;
	}

	private static List farooSearch(List searchkeyterms) {
		List retList = new ArrayList();
		FarooQueryPoint fqp = new FarooQueryPoint();
		for (int i = 0; i < searchkeyterms.size(); i++) {
			String searchkey = (String) searchkeyterms.get(i);
			FarooResultSet resultsSet = fqp.query(searchkey);
			retList.add(resultsSet);

		}
		return retList;
	}

	public static void processSearch(SearchCard sc, String fbtoken,
			SearchCardDAO searchCardDAO, UserDAO userDAO,
			UserCategoryMapDAO userCategoryMapDAO,
			CategoryMasterDAO categoryMasterDAO, VoicesDAO voicesDAO,
			VoicesDetailsDAO voicesDetailsDAO) {

		String searchtopic = StrUtil.nonNull(sc.getTopic());
		String interesttag = StrUtil.nonNull(sc.getInteresttag());

		// First clean the String
		searchtopic = clean(searchtopic);

		// Get Ranked Keywords 
		List rklist = rankedKeywordsParse(searchtopic);

		// The Stanford entity parse call to go here
		StanfordNerVO snvo = getStanfordNamedEntities(searchtopic);

		// Get relations tree 
		RelationsParseVO rvo = relationsParse(searchtopic);

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

		// Run different Searches

		// Twitter
		List twitterList = searchTwitter(twitter, twitterloc);
		List twittercleanList = TwitterHelper.cleanSearchList(twitterList,
				synonymList);

		// Youtube
		// first get the Youtube search results wth YoutubeVO objects - no need
		// for location for youtube
		// UNABLE TO PARSE CMMENTS FOR YOUTUBE - G+ ISSUE AS FOR BLOGSPOT.
		List YoutubeList = searchYoutube(twitter);

		// Google Searches
		List googleResultList = googleSearch(noLocationList);
		List googleLocationResultList = googleSearch(locationList);

		// Faroo Search
		List farooSearchResultList = farooSearch(noLocationList);
		List farooLocationSearchResultList = farooSearch(locationList);

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

		Map sitesearchresult = SiteSearchHelper.siteSearch(pipeline,
				googleResultList, googleLocationResultList,
				farooSearchResultList, farooLocationSearchResultList);

		sc = SBProcessingUtils.processSearchNoLoc(twitter, noLocationList, sc,
				userDAO, userCategoryMapDAO, searchCardDAO, categoryMasterDAO,
				voicesDAO);
		
		sc = ProcessingHelper.spotateSearchCard(sc, sentimentalTwitterList,
				sitesearchresult, voicesDAO, voicesDetailsDAO, YoutubeList);
		
		
		sc.setAction(ACTIONNO);
		searchCardDAO.addOrUpdateRecord(sc);
		// Notify the distribution list
		Distribution.distribute(sc, userDAO, userCategoryMapDAO,
				categoryMasterDAO);
	}
	
	public static void processConversation(ConversationCard cc, String fbtoken,
			ConversationCardDAO conversationCardDAO, UserDAO userDAO,
			UserCategoryMapDAO userCategoryMapDAO,
			CategoryMasterDAO categoryMasterDAO, VoicesDAO voicesDAO,
			VoicesDetailsDAO voicesDetailsDAO) {

		String searchtopic = StrUtil.nonNull(cc.getTopic());
		String interesttag = StrUtil.nonNull(cc.getInteresttag());

		// First clean the String
		searchtopic = clean(searchtopic);

		// Get Ranked Keywords 
		List rklist = rankedKeywordsParse(searchtopic);

		// The Stanford entity parse call to go here
		StanfordNerVO snvo = getStanfordNamedEntities(searchtopic);

		// Get relations tree 
		RelationsParseVO rvo = relationsParse(searchtopic);

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

		// Run different Searches

		// Twitter
		List twitterList = searchTwitter(twitter, twitterloc);
		List twittercleanList = TwitterHelper.cleanSearchList(twitterList,
				synonymList);

		// Youtube
		// first get the Youtube search results wth YoutubeVO objects - no need
		// for location for youtube
		// UNABLE TO PARSE CMMENTS FOR YOUTUBE - G+ ISSUE AS FOR BLOGSPOT.
		List YoutubeList = searchYoutube(twitter);

		// Google Searches
		List googleResultList = googleSearch(noLocationList);
		List googleLocationResultList = googleSearch(locationList);

		// Faroo Search
		List farooSearchResultList = farooSearch(noLocationList);
		List farooLocationSearchResultList = farooSearch(locationList);

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

		Map sitesearchresult = SiteSearchHelper.siteSearch(pipeline,
				googleResultList, googleLocationResultList,
				farooSearchResultList, farooLocationSearchResultList);

		cc = SBProcessingUtils.processConversationNoLoc(twitter, noLocationList, cc,
				userDAO, userCategoryMapDAO, conversationCardDAO, categoryMasterDAO,
				voicesDAO);
		cc = ProcessingHelper.spotateConversationCard(cc, sentimentalTwitterList,
				sitesearchresult, voicesDAO, voicesDetailsDAO, YoutubeList);
		cc.setAction(ACTIONNO);
		conversationCardDAO.addOrUpdateRecord(cc);
		// Notify the distribution list
		Distribution.distribute(cc, userDAO, userCategoryMapDAO,
				categoryMasterDAO,null,null);
	}

	public static void processFull(SearchCard sc, String fbtoken,
			SearchCardDAO searchCardDAO, UserDAO userDAO,
			UserCategoryMapDAO userCategoryMapDAO,
			CategoryMasterDAO categoryMasterDAO, VoicesDAO voicesDAO,
			VoicesDetailsDAO voicesDetailsDAO) {
		String searchtopic = StrUtil.nonNull(sc.getTopic());
		String interesttag = StrUtil.nonNull(sc.getInteresttag());

		// First clean the String
		searchtopic = clean(searchtopic);

		// Get Ranked Keywords 
		List rklist = rankedKeywordsParse(searchtopic);

		// The Stanford entity parse call to go here
		StanfordNerVO snvo = getStanfordNamedEntities(searchtopic);

		// Get relations tree 
		RelationsParseVO rvo = relationsParse(searchtopic);

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

		// Run different searches
		// Twitter
		List twitterList = searchTwitter(twitter, twitterloc);
		List twittercleanList = TwitterHelper.cleanSearchList(twitterList,
				synonymList);

		// Youtube
		// first get the Youtube search results wth YoutubeVO objects - no need
		// for location for youtube
		// UNABLE TO PARSE CMMENTS FOR YOUTUBE - G+ ISSUE AS FOR BLOGSPOT.
		List YoutubeList = searchYoutube(twitter);

		// Google Searches
		List resultList = googleSearch(noLocationList);
		List locationResultList = googleSearch(locationList);

		// Faroo Search
		List farooSearchResultSet = farooSearch(noLocationList);
		List farooLocationSearchResultSet = farooSearch(locationList);

		// Facebook
		List mypostlist = new FacebookCalls(fbtoken).getMyPosts();
		List mypostcleanlist = FBHelper.cleanMyPosts(mypostlist, synonymList);
		HashMap grouppostmap = new FacebookCalls(fbtoken).getAllGroupPosts();
		Map grouppostcleanmap = FBHelper.cleanGroupPosts(grouppostmap,
				synonymList);

		// Sentiment analysis
		Properties props = new Properties();
		props.setProperty("annotators", "tokenize, ssplit, parse, sentiment");
		StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
		TweetWithSentiments tws = new TweetWithSentiments();
		try {
			twitterList = tws.analyseTweetSentiment(twitterList, pipeline,null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Map convertedFBMYPost = new HashMap();
		if (mypostlist != null && mypostlist.size() > 0) {
			convertedFBMYPost = FBHelper.convertFBMypost(mypostlist,
					synonymList, pipeline);
		}

		Map converedFBGrouppost = new HashMap();
		if (grouppostmap != null && grouppostmap.size() > 0) {
			converedFBGrouppost = FBHelper.convertFBGrouppost(grouppostmap,
					synonymList, pipeline);
		}
		Map sitesearchresult = SiteSearchHelper.siteSearch(pipeline);

		sc = SBProcessingUtils.processSearchNoLoc(twitter, noLocationList, sc,
				userDAO, userCategoryMapDAO, searchCardDAO, categoryMasterDAO,
				voicesDAO);
		sc = ProcessingHelperUtils.spotateSearchCard(sc, twitterList,
				convertedFBMYPost, converedFBGrouppost, sitesearchresult,
				voicesDAO, voicesDetailsDAO);
		sc.setAction(ACTIONNO);
		searchCardDAO.addOrUpdateRecord(sc);
		// Notify the distribution list
		Distribution.distribute(sc, userDAO, userCategoryMapDAO,
				categoryMasterDAO);

	}

	public static void process(ConversationCard cc, String fbtoken,
			ConversationCardDAO conversationCardDAO, UserDAO userDAO,
			UserCategoryMapDAO userCategoryMapDAO,
			CategoryMasterDAO categoryMasterDAO, VoicesDAO voicesDAO,
			VoicesDetailsDAO voicesDetailsDAO) {
		String searchtopic = StrUtil.nonNull(cc.getTopic());
		String interesttag = StrUtil.nonNull(cc.getInteresttag());

		// First clean the String
		searchtopic = ProcessingUtils.clean(searchtopic);

		// Get Ranked Keywords 
		List rklist = ProcessingUtils.rankedKeywordsParse(searchtopic);

		// The Stanford entity parse call to go here
		StanfordNerVO snvo = ProcessingUtils
				.getStanfordNamedEntities(searchtopic);

		// Get relations tree - 
		RelationsParseVO rvo = ProcessingUtils
				.relationsParse(searchtopic);

		// The Stanford call to get relationships
		BasicDependencyVO bdvo = ProcessingUtils
				.getBasicStanfordDependencies(searchtopic);

		Map searchTermsMap = ProcessingUtils.getSearchTerms(searchtopic,
				interesttag, rklist, rvo, snvo, bdvo);

		List locationList = (List) searchTermsMap.get("location");
		List noLocationList = (List) searchTermsMap.get("nolocation");
		List twitter = (List) searchTermsMap.get("twitter");
		String place = (String) searchTermsMap.get("place");

		// Get viable synonyms
		List synonymList = ProcessingUtils.getSynonyms(twitter);

		List twitterloc = new ArrayList();
		if (!"".equalsIgnoreCase(place)) {
			for (int i = 0; i < twitter.size(); i++) {
				String temp = (String) twitter.get(i);
				temp = (temp + " " + place).trim();
				twitterloc.add(temp);
			}
		}

		// Run different searches
		// Twitter
		List twitterList = ProcessingUtils.searchTwitter(twitter, twitterloc);
		List twittercleanList = TwitterHelper.cleanSearchList(twitterList,
				synonymList);

		// Youtube
		// first get the Youtube search results wth YoutubeVO objects - no need
		// for location for youtube
		// UNABLE TO PARSE CMMENTS FOR YOUTUBE - G+ ISSUE AS FOR BLOGSPOT.
		List YoutubeList = ProcessingUtils.searchYoutube(twitter);

		// Google Searches
		List resultList = ProcessingUtils.googleSearch(noLocationList);
		List locationResultList = ProcessingUtils.googleSearch(locationList);

		// Faroo Search
		List farooSearchResultSet = ProcessingUtils.farooSearch(noLocationList);
		List farooLocationSearchResultSet = ProcessingUtils
				.farooSearch(locationList);

		// Facebook
		List mypostlist = new FacebookCalls(fbtoken).getMyPosts();
		List mypostcleanlist = FBHelper.cleanMyPosts(mypostlist, synonymList);
		HashMap grouppostmap = new FacebookCalls(fbtoken).getAllGroupPosts();
		Map grouppostcleanmap = FBHelper.cleanGroupPosts(grouppostmap,
				synonymList);

		// Sentiment analysis
		Properties props = new Properties();
		props.setProperty("annotators", "tokenize, ssplit, parse, sentiment");
		StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
		TweetWithSentiments tws = new TweetWithSentiments();
		try {
			twitterList = tws.analyseTweetSentiment(twitterList, pipeline,null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Map convertedFBMYPost = new HashMap();
		if (mypostlist != null && mypostlist.size() > 0) {
			convertedFBMYPost = FBHelper.convertFBMypost(mypostlist,
					synonymList, pipeline);
		}

		Map converedFBGrouppost = new HashMap();
		if (grouppostmap != null && grouppostmap.size() > 0) {
			converedFBGrouppost = FBHelper.convertFBGrouppost(grouppostmap,
					synonymList, pipeline);
		}
		Map sitesearchresult = SiteSearchHelper.siteSearch(pipeline);

		cc = SBProcessingUtils.processConversationNoLoc(twitter,
				noLocationList, cc, userDAO, userCategoryMapDAO,
				conversationCardDAO, categoryMasterDAO, voicesDAO);
		cc = ProcessingHelperUtils.spotateConversationCard(cc, twitterList,
				convertedFBMYPost, converedFBGrouppost, sitesearchresult,
				voicesDAO, voicesDetailsDAO);
		cc.setAction(ACTIONNO);
		conversationCardDAO.addOrUpdateRecord(cc);
		// Notify the distribution list
		Distribution.distribute(cc, userDAO, userCategoryMapDAO,
				categoryMasterDAO,null,null);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
