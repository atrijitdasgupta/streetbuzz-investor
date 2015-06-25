/**
 * 
 */
package com.crowd.streetbuzz.webservicecontroller;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.crowd.streetbuzz.common.Constants;
import com.crowd.streetbuzz.dao.implementation.PageTestDAO;
import com.crowd.streetbuzz.model.PageTest;
import com.crowd.streetbuzzalgo.utils.StrUtil;

/**
 * @author Atrijit
 * 
 */
public class PageTestController implements Controller, Constants {
	private PageTestDAO pageTestDAO;

	public PageTestDAO getPageTestDAO() {
		return pageTestDAO;
	}

	public void setPageTestDAO(PageTestDAO pageTestDAO) {
		this.pageTestDAO = pageTestDAO;
	}

	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String perpage = StrUtil.nonNull(request.getParameter("perpage"));
		String beforeid = StrUtil.nonNull(request.getParameter("beforeid"));
		String afterid = StrUtil.nonNull(request.getParameter("afterid"));
		String categorystr = StrUtil.nonNull(request.getParameter("category"));
		int perpageint = new Integer(perpage).intValue();
		Long beforeidLong = new Long(0);
		if(!"".equalsIgnoreCase(beforeid)){
			beforeidLong = new Long(beforeid);
		}
		Long afteridLong = new Long(0);
		if(!"".equalsIgnoreCase(afterid)){
			afteridLong = new Long(afterid);
		}
		Long category = new Long(1);
		if("2".equalsIgnoreCase(categorystr)){
			category = new Long(2);
		}
		
		List results = new ArrayList();
		if ("".equalsIgnoreCase(beforeid) && "".equalsIgnoreCase(afterid)) {
			results = pageTestDAO.getAllRecordsPerPage(perpageint, category);
		}
		
//		client scrolling down
		if ("".equalsIgnoreCase(beforeid) && !"".equalsIgnoreCase(afterid)) {
			
			results = pageTestDAO.getAllRecordsPerPageAfterId(perpageint, afteridLong, category);
		}
		
		//client scrolling up
		if (!"".equalsIgnoreCase(beforeid) && "".equalsIgnoreCase(afterid)) {
			results = pageTestDAO.getAllRecordsPerPageBeforeId(perpageint,beforeidLong, category);
		}
		
		List finallist = new ArrayList();
		int counter = 0;
		for (int i=0;i<results.size();i++){
			PageTest pt = (PageTest)results.get(i);
			String name = pt.getName();
			Long cat = pt.getCategory();
			System.out.println(name+" "+cat.toString());
		}
		/*for (int i=0;i<results.size();i++){
			PageTest pt = (PageTest)results.get(i);
			Long id = pt.getId();
			String name = pt.getName();
			if(counter == perpageint){
				break;
			}
			finallist.add(name);
			counter =  counter+1;
			
		}*/
		//System.out.println(finallist);
		response.setStatus(200);
		response.addHeader("Access-Control-Allow-Origin", "*");
		PrintWriter writer = response.getWriter();
		writer.write("");
		writer.close();
		return null;
	}
}
