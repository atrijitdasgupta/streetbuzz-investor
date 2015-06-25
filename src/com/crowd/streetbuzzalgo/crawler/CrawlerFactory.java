/**
 * 
 */
package com.crowd.streetbuzzalgo.crawler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.crowd.streetbuzzalgo.constants.SystemConstants;
import com.crowd.streetbuzzalgo.crawler.vo.CrawlerVO;
import com.crowd.streetbuzzalgo.utils.DataClean;
import com.crowd.streetbuzzalgo.utils.StrUtil;
import com.jaunt.Element;
import com.jaunt.Elements;
import com.jaunt.UserAgent;

/**
 * @author Atrijit
 *
 */
public class CrawlerFactory implements SystemConstants {

	/**
	 * 
	 */
	public CrawlerFactory() {
		// TODO Auto-generated constructor stub
	}
	
	public static List crawl(String url, Date date){
		List retList = new ArrayList();
		UserAgent userAgent = new UserAgent();
		try {
			userAgent.visit(url);
			
			Elements temp = userAgent.doc.findEach("<p>");
			List tempList = temp.toList();
			for (int i=0;i<tempList.size();i++){
				Element text = (Element)tempList.get(i);
				String temptxt = StrUtil.nonNull(text.getText());
				if(temptxt.length()<50){
					continue;
				}
				if(temptxt.length()>200){
					temptxt = temptxt.substring(0, 190)+".";
				}
				temptxt = DataClean.clean(temptxt);
				if(!"".equalsIgnoreCase(temptxt)){
					CrawlerVO cvo = new CrawlerVO();
					cvo.setComment(temptxt);
					cvo.setURLParsed(url);
					cvo.setUsername("");
					retList.add(cvo);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		try {
			userAgent.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (retList!=null && retList.size()>10){
			retList = retList.subList(0, 9);
		}
		return retList;
	}
	public static List crawl(String url){
		List retList = new ArrayList();
		UserAgent userAgent = new UserAgent();
		try {
			userAgent.visit(url);
			
			Elements temp = userAgent.doc.findEach("<p>");
			List tempList = temp.toList();
			for (int i=0;i<tempList.size();i++){
				Element text = (Element)tempList.get(i);
				String temptxt = StrUtil.nonNull(text.getText());
				temptxt = DataClean.clean(temptxt);
				if(!"".equalsIgnoreCase(temptxt)){
					CrawlerVO cvo = new CrawlerVO();
					cvo.setComment(temptxt);
					cvo.setURLParsed(url);
					cvo.setUsername("");
					retList.add(cvo);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (retList!=null && retList.size()>5){
			retList = retList.subList(0, 4);
		}
		return retList;
	}
	public static List crawlActual(String url){
		List retList = new ArrayList();
		if(url.indexOf(BLOGSPOT)>-1){
			System.out.println("Detected blogspot URL");
			try {
				retList=ParseBlogspot.getComments(url,BLOGSPOT);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else if(url.indexOf(WORDPRESS)>-1){
			System.out.println("Detected wordpress URL");
			try {
				retList=ParseWordpress.getComments(url,WORDPRESS);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else if(url.indexOf(FLIPKART)>-1){
			System.out.println("Detected flipkart URL");
			try {
				retList=ParseFlipkart.getComments(url,FLIPKART);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else if(url.indexOf(GSMARENA)>-1){
			System.out.println("Detected gsmarena URL");
			try {
				retList=ParseGSMArena.getComments(url,GSMARENA);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else if(url.indexOf(CHICTOPIA)>-1){
			System.out.println("Detected chictopia URL");
			try {
				retList=ParseChictopia.getComments(url,CHICTOPIA);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else if(url.indexOf(GIZMODO)>-1){
			System.out.println("Detected gizmodo URL");
			try {
				retList=ParseGizmodo.getComments(url,GIZMODO);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else if(url.indexOf(INDIATIMES)>-1){
			System.out.println("Detected indiatimes URL");
			try {
				retList=ParseIndiaTimes.getComments(url,INDIATIMES);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else if(url.indexOf(PHONEARENA)>-1){
			System.out.println("Detected phonearena URL");
			try {
				retList=ParsePhoneArena.getComments(url,PHONEARENA);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else if(url.indexOf(SITEJABBER)>-1){
			System.out.println("Detected sitejabber URL");
			try {
				retList=ParseSiteJabber.getComments(url,SITEJABBER);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else if(url.indexOf(THEVERGE)>-1){
			System.out.println("Detected theverge URL");
			try {
				retList=ParseTheVerge.getComments(url,THEVERGE);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else if(url.indexOf(URBANSPOON)>-1){
			System.out.println("Detected urbanspoon URL");
			try {
				retList=ParseUrbanSpoon.getComments(url,URBANSPOON);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else if(url.indexOf(WIRED)>-1){
			System.out.println("Detected wired URL");
			try {
				retList=ParseWired.getComments(url,WIRED);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else if(url.indexOf(YELP)>-1){
			System.out.println("Detected yelp URL");
			try {
				retList=ParseYelp.getComments(url,YELP);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}}else if(url.indexOf(ROTTENTOMATOES)>-1){
			System.out.println("Detected rottentomatoes URL");
			try {
				retList=ParseRottenTomatoes.getComments(url,ROTTENTOMATOES);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else if(url.indexOf(MOUTHSHUT)>-1){
			System.out.println("Detected mouthshut URL");
			try {
				retList=ParseMouthShut.getComments(url,MOUTHSHUT);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else if(url.indexOf(CHOW)>-1){
			System.out.println("Detected chow URL");
			try {
				retList=ParseChow.getComments(url,CHOW);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			System.out.println("Detected generic URL");
			try {
				retList=ParseGeneric.getComments(url,"GENERIC");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return retList;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String blogspot = "http://googleblog.blogspot.in/2014/10/android-be-together-not-same.html";
		String wordpress = "http://deansarablog.wordpress.com/2014/10/13/the-nurse-who-broke-protocol/";
		//wordpress="https://foodtravelbangalore.wordpress.com/2012/12/01/the-bangalore-burger-smackdown/";
		String generic = "http://tamaratattles.com/2014/10/16/real-housewives-of-melbourne-part-2/";
		generic = "http://www.flipkart.com/moto-e/p/itmdvuwsybgnbtha?semcmpid=sem_3856208402_mobilesnew_goog&tgi=sem,1,G,3856208402,g,search,,53796793741,1t1,b,%2Bmoto%20%2Be%20%2Breview,c,,,,,,,&gclid=CjwKEAjwwo2iBRCurdSQy9y8xWcSJABrrLiSOoMBSQpwMYTznyjkNnwn-lKTCLLfyEoXFLdLuRGY9xoC4sPw_wcB";
		String gsmarena = "http://www.gsmarena.com/reviewcomm-1090.php";
		String chictopia ="http://www.chictopia.com/photo/show/1075415-P+is+for+Pattern-blue-givenchy-bag";
		String chow = "http://www.chow.com/recipes/31022-gin-and-tonic-barcelona-style";
		String flipkart = "http://www.flipkart.com/moto-g-2nd-gen/p/itmdygz8gqk2w3xp?pid=MOBDYGZ6SHNB7RFC&otracker=from-search&srno=t_1&query=moto+g&ref=f35665c0-140c-434c-9077-43c80ccb93ea";
		String gizmodo = "http://www.gizmodo.in/software/11-Clever-Uses-for-Your-Old-Phone-or-Tablet/articleshow/45115004.cms";
		String indiatimes = "http://www.indiatimes.com/entertainment/bollywood/7-reasons-why-deepika-is-the-ideal-role-model-for-every-girl-in-india-228329.html";
		String mouthshut = "http://www.mouthshut.com/inverters/Su-Kam-Brainy-reviews-925053024";
		String phonearena = "http://www.phonearena.com/reviews/Samsung-Galaxy-Note-Edge-vs-Samsung-Galaxy-S5_id3848";
		String rottentomatoes = "http://www.phonearena.com/reviews/Samsung-Galaxy-Note-Edge-vs-Samsung-Galaxy-S5_id3848";
		String sitejabber = "http://www.sitejabber.com/reviews/www.coco-fashion.com";
		String theverge = "http://www.theverge.com/2014/11/3/7149505/google-nexus-player-review";
		String urbanspoon = "http://www.urbanspoon.com/r/1/5609/restaurant/Ballard/Rays-Boathouse-Seattle";
		String wired = "http://www.wired.co.uk/reviews/gadgets/2014-10/motorola-moto-360";
		String yelp = "http://www.yelp.com/biz/the-house-san-francisco";
//		crawl(blogspot);
		crawlActual(wordpress);
		//crawl(chow);
		//crawl(chictopia);
		//crawl(flipkart);
		//crawl(gizmodo);
		//crawl(indiatimes);
	     //crawl(mouthshut);
		//crawl(phonearena);
	//	crawl(rottentomatoes);
		//crawl(sitejabber);
		//crawl(urbanspoon);
		///crawl(theverge);
		//crawl(wired);
		//crawl(yelp);
		

	}

}
