/**
 * 
 */
package com.crowd.streetbuzz.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import com.crowd.streetbuzz.json.JsonComments;
import com.crowd.streetbuzz.json.JsonConversation;
import com.crowd.streetbuzz.json.JsonConversationCard;
import com.crowd.streetbuzz.json.JsonHeader;
import com.crowd.streetbuzz.json.JsonSearch;
import com.crowd.streetbuzz.json.JsonSearchCard;
import com.crowd.streetbuzz.json.JsonSubComments;
import com.crowd.streetbuzz.model.Voices;
import com.crowd.streetbuzz.model.VoicesDetails;
import com.crowd.streetbuzzalgo.faroosearch.vo.FarooResult;
import com.google.gson.Gson;

/**
 * @author Atrijit
 * 
 */
public class JsonTest {

	/**
	 * 
	 */
	public JsonTest() {
		// TODO Auto-generated constructor stub
	}
	
	public static void main(String[] args) {
		
		Voices v1 = new Voices();
		v1.setAuthor("author1");
		v1.setFbgroupid("fbgroupid1");
		v1.setAvatar("avatar");
		v1.setCardid(new Long (123));
		v1.setCardtype("C");
		v1.setCarduniqueid("811611151");
		v1.setCommentscount(new Long(23));
		v1.setExtcommentscount(new Long(12));
		v1.setExtlikescount(new Long(22));
		v1.setExtviewscount(new Long(24));
		v1.setFbgroupid("121212121");
		v1.setId(new Long(211));
		v1.setLikescount(new Long(97));
		v1.setPositivephrase("great");
		v1.setPostauthorid("747");
		v1.setPostid("323");
		v1.setPosttext("great coffee in Pune");
		v1.setPosttextauthor("Sam");
		v1.setSentimentrating("positive");
		v1.setSentimentscore(new Long(8));
		v1.setSource("FB");
		v1.setSourcelink("http://www.google.com");
		v1.setThumbsdowncount(new Long(2));
		v1.setThumbsupcount(new Long(9));
		v1.setUniquevoiceid("828223823238");
		v1.setUserid(new Long(22));
		v1.setVoicesdate(new Date());
		v1.setVoicetype("POST");
		
		Voices v2 = new Voices();
		v2.setAuthor("author2");
		v2.setFbgroupid("fbgroupid2");
		v2.setCarduniqueid("811333151");
		v2.setCommentscount(new Long(43));
		v2.setExtcommentscount(new Long(112));
		v2.setExtlikescount(new Long(212));
		v2.setExtviewscount(new Long(424));
		v2.setFbgroupid("12121654421");
		v2.setId(new Long(19));
		v2.setLikescount(new Long(73));
		v2.setPositivephrase("bad");
		v2.setPostauthorid("737");
		v2.setPostid("329");
		v2.setPosttext("great coffee in Pune");
		v2.setPosttextauthor("Pam");
		v2.setSentimentrating("negative");
		v2.setSentimentscore(new Long(9));
		v2.setSource("FB");
		v2.setSourcelink("http://www.google.com");
		v2.setThumbsdowncount(new Long(3));
		v2.setThumbsupcount(new Long(10));
		v2.setUniquevoiceid("928223823231");
		v2.setUserid(new Long(33));
		v2.setVoicesdate(new Date());
		v2.setVoicetype("POST");
		
		List v1List = new ArrayList();
		List v2List = new ArrayList();
		
		VoicesDetails vd1 = new VoicesDetails();
		vd1.setPosttext("text1");
		vd1.setNegativephrase("negativephrase1");
		vd1.setNeutralphrase("neutralphrase1");
		vd1.setPositivephrase("positivephrase1");
		vd1.setPostauthorid("postauthorid1");
		vd1.setPosttext("posttext1");
		vd1.setPosttextauthor("posttextauthor1");
		vd1.setSentimentrating("sentimentrating1");
		vd1.setSentimentscore(new Long(3));
		vd1.setVoicedate(new Date());
		vd1.setVoicesid(new Long(211));
		
		VoicesDetails vd2 = new VoicesDetails();
		vd2.setPosttext("text2");
		vd2.setNegativephrase("negativephrase2");
		vd2.setNeutralphrase("neutralphrase2");
		vd2.setPositivephrase("positivephrase2");
		vd2.setPostauthorid("postauthorid2");
		vd2.setPosttext("posttext2");
		vd2.setPosttextauthor("posttextauthor2");
		vd2.setSentimentrating("sentimentrating2");
		vd2.setSentimentscore(new Long(4));
		vd2.setVoicedate(new Date());
		vd2.setVoicesid(new Long(211));
		
		VoicesDetails vd3 = new VoicesDetails();
		vd3.setPosttext("text3");
		vd3.setNegativephrase("negativephrase3");
		vd3.setNeutralphrase("neutralphrase3");
		vd3.setPositivephrase("positivephrase3");
		vd3.setPostauthorid("postauthorid3");
		vd3.setPosttext("posttext3");
		vd3.setPosttextauthor("posttextauthor3");
		vd3.setSentimentrating("sentimentrating3");
		vd3.setSentimentscore(new Long(5));
		vd3.setVoicedate(new Date());
		vd3.setVoicesid(new Long(19));
		
		VoicesDetails vd4 = new VoicesDetails();
		vd4.setPosttext("text4");
		vd4.setNegativephrase("negativephrase4");
		vd4.setNeutralphrase("neutralphrase4");
		vd4.setPositivephrase("positivephrase4");
		vd4.setPostauthorid("postauthorid4");
		vd4.setPosttext("posttext4");
		vd4.setPosttextauthor("posttextauthor4");
		vd4.setSentimentrating("sentimentrating4");
		vd4.setSentimentscore(new Long(6));
		vd4.setVoicedate(new Date());
		vd4.setVoicesid(new Long(19));
		
		v1List.add(vd1);
		v1List.add(vd2);
		
		v2List.add(vd3);
		v2List.add(vd4);
		
		v1.setComments(v1List);
		v2.setComments(v2List);
		
		Gson gson = new Gson();
		
		StringBuffer sbfr = new StringBuffer();
		String temp = gson.toJson(v1);
		sbfr.append(temp);
		/*temp = gson.toJson(v1List);
		sbfr.append(temp);*/
		temp = gson.toJson(v2);
		sbfr.append(temp);
		/*temp = gson.toJson(v2List);
		sbfr.append(temp);*/
		
		System.out.println(sbfr.toString());
	}

