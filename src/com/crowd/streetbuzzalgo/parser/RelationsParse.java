/**
 * 
 */
package com.crowd.streetbuzzalgo.parser;

import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.crowd.streetbuzzalgo.constants.SystemConstants;
import com.crowd.streetbuzzalgo.parser.vo.LocationEntityVO;
import com.crowd.streetbuzzalgo.parser.vo.ObjectEntityVO;
import com.crowd.streetbuzzalgo.parser.vo.RelationsParseVO;
import com.crowd.streetbuzzalgo.parser.vo.SubjectEntityVO;

/**
 * @author Atrijit
 * 
 */
public class RelationsParse implements SystemConstants {

	/**
	 * 
	 */
	public RelationsParse() {
		// TODO Auto-generated constructor stub
	}

	public static RelationsParseVO parseRelations(String phrase)
			throws Exception {
		phrase = URLEncoder.encode(phrase, "UTF-8");
		StringBuffer params = new StringBuffer();
		params.append("?apikey=" + RPSERVICEKEY);
		params.append("&text=" + phrase);
		params.append("&knowledgeGraph=1");
		params.append("&entities=1");
		String url = RPRELATIONSURL + params.toString();
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpget = new HttpGet(url);
		CloseableHttpResponse response = httpclient.execute(httpget);
		HttpEntity entity = response.getEntity();
		StringBuffer sbfr = new StringBuffer();
		if (entity != null) {
			Reader reader = new InputStreamReader(entity.getContent());
			int i;
			char c;

			while ((i = reader.read()) != -1) {
				// int to character
				c = (char) i;
				sbfr.append(c);
				// print char
				// System.out.println("Character Read: "+c);
			}
			System.out.println(sbfr.toString());
		}
		httpclient.close();
		response.close();
		RelationsParseVO evo = parseXML(sbfr.toString());
		return evo;
	}

