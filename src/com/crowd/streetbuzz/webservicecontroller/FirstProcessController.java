/**
 * 
 */
package com.crowd.streetbuzz.webservicecontroller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.crowd.streetbuzz.common.Constants;
import com.crowd.streetbuzz.dao.implementation.ProcessADAO;
import com.crowd.streetbuzz.helper.FirstProcessHelper;
import com.crowd.streetbuzz.model.ProcessA;
import com.crowd.streetbuzzalgo.utils.StrUtil;

/**
 * @author Atrijit
 * 
 */
public class FirstProcessController implements Controller, Constants {
	private String returnView = "";
	private ProcessADAO processADAO;

	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String entry = StrUtil.nonNull(request.getParameter("entry"));
		String category = StrUtil.nonNull(request.getParameter("category"));
		ProcessA aclass = new ProcessA();
		aclass.setCategory(category);
		aclass.setText(entry);
		Long time = System.currentTimeMillis();
		String timeStr = time.toString();
		aclass.setUniqueid(timeStr);
		processADAO.addOrUpdateRecord(aclass);
		FirstProcessHelper firstProcessHelper = new FirstProcessHelper();
		HttpSession session = firstProcessHelper.process(entry, category,request);
		session.setAttribute("entry", entry);
		session.setAttribute("uniquesessionid", timeStr);
		returnView = FIRSTPROCESSRESULTPAGE;
		return new ModelAndView(returnView);
	}

	public ProcessADAO getProcessADAO() {
		return processADAO;
	}

	public void setProcessADAO(ProcessADAO processADAO) {
		this.processADAO = processADAO;
	}

	
}
