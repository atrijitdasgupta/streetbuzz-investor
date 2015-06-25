/**
 * 
 */
package com.crowd.streetbuzzalgo.topsy;

import java.util.ArrayList;
import java.util.List;

import com.crowd.streetbuzz.model.Voices;
import com.crowd.streetbuzzalgo.utils.DataClean;
import com.maruti.otterapi.ListParameters;
import com.maruti.otterapi.Otter4JavaException;
import com.maruti.otterapi.TopsyConfig;
import com.maruti.otterapi.search.Post;
import com.maruti.otterapi.search.Search;
import com.maruti.otterapi.search.SearchCriteria;
import com.maruti.otterapi.search.SearchResponse;

/**
 * @author Atrijit
 * 
 */
public class Otter {

	/**
	 * 
	 */
	public Otter() {
		// TODO Auto-generated constructor stub
	}

	public static List call(List keylist) {
		long start = System.currentTimeMillis();
		System.out.println("Inside config 6565575");
		TopsyConfig config = new TopsyConfig();
		config.setApiKey("09C43A9B270A470B8EB8F2946A9369F3");
		config.setSetProxy(false);
		List finalList = new ArrayList();
		
		for (int i = 0; i < keylist.size(); i++) {
			String temp = (String) keylist.get(i);
			List otList = search(config, temp);
			for (int j = 0; j < otList.size(); j++) {
				Post post = (Post) otList.get(j);
				finalList.add(post);
			
			}
		}
		long end = System.currentTimeMillis();
		System.out.println("time: "+((end-start)/1000)+" secs");
		return finalList;
		
	}
	
	public static List callTemp(List keylist) {
		System.out.println("Inside config 6565575");
		TopsyConfig config = new TopsyConfig();
		config.setApiKey("09C43A9B270A470B8EB8F2946A9369F3");
		config.setSetProxy(false);
		List finalList = new ArrayList();
		List holdingList = new ArrayList();
		for (int i = 0; i < keylist.size(); i++) {
			String temp = (String) keylist.get(i);
			List otterList = search(config, temp);
			List ottertruncatedList = new ArrayList();
			if (otterList.size() > 300) {
				ottertruncatedList = otterList.subList(0, 299);
			}
			for (int j = 0; j < ottertruncatedList.size(); j++) {
				Post post = (Post) ottertruncatedList.get(j);
				String text = post.getContent();
				//text = DataClean.clean(text);
				text = text.trim();
				post.setContent(text);
				finalList.add(post);
			
			}
		}
		return finalList;
	}
	
	public static List callCheck(List keylist, List twitvlist) {
		System.out.println("Inside config 6565575");
		TopsyConfig config = new TopsyConfig();
		config.setApiKey("09C43A9B270A470B8EB8F2946A9369F3");
		config.setSetProxy(false);
		List finalList = new ArrayList();
		List holdingList = new ArrayList();
		for (int i = 0; i < keylist.size(); i++) {
			String temp = (String) keylist.get(i);
			//List otList = searchCheck(config, temp, twitvlist);
			List otList = search(config, temp);
			for (int j = 0; j < otList.size(); j++) {
				Post post = (Post) otList.get(j);
				/*String text = post.getContent();
				text = DataClean.clean(text);
				text = text.trim();*/
				finalList.add(post);
				/*String hashtext = StrUtil.getHash(text);
				if (!holdingList.contains(hashtext)) {
					holdingList.add(hashtext);
					finalList.add(post);
				}*/
			}
		}
		return finalList;
	}

	public static List call(String key) {
		System.out.println("Inside config 6565575");
		TopsyConfig config = new TopsyConfig();
		config.setApiKey("09C43A9B270A470B8EB8F2946A9369F3");
		config.setSetProxy(false);
		return (search(config, key));
	}
	
	private static List searchCheck(TopsyConfig config, String key, List twitvlist) {
		List checklist = new ArrayList();
		for(int i=0;i<twitvlist.size();i++){
			Voices tv = (Voices) twitvlist.get(i);
			String posttext = tv.getPosttext();
			if(!checklist.contains(posttext)){
				posttext = posttext.toLowerCase();
				checklist.add(posttext);
			}
		}
		
		List otterList = new ArrayList();
		//List holdList = new ArrayList();
		System.out.println("Inside search 525545");
		Search searchTopsy = new Search();
		searchTopsy.setTopsyConfig(config);
		SearchResponse results = null;
		try {
			SearchCriteria criteria = new SearchCriteria();
			criteria.setQuery(key);
			criteria.setRankcount_maxdays("5");
			results = searchTopsy.search(criteria);
			// System.out.println(results.getResult().getList().size());
			// System.out.println(results.getResult().getTotal());
			List list = results.getResult().getList();
			//System.out.println("list size:: " + list.size());
			List finalList = new ArrayList();
			
			for (int i = 0; i < finalList.size(); i++) {
				Post post = (Post) finalList.get(i);
				otterList.add(post);
			
			}
		} catch (Otter4JavaException e) {
			e.printStackTrace();
		}

		return otterList;
	}

	private static List search(TopsyConfig config, String key) {
		List otterList = new ArrayList();
		//List holdList = new ArrayList();
		System.out.println("Inside search 525545");
		Search searchTopsy = new Search();
		searchTopsy.setTopsyConfig(config);
		SearchResponse results = null;
		try {
			SearchCriteria criteria = new SearchCriteria();
			criteria.setQuery(key);
			criteria.setRankcount_maxdays("5");
			ListParameters lp = new ListParameters();
			lp.setPage("1");
			lp.setPerpage("100");
			
			criteria.setListParams(lp);
			//criteria.setListParams(lp1);
			results = searchTopsy.search(criteria);
			// System.out.println(results.getResult().getList().size());
			// System.out.println(results.getResult().getTotal());
			List list = results.getResult().getList();
		//	System.out.println("list size:: " + list.size());
			for (int i = 0; i < list.size(); i++) {
				Post post = (Post) list.get(i);
				otterList.add(post);
				
				/*String text = post.getContent();
				text = text.trim();
				String authorurl = post.getTrackback_author_name();
				if (!holdList.contains(text)) {
					holdList.add(text);
					System.out.println(text + "::" + authorurl);
				}*/

			}
		} catch (Otter4JavaException e) {
			e.printStackTrace();
		}

		return otterList;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		List list = new ArrayList();
		list.add("Gazpacho");
		call(list);

	}

}
