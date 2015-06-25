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
public class EditSearch {

	/**
	 * 
	 */
	public EditSearch() {
		try {
			this.call();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void call()throws Exception{
		URL url = new URL("http://localhost/streetbuzz/editsearchasync.htm");
		BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
		in.close();
		
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new EditSearch();

	}

}
