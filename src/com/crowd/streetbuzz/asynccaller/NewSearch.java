/**
 * 
 */
package com.crowd.streetbuzz.asynccaller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * @author Atrijit
 *
 */
public class NewSearch {

	/**
	 * 
	 */
	public NewSearch() {
		while(true){
			try {
				this.call();
				Thread.sleep(30000);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	/*
	 * 
	 */
	private void call()throws Exception{
		System.out.println("NewSearch calling "+System.currentTimeMillis());
		URL url = new URL("http://203.123.190.50/streetbuzz/newsearchasync.htm");
		BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
		in.close();
		
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new NewSearch();

	}

}
