/**
 * 
 */
package com.crowd.streetbuzzalgo.crawler;

import java.io.IOException;
import java.util.List;

import com.crowd.streetbuzzalgo.utils.CreateFile;
import com.jaunt.UserAgent;

/**
 * @author Atrijit
 * 
 */
public class ParseBlogspot {

	/**
	 * 
	 */
	public ParseBlogspot() {
		// TODO Auto-generated constructor stub
	}

	public static List getComments(String url, String mode) throws Exception {
		UserAgent userAgent = new UserAgent();
		userAgent.visit(url);
		// System.out.println(userAgent.doc.innerHTML());
		String content = userAgent.doc.innerHTML();
		CreateFile.write(content, "youtube");
		try {
			userAgent.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//String url = "http://www.mouthshut.com/inverters/Su-Kam-Brainy-reviews-925053024";
		String url = "https://www.youtube.com/watch?v=KL8zTrkeGVo";
		try {
			getComments(url,"");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
