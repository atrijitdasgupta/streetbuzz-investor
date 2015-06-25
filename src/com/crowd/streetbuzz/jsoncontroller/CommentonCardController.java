/**
 * 
 */
package com.crowd.streetbuzz.jsoncontroller;

import java.io.PrintWriter;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.crowd.streetbuzz.common.Constants;
import com.crowd.streetbuzz.dao.implementation.CardCommentVoteModelDAO;
import com.crowd.streetbuzz.dao.implementation.CardCommentsDAO;
import com.crowd.streetbuzz.dao.implementation.MediaFilesDAO;
import com.crowd.streetbuzz.dao.implementation.UserDAO;
import com.crowd.streetbuzz.json.JsonCardCommentsIncoming;
import com.crowd.streetbuzz.json.JsonHeader;
import com.crowd.streetbuzz.model.CardCommentVoteModel;
import com.crowd.streetbuzz.model.CardComments;
import com.crowd.streetbuzz.model.MediaFiles;
import com.crowd.streetbuzz.model.User;
import com.crowd.streetbuzzalgo.utils.StrUtil;
import com.google.gson.Gson;

/**
 * @author Atrijit
 * 
 */
public class CommentonCardController implements Controller, Constants {

	private CardCommentsDAO cardCommentsDAO;

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

	public CardCommentsDAO getCardCommentsDAO() {
		return cardCommentsDAO;
	}

	public void setCardCommentsDAO(CardCommentsDAO cardCommentsDAO) {
		this.cardCommentsDAO = cardCommentsDAO;
	}

	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		System.out.println("Inside CommentonCardController ready to spotate");
		HttpSession session = request.getSession(true);
		JsonHeader jsonHeader = (JsonHeader) session.getAttribute("jsonHeader");
		String cardidStr = StrUtil.nonNull(jsonHeader.getCardid());
		String useridStr = StrUtil.nonNull(jsonHeader.getUserid());
		Long userid = new Long(0);
		if (!"".equalsIgnoreCase(useridStr)) {
			userid = new Long(useridStr);
		}
		Long cardid = new Long(0);
		if (!"".equalsIgnoreCase(cardidStr)) {
			cardid = new Long(cardidStr);
		}
		String jsonbody = (String) session.getAttribute("jsonbody");
		Gson gson = new Gson();
		JsonCardCommentsIncoming jcci = gson.fromJson(jsonbody,
				JsonCardCommentsIncoming.class);

		String comment = StrUtil.nonNull(jcci.getComment());
		String link = StrUtil.nonNull(jcci.getLink());
		String mediaid = StrUtil.nonNull(StrUtil.nonNull(jcci.getMediaid()));
		MediaFiles mf = (MediaFiles) mediaFilesDAO.getObjectByUniqueId(mediaid);

		String rating = StrUtil.nonNull(jcci.getRating());

		User user = (User) userDAO.getObjectById(userid);
		String username = StrUtil.nonNull(user.getName());
		String avatar = StrUtil.nonNull(user.getAvatar());
		String linkpreviewurl = "";
		String previewtext = "";

		CardComments ccom = new CardComments();
		ccom.setCardid(cardid);
		ccom.setCarduniqueid("");
		ccom.setComment(comment);
		ccom.setCommentdate(new Date());
		ccom.setCommenter(username);
		ccom.setSbcommenterid(userid);
		ccom.setExtcommentersource("");
		ccom.setExtcommenterid("");
		ccom.setMediaid(mediaid);
		ccom.setRating(rating);
		ccom.setLink(link);
		if (!"".equalsIgnoreCase(link)) {
			linkpreviewurl = StrUtil.nonNull(StrUtil.getThumb(link));
			previewtext = StrUtil.nonNull(StrUtil.getTitleAlt(link));
			ccom.setLinkpreviewurl(linkpreviewurl);
			ccom.setPreviewtext(previewtext);
		} else {
			ccom.setLinkpreviewurl("");
			ccom.setPreviewtext("");
		}
		String mediaurl = "";
		String videourl = "";
			try {
				mediaurl = StrUtil.nonNull(mf.getMediaurl());
				videourl = StrUtil.nonNull(mf.getVideourl());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println(e.getMessage());
			}
			ccom.setMediaurl(mediaurl);
			ccom.setMediavideourl(videourl);


		ccom.setAvatar(avatar);
		String commentuniqueid = StrUtil.getUniqueId();
		ccom.setCommentuniqueid(commentuniqueid);
		cardCommentsDAO.addOrUpdateRecord(ccom);

		CardComments justccom = (CardComments) cardCommentsDAO
				.getObjectByCommentUniqueId(commentuniqueid);
		justccom.setMediaurl(mediaurl);
		justccom.setMediavideourl(videourl);
		justccom.setUserrating(new Long(1));
		justccom.setLinkpreviewurl(linkpreviewurl);
		justccom.setPreviewtext(previewtext);
		justccom.setIsowner(true);
		
		Long justcommid = justccom.getId();
	
		justccom.setIsliked(true);
		justccom.setLikecount(new Long(0));
		
		

		String retStr = gson.toJson(justccom);
		response.setStatus(200);
		response.addHeader("Access-Control-Allow-Origin", "*");
		PrintWriter writer = response.getWriter();
		writer.write(retStr);
		writer.close();
		return null;
	}

}
