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
import com.crowd.streetbuzz.dao.implementation.CardCommentVoteModelDAO;
import com.crowd.streetbuzz.dao.implementation.CardCommentsDAO;
import com.crowd.streetbuzz.dao.implementation.MediaFilesDAO;
import com.crowd.streetbuzz.dao.implementation.UserDAO;
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
public class ShowCardCommentsController implements Controller, Constants {
	private CardCommentsDAO cardCommentsDAO;

	private UserDAO userDAO;

	private MediaFilesDAO mediaFilesDAO;

	private CardCommentVoteModelDAO cardCommentVoteModelDAO;

	public CardCommentVoteModelDAO getCardCommentVoteModelDAO() {
		return cardCommentVoteModelDAO;
	}

	public void setCardCommentVoteModelDAO(
			CardCommentVoteModelDAO cardCommentVoteModelDAO) {
		this.cardCommentVoteModelDAO = cardCommentVoteModelDAO;
	}

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
		System.out.println("Inside ShowCardCommentsController ready to spotate");
		HttpSession session = request.getSession(true);
		JsonHeader jsonHeader = (JsonHeader) session.getAttribute("jsonHeader");
		String useridStr = StrUtil.nonNull(jsonHeader.getUserid());
		Long userid = new Long(0);
		if (!"".equalsIgnoreCase(useridStr)) {
			userid = new Long(useridStr);
		}
		User user = (User) userDAO.getObjectById(userid);
		Long userratingL = new Long(0);
		String userrating = "";
		if (user != null) {
			userrating = StrUtil.nonNull(user.getRating());
			if ("".equalsIgnoreCase(userrating)) {
				userratingL = new Long(0);
			} else {
				userratingL = new Long(userrating);
			}
		}
		String cardidStr = jsonHeader.getCardid();
		Long cardid = new Long(0);
		if (!"".equalsIgnoreCase(cardidStr)) {
			cardid = new Long(cardidStr);
		}
		List cardComments = cardCommentsDAO.getAllRecordsbyCardid(cardid);
		List finalList = new ArrayList();
		for (int i = 0; i < cardComments.size(); i++) {
			CardComments ccom = (CardComments) cardComments.get(i);
			String mediaid = StrUtil.nonNull(ccom.getMediaid());
			MediaFiles mf = (MediaFiles) mediaFilesDAO
					.getObjectByUniqueId(mediaid);
			if (mf != null) {
				String mediaurl = StrUtil.nonNull(mf.getMediaurl());
				String videourl = StrUtil.nonNull(mf.getVideourl());
				ccom.setMediaurl(mediaurl);
				ccom.setMediavideourl(videourl);
			} else {
				ccom.setMediaurl("");
				ccom.setMediavideourl("");
			}

			// TODO temporary fix, to change when reputation index is put up.
			ccom.setUserrating(new Long(1));
			String link = StrUtil.nonNull(ccom.getLink());
			String previewtext = StrUtil.nonNull(StrUtil.getTitleAlt(link));
			ccom.setPreviewtext(previewtext);
			Long commenterid = ccom.getSbcommenterid();
			int commenteridint = commenterid.intValue();
			int useridint = userid.intValue();
			if (commenteridint == useridint) {
				ccom.setIsowner(true);
			} else {
				ccom.setIsowner(false);
			}
			Long commentid = ccom.getId();
			int commentidint = commentid.intValue();
			List upvotelist = cardCommentVoteModelDAO
					.getAllUpRecordsByCommentId(commentid);
			List downvotelist = cardCommentVoteModelDAO
					.getAllDownRecordsByCommentId(commentid);
			
		//	ccom.setUpvotecount(new Long(upvotelist.size()));
		//	ccom.setDownvotecount(new Long(downvotelist.size()));
			ccom.setLikecount(new Long(upvotelist.size()));
			
		//	ccom.setIsupvoted(false);
		//	ccom.setIsdownvoted(false);
			ccom.setIsliked(false);
			for (int g = 0; g < upvotelist.size(); g++) {
				CardCommentVoteModel ccvm = (CardCommentVoteModel) upvotelist
						.get(g);
				Long useridcommvote = ccvm.getUserid();
				int useridcommvoteint = useridcommvote.intValue();
				if (useridint == useridcommvoteint) {
				//	ccom.setIsupvoted(true);
			//		ccom.setIsdownvoted(false);
					ccom.setIsliked(true);
					break;
				}

			}

			for (int g = 0; g < downvotelist.size(); g++) {
				CardCommentVoteModel ccvm = (CardCommentVoteModel) downvotelist
						.get(g);
				Long useridcommvote = ccvm.getUserid();
				int useridcommvoteint = useridcommvote.intValue();
				if (useridint == useridcommvoteint) {
				//	ccom.setIsupvoted(false);
				//	ccom.setIsdownvoted(true);
					ccom.setIsliked(false);
					break;
				}

			}

			finalList.add(ccom);
		}
		Gson gson = new Gson();
		String retStr = gson.toJson(finalList);

		response.setStatus(200);
		response.addHeader("Access-Control-Allow-Origin", "*");
		PrintWriter writer = response.getWriter();
		writer.write(retStr);
		writer.close();
		return null;
	}

}
