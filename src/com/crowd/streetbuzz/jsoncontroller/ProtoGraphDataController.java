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
import com.crowd.streetbuzz.dao.implementation.ProtoSearchCardDAO;
import com.crowd.streetbuzz.dao.implementation.ProtoVoicesDAO;
import com.crowd.streetbuzz.dao.implementation.ProtoVoicesDetailsDAO;
import com.crowd.streetbuzz.json.JsonHeader;
import com.crowd.streetbuzz.json.JsonRequestBody;
import com.crowd.streetbuzzalgo.utils.GraphDataUtils;
import com.crowd.streetbuzzalgo.utils.StrUtil;
import com.google.gson.Gson;

/**
 * @author Atrijit
 *
 */
public class ProtoGraphDataController implements Controller, Constants{
	private ProtoSearchCardDAO protoSearchCardDAO;

	private ProtoVoicesDAO protoVoicesDAO;

	private ProtoVoicesDetailsDAO protoVoicesDetailsDAO;
	
	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		System.out.println("Inside ProtoGraphDataController ready to spotate");
		HttpSession session = request.getSession(true);
		JsonHeader jsonHeader = (JsonHeader) session.getAttribute("jsonHeader");
		// Get the card id for which data needed
		String cardid = jsonHeader.getCardid();
		// Get the card type
		String type = jsonHeader.getCardtype();

		// Get the unique id also just in case
		String carduniqueid = jsonHeader.getCarduniqueid();
		
		String jsonbody = (String)session.getAttribute("jsonbody");
		Gson gson = new Gson();
		JsonRequestBody jrb = gson.fromJson(jsonbody, JsonRequestBody.class);
		
		String channel = StrUtil.nonNull(jrb.getChannel());

		// We also need the voiceid for this to work
		String voiceid = StrUtil.nonNull(jsonHeader.getVoiceid());
		String retStr = "";
		if (!"".equalsIgnoreCase(voiceid)) {
			// Request is for a particular voice
			Long voiceidLong = new Long(voiceid);
			//TODO
			retStr = GraphDataUtils.getVoicesData(protoVoicesDAO, protoVoicesDetailsDAO,
					voiceidLong);
			//retStr = GraphDataUtils.sendVoiceDummyData();

		} else {
			//TODO
			retStr = GraphDataUtils.getCardDataAlt(protoVoicesDAO, protoVoicesDetailsDAO,
					carduniqueid,channel,protoSearchCardDAO);
			//retStr = GraphDataUtils.sendDummyData();

		}

		response.setStatus(200);
		response.addHeader("Access-Control-Allow-Origin", "*");
		PrintWriter writer = response.getWriter();
		System.out.println("************************Being sent from GraphDataController************************");
		System.out.println(retStr);
		writer.write(retStr);
		writer.close();
		return null;
	}

	public ProtoSearchCardDAO getProtoSearchCardDAO() {
		return protoSearchCardDAO;
	}

	public void setProtoSearchCardDAO(ProtoSearchCardDAO protoSearchCardDAO) {
		this.protoSearchCardDAO = protoSearchCardDAO;
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
