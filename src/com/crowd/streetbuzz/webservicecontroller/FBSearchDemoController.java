/**
 * 
 */
package com.crowd.streetbuzz.webservicecontroller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.crowd.streetbuzz.common.Constants;
import com.crowd.streetbuzz.dao.implementation.AccessTokenDAO;
import com.crowd.streetbuzz.helper.FBHelper;
import com.crowd.streetbuzz.model.AccessToken;
import com.crowd.streetbuzz.processhelperutils.DemoWordProcess;
import com.crowd.streetbuzzalgo.socialplugin.facebook.FacebookCalls;
import com.crowd.streetbuzzalgo.utils.StrUtil;

import edu.stanford.nlp.pipeline.StanfordCoreNLP;

/**
 * @author Atrijit
 *
 */
public class FBSearchDemoController implements Controller, Constants{
	private String returnView = "";
	AccessTokenDAO accessTokenDAO = null;
	
	public AccessTokenDAO getAccessTokenDAO() {
		return accessTokenDAO;
	}

	public void setAccessTokenDAO(AccessTokenDAO accessTokenDAO) {
		this.accessTokenDAO = accessTokenDAO;
	}

	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession(true);
		String searchtopic = StrUtil.nonNull(request
				.getParameter("searchstring"));
		String category = StrUtil.nonNull(request.getParameter("category"));
		String checktype = StrUtil.nonNull(request.getParameter("checktype"));
		checktype="tight";
		List accessTokenList = accessTokenDAO.getAllRecords();
		AccessToken accessToken = (AccessToken)accessTokenList.get(0);
		String fbtoken = accessToken.getAccesstoken();
		List mypostlist  = new FacebookCalls(fbtoken).getMyPosts();
		HashMap grouppostmap = new FacebookCalls(fbtoken).getAllGroupPosts();
		List coreList = DemoWordProcess.processFB(searchtopic);
		List synonymList = new ArrayList();
		if(!"tight".equalsIgnoreCase(checktype)){
			synonymList = getSynonyms(coreList);
		}else{
			synonymList = coreList;
		}
		Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, parse, sentiment");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
		List mypostcleanlist = FBHelper.cleanMyPosts(mypostlist,synonymList);
		Map grouppostcleanmap = FBHelper.cleanGroupPosts(grouppostmap,synonymList);
		Map convertedFBMYPost = new HashMap();
		if(mypostlist!=null && mypostlist.size()>0){
			convertedFBMYPost = FBHelper.convertFBMypost(mypostcleanlist,synonymList, pipeline);
		}
		Map converedFBGrouppost = new HashMap();
		if(grouppostmap!=null && grouppostmap.size()>0){
			converedFBGrouppost = FBHelper.convertFBGrouppost(grouppostmap,synonymList,pipeline);
		}
		session.setAttribute("convertedFBMYPost", convertedFBMYPost);
		session.setAttribute("converedFBGrouppost", converedFBGrouppost);
		returnView = "pages/fbsearchdemoresult.jsp?p=";
		return new ModelAndView(returnView);
	}
	
	private static List getSynonyms(List coreList){
		return coreList;
	}
}
