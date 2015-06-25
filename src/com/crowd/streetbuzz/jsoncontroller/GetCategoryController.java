/**
 * 
 */
package com.crowd.streetbuzz.jsoncontroller;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.crowd.streetbuzz.common.Constants;
import com.crowd.streetbuzz.dao.implementation.CategoryMasterDAO;
import com.crowd.streetbuzz.model.CategoryMaster;
import com.google.gson.Gson;

/**
 * @author Atrijit
 * 
 */
public class GetCategoryController implements Controller, Constants {

	private CategoryMasterDAO categoryMasterDAO;

	public CategoryMasterDAO getCategoryMasterDAO() {
		return categoryMasterDAO;
	}

	public void setCategoryMasterDAO(CategoryMasterDAO categoryMasterDAO) {
		this.categoryMasterDAO = categoryMasterDAO;
	}

	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// List catList = categoryMasterDAO.getAllRecords();
		List catList = new ArrayList();
		for (int i = 0; i < 16; i++) {
			Long interestid = new Long(i + 1);
			CategoryMaster cm = (CategoryMaster) categoryMasterDAO
					.getObjectById(interestid);
			Long parentid = cm.getId();
			System.out.println("got parentid: " + parentid.toString());
			int parentidint = parentid.intValue();
			if (parentidint != 0) {
				List subcategorylist = categoryMasterDAO
						.getAllRecordsByParentId(parentid);

				if (subcategorylist != null) {
					cm.setSubcategorylist(subcategorylist);
				}

				catList.add(cm);
			}
		}
		Gson gson = new Gson();
		String catStr = gson.toJson(catList);
		// System.out.println("Category JSON String: "+catStr);
		PrintWriter writer = response.getWriter();
		response.setStatus(200);
		response.addHeader("Access-Control-Allow-Origin", "*");
		writer.write(catStr);
		writer.close();
		return null;
	}

}
