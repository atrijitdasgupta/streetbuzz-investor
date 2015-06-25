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
import com.crowd.streetbuzz.dao.implementation.CardExclusionDAO;
import com.crowd.streetbuzz.dao.implementation.CardVoteModelDAO;
import com.crowd.streetbuzz.dao.implementation.VoiceVoteModelDAO;
import com.crowd.streetbuzz.json.CardVote;
import com.crowd.streetbuzz.json.CardVoteResponse;
import com.crowd.streetbuzz.json.JsonHeader;
import com.crowd.streetbuzz.json.VoiceVoteResponse;
import com.crowd.streetbuzz.model.CardExclusion;
import com.crowd.streetbuzz.model.CardVoteModel;
import com.crowd.streetbuzz.model.VoiceVoteModel;
import com.crowd.streetbuzzalgo.utils.StrUtil;
import com.google.gson.Gson;

/**
 * @author Atrijit
 * 
 */
public class CardVoteController implements Controller, Constants {
	private CardVoteModelDAO cardVoteModelDAO;
	private CardExclusionDAO cardExclusionDAO;
	private VoiceVoteModelDAO voiceVoteModelDAO;
	public VoiceVoteModelDAO getVoiceVoteModelDAO() {
		return voiceVoteModelDAO;
	}

	public void setVoiceVoteModelDAO(VoiceVoteModelDAO voiceVoteModelDAO) {
		this.voiceVoteModelDAO = voiceVoteModelDAO;
	}

	public CardVoteModelDAO getCardVoteModelDAO() {
		return cardVoteModelDAO;
	}

	public void setCardVoteModelDAO(CardVoteModelDAO cardVoteModelDAO) {
		this.cardVoteModelDAO = cardVoteModelDAO;
	}

	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		boolean noerror = true;
		System.out.println("Inside CardVoteController ready to spotate");
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
			noerror = false;
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
		boolean isvoicevote = true;
		String voiceidStr = StrUtil.nonNull(jsonHeader.getVoiceid());
		voiceidStr = voiceidStr.trim();
		if("".equalsIgnoreCase(voiceidStr)){
			isvoicevote = false;
		}
		if(!isvoicevote){
			CardVoteModel cvm = new CardVoteModel();
			cvm.setCardid(cardidLong);
			cvm.setUserid(useridLong);
			cvm.setVote(voteInt);
			cvm.setVotedatetime(new Date());
			cardVoteModelDAO.addOrUpdateRecord(cvm);
			
			int voteinteger = voteInt.intValue();
			if(0==voteinteger){
				CardExclusion ce = new CardExclusion();
				ce.setCardid(new Long(cardid));
				ce.setUserid(new Long(userid));
				cardExclusionDAO.addOrUpdateRecord(ce);
				
			}

			CardVoteResponse CVR = new CardVoteResponse();
			List cardvotelist = cardVoteModelDAO.getAllRecordsByCardid(cardidLong);
			int upcount = 0;
			int downcount = 0;

			for (int i = 0; i < cardvotelist.size(); i++) {
				CardVoteModel mvc = (CardVoteModel) cardvotelist.get(i);
				Integer gotvote = mvc.getVote();
				int num = gotvote.intValue();
				if (num == 1) {
					upcount = upcount + 1;
				} else {
					downcount = downcount + 1;
				}

			}
			CVR.setDowncount(new Long(downcount));
			CVR.setUpcount(new Long(upcount));
			int intVote = voteInt.intValue();
			if(intVote ==1){
				CVR.setIsupvoted(true);
				CVR.setIsdownvoted(false);
			}else{
				CVR.setIsupvoted(false);
				CVR.setIsdownvoted(true);
			}
			
			String responseString = gson.toJson(CVR);

			response.setStatus(200);
			response.addHeader("Access-Control-Allow-Origin", "*");

			writer.write(responseString);
			writer.close();
		}
		if(isvoicevote){
			Long voiceid = new Long(voiceidStr);
			List existvvm = voiceVoteModelDAO.getAllRecordsByVoiceidUserid(voiceid, new Long(userid));
			if(existvvm==null){
				existvvm = new ArrayList();
			}
			if(existvvm.size()==0 && voteInt.intValue()==1){
				VoiceVoteModel vvm = new VoiceVoteModel();
				vvm.setVoiceid(voiceid);
				vvm.setUserid(useridLong);
				vvm.setVote(voteInt);
				vvm.setVotedatetime(new Date());
				voiceVoteModelDAO.addOrUpdateRecord(vvm);
			}
			if(existvvm.size()>0 && voteInt.intValue()==0){
				for (int i=0;i<existvvm.size();i++){
					VoiceVoteModel evvm = (VoiceVoteModel)existvvm.get(i);
					voiceVoteModelDAO.deleteRecord(evvm);
				}
			}
			
			VoiceVoteResponse VVR = new VoiceVoteResponse();
			List voicevotelist = voiceVoteModelDAO.getAllRecordsByVoiceid(voiceid);
			int upcount = 0;
			int downcount = 0;
			
			for (int i = 0; i < voicevotelist.size(); i++) {
				VoiceVoteModel mvc = (VoiceVoteModel) voicevotelist.get(i);
				Integer gotvote = mvc.getVote();
				int num = gotvote.intValue();
				if (num == 1) {
					upcount = upcount + 1;
				} else {
					downcount = downcount + 1;
				}

			}
			
		//	VVR.setDislikecount(new Long(downcount));
			VVR.setLikecount(new Long(upcount));
			int intVote = voteInt.intValue();
			if(intVote ==1){
				VVR.setIsliked(true);
		//		VVR.setIsdisliked(false);
			}else{
				VVR.setIsliked(false);
		//		VVR.setIsdisliked(true);
			}
			
			String responseString = gson.toJson(VVR);

			response.setStatus(200);
			response.addHeader("Access-Control-Allow-Origin", "*");

			writer.write(responseString);
			writer.close();
			
		}
		
		return null;
	}

	public CardExclusionDAO getCardExclusionDAO() {
		return cardExclusionDAO;
	}

	public void setCardExclusionDAO(CardExclusionDAO cardExclusionDAO) {
		this.cardExclusionDAO = cardExclusionDAO;
	}
}
