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
import com.crowd.streetbuzz.dao.implementation.UserDAO;
import com.crowd.streetbuzz.dao.implementation.VoiceCommentsDAO;
import com.crowd.streetbuzz.dao.implementation.VoiceShareDAO;
import com.crowd.streetbuzz.dao.implementation.VoiceVoteModelDAO;
import com.crowd.streetbuzz.dao.implementation.VoicesDAO;
import com.crowd.streetbuzz.dao.implementation.VoicesDetailsDAO;
import com.crowd.streetbuzz.json.JsonHeader;
import com.crowd.streetbuzz.json.JsonRequestBody;
import com.crowd.streetbuzz.model.User;
import com.crowd.streetbuzz.model.VoiceVoteModel;
import com.crowd.streetbuzz.model.Voices;
import com.crowd.streetbuzz.model.VoicesDetails;
import com.crowd.streetbuzzalgo.utils.StrUtil;
import com.google.gson.Gson;

/**
 * @author Atrijit
 * 
 */
public class GetVoicesController implements Controller, Constants {

	private VoicesDAO voicesDAO;

	private VoicesDetailsDAO voicesDetailsDAO;
	
	private UserDAO userDAO;
	
	private VoiceVoteModelDAO voiceVoteModelDAO;
	
	private VoiceCommentsDAO voiceCommentsDAO;
	
	private VoiceShareDAO voiceShareDAO;

	public VoiceShareDAO getVoiceShareDAO() {
		return voiceShareDAO;
	}

	public void setVoiceShareDAO(VoiceShareDAO voiceShareDAO) {
		this.voiceShareDAO = voiceShareDAO;
	}

	public VoiceVoteModelDAO getVoiceVoteModelDAO() {
		return voiceVoteModelDAO;
	}

	public void setVoiceVoteModelDAO(VoiceVoteModelDAO voiceVoteModelDAO) {
		this.voiceVoteModelDAO = voiceVoteModelDAO;
	}

	public VoicesDAO getVoicesDAO() {
		return voicesDAO;
	}

	public UserDAO getUserDAO() {
		return userDAO;
	}

	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

	public void setVoicesDAO(VoicesDAO voicesDAO) {
		this.voicesDAO = voicesDAO;
	}

	public VoicesDetailsDAO getVoicesDetailsDAO() {
		return voicesDetailsDAO;
	}

	public void setVoicesDetailsDAO(VoicesDetailsDAO voicesDetailsDAO) {
		this.voicesDetailsDAO = voicesDetailsDAO;
	}
	

	private List callQuery(JsonRequestBody jrb, JsonHeader jh) {
		String perpage = StrUtil.nonNull(jrb.getPerpage());
		String beforeid = StrUtil.nonNull(jrb.getBeforeid());
		String afterid = StrUtil.nonNull(jrb.getAfterid());
		String channel = StrUtil.nonNull(jrb.getChannel());
		String query = StrUtil.nonNull(jrb.getQ());
		String cardid = jh.getCardid();
		List vList = new ArrayList();
		if ("".equalsIgnoreCase(channel)) {
			channel = "all";
		}
		channel = channel.toLowerCase();
		if(!"".equalsIgnoreCase(query)){
			//It's a search request - and "all" is never the case anyway
			query = query.toLowerCase();
			vList = voicesDAO.doSearch(query, channel, new Long(cardid));
			return vList;
		}
		int perpageint = PERPAGEVOICES;

		if ("all".equalsIgnoreCase(channel)) {
			if (!"".equalsIgnoreCase(perpage) && "".equalsIgnoreCase(beforeid)
					&& "".equalsIgnoreCase(afterid)) {
				vList = voicesDAO.getAllRecordsbyCardIdPerpage(
						new Long(cardid), perpageint);
			}
			if (!"".equalsIgnoreCase(perpage) && !"".equalsIgnoreCase(beforeid)
					&& "".equalsIgnoreCase(afterid)) {
				vList = voicesDAO.getAllRecordsbyCardIdPerpageBeforeId(
						new Long(cardid), perpageint,
						new Long(beforeid));
			}
			if (!"".equalsIgnoreCase(perpage) && "".equalsIgnoreCase(beforeid)
					&& !"".equalsIgnoreCase(afterid)) {
				vList = voicesDAO.getAllRecordsbyCardIdPerpageAfterId(new Long(
						cardid), perpageint,
						new Long(afterid));
			}
		} else {
			if (!"".equalsIgnoreCase(perpage) && "".equalsIgnoreCase(beforeid)
					&& "".equalsIgnoreCase(afterid)) {
				vList = voicesDAO.getAllRecordsbyCardIdPerpageChannel(new Long(
						cardid), perpageint, channel);
			}
			if (!"".equalsIgnoreCase(perpage) && !"".equalsIgnoreCase(beforeid)
					&& "".equalsIgnoreCase(afterid)) {
				vList = voicesDAO.getAllRecordsbyCardIdPerpageBeforeIdChannel(
						new Long(cardid), perpageint,
						new Long(beforeid), channel);
			}
			if (!"".equalsIgnoreCase(perpage) && "".equalsIgnoreCase(beforeid)
					&& !"".equalsIgnoreCase(afterid)) {
				vList = voicesDAO.getAllRecordsbyCardIdPerpageAfterIdChannel(
						new Long(cardid), perpageint,
						new Long(afterid), channel);
			}
		}
		System.out.println("vList size:: "+vList.size());
		return vList;
	}

	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		System.out.println("Inside GetVoicesController ready to spotate");
		HttpSession session = request.getSession(true);
		JsonHeader jsonHeader = (JsonHeader) session.getAttribute("jsonHeader");
		String cardid = jsonHeader.getCardid();
		String cardtype = jsonHeader.getCardtype();
		String carduniqueid = jsonHeader.getCarduniqueid();
		String useridstr = jsonHeader.getUserid();
		Long userid = new Long(useridstr);
		User user = (User)userDAO.getObjectById(userid);

