/**
 * 
 */
package com.crowd.streetbuzzalgo.yandexsearch;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.crowd.streetbuzzalgo.constants.SystemConstants;
import com.crowd.streetbuzzalgo.utils.StrUtil;
import com.jaunt.Element;
import com.jaunt.Elements;
import com.jaunt.ResponseException;
import com.jaunt.UserAgent;

/**
 * @author Atrijit
 *
 */
public class YandexSearch implements SystemConstants{

	/**
	 * 
	 */
	public YandexSearch() {
		// TODO Auto-generated constructor stub
	}
	
	private static String doSearch(String keyword) throws Exception{
		String url = YANDEX_SEARCH_URL;
		String requestString = YANDEX_SEARCH_REQUEST_XML;
		requestString = requestString.replaceFirst("#QUERY#", keyword);
		System.out.println(requestString);
		
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
//		add reuqest header
		con.setRequestMethod("POST");
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(requestString);
		wr.flush();
		wr.close();
		
		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();
 
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		 
		//print result
		//System.out.println(response.toString());
		return response.toString();
		
	}
	
	public static List yandexSearch(List searchterms){
		List yandexList = new ArrayList();
		UserAgent userAgent = new UserAgent();
		for (int i=0;i<searchterms.size();i++){
			String keyword = (String)searchterms.get(i);
			String searchresult = "";
			try {
				searchresult = doSearch(keyword);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(!"".equalsIgnoreCase(searchresult)){
				try {
					userAgent.openContent(searchresult);
					
				} catch (ResponseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			Elements elem = userAgent.doc.findEach("<url>");
			Elements titlelem = userAgent.doc.findEach("<title>");
			Elements modtimelem = userAgent.doc.findEach("<modtime>");
			
			List elemList = elem.toList();
			List titleList = titlelem.toList();
			List modtimeList = modtimelem.toList();
			
			for (int j=0;j<elemList.size();j++){
				Element url = (Element)elemList.get(j);
				String urltxt = StrUtil.nonNull(url.getText());
				
				Element title = (Element)titleList.get(j);
				String titletxt = StrUtil.nonNull(title.getText());
				
				Element modtime = (Element)modtimeList.get(j);
				String modtimetxt = StrUtil.nonNull(modtime.getText());
				Date mddate = StrUtil.getYandexDate(modtimetxt);
				
				YandexVO yndxvo = new YandexVO();
				yndxvo.setModdate(mddate);
				yndxvo.setTitle(titletxt);
				yndxvo.setUrl(urltxt);
				
				yandexList.add(yndxvo);
			}
		}
		try {
			userAgent.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
		}
		System.out.println(yandexList.size());
		return yandexList;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			List list = new ArrayList();
			list.add("pizza");
			yandexSearch(list);
			/*UserAgent userAgent = new UserAgent();
			userAgent.openContent(temp);
			Elements elem = userAgent.doc.findEach("<url>");
			Elements titlelem = userAgent.doc.findEach("<title>");
			Elements modtimelem = userAgent.doc.findEach("<modtime>");
			List elemList = elem.toList();
			List titleList = titlelem.toList();
			List modtimeList = modtimelem.toList();
			for (int i=0;i<elemList.size();i++){
				Element text = (Element)elemList.get(i);
				String temptxt = StrUtil.nonNull(text.getText());
				System.out.println(i+" "+temptxt);
			}
			
			for (int i=0;i<titleList.size();i++){
				Element text = (Element)titleList.get(i);
				String temptxt = StrUtil.nonNull(text.getText());
				System.out.println(i+" "+temptxt);
			}
			
			for (int i=0;i<modtimeList.size();i++){
				Element text = (Element)modtimeList.get(i);
				String temptxt = StrUtil.nonNull(text.getText());
				System.out.println(i+" "+temptxt);
			}*/
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
