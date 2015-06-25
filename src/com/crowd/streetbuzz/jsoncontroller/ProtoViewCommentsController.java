/**
 * 
 */
package com.crowd.streetbuzz.jsoncontroller;

import java.io.PrintWriter;
import java.util.ArrayList;
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
import com.crowd.streetbuzzalgo.utils.StrUtil;
import com.google.gson.Gson;

/**
 * @author Atrijit
 *
 */
public class ProtoViewCommentsController implements Controller, Constants{
	private ProtoVoicesDAO protoVoicesDAO;

	private ProtoVoicesDetailsDAO protoVoicesDetailsDAO;
	
	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		System.out.println("Inside ProtoViewCommentsController ready to spotate");
		HttpSession session = request.getSession(true);
		JsonHeader jsonHeader = (JsonHeader) session.getAttribute("jsonHeader");
		String voicesid = jsonHeader.getVoiceid();
		String afterid = StrUtil.nonNull(jsonHeader.getAfterid());
		
		Long voicesidLong = new Long(voicesid);
		List voiceDetailsList = new ArrayList();
		if ("".equalsIgnoreCase(afterid)) {
			voiceDetailsList = protoVoicesDetailsDAO
					.getAllRecordsbyVoices(voicesidLong);
		} else {
			Long afteridLong = new Long(afterid);
			voiceDetailsList = protoVoicesDetailsDAO
					.getAllRecordsbyVoicesAfterId(voicesidLong, afteridLong);
		}
		Gson gson = new Gson();

		String responseString = gson.toJson(voiceDetailsList);
	//	String responseString = dummy();
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
