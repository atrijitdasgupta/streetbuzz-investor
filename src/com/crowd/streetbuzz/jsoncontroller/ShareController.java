/**
 * 
 */
package com.crowd.streetbuzz.jsoncontroller;

import java.io.PrintWriter;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.crowd.streetbuzz.common.Constants;
import com.crowd.streetbuzz.dao.implementation.ConversationCardDAO;
import com.crowd.streetbuzz.dao.implementation.UserActivitiesDAO;
import com.crowd.streetbuzz.dao.implementation.VoicesDAO;
import com.crowd.streetbuzz.json.JsonHeader;
import com.crowd.streetbuzz.model.ConversationCard;
import com.crowd.streetbuzz.model.UserActivities;
import com.crowd.streetbuzz.model.Voices;
import com.crowd.streetbuzzalgo.utils.StrUtil;

/**
 * @author Atrijit
 * 
 */
public class ShareController implements Controller, Constants {
	private UserActivitiesDAO userActivitiesDAO;

	private ConversationCardDAO conversationCardDAO;

	private VoicesDAO voicesDAO;

	public VoicesDAO getVoicesDAO() {
		return voicesDAO;
	}

	public void setVoicesDAO(VoicesDAO voicesDAO) {
		this.voicesDAO = voicesDAO;
	}

	public ConversationCardDAO getConversationCardDAO() {
		return conversationCardDAO;
	}

	public void setConversationCardDAO(ConversationCardDAO conversationCardDAO) {
		this.conversationCardDAO = conversationCardDAO;
	}

	public UserActivitiesDAO getUserActivitiesDAO() {
		return userActivitiesDAO;
	}

	public void setUserActivitiesDAO(UserActivitiesDAO userActivitiesDAO) {
		this.userActivitiesDAO = userActivitiesDAO;
	}

	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession(true);
		JsonHeader jh = (JsonHeader) session.getAttribute("jsonHeader");
		String useridStr = StrUtil.nonNull(jh.getUserid());
		String cardidStr = StrUtil.nonNull(jh.getCardid());
		String voiceidStr = StrUtil.nonNull(jh.getVoiceid());

		Long userid = new Long(0);
		Long cardid = new Long(0);
		Long voiceid = new Long(0);

		if (!"".equalsIgnoreCase(useridStr)) {
			userid = new Long(useridStr);
		}
		if (!"".equalsIgnoreCase(cardidStr)) {
			cardid = new Long(cardidStr);
		}
		if (!"".equalsIgnoreCase(voiceidStr)) {
			voiceid = new Long(voiceidStr);
		}
		ConversationCard cc = null;
		if (!cardid.equals(0)) {
			cc = (ConversationCard) conversationCardDAO.getObjectById(cardid);
		}

		String useridccStr = StrUtil.nonNull(cc.getUserid());
		Long useridcc = new Long(useridccStr);
		Voices voices = null;
		if (!voiceid.equals(0)) {
			voices = (Voices) voicesDAO.getObjectById(voiceid);
		}
		if ("".equalsIgnoreCase(useridccStr)) {
			Long uid = voices.getUserid();
			useridcc = uid;
		}

		String label = "";
		Long id = new Long(0);
		String action = "";
		String contraaction = "";
		int actionscore = 0;
		int contraactionscore = 0;

		if ("".equalsIgnoreCase(voiceidStr)) {
			label = "Card";
			id = cardid;
			action = ACTION_SHARE_CARD;
			contraaction = ACTION_GET_SHARED_CARD;
			actionscore = ACTION_SHARE_CARD_VALUE;
			contraactionscore = ACTION_GET_SHARED_CARD_VALUE;
		} else {
			label = "Voice";
			id = voiceid;
			action = ACTION_SHARE_VOICE;
			contraaction = ACTION_GET_SHARED_VOICE;
			actionscore = ACTION_SHARE_VOICE_VALUE;
			contraactionscore = ACTION_GET_SHARED_VOICE_VALUE;
		}

		UserActivities uaone = new UserActivities();
		uaone.setActionarea("");
		uaone.setActiondatetime(new Date());
		uaone.setActiondetails("Shared " + label + " id " + cardid
				+ " from user " + userid);
		uaone.setActiontype(action);
		uaone.setEntityid(id);
		uaone.setEntityuserid(useridcc);
		uaone.setPoints(new Long(actionscore));
		uaone.setUserid(userid);

		UserActivities uatwo = new UserActivities();
		uatwo.setActionarea("");
		uatwo.setActiondatetime(new Date());
		uatwo.setActiondetails("Got " + label + " id " + cardid
				+ " shared by user " + userid);
		uatwo.setActiontype(contraaction);
		uatwo.setEntityid(id);
		uatwo.setEntityuserid(userid);
		uatwo.setPoints(new Long(contraactionscore));
		uatwo.setUserid(useridcc);

		userActivitiesDAO.addOrUpdateRecord(uaone);
		userActivitiesDAO.addOrUpdateRecord(uatwo);

		// Send back total number of shares for this card
		List sharelist = userActivitiesDAO.getAllShareRecordsforEntity(id,
				action);
		int numsharesforentity = 0;
		if (sharelist != null) {
			numsharesforentity = sharelist.size();
		}

		String numsharesforentityStr = new Integer(numsharesforentity)
				.toString();

		response.setStatus(200);
		response.addHeader("Access-Control-Allow-Origin", "*");
		PrintWriter writer = response.getWriter();
		writer.write(numsharesforentityStr);
		writer.close();
		return null;
	}

}
