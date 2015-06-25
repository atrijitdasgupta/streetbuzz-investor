/**
 * 
 */
package com.crowd.streetbuzz.webservicecontroller;

import java.io.PrintWriter;
import java.util.List;
import java.util.SortedSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.aliasi.util.ScoredObject;
import com.crowd.streetbuzz.common.Constants;
import com.crowd.streetbuzz.dao.implementation.InterestKeywordsDAO;
import com.crowd.streetbuzz.dao.implementation.InterestReferenceModelDAO;
import com.crowd.streetbuzz.dao.implementation.InterestSearchTermsDAO;
import com.crowd.streetbuzz.model.InterestKeywords;
import com.crowd.streetbuzz.model.InterestReferenceModel;
import com.crowd.streetbuzz.model.InterestSearchTerms;
import com.crowd.streetbuzzalgo.lingpipe.InterestingPhrases;
import com.crowd.streetbuzzalgo.parser.EntityParser;
import com.crowd.streetbuzzalgo.parser.vo.StanfordNerVO;
import com.crowd.streetbuzzalgo.utils.StrUtil;

import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreLabel;

/**
 * @author Atrijit
 * 
 */
public class InterestKeywordsController implements Controller, Constants {
	private InterestSearchTermsDAO interestSearchTermsDAO;

	private InterestReferenceModelDAO interestReferenceModelDAO;

	private InterestKeywordsDAO interestKeywordsDAO;

	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		List completedList = interestSearchTermsDAO.getAllRecordsCompleted();
		for (int i = 0; i < completedList.size(); i++) {
			InterestSearchTerms ist = (InterestSearchTerms) completedList
					.get(i);
			Long interestid = ist.getInterestid();
			Long parentinterestid = ist.getParentinterestid();
			List matches = interestReferenceModelDAO
					.getAllRecordsByIntParentIntIds(interestid,
							parentinterestid);
			StringBuffer sbfr = new StringBuffer();
			for (int j = 0; j < matches.size(); j++) {
				InterestReferenceModel irm = (InterestReferenceModel) matches
						.get(j);
				String body = StrUtil.nonNull(irm.getBody());
				sbfr.append(body + " ");
			}
			InterestingPhrases obj = new InterestingPhrases();
			SortedSet<ScoredObject<String[]>> nGrams = obj.trainFromString(sbfr
					.toString());
			for (ScoredObject<String[]> nGram : nGrams) {
				double score = nGram.score();
				String[] toks = nGram.getObject();
				String accum = "";
				for (int j = 0; j < toks.length; ++j) {
					accum += " " + toks[j];

				}
				InterestKeywords ik = new InterestKeywords();
				ik.setInterestid(interestid);
				ik.setParentinterestid(parentinterestid);
				ik.setKeyword(accum);
				ik.setScore(new Double(score));
				System.out.println(accum + " :: " + score);
				interestKeywordsDAO.addOrUpdateRecord(ik);
			}
			
		}
		response.setStatus(200);
		response.addHeader("Access-Control-Allow-Origin", "*");
		PrintWriter writer = response.getWriter();
		writer.write("");
		writer.close();
		return null;
	}

	public InterestKeywordsDAO getInterestKeywordsDAO() {
		return interestKeywordsDAO;
	}

	public void setInterestKeywordsDAO(InterestKeywordsDAO interestKeywordsDAO) {
		this.interestKeywordsDAO = interestKeywordsDAO;
	}

	public InterestReferenceModelDAO getInterestReferenceModelDAO() {
		return interestReferenceModelDAO;
	}

	public void setInterestReferenceModelDAO(
			InterestReferenceModelDAO interestReferenceModelDAO) {
		this.interestReferenceModelDAO = interestReferenceModelDAO;
	}

	public InterestSearchTermsDAO getInterestSearchTermsDAO() {
		return interestSearchTermsDAO;
	}

	public void setInterestSearchTermsDAO(
			InterestSearchTermsDAO interestSearchTermsDAO) {
		this.interestSearchTermsDAO = interestSearchTermsDAO;
	}
}
