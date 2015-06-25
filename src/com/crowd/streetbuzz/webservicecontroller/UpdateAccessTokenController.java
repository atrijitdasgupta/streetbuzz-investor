/**
 * 
 */
package com.crowd.streetbuzz.webservicecontroller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.crowd.streetbuzz.common.Constants;
import com.crowd.streetbuzz.dao.implementation.AccessTokenDAO;
import com.crowd.streetbuzz.model.AccessToken;
import com.crowd.streetbuzzalgo.utils.StrUtil;

/**
 * @author Atrijit
 *
 */
public class UpdateAccessTokenController implements Controller, Constants{
	private String returnView = "";
	private AccessTokenDAO accessTokenDAO;
	
	public AccessTokenDAO getAccessTokenDAO() {
		return accessTokenDAO;
	}

	public void setAccessTokenDAO(AccessTokenDAO accessTokenDAO) {
		this.accessTokenDAO = accessTokenDAO;
	}

	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		returnView = UPDATEGRAPHAPITOKENPAGE;
		String graph = StrUtil.nonNull(request.getParameter("graph"));
		graph = graph.trim();
		if("".equals(graph)){
			return new ModelAndView(returnView);
		}else{
			AccessToken accessToken = (AccessToken)accessTokenDAO.getObjectById(new Long(1));
			accessToken.setAccesstoken(graph);
			accessTokenDAO.addOrUpdateRecord(accessToken);
			returnView = UPDATEGRAPHAPITOKENPAGE+"Access Token Updated Successfully";
			return new ModelAndView(returnView);
		}
		
		
	}
}
