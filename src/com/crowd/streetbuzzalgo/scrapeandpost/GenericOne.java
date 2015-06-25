/**
 * 
 */
package com.crowd.streetbuzzalgo.scrapeandpost;

import java.io.IOException;
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
public class GenericOne {

	/**
	 * 
	 */
	public GenericOne() {
		// TODO Auto-generated constructor stub
	}

	public static List scrape(String url) throws Exception {
		List retList = new ArrayList();
		UserAgent userAgent = new UserAgent();
		try {
			userAgent.visit(url);
		} catch (Exception e2) {
			// TODO Auto-generated catch block
			System.out.println("GenericOne JAUNT parse exception: "+e2.getMessage());
		}
		Element div = null;
		try {
			div = userAgent.doc.findFirst("<div id=comments>");
		} catch (Exception e2) {
			// TODO Auto-generated catch block
			System.out.println("GenericOne JAUNT parse exception: "+e2.getMessage());
		}
		Elements comments = null;
		Elements article = null;
		if(div!=null){
			try {
				comments = div.findEach("<div class=comment-content>");
				article = div.findEach("<article id>");
			} catch (RuntimeException e) {
				// TODO Auto-generated catch block
				System.out.println("GenericOne JAUNT parse exception: "+e.getMessage());
			}
		}
		List commentsList = new ArrayList();
		List articlelist = new ArrayList();
		if(comments!=null){
			commentsList = comments.toList();
		}
		if(article!=null){
			articlelist = article.toList();
		}
		
	//	System.out.println("article.size()::"+article.size());
		int comsize = commentsList.size();
		for (int i = 0; i < comsize; i++) {
			Element comnt = (Element) commentsList.get(i);
			String comment = StrUtil.nonNull(comnt.innerText());
			System.out.println(comment);
			CrawlerVO cvo = new CrawlerVO();
			cvo.setComment(comment);
			retList.add(cvo);
			

		}
		List finalList = new ArrayList();
		for (int i = 0; i < articlelist.size(); i++) {
			CrawlerVO cvo = (CrawlerVO)retList.get(i);
			Element temp = (Element) articlelist.get(i);
			String tempStr = StrUtil.nonNull(temp.innerHTML());
		//	System.out.println(tempStr);
			String name = "";
			try {
				name = tempStr.substring((tempStr.indexOf("class='url'>")+"class='url'>".length()), tempStr.indexOf("</a></cite>"));
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				System.out.println("Genericone e1: "+e1.getMessage());
				name = "Anonymous";
			}
			System.out.println(name);
			cvo.setUsername(name);
			String avatar = "";
			try {
				avatar = tempStr.substring((tempStr.indexOf("src='")+"src='".length()), tempStr.indexOf("' class='avatar"));
				if(avatar.indexOf("srcset")>0){
					avatar = "http://2.gravatar.com/avatar/ed7cd66c63b5d7477a05265a9074dd0d";
				}
			} catch (Exception e) {
				avatar = "http://2.gravatar.com/avatar/ed7cd66c63b5d7477a05265a9074dd0d";
			}
			System.out.println(avatar);
			cvo.setAvatar(avatar);
			finalList.add(cvo);
		}
		
		try {
			userAgent.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
		}
		return finalList;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
