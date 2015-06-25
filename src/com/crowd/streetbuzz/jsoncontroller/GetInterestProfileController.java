/**
 * 
 */
package com.crowd.streetbuzz.jsoncontroller;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.crowd.streetbuzz.common.Constants;
import com.crowd.streetbuzz.dao.implementation.CategoryMasterDAO;
import com.crowd.streetbuzz.json.JsonHeader;
import com.crowd.streetbuzz.json.JsonInterestProfileRequest;
import com.crowd.streetbuzz.model.CategoryMaster;
import com.crowd.streetbuzzalgo.interestdetection.InterestDetector;
import com.crowd.streetbuzzalgo.utils.StrUtil;
import com.google.gson.Gson;

/**
 * @author Atrijit
 *
 */
public class GetInterestProfileController implements Controller, Constants{
	private CategoryMasterDAO categoryMasterDAO;
	
	public CategoryMasterDAO getCategoryMasterDAO() {
		return categoryMasterDAO;
	}

	public void setCategoryMasterDAO(CategoryMasterDAO categoryMasterDAO) {
		this.categoryMasterDAO = categoryMasterDAO;
	}

	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession(true);
		JsonHeader jsonHeader = (JsonHeader)session.getAttribute("jsonHeader");
		String jsonbody = StrUtil.nonNull((String) session
				.getAttribute("jsonbody"));
		Gson gson = new Gson();
		JsonInterestProfileRequest jipr = new JsonInterestProfileRequest();
		try {
			jipr = gson.fromJson(jsonbody, JsonInterestProfileRequest.class);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		String topic = StrUtil.nonNull(jipr.getConversationtopic());
		topic = topic.trim();
		
		List interestlist =  new ArrayList();
		try {
			interestlist = InterestDetector.detectInterest(topic);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(interestlist!=null && interestlist.size()>0){
			Collections.sort(interestlist);
		}
		
		List finalList = new ArrayList();
		for (int i=0;i<interestlist.size();i++){
			Long interest = (Long)interestlist.get(i);
			CategoryMaster cm = (CategoryMaster)categoryMasterDAO.getObjectById(interest);
			cm.setPriority(new Long(i+1));
			finalList.add(cm);
		}
		if(finalList.size()==0){
			finalList = categoryMasterDAO.getAllRecords();
		}
		
		String responseString = gson.toJson(finalList);
		response.setStatus(200);
		response.addHeader("Access-Control-Allow-Origin", "*");
		PrintWriter writer = response.getWriter();
		writer.write(responseString);
		writer.close();
		return null;
	}
}
