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
import com.crowd.streetbuzz.dao.implementation.VoicesDAO;
import com.crowd.streetbuzz.dao.implementation.VoicesDetailsDAO;
import com.crowd.streetbuzz.json.JsonHeader;
import com.crowd.streetbuzz.json.JsonRequestBody;
import com.crowd.streetbuzz.model.Voices;
import com.crowd.streetbuzz.model.VoicesDetails;
import com.crowd.streetbuzzalgo.utils.StrUtil;
import com.google.gson.Gson;

/**
 * @author Atrijit
 * 
 */
public class CommentController implements Controller, Constants {
	private VoicesDAO voicesDAO;

	private VoicesDetailsDAO voicesDetailsDAO;

	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		System.out.println("Inside CommentController ready to spotate");
		HttpSession session = request.getSession(true);
		JsonHeader jsonHeader = (JsonHeader) session.getAttribute("jsonHeader");

		String cardid = jsonHeader.getCardid();
		String cardtype = jsonHeader.getCardtype();
		String carduniqueid = jsonHeader.getCarduniqueid();
		String afterid = StrUtil.nonNull(jsonHeader.getAfterid());

		String jsonbody = (String) session.getAttribute("jsonbody");
		Gson gson = new Gson();
		JsonRequestBody jrb = gson.fromJson(jsonbody, JsonRequestBody.class);
		String channel = StrUtil.nonNull(jrb.getChannel());

		List voicesList = new ArrayList();

		if ("all".equalsIgnoreCase(channel)) {
			voicesList = voicesDAO.getAllRecordsbyCardUniqueId(carduniqueid);
		} else {
			voicesList = voicesDAO.getAllRecordsbyCardUniqueIdAndSource(
					carduniqueid, channel);
		}
		StringBuffer sbfr = new StringBuffer();
		String temp = "";
		for (int i = 0; i < voicesList.size(); i++) {
			Voices voices = (Voices) voicesList.get(i);
			sbfr.append(temp);
			Long voicesid = voices.getId();
			List voiceDetailsList = new ArrayList();
			if ("".equalsIgnoreCase(afterid)) {
				voiceDetailsList = voicesDetailsDAO
						.getAllRecordsbyVoices(voicesid);
			} else {
				Long afteridLong = new Long(afterid);
				voiceDetailsList = voicesDetailsDAO
						.getAllRecordsbyVoicesAfterId(voicesid, afteridLong);
			}
			voices.setComments(voiceDetailsList);
			temp = gson.toJson(voices);
			sbfr.append(temp);
		}

		String responseString = sbfr.toString();
		//String responseString = dummy();
		System.out.println("responseString: " + responseString);
		response.addHeader("Access-Control-Allow-Origin", "*");
		response.setStatus(200);
		PrintWriter writer = response.getWriter();
		writer.write(responseString);
		writer.close();

		return null;
	}
	
	private String dummy(){
		Voices v1 = new Voices();
		v1.setAuthor("author1");
		v1.setFbgroupid("fbgroupid1");
		v1.setAvatar("http://lorempixel.com/output/people-q-c-40-40-2.jpg");
		v1.setCardid(new Long (123));
		v1.setCardtype("C");
		v1.setCarduniqueid("811611151");
		v1.setCommentscount(new Long(23));
		v1.setExtcommentscount(new Long(12));
		v1.setExtlikescount(new Long(22));
		v1.setExtviewscount(new Long(24));
		v1.setFbgroupid("121212121");
		v1.setId(new Long(211));
		v1.setLikescount(new Long(97));
		v1.setPositivephrase("great");
		v1.setPostauthorid("747");
		v1.setPostid("323");
		v1.setPosttext("great coffee in Pune");
		v1.setPosttextauthor("Sam");
		v1.setSentimentrating("positive");
		v1.setSentimentscore(new Long(8));
		v1.setSource("FB");
		v1.setSourcelink("http://www.google.com");
		v1.setThumbsdowncount(new Long(2));
		v1.setThumbsupcount(new Long(9));
		v1.setUniquevoiceid("828223823238");
		v1.setUserid(new Long(22));
		v1.setVoicesdate(new Date());
		v1.setVoicetype("POST");
		v1.setThumb("http://lorempixel.com/400/200/sports");
		
		
		
		List v1List = new ArrayList();
		List v2List = new ArrayList();
		
		VoicesDetails vd1 = new VoicesDetails();
		vd1.setPosttext("text1");
		vd1.setNegativephrase("negativephrase1");
		vd1.setNeutralphrase("neutralphrase1");
		vd1.setPositivephrase("positivephrase1");
		vd1.setPostauthorid("postauthorid1");
		vd1.setPosttext("posttext1");
		vd1.setPosttextauthor("posttextauthor1");
		vd1.setSentimentrating("sentimentrating1");
		vd1.setSentimentscore(new Long(3));
		vd1.setVoicedate(new Date());
		vd1.setVoicesid(new Long(211));
		
		VoicesDetails vd2 = new VoicesDetails();
		vd2.setPosttext("text2");
		vd2.setNegativephrase("negativephrase2");
		vd2.setNeutralphrase("neutralphrase2");
		vd2.setPositivephrase("positivephrase2");
		vd2.setPostauthorid("postauthorid2");
		vd2.setPosttext("posttext2");
		vd2.setPosttextauthor("posttextauthor2");
		vd2.setSentimentrating("sentimentrating2");
		vd2.setSentimentscore(new Long(4));
		vd2.setVoicedate(new Date());
		vd2.setVoicesid(new Long(211));
		
		
		
		v1List.add(vd1);
		v1List.add(vd2);
		
		
		
		v1.setComments(v1List);
		
		
		Gson gson = new Gson();
		
		
		String temp = gson.toJson(v1);
		
		
		
		return temp;
	}

	public VoicesDAO getVoicesDAO() {
		return voicesDAO;
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

}
