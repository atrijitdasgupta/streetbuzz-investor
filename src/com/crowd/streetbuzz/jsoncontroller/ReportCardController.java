/**
 * 
 */
package com.crowd.streetbuzz.jsoncontroller;

import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.crowd.streetbuzz.common.Constants;
import com.crowd.streetbuzz.dao.implementation.ConversationCardDAO;
import com.crowd.streetbuzz.dao.implementation.UserActivitiesDAO;
import com.crowd.streetbuzz.json.JsonHeader;
import com.crowd.streetbuzz.model.ConversationCard;
import com.crowd.streetbuzz.model.UserActivities;

/**
 * @author Atrijit
 *
 */
public class ReportCardController implements Controller, Constants{
	private UserActivitiesDAO userActivitiesDAO;
	private ConversationCardDAO conversationCardDAO;
	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession(true);
		JsonHeader jh = (JsonHeader)session.getAttribute("jsonHeader");
		
		String cardid = jh.getCardid();
		Long cardidLong = new Long(cardid);
		String userid = jh.getUserid();
		Long useridLong = new Long(userid);
		
		ConversationCard entity = (ConversationCard)conversationCardDAO.getObjectById(cardidLong);
		String entityuseridStr = entity.getUserid();
		Long entityuserid = new Long(entityuseridStr);
		
		UserActivities uaone = new UserActivities();
		uaone.setActionarea("");
		uaone.setActiondatetime(new Date());
		uaone.setActiondetails("Reported card id "+cardid+" from user "+userid);
		uaone.setActiontype(ACTION_REPORT);
		uaone.setEntityid(cardidLong);
		uaone.setEntityuserid(entityuserid);
		uaone.setPoints(new Long(ACTION_REPORT_VALUE));
		uaone.setUserid(useridLong);
		
		UserActivities uatwo = new UserActivities();
		uatwo.setActionarea("");
		uatwo.setActiondatetime(new Date());
		uatwo.setActiondetails("Got card id "+cardid+" reported by user "+userid);
		uatwo.setActiontype(ACTION_GET_REPORTED);
		uatwo.setEntityid(cardidLong);
		uatwo.setEntityuserid(entityuserid);
		uatwo.setPoints(new Long(ACTION_GET_REPORTED_VALUE));
		uatwo.setUserid(useridLong);
		
		userActivitiesDAO.addOrUpdateRecord(uaone);
		userActivitiesDAO.addOrUpdateRecord(uatwo);
		
		
		response.setStatus(200);
		response.addHeader("Access-Control-Allow-Origin", "*");
		PrintWriter writer = response.getWriter();
		writer.write("");
		writer.close();
		return null;
	}
	public UserActivitiesDAO getUserActivitiesDAO() {
		return userActivitiesDAO;
	}
	public void setUserActivitiesDAO(UserActivitiesDAO userActivitiesDAO) {
		this.userActivitiesDAO = userActivitiesDAO;
	}
	public ConversationCardDAO getConversationCardDAO() {
		return conversationCardDAO;
	}
	public void setConversationCardDAO(ConversationCardDAO conversationCardDAO) {
		this.conversationCardDAO = conversationCardDAO;
	}

}
