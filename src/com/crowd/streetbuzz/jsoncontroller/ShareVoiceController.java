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
import com.crowd.streetbuzz.dao.implementation.VoiceShareDAO;
import com.crowd.streetbuzz.json.CardAndVoiceShareResponse;
import com.crowd.streetbuzz.json.JsonHeader;
import com.crowd.streetbuzz.json.VoiceShareIncomingJSON;
import com.crowd.streetbuzz.model.VoiceShare;
import com.crowd.streetbuzzalgo.utils.StrUtil;
import com.google.gson.Gson;

public class ShareVoiceController implements Controller, Constants{
	private VoiceShareDAO voiceShareDAO;
	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession(true);
		JsonHeader jsonHeader = (JsonHeader) session.getAttribute("jsonHeader");
		String useridStr = StrUtil.nonNull(jsonHeader.getUserid());
		Long userid = new Long(0);
		if(!"".equalsIgnoreCase(useridStr)){
			userid = new Long(useridStr);
		}
		String jsonbody = (String)session.getAttribute("jsonbody");
		Gson gson = new Gson();
		VoiceShareIncomingJSON vsij = gson.fromJson(jsonbody, VoiceShareIncomingJSON.class);
		String voiceidStr = StrUtil.nonNull(vsij.getVoiceid());
		Long voiceid = new Long(0);
		if(!"".equalsIgnoreCase(voiceidStr)){
			voiceid = new Long(voiceidStr);
		}
		VoiceShare vs = new VoiceShare();
		vs.setUserid(userid);
		vs.setVoiceid(voiceid);
		vs.setSharedate(new Date());
		voiceShareDAO.addOrUpdateRecord(vs);
		List sharelist = voiceShareDAO.getAllSharesForVoiceid(voiceid);
		int count = 0;
		if(sharelist!=null){
			count = sharelist.size();
		}
		Long countL = new Long(count);
		CardAndVoiceShareResponse cvsr = new CardAndVoiceShareResponse();
		cvsr.setTotalshare(countL);
		String responseString = gson.toJson(cvsr);
		response.setStatus(200);
		response.addHeader("Access-Control-Allow-Origin", "*");
		PrintWriter writer = response.getWriter();
		writer.write("");
		writer.close();
		return null;
	}
	public VoiceShareDAO getVoiceShareDAO() {
		return voiceShareDAO;
	}
	public void setVoiceShareDAO(VoiceShareDAO voiceShareDAO) {
		this.voiceShareDAO = voiceShareDAO;
	}
}
