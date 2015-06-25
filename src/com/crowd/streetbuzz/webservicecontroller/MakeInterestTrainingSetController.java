/**
 * 
 */
package com.crowd.streetbuzz.webservicecontroller;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.crowd.streetbuzz.common.Constants;
import com.crowd.streetbuzz.dao.implementation.CategoryMasterDAO;
import com.crowd.streetbuzz.dao.implementation.InterestKeywordsDAO;
import com.crowd.streetbuzz.dao.implementation.InterestReferenceModelDAO;
import com.crowd.streetbuzz.dao.implementation.InterestSearchTermsDAO;
import com.crowd.streetbuzz.model.CategoryMaster;
import com.crowd.streetbuzz.model.InterestKeywords;
import com.crowd.streetbuzz.model.InterestReferenceModel;
import com.crowd.streetbuzzalgo.utils.DataClean;
import com.crowd.streetbuzzalgo.utils.StrUtil;

/**
 * @author Atrijit
 *
 */
public class MakeInterestTrainingSetController implements Controller, Constants {
	private InterestSearchTermsDAO interestSearchTermsDAO;

	private InterestReferenceModelDAO interestReferenceModelDAO;

	private InterestKeywordsDAO interestKeywordsDAO;
	
	private CategoryMasterDAO categoryMasterDAO;
	
	private static final String TRAININGDIRPATH = "/home/streetbuzz/interests/training/";
	
	public CategoryMasterDAO getCategoryMasterDAO() {
		return categoryMasterDAO;
	}

	public void setCategoryMasterDAO(CategoryMasterDAO categoryMasterDAO) {
		this.categoryMasterDAO = categoryMasterDAO;
	}

	public InterestKeywordsDAO getInterestKeywordsDAO() {
		return interestKeywordsDAO;
	}

	public void setInterestKeywordsDAO(InterestKeywordsDAO interestKeywordsDAO) {
		this.interestKeywordsDAO = interestKeywordsDAO;
	}

	public InterestReferenceModelDAO getInterestReferenceModelDAO() {
		return interestReferenceModelDAO;
	}

	public void setInterestReferenceModelDAO(
			InterestReferenceModelDAO interestReferenceModelDAO) {
		this.interestReferenceModelDAO = interestReferenceModelDAO;
	}

	public InterestSearchTermsDAO getInterestSearchTermsDAO() {
		return interestSearchTermsDAO;
	}

	public void setInterestSearchTermsDAO(
			InterestSearchTermsDAO interestSearchTermsDAO) {
		this.interestSearchTermsDAO = interestSearchTermsDAO;
	}
/*
 * (non-Javadoc)
 * @see org.springframework.web.servlet.mvc.Controller#handleRequest(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
 * 
 */
	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		List interests = categoryMasterDAO.getAllRecords();
		for (int i=0;i<interests.size();i++){
			CategoryMaster cm = (CategoryMaster)interests.get(i);
			Long interestid = cm.getId();
			String filename = interestid.toString()+".txt";
			try {
				System.setOut(new PrintStream(new BufferedOutputStream(
						new FileOutputStream(TRAININGDIRPATH+filename))));
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
			List titlebodies = interestReferenceModelDAO.getAllRecordsByInterestIds(interestid);
			List keywords = interestKeywordsDAO.getAllRecordsByInterestId(interestid);
			for (int j=0;j<titlebodies.size();j++){
				InterestReferenceModel irm = (InterestReferenceModel)titlebodies.get(j);
				String title = StrUtil.nonNull(irm.getTitle());
				if(!"".equalsIgnoreCase(title)){
					System.out.println(title);
				}
				String body = StrUtil.nonNull(irm.getBody());
				if(!"".equalsIgnoreCase(body)){
					body = DataClean.clean(body);
					List para = StrUtil.breakPara(body);
					for (int x=0;x<para.size();x++){
						String xx = (String)para.get(x);
						xx=xx.trim();
						System.out.println(xx);
					}
					
				}
			}
			for (int j=0;j<keywords.size();j++){
				InterestKeywords ik = (InterestKeywords)keywords.get(j);
				String keyword = StrUtil.nonNull(ik.getKeyword());
				if(!"".equalsIgnoreCase(keyword)){
					System.out.println(keyword);
				}
			}
		}
		
		response.setStatus(200);
		response.addHeader("Access-Control-Allow-Origin", "*");
		PrintWriter writer = response.getWriter();
		writer.write("");
		writer.close();
		return null;
	}
}
