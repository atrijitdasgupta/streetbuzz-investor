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
import com.crowd.streetbuzz.dao.implementation.VoicesDAO;
import com.crowd.streetbuzz.json.JsonHeader;
import com.crowd.streetbuzz.model.Voices;
import com.crowd.streetbuzzalgo.utils.StrUtil;

/**
 * @author Atrijit
 *
 */
public class RefreshVoiceController implements Controller, Constants{
	private VoicesDAO voicesDAO;
	public VoicesDAO getVoicesDAO() {
		return voicesDAO;
	}
	public void setVoicesDAO(VoicesDAO voicesDAO) {
		this.voicesDAO = voicesDAO;
	}
	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession(true);
		JsonHeader jh = (JsonHeader)session.getAttribute("jsonHeader");
		
		String voiceidStr = jh.getVoiceid();
		String useridStr = jh.getUserid();
		String cardidStr = jh.getCardid();
		
		Long voiceid = new Long(voiceidStr);
		Long cardid = new Long(cardidStr);
		Long userid = new Long(useridStr);
		
		Voices voices = (Voices)voicesDAO.getObjectById(voiceid);
		String url = StrUtil.nonNull(voices.getSourcelink());
		
		PrintWriter writer = response.getWriter();
		response.setStatus(200);
		response.addHeader("Access-Control-Allow-Origin", "*");
		writer.write("");
		writer.close();
		return null;
	}
}
