/**
 * 
 */
package com.crowd.streetbuzzalgo.interestdetection;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.crowd.streetbuzzalgo.lingpipe.LingPipeInterest;
import com.crowd.streetbuzzalgo.taxonomy.conceptnet.ConceptNet;
import com.crowd.streetbuzzalgo.uclassify.UClassify;

/**
 * @author Atrijit
 * 
 */
public class FreshInterestDetector {

	/**
	 * 
	 */
	public FreshInterestDetector() {
		// TODO Auto-generated constructor stub
	}

	public static List detectInterest(String topic) throws Exception {
		System.out.println(topic);
		List list = new ArrayList();
	//	list = runConceptNet(topic);
	//	System.out.println("from ConceptNet:"+ list);
	//	String interestidStr = new LingPipeInterest().classify(topic);
		//System.out.println("from Lingpipe:"+ interestidStr);
		List headers = GoogleBasedInterest.searchGoogle(topic);
		headers.add(topic);
		for (int i=0;i<headers.size();i++){
			String head = (String)headers.get(i);
			System.out.println(head);
			Map map = new UClassify().run(head);
			Set set = map.keySet();
			Iterator it = set.iterator();
			while (it.hasNext()){
				String key = (String)it.next();
				Double value = (Double)map.get(key);
				System.out.println(key+"::"+value);
			}
		}
	//	System.out.println("Google: "+headers);
		return list;
	}

	private static List runConceptNet(String topic) throws Exception {
		List termslist = new ArrayList();
		termslist.add(topic);
		List finalconceptlist = ConceptNet.conceptNet(termslist);
		return finalconceptlist;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			detectInterest("Nepal Earthquake");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
