/**
 * 
 */
package com.crowd.streetbuzzalgo.crawler;

import java.util.ArrayList;
import java.util.List;

import com.crowd.streetbuzzalgo.crawler.vo.CrawlerVO;
import com.crowd.streetbuzzalgo.utils.DataClean;
import com.crowd.streetbuzzalgo.utils.StrUtil;
import com.jaunt.Element;
import com.jaunt.Elements;
import com.jaunt.UserAgent;

/**
 * @author vasu
 *
 */
public class ParseChictopia {

	/**
	 * 
	 */
	public ParseChictopia() {
		// TODO Auto-generated constructor stub
	}
	public static List getComments(String url, String mode)throws Exception{
		UserAgent userAgent = new UserAgent();
		userAgent.visit(url);
		Element anchor = userAgent.doc.findFirst("<a href>");
		System.out.println("anchor element: " + anchor);
		System.out.println("anchor's tagname: " + anchor.getName());
		System.out.println("anchor's href attribute: " + anchor.getAt("href"));
		System.out.println("anchor's parent Element: " + anchor.getParent());
		//Element meta = userAgent.doc.findFirst("<meta itemprop=commentTime content=2014-11-06 />");
		/* System.out.println("meta element: " + meta);
	    System.out.println("meta's tagname: " + meta.getName());
		System.out.println("meta's content attribute: " + meta.getAt("content"));
		System.out.println("meta's parent Element: " + meta.getParent());*/
		Element div = userAgent.doc.findFirst("<div id=comment>");
		Elements comments = div.findEach("<div itemprop=comment itemscope itemtype=http://schema.org/UserComments class=single_comment style=width:400px;>");
		//Elements comments = div.findEach("<div itemprop=commentText class=comment_content>");
		Elements commenters = div.findEach("<div class=userComment style=width:330px>");
		//Elements commenters = div.findEach("");
		//Elements commentersname = meta.findEach("<meta itemprop=commentTime content=2014-11-06 />");
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
			name = DataClean.clean(name);
			CrawlerVO cvo = new CrawlerVO();
			cvo.setComment(comment);
			cvo.setUsername(name);
			System.out.println(comment);
			cvo.setURLParsed(url);
			cvo.setMode(mode);
			//System.out.println("location:" + userAgent.getLocation());   
			
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
