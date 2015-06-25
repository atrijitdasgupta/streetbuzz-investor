/**
 * 
 */
package com.crowd.streetbuzz.helper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.crowd.streetbuzzalgo.parser.TweetWithSentiments;

import edu.stanford.nlp.pipeline.StanfordCoreNLP;

/**
 * @author Atrijit
 * 
 */
public class ThirdProcessHelper {

	/**
	 * 
	 */
	public ThirdProcessHelper() {
		// TODO Auto-generated constructor stub
	}

	public HttpSession process(HttpServletRequest request) {
		HttpSession session = request.getSession(true);
		
		String entry = (String)session.getAttribute("entry");
		String categoryStr = (String)session.getAttribute("categoryStr");
		List twitterList = (List) session.getAttribute("twitterList");
		List YoutubeList = (List) session.getAttribute("YoutubeList");
		List resultList = (List) session.getAttribute("resultList");
		List locationResultList = (List) session
				.getAttribute("locationResultList");
		List mypostlist = (List) session.getAttribute("mypostlist");
		Map grouppostmap = (HashMap) session
				.getAttribute("grouppostmap");
		List farooSearchResultSet = (List)session.getAttribute("farooSearchResultSet");
		List farooLocationSearchResultSet = (List)session.getAttribute("farooLocationSearchResultSet");
		List synonymList = (List) session.getAttribute("synonymList"); 
		
		Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, parse, sentiment");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
		
		TweetWithSentiments tws = new TweetWithSentiments();
		//Special check for twitter
		if(twitterList==null || twitterList.size()<1){
			twitterList = TwitterHelper.fillTwitterList(pipeline);
		}else{
			try {
				twitterList = tws.analyseTweetSentiment(twitterList,pipeline,null);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		Map convertedFBMYPost = new HashMap();
		if(mypostlist!=null && mypostlist.size()>0){
			convertedFBMYPost = FBHelper.convertFBMypost(mypostlist,synonymList, pipeline);
		}
		
		Map converedFBGrouppost = new HashMap();
		if(grouppostmap!=null && grouppostmap.size()>0){
			converedFBGrouppost = FBHelper.convertFBGrouppost(grouppostmap,synonymList,pipeline);
		}
		
		Map sitesearchresult = SiteSearchHelper.siteSearch(pipeline);
		
		session.setAttribute("twitterList", twitterList);
		session.setAttribute("convertedFBMYPost", convertedFBMYPost);
		session.setAttribute("converedFBGrouppost", converedFBGrouppost);
		session.setAttribute("sitesearchresult", sitesearchresult);
		
		System.out.println("about to return session");
		
		return session;
	}
	
	

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