	public static void main_old(String[] args) {
		String header = "{'userid':1,'socialnetwork':'F','processid':'2.3','cardid':0,'cardtype':'','voiceid':'','source':'','lastupdate':'','afterid':0,'latitude':'','longitude':''}";
		Gson gson = new Gson();
		JsonHeader jh = gson.fromJson(header, JsonHeader.class);
		System.out.println(jh);
		System.out
				.println(jh.getAfterid() + ":" + jh.getCardid() + ":"
						+ jh.getCardtype() + ":" + jh.getLastupdate() + ":"
						+ jh.getLatitude() + ":" + jh.getLongitude() + ":"
						+ jh.getProcessid() + ":" + jh.getSocialnetwork() + ":"
						+ jh.getSource() + ":" + jh.getUserid() + ":"
						+ jh.getVoiceid());
	}

	/**
	 * @param args
	 */
	public static void main_older(String[] args) {
		// TODO Auto-generated method stub
		FarooResult fr = new FarooResult();
		fr.setAuthor("me");
		fr.setDate("20-12-2014");
		fr.setDomain("yahoo.com");
		fr.setIurl("http//www.yahoo.com");
		fr.setKwic("kwiconly");
		fr.setNews(false);
		fr.setTitle("This is the title");
		fr.setUrl("http://www.gmail.com");

		List aList = new ArrayList();
		FarooResult fr1 = new FarooResult();
		fr1 = fr;
		fr1.setAuthor("you");
		aList.add(fr);
		aList.add(fr1);
		Gson gson = new Gson();
		String abc = gson.toJson(aList);

		// System.out.println(abc);

		JsonHeader jh = new JsonHeader();
		jh.setCardid("12112");
		// jh.setCity("Bangalore");
		// jh.setCountry("India");
		jh.setProcessid("3.1.1");
		// jh.setServerid("181161171");
		jh.setSocialnetwork("FB");
		jh.setUserid("99282");
		jh.setVoiceid("8338");
		jh.setSource("FB");
		// jh.setBeforeid("18138");
		jh.setLastupdate("23-12-2014 08:33:21");
		jh.setAfterid("823823");
		jh.setCardtype("S");
		abc = gson.toJson(jh);
		System.out.println(abc);

		JsonSearch js = new JsonSearch();
		js.setCity("Tumkur");
		js.setCountry("India");
		js.setFacebook("Y");
		js.setGoogleplus("N");
		js.setTwitter("N");
		js.setPostonsocial("Y");
		js.setInteresttag("politics");
		js.setPostanonymous("N");
		js.setType("S");
		js.setSearchtopic("BJP in Delhi");
		abc = gson.toJson(js);
		// System.out.println(abc);

		JsonConversation jc = new JsonConversation();
		jc.setAdditionalviews("I have no additional views.");
		jc.setConversationtopic("Best Coffee in Pune");
		//jc.setFacebook("Y");
		//jc.setGoogleplus("N");
		jc.setInteresttag("food and beverages");
		jc.setLink("http://www.teadude.com");
		// jc.setPhotoid("18117117117");
		// jc.setVideoid("");
		jc.setMediaid("18117117117");
	//	jc.setPostanonymous("N");
	//	jc.setPostonsocial("Y");
		jc.setRating("6");
	//	jc.setTwitter("N");
	//	jc.setType("C");
		// abc = gson.toJson(jc);

		List bList = new ArrayList();
		bList.add(jh);
		bList.add(jc);
		abc = gson.toJson(bList);
		// System.out.println(abc);

		JsonSearchCard jsc = new JsonSearchCard();
		jsc.setCommentscount("22");
		jsc.setInteresttag("politics");
		jsc.setNegativevoices("22");
		jsc.setNeutralvoices("10");
		jsc.setNewcommentscount("5");
		jsc.setNewvoicescount("11");
		jsc.setPositivevoices("54");
		jsc.setSearchtopic("BJP in Delhi");
		jsc.setUsericonimage("12116");
		jsc.setUserid("99383");
		jsc.setUsername("Robert Norem");
		jsc.setVoicescount("299");
		jsc.setPriority("1");
		jsc.setTimestamp("22-08-2014 07:33:23");
		jsc.setId("23");

		JsonSearchCard jsc1 = new JsonSearchCard();
		jsc1.setCommentscount("32");
		jsc1.setInteresttag("politics");
		jsc1.setNegativevoices("12");
		jsc1.setNeutralvoices("19");
		jsc1.setNewcommentscount("8");
		jsc1.setNewvoicescount("37");
		jsc1.setPositivevoices("47");
		jsc1.setSearchtopic("BJP Delhi");
		jsc1.setUsericonimage("12332");
		jsc1.setUserid("89371");
		jsc1.setUsername("Raja Murad");
		jsc1.setVoicescount("243");
		jsc1.setPriority("2");
		jsc.setTimestamp("12-08-2014 21:13:13");
		jsc1.setId("223");

		List jscList = new ArrayList();
		// jscList.add(jh);
		jscList.add(jsc);
		jscList.add(jsc1);

		/*
		 * abc = gson.toJson(jscList); System.out.println(abc);
		 */

		JsonConversationCard jcc = new JsonConversationCard();
		jcc.setAdditionalviews("None");
		jcc.setBlogcount("22");
		jcc.setCommentscount("22");
		jcc.setFacebookcount("22");
		jcc.setGooglepluscount("22");
		jcc.setInteresttag("F n B");
		jcc.setNewblogcount("22");
		jcc.setNewcommentscount("22");
		jcc.setNewfacebookcount("22");
		jcc.setNewgooglepluscount("22");
		jcc.setNewstreetbuzzcount("22");
		jcc.setNewtwittercount("22");
		jcc.setNewvoicescount("22");
		jcc.setNewwebcount("22");
		jcc.setId("72273");
		jcc.setPhotoid("223");
		jcc.setPhotoid("4232332");
		jcc.setPriority("1");
		jcc.setRating("7");
		jcc.setStreetbuzzcount("22");
		jcc.setTwittercount("22");
		jcc.setUsericonimage("22");
		jcc.setUserid("9989");
		jcc.setUsername("Robert Norem");
		jcc.setVideoid("4322");
		jcc.setVoicescount("22");
		jcc.setWebcount("22");
		jcc.setTimestamp("22-08-2014 07:33:23");

		JsonConversationCard jcc1 = new JsonConversationCard();
		jcc1.setAdditionalviews("None");
		jcc1.setBlogcount("22");
		jcc1.setCommentscount("222");
		jcc1.setFacebookcount("22");
		jcc1.setGooglepluscount("32");
		jcc1.setInteresttag("F n B");
		jcc1.setNewblogcount("21");
		jcc1.setNewcommentscount("23");
		jcc1.setNewfacebookcount("44");
		jcc1.setNewgooglepluscount("55");
		jcc1.setNewstreetbuzzcount("262");
		jcc1.setNewtwittercount("66");
		jcc1.setNewvoicescount("77");
		jcc1.setNewwebcount("87");
		jcc1.setId("42273");
		jcc1.setPhotoid("213");
		jcc1.setPhotoid("4239832");
		jcc1.setPriority("2");
		jcc1.setRating("5");
		jcc1.setStreetbuzzcount("44");
		jcc1.setTwittercount("76");
		jcc1.setUsericonimage("87");
		jcc1.setUserid("99819");
		jcc1.setUsername("Robert Frost");
		jcc1.setVideoid("4112");
		jcc1.setVoicescount("222");
		jcc1.setWebcount("212");
		jcc.setTimestamp("12-08-2014 21:13:13");

		List jccList = new ArrayList();
		jscList.add(jh);
		jccList.add(jcc);
		jccList.add(jcc1);

		// abc = gson.toJson(jccList);
		// System.out.println(abc);

		JsonSubComments jscom = new JsonSubComments();
		jscom.setCommentid("212");
		jscom.setOrigin("SB");
		jscom.setPostid("29");
		jscom.setSubcommentid("2");
		jscom.setSubcommenttext("Is it so?");

		JsonComments jcom = new JsonComments();
		jcom.setCommentid("212");
		jcom.setCommenttext("Are you sure?");
		jcom.setOrigin("SB");
		jcom.setPostid("29");
		List subcomments = new ArrayList();
		subcomments.add(jscom);
		jcom.setSubcomments(subcomments);

		JsonComments jcom1 = new JsonComments();
		jcom1.setCommentid("214");
		jcom1.setCommenttext("Are you confident?");
		jcom1.setOrigin("SB");
		jcom1.setPostid("29");

		List comList = new ArrayList();
		comList.add(jcom);
		comList.add(jcom1);

		// abc = gson.toJson(comList);
		// System.out.println(abc);

		List temp = gson.fromJson(abc, ArrayList.class);
		System.out.println(temp.size());
		Object header = temp.get(0);
		// System.out.println(header.getClass());
		Object body = temp.get(1);
		// System.out.println(body.getClass());

		LinkedHashMap hmap = (LinkedHashMap) header;
		LinkedHashMap bmap = (LinkedHashMap) body;

		Set set = hmap.keySet();
		Iterator it = set.iterator();

		while (it.hasNext()) {
			String key = (String) it.next();
			String value = (String) hmap.get(key);
			// System.out.println(key+":"+value);
		}
		System.out.println("**********************************************");
		Set setb = bmap.keySet();
		Iterator itb = setb.iterator();

		while (itb.hasNext()) {
			String key = (String) itb.next();
			String value = (String) bmap.get(key);
			// System.out.println(key+":"+value);
		}

	}

}
