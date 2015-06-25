/**
 * 
 */
package com.crowd.streetbuzz.helper;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.crowd.streetbuzz.common.Constants;
import com.crowd.streetbuzzalgo.parser.EntityParser;
import com.crowd.streetbuzzalgo.parser.RankedKeywordParse;
import com.crowd.streetbuzzalgo.parser.RelationsParse;
import com.crowd.streetbuzzalgo.parser.vo.LocationEntityVO;
import com.crowd.streetbuzzalgo.parser.vo.RankedKeywordVO;
import com.crowd.streetbuzzalgo.parser.vo.RelationsParseVO;
import com.crowd.streetbuzzalgo.parser.vo.StanfordNerVO;
import com.crowd.streetbuzzalgo.parser.vo.SubjectEntityVO;
import com.crowd.streetbuzzalgo.stopwords.StopWordRemoval;
import com.crowd.streetbuzzalgo.synonym.WikiSynonymAPICaller;
import com.crowd.streetbuzzalgo.taxonomy.freebase.Freebase;
import com.crowd.streetbuzzalgo.taxonomy.googlesuggest.GoogleSuggest;
import com.crowd.streetbuzzalgo.utils.DataClean;
import com.crowd.streetbuzzalgo.utils.StrUtil;
import com.crowd.streetbuzzalgo.utils.TemporaryCategoryUtils;

import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreLabel;

/**
 * @author Atrijit
 * 
 */
public class FirstProcessHelperOld implements Constants {

	/**
	 * 
	 */
	public FirstProcessHelperOld() {
		// TODO Auto-generated constructor stub
	}

	public HttpSession process(String entry, String category,
			HttpServletRequest request) {
		HttpSession session = request.getSession(true);
		// First clean the String
		entry = this.clean(entry);

		// Check out the category value (page passes numeric value)
		String categoryStr = getCategory(category);

		// do core parsing on the String to find concepts, entities etc.
		List rankedKeywordsList = this.rankedKeywordsParse(entry);
		RelationsParseVO rvo = this.relationsParse(entry);
		StanfordNerVO stanfordNerVO = this.getStanfordNamedEntities(entry);
		
		
		
		List subjectverbactionlist = rvo.getSubjectactiontext();
		String action = "";
		if (subjectverbactionlist != null && subjectverbactionlist.size() > 0) {
			action = (String) subjectverbactionlist.get(0);
		}
		List subjectverbtext = rvo.getSubjectverbtext();
		if ("".equalsIgnoreCase(action)){
			if(subjectverbtext!=null && subjectverbtext.size()>0){
				action = (String)subjectverbtext.get(0);
			}
			
		}
		if("do".equalsIgnoreCase(action)){
			action = "";
		}
		
		
		List subjectentities = rvo.getSubjectentities();
		List synonymList = getSynonyms(subjectentities);
		List keywordSynonymList = getSynonymsKeywords(rankedKeywordsList,
				synonymList);
		

		List objecttext = rvo.getObjecttext();
		// this list has all synonyms
		List objecttextSynonymList = getObjectTextSynonymList(objecttext,
				keywordSynonymList);

		//List googleSuggestList = googleSuggest(rankedKeywordsList,objecttextSynonymList);
//		 this list has all synonyms
		//List finalSynonymList = getStemmedSynonymsKeywords(rankedKeywordsList,googleSuggestList);
		List finalSynonymList = getStemmedSynonymsKeywords(rankedKeywordsList,objecttextSynonymList);
		

		// Make up the different search terms
		List searchTermsListA = this.createSearchTermsRanked(categoryStr,
				rankedKeywordsList, action);
		List searchTermsListB = this.createSearchTermsRelations(categoryStr,
				rvo, finalSynonymList);
		List searchTermsListLocationC = this.createLocationSearchTerms(
				searchTermsListA, searchTermsListB, rvo);

		List searchTermsList = new ArrayList();
		for (int i = 0; i < searchTermsListA.size(); i++) {
			String st1 = (String) searchTermsListA.get(i);
			if (!searchTermsList.contains(st1)) {
				searchTermsList.add(st1);
			}
		}

		for (int j = 0; j < searchTermsListB.size(); j++) {
			String st2 = (String) searchTermsListB.get(j);
			if (!searchTermsList.contains(st2)) {
				searchTermsList.add(st2);
			}
		}
		List searchTermsListCleaned = new ArrayList();
		if(searchTermsList!=null && searchTermsList.size()>0){
			searchTermsListCleaned = SearchResultListCleanerHelper.removeIrrelevancy(searchTermsList, entry, action);
		}
		
		List searchTermsListLocationCleaned = new ArrayList();
		if(searchTermsListLocationC!=null && searchTermsListLocationC.size()>0){
			searchTermsListLocationCleaned = SearchResultListCleanerHelper.removeIrrelevancy(searchTermsListLocationC, entry, action);
		}
		List firstList = removeDuplicateVerbs(searchTermsListCleaned, action);
		List secondList = removeDuplicateVerbs(searchTermsListLocationCleaned, action);
		firstList.add(entry);

		session.setAttribute("categoryStr", categoryStr);
		session.setAttribute("synonymList", finalSynonymList);
		session.setAttribute("rankedKeywordsList", rankedKeywordsList);
		session.setAttribute("searchTermsList", firstList);
		session.setAttribute("RelationsParseVO", rvo);
		session.setAttribute("searchTermsListLocation",
				secondList);
		session.setAttribute("stanfordNerVO", stanfordNerVO);
		return session;
	}
	private List removeDuplicateVerbs(List list, String verb){
		List retList = new ArrayList();
		for(int i=0;i<list.size();i++){
			String temp = (String)list.get(i);
			temp = StrUtil.removeDuplicates(temp, verb);
			retList.add(temp);
		}
		return retList;
	}
	
