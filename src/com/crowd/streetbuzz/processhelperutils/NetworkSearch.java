/**
 * 
 */
package com.crowd.streetbuzz.processhelperutils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import twitter4j.GeoLocation;
import twitter4j.Status;

import com.crowd.streetbuzz.common.Constants;
import com.crowd.streetbuzz.model.Voices;
import com.crowd.streetbuzzalgo.parser.TweetWithSentiments;
import com.crowd.streetbuzzalgo.utils.DataClean;
import com.crowd.streetbuzzalgo.vo.SearchVO;

/**
 * @author Atrijit
 *
 */
public class NetworkSearch implements Constants {

	/**
	 * 
	 */
	public NetworkSearch() {
		// TODO Auto-generated constructor stub
	}
	public static List searchTwitterCheck(List searchTermsList,List twitvlist) {
		List checklist = new ArrayList();
		/*for(int i=0;i<twitvlist.size();i++){
			Voices tv = (Voices) twitvlist.get(i);
			String posttext = tv.getPosttext();
			if(!checklist.contains(posttext)){
				posttext = posttext.toLowerCase();
				checklist.add(posttext);
			}
		}*/
		List retList = new ArrayList();
		TweetWithSentiments tWS = new TweetWithSentiments();
		int count = searchTermsList.size();
		for (int i = 0; i < count; i++) {
			String querystr = (String) searchTermsList.get(i);
			List result = new ArrayList();
			try {
				try {
					result = tWS.search(querystr);
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
				
				if (result != null) {
					for (int j = 0; j < result.size(); j++) {
						Status status = (Status) result.get(j);
						long statusid = status.getId();
						Long statusidLong = new Long(statusid);
						String url = TWITTERURL+statusidLong.toString();
						String line = status.getText();
						line = DataClean.htmlClean(line);
						/*String linelower = line.toLowerCase();
						if(checklist.contains(linelower)){
							continue;
						}*/
						Date dt = status.getCreatedAt();
						SearchVO svo = new SearchVO();
						svo.setCommentdate(dt);
						svo.setUrl(url);
						svo.setLocation("");
						svo.setSearchterm(querystr);
						svo.setMode(TWITTER);
						svo.setText(line);
						String commenter = status.getUser().getDescription();
						svo.setCommenter(commenter);
						svo.setSearchwithlocation(false);
						if (!retList.contains(svo)) {
							retList.add(svo);
						}

					}
				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return retList;
	}
	
	public static List searchTwitterCheck(List searchTermsList,
			List searchTermsListLocation,List twitvlist) {
		List checklist = new ArrayList();
		for(int i=0;i<twitvlist.size();i++){
			Voices tv = (Voices) twitvlist.get(i);
			String posttext = tv.getPosttext();
			if(!checklist.contains(posttext)){
				posttext = posttext.toLowerCase();
				checklist.add(posttext);
			}
		}
		List retList = new ArrayList();
		TweetWithSentiments tWS = new TweetWithSentiments();
		int count = searchTermsList.size();
		/*int num = 20;
		if (count > 4) {
			count = 4;
		}*/
		for (int i = 0; i < count; i++) {
			String querystr = (String) searchTermsList.get(i);
			List result = new ArrayList();
			try {
				try {
					result = tWS.search(querystr);
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
				
				if (result != null) {
					for (int j = 0; j < result.size(); j++) {
						Status status = (Status) result.get(j);
						long statusid = status.getId();
						Long statusidLong = new Long(statusid);
						String url = TWITTERURL+statusidLong.toString();
						String line = status.getText();
						line = DataClean.htmlClean(line);
						String linelower = line.toLowerCase();
						if(checklist.contains(linelower)){
							continue;
						}
						GeoLocation gl = status.getGeoLocation();
						Date dt = status.getCreatedAt();
						SearchVO svo = new SearchVO();
						svo.setCommentdate(dt);
						svo.setUrl(url);
						svo.setLocation("");
						svo.setSearchterm(querystr);
						svo.setMode(TWITTER);
						svo.setText(line);
						String commenter = status.getUser().getDescription();
						svo.setCommenter(commenter);
						svo.setSearchwithlocation(false);
						if (!retList.contains(svo)) {
							retList.add(svo);
						}

					}
				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		count = searchTermsListLocation.size();
		
		for (int i = 0; i < count; i++) {
			String querystr = (String) searchTermsList.get(i);
			List result = new ArrayList();
			try {
				try {
					result = tWS.search(querystr);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					continue;
				}
			
				if (result != null) {
					for (int j = 0; j < result.size(); j++) {
						Status status = (Status) result.get(j);
						long statusid = status.getId();
						Long statusidLong = new Long(statusid);
						String url = TWITTERURL+statusidLong.toString();
						String line = status.getText();
						GeoLocation gl = status.getGeoLocation();
						Date dt = status.getCreatedAt();
						SearchVO svo = new SearchVO();
						svo.setCommentdate(dt);
						svo.setUrl(url);
						svo.setLocation("");
						svo.setSearchterm(querystr);
						svo.setMode(TWITTER);
						svo.setText(line);
						String commenter = status.getUser().getDescription();
						svo.setCommenter(commenter);
						svo.setSearchwithlocation(true);
						if (!retList.contains(svo)) {
							retList.add(svo);
						}
					}
				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//No need to further check for includes and clean up the list
		
		return retList;
	}
	
	public static List searchTwitter(List searchTermsList,
			List searchTermsListLocation) {
		List retList = new ArrayList();
		TweetWithSentiments tWS = new TweetWithSentiments();
		int count = searchTermsList.size();
		/*int num = 20;
		if (count > 4) {
			count = 4;
		}*/
		for (int i = 0; i < count; i++) {
			String querystr = (String) searchTermsList.get(i);
			List result = new ArrayList();
			try {
				try {
					result = tWS.search(querystr);
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
				
				if (result != null) {
					for (int j = 0; j < result.size(); j++) {
						Status status = (Status) result.get(j);
						long statusid = status.getId();
						Long statusidLong = new Long(statusid);
						String url = TWITTERURL+statusidLong.toString();
						String line = status.getText();
						GeoLocation gl = status.getGeoLocation();
						Date dt = status.getCreatedAt();
						SearchVO svo = new SearchVO();
						svo.setCommentdate(dt);
						svo.setUrl(url);
						svo.setLocation("");
						svo.setSearchterm(querystr);
						svo.setMode(TWITTER);
						svo.setText(line);
						String commenter = status.getUser().getDescription();
						svo.setCommenter(commenter);
						svo.setSearchwithlocation(false);
						if (!retList.contains(svo)) {
							retList.add(svo);
						}

					}
				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		count = searchTermsListLocation.size();
		
		for (int i = 0; i < count; i++) {
			String querystr = (String) searchTermsList.get(i);
			List result = new ArrayList();
			try {
				try {
					result = tWS.search(querystr);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					continue;
				}
			
				if (result != null) {
					for (int j = 0; j < result.size(); j++) {
						Status status = (Status) result.get(j);
						long statusid = status.getId();
						Long statusidLong = new Long(statusid);
						String url = TWITTERURL+statusidLong.toString();
						String line = status.getText();
						GeoLocation gl = status.getGeoLocation();
						Date dt = status.getCreatedAt();
						SearchVO svo = new SearchVO();
						svo.setCommentdate(dt);
						svo.setUrl(url);
						svo.setLocation("");
						svo.setSearchterm(querystr);
						svo.setMode(TWITTER);
						svo.setText(line);
						String commenter = status.getUser().getDescription();
						svo.setCommenter(commenter);
						svo.setSearchwithlocation(true);
						if (!retList.contains(svo)) {
							retList.add(svo);
						}
					}
				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//No need to further check for includes and clean up the list
		
		return retList;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
