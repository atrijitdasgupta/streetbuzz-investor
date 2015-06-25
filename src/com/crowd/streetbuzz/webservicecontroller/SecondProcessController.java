/**
 * 
 */
package com.crowd.streetbuzz.webservicecontroller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.crowd.streetbuzz.common.Constants;
import com.crowd.streetbuzz.dao.implementation.AccessTokenDAO;
import com.crowd.streetbuzz.helper.SecondProcessHelper;
import com.crowd.streetbuzz.model.AccessToken;

/**
 * @author Atrijit
 *
 */
public class SecondProcessController implements Controller, Constants{
	private String returnView = "";
	private AccessTokenDAO accessTokenDAO;
	
	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		SecondProcessHelper secondProcessHelper = new SecondProcessHelper();
		List accessTokenList = accessTokenDAO.getAllRecords();
		AccessToken accessToken = (AccessToken)accessTokenList.get(0);
		String fbtoken = accessToken.getAccesstoken();
		
		HttpSession session = secondProcessHelper.process(request,fbtoken);
		
		returnView = SECONDPROCESSRESULTPAGE;
		return new ModelAndView(returnView);
	}

	public AccessTokenDAO getAccessTokenDAO() {
		return accessTokenDAO;
	}

	public void setAccessTokenDAO(AccessTokenDAO accessTokenDAO) {
		this.accessTokenDAO = accessTokenDAO;
	}
}
