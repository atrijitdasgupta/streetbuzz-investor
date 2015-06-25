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
import com.jaunt.NotFound;
import com.jaunt.ResponseException;
import com.jaunt.UserAgent;

/**
 * @author Atrijit
 * 
 */
public class YoutubeCommentReader {

	/**
	 * 
	 */
	public YoutubeCommentReader() {
		// TODO Auto-generated constructor stub
	}

	public static List readComments(String youtubeid) {
		String commentsurl = "http://gdata.youtube.com/feeds/api/videos/"
				+ youtubeid + "/comments";
		//System.out.println(commentsurl);
		List crawlerList = new ArrayList();
		UserAgent userAgent = new UserAgent();
		try {
			userAgent.visit(commentsurl);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
		}
		String doc = "";
		try {
			doc = userAgent.doc.innerHTML();
		} catch (Exception e2) {
			// TODO Auto-generated catch block
			System.out.println(e2.getMessage());
		}
		// System.out.println(doc);
		Elements content = null;
		try {
			content = userAgent.doc.findEach("<content type=text>");
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			System.out.println(e1.getMessage());
		}
		Elements commenter = null;

		try {
			commenter = userAgent.doc.findEach("<name>");
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			System.out.println(e1.getMessage());
		}
		if (content != null) {
			for (int i = 0; i < content.size(); i++) {
				try {
					if(i>10){
						break;
					}
					Element c = content.getElement(i);
					String text = StrUtil.nonNull(c.getText());
					CrawlerVO cvo = new CrawlerVO();
					cvo.setComment(text);
					crawlerList.add(cvo);
					

				} catch (NotFound e) {
					// TODO Auto-generated catch block
					System.out.println(e.getMessage());
				}
			}
		}

	//	System.out.println("************************************************");
		List commenterlist = new ArrayList();
		if (commenter != null) {
			for (int i = 0; i < commenter.size(); i++) {
				try {
					Element s = commenter.getElement(i);
					String cmntr = StrUtil.nonNull(s.getText());
					if (!"".equalsIgnoreCase(cmntr)) {
						cmntr = cmntr.trim();
						if (!"youtube".equalsIgnoreCase(cmntr)) {
							commenterlist.add(cmntr);
						}

					}

				} catch (NotFound e) {
					// TODO Auto-generated catch block
					System.out.println(e.getMessage());
				}
			}
		}

		List list = new ArrayList();
		if (commenterlist != null && commenterlist.size() > 0) {
			for (int i = 0; i < commenterlist.size(); i++) {
				String who = (String) commenterlist.get(i);
				CrawlerVO cvo = (CrawlerVO) crawlerList.get(i);
				cvo.setUsername(who);
				list.add(cvo);
			}
		}
		try {
			userAgent.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//System.out.println(e.getMessage());
		}
		return list;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		readComments("-21pd1n1f7c");
		// https://www.youtube.com/watch?v=Se1y2R5QRKU
		// http://gdata.youtube.com/feeds/api/videos/-21pd1n1f7c/comments
	}

}
