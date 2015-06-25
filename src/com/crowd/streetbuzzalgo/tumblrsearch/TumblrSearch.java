/**
 * 
 */
package com.crowd.streetbuzzalgo.tumblrsearch;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jsoup.Jsoup;

import com.crowd.streetbuzzalgo.constants.SystemConstants;
import com.crowd.streetbuzzalgo.utils.StrUtil;
import com.tumblr.jumblr.JumblrClient;

/**
 * @author Atrijit
 *
 */
public class TumblrSearch implements SystemConstants{

	/**
	 * 
	 */
	public TumblrSearch() {
		// TODO Auto-generated constructor stub
	}
	public static List search(List keylist){
		List retList = new ArrayList();
		
		for (int i=0;i<keylist.size();i++){
			String key = (String)keylist.get(i);
			try {
				List list = search(key);
				
				retList.addAll(list);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		return retList;
	}
	private static List search(String query) throws Exception {
		List list = new ArrayList();
		JumblrClient client = new JumblrClient(tumblroauthconsumerkey, tumblrsecretkey);
		try {
			list = client.tagged(query);
			//ONLY FOR DEBUGGING, COMMENTED OUT.
			/*for (int i=0;i<list.size();i++){
				com.tumblr.jumblr.types.Post p = (com.tumblr.jumblr.types.Post) list
				.get(i);
				String tags = StrUtil.nonNull(p.getTags().toString());
				String authorid = p.getAuthorId();
				String blogname = StrUtil.nonNull(p.getBlogName());
				String posturl = StrUtil.nonNull(p.getPostUrl());
				//String sourcetitle = StrUtil.nonNull(p.getSourceTitle());
				org.jsoup.nodes.Document doc = null;
				try {
					doc = Jsoup.connect(posturl).get();
				} catch (Exception e1) {
					System.out.println(e1.getMessage());
				}
				String sourcetitle = "";
				if(doc!=null){
					try {
						sourcetitle = StrUtil.nonNull(doc.title());
					} catch (Exception e) {
						System.out.println(e.getMessage());
					}
				}
				String date = StrUtil.nonNull(p.getDateGMT());
				System.out.println(date);
				Date dt = StrUtil.getTumblrDate(date);
				System.out.println(dt);
				
			}*/
			//list = client.blogPosts(query);
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//System.out.println("#######################################");
	//	System.out.println("Got Tumblr search size: "+list.size());
		//System.out.println("#######################################");
		return list;
	}
	

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			List list = new ArrayList();
			//list.add("mozzarella");
			//list.add("Mayweather vs Pacman");
			//search(list);
			//directsearch("Mayweather vs Pacman");
			search("gazpacho");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
