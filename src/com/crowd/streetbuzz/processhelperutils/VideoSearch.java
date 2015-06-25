/**
 * 
 */
package com.crowd.streetbuzz.processhelperutils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import com.crowd.streetbuzzalgo.constants.SystemConstants;
import com.crowd.streetbuzzalgo.socialplugin.youtube.YTSearch;
import com.crowd.streetbuzzalgo.vimeo.Vimeo;
import com.crowd.streetbuzzalgo.vimeo.VimeoResponse;

/**
 * @author Atrijit
 *
 */
public class VideoSearch implements SystemConstants{

	/**
	 * 
	 */
	public VideoSearch() {
		// TODO Auto-generated constructor stub
	}
	public static List searchYoutubeCheck(List searchTermsList, List ytvlist) {
		List finalList = new ArrayList();
		YTSearch ytSearch = new YTSearch();
		for (int i = 0; i < searchTermsList.size(); i++) {
			String querystr = (String) searchTermsList.get(i);
			List result = YTSearch.searchYoutubeCheck(querystr, ytvlist);
			finalList.addAll(result);
		}

		return finalList;
	}
	public static List searchYoutube(List searchTermsList) {
		List finalList = new ArrayList();
		YTSearch ytSearch = new YTSearch();
		for (int i = 0; i < searchTermsList.size(); i++) {
			String querystr = (String) searchTermsList.get(i);
			List result = YTSearch.searchYoutube(querystr);
			finalList.addAll(result);
		}

		return finalList;
	}
	
	public static void searchVimeo(String term){
		try {
			term = URLEncoder.encode(term,"UTF-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Vimeo vimeo = new Vimeo(VIMEO_ACCESS_TOKEN); 
		VimeoResponse vr = null;
		try {
			vr = vimeo.searchVideos(term);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JSONObject jo = vr.getJson();
		System.out.println(jo.toString());
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		searchVimeo("Whales");
	}

}
