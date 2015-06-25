/**
 * 
 */
package com.crowd.streetbuzzalgo.webcrawler;

import java.util.concurrent.Callable;

/**
 * @author Atrijit
 *
 */
public class RunnableCrawler implements Callable{
	private final String url;
	/**
	 * 
	 */
	public RunnableCrawler(String url, int i) {
		System.out.println("Starting thread: "+(i+1));
		this.url = url;
	}
	
	public String call() {
		CrawlerController CC = new CrawlerController(url);
		return null;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