	private List getStemmedSynonymsKeywords(List rankedKeywordsList,List googleSuggestList){
		List stemKeywordsRKVOList = new ArrayList();
		for (int i=0;i<rankedKeywordsList.size();i++){
			RankedKeywordVO rkvo = (RankedKeywordVO) rankedKeywordsList.get(i);
			String rankedkey = rkvo.getText();
			StringBuffer sbfr = new StringBuffer();
			String [] textArr = rankedkey.split(" ");
			for (int j=0;j<textArr.length;j++){
				String temp = textArr[j];
				String stem = StrUtil.nonNull(new StopWordRemoval().stem(temp));
				if("".equalsIgnoreCase(stem)){
					sbfr.append(temp+" ");
				}
			}
			String ftemp = sbfr.toString();
			ftemp = ftemp.trim();
			rkvo.setText(ftemp);
			stemKeywordsRKVOList.add(rkvo);
		}
		//now use the existing function
		List fList = getSynonymsKeywords(stemKeywordsRKVOList,googleSuggestList);
		return fList;
	}

	private List googleSuggest(List rankedKeywordsList,
			List objecttextSynonymList) {
		List finalList = new ArrayList();
		for (int j = 0; j < rankedKeywordsList.size(); j++) {
			RankedKeywordVO rkvo = (RankedKeywordVO) rankedKeywordsList.get(j);
			String rankedkey = rkvo.getText();
			List gList = GoogleSuggest.googleSuggest(rankedkey);
			finalList.addAll(gList);
		}
		finalList.addAll(objecttextSynonymList);
		return finalList;
	}

