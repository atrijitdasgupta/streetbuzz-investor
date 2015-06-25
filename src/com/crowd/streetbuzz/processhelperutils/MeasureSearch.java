/**
 * 
 */
package com.crowd.streetbuzz.processhelperutils;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import com.crowd.streetbuzzalgo.faroosearch.vo.FarooResult;
import com.crowd.streetbuzzalgo.faroosearch.vo.FarooResultSet;
import com.crowd.streetbuzzalgo.googlesearch.vo.Result;
import com.crowd.streetbuzzalgo.yandexsearch.YandexVO;

/**
 * @author Atrijit
 *
 */
public class MeasureSearch {

	/**
	 * 
	 */
	public MeasureSearch() {
		try {
			System.setOut(new PrintStream(new BufferedOutputStream(
					new FileOutputStream("C:/Mywork/StreetBuzz/boom/measure23.txt"))));
			List list = new ArrayList();
			/*list.add("Entertainment");
			list.add("Entrepreneurship");
			list.add("Printing");
			list.add("Soul Music");
			list.add("Medical Careers");
			list.add("Formal wear");*/
			list.add("Maternity");
			list.add("Gardening");
			list.add("Metereology");
			list.add("Agnosticism");
			search(list);
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void search(List terms){
		for (int k=0;k<terms.size();k++){
			String term = (String)terms.get(k);
			List list = new ArrayList();
			list.add(term);
			//List glist = WebSiteSearch.googleSearch(list);
			//List frlist = WebSiteSearch.farooSearch(list);
			List ylist = WebSiteSearch.yandexSearch(list);
			
			System.out.println(term);
			System.out.println("****************************************************");
			/*System.out.println("Google::");
			for (int i=0;i<glist.size();i++){
				Result r = (Result)glist.get(i);
				System.out.println(r.getUrl());
				
			}*/
			/*System.out.println("Faroo::");
			for (int i = 0; i < frlist.size(); i++) {
				FarooResultSet frs = (FarooResultSet) frlist.get(i);
				List results = frs.getResults();
				if (results != null) {
					for (int j = 0; j < results.size(); j++) {
						FarooResult fr = (FarooResult) results.get(j);
						String url = fr.getUrl();
						System.out.println(url);
					}
				}
			}*/
			
			System.out.println("Yandex::");
			for (int i=0;i<ylist.size();i++){
				YandexVO yndxvo = (YandexVO) ylist.get(i);
				String url = yndxvo.getUrl();
				System.out.println(url);
			}
		}
		
	
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		MeasureSearch obj = new MeasureSearch();

	}

}
