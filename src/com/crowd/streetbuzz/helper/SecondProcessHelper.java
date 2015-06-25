/**
 * 
 */
package com.crowd.streetbuzz.helper;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.crowd.streetbuzzalgo.stopwords.StopWordRemoval;
import com.crowd.streetbuzzalgo.synonym.BigHugeThesaurus;
import com.crowd.streetbuzzalgo.utils.StrUtil;
import com.crowd.streetbuzzalgo.vo.SearchVO;

/**
 * @author Atrijit
 *
 */
public class SecondProcessHelper implements Constants{

	/**
	 * 
	 */
	public SecondProcessHelper() {
		// TODO Auto-generated constructor stub
	}
	
	public HttpSession process(HttpServletRequest request, String fbtoken) {
		HttpSession session = request.getSession(true);
		
		List nolocation = (List)session.getAttribute("nolocation");
		List location = (List)session.getAttribute("location");
		List twitter = (List)session.getAttribute("twitter");
//		Get viable synonyms
		List synonymList = this.getSynonyms(twitter);
		String place = StrUtil.nonNull((String)session.getAttribute("place"));
		List twitterloc = new ArrayList();
		if(!"".equalsIgnoreCase(place)){
			for(int i=0;i<twitter.size();i++){
				String temp = (String)twitter.get(i);
				temp = (temp+" "+place).trim();
				twitterloc.add(temp);
			}
		}
		
		//		Run different searches
		List twitterList = this.searchTwitter(twitter,twitterloc);
		System.out.println("############# twitter list size: "+twitterList.size()+"#############");
		List twittercleanList = TwitterHelper.cleanSearchList(twitterList,synonymList);
		
		
		
//		Youtube
		//first get the Youtube search results wth YoutubeVO objects - no need for location for youtube
		//UNABLE TO PARSE CMMENTS FOR YOUTUBE - G+ ISSUE AS FOR BLOGSPOT.
		List YoutubeList = this.searchYoutube(twitter);
		
//		Google Searches
		List resultList = this.googleSearch(nolocation);
		List locationResultList = this.googleSearch(location);
		
//		Faroo Search
		List farooSearchResultSet = this.farooSearch(nolocation);
		List farooLocationSearchResultSet = this.farooSearch(location);
		
//		Facebook
		//FacebookClient facebookClient = (FacebookClient)session.getAttribute("facebookClient");
		List mypostlist  = new FacebookCalls(fbtoken).getMyPosts();
		List mypostcleanlist = FBHelper.cleanMyPosts(mypostlist,synonymList);
		HashMap grouppostmap = new FacebookCalls(fbtoken).getAllGroupPosts();
		Map grouppostcleanmap = FBHelper.cleanGroupPosts(grouppostmap,synonymList);
		
		session.setAttribute("twitterList", twittercleanList);
		session.setAttribute("YoutubeList", YoutubeList);
		session.setAttribute("resultList", resultList);
		session.setAttribute("locationResultList", locationResultList);
		session.setAttribute("mypostlist", mypostcleanlist);
		session.setAttribute("grouppostmap", grouppostcleanmap);
		session.setAttribute("farooResultSet", farooSearchResultSet);
		session.setAttribute("farooLocationResultSet", farooLocationSearchResultSet);
		session.setAttribute("synonymList", synonymList);
		
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
	
	private List getSynonyms(List twitter){
		List synonymList = new ArrayList();
		if (twitter!=null && twitter.size()>0){
			for (int i=0;i<twitter.size();i++){
				String temp = (String)twitter.get(i);
				if(StrUtil.isSingle(temp)){
					//find synonyms from Thesaurus
					List synList = getThesaurus(temp);
					synonymList.addAll(synList);
					synonymList.add(temp);
				}else{
					//Find the root and find synonyms from Thesaurus
					String root = getRoot(temp);
					System.out.println("got root: "+root);
					List synList = getThesaurus(root);
					synonymList.addAll(synList);
					synonymList.add(root);
				}
			}
			
		}
		return synonymList;
	}
	
	private String getRoot(String text){
		StopWordRemoval swr = new StopWordRemoval();
		String temp = swr.removeStopwords(text);
		return temp;
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
	
	private List getThesaurus(String text) {
		return BigHugeThesaurus.getThesaurus(text);
	}
	private List searchTwitter(List searchTermsList,List searchTermsListLocation){
		List retList = new ArrayList();
		
		return retList;
	}
	private List searchTwitterOld(List searchTermsList,List searchTermsListLocation){
		List retList = new ArrayList();
		TweetWithSentiments tWS = new TweetWithSentiments();
		int count = searchTermsList.size();
		int num = 50;
		if(count>16){
			count = 16;
		}
		for(int i=0;i<count;i++){
			String querystr = (String)searchTermsList.get(i);
			List result = new ArrayList();
			try {
				try {
					result = tWS.search(querystr);
				} catch (Exception e) {
					continue;
				}
				if(result!=null){
					num = result.size();
					if(num>50){
						num = 50;
					}
				}
				if(result!=null){
					for (int j=0;j<num;j++){
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
				}
				
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		count = searchTermsListLocation.size();
		if(count>16){
			count = 16;
		}
		for(int i=0;i<count;i++){
			String querystr = (String)searchTermsList.get(i);
			List result = new ArrayList();
			try {
				try {
					result = tWS.search(querystr);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					continue;
				}
				if(result!=null){
					num = result.size();
					if(num>50){
						num = 50;
					}
				}
				if(result!=null){
					for (int j=0;j<num;j++){
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
				}
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		/*List finalList =  new ArrayList();
		try {
			finalList = tWS.analyseTweetSentiment(retList);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return finalList;*/
		
		return retList;
		
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
