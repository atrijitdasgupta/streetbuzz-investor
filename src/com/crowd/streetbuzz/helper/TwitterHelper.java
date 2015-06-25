/**
 * 
 */
package com.crowd.streetbuzz.helper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import twitter4j.Status;

import com.crowd.streetbuzz.common.Constants;
import com.crowd.streetbuzzalgo.parser.TweetWithSentiments;
import com.crowd.streetbuzzalgo.utils.StrUtil;
import com.crowd.streetbuzzalgo.vo.SearchVO;

import edu.stanford.nlp.pipeline.StanfordCoreNLP;

/**
 * @author Atrijit
 * 
 */
public class TwitterHelper implements Constants{

	/**
	 * 
	 */
	public TwitterHelper() {
		// TODO Auto-generated constructor stub
	}

	public static List cleanSearchList(List twitterList,List synonymList) {
		System.out.println("Inside Twitter Helper clean");
		List cleantwitterList = new ArrayList();
		for(int i=0;i<twitterList.size();i++){
			Status svo = (Status)twitterList.get(i);
			String text = StrUtil.nonNull(svo.getText().trim());
			if(StrUtil.containsOrNot(text, synonymList)){
				cleantwitterList.add(svo);
			}
			
		}
		return cleantwitterList;
	}
	
	public static List fillTwitterList(StanfordCoreNLP pipeline){
		List tList = new ArrayList();
		tList.add("Random Common Sentences");
		tList.add("He speaks English fluently.");
		tList.add("Do you think that you would enjoy being famous?");
		tList.add("Tom became good friends with the elevator operator in their hotel.");
		tList.add("Will you let me know when he comes?");
		tList.add("I should not have said that.");
		tList.add("The pain is getting worse.");
		tList.add("When did he say he would come?");
		tList.add("Give me the same, please.");
		tList.add("Would you teach me?");
		tList.add("It's now or never.");
		tList.add("What an interesting book!");
		tList.add("I've never been to Paris.");
		tList.add("Yes. The rate is 55 dollars.");
		tList.add("She was only too glad to help us.");
		tList.add("Do you have any tickets for today's performance?");
		tList.add("The moon is already out.");
		tList.add("I feel happy.");
		tList.add("Well, let's go.");
		tList.add("Tom is a good cook.");
		tList.add("This car is like new.");
		tList.add("But they speak it in school, right? Maybe they just didn't like me so they didn't want to talk to me.");
		tList.add("Bill has a lot of original ideas.");
		tList.add("Write your name in capitals.");
		tList.add("Come along with us.");
		tList.add("Tom looks very happy.");
		tList.add("Do you think you would ever consider suicide?");
		tList.add("He stuck out his tongue at his teacher.");
		tList.add("I want to emphasize this point in particular.");
		tList.add("I always thought that Shirley and Alan would get together.");
		tList.add("Is she at home?");
		
		List svoList = new ArrayList();
		for(int i=0;i<tList.size();i++){
			SearchVO svo = new SearchVO();
			String temp = (String)tList.get(i);
			//System.out.println("temp: "+temp);
			svo.setText(temp);
			svo.setMode(TWITTER);
			svo.setCommentdate(new Date());
			svoList.add(svo);
		}
		List sentimentScoredTwitList = new ArrayList();
		try {
			sentimentScoredTwitList = new TweetWithSentiments().analyseTweetSentiment(svoList,pipeline,null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sentimentScoredTwitList;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//fillTwitterList();

	}

}
