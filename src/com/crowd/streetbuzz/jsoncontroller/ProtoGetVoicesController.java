/**
 * 
 */
package com.crowd.streetbuzz.jsoncontroller;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.crowd.streetbuzz.common.Constants;
import com.crowd.streetbuzz.dao.implementation.ProtoVoicesDAO;
import com.crowd.streetbuzz.dao.implementation.ProtoVoicesDetailsDAO;
import com.crowd.streetbuzz.json.JsonHeader;
import com.crowd.streetbuzz.json.JsonRequestBody;
import com.crowd.streetbuzz.model.ProtoVoices;
import com.crowd.streetbuzz.model.ProtoVoicesDetails;
import com.crowd.streetbuzzalgo.utils.StrUtil;
import com.google.gson.Gson;

/**
 * @author Atrijit
 *
 */
public class ProtoGetVoicesController implements Controller, Constants{
	private ProtoVoicesDAO protoVoicesDAO;

	private ProtoVoicesDetailsDAO protoVoicesDetailsDAO;
	
	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		System.out.println("Inside ProtoGetVoicesController ready to spotate");
		HttpSession session = request.getSession(true);
		JsonHeader jsonHeader = (JsonHeader) session.getAttribute("jsonHeader");
		String cardid = jsonHeader.getCardid();
		String cardtype = jsonHeader.getCardtype();
		String carduniqueid = jsonHeader.getCarduniqueid();
		String afterid = StrUtil.nonNull(jsonHeader.getAfterid());
		
		String jsonbody = (String)session.getAttribute("jsonbody");
		Gson gson = new Gson();
		JsonRequestBody jrb = gson.fromJson(jsonbody, JsonRequestBody.class);
		//String source = StrUtil.nonNull(jrb.getSource());
		String channel = StrUtil.nonNull(jrb.getChannel());
		channel = channel.trim();
		
		List voicesList = new ArrayList();
		
		if("".equalsIgnoreCase(channel)){
			voicesList = protoVoicesDAO.getAllRecordsbyCardUniqueId(carduniqueid);
		}else{
			voicesList = protoVoicesDAO.getAllRecordsbyCardUniqueIdAndChannel(carduniqueid, channel);
		}

		
		List listforJson = new ArrayList();
		StringBuffer sbfr = new StringBuffer();
		String temp = "";
		for (int i = 0; i < voicesList.size(); i++) {
			ProtoVoices voices = (ProtoVoices) voicesList.get(i);
			sbfr.append(temp);
			Long voicesid = voices.getId();
			List voiceDetailsList = new ArrayList();
			if ("".equalsIgnoreCase(afterid)) {
				voiceDetailsList = protoVoicesDetailsDAO
						.getAllRecordsbyVoices(voicesid);
			} else {
				Long afteridLong = new Long(afterid);
				voiceDetailsList = protoVoicesDetailsDAO
						.getAllRecordsbyVoicesAfterId(voicesid, afteridLong);
			}
			if(voiceDetailsList!=null && voiceDetailsList.size() > 0){
				List voiceDetailsListToAdd = new ArrayList();
				for (int j=0;j<voiceDetailsList.size();j++){
					ProtoVoicesDetails pvd = (ProtoVoicesDetails)voiceDetailsList.get(j);
					Date vdate = pvd.getVoicedate();
					int daysagov = StrUtil.getDaysAgo(vdate);
					pvd.setDaysago(daysagov);
					voiceDetailsListToAdd.add(pvd);
				}
				voices.setComments(voiceDetailsListToAdd);
			}
			Date date = voices.getVoicesdate();
			int daysago = StrUtil.getDaysAgo(date);
			voices.setDaysago(daysago);
			//temp = gson.toJson(voices);
			listforJson.add(voices);
		//	sbfr.append(temp);
		}
		//String responseString = sbfr.toString();
		String responseString = gson.toJson(listforJson);
		//String responseString = dummy();
		System.out.println("responseString: " + responseString);
		response.addHeader("Access-Control-Allow-Origin", "*");
		response.setStatus(200);
		PrintWriter writer = response.getWriter();
		writer.write(responseString);
		writer.close();

		return null;
	}

	public ProtoVoicesDAO getProtoVoicesDAO() {
		return protoVoicesDAO;
	}

	public void setProtoVoicesDAO(ProtoVoicesDAO protoVoicesDAO) {
		this.protoVoicesDAO = protoVoicesDAO;
	}

	public ProtoVoicesDetailsDAO getProtoVoicesDetailsDAO() {
		return protoVoicesDetailsDAO;
	}

	public void setProtoVoicesDetailsDAO(ProtoVoicesDetailsDAO protoVoicesDetailsDAO) {
		this.protoVoicesDetailsDAO = protoVoicesDetailsDAO;
	}
}
