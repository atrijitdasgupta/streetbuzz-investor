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
import com.crowd.streetbuzz.dao.implementation.ProcessIDDAO;
import com.crowd.streetbuzz.json.JsonHeader;
import com.crowd.streetbuzz.model.ProcessID;
import com.crowd.streetbuzzalgo.utils.StrUtil;
import com.google.gson.Gson;

/**
 * @author Atrijit
 *
 */
public class EndPointController implements Controller, Constants{
	private ProcessIDDAO processIDDAO;
	
	
	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		System.out.println("Inside EndPointController handleRequest method");
		boolean noerror = true;
		String jsonheader = StrUtil.nonNull(request.getParameter(JSONHEADERPARAM));
		String jsonbody = StrUtil.nonNull(request.getParameter(JSONBODYPARAM));
		System.out.println("jsonheader: "+jsonheader);
		System.out.println("jsonbody: "+jsonbody);
		if("".equalsIgnoreCase(jsonheader)){
			PrintWriter writer = response.getWriter();
			noerror = false;
			String errMsg = "Empty JSON header";
			response.setStatus(500);
			writer.write(STANDARERRORRESPONSE+errMsg);
			return null;
		}
		Gson gson = new Gson();
		JsonHeader jsonHeader;
		try {
			jsonHeader = gson.fromJson(jsonheader, JsonHeader.class);
			System.out
			.println(jsonHeader.getAfterid() + ":" + jsonHeader.getCardid() + ":"
					+ jsonHeader.getCardtype() + ":" + jsonHeader.getLastupdate() + ":"
					+ jsonHeader.getLatitude() + ":" + jsonHeader.getLongitude() + ":"
					+ jsonHeader.getProcessid() + ":" + jsonHeader.getSocialnetwork() + ":"
					+ jsonHeader.getSource() + ":" + jsonHeader.getUserid() + ":"
					+ jsonHeader.getVoiceid());
			
		} catch (Exception e) {
			
			PrintWriter writer = response.getWriter();
			noerror = false;
			String errMsg = e.getMessage();
			response.setStatus(500);
			writer.write(STANDARERRORRESPONSE+errMsg);
			writer.close();
			return null;
		}
		String forwardPath = FORWARDDDENDUM+processJsonHeader(jsonHeader);
		HttpSession session = request.getSession(true);
		session.setAttribute("jsonHeader", jsonHeader);
		session.setAttribute("jsonbody", jsonbody);
		request.getRequestDispatcher(forwardPath).forward(request, response);
		return null;
	}
	
	private String processJsonHeader(JsonHeader jsonHeader){
		String processid = StrUtil.nonNull(jsonHeader.getProcessid());
		System.out.println("Inside processJsonHeader processid: "+processid);
		processid = processid.trim();
		ProcessID processID = (ProcessID)processIDDAO.getObjectByProcessId(processid);
	
		String forwardpath =  "";
		if(processID!=null){
			forwardpath = StrUtil.nonNull(processID.getForwardpath());
		}
		
		System.out.println("Inside processJsonHeader forwardpath: "+forwardpath);
		return forwardpath;
	}
	
	public ProcessIDDAO getProcessIDDAO() {
		return processIDDAO;
	}

	public void setProcessIDDAO(ProcessIDDAO processIDDAO) {
		this.processIDDAO = processIDDAO;
	}
}
