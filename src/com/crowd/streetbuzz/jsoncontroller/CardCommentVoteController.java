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
import com.crowd.streetbuzz.dao.implementation.CardCommentVoteModelDAO;
import com.crowd.streetbuzz.json.CardCommentVoteJSON;
import com.crowd.streetbuzz.json.CardCommentVoteResponse;
import com.crowd.streetbuzz.json.JsonHeader;
import com.crowd.streetbuzz.model.CardCommentVoteModel;
import com.crowd.streetbuzzalgo.utils.StrUtil;
import com.google.gson.Gson;

/**
 * @author Atrijit
 *
 */
public class CardCommentVoteController implements Controller, Constants{
	private CardCommentVoteModelDAO cardCommentVoteModelDAO;
	
	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		boolean noerror = true;
		System.out.println("Inside CardCommentVoteController ready to spotate");
		HttpSession session = request.getSession(true);
		JsonHeader jsonHeader = (JsonHeader) session.getAttribute("jsonHeader");
		String cardid = StrUtil.nonNull(jsonHeader.getCardid());
		String userid = StrUtil.nonNull(jsonHeader.getUserid());
		String jsonbody = StrUtil.nonNull((String) session
				.getAttribute("jsonbody"));
		Gson gson = new Gson();
		PrintWriter writer = response.getWriter();
		
		CardCommentVoteJSON ccvj = new CardCommentVoteJSON();
		try {
			ccvj = (CardCommentVoteJSON) gson.fromJson(jsonbody, CardCommentVoteJSON.class);
		} catch (Exception e) {
			noerror = false;
			String errMsg = "empty jsonbody";
			response.setStatus(500);
			writer.write(STANDARERRORRESPONSE + errMsg);
			writer.close();
			return null;
		}
		
		Integer voteInt = ccvj.getStatus();
		
		Long commentid = ccvj.getCommentid();
		
		List existccvm = cardCommentVoteModelDAO.getAllRecordsByCommentidUserid(commentid, new Long(userid));
		if(existccvm==null){
			existccvm = new ArrayList();
		}
		
		if(voteInt.intValue()==1 && existccvm.size()==0){
			CardCommentVoteModel ccvm = new CardCommentVoteModel();
			ccvm.setCommentid(commentid);
			ccvm.setVote(voteInt);
			ccvm.setUserid(new Long(userid));
			ccvm.setVotedatetime(new Date());
			cardCommentVoteModelDAO.addOrUpdateRecord(ccvm);
		}
		if(voteInt.intValue()==0 && existccvm.size()>0){
			for (int i=0;i<existccvm.size();i++){
				CardCommentVoteModel ccvm = (CardCommentVoteModel)existccvm.get(i);
				cardCommentVoteModelDAO.deleteRecord(ccvm);
			}
		}
		
		
		//Send response
		CardCommentVoteResponse ccvr = new CardCommentVoteResponse();
		List commentvotelist = cardCommentVoteModelDAO.getAllRecordsByCommentId(commentid);
		int upcount = 0;
		int downcount = 0;
		for (int i=0;i<commentvotelist.size();i++){
			CardCommentVoteModel mvcc = (CardCommentVoteModel)commentvotelist.get(i);
			Integer gotvote = mvcc.getVote();
			int num = gotvote.intValue();
			if (num == 1) {
				upcount = upcount + 1;
			} else {
				downcount = downcount + 1;
			}
		}
		ccvr.setLikecount(new Long(upcount));
		//ccvr.setDowncount(new Long(downcount));
		//ccvr.setUpcount(new Long(upcount));
		int intVote = voteInt.intValue();
		if(intVote ==1){
			ccvr.setIsliked(true);
			//ccvr.setIsupvoted(true);
			//ccvr.setIsdownvoted(false);
		}else{
			ccvr.setIsliked(false);
			//ccvr.setIsupvoted(false);
			//ccvr.setIsdownvoted(true);
		}
	
		
		String responseString = gson.toJson(ccvr);
		
		response.setStatus(200);
		response.addHeader("Access-Control-Allow-Origin", "*");
		writer.write(responseString);
		writer.close();
		return null;
	}

	public CardCommentVoteModelDAO getCardCommentVoteModelDAO() {
		return cardCommentVoteModelDAO;
	}

	public void setCardCommentVoteModelDAO(
			CardCommentVoteModelDAO cardCommentVoteModelDAO) {
		this.cardCommentVoteModelDAO = cardCommentVoteModelDAO;
	}
}
