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
import com.crowd.streetbuzz.dao.implementation.MediaFilesDAO;
import com.crowd.streetbuzz.dao.implementation.PrivateMessageDAO;
import com.crowd.streetbuzz.dao.implementation.UserDAO;
import com.crowd.streetbuzz.json.JsonHeader;
import com.crowd.streetbuzz.json.JsonPostMessage;
import com.crowd.streetbuzz.json.JsonPrivateMessageResponse;
import com.crowd.streetbuzz.model.MediaFiles;
import com.crowd.streetbuzz.model.PrivateMessage;
import com.crowd.streetbuzz.model.User;
import com.crowd.streetbuzzalgo.utils.StrUtil;
import com.google.gson.Gson;

/**
 * @author Atrijit
 * 
 */
public class PostMessageController implements Controller, Constants {
	private PrivateMessageDAO privateMessageDAO;

	private UserDAO userDAO;

	private MediaFilesDAO mediaFilesDAO;

	public MediaFilesDAO getMediaFilesDAO() {
		return mediaFilesDAO;
	}

	public void setMediaFilesDAO(MediaFilesDAO mediaFilesDAO) {
		this.mediaFilesDAO = mediaFilesDAO;
	}

	public UserDAO getUserDAO() {
		return userDAO;
	}

	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

	public PrivateMessageDAO getPrivateMessageDAO() {
		return privateMessageDAO;
	}

	public void setPrivateMessageDAO(PrivateMessageDAO privateMessageDAO) {
		this.privateMessageDAO = privateMessageDAO;
	}

	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		boolean noerror = true;
		System.out.println("Inside GetMessagesController ready to spotate");
		HttpSession session = request.getSession(true);
		JsonHeader jsonHeader = (JsonHeader) session.getAttribute("jsonHeader");
		String jsonbody = StrUtil.nonNull((String) session
				.getAttribute("jsonbody"));
		Gson gson = new Gson();
		PrintWriter writer = response.getWriter();

		JsonPostMessage jpm = new JsonPostMessage();
		try {
			jpm = (JsonPostMessage) gson.fromJson(jsonbody,
					JsonPostMessage.class);
		} catch (Exception e) {
			noerror = false;
			String errMsg = "empty jsonbody";
			response.setStatus(500);
			writer.write(STANDARERRORRESPONSE + errMsg);
			writer.close();
			return null;
		}

		PrivateMessage pm = new PrivateMessage();

		String msg = StrUtil.nonNull(jpm.getMessage());
		String link = StrUtil.nonNull(jpm.getLink());
		String mediaid = StrUtil.nonNull(jpm.getMediaid());
		String replytomsgid = StrUtil.nonNull(jpm.getReplytomessageid());
		replytomsgid.trim();
		Long replytomsgid_Long = new Long(0);
		if (!"".equalsIgnoreCase(replytomsgid)) {
			replytomsgid_Long = new Long(replytomsgid);
		}
		String recipientuserid = StrUtil.nonNull(jpm.getTouserid());
		Long messagetouserid = new Long(0);
		if (!"".equalsIgnoreCase(recipientuserid)) {
			messagetouserid = new Long(recipientuserid);
		}
		String postinguserid = StrUtil.nonNull(jsonHeader.getUserid());
		Long messagebyuserid = new Long(0);
		if (!"".equalsIgnoreCase(postinguserid)) {
			messagebyuserid = new Long(postinguserid);
		}
		User fromuser = (User) userDAO.getObjectById(messagebyuserid);
		User touser = (User) userDAO.getObjectById(messagetouserid);
		if (fromuser != null) {
			pm.setMessageby(StrUtil.nonNull(fromuser.getName()));
			pm.setAuthoravatar(StrUtil.nonNull(fromuser.getAvatar()));
		}
		if (touser != null) {
			pm.setMessageto(StrUtil.nonNull(touser.getName()));
		}

		pm.setReplytomessageid(replytomsgid_Long);
		pm.setMessageat(new Date());
		pm.setLink(link);
		if (!"".equalsIgnoreCase(link)) {
			String linkpreviewurl = StrUtil.nonNull(StrUtil.getThumb(link));
			pm.setLinkpreviewurl(linkpreviewurl);
		} else {
			pm.setLinkpreviewurl("");
		}
		String previewtext = "";
		if (link != null) {
			try {
				previewtext = StrUtil.nonNull(StrUtil.getTitleAlt(link));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println(e.getMessage());
			}
		}
		pm.setPreviewtext(previewtext);

