/**
 * 
 */
package com.crowd.streetbuzzalgo.crawler;

import java.util.ArrayList;
import java.util.List;

import com.crowd.streetbuzzalgo.crawler.vo.CrawlerVO;
import com.crowd.streetbuzzalgo.utils.StrUtil;
import com.jaunt.Element;
import com.jaunt.Elements;
import com.jaunt.UserAgent;

/**
 * @author vasu
 *
 */
public class ParseTheVerge {

	/**
	 * 
	 */
	public ParseTheVerge() {
		// TODO Auto-generated constructor stub
	}
	public static List getComments(String url, String mode)throws Exception{
		UserAgent userAgent = new UserAgent();
		userAgent.visit(url);
		Element div = userAgent.doc.findFirst("<div id=comments>");
		Elements comments = div.findEach("<div class=comment-body><p>");
		Elements commenters = div.findEach("<cite class=fn>");
		List commentsList = comments.toList();
		List commentersList = commenters.toList();
		int comsize = commentsList.size();
		List retList = new ArrayList();
		for (int i=0;i<comsize;i++){
			Element comnt = (Element)commentsList.get(i);
			Element comntr = (Element)commentersList.get(i);
			String comment = StrUtil.nonNull(comnt.innerText());
			System.out.println(comment);
			String name = StrUtil.nonNull(comntr.getText());
			if("".equalsIgnoreCase(name)){
				name = "Unknown";
			}
			CrawlerVO cvo = new CrawlerVO();
			cvo.setComment(comment);
			cvo.setUsername(name);
			cvo.setURLParsed(url);
			cvo.setMode(mode);
			retList.add(cvo);
		}
		return retList;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
