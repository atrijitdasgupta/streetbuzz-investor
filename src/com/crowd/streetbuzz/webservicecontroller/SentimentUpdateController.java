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
import com.crowd.streetbuzz.dao.implementation.ConversationCardDAO;
import com.crowd.streetbuzz.dao.implementation.VoicesDAO;
import com.crowd.streetbuzz.dao.implementation.VoicesDetailsDAO;
import com.crowd.streetbuzz.model.ConversationCard;
import com.crowd.streetbuzz.model.Voices;
import com.crowd.streetbuzz.model.VoicesDetails;
import com.crowd.streetbuzzalgo.parser.MahoutCaller;
import com.crowd.streetbuzzalgo.utils.StrUtil;

/**
 * @author Atrijit
 * 
 */
public class SentimentUpdateController implements Controller, Constants {

	private ConversationCardDAO conversationCardDAO;

	private VoicesDAO voicesDAO;

	private VoicesDetailsDAO voicesDetailsDAO;

	private static final String POSITIVEPLUS = "positiveplus";

	private static final String POSITIVE = "positive";

	private static final String NEUTRAL = "neutral";

	private static final String NEGATIVEPLUS = "negative";

	private static final String NEGATIVE = "negativeplus";

	public ConversationCardDAO getConversationCardDAO() {
		return conversationCardDAO;
	}

	public void setConversationCardDAO(ConversationCardDAO conversationCardDAO) {
		this.conversationCardDAO = conversationCardDAO;
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

	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		System.out.println("Started processing sentiment update");
		List allcardslist = conversationCardDAO.getAllRecords();
		int pos = 0;
		int posplus = 0;
		int neu = 0;
		int neg = 0;
		int negplus = 0;
		List posl = new ArrayList();
		List pospl = new ArrayList();
		List neul = new ArrayList();
		List negl = new ArrayList();
		List negpl = new ArrayList();
		for (int i = 0; i < allcardslist.size(); i++) {
			ConversationCard cc = (ConversationCard) allcardslist.get(i);
			Long cardid = cc.getId();
			List voiceslist = voicesDAO.getAllRecordsbyCardId(cardid);
			/*int pos = 0;
			int posplus = 0;
			int neu = 0;
			int neg = 0;
			int negplus = 0;*/
			for (int j = 0; j < voiceslist.size(); j++) {
				Voices voices = (Voices) voiceslist.get(j);
				Long voicesid = voices.getId();
				List vdlist = voicesDetailsDAO.getAllRecordsbyVoices(voicesid);
				for (int k = 0; k < vdlist.size(); k++) {
					VoicesDetails vd = (VoicesDetails) vdlist.get(k);
					String line = StrUtil.nonNull(vd.getPosttext());
					if (!"".equalsIgnoreCase(line)) {
						String sentimentstr = MahoutCaller
								.getSentiments(line);
						System.out.println("vd sentimentstr: "+k+" : "+sentimentstr);
						if (POSITIVEPLUS.equalsIgnoreCase(sentimentstr)) {
							posplus = posplus + 1;
							pospl.add("");
						} else if (POSITIVE.equalsIgnoreCase(sentimentstr)) {
							pos = pos + 1;
							posl.add("");
						} else if (NEUTRAL.equalsIgnoreCase(sentimentstr)) {
							neu = neu + 1;
							neul.add("");
						} else if (NEGATIVEPLUS.equalsIgnoreCase(sentimentstr)) {
							negplus = negplus + 1;
							negpl.add("");
						} else if (NEGATIVE.equalsIgnoreCase(sentimentstr)) {
							neg = neg + 1;
							negl.add("");
						}
						int sentimentscore = MahoutCaller
								.getSentimentScore(sentimentstr);
						vd.setSentimentrating(sentimentstr);
						vd.setSentimentscore(new Long(sentimentscore));
						try {
							voicesDetailsDAO.addOrUpdateRecord(vd);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				String line = StrUtil.nonNull(voices.getPosttext());
				if (!"".equalsIgnoreCase(line)) {
					String sentimentstr = MahoutCaller.getSentiments(line);
					System.out.println("voices sentimentstr: "+j+" : "+sentimentstr);
					if (POSITIVEPLUS.equalsIgnoreCase(sentimentstr)) {
						posplus = posplus + 1;
						pospl.add("");
					} else if (POSITIVE.equalsIgnoreCase(sentimentstr)) {
						pos = pos + 1;
						posl.add("");
					} else if (NEUTRAL.equalsIgnoreCase(sentimentstr)) {
						neu = neu + 1;
						neul.add("");
					} else if (NEGATIVEPLUS.equalsIgnoreCase(sentimentstr)) {
						negplus = negplus + 1;
						negpl.add("");
					} else if (NEGATIVE.equalsIgnoreCase(sentimentstr)) {
						neg = neg + 1;
						negl.add("");
					}
					int sentimentscore = MahoutCaller
							.getSentimentScore(sentimentstr);
					voices.setSentimentrating(sentimentstr);
					voices.setSentimentscore(new Long(sentimentscore));
					try {
						voicesDAO.addOrUpdateRecord(voices);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}

			double finalpos = 0;
			double finalneg = 0;
			
			int posnum = posl.size();
			int pospnum = pospl.size();
			int neunum = neul.size();
			int negnum = negl.size();
			int negpnum = negpl.size();

			finalpos = posnum + 1.5 * pospnum;
			finalneg = negnum + 1.5 * negpnum;
			Double finalposd = new Double(finalpos);
			Double finalnegd = new Double(finalneg);
			cc.setPositivevoices(new Long(finalposd.longValue()));
			cc.setNegativevoices(new Long(finalnegd.longValue()));
			cc.setNeutralvoices(new Long(neunum));
			System.out.println("For card id: "+cc.getId()+" count is pos: "+finalposd.longValue()+", neg: "+finalnegd.longValue()+", neu: "+new Long(neu).toString());
			
			try {
				conversationCardDAO.addOrUpdateRecord(cc);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//Reset
			pos = 0;
			posplus = 0;
			neu = 0;
			neg = 0;
			negplus = 0;
			posl.clear();
			pospl.clear();
			neul.clear();
			negl.clear();
			negpl.clear();

		}
		System.out.println("Finished processing sentiment update");
		return null;
	}
}
