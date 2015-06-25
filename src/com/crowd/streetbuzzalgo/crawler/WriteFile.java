/**
 * 
 */
package com.crowd.streetbuzzalgo.crawler;

import com.crowd.streetbuzzalgo.utils.CreateFile;
import com.jaunt.UserAgent;

/**
 * @author vasu
 *
 */
public class WriteFile {

	/**
	 * 
	 */
	public WriteFile() {
		// TODO Auto-generated constructor stub
	}
	
	public static void getComments(String url, String mode) throws Exception {
		UserAgent userAgent = new UserAgent();
		userAgent.visit(url);
		// System.out.println(userAgent.doc.innerHTML());
		String content = userAgent.doc.innerHTML();
		CreateFile.write(content, mode);

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String url = "http://www.yelp.com/biz/the-house-san-francisco";
		String mode="YELP";
		try {
			getComments(url,mode);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
