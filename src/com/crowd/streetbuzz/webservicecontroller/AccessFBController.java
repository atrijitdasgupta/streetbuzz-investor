/**
 * 
 */
package com.crowd.streetbuzz.webservicecontroller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.crowd.streetbuzz.common.Constants;

import facebook4j.Facebook;
import facebook4j.FacebookFactory;

/**
 * @author Atrijit
 *
 */
public class AccessFBController implements Controller, Constants{
	private String returnView = "";
	private String apptoken = "749970561718840|Cug6XtEFL4bqE2NtMxK_5-s6Nsg";
	
	private String fbCanvasPage = "http://apps.facebook.com/krowdpoc/";
    private String fbCanvasUrl = "http://example.com/";
    private String CALLBACKURLFB = "/gotopocfb.htm";
	
	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Facebook facebook = new FacebookFactory().getInstance();
		facebook.setOAuthAppId(fbappId, fbappSecret);
		request.getSession().setAttribute("facebook", facebook);
		
		StringBuffer callbackURL = request.getRequestURL();
		int index = callbackURL.lastIndexOf("/");
		callbackURL.replace(index, callbackURL.length(), "").append(
				CALLBACKURLFB);
		String authUrl = facebook.getOAuthAuthorizationURL(callbackURL
				.toString());
		authUrl = authUrl + "&scope=publish_stream,offline_access,email,publish_actions,read_insights,read_stream,user_about_me,user_actions.music,user_actions.book,user_actions.news,user_actions.fitness,user_actions.video,user_photos,user_interests,user_activities,user_events,user_groups,user_likes,user_status,user_website,user_friends,user_hometown,user_location,user_tagged_places,user_work_history,";
		response.sendRedirect(authUrl);
		
		return new ModelAndView(returnView);
	}
}
