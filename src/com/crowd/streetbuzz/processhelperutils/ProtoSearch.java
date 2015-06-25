/**
 * 
 */
package com.crowd.streetbuzz.processhelperutils;

import java.util.ArrayList;
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
import com.crowd.streetbuzzalgo.parser.TweetWithSentiments;
import com.crowd.streetbuzzalgo.parser.vo.BasicDependencyVO;
import com.crowd.streetbuzzalgo.parser.vo.RelationsParseVO;
import com.crowd.streetbuzzalgo.parser.vo.StanfordNerVO;
import com.crowd.streetbuzzalgo.yandexsearch.YandexSearch;

import edu.stanford.nlp.pipeline.StanfordCoreNLP;

/**
 * @author Atrijit
 * 
 */
public class ProtoSearch implements Constants{

	/**
	 * 
	 */
	public ProtoSearch() {
		// TODO Auto-generated constructor stub
	}

	private static Map Search(String searchtopic, String interesttag) {
		searchtopic = searchtopic.toLowerCase();
		searchtopic = ProcessHelperUtils.clean(searchtopic);
		List rklist = ProcessHelperUtils.rankedKeywordsParse(searchtopic);
		RelationsParseVO rvo = ProcessHelperUtils
				.relationsParse(searchtopic);
		BasicDependencyVO bdvo = ProcessHelperUtils
				.getBasicStanfordDependencies(searchtopic);
		StanfordNerVO snvo = ProcessHelperUtils
				.getStanfordNamedEntities(searchtopic);
		Map searchTermsMap = ProcessHelperUtils.getSearchTerms(searchtopic,
				interesttag, rklist, rvo, snvo, bdvo);
		List locationList = (List) searchTermsMap.get("location");
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
		searchTermsMap.put("twitterloc", twitterloc);
		
		/*  if (locationList != null && locationList.size() > 0) {
		  System.out.println("Printing searchterms in locationList"); for (int
		  i = 0; i < locationList.size(); i++) { String str = (String)
		  locationList.get(i); System.out.println(str); } }
		  
		  if (noLocationList != null && noLocationList.size() > 0) {
		  System.out.println("Printing searchterms in noLocationList"); for
		  (int i = 0; i < noLocationList.size(); i++) { String str = (String)
		  noLocationList.get(i); System.out.println(str); } }
		  
		  if (twitter != null && twitter.size() > 0) {
		  System.out.println("Printing searchterms in twitter"); for (int i =
		  0; i < twitter.size(); i++) { String str = (String) twitter.get(i);
		  System.out.println(str); } }
		  
		  if (twitterloc != null && twitterloc.size() > 0) {
		  System.out.println("Printing searchterms in twitterloc"); for (int i =
		  0; i < twitterloc.size(); i++) { String str = (String)
		  twitterloc.get(i); System.out.println(str); } }*/
		 

		return searchTermsMap;
	}

	public static void doSearch(SearchCard sc, SearchCardDAO searchCardDAO,
			UserDAO userDAO, UserCategoryMapDAO userCategoryMapDAO,
			CategoryMasterDAO categoryMasterDAO, VoicesDAO voicesDAO,
			VoicesDetailsDAO voicesDetailsDAO) {
		String searchterm = sc.getTopic();
		String interesttag = sc.getInteresttag();
		Map searchTermsMap = Search(searchterm, interesttag);
		List locationList = (List) searchTermsMap.get("location");
		if(locationList.size()>10){
			locationList = locationList.subList(0, 9);
		}
		List noLocationList = (List) searchTermsMap.get("nolocation");
		List twitter = (List) searchTermsMap.get("twitter");
		List twitterloc = (List) searchTermsMap.get("twitterloc");

		// Run different searches
		List twitterList = NetworkSearch.searchTwitter(twitter, twitterloc);

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
		List googleLocationResultList = new ArrayList();
		/*List googleLocationResultList = WebSiteSearch
				.googleSearch(locationList);*/
		if(googleResultList.size()>5){
			googleResultList = googleResultList.subList(0, 4);
		}

		// Faroo Search
		List farooSearchResultList = WebSiteSearch.farooSearch(noLocationList);
		/*List farooLocationSearchResultList = WebSiteSearch
				.farooSearch(locationList);*/
		List farooLocationSearchResultList = new ArrayList();
		if(farooSearchResultList.size()>5){
			farooSearchResultList = farooSearchResultList.subList(0, 4);
		}

		// Yandex Search
	//	List yandexSearchResultList = YandexSearch.yandexSearch(noLocationList);
		List yandexSearchResultList = YandexSearch.yandexSearch(twitter);
		/*List yandexLocationSearchResultList = YandexSearch
				.yandexSearch(locationList);*/
		List yandexLocationSearchResultList = new ArrayList();
		if(yandexSearchResultList.size()>10){
			yandexSearchResultList = yandexSearchResultList.subList(0, 9);
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
				farooSearchResultList, farooLocationSearchResultList,
				yandexSearchResultList, yandexLocationSearchResultList);
		
		System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
		System.out.println("sitesearchresult size:"+sitesearchresult.size());
		
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
			sc = Spotator.spotateYandexSites(sc, sitesearchresult, voicesDAO,
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
		searchCardDAO.addOrUpdateRecord(sc);


	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		 Search("sunny leone","Culture & Entertainment");
		// Search("moto 360","Gadgets & Appliances");
		// Search("Real Madrid","Sports & Hobbies");
		// Search("Bitcoins","Politics & Economy");
		// Search("The theory of everything","Culture & Entertainment");
		// Search("ebola","Health & Fitness");
		// Search("ISIS","Politics & Economy");
		//Search("Malaysian Airlines", "Politics & Economy");
		Search("je suis charlie", "Politics & Economy");

	}

}
