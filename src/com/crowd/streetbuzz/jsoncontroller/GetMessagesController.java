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
import com.crowd.streetbuzz.dao.implementation.MediaFilesDAO;
import com.crowd.streetbuzz.dao.implementation.PrivateMessageDAO;
import com.crowd.streetbuzz.dao.implementation.UserDAO;
import com.crowd.streetbuzz.json.JsonGetMessages;
import com.crowd.streetbuzz.json.JsonHeader;
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
public class GetMessagesController implements Controller, Constants {
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

	private List callQuery(JsonGetMessages jgm, Long messagebyid) {
		List holdList = new ArrayList();
		String perpageStr = StrUtil.nonNull(jgm.getPerpage());
		String beforeidStr = StrUtil.nonNull(jgm.getBeforeid());
		String afteridStr = StrUtil.nonNull(jgm.getAfterid());

		int perpage = new Long(perpageStr).intValue();

		if ("".equalsIgnoreCase(beforeidStr) && "".equalsIgnoreCase(afteridStr)) {
			holdList = privateMessageDAO.getAllOriginalRecordsPerPage(
					messagebyid, perpage);
		}

		else if (!"".equalsIgnoreCase(beforeidStr)
				&& "".equalsIgnoreCase(afteridStr)) {
			holdList = privateMessageDAO.getAllOriginalRecordsPerPageBeforeId(
					messagebyid, perpage, new Long(beforeidStr));
		}

		else if ("".equalsIgnoreCase(beforeidStr)
				&& !"".equalsIgnoreCase(afteridStr)) {
			holdList = privateMessageDAO.getAllOriginalRecordsPerPageAfterId(
					messagebyid, perpage, new Long(afteridStr));
		}

		return holdList;
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
		JsonGetMessages jgm = gson.fromJson(jsonbody, JsonGetMessages.class);
		PrintWriter writer = response.getWriter();
		String userid = StrUtil.nonNull(jsonHeader.getUserid());
		Long messagebyid = new Long(0);
		if (!"".equalsIgnoreCase(userid)) {
			try {
				messagebyid = new Long(userid);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();

			}
		}

		List messagesList = callQuery(jgm, messagebyid);

		List finalMessagesList = new ArrayList();

		for (int i = 0; i < messagesList.size(); i++) {
			PrivateMessage pm = (PrivateMessage) messagesList.get(i);
			String mediaid = StrUtil.nonNull(pm.getMediaid());
			MediaFiles mf = new MediaFiles();
			if (!"".equalsIgnoreCase(mediaid)) {
				mf = (MediaFiles) mediaFilesDAO.getObjectByUniqueId(mediaid);
			}

			JsonPrivateMessageResponse jpmr = convert(pm, messagebyid, mf);
			Long id = pm.getId();
			List replylist = privateMessageDAO
					.getAllRecordsbyReplyToMessageId(id);
			System.out.println("replylist size: " + replylist.size());
			List repliesjpmrList = new ArrayList();

			for (int j = 0; j < replylist.size(); j++) {
				PrivateMessage newpmff = (PrivateMessage) replylist.get(j);
				Long newpmid = newpmff.getId();
				System.out.println("newpmid: " + newpmid.toString());

				System.out.println("Adding newpm " + newpmid);
				JsonPrivateMessageResponse newjpmr = convert(newpmff,
						messagebyid, mf);
				repliesjpmrList.add(newjpmr);

			}
			System.out.println("repliesjpmrList: " + repliesjpmrList.size());
			jpmr.setReplies(repliesjpmrList);
			finalMessagesList.add(jpmr);
		}

		String jsonresponse = gson.toJson(finalMessagesList);
		System.out.println("jsonresponse: " + jsonresponse);

		response.setStatus(200);
		response.addHeader("Access-Control-Allow-Origin", "*");
		writer = response.getWriter();
		writer.write(jsonresponse);
		writer.close();
		return null;
	}

	private JsonPrivateMessageResponse convert(PrivateMessage newpm,
			Long viewinguserid, MediaFiles mf) {
		JsonPrivateMessageResponse jpmr = new JsonPrivateMessageResponse();
		if (newpm != null) {
			Long id = newpm.getId();
			System.out.println("Converting id: " + id.toString());
			String text = StrUtil.nonNull(newpm.getText());
			System.out.println("Converting text: " + text);
			String authoravatar = StrUtil.nonNull(newpm.getAuthoravatar());
			Long messagetoid = new Long(0);
			if (newpm != null) {
				messagetoid = newpm.getMessagetouserid();
			}
			User touser = null;
			String messageatavatar = "";
			if (messagetoid != null) {
				touser = (User) userDAO.getObjectById(messagetoid);
				messageatavatar = StrUtil.nonNull(touser.getAvatar());
			}

			Long messagebyid = newpm.getMessagebyuserid();
			String messageto = StrUtil.nonNull(newpm.getMessageto());
			String messageby = StrUtil.nonNull(newpm.getMessageby());
			String mediaurl = StrUtil.nonNull(newpm.getMediaurl());
			String mediavideourl = StrUtil.nonNull(newpm.getMediavideourl());
			String previewtext = StrUtil.nonNull(newpm.getPreviewtext());
			String isownerStr = StrUtil.nonNull(newpm.getIsowner());
			boolean isowner = false;
			if (viewinguserid.equals(newpm.getMessagebyuserid())) {
				isowner = true;
			}
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
			jpmr.setIsowner(isowner);
			jpmr.setLink(newlink);
			jpmr.setMediaurl(mediaurl);
			jpmr.setMediavideourl(mediavideourl);
			jpmr.setLinkpreviewurl(linkpreviewurl);
			jpmr.setPreviewtext(previewtext);
			jpmr.setMessageat(messageto);
			jpmr.setMessageby(messageby);
			jpmr.setMessageatid(messagetoid);
			jpmr.setMessagebyid(messagebyid);
			jpmr.setText(text);
			jpmr.setUserrating(new Long(1));
			jpmr.setDate(dt);
		}

		return jpmr;
	}
}
