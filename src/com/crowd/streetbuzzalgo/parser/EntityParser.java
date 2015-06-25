/**
 * 
 */
package com.crowd.streetbuzzalgo.parser;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.crowd.streetbuzz.common.Constants;
import com.crowd.streetbuzz.helper.ParserResourceHelper;
import com.crowd.streetbuzzalgo.parser.vo.StanfordNerVO;
import com.crowd.streetbuzzalgo.utils.StrUtil;

import edu.stanford.nlp.dcoref.CorefCoreAnnotations.CorefGraphAnnotation;
import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.IntTuple;
import edu.stanford.nlp.util.Pair;

/**
 * @author Atrijit
 * 
 */
public class EntityParser implements Constants {

	/**
	 * 
	 */
	public EntityParser() {

	}

	private void smartparse(String inputLine) throws Exception {
		Properties props = new Properties();
		props.put("annotators", "tokenize, ssplit, pos, lemma, parse, dcoref");
		StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
		Annotation document = new Annotation(inputLine);
		pipeline.annotate(document);
		/*
		 * List<CoreMap> sentences = (List<CoreMap>)
		 * document.get(SentencesAnnotation.class); for(CoreMap sentence:
		 * sentences) { for (CoreLabel token:
		 * sentence.get(TokensAnnotation.class)) { } }
		 */
		List<Pair<IntTuple, IntTuple>> graph = document
				.get(CorefGraphAnnotation.class);
		System.out.println(graph);

	}

	private void parse(String inputLine) throws Exception {
		String serializedClassifier = BASEEXTLIBPATH
				+ "stanfordcorelib/english.all.3class.distsim.crf.ser.gz";
		AbstractSequenceClassifier<CoreLabel> classifier = CRFClassifier
				.getClassifierNoExceptions(serializedClassifier);
		File xmlfile = new File("C:/Mywork/StreetBuzz/output.xml");

		PrintWriter writer = new PrintWriter(xmlfile);
		writer.println("<Sentences>");
		writer.flush();
		String output = "<Sentence>"
				+ classifier.classifyToString(inputLine, "xml", true)
				+ "</Sentence>";
		writer.println(output);
		writer.flush();
		writer.println("</Sentences>");
		writer.flush();
	}

	private static String coreparseStr(String inputLine) throws Exception {
		String serializedClassifier = BASEEXTLIBPATH
				+ "stanfordcorelib/english.all.3class.distsim.crf.ser.gz";
		AbstractSequenceClassifier<CoreLabel> classifier = CRFClassifier
				.getClassifierNoExceptions(serializedClassifier);
		//AbstractSequenceClassifier<CoreLabel> classifier = getClassifier();
		StringBuffer sbfr = new StringBuffer();
		sbfr.append("<Sentences>");
		sbfr.append("<Sentence>"
				+ classifier.classifyToString(inputLine, "xml", true)
				+ "</Sentence>");
		sbfr.append("</Sentences>");

		return sbfr.toString();
	}
	
	private static String coreparseStrAlt(String inputLine,AbstractSequenceClassifier<CoreLabel> classifier) throws Exception {
		StringBuffer sbfr = new StringBuffer();
		sbfr.append("<Sentences>");
		sbfr.append("<Sentence>"
				+ classifier.classifyToString(inputLine, "xml", true)
				+ "</Sentence>");
		sbfr.append("</Sentences>");

		return sbfr.toString();
	}

	public static StanfordNerVO coreparse(String inputLine) throws Exception {
		String xml = coreparseStr(inputLine);
		StanfordNerVO snvo = parseCoreXml(xml);
		return snvo;
	}
	
