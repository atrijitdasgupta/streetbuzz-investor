/**
 * 
 */
package com.crowd.streetbuzz.jsoncontroller;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.crowd.streetbuzz.common.Constants;
import com.crowd.streetbuzz.dao.implementation.UserActivitiesDAO;
import com.crowd.streetbuzz.json.JsonHeader;
import com.crowd.streetbuzz.json.JsonUserActivities;
import com.crowd.streetbuzz.model.UserActivities;
import com.crowd.streetbuzzalgo.utils.StrUtil;
import com.google.gson.Gson;

/**
 * @author Atrijit
 * 
 */
public class GetUserActivitiesController implements Controller, Constants {
	private UserActivitiesDAO userActivitiesDAO;

	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		boolean noerror = true;
		System.out.println("Inside GetUserActivitiesController ready to spotate");
		HttpSession session = request.getSession(true);
		JsonHeader jsonHeader = (JsonHeader) session.getAttribute("jsonHeader");
		String jsonbody = StrUtil.nonNull((String) session
				.getAttribute("jsonbody"));
		Gson gson = new Gson();
		PrintWriter writer = response.getWriter();

		String userid = StrUtil.nonNull(jsonHeader.getUserid());
		Long useridLong = new Long(0);
		if (!"".equalsIgnoreCase(userid)) {
			useridLong = new Long(userid);
		}
		List activityList = userActivitiesDAO.getAllRecordsByUserId(useridLong);
		List jsonActivityList = convert(activityList);
		String jsonresponse = gson.toJson(jsonActivityList);
		
		response.setStatus(200);
		response.addHeader("Access-Control-Allow-Origin", "*");
		writer = response.getWriter();
		writer.write(jsonresponse);
		writer.close();
		return null;
	}

	private List convert(List activityList) {
		List jsonActivityList = new ArrayList();
		for (int i = 0; i < activityList.size(); i++) {
			UserActivities ua = (UserActivities) activityList.get(i);
			Long id = ua.getId();
			String actiontype = ua.getActiontype();
			String actiondetails = ua.getActiondetails();
			String actionarea = ua.getActionarea();
			Long points = ua.getPoints();
			Date actiondatetime = ua.getActiondatetime();

			JsonUserActivities jua = new JsonUserActivities();
			jua.setActivititytype(actiontype);
			jua.setActivityarea(actionarea);
			jua.setActivitytext(actiondetails);
			jua.setPoints(new Long(points).toString());
			String dateStr = StrUtil.getDateString(actiondatetime, dateformat);
			jua.setDatetime(dateformat2);
			jua.setId(id);

		}
		return jsonActivityList;
	}

	public UserActivitiesDAO getUserActivitiesDAO() {
		return userActivitiesDAO;
	}

	public void setUserActivitiesDAO(UserActivitiesDAO userActivitiesDAO) {
		this.userActivitiesDAO = userActivitiesDAO;
	}

	
}
