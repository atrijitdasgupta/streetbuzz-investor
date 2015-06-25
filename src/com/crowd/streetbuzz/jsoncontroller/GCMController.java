/**
 * 
 */
package com.crowd.streetbuzz.jsoncontroller;

import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.crowd.streetbuzz.common.Constants;
import com.crowd.streetbuzz.dao.implementation.GCMDAO;
import com.crowd.streetbuzz.dao.implementation.UserDAO;
import com.crowd.streetbuzz.json.JsonGCM;
import com.crowd.streetbuzz.json.JsonHeader;
import com.crowd.streetbuzz.model.GCM;
import com.crowd.streetbuzz.model.User;
import com.crowd.streetbuzzalgo.utils.StrUtil;
import com.google.gson.Gson;

/**
 * @author Atrijit
 *
 */
public class GCMController implements Controller, Constants{
	
	private GCMDAO gcmDAO;
	private UserDAO userDAO;
	
	public GCMDAO getGcmDAO() {
		return gcmDAO;
	}

	public void setGcmDAO(GCMDAO gcmDAO) {
		this.gcmDAO = gcmDAO;
	}

	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		boolean noerror = true;
		System.out
		.println("Inside GCMController ready to spotate");
		HttpSession session = request.getSession(true);
		JsonHeader jsonHeader = (JsonHeader) session.getAttribute("jsonHeader");
		String jsonbody = StrUtil.nonNull((String) session
				.getAttribute("jsonbody"));
		PrintWriter writer = response.getWriter();
		if ("".equalsIgnoreCase(jsonbody)) {
			noerror = false;
			String errMsg = "empty jsonbody";
			response.setStatus(500);
			writer.write(STANDARERRORRESPONSE+errMsg);
			writer.close();
			return null;

		}
		JsonGCM jsonGCM = new JsonGCM();
		Gson gson = new Gson();
		try {
			jsonGCM = gson.fromJson(jsonbody, JsonGCM.class);
		} catch (Exception e) {
			noerror = false;
			String errMsg = e.getMessage();
			response.setStatus(500);
			writer.write(STANDARERRORRESPONSE+errMsg);
			writer.close();
			return null;
		}
		
		String userid = StrUtil.nonNull(jsonHeader.getUserid());
		String socialnetwork = StrUtil.nonNull(jsonHeader.getSocialnetwork());
		//String userid = "1";
		//String socialnetwork = "FB";
		userid = userid.trim();
		socialnetwork = socialnetwork.trim();
		String googleid = StrUtil.nonNull(jsonGCM.getGoogleid());
		String appleid = StrUtil.nonNull(jsonGCM.getAppleid());
		GCM gcm = new GCM();
		try {
			gcm = (GCM)gcmDAO.getObjectByUserId(new Long(userid));
		} catch (RuntimeException e) {
			
		}
		if(gcm!=null){
			
			gcm.setAppleid(appleid);
			gcm.setGoogleid(googleid);
			gcm.setUpdatedate(new Date());
			gcmDAO.addOrUpdateRecord(gcm);
		
		}else{
			gcm = new GCM();
			gcm.setUserid(userid);
			gcm.setSocialnetwork(socialnetwork);
			gcm.setAppleid(appleid);
			gcm.setGoogleid(googleid);
			gcm.setUpdatedate(new Date());
			gcmDAO.addOrUpdateRecord(gcm);
		}
		/*try {
			gcmDAO.addOrUpdateRecord(gcm);
		} catch (Exception e) {
			noerror = false;
			String errMsg = e.getMessage();
			response.setStatus(500);
			writer.write(STANDARERRORRESPONSE+errMsg);
			writer.close();
			return null;
		}*/
		
		User user = (User)userDAO.getObjectById(new Long(userid));
		user.setGoogledeviceid(googleid);
		user.setAppledeviceid(appleid);
		userDAO.addOrUpdateRecord(user);
		response.setStatus(200);
		writer.write(SUCCESSESPONSE);
		//now send the message
		System.out.println("googleid: "+googleid);
		//GooglePushMessage.sendPushMessage(googleid,"GCM:this is a test push message");
		return null;
	}

	public UserDAO getUserDAO() {
		return userDAO;
	}

	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}
}
