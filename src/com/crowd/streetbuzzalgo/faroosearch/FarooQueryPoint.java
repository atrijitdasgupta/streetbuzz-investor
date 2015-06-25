/**
 * 
 */
package com.crowd.streetbuzzalgo.faroosearch;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.crowd.streetbuzzalgo.constants.SystemConstants;
import com.crowd.streetbuzzalgo.faroosearch.vo.FarooResult;
import com.crowd.streetbuzzalgo.faroosearch.vo.FarooResultSet;
import com.google.gson.Gson;

/**
 * @author Atrijit
 * 
 */
public class FarooQueryPoint implements SystemConstants{
	private static Date lastAccessed;

	private static final String key = FAROO_SEARCH_KEY;

	/**
	 * 
	 */
	public FarooQueryPoint() {
		// TODO Auto-generated constructor stub
	}

	public static enum language {
		en, de, zh;
	}

	/*public FarooQueryPoint(String key) {
		this.key = key;
	}*/

	public FarooResultSet query(String query) {
		

		handleOneSecondBetweenQueriesRule();

		String encodedQuery = encodeQuery(query);

		/*String json = runQuery("http://www.faroo.com/api?q=" + encodedQuery
				+ "&key=" + key);*/
		/*String json = runQuery("http://www.faroo.com/api?q=" + encodedQuery
				+ "&key=" + key+"&start=0&length=100");*/
		
		String json = runQuery("http://www.faroo.com/api?q=" + encodedQuery
				+ "&key=" + key+"&start=0&length=20");

		FarooResultSet resultsSet = new Gson().fromJson(json,
				FarooResultSet.class);
		List farooResultList = new ArrayList();
		if(resultsSet!=null){
			farooResultList = resultsSet.getResults();
		}
		
		for(int j=0;j<farooResultList.size();j++){
			FarooResult fr = (FarooResult)farooResultList.get(j);
			String url = fr.getUrl();
			String title = fr.getTitle();
			//System.out.println("Title: "+title+", Url: "+url);
		}
		return resultsSet;

	}

	/**
	 * query faroo web search with given parameters
	 * 
	 * @param query
	 *            the search term
	 * @param startPointOfResults
	 *            start point of the resultslist (max is 100). default 1
	 * @param nrOfResults
	 *            number of results (max is 100). default 20
	 * @param language
	 *            the language of the results. either en, de or zh. default en
	 * @param snippetFromArticleBeginning
	 *            true if snippet from the article should always be from the
	 *            beginning. default false
	 * @param instantSearch
	 *            false if faroo should search with given query. true if faroo
	 *            should first check for spelling and suggestions and take the
	 *            best match. default false
	 * @return
	 */
	public FarooResultSet query(String query, int startPointOfResults,
			int nrOfResults, language language,
			boolean snippetFromArticleBeginning, boolean instantSearch) {

		handleOneSecondBetweenQueriesRule();

		String encodedQuery = encodeQuery(query);

		String queryStr = "http://www.faroo.com/api?q=" + encodedQuery;
		if (startPointOfResults > 0)
			queryStr += "&start=" + startPointOfResults;
		if (nrOfResults > 0)
			queryStr += "&length=" + nrOfResults;
		if (language != null)
			queryStr += "&l=" + language;
		if (snippetFromArticleBeginning)
			queryStr += "&kwic=" + snippetFromArticleBeginning;
		if (instantSearch)
			queryStr += "&i=" + instantSearch;
		queryStr += "&key=" + key;

		String json = runQuery(queryStr);

		FarooResultSet resultsSet = new Gson().fromJson(json,
				FarooResultSet.class);
		return resultsSet;
	}

	private String encodeQuery(String query) {
		String encodedQuery = "";
		try {
			encodedQuery = URLEncoder.encode(query, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return encodedQuery;
	}

	private void handleOneSecondBetweenQueriesRule() {
		if (!lastQueryMoreThanOneSecondAway()) {
			try {
				Thread.sleep(timeToWaitUntilNextQuery());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private String runQuery(String url) {
		try {

			URL theUrl = new URL(url);

			BufferedReader in = new BufferedReader(new InputStreamReader(theUrl
					.openStream()));

			String jsonString = "";
			String line;
			while ((line = in.readLine()) != null)
				jsonString += line;
			in.close();

			blockAccessForNextSecond();

			return jsonString;

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	private void blockAccessForNextSecond() {
		lastAccessed = new Date();
	}

	private boolean lastQueryMoreThanOneSecondAway() {
		if (lastAccessed == null
				|| (new Date().getTime() - lastAccessed.getTime()) > 1000)
			return true;
		return false;
	}

	private long timeToWaitUntilNextQuery() {
		if (lastAccessed == null)
			return 0;

		long timeToWait = 1000 - (new Date().getTime() - lastAccessed.getTime());
		if (timeToWait < 0)
			return 0;
		else
			return timeToWait;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		FarooQueryPoint fqp = new FarooQueryPoint();
		FarooResultSet fqs = fqp.query("Will BJP win in Delhi");
		if(fqs!=null){
			List results = fqs.getResults();
			for (int i=0;i<results.size();i++){
				FarooResult fr = (FarooResult)results.get(i);
				String title = fr.getTitle();
				String url = fr.getUrl();
				System.out.println("title: "+title+", url:"+url);
			}
			String query = fqs.getQuery();
			System.out.println("query: "+query);
			List suggestions = fqs.getSuggestions();
			for (int j=0;j<suggestions.size();j++){
				String suggest = (String)suggestions.get(j);
				System.out.println("suggestions: "+suggest);
			}
		}
	}

}
