/**
 * 
 */
package com.crowd.streetbuzz.jsoncontroller;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.crowd.streetbuzz.common.Constants;
import com.crowd.streetbuzz.dao.implementation.ConversationCardDAO;
import com.crowd.streetbuzz.dao.implementation.MediaFilesDAO;
import com.crowd.streetbuzz.dao.implementation.SearchCardDAO;
import com.crowd.streetbuzz.dao.implementation.VoicesDAO;
import com.crowd.streetbuzz.dao.implementation.VoicesDetailsDAO;
import com.crowd.streetbuzz.dao.implementation.WordCloudModelDAO;
import com.crowd.streetbuzz.dao.implementation.WordCloudStoreDAO;
import com.crowd.streetbuzz.json.JsonHeader;
import com.crowd.streetbuzz.json.JsonRequestBody;
import com.crowd.streetbuzzalgo.utils.GraphDataUtils;
import com.crowd.streetbuzzalgo.utils.StrUtil;
import com.google.gson.Gson;

/**
 * @author Atrijit
 * 
 */
public class GraphDataController implements Controller, Constants {

	private SearchCardDAO searchCardDAO;

	private ConversationCardDAO conversationCardDAO;

	private VoicesDAO voicesDAO;

	private VoicesDetailsDAO voicesDetailsDAO;

	private MediaFilesDAO mediaFilesDAO;

	private WordCloudModelDAO wordCloudModelDAO;
	
	private WordCloudStoreDAO wordCloudStoreDAO;

	public MediaFilesDAO getMediaFilesDAO() {
		return mediaFilesDAO;
	}

	public void setMediaFilesDAO(MediaFilesDAO mediaFilesDAO) {
		this.mediaFilesDAO = mediaFilesDAO;
	}

	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		System.out.println("Inside GraphDataController ready to spotate");
		HttpSession session = request.getSession(true);
		JsonHeader jsonHeader = (JsonHeader) session.getAttribute("jsonHeader");
		// Get the card id for which data needed
		String cardid = jsonHeader.getCardid();
		// Get the card type
		String type = jsonHeader.getCardtype();

		// Get the unique id also just in case
		String carduniqueid = jsonHeader.getCarduniqueid();
		String height = StrUtil.nonNull(jsonHeader.getHeight());
		String width = StrUtil.nonNull(jsonHeader.getWidth());

		String jsonbody = (String) session.getAttribute("jsonbody");
		Gson gson = new Gson();
		JsonRequestBody jrb = gson.fromJson(jsonbody, JsonRequestBody.class);

		String channel = StrUtil.nonNull(jrb.getChannel());

		// We also need the voiceid for this to work
		String voiceid = StrUtil.nonNull(jsonHeader.getVoiceid());
		String retStr = "";
		retStr = GraphDataUtils.getCardData(voicesDAO, voicesDetailsDAO,
				new Long(cardid), channel, conversationCardDAO, mediaFilesDAO,
				wordCloudModelDAO,wordCloudStoreDAO,height, width);
		

		response.setStatus(200);
		response.addHeader("Access-Control-Allow-Origin", "*");
		PrintWriter writer = response.getWriter();
		System.out
				.println("************************Being sent from GraphDataController************************");
		System.out.println(retStr);
		writer.write(retStr);
		writer.close();
		return null;
	}

	public ConversationCardDAO getConversationCardDAO() {
		return conversationCardDAO;
	}

	public void setConversationCardDAO(ConversationCardDAO conversationCardDAO) {
		this.conversationCardDAO = conversationCardDAO;
	}

	public SearchCardDAO getSearchCardDAO() {
		return searchCardDAO;
	}

	public void setSearchCardDAO(SearchCardDAO searchCardDAO) {
		this.searchCardDAO = searchCardDAO;
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

	public WordCloudModelDAO getWordCloudModelDAO() {
		return wordCloudModelDAO;
	}

	public void setWordCloudModelDAO(WordCloudModelDAO wordCloudModelDAO) {
		this.wordCloudModelDAO = wordCloudModelDAO;
	}

	public WordCloudStoreDAO getWordCloudStoreDAO() {
		return wordCloudStoreDAO;
	}

	public void setWordCloudStoreDAO(WordCloudStoreDAO wordCloudStoreDAO) {
		this.wordCloudStoreDAO = wordCloudStoreDAO;
	}

}
