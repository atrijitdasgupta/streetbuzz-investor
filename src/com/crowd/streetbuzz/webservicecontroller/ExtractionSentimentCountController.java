/**
 * 
 */
package com.crowd.streetbuzz.webservicecontroller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.crowd.streetbuzz.common.Constants;
import com.crowd.streetbuzz.dao.implementation.VoicesDAO;
import com.crowd.streetbuzz.dao.implementation.VoicesDetailsDAO;
import com.crowd.streetbuzz.model.Voices;
import com.crowd.streetbuzz.model.VoicesDetails;
import com.crowd.streetbuzzalgo.parser.TweetWithSentiments;
import com.crowd.streetbuzzalgo.scrapeandpost.ScrapePostFactory;
import com.crowd.streetbuzzalgo.utils.StrUtil;

/**
 * @author Atrijit
 * 
 */
public class ExtractionSentimentCountController implements Controller,
		Constants {
	private String returnView = "";

	private VoicesDAO voicesDAO;
	
	private VoicesDetailsDAO voicesDetailsDAO;

	public VoicesDAO getVoicesDAO() {
		return voicesDAO;
	}

	public void setVoicesDAO(VoicesDAO voicesDAO) {
		this.voicesDAO = voicesDAO;
	}

	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		List list = new ArrayList();

		list.add(new Long(581));
		list.add(new Long(547));
		list.add(new Long(537));
		list.add(new Long(430));
		list.add(new Long(416));
		list.add(new Long(359));
		list.add(new Long(282));
		list.add(new Long(330));
		list.add(new Long(564));
		list.add(new Long(422));
		
	/*	for (int i=0;i<list.size();i++){
			
			Long cardid = (Long)list.get(i);
			List voiceslist = voicesDAO.getAllRecordsbyCardId(cardid);
			long begin = System.currentTimeMillis();
			for (int j=0;j<voiceslist.size();j++){
				Voices voices = (Voices)voiceslist.get(j);
				String url = voices.getSourcelink();
				List scrapelist = new ArrayList();
				try {
					scrapelist = ScrapePostFactory.scrape(url);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					//System.out.println(e.getMessage());
				}
			}
			long end = System.currentTimeMillis();
			System.out.println("@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%");
			System.out.println("Time taken for comment extraction run for cardid "+cardid+", topic <topic> : "+((end-begin)/1000)+" seconds");
			System.out.println("@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%");
		}*/
		TweetWithSentiments tws = new TweetWithSentiments();
		
		for (int i=0;i<list.size();i++){
			Long cardid = (Long)list.get(i);
			List voiceslist = voicesDAO.getAllRecordsbyCardId(cardid);
			System.out.println("Got voiceslist");
			long begin = System.currentTimeMillis();
			for (int j=0;j<voiceslist.size();j++){
				Voices voices = (Voices)voiceslist.get(j);
				String channel = voices.getChannel();
				if(TWITTERSPP.equalsIgnoreCase(channel)){
					String posttext = voices.getPosttext();
					tws.analyseStringSentiment(posttext);
				}else{
					String posttext = StrUtil.nonNull(voices.getPosttext());
					if(!"".equalsIgnoreCase(posttext)){
						tws.analyseStringSentiment(posttext);
					}
					List vdlist = voicesDetailsDAO.getAllRecordsbyVoices(voices.getId());
					System.out.println("Got voicesdetailslist");
					for (int k=0;k<vdlist.size();k++){
						VoicesDetails vd = (VoicesDetails)vdlist.get(k);
						String posttextvd = vd.getPosttext();
						if(!"".equalsIgnoreCase(posttextvd)){
							tws.analyseStringSentiment(posttextvd);
						}
					}
				}
					
			}
			long end = System.currentTimeMillis();
			System.out.println("@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%");
			System.out.println("Time taken for sentiment analysis run for cardid "+cardid+", topic <topic> : "+((end-begin)/1000)+" seconds");
			System.out.println("@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%");
		}
		
		
		
		

		return new ModelAndView(returnView);
	}

	public VoicesDetailsDAO getVoicesDetailsDAO() {
		return voicesDetailsDAO;
	}

	public void setVoicesDetailsDAO(VoicesDetailsDAO voicesDetailsDAO) {
		this.voicesDetailsDAO = voicesDetailsDAO;
	}
}
