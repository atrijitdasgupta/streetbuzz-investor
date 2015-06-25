/**
 * 
 */
package com.crowd.streetbuzz.processhelperutils;

import java.util.ArrayList;
import java.util.List;

import com.crowd.streetbuzz.common.Constants;
import com.crowd.streetbuzz.stopword.Stopper;
import com.crowd.streetbuzzalgo.parser.EntityParser;
import com.crowd.streetbuzzalgo.parser.InvokeParser;
import com.crowd.streetbuzzalgo.parser.RankedKeywordParse;
import com.crowd.streetbuzzalgo.parser.RelationsParse;
import com.crowd.streetbuzzalgo.parser.vo.BasicDependencyVO;
import com.crowd.streetbuzzalgo.parser.vo.ObjectEntityVO;
import com.crowd.streetbuzzalgo.parser.vo.RelationsParseVO;
import com.crowd.streetbuzzalgo.parser.vo.StanfordNerVO;
import com.crowd.streetbuzzalgo.parser.vo.SubjectEntityVO;
import com.crowd.streetbuzzalgo.stopwords.StopWordRemoval;
import com.crowd.streetbuzzalgo.utils.StrUtil;

import edu.stanford.nlp.parser.lexparser.LexicalizedParser;

/**
 * @author Atrijit
 * 
 */
public class DemoWordProcess implements Constants {

	/**
	 * 
	 */
	public DemoWordProcess() {
		// TODO Auto-generated constructor stub
	}

	public static List processFB(String searchtopic) {
		searchtopic = ProcessHelperUtils.clean(searchtopic);
		searchtopic = ProcessHelperUtils.numberClean(searchtopic);

		StanfordNerVO snvo = getStanfordNamedEntities(searchtopic);
		List locationsList = snvo.getLocation();
		List personList = snvo.getPerson();
		List orgList = snvo.getOrganization();

		// Get relations tree
		RelationsParseVO rvo = relationsParse(searchtopic);

		List subjentities = rvo.getSubjectentities();
		List objentities = rvo.getObjectentities();

		// Get Verb
		String verb = getVerb(rvo);

		// The Stanford call to get relationships
		BasicDependencyVO bdvo = getBasicStanfordDependencies(searchtopic);

		String location = "";
		if (locationsList != null && locationsList.size() > 0) {
			location = (String) locationsList.get(0);
		}

		String person = "";
		if (personList != null && personList.size() > 0) {
			person = (String) personList.get(0);
		}

		String organization = "";
		if (orgList != null && orgList.size() > 0) {
			organization = (String) orgList.get(0);
		}

		List subj = rvo.getSubject();
		String subject = "";
		if (subj != null && subj.size() > 0) {
			subject = (String) subj.get(0);
		}
		if ("".equalsIgnoreCase(subject)) {
			if (subjentities != null && subjentities.size() > 0) {
				SubjectEntityVO subjvo = (SubjectEntityVO) subjentities.get(0);
				subject = subjvo.getText();
			}

		}

		String object = "";
		List obj = rvo.getObjecttext();
		if (obj != null && obj.size() > 0) {
			object = (String) obj.get(0);
		}
		if ("".equalsIgnoreCase(object)) {
			if (objentities != null && objentities.size() > 0) {
				ObjectEntityVO objvo = (ObjectEntityVO) objentities.get(0);
				object = objvo.getText();
			}
		}

		person = person.trim();
		organization = organization.trim();
		subject = subject.trim();
		verb = verb.trim();
		object = object.trim();
		location = location.trim();

		System.out.println("location=> " + location);
		System.out.println("person=> " + person);
		System.out.println("organization=> " + organization);
		System.out.println("subject=> " + subject);
		System.out.println("object=> " + object);

		List finalList = new ArrayList();
		List stoppedList = Stopper.stop(searchtopic);
		for (int i = 0; i < stoppedList.size(); i++) {
			String temp = (String) stoppedList.get(i);
			if (!temp.equalsIgnoreCase(location)) {
				finalList.add(temp);
			}
		}
		if (!"".equalsIgnoreCase(person)) {
			if (!finalList.contains(person)) {
				finalList.add(person);
			}
		}
		if (!"".equalsIgnoreCase(organization)) {
			if (!finalList.contains(organization)) {
				finalList.add(organization);
			}
		}
		if (!"".equalsIgnoreCase(subject)) {
			if (!finalList.contains(subject)) {
				finalList.add(subject);
			}
		}
		if (!"".equalsIgnoreCase(object)) {
			if (!finalList.contains(object)) {
				finalList.add(object);
			}
		}
		
	
		return finalList;

	}
	public static List process(String searchtopic) {
//		 First clean the String
		searchtopic = ProcessHelperUtils.clean(searchtopic);
		List list = new ArrayList();
		list.add(searchtopic);
		return list;
	}

