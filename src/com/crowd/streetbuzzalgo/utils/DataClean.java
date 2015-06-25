/**
 * 
 */
package com.crowd.streetbuzzalgo.utils;

import org.jsoup.Jsoup;

/**
 * @author Atrijit
 *
 */
public class DataClean {

	/**
	 * 
	 */
	public DataClean() {
		// TODO Auto-generated constructor stub
	}
	public static String clean(String source){
		source = Jsoup.parse(source).text();
		//source = source.replaceAll("\\P{Alnum}", "");
		source = source.replaceAll("[^A-Za-z0-9 ]", "");
		source = source.trim();
		//System.out.println(source);
		 return source;
	}
	public static String numberClean(String source){
		source = Jsoup.parse(source).text();
		source = source.replaceAll("[^A-Za-z ]", "");
		source = source.trim();
		//System.out.println(source);
		 return source;
	}
	
	public static String htmlClean(String source){
		source = Jsoup.parse(source).text();
		source = source.trim();
		//System.out.println(source);
		 return source;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//String reg = "ejkrggb  ng ## /? ~ <a></a> No way! 223";
		String reg = "<b>Who is</b> The leader %20?";
		clean(reg);
		

	}

}