		pm.setText(msg);
		pm.setMediaid(mediaid);
		MediaFiles mf = (MediaFiles) mediaFilesDAO.getObjectByUniqueId(mediaid);
		if (mf != null) {
			String medurl = StrUtil.nonNull(mf.getMediaurl());
			String vidurl = StrUtil.nonNull(mf.getVideourl());
			pm.setMediaurl(medurl);
			pm.setMediavideourl(vidurl);

		}
		
		if (fromuser != null) {
			pm.setMessagebyuserid(fromuser.getId());
		}
		if (touser != null) {
			pm.setMessagetouserid(touser.getId());
		}
		pm.setMessageat(new Date());
		if ("0".equalsIgnoreCase(replytomsgid)) {
			pm.setIsreply("N");

		} else {
			pm.setIsreply("Y");

		}

		// decide isOwner
		pm.setIsowner("N");
		if (!"".equalsIgnoreCase(replytomsgid)) {
			PrivateMessage orgpm = (PrivateMessage) privateMessageDAO
					.getObjectById(replytomsgid_Long);
			if (orgpm != null) {
				Long orgpmuserid = orgpm.getMessagebyuserid();
				if (fromuser.getId().equals(orgpmuserid)) {
					pm.setIsowner("Y");
				}
			}

		}
		if ("".equalsIgnoreCase(replytomsgid)) {
			pm.setIsowner("Y");
		}

		String uniqueid = StrUtil.getUniqueId();
		pm.setUniqueid(uniqueid);

		privateMessageDAO.addOrUpdateRecord(pm);

		PrivateMessage newpm = (PrivateMessage) privateMessageDAO
				.getObjectByUniqueId(uniqueid);

		// Convert to the JSON format minus variables that the client does not
		// need.
		JsonPrivateMessageResponse jpmr = convert(newpm, fromuser, touser, mf);

		String jsonresponse = gson.toJson(jpmr);

		response.setStatus(200);
		response.addHeader("Access-Control-Allow-Origin", "*");
		writer = response.getWriter();
		writer.write(jsonresponse);
		writer.close();
		return null;
	}

	private JsonPrivateMessageResponse convert(PrivateMessage newpm,
			User fromuser, User touser, MediaFiles mf) {
		JsonPrivateMessageResponse jpmr = new JsonPrivateMessageResponse();
		Long id = newpm.getId();
		String text = StrUtil.nonNull(newpm.getText());
		String authoravatar = StrUtil.nonNull(newpm.getAuthoravatar());
		String messageatavatar = "";
		if (touser != null) {
			messageatavatar = StrUtil.nonNull(touser.getAvatar());
		}

		Long messageto = newpm.getMessagetouserid();
		Long messageby = newpm.getMessagebyuserid();
		String mediaurl = StrUtil.nonNull(newpm.getMediaurl());
		String videourl = StrUtil.nonNull(newpm.getMediavideourl());
		String isownerStr = StrUtil.nonNull(newpm.getIsowner());
		String previewtext = StrUtil.nonNull(newpm.getPreviewtext());

		String newlink = StrUtil.nonNull(newpm.getLink());
		String linkpreviewurl = "";
		try {
			linkpreviewurl = StrUtil.nonNull(StrUtil.getThumb(newlink));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			linkpreviewurl = "";
		}
		Date dt = newpm.getMessageat();

		jpmr.setId(id);
		jpmr.setAuthoravatar(authoravatar);
		jpmr.setMessageatavatar(messageatavatar);
		jpmr.setIsowner(true);
		jpmr.setLink(newlink);
		jpmr.setMediaurl(mediaurl);
		jpmr.setMediavideourl(videourl);
		jpmr.setPreviewtext(previewtext);
		jpmr.setLinkpreviewurl(linkpreviewurl);
		if (fromuser != null) {
			jpmr.setMessageby(fromuser.getName());
			jpmr.setMessagebyid(fromuser.getId());
		}
		if (touser != null) {
			jpmr.setMessageat(touser.getName());
			jpmr.setMessageatid(touser.getId());
		}

		jpmr.setText(text);
		jpmr.setDate(dt);
		jpmr.setUserrating(new Long(1));
		return jpmr;
	}
}