	public static StanfordNerVO coreparseAlt(String inputLine, AbstractSequenceClassifier<CoreLabel> classifier) throws Exception {
		String xml = coreparseStrAlt(inputLine,classifier);
		StanfordNerVO snvo = new StanfordNerVO();
		try {
			snvo = parseCoreXml(xml);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return snvo;
	}
	
	

	public static boolean coreparseCheck(String inputLine,
			AbstractSequenceClassifier<CoreLabel> classifier) throws Exception {
		boolean retBool = true;
		String temp = classifier.classifyToString(inputLine, "xml", true);
		// System.out.println(temp);
		temp = temp.toUpperCase();
		if (temp.indexOf("LOCATION") > -1) {
			retBool = false;
		} else if (temp.indexOf("ORGANIZATION") > -1) {
			retBool = false;
		}else if (temp.indexOf("O") > -1) {
			retBool = false;
		}
		// System.out.println(retBool);
		return retBool;
	}

	private static boolean isOneOf(String key) {
		final List aList = new ArrayList();
		aList.add(LOCATION);
		aList.add(TIME);
		aList.add(PERSON);
		aList.add(ORGANIZATION);
		aList.add(MONEY);
		aList.add(PERCENT);
		aList.add(DATE);
		if (aList.contains(key)) {
			return true;
		}
		return false;
	}
	
	private static AbstractSequenceClassifier getClassifier(){
		ParserResourceHelper prh = ParserResourceHelper.getInstance();
		Map resourcesMap = prh.getResourcesMap();
		AbstractSequenceClassifier<CoreLabel> classifier =  (AbstractSequenceClassifier)resourcesMap.get("classifier");
		return classifier;
	}

	private static StanfordNerVO parseCoreXml(String xmlStr) throws Exception {
		//System.out.println(xmlStr);
		HashMap hMap = new HashMap();
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		InputSource is = new InputSource(new StringReader(xmlStr));
		Document document = builder.parse(is);
		NodeList nList = document.getElementsByTagName("wi");
		for (int i = 0; i < nList.getLength(); i++) {
			NamedNodeMap attributes = nList.item(i).getAttributes();
			Node node = nList.item(i);
			for (int a = 0; a < attributes.getLength(); a++) {
				Node theAttribute = attributes.item(a);
				String attrib = StrUtil.nonNull(theAttribute.getNodeValue());
				String value = StrUtil.nonNull(node.getTextContent());
				// System.out.println(i + ":" + name + ":" + attrib);
				if (isOneOf(attrib)) {
					String iStr = new Integer(i).toString();
					//System.out.println(attrib + "#" + value);
					hMap.put(iStr, (attrib + "#" + value));
				}

			}
		}

		StanfordNerVO snvo = deCode(hMap);
		// snvo.setEntry(inputLine);
		List location = snvo.getLocation();
		List person = snvo.getPerson();
		List org = snvo.getOrganization();
	//	System.out.println(location.toString());
//		System.out.println(person.toString());
//		System.out.println(org.toString());
		snvo.setParsexml(xmlStr);
		return snvo;
	}

	private static StanfordNerVO deCode(HashMap hMap) {
		StanfordNerVO snvo = new StanfordNerVO();
		Set keySet = hMap.keySet();
		Iterator it = keySet.iterator();
		int num = 0;
		StringBuffer sbfr = new StringBuffer();

		List location = new ArrayList();
		List person = new ArrayList();
		List organization = new ArrayList();

		while (it.hasNext()) {
			String key = (String) it.next();
			int i = new Integer(key).intValue();
			String value = (String) hMap.get(key);
			// System.out.println(key+" : "+value);
			String[] nvArr = value.split("#");
			String entity = nvArr[0];
			String text = nvArr[1];
			// System.out.println(i+" : "+entity+"##"+text);
			//System.out.println("i:" + i + ", num:" + num);
			int diff = i - num;
			System.out.println(diff);

			sbfr.append(text + " ");

			if (LOCATION.equalsIgnoreCase(entity)) {
				location.add(sbfr.toString());

				sbfr = new StringBuffer();
			} else if (PERSON.equalsIgnoreCase(entity)) {
				person.add(sbfr.toString());
				sbfr = new StringBuffer();
			} else if (ORGANIZATION.equalsIgnoreCase(entity)) {
				organization.add(sbfr.toString());
				sbfr = new StringBuffer();
			}

			num = new Integer(key).intValue();
		}
		snvo.setLocation(location);
		snvo.setOrganization(organization);
		snvo.setPerson(person);
		return snvo;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// EntityParser EP = new EntityParser();
		/*
		 * String serializedClassifier =
		 * BASEEXTLIBPATH+"stanfordcorelib/english.all.3class.distsim.crf.ser.gz";
		 * AbstractSequenceClassifier<CoreLabel> classifier =
		 * CRFClassifier.getClassifierNoExceptions(serializedClassifier);
		 */
		/*
		 * try { System.out.println(coreparse("That she lied was suspected in
		 * Pune by everyone")); } catch (Exception e) { // TODO Auto-generated
		 * catch block e.printStackTrace(); }
		 */
		String input = "Movies like it happened one Night.";

		try {
			String xmlStr = coreparseStr(input);
			parseCoreXml(xmlStr);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
