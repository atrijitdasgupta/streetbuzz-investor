/**
 * 
 */
package com.crowd.streetbuzzalgo.instagram;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jinstagram.Instagram;
import org.jinstagram.entity.comments.CommentData;
import org.jinstagram.entity.common.Caption;
import org.jinstagram.entity.common.Comments;
import org.jinstagram.entity.common.Images;
import org.jinstagram.entity.common.User;
import org.jinstagram.entity.tags.TagMediaFeed;
import org.jinstagram.entity.users.feed.MediaFeedData;

import com.crowd.streetbuzzalgo.constants.SystemConstants;
import com.crowd.streetbuzzalgo.utils.StrUtil;

/**
 * @author Atrijit
 *
 */
public class InstagramSearch implements SystemConstants{

	/**
	 * 
	 */
	public InstagramSearch() {
		// TODO Auto-generated constructor stub
	}
	
	public static List search(List keylist){
		List retList = new ArrayList();
		
		for (int i=0;i<keylist.size();i++){
			String key = (String)keylist.get(i);
			key = key.replaceAll(" ", "");
			key = key.trim();
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
	
	private static List search(String query)throws Exception{
		List list = new ArrayList();
		query = URLEncoder.encode(query,"UTF-8");
		Instagram inst = new Instagram(instagramclientid);
		TagMediaFeed tmf = inst.getRecentMediaTags(query);
		list = tmf.getData();
		for (int i=0;i<list.size();i++){
			MediaFeedData mfd = (MediaFeedData)list.get(i);
			/*System.out.println(mfd.getLink());
			System.out.println(mfd.getType());
			System.out.println(mfd.getCaption());
			System.out.println(mfd.getCreatedTime());*/
		//	System.out.println("thumb: "+mfd.getImages().getThumbnail().getImageUrl());
			Comments comments = mfd.getComments();
			Images images = mfd.getImages();
		//	System.out.println("link: "+mfd.getLink());
			String ctime = mfd.getCreatedTime();
			long ctimelong =  0;
			if(!"".equalsIgnoreCase(ctime)){
				ctimelong = new Long(ctime).longValue();
			}
			Date dt = new Date(ctimelong*1000);
			System.out.println(dt);
			
		//	System.out.println(ctimelong);
			
		//	System.out.println("ctime: "+ctime);
			Caption caption = mfd.getCaption();
		//	System.out.println(caption.getText());
			User user = mfd.getUser();
			String name = user.getFullName();
			String thumbnail = "";
			
			try {
				thumbnail = StrUtil.nonNull(images.getStandardResolution().getImageUrl());
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
			List commentsList = comments.getComments();
			for (int j=0;j<commentsList.size();j++){
				CommentData cd = (CommentData)commentsList.get(j);
				String text = cd.getText();
				String createdtime = cd.getCreatedTime();
				System.out.println("createdtime "+createdtime);
				String cname = cd.getCommentFrom().getUsername();
				String pic = cd.getCommentFrom().getProfilePicture();
				System.out.println("pic: "+pic);
			}
			
		//	System.out.println("*****************************");
			
		}
		
		return list;
	}
	

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			search("Inception");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
