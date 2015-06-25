/**
 * 
 */
package com.crowd.streetbuzzalgo.socialplugin.facebook;

import java.util.HashMap;
import java.util.List;

import com.crowd.streetbuzzalgo.constants.SystemConstants;
import com.restfb.Connection;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.types.CategorizedFacebookType;
import com.restfb.types.Comment;
import com.restfb.types.FacebookType;
import com.restfb.types.Group;
import com.restfb.types.Post;
import com.restfb.types.Post.Comments;

/**
 * @author Atrijit
 * 
 */
public class FacebookCalls implements SystemConstants {
	FacebookClient facebookClient = null;
	String fbtoken = "";
	/**
	 * 
	 */
	public FacebookCalls() {
		
	}
	public FacebookCalls(FacebookClient facebookClient) {
		this.facebookClient = facebookClient;
	}
	
	public FacebookCalls(String fbtoken) {
		this.fbtoken = fbtoken;
	}

	private  FacebookClient initialise() {
		/*FacebookClient facebookClient = new DefaultFacebookClient(
				FB_GRAPH_ACCESS_TOKEN);*/
		FacebookClient facebookClient = new DefaultFacebookClient(
				fbtoken);
		return facebookClient;
	}

	public List getMyPosts() {
		FacebookClient facebookClient = initialise();
		Connection<Post> myFeed = facebookClient.fetchConnection("me/feed",
				Post.class);
		List postList = myFeed.getData();
		
		return postList;

	}

		
	public List getMyGroups() {
		FacebookClient facebookClient = initialise();
		com.restfb.Connection<Group> group = facebookClient.fetchConnection(
				"me/groups", Group.class);
		List<Group> gList = group.getData();
		return gList;
	}

	public String postToMyWall(String message) {
		FacebookClient facebookClient = initialise();
		FacebookType publishMessageResponse = facebookClient.publish("me/feed",
				FacebookType.class, Parameter.with("message", message));
		String idpublished = publishMessageResponse.getId();
		return idpublished;
	}

	public void postComment(String idpublished) {
		FacebookClient facebookClient = initialise();
		facebookClient.publish(idpublished + "/comments", String.class,
				Parameter.with("message", "Your weird comment here"));
	}

	public String postToGroup(String groupId, String message) {
		FacebookClient facebookClient = initialise();
		FacebookType publishtoGroupResponse = facebookClient.publish(groupId
				+ "/feed", FacebookType.class, Parameter.with("message",
				message));
		String idgrouppublished = publishtoGroupResponse.getId();
		return idgrouppublished;
	}

	public String postLinkToGroup(String groupId, String link) {
		FacebookClient facebookClient = initialise();
		FacebookType publishtoGroupResponse = facebookClient.publish(groupId
				+ "/feed", FacebookType.class, Parameter.with("link", link));
		String idgrouppublished = publishtoGroupResponse.getId();
		return idgrouppublished;
	}

	public List getGroupPosts(String groupId) {
		FacebookClient facebookClient = initialise();
		Connection<Post> groupFeed = facebookClient.fetchConnection(groupId
				+ "/feed", Post.class);
		List postList = groupFeed.getData();

		return postList;
	}
	
	public HashMap getAllGroupPosts(){
		HashMap map = new HashMap();
		List groups = this.getMyGroups();
		for (int i=0;i<groups.size();i++){
			Group G = (Group)groups.get(i);
			String groupId = G.getId();
			String groupName = G.getName();
			List groupPosts = this.getGroupPosts(groupId);
			map.put(G, groupPosts);
		}
		return map;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		FacebookCalls fbc = new FacebookCalls();
	}

}
