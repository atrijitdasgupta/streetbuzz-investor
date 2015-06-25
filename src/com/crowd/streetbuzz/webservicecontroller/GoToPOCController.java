/**
 * 
 */
package com.crowd.streetbuzz.webservicecontroller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.crowd.streetbuzz.common.Constants;

/**
 * @author Atrijit
 * 
 */
public class GoToPOCController implements Controller, Constants {
	private String returnView = "";

	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		//returnView = INPUTPAGE;
		returnView = "pages/protoentry.jsp?p=";
		return new ModelAndView(returnView);
	}

}
