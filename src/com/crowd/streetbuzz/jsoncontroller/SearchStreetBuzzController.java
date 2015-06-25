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
import com.crowd.streetbuzz.dao.implementation.ConversationCardDAO;
import com.crowd.streetbuzz.json.JSONStreetBuzzSearch;
import com.crowd.streetbuzz.json.JsonHeader;
import com.google.gson.Gson;

/**
 * @author Atrijit
 * 
 */
public class SearchStreetBuzzController implements Controller, Constants {
	private ConversationCardDAO conversationCardDAO;

	public ConversationCardDAO getConversationCardDAO() {
		return conversationCardDAO;
	}

	public void setConversationCardDAO(ConversationCardDAO conversationCardDAO) {
		this.conversationCardDAO = conversationCardDAO;
	}

	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession(true);
		JsonHeader jh = (JsonHeader) session.getAttribute("jsonHeader");

		String cardid = jh.getCardid();
		Long cardidLong = new Long(cardid);

		String userid = jh.getUserid();
		Long useridLong = new Long(userid);

		String jsonbody = (String) session.getAttribute("jsonbody");
		Gson gson = new Gson();
		JSONStreetBuzzSearch jsbs = gson.fromJson(jsonbody,
				JSONStreetBuzzSearch.class);
		Long interestid = jsbs.getInterestid();
		String searchterm = jsbs.getSearchterm();
		searchterm = searchterm.trim();
		searchterm = searchterm.toLowerCase();
		List cardlist = conversationCardDAO.searchCards(interestid,searchterm);
		String responseStr = gson.toJson(cardlist);

		response.setStatus(200);
		response.addHeader("Access-Control-Allow-Origin", "*");
		PrintWriter writer = response.getWriter();
		writer.write(responseStr);
		writer.close();
		return null;
	}
}