	private StanfordNerVO getStanfordNamedEntities(String entry) {
		StanfordNerVO snvo = new StanfordNerVO();
		try {
			snvo = EntityParser.coreparse(entry);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return snvo;
	}

	private List createSearchTermsRelations(String categoryStr,
			RelationsParseVO rvo, List synonymList) {
		List searchTermsList = new ArrayList();
		/*
		 * String verb = rvo.getSubjectverbtext(); String action =
		 * rvo.getSubjectactiontext();
		 */
		List subjectverbtextlist = rvo.getSubjectverbtext();
		String verb = "";
		if (subjectverbtextlist != null && subjectverbtextlist.size() > 0) {
			verb = (String) subjectverbtextlist.get(0);
		}

		List subjectverbactionlist = rvo.getSubjectactiontext();
		String action = "";
		if (subjectverbactionlist != null && subjectverbactionlist.size() > 0) {
			action = (String) subjectverbactionlist.get(0);
		}
		if("do".equalsIgnoreCase(action)){
			action = "";
			verb = "";
		}
		List subjectverbtext = rvo.getSubjectverbtext();
		if ("".equalsIgnoreCase(action)){
			if(subjectverbtext!=null && subjectverbtext.size()>0 ){
				action = (String)subjectverbtext.get(0);
			}
			
		}

		List loclist = rvo.getLocationentities();
		for (int i = 0; i < synonymList.size(); i++) {
			String synonym = (String) synonymList.get(i);
			String temp = "";
			if(!"".equalsIgnoreCase(verb)){
				//temp = verb + " " + synonym;
				if(synonym.indexOf(verb)<0){
					temp = synonym+ " "+verb;
				}
				 
			}else{
				//temp = action + " " + synonym;
				if(synonym.indexOf(action)<0){
					temp = synonym+" "+action;
				}
				
			}
			
			searchTermsList.add(temp);
			/*
			 * temp = action +" "+ synonym; searchTermsList.add(temp);
			 */

		}
		List locSpecList = new ArrayList();
		for (int j = 0; j < searchTermsList.size(); j++) {
			String temp = (String) searchTermsList.get(j);
			for (int k = 0; k < loclist.size(); k++) {
				LocationEntityVO levo = (LocationEntityVO) loclist.get(k);
				String text = levo.getText();
				if (temp.indexOf(text) < 0) {
					temp = temp + " " + text;
				}
				if (!locSpecList.contains(temp)) {
					locSpecList.add(temp);
				}

			}
		}
		return searchTermsList;
	}

	private List createLocationSearchTerms(List searchTermsListA,
			List searchTermsListB, RelationsParseVO rvo) {
		List list = new ArrayList();
		List loclist = rvo.getLocationentities();
		boolean addbool = false;
		for (int i = 0; i < loclist.size(); i++) {
			LocationEntityVO levo = (LocationEntityVO) loclist.get(i);

			String loctext = StrUtil.nonNull(levo.getEtext());
			if ("".equalsIgnoreCase(loctext)) {
				loctext = StrUtil.nonNull(levo.getText());
			}
			System.out.println("Getting loctext:: " + loctext);
			for (int j = 0; j < searchTermsListA.size(); j++) {
				String temp = (String) searchTermsListA.get(j);
				String templow = temp.toLowerCase();
				String loctextlow = loctext.toLowerCase();
				if (templow.indexOf(loctextlow) < 0) {
					temp = temp + " " + loctext;
					addbool = true;
				}
				if (!list.contains(temp)) {
					if (addbool) {
						list.add(temp);
						addbool = false;
					}

				}
			}
			for (int k = 0; k < searchTermsListB.size(); k++) {
				String temp = (String) searchTermsListB.get(k);
				String templow = temp.toLowerCase();
				String loctextlow = loctext.toLowerCase();
				if (templow.indexOf(loctextlow) < 0) {
					temp = temp + " " + loctext;
					addbool = true;
				}
				if (!list.contains(temp)) {
					if (addbool) {
						temp = temp.trim();
						list.add(temp);
						addbool = false;
					}

				}
			}
		}
		return list;
	}

	private List createSearchTermsRanked(String categoryStr,
			List rankedKeywordsList, String action) {
		List searchTermsList = new ArrayList();
		StringBuffer sbfr = new StringBuffer();
		for (int i = 0; i < rankedKeywordsList.size(); i++) {
			RankedKeywordVO rkvo = (RankedKeywordVO) rankedKeywordsList.get(i);
			String temp = StrUtil.nonNull(rkvo.getText());
			if(temp.indexOf(action)<0){
				sbfr.append(temp + " "+action);
			}
			
		}
		String str = sbfr.toString();
		str = str.trim();
		searchTermsList.add(str);
		searchTermsList.add(categoryStr + " " + str);

		return searchTermsList;
	}

	private List getObjectTextSynonymList(List objecttext,
			List keywordSynonymList) {

		List retList = new ArrayList();
		if (objecttext != null) {
			for (int i = 0; i < objecttext.size(); i++) {
				String temp = (String) objecttext.get(i);
				temp = this.coreparseCheck(temp);
				try {
					List wikilist = WikiSynonymAPICaller.getSynonymsList(temp);
					retList.addAll(wikilist);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					List freebaselist = Freebase.getList(temp);
					retList.addAll(freebaselist);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		List finalList = new ArrayList();
		for (int j = 0; j < retList.size(); j++) {
			String temp = (String) retList.get(j);
			if (!finalList.contains(temp)) {
				finalList.add(temp);
			}
		}

		for (int g = 0; g < keywordSynonymList.size(); g++) {
			String temp = (String) keywordSynonymList.get(g);
			if (!finalList.contains(temp)) {
				finalList.add(temp);
			}
		}

		return finalList;
	}

	private String coreparseCheck(String temp) {
		String serializedClassifier = BASEEXTLIBPATH+"stanfordcorelib/english.all.3class.distsim.crf.ser.gz";
		AbstractSequenceClassifier<CoreLabel> classifier = CRFClassifier
				.getClassifierNoExceptions(serializedClassifier);
		String[] entretemps = temp.split(" ");
		StringBuffer sbfr = new StringBuffer();
		for (int k = 0; k < entretemps.length; k++) {
			String etemp = entretemps[k];
			try {
				if (EntityParser.coreparseCheck(etemp, classifier)) {
					sbfr.append(" " + etemp);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		temp = sbfr.toString();
		return temp;
	}

	private List getSynonymsKeywords(List keywordList, List synonymList) {
		List retList = new ArrayList();
		if (keywordList != null) {
			for (int i = 0; i < keywordList.size(); i++) {
				RankedKeywordVO rkvo = (RankedKeywordVO) keywordList.get(i);
				String temp = StrUtil.nonNull(rkvo.getText());
				temp = this.coreparseCheck(temp);
				try {
					List wikilist = WikiSynonymAPICaller.getSynonymsList(temp);
					retList.addAll(wikilist);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					List freebaselist = Freebase.getList(temp);
					retList.addAll(freebaselist);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		List finalList = new ArrayList();
		for (int j = 0; j < retList.size(); j++) {
			String temp = (String) retList.get(j);
			if (!finalList.contains(temp)) {
				finalList.add(temp);
			}
		}
		for (int g = 0; g < synonymList.size(); g++) {
			String temp = (String) synonymList.get(g);
			if (!finalList.contains(temp)) {
				finalList.add(temp);
			}
		}
		return finalList;
	}

	private List getSynonyms(List subjectentities) {
		List retList = new ArrayList();
		if (subjectentities != null) {
			for (int i = 0; i < subjectentities.size(); i++) {
				SubjectEntityVO sevo = (SubjectEntityVO) subjectentities.get(i);
				String temp = sevo.getText();
				temp = this.coreparseCheck(temp);
				try {
					List wikilist = WikiSynonymAPICaller.getSynonymsList(temp);
					retList.addAll(wikilist);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					List freebaselist = Freebase.getList(temp);
					retList.addAll(freebaselist);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		List finalList = new ArrayList();
		for (int j = 0; j < retList.size(); j++) {
			String temp = (String) retList.get(j);
			if (!finalList.contains(temp)) {
				finalList.add(temp);
			}
		}
		return finalList;
	}

	private List getWikiSynonymsForCategory(String str, String[] array) {

		List list = new ArrayList();
		try {
			list = WikiSynonymAPICaller.getSynonymsList(str);
			for (int j = 0; j < array.length; j++) {
				String temp = array[j];
				temp = temp.trim();
				// temp = this.coreparseCheck(temp);
				List tempList = WikiSynonymAPICaller.getSynonymsList(temp);
				list.addAll(tempList);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		// clean the list for duplicates - can come from multiple keyword
		// lookups
		List finalList = new ArrayList();
		for (int k = 0; k < list.size(); k++) {
			String val = (String) list.get(k);
			if (!finalList.contains(val)) {
				finalList.add(val);
			}
		}
		return finalList;
	}

	private List getFreebaseValuesForCategory(String str, String[] array) {
		List list = new ArrayList();
		try {
			list = Freebase.getList(str);
			for (int j = 0; j < array.length; j++) {
				String temp = array[j];
				temp = temp.trim();
				List tempList = Freebase.getList(temp);
				list.addAll(tempList);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// clean the list for duplicates - can come from multiple keyword
		// lookups
		List finalList = new ArrayList();
		for (int k = 0; k < list.size(); k++) {
			String val = (String) list.get(k);
			if (!finalList.contains(val)) {
				finalList.add(val);
			}
		}
		return finalList;
	}

	private String getCategory(String category) {
		return TemporaryCategoryUtils.getCategoryStr(category);
	}

	private String clean(String entry) {
		entry = DataClean.clean(entry);
		return entry;
	}

	private List rankedKeywordsParse(String entry) {
		List result = new ArrayList();
		try {
			result = RankedKeywordParse.parseRankedKeywords(entry);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	private RelationsParseVO relationsParse(String entry) {
		RelationsParseVO rvo = new RelationsParseVO();
		try {
			rvo = RelationsParse.parseRelations(entry);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rvo;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		FirstProcessHelperOld fph = new FirstProcessHelperOld();
		System.out.println(fph.coreparseCheck("I would be Pune"));
	}

}
