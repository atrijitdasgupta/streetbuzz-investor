/**
 * 
 */
package com.crowd.streetbuzzalgo.webhose;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.buzzilla.webhose.client.WebhoseClient;
import com.buzzilla.webhose.client.WebhosePost;
import com.buzzilla.webhose.client.WebhoseQuery;
import com.buzzilla.webhose.client.WebhoseResponse;
import com.buzzilla.webhose.client.WebhoseQuery.SiteType;
import com.crowd.streetbuzz.common.Constants;
import com.crowd.streetbuzzalgo.constants.SystemConstants;
import com.crowd.streetbuzzalgo.utils.StrUtil;

/**
 * @author Atrijit
 * 
 */
public class WebHose implements SystemConstants, Constants {

	/**
	 * 
	 */
	public WebHose() {
		// TODO Auto-generated constructor stub
		main(null);
	}

	public static List search(String topic, String type) {
		List list = new ArrayList();
		WebhoseClient client = new WebhoseClient(WEBHOSEKEY);
		WebhoseResponse response = null;

		WebhoseQuery query = new WebhoseQuery();
		if ("blog".equalsIgnoreCase(type)) {
			query.siteTypes.add(SiteType.blogs);
		} else {
			query.siteTypes.add(SiteType.discussions);
		}
		query.language.add("english");
		query.allTerms.add(topic);
			
		try {
			response = client.search(query);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		int counter = 0;
		try {
			for (WebhosePost post : response.posts) {
				counter = counter+1;
			//	System.out.println("############################### "+counter);
				String text = post.text;
				String url = post.url;
				String title = StrUtil.nonNull(post.title);
				String published = StrUtil.nonNull(post.published);
				Date dt = StrUtil.getWebhoseDate(published);
				if("".equalsIgnoreCase(title)){
					List para = StrUtil.breakPara(text);
					if(para!=null && para.size()>0){
						title = (String)para.get(0);
					}
					
				}
				/*String author = StrUtil.nonNull(post.author);
				if("".equalsIgnoreCase(author)){
					author = "Webhose";
				}*/
				String author = "Webhose";
				
				
			//	System.out.println("title: "+title);
			//	System.out.println("author: "+author);
			//	System.out.println("url: "+url);
			//	System.out.println("text: "+text);
				
				
				WebHoseVO whv = new WebHoseVO();
				whv.setAuthor(author);
				whv.setText(text);
				whv.setDate(dt);
				whv.setTitle(title);
				whv.setUrl(url);
				list.add(whv);
				if(list.size()==100){
					break;
				}

			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		/*while (response.moreResultsAvailable > 0) {
			try {
				response = client.getMore(response);
				for (WebhosePost post : response.posts) {
					String title = post.title;
					String author = post.author;
					String text = post.text;
					String url = post.url;

					WebHoseVO whv = new WebHoseVO();
					whv.setAuthor(author);
					whv.setText(text);
					whv.setTitle(title);
					whv.setUrl(url);
					list.add(whv);

				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}*/
		//For the investor release just taking top 50: 08.06.2015
		List tlist = new ArrayList();
		if(list.size()>50){
			tlist = list.subList(0, 50);
		}else{
			tlist = list;
		}
		return tlist;
	}

	public static void searchOld(String topic) {
		WebhoseClient client = new WebhoseClient(WEBHOSEKEY);
		WebhoseResponse response = null;
		WebhoseQuery query = new WebhoseQuery();
		// query.siteTypes.add(SiteType.news);
		query.siteTypes.add(SiteType.blogs);
		query.siteTypes.add(SiteType.discussions);
		query.language.add("english");
		query.allTerms.add(topic);

		try {

			System.setOut(new PrintStream(new BufferedOutputStream(
					new FileOutputStream(BASESBSTORAGEPATH+"longtext/" + topic
							+ "_OUTPUT.txt"))));
		} catch (FileNotFoundException e2) { // TODO Auto-generated catch
			// block
			e2.printStackTrace();
		}

		System.out.println(topic + " " + SiteType.discussions.name() + ":");

		try {
			response = client.search(query);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		List posts = response.posts;
		for (int j = 0; j < posts.size(); j++) {
			if(j>39){
				break;
			}
			WebhosePost post = (WebhosePost) posts.get(j);
			System.out.println(post.title);
			String text = post.text;
			List list = StrUtil.breakPara(text);
			for (int i = 0; i < list.size(); i++) {
				String temp = (String) list.get(i);
				System.out.println(temp);
				System.out.println("\n");
			}
			if(j>39){
				break;
			}
		}
		/*for (WebhosePost post : response.posts) {
			System.out.println(post.title);
			String text = post.text;
			List list = StrUtil.breakPara(text);
			for (int i = 0; i < list.size(); i++) {
				String temp = (String) list.get(i);
				System.out.println(temp);
				System.out.println("\n");
			}

		}
*/
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		List list = new ArrayList();
	
		list.add("taekwondo");
		/*list.add("Foxconn");
		list.add("Sky Diving");
		list.add("CNN");
		list.add("Nasa");
		list.add("Shahrukh Khan");
		list.add("Jazz");
		list.add("Nargis");
		list.add("Desert");
		list.add("Tintin");*/
		

		for (int i = 0; i < list.size(); i++) {
			String str = (String) list.get(i);
			search(str,"blog");
		}

	}

}
