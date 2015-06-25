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
import java.util.Properties;

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
import com.crowd.streetbuzzalgo.parser.vo.RankedKeywordVO;

import edu.stanford.nlp.pipeline.StanfordCoreNLP;

/**
 * @author Atrijit
 *
 */
public class RankedKeywordParse implements SystemConstants{

	/**
	 * 
	 */
	public RankedKeywordParse() {
		// TODO Auto-generated constructor stub
	}
	
	public static List parseRankedKeywords(String phrase)throws Exception{
		phrase = URLEncoder.encode(phrase, "UTF-8");
		StringBuffer params = new StringBuffer();
		params.append("?apikey="+RPSERVICEKEY);
		params.append("&text="+phrase);
		String url = RPRANKEDKEYWORDSURL + params.toString();
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
			//System.out.println(sbfr.toString());
		}
		httpclient.close();
		response.close();
		List rkvolist = parseXML(sbfr.toString());
		return rkvolist;
	}
	private static List parseXML(String temp)throws Exception{
		List rankList = new ArrayList();
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		InputSource is = new InputSource(new StringReader(temp));
		Document document = builder.parse(is);
		List textlist = new ArrayList();
		List relevancelist = new ArrayList();
		String expression = "/results/keywords/keyword/text";
		//System.out.println(expression);
		XPath xPath =  XPathFactory.newInstance().newXPath();
		NodeList nodeList = (NodeList) xPath.compile(expression).evaluate(document, XPathConstants.NODESET);
		for (int i = 0; i < nodeList.getLength(); i++) {
			String text = nodeList.item(i).getFirstChild().getNodeValue();
			//System.out.println("text::"+text);
			textlist.add(text);
		}
		
		expression = "/results/keywords/keyword/relevance";
	//	System.out.println(expression);
		nodeList = (NodeList) xPath.compile(expression).evaluate(document, XPathConstants.NODESET);
		for (int i = 0; i < nodeList.getLength(); i++) {
			String relevance = nodeList.item(i).getFirstChild().getNodeValue();
			//System.out.println("relevance::"+relevance);
			relevancelist.add(relevance);
		}
		for(int j=0;j<textlist.size();j++){
			RankedKeywordVO rkvo = new RankedKeywordVO();
			String text = (String)textlist.get(j);
			String relevance = (String)relevancelist.get(j);
			rkvo.setRelevance(relevance);
			rkvo.setText(text);
			rankList.add(rkvo);
		}
		
		
		return rankList;
	}
	

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		/*Properties props = new Properties();
		props.setProperty("annotators", "tokenize, ssplit, parse, sentiment");
		StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
		TweetWithSentiments tws = new TweetWithSentiments();*/
		String input = "Other movies like It happened One Night";
		try {
			List list = parseRankedKeywords(input);
			if(list!=null && list.size()>0){
				for(int i=0;i<list.size();i++){
					RankedKeywordVO rkvo = (RankedKeywordVO)list.get(i);
					String phrase = rkvo.getText();
					String relevance = rkvo.getRelevance();
					System.out.println(phrase+" "+relevance);
					//tws.analyseSentiment(input,pipeline);
				}
				
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
