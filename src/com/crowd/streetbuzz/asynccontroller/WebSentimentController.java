/**
 * 
 */
package com.crowd.streetbuzz.asynccontroller;

import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.crowd.streetbuzz.common.Constants;
import com.crowd.streetbuzz.dao.implementation.SentimentQueueDAO;
import com.crowd.streetbuzz.model.SentimentQueue;
import com.crowd.streetbuzzalgo.utils.ByteUtils;
import com.crowd.streetbuzzalgo.vo.ThreadObject;

/**
 * @author Atrijit
 *
 */
public class WebSentimentController implements Controller, Constants{
	private SentimentQueueDAO sentimentQueueDAO;
	public SentimentQueueDAO getSentimentQueueDAO() {
		return sentimentQueueDAO;
	}
	public void setSentimentQueueDAO(SentimentQueueDAO sentimentQueueDAO) {
		this.sentimentQueueDAO = sentimentQueueDAO;
	}
	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		List list = sentimentQueueDAO.getAllPendingRecords(new Long(0), WEBSPP);
		for (int i=0;i<list.size();i++){
			SentimentQueue sqweb = (SentimentQueue)list.get(i);
			String type = sqweb.getType();
			System.out.println(type);
			Long status = sqweb.getStatus();
			System.out.println(status);
			Long cardid = sqweb.getCardid();
			System.out.println(cardid);
			byte [] inblob = sqweb.getInblob();
			ThreadObject webobj = (ThreadObject)ByteUtils.deserialize(inblob);
			String objtype = webobj.getType();
			System.out.println(objtype);
			Map map = webobj.getMap();
			System.out.println(map);
		}
		response.setStatus(200);
		response.addHeader("Access-Control-Allow-Origin", "*");
		PrintWriter writer = response.getWriter();
		writer.write("");
		writer.close();
		return null;
	}
}