		String jsonbody = (String) session.getAttribute("jsonbody");
		Gson gson = new Gson();
		JsonRequestBody jrb = gson.fromJson(jsonbody, JsonRequestBody.class);

		
		List voicesList = callQuery(jrb, jsonHeader);

		List listforJson = new ArrayList();
		StringBuffer sbfr = new StringBuffer();
		String temp = "";
		for (int i = 0; i < voicesList.size(); i++) {
			Voices voices = (Voices) voicesList.get(i);
			sbfr.append(temp);
			Long voicesid = voices.getId();
			
			List commentslist = voiceCommentsDAO.getAllRecordsbyVoiceid(voicesid);
			
			voices.setCancomment(true);
			Long commentscount = new Long(0);
			if(commentslist!=null && commentslist.size()>0){
				commentscount = new Long(commentslist.size());
			}
			voices.setCommentscount(commentscount);
			voices.setExtcommentscount(new Long(0));
			voices.setSbuser(false);
			List upvotelist = voiceVoteModelDAO.getAllUpRecordsByVoiceid(voicesid);
			List downvotelist = voiceVoteModelDAO.getAllDownRecordsByVoiceid(voicesid);
			int upsize = 0;
			if(upvotelist!=null){
				upsize = upvotelist.size();
			}
			int downsize = 0;
			if(downvotelist!=null){
				downsize = downvotelist.size();
			}
		//	voices.setDislikecount(new Integer(downsize));
			voices.setLikecount(new Integer(upsize));
			
		//	voices.setIsdisliked(false);
			voices.setIsliked(false);
			String source = voices.getSource();
			String firstalph = StrUtil.getFirstletter(source);
			voices.setFirstletter(firstalph);
			
			for (int h=0;h<upvotelist.size();h++){
				VoiceVoteModel cvm = (VoiceVoteModel) upvotelist.get(h);
				Long useridcvmvote = cvm.getUserid();
				int useridcvmvoteint = useridcvmvote.intValue();
				int useridint = userid.intValue();
				if (useridint == useridcvmvoteint) {
					voices.setIsliked(true);
				//	voices.setIsdisliked(false);
					break;
				}
			}
			
			for (int h=0;h<downvotelist.size();h++){
				VoiceVoteModel cvm = (VoiceVoteModel) downvotelist.get(h);
				Long useridcvmvote = cvm.getUserid();
				int useridcvmvoteint = useridcvmvote.intValue();
				int useridint = userid.intValue();
				if (useridint == useridcvmvoteint) {
					voices.setIsliked(false);
				//	voices.setIsdisliked(true);
					break;
				}
			}
			
			List sharelist = voiceShareDAO.getAllSharesForVoiceid(voices.getId());
			Long sharecount = new Long(0);
			if(sharecount!=null){
				sharecount = new Long(sharelist.size());
			}
			voices.setSharecount(sharecount);

			// temp = gson.toJson(voices);
			listforJson.add(voices);
			// sbfr.append(temp);
		}
		// String responseString = sbfr.toString();
		String responseString = gson.toJson(listforJson);
		// String responseString = dummy();
	//	System.out.println("responseString: " + responseString);
		response.addHeader("Access-Control-Allow-Origin", "*");
		response.setStatus(200);
		PrintWriter writer = response.getWriter();
		writer.write(responseString);
		writer.close();

		return null;
	}
	
	
	
	private List convertVoicesDetails(List voicesDetailsList, User user){
		List list = new ArrayList();
		for (int i=0;i<voicesDetailsList.size();i++){
			VoicesDetails vd = (VoicesDetails)voicesDetailsList.get(i);
			Date voicedate = vd.getVoicedate();
			vd.setCommentdate(voicedate);
			vd.setAvatar("");
			vd.setMediaid("");
			vd.setMediaurl("");
			vd.setMediavideourl("");
			vd.setRating("");
			vd.setLink("");
			vd.setLinkpreviewurl("");
			vd.setPreviewtext("");
			vd.setUserrating(new Long(1));
			list.add(vd);
		}
		return list;
	}

	public VoiceCommentsDAO getVoiceCommentsDAO() {
		return voiceCommentsDAO;
	}

	public void setVoiceCommentsDAO(VoiceCommentsDAO voiceCommentsDAO) {
		this.voiceCommentsDAO = voiceCommentsDAO;
	}

	
}
