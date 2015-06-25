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
import com.crowd.streetbuzz.dao.implementation.ConversationCardDAO;
import com.crowd.streetbuzz.dao.implementation.UserDAO;
import com.crowd.streetbuzz.model.ConversationCard;
import com.crowd.streetbuzz.model.User;
import com.crowd.streetbuzzalgo.utils.StrUtil;

/**
 * @author Atrijit
 * 
 */
public class DeleteUserController implements Controller, Constants {
	private String returnView = "";

	private UserDAO userDAO;

	private ConversationCardDAO conversationCardDAO;

	public ConversationCardDAO getConversationCardDAO() {
		return conversationCardDAO;
	}

	public void setConversationCardDAO(ConversationCardDAO conversationCardDAO) {
		this.conversationCardDAO = conversationCardDAO;
	}

	public UserDAO getUserDAO() {
		return userDAO;
	}

	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String email = StrUtil.nonNull(request.getParameter("email"));
		User user = (User) userDAO.getObjectByEmailId(email);
		List cclist = new ArrayList();
		if(user!=null){
			try {
				cclist = conversationCardDAO.getAllRecordsOfUser(user.getId()
						.toString());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if (cclist.size() > 0) {
			for (int i = 0; i < cclist.size(); i++) {
				ConversationCard cc = (ConversationCard) cclist.get(i);
				conversationCardDAO.deleteRecord(cc);
			}

		}
		String msg = "Deleted user with email id " + email + " successfully with his cards";
		if (user != null) {
			userDAO.deleteRecord(user);
		} else {
			msg = "No such user exists, pl check the email id";
		}

		returnView = "pages/delete.jsp?p=" + msg;

		return new ModelAndView(returnView);
	}
}
