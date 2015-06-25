/**
 * 
 */
package com.crowd.streetbuzz.helper;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.crowd.streetbuzzalgo.parser.TweetWithSentiments;
import com.crowd.streetbuzzalgo.utils.StrUtil;
import com.crowd.streetbuzzalgo.vo.SearchVO;
import com.restfb.types.CategorizedFacebookType;
import com.restfb.types.Comment;
import com.restfb.types.Group;
import com.restfb.types.Post;
import com.restfb.types.Post.Comments;

import edu.stanford.nlp.pipeline.StanfordCoreNLP;

/**
 * @author Atrijit
 * 
 */
public class FBHelper {

	/**
	 * 
	 */
	public FBHelper() {
		// TODO Auto-generated constructor stub
	}

	public static List cleanMyPosts(List mypostlist, List synonymList) {
		List newpostlist = new ArrayList();
		for (int i = 0; i < mypostlist.size(); i++) {
			Post post = (Post) mypostlist.get(i);
			String msg = StrUtil.nonNull(post.getMessage());
			if (!"".equalsIgnoreCase(msg)) {
				System.out.println("msg");
			}

			if (StrUtil.containsOrNot(msg, synonymList)) {
				newpostlist.add(post);
			} else {
				Comments comments = post.getComments();
				List commentsList = new ArrayList();
				if (comments != null) {
					commentsList = comments.getData();
				}

				for (int j = 0; j < commentsList.size(); j++) {
					Comment comment = (Comment) commentsList.get(j);
					String cmsg = StrUtil.nonNull(comment.getMessage());
					if (StrUtil.containsOrNot(cmsg, synonymList)) {
						newpostlist.add(post);
						break;
					}
				}
			}
		}
		return newpostlist;
	}

	public static Map cleanGroupPosts(HashMap grouppostmap, List synonymList) {
		Map newgroupmap = new HashMap();
		Set keySet = grouppostmap.keySet();
		Iterator it = keySet.iterator();
		while (it.hasNext()) {
			Group group = (Group) it.next();
			String gname = StrUtil.nonNull(group.getName());
			String gdesc = StrUtil.nonNull(group.getDescription());
			if (StrUtil.justContains((gname + gdesc), synonymList)) {
				List grouppostlist = (List) grouppostmap.get(group);
				newgroupmap.put(group, grouppostlist);
			}
		}
		return newgroupmap;
	}

	public static Map convertFBMypost(List mypostlist, List synonymList,StanfordCoreNLP pipeline) {
		Map newMap = new HashMap();
		TweetWithSentiments tws = new TweetWithSentiments();
		for (int i = 0; i < mypostlist.size(); i++) {
			Post post = (Post) mypostlist.get(i);
			SearchVO svopost = new SearchVO();
			String text = StrUtil.nonNull(post.getMessage());
			svopost.setText(text);
			Date posttime = post.getCreatedTime();
			if (posttime != null) {
				svopost.setCommentdate(posttime);
			} else {
				svopost.setCommentdate(new Date());
			}
			String postid = post.getId();
			svopost.setId(postid);

			CategorizedFacebookType cftpost = post.getFrom();
			String name = "";
			String nameid = "";
			if (cftpost != null) {
				name = StrUtil.nonNull(cftpost.getName());
				nameid = StrUtil.nonNull(cftpost.getId());
				svopost.setCommenter(name);
				svopost.setCommenterid(nameid);
			}
			// analyse sentiment
		//	svopost = tws.analyseSentimentforSVO(svopost,pipeline);
			// Now get the comments
			List commentsHoldingList = new ArrayList();
			Comments comments = post.getComments();
			if (comments != null) {
				List commentsList = comments.getData();
				for (int j = 0; j < commentsList.size(); j++) {
					Comment comment = (Comment) commentsList.get(j);
					SearchVO svocomment = new SearchVO();
					String commenttxt = StrUtil.nonNull(comment.getMessage());
					if (StrUtil.containsOrNot(commenttxt, synonymList)) {
						svocomment.setText(commenttxt);
						String commentid = StrUtil.nonNull(comment.getId());
						svocomment.setId(commentid);
						Date commentposttime = comment.getCreatedTime();
						if (commentposttime != null) {
							svocomment.setCommentdate(commentposttime);
						} else {
							svocomment.setCommentdate(new Date());
						}
						CategorizedFacebookType cftcomment = post.getFrom();
						String commenter = "";
						String commenterid = "";
						if (cftcomment != null) {
							commenter = StrUtil.nonNull(cftcomment.getName());
							commenterid = StrUtil.nonNull(cftcomment.getId());
							svocomment.setCommenter(commenter);
							svocomment.setCommenterid(commenterid);
						}
						// analyse sentiment
					//	svocomment = tws.analyseSentimentforSVO(svocomment,pipeline);
						commentsHoldingList.add(svocomment);
					}

				}
			}
			newMap.put(svopost, commentsHoldingList);
		}
		return newMap;
	}

