/**
 * 
 */
package com.crowd.streetbuzz.jsoncontroller;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.crowd.streetbuzz.common.Constants;
import com.crowd.streetbuzz.dao.implementation.UserCategoryMapDAO;
import com.crowd.streetbuzz.dao.implementation.UserDAO;
import com.crowd.streetbuzz.json.FBAccessToken;
import com.crowd.streetbuzz.json.JsonHeader;
import com.crowd.streetbuzz.model.User;
import com.crowd.streetbuzz.model.UserCategoryMap;
import com.crowd.streetbuzzalgo.utils.GeoCodingReverseLookupUtils;
import com.crowd.streetbuzzalgo.utils.StrUtil;
import com.google.gson.Gson;

/**
 * @author Atrijit
 * 
 */
public class FBSignUpController implements Controller, Constants {
	private UserDAO userDAO;
	private UserCategoryMapDAO userCategoryMapDAO;
	
	public UserCategoryMapDAO getUserCategoryMapDAO() {
		return userCategoryMapDAO;
	}

	public void setUserCategoryMapDAO(UserCategoryMapDAO userCategoryMapDAO) {
		this.userCategoryMapDAO = userCategoryMapDAO;
	}

	public UserDAO getUserDAO() {
		return userDAO;
	}

	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		boolean noerror = true;
		System.out.println("Inside FBSignUpController ready to spotate");
		HttpSession session = request.getSession(true);
		JsonHeader jsonHeader = (JsonHeader) session.getAttribute("jsonHeader");
		String jsonbody = StrUtil.nonNull((String) session
				.getAttribute("jsonbody"));
		
		FBAccessToken fat = new FBAccessToken();
		Gson gson = new Gson();
		PrintWriter writer = response.getWriter();
		try {
			fat = (FBAccessToken) gson.fromJson(jsonbody,
					FBAccessToken.class);
		} catch (Exception e) {
			noerror = false;
			String errMsg = "empty jsonbody";
			response.setStatus(500);
			writer.write(STANDARERRORRESPONSE+errMsg);
			writer.close();
			return null;
		}
		
		//TODO do some processing here to persist user token
		
		//Get the user
		String emailid = fat.getEmail();
		emailid = emailid.trim();
		User user = (User)userDAO.getObjectByEmailId(emailid);
		User newUser = new User();
		
		String latitude = StrUtil.nonNull(jsonHeader.getLatitude());
		String longitude = StrUtil.nonNull(jsonHeader.getLongitude());
		
		Map locationMap = new HashMap();
		try {
		//	locationMap = GeoCodingReverseLookupUtils.reverseLookup(latitude,longitude);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			System.out.println(e1.getMessage());
		}
		
		String usercity = "";
		String usercountry = "";
		if(locationMap!=null && locationMap.size()>1){
			usercity = (String)locationMap.get(CITY);
			usercountry = (String)locationMap.get(COUNTRY);
		}
		
		if(user==null){
			
			//Sign him up
			newUser.setEmail(emailid);
			String access_token = fat.getAccess_token();
			String social_user_id = fat.getSocial_user_id();
			String social_network = "facebook";
			String name = fat.getName();
			String avatar = "https://graph.facebook.com/"+social_user_id+"/picture?type=large";
			newUser.setAccesstoken(access_token);
			newUser.setName(name);
			newUser.setAvatar(avatar);
			newUser.setSocialnetwork(social_network);
			newUser.setJoindate(new Date());
			newUser.setLatitude(latitude);
			newUser.setLongitude(longitude);
			newUser.setCity(usercity);
			newUser.setCountry(usercountry);
			newUser.setSocialuserid(social_user_id);
			userDAO.addOrUpdateRecord(newUser);
			
		}
		User justCreatedUser = (User) userDAO.getObjectByEmailId(emailid);
		
		List userinterestlist = userCategoryMapDAO.getAllCategoriesforUser(justCreatedUser.getId());
		List interestidlist = new ArrayList();
		if(userinterestlist!=null){
		for (int j=0;j<userinterestlist.size();j++){
			UserCategoryMap ucm = (UserCategoryMap)userinterestlist.get(j);
			Long interestid = ucm.getCategoryid();
			
			if(!interestidlist.contains(interestid)){
				interestidlist.add(interestid);
			}
		}
		}
		justCreatedUser.setUserinterest(interestidlist);
		
		String jsonresponse = gson.toJson(justCreatedUser);
		System.out.println("FBSignUpController:: "+jsonresponse);
		
		response.setStatus(200);
		response.addHeader("Access-Control-Allow-Origin", "*");
		writer = response.getWriter();
		writer.write(jsonresponse);
		writer.close();
		return null;
	}
}
