/**
 * 
 */
package com.crowd.streetbuzzalgo.bingsearch;

import net.billylieurance.azuresearch.AzureSearchResultSet;
import net.billylieurance.azuresearch.AzureSearchWebQuery;
import net.billylieurance.azuresearch.AzureSearchWebResult;

import com.crowd.streetbuzzalgo.constants.SystemConstants;

/**
 * @author Atrijit
 *
 */
public class BingSearch implements SystemConstants{

	/**
	 * 
	 */
	public BingSearch() {
		// TODO Auto-generated constructor stub
	}
	
		
	private static void doSearch(String keyword){
		AzureSearchWebQuery aq = new AzureSearchWebQuery();
		aq.setAppid(account_key);
		aq.setQuery(keyword);
		for (int i=1; i<=3 ; i++) {
			aq.setPage(i);
	        aq.doQuery();
	        AzureSearchResultSet<AzureSearchWebResult> ars = aq.getQueryResult();
	           for (AzureSearchWebResult anr : ars) {
	            System.out.println(anr.getTitle());
	            System.out.println(anr.getUrl());
	            System.out.println(anr.getDisplayUrl());
	            System.out.println(anr.getDescription());
	           }
		}
		
		
		
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		doSearch("Narendra Modi");

	}

}
