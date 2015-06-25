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
import com.crowd.streetbuzz.dao.implementation.UserDAO;
import com.crowd.streetbuzz.json.JsonHeader;
import com.crowd.streetbuzz.json.JsonUserDistributionData;
import com.crowd.streetbuzz.model.User;
import com.crowd.streetbuzzalgo.utils.GeoCodingReverseLookupUtils;
import com.crowd.streetbuzzalgo.utils.StrUtil;
import com.google.gson.Gson;

/**
 * @author Atrijit
 * 
 */
public class GetDistributionNetworkController implements Controller, Constants {
	private UserDAO userDAO;

	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession(true);
		JsonHeader jh = (JsonHeader) session.getAttribute("jsonHeader");
		String userIdStr = jh.getUserid();
		Long userid = new Long(userIdStr);
		int selfidint = userid.intValue();
		User requser = (User) userDAO.getObjectById(userid);
		List userList = userDAO.getAllRecords();
		List finaluserlist = new ArrayList();
		for (int i = 0; i < userList.size(); i++) {
			User user = (User) userList.get(i);
			Long uid = user.getId();
			int uidint = uid.intValue();
			if (uidint != selfidint) {
				finaluserlist.add(user);
			}
		}
		List distList = convert(finaluserlist, requser);
		Gson gson = new Gson();
		String responseString = gson.toJson(distList);
		response.setStatus(200);
		response.addHeader("Access-Control-Allow-Origin", "*");
		PrintWriter writer = response.getWriter();
		writer.write(responseString);
		writer.close();
		return null;
	}

	private List convert(List userlist, User requser) {
		List list = new ArrayList();
		for (int j = 0; j < userlist.size(); j++) {
			User user = (User) userlist.get(j);
			JsonUserDistributionData judd = new JsonUserDistributionData();
			String avatar = StrUtil.nonNull(user.getAvatar());
			Long userid = user.getId();
			judd.setId(userid);
			judd.setAvatar(avatar);
			String name = StrUtil.nonNull(user.getName());
			String usersocialnetwork = StrUtil.nonNull(user.getSocialnetwork());
			String city = StrUtil.nonNull(user.getCity());
			String country = StrUtil.nonNull(user.getCountry());
			String location = city;
			
			String mylat = requser.getLatitude();
			String mylon = requser.getLongitude();
			String yourlat = user.getLatitude();
			String yourlon = user.getLongitude();
			
			String distancestr = GeoCodingReverseLookupUtils.distance(mylat,mylon,yourlat,yourlon);

			judd.setName(name);
			judd.setLocation(location);
			judd.setUsersocialnetwork(usersocialnetwork);
			judd.setDistance(distancestr);
			list.add(judd);

		}
		return list;
	}

	private static final double distance(double lat1, double lon1, double lat2,
			double lon2, String unit) {
		double theta = lon1 - lon2;
		double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2))
				+ Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2))
				* Math.cos(deg2rad(theta));
		dist = Math.acos(dist);
		dist = rad2deg(dist);
		dist = dist * 60 * 1.1515;

		if (unit.equalsIgnoreCase("K")) {
			dist = dist * 1.609344;
		} else if (unit.equalsIgnoreCase("N")) {
			dist = dist * 0.8684;
		}

		return (dist);
	}

	private static final double deg2rad(double deg) {
		return (deg * Math.PI / 180.0);
	}

	private static final double rad2deg(double rad) {
		return (rad * 180 / Math.PI);
	}

	public UserDAO getUserDAO() {
		return userDAO;
	}

	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}
}
