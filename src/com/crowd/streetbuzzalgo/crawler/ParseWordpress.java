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
import com.jaunt.ResponseException;
import com.jaunt.UserAgent;

/**
 * @author Atrijit
 * 
 */
public class ParseWordpress {

	/**
	 * 
	 */
	public ParseWordpress() {
		// TODO Auto-generated constructor stub
	}
	
	public static List getComments(String url, String mode)throws Exception{
		UserAgent userAgent = new UserAgent();
		userAgent.visit(url);
		Element div = userAgent.doc.findFirst("<div id=comments>");
		Elements comments = div.findEach("<div class=comment-body><p>");
		//Elements commenters = div.findEach("<span class>");
		Elements commenters = div.findEach("<div class=comment-author vcard>");
		List commentsList = comments.toList();
		List commentersList = commenters.toList();
		int comsize = commentsList.size();
		List retList = new ArrayList();
		for (int i=0;i<comsize;i++){
			Element comnt = (Element)commentsList.get(i);
		//	Element comntr = (Element)commentersList.get(i);
			String comment = StrUtil.nonNull(comnt.innerText());
			//System.out.println(comment);
			//String name = StrUtil.nonNull(comntr.getText());
			String name = "";
			System.out.println(comment+"::"+name);
			if("".equalsIgnoreCase(name)){
				name = "Anonymous";
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
	
	public static void parseTest(String url) {
		UserAgent userAgent = new UserAgent();
		
		try {
			userAgent.visit(url);
			Elements temp = userAgent.doc.findEach("<p>");
			List tempList = temp.toList();
			for (int i=0;i<tempList.size();i++){
				Element text = (Element)tempList.get(i);
				String temptxt = StrUtil.nonNull(text.getText());
				if(!"".equalsIgnoreCase(temptxt)){
					System.out.println("temptxt: "+temptxt);
				}
				
			}
			System.out.println(temp);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void parse(String url) {
		UserAgent userAgent = new UserAgent();
		try {
			userAgent.visit(url);
			try {
				System.out.println("here");
				
				Element div = userAgent.doc.findFirst("<div id=comments>");
				//System.out.println("div's innerText: " + div.innerText());
				Elements comments = div.findEach("<div class=comment-body><p>");
				List commentsList = comments.toList();
				Elements commenters = div.findEach("<div class=comment-author vcard>");
				List commentersList = commenters.toList();
				//Elements commentsmeta  = div.findEach("<span class=comment-meta commentmetadata>");
				//List commentsmetaList = commentsmeta.toList();
				//System.out.println(commentsList.size()+" <-> "+commentersList.size()+">-<"+commentsmetaList.size());
				
				int comsize = commentsList.size();
				int comntrsize = commentersList.size();
				
				for (int i=0;i<comsize;i++){
					Element comnt = (Element)commentsList.get(i);
					Element comntr = (Element)commentersList.get(i);
					//Element meta = (Element)commentsmetaList.get(i);
					String name = StrUtil.nonNull(comntr.getText());
					
					if("".equalsIgnoreCase(name)){
						name = "Unknown";
					}
					
					System.out.println(name+"<==>"+comnt.innerText());
				}
				
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
//			System.out.println(userAgent.doc.innerHTML()); 
			//String content = userAgent.doc.innerHTML();
			//CreateFile.write(content, "wordpress");
			/*try {
				Element div = userAgent.doc.findFirst("<div id=comments>");
				if(div==null){
					System.out.println("got null");
				}else{
					System.out.println("Foudn the div");
				}
				System.out.println("div's name: " + div.getName());   
				
			  //  System.out.println("div's innerText: " + div.innerText());
			} catch (NodeNotFound e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
		} catch (ResponseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new ParseWordpress().parseTest("http://jaunt-api.com/jaunt-tutorial.htm");
	}

}
