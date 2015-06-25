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
import com.crowd.streetbuzz.dao.implementation.CardShareDAO;
import com.crowd.streetbuzz.json.CardAndVoiceShareResponse;
import com.crowd.streetbuzz.json.JsonHeader;
import com.crowd.streetbuzz.model.CardShare;
import com.crowd.streetbuzzalgo.utils.StrUtil;
import com.google.gson.Gson;

/**
 * @author Atrijit
 *
 */
public class ShareCardController implements Controller, Constants{
	private CardShareDAO cardShareDAO;
	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession(true);
		JsonHeader jsonHeader = (JsonHeader) session.getAttribute("jsonHeader");
		String cardidStr = StrUtil.nonNull(jsonHeader.getCardid());
		Long cardid = new Long(0);
		if (!"".equalsIgnoreCase(cardidStr)) {
			cardid = new Long(cardidStr);
		}
		String useridStr = StrUtil.nonNull(jsonHeader.getUserid());
		Long userid = new Long(0);
		if(!"".equalsIgnoreCase(useridStr)){
			userid = new Long(useridStr);
		}
		CardShare cs = new CardShare();
		cs.setCardid(cardid);
		cs.setUserid(userid);
		cs.setSharedate(new Date());
		cardShareDAO.addOrUpdateRecord(cs);
		
		List sharelist = cardShareDAO.getAllSharesforCardId(cardid);
		int count = 0;
		if(sharelist!=null){
			count = sharelist.size();
		}
		Long countL = new Long(count);
		CardAndVoiceShareResponse cvsr = new CardAndVoiceShareResponse();
		cvsr.setTotalshare(countL);
		Gson gson = new Gson();
		String responseString = gson.toJson(cvsr);
		
		response.setStatus(200);
		response.addHeader("Access-Control-Allow-Origin", "*");
		PrintWriter writer = response.getWriter();
		writer.write(responseString);
		writer.close();
		return null;
	}
	public CardShareDAO getCardShareDAO() {
		return cardShareDAO;
	}
	public void setCardShareDAO(CardShareDAO cardShareDAO) {
		this.cardShareDAO = cardShareDAO;
	}


}
