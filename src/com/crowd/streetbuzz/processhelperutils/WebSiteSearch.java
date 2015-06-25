/**
 * 
 */
package com.crowd.streetbuzz.processhelperutils;

import java.util.ArrayList;
import java.util.List;

import com.crowd.streetbuzzalgo.faroosearch.FarooQueryPoint;
import com.crowd.streetbuzzalgo.faroosearch.vo.FarooResultSet;
import com.crowd.streetbuzzalgo.googlesearch.SearchGoogle;
import com.crowd.streetbuzzalgo.yandexsearch.YandexSearch;

/**
 * @author Atrijit
 *
 */
public class WebSiteSearch {

	/**
	 * 
	 */
	public WebSiteSearch() {
		// TODO Auto-generated constructor stub
	}
	public static List googleSearch(List searchTermsList) {
		List finalList = new ArrayList();
		for (int i = 0; i < searchTermsList.size(); i++) {
			String querystr = (String) searchTermsList.get(i);
			
			List result = new ArrayList();
			try {
				result = SearchGoogle.search(querystr);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			finalList.addAll(result);
		}
		//System.out.println("google size=>"+finalList.size());
		return finalList;
	}
	
	public static List farooSearch(List searchkeyterms) {
		List retList = new ArrayList();
		FarooQueryPoint fqp = new FarooQueryPoint();
		for (int i = 0; i < searchkeyterms.size(); i++) {
			String searchkey = (String) searchkeyterms.get(i);
			FarooResultSet resultsSet = fqp.query(searchkey);
			retList.add(resultsSet);

		}
	//	System.out.println("Faroo size=>"+retList.size());
		return retList;
	}
	
	public static List yandexSearch(List searchkeyterms){
		List yList =  YandexSearch.yandexSearch(searchkeyterms);
		//System.out.println("Yandex size=>"+yList.size());
		return yList;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		List list = new ArrayList();
	list.add("Will BJP win in Delhi?");
	//	list.add("Best coffee in Pune blog");
		
	googleSearch(list);
	farooSearch(list);
	yandexSearch(list);

	}

}
