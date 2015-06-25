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
import com.crowd.streetbuzz.helper.ThirdProcessHelper;

/**
 * @author Atrijit
 *
 */
public class ThirdProcessController implements Controller, Constants{
	private String returnView = "";
	
	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ThirdProcessHelper thirdProcessHelper = new ThirdProcessHelper();
		HttpSession session = thirdProcessHelper.process(request);
		
		returnView = THIRDPROCESSRESULTPAGE;
		System.out.println("Sending to: "+THIRDPROCESSRESULTPAGE);
		return new ModelAndView(returnView);
	}
}
