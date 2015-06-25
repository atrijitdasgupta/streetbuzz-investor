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
import com.crowd.streetbuzz.dao.implementation.UserCategoryMapDAO;
import com.crowd.streetbuzz.dao.implementation.UserDAO;
import com.crowd.streetbuzz.json.JsonHeader;
import com.crowd.streetbuzz.model.User;
import com.crowd.streetbuzz.model.UserCategoryMap;
import com.crowd.streetbuzzalgo.utils.StrUtil;
import com.google.gson.Gson;

/**
 * @author Atrijit
 * 
 */
public class GetUserProfileController implements Controller, Constants {

	private UserDAO userDAO;
	private UserCategoryMapDAO userCategoryMapDAO;

	public UserDAO getUserDAO() {
		return userDAO;
	}

	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		boolean noerror = true;
		System.out.println("Inside GetUserProfileController ready to spotate");
		HttpSession session = request.getSession(true);
		JsonHeader jsonHeader = (JsonHeader) session.getAttribute("jsonHeader");
		String jsonbody = StrUtil.nonNull((String) session
				.getAttribute("jsonbody"));
		Gson gson = new Gson();
		PrintWriter writer = response.getWriter();

		String userid = jsonHeader.getUserid();
		Long useridLong = new Long(userid);
		User user = (User)userDAO.getObjectById(useridLong);
		List userinterestlist = userCategoryMapDAO.getAllCategoriesforUser(useridLong);
		List interestidlist = new ArrayList();
		if(userinterestlist!=null){
		for (int j=0;j<userinterestlist.size();j++){
			UserCategoryMap ucm = (UserCategoryMap)userinterestlist.get(j);
			Long interestid = ucm.getCategoryid();
			int interestidint = interestid.intValue();
			if(!interestidlist.contains(interestid)){
				if(interestidint<17){
					interestidlist.add(interestid);
				}
				
			}
		}
		}
		user.setUserinterest(interestidlist);
		
		String jsonresponse = gson.toJson(user);

		response.setStatus(200);
		response.addHeader("Access-Control-Allow-Origin", "*");
		writer = response.getWriter();
		writer.write(jsonresponse);
		writer.close();
		return null;
	}

	public UserCategoryMapDAO getUserCategoryMapDAO() {
		return userCategoryMapDAO;
	}

	public void setUserCategoryMapDAO(UserCategoryMapDAO userCategoryMapDAO) {
		this.userCategoryMapDAO = userCategoryMapDAO;
	}
}
