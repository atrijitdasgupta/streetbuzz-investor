/**
 * 
 */
package com.crowd.streetbuzz.webservicecontroller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.crowd.streetbuzz.common.Constants;
import com.crowd.streetbuzzalgo.webcrawler.CrawlerController;

/**
 * @author Atrijit
 *
 */
public class CrawlerControllerSpring implements Controller, Constants{
	private String returnView = "";
	
	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String seedurl = (String)request.getParameter("seedurl");
		CrawlerController CC = new CrawlerController(seedurl);
		return new ModelAndView(returnView);
	}
}
