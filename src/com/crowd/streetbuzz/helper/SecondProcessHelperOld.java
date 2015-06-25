/**
 * 
 */
package com.crowd.streetbuzz.helper;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import twitter4j.GeoLocation;
import twitter4j.Status;

import com.crowd.streetbuzz.common.Constants;
import com.crowd.streetbuzzalgo.faroosearch.FarooQueryPoint;
import com.crowd.streetbuzzalgo.faroosearch.vo.FarooResultSet;
import com.crowd.streetbuzzalgo.googlesearch.SearchGoogle;
import com.crowd.streetbuzzalgo.parser.TweetWithSentiments;
import com.crowd.streetbuzzalgo.socialplugin.facebook.FacebookCalls;
import com.crowd.streetbuzzalgo.socialplugin.youtube.YTSearch;
import com.crowd.streetbuzzalgo.vo.SearchVO;

/**
 * @author Atrijit
 *
 */
public class SecondProcessHelperOld implements Constants{

	/**
	 * 
	 */
	public SecondProcessHelperOld() {
		// TODO Auto-generated constructor stub
	}
	
	public HttpSession process(HttpServletRequest request, String fbtoken) {
		HttpSession session = request.getSession(true);
		List searchTermsList = (List)session.getAttribute("searchTermsList");
		List searchTermsListLocation = (List) session.getAttribute("searchTermsListLocation");
		List synonymList = (List)session.getAttribute("synonymList");
		//run different searches
		
		//Twitter
		List twitterList = this.searchTwitter(searchTermsList,searchTermsListLocation);
		
		//Youtube
		//first get the Youtube search results wth YoutubeVO objects - no need for location for youtube
		//UNABLE TO PARSE CMMENTS FOR YOUTUBE - G+ ISSUE AS FOR BLOGSPOT.
		List YoutubeList = this.searchYoutube(searchTermsList);
		
		//Google Searches
		List resultList = this.googleSearch(searchTermsList);
		List locationResultList = this.googleSearch(searchTermsListLocation);
		
		//Faroo Search
		List farooSearchResultSet = this.farooSearch(searchTermsList);
		List farooLocationSearchResultSet = this.farooSearch(searchTermsList);
		
		//Facebook
		//FacebookClient facebookClient = (FacebookClient)session.getAttribute("facebookClient");
		List mypostlist  = new FacebookCalls(fbtoken).getMyPosts();
		HashMap grouppostmap = new FacebookCalls(fbtoken).getAllGroupPosts();
		
		// Clean up all the lists - essentially heavy matching
		twitterList = SearchResultListCleanerHelper.cleanList(twitterList, synonymList, "twitter");
		//Pattern patton = SearchResultListCleanerHelper.getPattern(synonymList);
		
		session.setAttribute("twitterList", twitterList);
		session.setAttribute("YoutubeList", YoutubeList);
		session.setAttribute("resultList", resultList);
		session.setAttribute("locationResultList", locationResultList);
		session.setAttribute("mypostlist", mypostlist);
		session.setAttribute("grouppostmap", grouppostmap);
		session.setAttribute("farooResultSet", farooSearchResultSet);
		session.setAttribute("farooLocationResultSet", farooLocationSearchResultSet);
		//session.setAttribute("patton", patton);
		return session;
	}
	
	private List farooSearch(List searchkeyterms){
		List retList = new ArrayList();
		FarooQueryPoint fqp = new FarooQueryPoint();
		for (int i=0;i<searchkeyterms.size();i++){
			String searchkey = (String)searchkeyterms.get(i);
			FarooResultSet resultsSet = fqp.query(searchkey);
			retList.add(resultsSet);
			
		}
		return retList;
	}
	
	private List googleSearch(List searchTermsList){
		List finalList = new ArrayList();
		for(int i=0;i<searchTermsList.size();i++){
			String querystr = (String)searchTermsList.get(i);
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
	
	private List searchYoutube(List searchTermsList){
		List finalList = new ArrayList();
		YTSearch ytSearch = new YTSearch();
		for(int i=0;i<searchTermsList.size();i++){
			String querystr = (String)searchTermsList.get(i);
			List result = YTSearch.searchYoutube(querystr);
			finalList.addAll(result);
		}
		
		return finalList;
	}
	
	private List searchTwitter(List searchTermsList,List searchTermsListLocation){
		List retList = new ArrayList();
		TweetWithSentiments tWS = new TweetWithSentiments();
		int count = searchTermsList.size();
		if(count>10){
			count = 10;
		}
		for(int i=0;i<count;i++){
			String querystr = (String)searchTermsList.get(i);
			List result = new ArrayList();
			try {
				result = tWS.search(querystr);
				for (int j=0;j<result.size();j++){
					Status status = (Status)result.get(j);
					String line = status.getText();
					GeoLocation gl = status.getGeoLocation();
					Date dt =  status.getCreatedAt();
					SearchVO svo = new SearchVO();
					svo.setCommentdate(dt);
					svo.setLocation("");
					svo.setSearchterm(querystr);
					svo.setMode(TWITTER);
					svo.setText(line);
					String commenter = status.getUser().getDescription();
					svo.setCommenter(commenter);
					svo.setSearchwithlocation(false);
					retList.add(svo);
				}
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		/*for(int i=0;i<searchTermsListLocation.size();i++){
			String querystr = (String)searchTermsList.get(i);
			List result = new ArrayList();
			try {
				result = tWS.search(querystr);
				for (int j=0;j<result.size();j++){
					Status status = (Status)result.get(j);
					String line = status.getText();
					GeoLocation gl = status.getGeoLocation();
					Date dt =  status.getCreatedAt();
					SearchVO svo = new SearchVO();
					svo.setCommentdate(dt);
					svo.setLocation("");
					svo.setSearchterm(querystr);
					svo.setMode(TWITTER);
					svo.setText(line);
					String commenter = status.getUser().getDescription();
					svo.setCommenter(commenter);
					svo.setSearchwithlocation(true);
					retList.add(svo);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}*/
		/*List finalList =  new ArrayList();
		try {
			finalList = tWS.analyseTweetSentiment(retList);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return finalList;
		*/
		return retList;
		
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