	public static List processOld(String searchtopic) {
		// First clean the String
		searchtopic = ProcessHelperUtils.clean(searchtopic);

		// Get Ranked Keywords
		// List rklist = rankedKeywordsParse(searchtopic);
		// System.out.println("rklist size:: " + rklist.size());
		/*
		 * for (int i = 0; i < rklist.size(); i++) { RankedKeywordVO rkvo =
		 * (RankedKeywordVO) rklist.get(i); String text = rkvo.getText(); String
		 * relevance = rkvo.getRelevance(); System.out.println(text + "::" +
		 * relevance); }
		 */

		// The Stanford entity parse call to go here
		StanfordNerVO snvo = getStanfordNamedEntities(searchtopic);
		List locationsList = snvo.getLocation();
		List personList = snvo.getPerson();
		List orgList = snvo.getOrganization();
		/*
		 * for (int i = 0; i < personList.size(); i++) { String person =
		 * (String) personList.get(i); System.out.println("person: " + person); }
		 * for (int i = 0; i < locationsList.size(); i++) { String loc =
		 * (String) locationsList.get(i); System.out.println("location: " +
		 * loc); } for (int i = 0; i < orgList.size(); i++) { String org =
		 * (String) orgList.get(i); System.out.println("org: " + org); }
		 */

		// Get relations tree
		RelationsParseVO rvo = relationsParse(searchtopic);
		/*
		 * System.out.println("Objecttext::" + rvo.getObjecttext() + "Subject::" +
		 * rvo.getSubject() + "Subjectactiontext::" + rvo.getSubjectactiontext() +
		 * "Subjectverbtext::" + rvo.getSubjectverbtext());
		 */
		// List locentities = rvo.getLocationentities();
		List subjentities = rvo.getSubjectentities();
		List objentities = rvo.getObjectentities();
		/*
		 * for (int i=0;i<locentities.size();i++){ LocationEntityVO loc =
		 * (LocationEntityVO)locentities.get(i); System.out.println("loc::
		 * "+loc.getPlace()); } for (int i=0;i<subjentities.size();i++){
		 * SubjectEntityVO subj = (SubjectEntityVO)subjentities.get(i);
		 * System.out.println("subj:: "+subj.getText()); } for (int i=0;i<objentities.size();i++){
		 * ObjectEntityVO obj = (ObjectEntityVO)objentities.get(i);
		 * System.out.println("obj:: "+obj.getText()); }
		 */

		// Get Verb
		String verb = getVerb(rvo);
		System.out.println("verb:: " + verb);

		// The Stanford call to get relationships
		BasicDependencyVO bdvo = getBasicStanfordDependencies(searchtopic);

		System.out.println("*******************************************");

		String location = "";
		if (locationsList != null && locationsList.size() > 0) {
			location = (String) locationsList.get(0);
		}
		System.out.println("location=> " + location);

		String person = "";
		if (personList != null && personList.size() > 0) {
			person = (String) personList.get(0);
		}
		System.out.println("person=> " + person);

		String organization = "";
		if (orgList != null && orgList.size() > 0) {
			organization = (String) orgList.get(0);
		}
		System.out.println("organization=> " + organization);

		List subj = rvo.getSubject();
		String subject = "";
		if (subj != null && subj.size() > 0) {
			subject = (String) subj.get(0);
		}
		if ("".equalsIgnoreCase(subject)) {
			if (subjentities != null && subjentities.size() > 0) {
				SubjectEntityVO subjvo = (SubjectEntityVO) subjentities.get(0);
				subject = subjvo.getText();
			}

		}

		System.out.println("subject=> " + subject);

		String object = "";
		List obj = rvo.getObjecttext();
		if (obj != null && obj.size() > 0) {
			object = (String) obj.get(0);
		}
		if ("".equalsIgnoreCase(object)) {
			if (objentities != null && objentities.size() > 0) {
				ObjectEntityVO objvo = (ObjectEntityVO) objentities.get(0);
				object = objvo.getText();
			}
		}
		System.out.println("object=> " + object);
		List tempList = new ArrayList();
		person = person.trim();
		organization = organization.trim();
		subject = subject.trim();
		verb = verb.trim();
		object = object.trim();
		location = location.trim();

		if (!tempList.contains(person)) {
			tempList.add(person);
		}
		if (!tempList.contains(organization)) {
			if (subject.indexOf(organization) < 0) {
				tempList.add(organization);
			}
		}
		if (!tempList.contains(subject)) {
			tempList.add(subject);
		}
		if (!tempList.contains(verb)) {
			tempList.add(verb);
		}
		if (!tempList.contains(object)) {
			tempList.add(object);
		}
		if (!tempList.contains(location)) {
			tempList.add(location);
		}
		StringBuffer sbfr = new StringBuffer();
		for (int i = 0; i < tempList.size(); i++) {
			String ptemp = (String) tempList.get(i);
			sbfr.append(ptemp + " ");
		}

		String temp = sbfr.toString().trim();
		if (temp.equalsIgnoreCase(location)) {
			temp = "";
		}
		if (temp.equalsIgnoreCase(subject)) {
			temp = "";
		}
		if (temp.equalsIgnoreCase(verb)) {
			temp = "";
		}
		if (temp.equalsIgnoreCase(organization)) {
			temp = "";
		}

		System.out.println("temp:: " + temp);
		List list = new ArrayList();
		if (!"".equalsIgnoreCase(temp)) {
			list.add(temp);

		}

		String numsearchtopic = ProcessHelperUtils.numberClean(searchtopic);
		if (!numsearchtopic.equalsIgnoreCase(searchtopic)) {
			list.add(numsearchtopic);
		}
		// Add the original cleaned up String
		list.add(searchtopic);
		return list;

	}

