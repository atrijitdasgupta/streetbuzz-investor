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
public class NewConversationTwo {

	/**
	 * 
	 */
	public NewConversationTwo() {
		// TODO Auto-generated constructor stub
		while (true){
			try {
				this.call();
				Thread.sleep(11000);
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
	}
	
	private void call()throws Exception{
		URL url = new URL("http://203.123.190.50/streetbuzz/newconvasync.htm");
		BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
		in.close();
		
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new NewConversationTwo();
	}

}