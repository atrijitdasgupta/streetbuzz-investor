/**
 * 
 */
package com.crowd.streetbuzzalgo.socialplugin.facebook;

import java.util.List;

import com.crowd.streetbuzzalgo.constants.SystemConstants;
import com.restfb.Connection;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.types.FacebookType;
import com.restfb.types.Group;
import com.restfb.types.Post;
import com.restfb.types.User;

/**
 * @author Atrijit
 * 
 */
public class FBAccess implements SystemConstants {

	/**
	 * 
	 */
	public FBAccess() {
		run();
	}

	public void run() {
		FacebookClient facebookClient = new DefaultFacebookClient(
				FB_GRAPH_ACCESS_TOKEN);
		User user = facebookClient.fetchObject("me", User.class);
		System.out.println("User name: " + user.getName());

		Connection<User> myFriends = facebookClient.fetchConnection(
				"me/friends", User.class);
		Connection<Post> myFeed = facebookClient.fetchConnection("me/feed",
				Post.class);

		System.out
				.println("Count of my friends: " + myFriends.getData().size());
		System.out.println("First item in my feed: " + myFeed.getData().get(0));

		Post postsingle = facebookClient.fetchObject(
				"100001025660215_827343127309868", Post.class, Parameter.with(
						"fields",
						"from,to,likes.summary(true),comments.summary(true)"));

		System.out.println("Likes count: " + postsingle.getLikesCount());
		System.out.println("Likes count (from Likes): "
				+ postsingle.getLikes().getCount());
		// System.out.println("Comments count: " +
		// postsingle.getComments().getTotalCount());

		FacebookType publishMessageResponse = facebookClient.publish("me/feed",
				FacebookType.class, Parameter.with("message",
						"fdfd New RestFb weird comment Test Post"));
		String idpublished = publishMessageResponse.getId();
		System.out.println("Published message ID: " + idpublished);

		facebookClient.publish(idpublished + "/comments", String.class,
				Parameter.with("message", "Your weird comment here"));

		/*
		 * FacebookType publishPhotoResponse =
		 * facebookClient.publish("me/photos", FacebookType.class,
		 * BinaryAttachment.with("C:/Mywork/MIH/makeithappen/images/corbettlarge.jpg",
		 * getClass().getResourceAsStream("/corbettlarge.jpg")),
		 * Parameter.with("message", "Test Large cat"));
		 * 
		 * System.out.println("Published photo ID: " +
		 * publishPhotoResponse.getId());
		 */

		/*
		 * for (List<Post> myFeedConnectionPage : myFeed){ for (Post post :
		 * myFeedConnectionPage){ System.out.println("Post: " + post);
		 * System.out.println(post.getId()); }
		 *  }
		 */

		Connection<Post> publicSearch = facebookClient.fetchConnection(
				"search", Post.class, Parameter.with("q", "robert plant"),
				Parameter.with("type", "post"));
		Connection<User> targetedSearch = facebookClient.fetchConnection(
				"me/home", User.class, Parameter.with("q", "Jovelean"),
				Parameter.with("type", "user"));
		System.out.println("Public search: "
				+ publicSearch.getData().get(0).getMessage());
		System.out.println("Posts on my wall by friends named Mark: "
				+ targetedSearch.getData().size());

		com.restfb.Connection<Group> group = facebookClient.fetchConnection(
				"me/groups", Group.class);
		List<Group> g = group.getData();
		int len = g.size();
		for (int i = 0; i < len; i++) {
			Group G = g.get(i);
	        String groupName = G.getName();
	        String groupId = G.getId();
	        System.out.println(groupName+" "+groupId);
	        
		}
		//posting to group
		FacebookType publishtoGroupResponse = facebookClient.publish("726791370700280/feed",
				FacebookType.class, Parameter.with("message",
						"Test post on group"));
		String idgrouppublished = publishtoGroupResponse.getId();
		System.out.println("Published group message ID: " + idgrouppublished);
		//posting link to group
		FacebookType publishtoGroupResponse1 = facebookClient.publish("726791370700280/feed",
				FacebookType.class, Parameter.with("link",
						"http://www.yahoo.com"));
		String idgrouppublished1 = publishtoGroupResponse1.getId();
		System.out.println("Published group message ID: " + idgrouppublished1);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		FBAccess fb = new FBAccess();
	}

}
