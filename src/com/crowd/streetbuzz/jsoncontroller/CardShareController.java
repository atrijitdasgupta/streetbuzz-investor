/**
 * 
 */
package com.crowd.streetbuzz.jsoncontroller;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.crowd.streetbuzz.common.Constants;
import com.crowd.streetbuzz.dao.implementation.ConversationCardDAO;
import com.crowd.streetbuzz.model.ConversationCard;
import com.crowd.streetbuzzalgo.utils.StrUtil;

/**
 * @author Atrijit
 * 
 */
public class CardShareController implements Controller, Constants {
	private ConversationCardDAO conversationCardDAO;

	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		/*String cardidstr = StrUtil.nonNull(request.getParameter("id"));
		Long cardid = new Long(0);
		if (!"".equalsIgnoreCase(cardidstr)) {
			cardid = new Long(cardidstr);
		}
		ConversationCard conversationCard = (ConversationCard) conversationCardDAO
				.getObjectById(cardid);
		HttpSession session = request.getSession(true);
		session.setAttribute("conversationCard", conversationCard);*/

		String returnView = UNDERCONSTRUCTIONPAGE;
		//System.out.println("Sending from CardShareController for card "+cardidstr+" to "+CARDSHAREPAGE);
		return new ModelAndView(returnView);
	}

	public ConversationCardDAO getConversationCardDAO() {
		return conversationCardDAO;
	}

	public void setConversationCardDAO(ConversationCardDAO conversationCardDAO) {
		this.conversationCardDAO = conversationCardDAO;
	}
}
