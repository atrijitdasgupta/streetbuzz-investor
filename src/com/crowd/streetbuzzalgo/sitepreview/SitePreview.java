/**
 * 
 */
package com.crowd.streetbuzzalgo.sitepreview;

import java.io.IOException;

import com.jaunt.Element;
import com.jaunt.UserAgent;

/**
 * @author Atrijit
 *
 */
public class SitePreview {

	/**
	 * 
	 */
	public SitePreview() {
		// TODO Auto-generated constructor stub
	}
	private static void parseForPreview(String url)throws Exception{
		UserAgent userAgent = new UserAgent();
		userAgent.visit(url);
		
		Element title;
		Element metadescription;
		Element img;
		
		title = userAgent.doc.findFirst("<title>");
		metadescription = userAgent.doc.findFirst("<Meta>");
		img = userAgent.doc.findFirst("<img>");
		
		System.out.println("title:: "+title.getText());
		System.out.println("metadescription:: "+metadescription.getText());
		System.out.println("img:: "+img.getText());
		try {
			userAgent.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
		}
		
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			parseForPreview("http://www.7casp.in/sevencasp/home.htm");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
