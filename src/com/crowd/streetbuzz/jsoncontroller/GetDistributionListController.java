/**
 * 
 */
package com.crowd.streetbuzz.jsoncontroller;

import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.crowd.streetbuzz.common.Constants;
import com.crowd.streetbuzz.dao.implementation.UserDAO;
import com.crowd.streetbuzz.json.JsonHeader;
import com.crowd.streetbuzzalgo.distribution.Distribution;
import com.crowd.streetbuzzalgo.utils.StrUtil;
import com.google.gson.Gson;

/**
 * @author Atrijit
 * 
 */

/*
 * Get User friend List / SB network list for card
 */
public class GetDistributionListController implements Controller, Constants {
	UserDAO userDAO = null;

	public UserDAO getUserDAO() {
		return userDAO;
	}

	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		System.out.println("Inside GetMessagesController ready to spotate");
		HttpSession session = request.getSession(true);
		JsonHeader jsonHeader = (JsonHeader) session.getAttribute("jsonHeader");
		String jsonbody = StrUtil.nonNull((String) session
				.getAttribute("jsonbody"));
		String userid = StrUtil.nonNull(jsonHeader.getUserid());
		String cardid = StrUtil.nonNull(jsonHeader.getCardid());
		List distributionList = Distribution.getDistributionList(userid,
				cardid, userDAO);
		Gson gson = new Gson();
		PrintWriter writer = response.getWriter();
		String jsonresponse = gson.toJson(distributionList);

		response.setStatus(200);
		response.addHeader("Access-Control-Allow-Origin", "*");
		writer = response.getWriter();
		writer.write(jsonresponse);
		writer.close();
		return null;
	}
}