	private static RelationsParseVO parseXML(String temp)
			throws Exception {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		InputSource is = new InputSource(new StringReader(temp));
		Document document = builder.parse(is);
		List objectentitylist = new ArrayList();
		List subjectentitylist = new ArrayList();
		List locationentitylist = new ArrayList();
		List entitylist = new ArrayList();
		List typelist = new ArrayList();
		// extra for locations
		List loctextlist = new ArrayList();
		List subjectlist = new ArrayList();
		List subjectactiontextlist = new ArrayList();
		List subjectverbtextlist = new ArrayList();
		List objecttextlist = new ArrayList();

		RelationsParseVO rvo = new RelationsParseVO();
		String expression = "/results/relations/relation/subject/text";
		//System.out.println(expression);
		XPath xPath = XPathFactory.newInstance().newXPath();
		NodeList nodeList = (NodeList) xPath.compile(expression).evaluate(
				document, XPathConstants.NODESET);
		for (int i = 0; i < nodeList.getLength(); i++) {
			String subject = nodeList.item(i).getFirstChild().getNodeValue();
			//System.out.println("subject::" + subject);
			subjectlist.add(subject);

		}
		rvo.setSubject(subjectlist);

		expression = "/results/relations/relation/action/text";
		nodeList = (NodeList) xPath.compile(expression).evaluate(document,
				XPathConstants.NODESET);
		for (int i = 0; i < nodeList.getLength(); i++) {
			String subjectactiontext = nodeList.item(i).getFirstChild()
					.getNodeValue();
			//System.out.println("subjectactiontext::" + subjectactiontext);
			subjectactiontextlist.add(subjectactiontext);

		}
		rvo.setSubjectactiontext(subjectactiontextlist);

		expression = "/results/relations/relation/action/verb/text";
		nodeList = (NodeList) xPath.compile(expression).evaluate(document,
				XPathConstants.NODESET);
		for (int i = 0; i < nodeList.getLength(); i++) {
			String subjectverbtext = nodeList.item(i).getFirstChild()
					.getNodeValue();
		//	System.out.println("subjectverbtext::" + subjectverbtext);
			rvo.setSubjectverbtext(subjectverbtextlist);
		}
		rvo.setSubjectverbtext(subjectverbtextlist);

		expression = "/results/relations/relation/object/text";
		nodeList = (NodeList) xPath.compile(expression).evaluate(document,
				XPathConstants.NODESET);
		for (int i = 0; i < nodeList.getLength(); i++) {
			String objecttext = nodeList.item(i).getFirstChild().getNodeValue();
		//	System.out.println("objecttext::" + objecttext);
			objecttextlist.add(objecttext);

		}
		rvo.setObjecttext(objecttextlist);

		expression = "/results/relations/relation/object/entities/entity/text";
		nodeList = (NodeList) xPath.compile(expression).evaluate(document,
				XPathConstants.NODESET);
		for (int i = 0; i < nodeList.getLength(); i++) {
			String entitytext = nodeList.item(i).getFirstChild().getNodeValue();
		//	System.out.println("object entitytext::" + entitytext);
			entitylist.add(entitytext);

		}

		expression = "/results/relations/relation/object/entities/entity/type";
		nodeList = (NodeList) xPath.compile(expression).evaluate(document,
				XPathConstants.NODESET);
		for (int i = 0; i < nodeList.getLength(); i++) {
			String entitytype = nodeList.item(i).getFirstChild().getNodeValue();
		//	System.out.println("object entitytype::" + entitytype);
			typelist.add(entitytype);

		}
		for (int i = 0; i < entitylist.size(); i++) {
			String entitytext = (String) entitylist.get(i);
			String entitytype = (String) typelist.get(i);
			ObjectEntityVO oevo = new ObjectEntityVO();
			oevo.setText(entitytext);
			oevo.setType(entitytype);
			objectentitylist.add(oevo);
		}
		rvo.setObjectentities(objectentitylist);
		entitylist.clear();
		typelist.clear();

		expression = "/results/relations/relation/subject/entities/entity/text";
		nodeList = (NodeList) xPath.compile(expression).evaluate(document,
				XPathConstants.NODESET);
		for (int i = 0; i < nodeList.getLength(); i++) {
			String entitytext = nodeList.item(i).getFirstChild().getNodeValue();
		//	System.out.println("subject entitytext::" + entitytext);
			entitylist.add(entitytext);

		}
		expression = "/results/relations/relation/subject/entities/entity/type";
		nodeList = (NodeList) xPath.compile(expression).evaluate(document,
				XPathConstants.NODESET);
		for (int i = 0; i < nodeList.getLength(); i++) {
			String entitytype = nodeList.item(i).getFirstChild().getNodeValue();
		//	System.out.println("subject entitytype::" + entitytype);
			typelist.add(entitytype);

		}
		for (int i = 0; i < entitylist.size(); i++) {
			String entitytext = (String) entitylist.get(i);
			String entitytype = (String) typelist.get(i);
			SubjectEntityVO sevo = new SubjectEntityVO();
			sevo.setText(entitytext);
			sevo.setType(entitytype);
			subjectentitylist.add(sevo);
		}
		rvo.setSubjectentities(subjectentitylist);
		entitylist.clear();
		typelist.clear();

		expression = "/results/relations/relation/location/text";
		nodeList = (NodeList) xPath.compile(expression).evaluate(document,
				XPathConstants.NODESET);
		for (int i = 0; i < nodeList.getLength(); i++) {
			String loctext = nodeList.item(i).getFirstChild().getNodeValue();
		//	System.out.println("loctext::" + loctext);
			loctextlist.add(loctext);

		}
		expression = "/results/relations/relation/location/entities/entity/text";
		nodeList = (NodeList) xPath.compile(expression).evaluate(document,
				XPathConstants.NODESET);
		for (int i = 0; i < nodeList.getLength(); i++) {
			String locenttext = nodeList.item(i).getFirstChild().getNodeValue();
		//	System.out.println("locenttext::" + locenttext);
			entitylist.add(locenttext);

		}

		expression = "/results/relations/relation/location/entities/entity/type";
		nodeList = (NodeList) xPath.compile(expression).evaluate(document,
				XPathConstants.NODESET);
		for (int i = 0; i < nodeList.getLength(); i++) {
			String locenttype = nodeList.item(i).getFirstChild().getNodeValue();
		//	System.out.println("locenttype::" + locenttype);
			typelist.add(locenttype);

		}
		for (int k = 0; k < loctextlist.size(); k++) {
			LocationEntityVO levo = new LocationEntityVO();
			String loctext = (String) loctextlist.get(k);
			String locenttext = (String) entitylist.get(k);
			String locenttype = (String) typelist.get(k);
			levo.setText(loctext);
			levo.setEtext(locenttext);
			levo.setEtype(locenttype);
			locationentitylist.add(levo);
		}
		rvo.setLocationentities(locationentitylist);
		return rvo;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		try {
			String input = "Movies like it happened one Night";
			RelationsParseVO evo = parseRelations(input);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
