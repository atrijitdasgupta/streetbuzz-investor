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
import com.crowd.streetbuzz.dao.implementation.CardVoteModelDAO;
import com.crowd.streetbuzz.json.CardUpDownVoteResponse;
import com.crowd.streetbuzz.json.CardVote;
import com.crowd.streetbuzz.json.JsonHeader;
import com.crowd.streetbuzz.model.CardVoteModel;
import com.crowd.streetbuzzalgo.utils.StrUtil;
import com.google.gson.Gson;

/**
 * @author Atrijit
 * 
 */
public class CardVoteUpController implements Controller, Constants {
	private CardVoteModelDAO cardVoteModelDAO;

	public CardVoteModelDAO getCardVoteModelDAO() {
		return cardVoteModelDAO;
	}

	public void setCardVoteModelDAO(CardVoteModelDAO cardVoteModelDAO) {
		this.cardVoteModelDAO = cardVoteModelDAO;
	}

	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		System.out.println("Inside CardVoteUpController ready to spotate");
		HttpSession session = request.getSession(true);
		JsonHeader jsonHeader = (JsonHeader) session.getAttribute("jsonHeader");
		String cardid = StrUtil.nonNull(jsonHeader.getCardid());
		String userid = StrUtil.nonNull(jsonHeader.getUserid());
		String jsonbody = StrUtil.nonNull((String) session
				.getAttribute("jsonbody"));
		Gson gson = new Gson();
		PrintWriter writer = response.getWriter();
		CardVote cv = new CardVote();
		try {
			cv = (CardVote) gson.fromJson(jsonbody, CardVote.class);
		} catch (Exception e) {
			String errMsg = "empty jsonbody";
			response.setStatus(500);
			writer.write(STANDARERRORRESPONSE + errMsg);
			writer.close();
			return null;
		}
		String status = StrUtil.nonNull(cv.getStatus());

		Long cardidLong = new Long(0);
		if (!"".equalsIgnoreCase(cardid)) {
			cardidLong = new Long(cardid);
		}
		Long useridLong = new Long(0);
		if (!"".equalsIgnoreCase(userid)) {
			useridLong = new Long(userid);
		}
		Integer voteInt = new Integer(0);
		if (!"".equalsIgnoreCase(status)) {
			voteInt = new Integer(status);
		}
		CardUpDownVoteResponse cdvr = new CardUpDownVoteResponse();
		CardVoteModel existcvm = (CardVoteModel) cardVoteModelDAO
				.getObjectByUserIdCardIdType(useridLong, cardidLong, UPVOTE);
		if (existcvm != null && voteInt.intValue() == 0) {
			cardVoteModelDAO.deleteRecord(existcvm);
			
		}
		if (existcvm == null && voteInt.intValue() == 1) {
			CardVoteModel cvm = new CardVoteModel();
			cvm.setCardid(cardidLong);
			cvm.setUserid(useridLong);
			cvm.setVote(voteInt);
			cvm.setVotedatetime(new Date());
			cvm.setType(UPVOTE);
			cardVoteModelDAO.addOrUpdateRecord(cvm);
			
		}
		List cardvotelist = cardVoteModelDAO
				.getAllUpTypeRecordsByCardid(cardidLong);
		if (cardvotelist != null) {
			cdvr.setUpvotecount(new Long(cardvotelist.size()));
		} else {
			cdvr.setUpvotecount(new Long(0));
		}
		if(voteInt.intValue()==0){
			cdvr.setIsdownvoted(false);
			cdvr.setIsupvoted(false);
		}
		if(voteInt.intValue()==1){
			cdvr.setIsdownvoted(false);
			cdvr.setIsupvoted(true);
		}

		String responseString = gson.toJson(cdvr);
		response.setStatus(200);
		response.addHeader("Access-Control-Allow-Origin", "*");
		writer.write(responseString);
		writer.close();
		return null;
	}
}
