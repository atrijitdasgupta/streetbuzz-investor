/**
 * 
 */
package com.crowd.streetbuzz.jsoncontroller;

import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.crowd.streetbuzz.common.Constants;
import com.crowd.streetbuzz.dao.implementation.ProtoVoicesDAO;
import com.crowd.streetbuzz.dao.implementation.ProtoVoicesDetailsDAO;
import com.crowd.streetbuzz.model.ProtoVoices;
import com.crowd.streetbuzz.model.ProtoVoicesDetails;
import com.crowd.streetbuzzalgo.parser.TweetWithSentiments;
import com.crowd.streetbuzzalgo.utils.DataClean;
import com.crowd.streetbuzzalgo.utils.StrUtil;

import edu.stanford.nlp.pipeline.StanfordCoreNLP;

/**
 * @author Atrijit
 *
 */
public class ProtoFixController implements Controller, Constants{
	private ProtoVoicesDAO protoVoicesDAO;

	private ProtoVoicesDetailsDAO protoVoicesDetailsDAO;
	
	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
	//	List voicelist = protoVoicesDAO.getAllRecords();
		List voicedetailslist = protoVoicesDetailsDAO.getAllRecords();
		//System.out.println("Cleaning protovoices");
	/*	for (int i =0;i<voicelist.size();i++){
			ProtoVoices pv = (ProtoVoices)voicelist.get(i);
			String posttext = StrUtil.nonNull(pv.getPosttext());
			String voicetype = StrUtil.nonNull(pv.getVoicetype());
			posttext = DataClean.clean(posttext);
			voicetype = DataClean.clean(voicetype);
			pv.setPosttext(posttext);
			pv.setVoicetype(voicetype);
			protoVoicesDAO.addOrUpdateRecord(pv);
		}*/
	//	System.out.println("done Cleaning protovoices");
//		 Sentiment analysis
		/*Properties props = new Properties();
		props.setProperty("annotators", "tokenize, ssplit, parse, sentiment");
		StanfordCoreNLP pipeline = new StanfordCoreNLP(props);*/
		System.out.println("Cleaning protovoicesdetails");
		for (int i=0;i<voicedetailslist.size();i++){
			ProtoVoicesDetails pvd = (ProtoVoicesDetails)voicedetailslist.get(i);
			String temp = StrUtil.nonNull(pvd.getPosttext());
			System.out.println("temp: "+temp);
			if(!"".equalsIgnoreCase(temp)){
				pvd = new TweetWithSentiments().analyseSentmentProtoVoices(pvd);
				protoVoicesDetailsDAO.addOrUpdateRecord(pvd);
			}
			
			
		}
		System.out.println("Done Cleaning protovoicesdetails");
		return null;
	}

	public ProtoVoicesDAO getProtoVoicesDAO() {
		return protoVoicesDAO;
	}

	public void setProtoVoicesDAO(ProtoVoicesDAO protoVoicesDAO) {
		this.protoVoicesDAO = protoVoicesDAO;
	}

	public ProtoVoicesDetailsDAO getProtoVoicesDetailsDAO() {
		return protoVoicesDetailsDAO;
	}

	public void setProtoVoicesDetailsDAO(ProtoVoicesDetailsDAO protoVoicesDetailsDAO) {
		this.protoVoicesDetailsDAO = protoVoicesDetailsDAO;
	}
}