	private static String removeNumbers(String searchtopic) {
		searchtopic = ProcessHelperUtils.numberClean(searchtopic);
		return searchtopic;
	}

	private static String getRoot(String text) {
		StopWordRemoval swr = new StopWordRemoval();
		String temp = swr.removeStopwords(text);
		System.out.println("root:: " + temp);
		return temp;
	}

	public static String getVerb(RelationsParseVO rvo) {
		List subjectverbtextlist = rvo.getSubjectverbtext();
		List subjectverbactionlist = rvo.getSubjectactiontext();
		String verb = "";
		if (subjectverbtextlist != null && subjectverbtextlist.size() > 0) {
			verb = StrUtil.nonNull((String) subjectverbtextlist.get(0));
		}
		if (!"".equalsIgnoreCase(verb)) {
			return verb;
		} else if (subjectverbactionlist != null
				&& subjectverbactionlist.size() > 0) {
			verb = StrUtil.nonNull((String) subjectverbactionlist.get(0));
			return verb;
		} else {
			return verb;
		}
	}

	public static RelationsParseVO relationsParse(String entry) {
		RelationsParseVO rvo = new RelationsParseVO();
		try {
			rvo = RelationsParse.parseRelations(entry);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rvo;
	}

	private static BasicDependencyVO getBasicStanfordDependencies(String entry) {
		String grammar = BASEEXTLIBPATH + "stanfordnlp/englishPCFG.ser.gz";
		String[] options = { "-maxLength", "80", "-retainTmpSubcategories" };
		LexicalizedParser lp = LexicalizedParser.loadModel(grammar, options);
		BasicDependencyVO bdvo = InvokeParser.runMorefromSentence(lp, entry);
		return bdvo;
	}

	private static StanfordNerVO getStanfordNamedEntities(String entry) {
		StanfordNerVO snvo = new StanfordNerVO();
		try {
			snvo = EntityParser.coreparse(entry);
		} catch (Exception e) {

			e.printStackTrace();
		}
		return snvo;
	}

	private static List rankedKeywordsParse(String entry) {
		List result = new ArrayList();
		try {
			result = RankedKeywordParse.parseRankedKeywords(entry);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// process("Delhi Elections 2015");
		List list = processFB("Would BJP or AAP win in Delhi");
		for (int i = 0; i < list.size(); i++) {
			String temp = (String) list.get(i);
			System.out.println(temp);
		}

	}

}
