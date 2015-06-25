/**
 * 
 */
package com.crowd.streetbuzzalgo.deduplicate;

import java.util.ArrayList;
import java.util.List;

import com.crowd.streetbuzzalgo.taxonomy.freebase.Freebase;

/**
 * @author Atrijit
 *
 */
public class TopicDisambiguation {

	/**
	 * 
	 */
	public TopicDisambiguation() {
		// TODO Auto-generated constructor stub
	}
	
	public static String call(String topic){
		List freebaselist = new ArrayList();
		String retStr = "";
		try {
			freebaselist = Freebase.getList(topic);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
		}
		if(freebaselist!=null && freebaselist.size()>0){
			retStr = (String)freebaselist.get(0);
		}
		return retStr;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println(call("N Modi"));
	}

}
