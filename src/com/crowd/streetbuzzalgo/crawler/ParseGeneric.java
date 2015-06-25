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
 * @author Atrijit
 * 
 */
public class ParseGeneric {

	/**
	 * 
	 */
	public ParseGeneric() {
		// TODO Auto-generated constructor stub
	}

	public static List getComments(String url, String mode)throws Exception {
		UserAgent userAgent = new UserAgent();
		List retList = new ArrayList();
		Element div;
		Elements comments;
		List commentsList = new ArrayList();
		Elements commenters;
		List commentersList = new ArrayList();
		int comsize=0;
		int comntrsize=0;
		try {
			userAgent.visit(url);
//			System.out.println(userAgent.doc.innerHTML()); 
			/*String content = userAgent.doc.innerHTML();
			CreateFile.write(content, "generic");*/
			
			//Generic: http://tamaratattles.com/2014/10/16/real-housewives-of-melbourne-part-2/
			
			
			
			try {
				div = userAgent.doc.findFirst("<div id=comments>");
				comments = div.findEach("<div class=comment-body><p>");
				commentsList = comments.toList();
				commenters = div.findEach("<cite class=fn>");
				commentersList = commenters.toList();
				comsize = commentsList.size();
				comntrsize = commentersList.size();
				for (int i=0;i<comsize;i++){
					Element comnt = (Element)commentsList.get(i);
					Element comntr = (Element)commentersList.get(i);
					
					String name = StrUtil.nonNull(comntr.getText());
					if("".equalsIgnoreCase(name)){
						name = "Unknown";
					}
					System.out.println(name+"<==>"+comnt.innerText());
				}
			} catch (Exception e) {
				
				
			}
			
			//Generic: Flipkart review
			try {
			div = userAgent.doc.findFirst("<div class=recentReviews>");
			comments = div.findEach("<span class=review-text>");
			commenters = div.findEach("<p class=review-userName>");
			commentsList = comments.toList();
			commentersList = commenters.toList();
			comsize = commentsList.size();
			comntrsize = commentersList.size();
			
			for (int j=0;j<comsize;j++){
				Element comnt = (Element)commentsList.get(j);
				Element comntr = (Element)commentersList.get(j);
				String name = StrUtil.nonNull(comntr.getText());
				if("".equalsIgnoreCase(name)){
					name = "Unknown";
				}
				System.out.println(name+"<==>"+comnt.innerText());
			}
			} catch (Exception e) {
								
			}
			
			//GSMArena review
			try {
				div = userAgent.doc.findFirst("<body>");
				comments = div.findEach("<p class=uopin>");
				commenters = div.findEach("<li class=uname2>");
				commentsList = comments.toList();
				commentersList = commenters.toList();
				comsize = commentsList.size();
				comntrsize = commentersList.size();
				
				for (int j=0;j<comsize;j++){
					Element comnt = (Element)commentsList.get(j);
					Element comntr = (Element)commentersList.get(j);
					String name = StrUtil.nonNull(comntr.getText());
					if("".equalsIgnoreCase(name)){
						name = "Unknown";
					}
					System.out.println(name+"<==>"+comnt.innerText());
				}
				} catch (Exception e) {
									
				}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for (int i=0;i<comsize;i++){
			Element comnt = (Element)commentsList.get(i);
			Element comntr = (Element)commentersList.get(i);
			String comment = StrUtil.nonNull(comnt.innerText());
			String name = StrUtil.nonNull(comntr.getText());
			if("".equalsIgnoreCase(name)){
				name = "Unknown";
			}
			CrawlerVO cvo = new CrawlerVO();
			cvo.setComment(comment);
			cvo.setUsername(name);
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
