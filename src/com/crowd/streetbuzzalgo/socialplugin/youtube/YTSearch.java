/**
 * 
 */
package com.crowd.streetbuzzalgo.socialplugin.youtube;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.crowd.streetbuzz.model.Voices;
import com.crowd.streetbuzzalgo.constants.SystemConstants;
import com.crowd.streetbuzzalgo.scrapeandpost.YoutubeCommentReader;
import com.crowd.streetbuzzalgo.vo.YoutubeVO;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.ResourceId;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.Thumbnail;

/**
 * @author Atrijit
 *
 */
public class YTSearch implements SystemConstants{
	private static YouTube youtube;
	private static final long NUMBER_OF_VIDEOS_RETURNED = 25;
	/**
	 * 
	 */
	public YTSearch() {
		
	}
	
	public static List searchYoutubeCheck(String query, List ytvlist){
		List checklist = new ArrayList();
		for (int i=0;i<ytvlist.size();i++){
			Voices ytv = (Voices) ytvlist.get(i);
			String url = ytv.getSourcelink();
			if(checklist.contains(url)){
				checklist.add(url);
			}
		}
		List retList = new ArrayList();
		try{
			youtube = new YouTube.Builder(new NetHttpTransport(), new JacksonFactory(), new HttpRequestInitializer(){
	            public void initialize(HttpRequest request) throws IOException {
	            }
	        }).setApplicationName("youtube-cmdline-search-sample").build();
			YouTube.Search.List search = youtube.search().list("id,snippet");
			String apiKey = YT_DEV_KEY;
			search.setKey(apiKey);
            search.setQ(query);
            search.setType("video");
            search.setMaxResults(NUMBER_OF_VIDEOS_RETURNED);
            SearchListResponse searchResponse = search.execute();
            List<SearchResult> searchResultList = searchResponse.getItems();
            if (searchResultList != null) {
            	
            	Iterator <SearchResult> iteratorSearchResults = searchResultList.iterator();
            	while(iteratorSearchResults.hasNext()){
            		SearchResult singleVideo = iteratorSearchResults.next();
                    ResourceId rId = singleVideo.getId();
                    if (rId.getKind().equals("youtube#video")) {
                    	Thumbnail thumbnail = singleVideo.getSnippet().getThumbnails().getDefault();
                    	String videoId = rId.getVideoId();
                    	String title = singleVideo.getSnippet().getTitle();
                    	String thumbnailurl = thumbnail.getUrl();
                    	String description = singleVideo.getSnippet().getDescription();
                    	DateTime publishdatetime = singleVideo.getSnippet().getPublishedAt();
                    	
                    	String youtubeurl = "https://www.youtube.com/watch?v="+videoId;
                    	if(checklist.contains(youtubeurl)){
                    		continue;
                    	}
                    	YoutubeVO ytvo = new YoutubeVO();
                    	ytvo.setTitle(title);
                    	ytvo.setThumbnailurl(thumbnailurl);
                    	ytvo.setVideoId(videoId);
                    	ytvo.setYoutubeurl(youtubeurl);
                    	ytvo.setDescription(description);
                    	ytvo.setPublishdatetime(publishdatetime);
                    	List crawlerList = YoutubeCommentReader.readComments(videoId);
                    	ytvo.setCrawlerList(crawlerList);
                    	if(crawlerList!=null && crawlerList.size()>0){
                    		retList.add(ytvo);
                    	}
                    	
                    	
                    }
            	}
            }
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return retList;
	}
	
	public static List searchYoutube(String query){
		List retList = new ArrayList();
		try{
			youtube = new YouTube.Builder(new NetHttpTransport(), new JacksonFactory(), new HttpRequestInitializer(){
	            public void initialize(HttpRequest request) throws IOException {
	            }
	        }).setApplicationName("youtube-cmdline-search-sample").build();
			YouTube.Search.List search = youtube.search().list("id,snippet");
			String apiKey = YT_DEV_KEY;
			search.setKey(apiKey);
            search.setQ(query);
            search.setType("video");
            search.setMaxResults(NUMBER_OF_VIDEOS_RETURNED);
            SearchListResponse searchResponse = search.execute();
            List<SearchResult> searchResultList = searchResponse.getItems();
            if (searchResultList != null) {
            	
            	Iterator <SearchResult> iteratorSearchResults = searchResultList.iterator();
            	int counter = 0;
            	while(iteratorSearchResults.hasNext()){
            		counter = counter+1;
            		           		
            		SearchResult singleVideo = iteratorSearchResults.next();
                    ResourceId rId = singleVideo.getId();
                    if (rId.getKind().equals("youtube#video")) {
                    	Thumbnail thumbnail = singleVideo.getSnippet().getThumbnails().getMaxres();
                    	
                    	String thumbnailurl = "";
                    	if(thumbnail!=null){
                    		thumbnailurl = thumbnail.getUrl();
                    	}else{
                    		thumbnail = singleVideo.getSnippet().getThumbnails().getHigh();
                    		
                    	}
                    	if(thumbnail!=null){
                    		thumbnailurl = thumbnail.getUrl();
                    	}else{
                    		thumbnail = singleVideo.getSnippet().getThumbnails().getDefault();
                    		thumbnailurl = thumbnail.getUrl();
                    	}
                    		
                    	
                    	//
                    	String videoId = rId.getVideoId();
                    	String title = singleVideo.getSnippet().getTitle();
                    	                  	
                    	String description = singleVideo.getSnippet().getDescription();
                    	DateTime publishdatetime = singleVideo.getSnippet().getPublishedAt();
                    	
                    	String youtubeurl = "https://www.youtube.com/watch?v="+videoId;
                    	YoutubeVO ytvo = new YoutubeVO();
                    	ytvo.setTitle(title);
                    	ytvo.setThumbnailurl(thumbnailurl);
                    	ytvo.setVideoId(videoId);
                    	ytvo.setYoutubeurl(youtubeurl);
                    	ytvo.setDescription(description);
                    	ytvo.setPublishdatetime(publishdatetime);
                    	List crawlerList = new ArrayList();
                    	if(counter<11){
                    	//	System.out.println("In here ...counter: "+counter);
                    		 try {
								crawlerList = YoutubeCommentReader.readComments(videoId);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								System.out.println(e.getMessage());
							}
                    		 
                    	}
                    	
                    	ytvo.setCrawlerList(crawlerList);
                    //	System.out.println("Adding ...counter "+counter);
                    	retList.add(ytvo);
                    	/*if(crawlerList!=null && crawlerList.size()>0){
                    		retList.add(ytvo);
                    	}*/
                    	
                    	
                    }
                    
            	}
            }
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return retList;
	}
	
	public void search(String query){
		System.out.println("In here");
		try{
			youtube = new YouTube.Builder(new NetHttpTransport(), new JacksonFactory(), new HttpRequestInitializer(){
                public void initialize(HttpRequest request) throws IOException {
                }
            }).setApplicationName("youtube-cmdline-search-sample").build();
			System.out.println("youtube "+youtube);
			
			YouTube.Search.List search = youtube.search().list("id,snippet");
			String apiKey = YT_DEV_KEY;
			search.setKey(apiKey);
            search.setQ(query);
            search.setType("video");
            search.setMaxResults(NUMBER_OF_VIDEOS_RETURNED);
            System.out.println("About to run search");
            SearchListResponse searchResponse = search.execute();
            System.out.println("searchResponse "+searchResponse);
            List<SearchResult> searchResultList = searchResponse.getItems();
            if(searchResultList==null){
            	System.out.println("got null list");
            }
            if (searchResultList != null) {
            	System.out.println("not null");
                prettyPrint(searchResultList.iterator(), query);
            }
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
	}
	
	private static void prettyPrint(Iterator<SearchResult> iteratorSearchResults, String query) {

        System.out.println("\n=============================================================");
        System.out.println(
                "   First " + NUMBER_OF_VIDEOS_RETURNED + " videos for search on \"" + query + "\".");
        System.out.println("=============================================================\n");

        if (!iteratorSearchResults.hasNext()) {
            System.out.println(" There aren't any results for your query.");
        }

        while (iteratorSearchResults.hasNext()) {

            SearchResult singleVideo = iteratorSearchResults.next();
            ResourceId rId = singleVideo.getId();

            // Confirm that the result represents a video. Otherwise, the
            // item will not contain a video ID.
            if (rId.getKind().equals("youtube#video")) {
                Thumbnail thumbnail = singleVideo.getSnippet().getThumbnails().getDefault();
                
              //  System.out.println(" Video Id" + rId.getVideoId());
             //   System.out.println(" Title: " + singleVideo.getSnippet().getTitle());
             //   System.out.println(" Thumbnail: " + thumbnail.getUrl());
             //   System.out.println("\n-------------------------------------------------------------\n");
            }
        }
    }

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		YTSearch yts = new YTSearch();
		String query = "Bruce Lee";
		long start = System.currentTimeMillis();
		yts.searchYoutube(query);
		long end = System.currentTimeMillis();
		long gap = end - start;
		System.out.println(gap/1000+" secs.");

	}

}
