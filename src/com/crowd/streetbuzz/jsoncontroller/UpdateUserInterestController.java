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
import com.crowd.streetbuzz.dao.implementation.CategoryMasterDAO;
import com.crowd.streetbuzz.dao.implementation.UserCategoryMapDAO;
import com.crowd.streetbuzz.dao.implementation.UserDAO;
import com.crowd.streetbuzz.json.JsonHeader;
import com.crowd.streetbuzz.model.CategoryMaster;
import com.crowd.streetbuzz.model.User;
import com.crowd.streetbuzz.model.UserCategoryMap;
import com.crowd.streetbuzzalgo.utils.StrUtil;
import com.google.gson.Gson;

/**
 * @author Atrijit
 * 
 */
public class UpdateUserInterestController implements Controller, Constants {
	private UserCategoryMapDAO userCategoryMapDAO;

	private CategoryMasterDAO categoryMasterDAO;

	private UserDAO userDAO;

	public UserDAO getUserDAO() {
		return userDAO;
	}

	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession(true);
		JsonHeader jsonHeader = (JsonHeader) session.getAttribute("jsonHeader");
		String jsonbody = (String) session.getAttribute("jsonbody");
		jsonbody = jsonbody.trim();
		String[] interestidArr = jsonbody.split(",");
		List receivedArray = new ArrayList();
		for (int i = 0; i < interestidArr.length; i++) {
			String temp = interestidArr[i];
			temp = temp.trim();
			Long id = new Long(temp);
			receivedArray.add(id);
		}
		String useridStr = StrUtil.nonNull(jsonHeader.getUserid());
		Long userid = new Long(useridStr);

		List catmaplist = userCategoryMapDAO.getAllCategoriesforUser(userid);
		for (int i = 0; i < catmaplist.size(); i++) {
			UserCategoryMap ucm = (UserCategoryMap) catmaplist.get(i);
			userCategoryMapDAO.deleteRecord(ucm);
		}

		List categoryList = categoryMasterDAO.getAllRecords();

		for (int i = 0; i < categoryList.size(); i++) {
			CategoryMaster cm = (CategoryMaster) categoryList.get(i);
			Long catid = cm.getId();
			Long parentid = cm.getParentid();
			int parentidint = parentid.intValue();
			if (receivedArray.contains(catid)) {
				Long acat = new Long(0);
				Long bsubcat = new Long(0);
				if(parentidint==0){
					acat = catid;
					bsubcat = catid;
				}else{
					acat = parentid;
					bsubcat = catid;
				}
				UserCategoryMap ucm = (UserCategoryMap) userCategoryMapDAO
						.getObjectByCatIdSubCatIdUserId(acat, userid, bsubcat);
				
				if (ucm == null) {
					UserCategoryMap ucmnew = new UserCategoryMap();
					ucmnew.setCategory(cm.getCategoryname());
					if (parentidint == 0) {
						ucmnew.setCategoryid(catid);
					} else {
						ucmnew.setCategoryid(parentid);
					}

					ucmnew.setSubcategoryid(catid);
					ucmnew.setUserid(userid);
					ucmnew.setReputationscore(new Long(0));
					userCategoryMapDAO.addOrUpdateRecord(ucmnew);
				}

			}

		}

		User user = (User) userDAO.getObjectById(userid);
		List userinterestlist = userCategoryMapDAO
				.getAllCategoriesforUser(userid);
		List interestidlist = new ArrayList();
		if (userinterestlist != null) {
			for (int j = 0; j < userinterestlist.size(); j++) {
				UserCategoryMap ucm = (UserCategoryMap) userinterestlist.get(j);
				Long interestid = ucm.getCategoryid();
				int interestidint = interestid.intValue();

				if (!interestidlist.contains(interestid)) {
					/*
					 * if(interestidint<17){
					 *  }
					 */
					interestidlist.add(interestid);
				}
			}
		}
		user.setUserinterest(interestidlist);
		Gson gson = new Gson();
		String jsonresponse = gson.toJson(user);
		response.setStatus(200);
		response.addHeader("Access-Control-Allow-Origin", "*");
		PrintWriter writer = response.getWriter();
		writer.write(jsonresponse);
		writer.close();
		return null;
	}

	public CategoryMasterDAO getCategoryMasterDAO() {
		return categoryMasterDAO;
	}

	public void setCategoryMasterDAO(CategoryMasterDAO categoryMasterDAO) {
		this.categoryMasterDAO = categoryMasterDAO;
	}

	public UserCategoryMapDAO getUserCategoryMapDAO() {
		return userCategoryMapDAO;
	}

	public void setUserCategoryMapDAO(UserCategoryMapDAO userCategoryMapDAO) {
		this.userCategoryMapDAO = userCategoryMapDAO;
	}
}
