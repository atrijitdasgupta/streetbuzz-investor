/**
 * 
 */
package com.crowd.streetbuzzalgo.googlesearch;

import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import com.crowd.streetbuzzalgo.constants.SystemConstants;
import com.crowd.streetbuzzalgo.googlesearch.vo.GoogleResults;
import com.crowd.streetbuzzalgo.googlesearch.vo.ResponseData;
import com.crowd.streetbuzzalgo.googlesearch.vo.Result;
import com.crowd.streetbuzzalgo.utils.StrUtil;
import com.google.gson.Gson;

/**
 * @author Atrijit
 * 
 */
public class SearchGoogle implements SystemConstants {

	public SearchGoogle() {

	}

	public static List search(String query) throws Exception {
		List searchList = new ArrayList();
		for (int i = 0; i < 16; i = i + 4) {
			String address = GOOGLE_SEARCH_URL;
			String iStr = new Integer(i).toString();
			address = address.replaceAll("<i>", iStr);
			String uniqueid = StrUtil.getUniqueGoogleId();
			address = address.replaceAll("<qu>", uniqueid);
			String charset = CHARSET;
			URL url = new URL(address + URLEncoder.encode(query, charset));
			//System.out.println("URL:" + url.toString());
			Reader reader = new InputStreamReader(url.openStream(), charset);
			GoogleResults results = new Gson().fromJson(reader,
					GoogleResults.class);
			if (results != null) {
				int size = 0;
				ResponseData rd = results.getResponseData();
				if (rd != null) {
					List resultList = rd.getResults();
					if (resultList != null) {
						if (resultList.size() > 0) {
							for (int j = 0; j < resultList.size(); j++) {
								Result result = (Result) resultList.get(j);
								if (result != null) {
									System.out.println("Title: "
											+ result.getTitle() + ", Link: "
											+ result.getUrl());
									searchList.add(result);
									
								}

							}
						}
					}
				}
			}
			try {
				reader.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			Thread.sleep(5000);

		}
		return searchList;
	}

	public static void limitedsearch(String query) throws Exception {
		String address = GOOGLE_SEARCH_URL;
		String charset = CHARSET;
		URL url = new URL(address + URLEncoder.encode(query, charset));
		Reader reader = new InputStreamReader(url.openStream(), charset);
		GoogleResults results = new Gson()
				.fromJson(reader, GoogleResults.class);
		int total = results.getResponseData().getResults().size();
		System.out.println("total: " + total);
		// Show title and URL of each results
		/*for (int i = 0; i <= total - 1; i++) {
			System.out.println("Title: "
					+ results.getResponseData().getResults().get(i).getTitle());
			System.out.println("URL: "
					+ results.getResponseData().getResults().get(i).getUrl()
					+ "\n");

		}*/
	}

	public static void main(String[] args) {
		try {
			search("Hole in the wall Bangalore");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
