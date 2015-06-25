/**
 * 
 */
package com.crowd.streetbuzz.webservicecontroller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.crowd.streetbuzz.common.Constants;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;

import facebook4j.Facebook;
import facebook4j.FacebookFactory;

/**
 * @author Atrijit
 *
 */
public class GoToPOCFBController implements Controller, Constants{
	private String returnView = "";
	

	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession(true);
		Facebook facebook = null;
		facebook = (Facebook) request.getSession().getAttribute("facebook");
		if(facebook==null){
			facebook = new FacebookFactory().getInstance();
			facebook.setOAuthAppId(fbappId, fbappSecret);
		}
		String oauthCode = request.getParameter("code");
        System.out.println("oauthCode: "+oauthCode);
        String token = facebook.getOAuthAccessToken(oauthCode).getToken();
        System.out.println("token: "+token);
        FacebookClient facebookClient = new DefaultFacebookClient(token);
        session.setAttribute("facebookClient", facebookClient);
        returnView = INPUTPAGE;
		return new ModelAndView(returnView);
		
		
	}
}