	public static Map convertFBGrouppost(Map grouppostmap,List synonymList,StanfordCoreNLP pipeline) {
		Map newMap = new HashMap();
		TweetWithSentiments tws = new TweetWithSentiments();
		Set keySet = grouppostmap.keySet();
		Iterator it = keySet.iterator();
		while(it.hasNext()){
			
			Group group = (Group) it.next();
			List grouppostlist = (List) grouppostmap.get(group);
			for(int i=0;i<grouppostlist.size();i++){
				Map postMap = new HashMap();
				Post post = (Post)grouppostlist.get(i);
				SearchVO svopost = new SearchVO();
				String text = StrUtil.nonNull(post.getMessage());
				svopost.setText(text);
				Date posttime = post.getCreatedTime();
				if (posttime != null) {
					svopost.setCommentdate(posttime);
				} else {
					svopost.setCommentdate(new Date());
				}
				String postid = post.getId();
				svopost.setId(postid);

				CategorizedFacebookType cftpost = post.getFrom();
				String name = "";
				String nameid = "";
				if (cftpost != null) {
					name = StrUtil.nonNull(cftpost.getName());
					nameid = StrUtil.nonNull(cftpost.getId());
					svopost.setCommenter(name);
					svopost.setCommenterid(nameid);
				}
				// analyse sentiment
			//	svopost = tws.analyseSentimentforSVO(svopost,pipeline);
				// Now get the comments
				List commentsHoldingList = new ArrayList();
				Comments comments = post.getComments();
				if (comments != null) {
					List commentsList = comments.getData();
					for (int j = 0; j < commentsList.size(); j++) {
						Comment comment = (Comment) commentsList.get(j);
						SearchVO svocomment = new SearchVO();
						String commenttxt = StrUtil.nonNull(comment.getMessage());
						if (StrUtil.containsOrNot(commenttxt, synonymList)) {
							svocomment.setText(commenttxt);
							String commentid = StrUtil.nonNull(comment.getId());
							svocomment.setId(commentid);
							Date commentposttime = comment.getCreatedTime();
							if (commentposttime != null) {
								svocomment.setCommentdate(commentposttime);
							} else {
								svocomment.setCommentdate(new Date());
							}
							CategorizedFacebookType cftcomment = post.getFrom();
							String commenter = "";
							String commenterid = "";
							if (cftcomment != null) {
								commenter = StrUtil.nonNull(cftcomment.getName());
								commenterid = StrUtil.nonNull(cftcomment.getId());
								svocomment.setCommenter(commenter);
								svocomment.setCommenterid(commenterid);
							}
							// analyse sentiment
							//svocomment = tws.analyseSentimentforSVO(svocomment,pipeline);
							commentsHoldingList.add(svocomment);
						}
					//	postMap.put(svopost, svocomment);
						postMap.put(svopost, commentsHoldingList);
					}
				}
				newMap.put(group, postMap);
			}
			
		}
		return newMap;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
