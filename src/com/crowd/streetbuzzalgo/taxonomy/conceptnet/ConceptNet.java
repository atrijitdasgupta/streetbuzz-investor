/**
 * 
 */
package com.crowd.streetbuzzalgo.taxonomy.conceptnet;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.crowd.streetbuzzalgo.utils.StrUtil;
import com.jaunt.Element;
import com.jaunt.Elements;
import com.jaunt.UserAgent;

/**
 * @author Atrijit
 * 
 */
public class ConceptNet {

	/**
	 * 
	 */
	public ConceptNet() {
		// TODO Auto-generated constructor stub
	}

	public static List conceptNet(List list) throws Exception {
		List finalList = new ArrayList();
		for (int i = 0; i < list.size(); i++) {
			String topic = (String) list.get(i);
			List conlist = conceptNet(topic);
			finalList.addAll(conlist);
		}
		return finalList;
	}

	private static List conceptNet(String topic) throws Exception {
		topic = topic.toLowerCase();
		topic = StrUtil.prepareforSentinet(topic);
		//System.out.println("post prepn: "+topic);
		UserAgent userAgent = new UserAgent();
		String url = "http://conceptnet5.media.mit.edu/web/c/en/" + topic;
		try {
			userAgent.visit(url);
		} catch (Exception e1) {
			System.out.println(e1.getMessage());
		}
		Element divspan = null;
		try {
			divspan = userAgent.doc.findEach("<div class=span6>");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		Elements relation = null;
		Elements concept = null;
		Elements context = null;
		if (divspan != null) {
			relation = divspan.findEach("<span class=relation>");
			concept = divspan.findEach("<span class=concept>");
		}
		List relationlist = new ArrayList();
		List conceptlist = new ArrayList();
		if (relation != null) {
			relationlist = relation.toList();
		}
		if (concept != null) {
			conceptlist = concept.toList();
		}
		//System.out.println(relationlist.size() + "::" + conceptlist.size());
		List countlist = new ArrayList();
		for (int i = 0; i < relationlist.size(); i++) {
			Element relate = (Element) relationlist.get(i);

			String seek = relate.innerText();
			seek = seek.trim();

			if ("IsA".equalsIgnoreCase(seek)
					|| "HasContext".equalsIgnoreCase(seek)) {
				/*if ("IsA".equalsIgnoreCase(seek)) {
					System.out.println("IsA match");
				}
				if ("HasContext".equalsIgnoreCase(seek)) {
					System.out.println("HasContext match");
				}
*/
				countlist.add(new Integer(i));
			}

		}
		List concountlist = new ArrayList();

		for (int i = 0; i < countlist.size(); i++) {
			Integer intc = (Integer) countlist.get(i);
			int intcint = intc.intValue();
			int counter = 2 * intcint + 1;
			concountlist.add(new Integer(counter));
		}
		List selectedconceptlist = new ArrayList();
		for (int i = 0; i < concountlist.size(); i++) {
			Integer counter = (Integer) concountlist.get(i);
			Element conc = (Element) conceptlist.get(counter.intValue());
			String concstr = conc.innerText();
			concstr = concstr.trim();
			//System.out.println((i+1) + ". topic: "+topic+" concept: " + concstr);
			selectedconceptlist.add(concstr);

		}
		try {
			userAgent.close();
		} catch (IOException e) {
			
		}
		return selectedconceptlist;

	}

	public static void conceptNetOld(String topic) throws Exception {
		topic = topic.toLowerCase();
		try {
			topic = URLEncoder.encode(topic, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String url = "http://conceptnet5.media.mit.edu/data/5.3/c/en/" + topic;
		System.out.println(url);
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
			// System.out.println(sbfr.toString());
		}
		httpclient.close();
		response.close();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			conceptNet("pizza");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
