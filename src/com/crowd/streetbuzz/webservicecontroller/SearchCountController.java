/**
 * 
 */
package com.crowd.streetbuzz.webservicecontroller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.crowd.streetbuzz.common.Constants;
import com.crowd.streetbuzz.processhelperutils.WebSiteSearch;
import com.crowd.streetbuzzalgo.faroosearch.vo.FarooResultSet;
import com.crowd.streetbuzzalgo.utils.StrUtil;

/**
 * @author Atrijit
 * 
 */
public class SearchCountController implements Controller, Constants {
	private String returnView = "";

	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		List terms = new ArrayList();
		//terms.add("Sachin Tendulkar");
		terms.add("Tikarpada");
		//terms.add("The book of Eli");
		//terms.add("Arnab Goswami");
		//terms.add("Black holes");
		//terms.add("Trivago");
		//terms.add("Audi A5");
	//	terms.add("Nepal earthquake");
	//	terms.add("Toit Bangalore");
	//	terms.add("Doremon");
		
		for (int i=0;i<terms.size();i++){
			String topic = (String)terms.get(i);
			String topicblog = topic+" blog";
			String topicwp = topic+" wordpress";
			String topicblogspot = topic+" blogspot";
			String topicblogger = topic+" blogger";
			
			List topiclist = new ArrayList();
			topiclist.add(topic);
			
			List topicbloglist = new ArrayList();
			topicbloglist.add(topicblog);
			
			List topicwplist = new ArrayList();
			topicwplist.add(topicwp);
			
			List topicblogspotlist = new ArrayList();
			topicblogspotlist.add(topicblogspot);
			
			List topicbloggerlist = new ArrayList();
			topicbloggerlist.add(topicblogger);
			
			long zero = System.currentTimeMillis();
			
			//GOOOGLE
			List gtopicList = WebSiteSearch.googleSearch(topiclist);
			long one = System.currentTimeMillis();
			System.out.println("@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%");
			System.out.println("Google::: Topic: "+topic+", time taken: "+((one-zero)/1000)+", result size: "+gtopicList.size());
			System.out.println("@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%");
			List gtopicblogList = WebSiteSearch.googleSearch(topicbloglist);
			long two = System.currentTimeMillis();
			System.out.println("@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%");
			System.out.println("Google::: Topic: "+topicblog+", time taken: "+((two-one)/1000)+", result size: "+gtopicblogList.size());
			System.out.println("@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%");
			
			List gtopicwpList = WebSiteSearch.googleSearch(topicwplist);
			long three = System.currentTimeMillis();
			System.out.println("@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%");
			System.out.println("Google::: Topic: "+topicwp+", time taken: "+((three-two)/1000)+", result size: "+gtopicwpList.size());
			System.out.println("@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%");
			
			List gtopicblogspotList = WebSiteSearch.googleSearch(topicblogspotlist);
			long four = System.currentTimeMillis();
			System.out.println("@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%");
			System.out.println("Google::: Topic: "+topicblogspot+", time taken: "+((four-three)/1000)+", result size: "+gtopicblogspotList.size());
			System.out.println("@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%");
			
			List gtopicbloggerList = WebSiteSearch.googleSearch(topicbloggerlist);
			long five = System.currentTimeMillis();
			System.out.println("@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%");
			System.out.println("Google::: Topic: "+topicblogger+", time taken: "+((five-four)/1000)+", result size: "+gtopicbloggerList.size());
			System.out.println("@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%");
			
			//FAROO
			List ftopicList = WebSiteSearch.farooSearch(topiclist);
			long six = System.currentTimeMillis();
			int size = 0;
			if(ftopicList!=null&& ftopicList.size()>0){
				for (int j = 0; j < ftopicList.size(); j++) {
					FarooResultSet frs = (FarooResultSet) ftopicList.get(j);
					List results = frs.getResults();
					size = size+results.size();
				}
			}
			System.out.println("@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%");
			System.out.println("Faroo::: Topic: "+topic+", time taken: "+((six-five)/1000)+", result size: "+size);
			System.out.println("@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%");
			size = 0;
			
			
			List ftopicblogList = WebSiteSearch.farooSearch(topiclist);
			long seven = System.currentTimeMillis();
			if(ftopicblogList!=null&& ftopicblogList.size()>0){
				for (int j = 0; j < ftopicblogList.size(); j++) {
					FarooResultSet frs = (FarooResultSet) ftopicblogList.get(j);
					List results = frs.getResults();
					size = size+results.size();
				}
			}
			System.out.println("@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%");
			System.out.println("Faroo::: Topic: "+topicblog+", time taken: "+((seven-six)/1000)+", result size: "+ftopicblogList.size());
			System.out.println("@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%");
			size = 0;
			
			List ftopicwpList = WebSiteSearch.farooSearch(topiclist);
			long eight = System.currentTimeMillis();
			if(ftopicwpList!=null&& ftopicwpList.size()>0){
				for (int j = 0; j < ftopicwpList.size(); j++) {
					FarooResultSet frs = (FarooResultSet) ftopicwpList.get(j);
					List results = frs.getResults();
					size = size+results.size();
				}
			}
			System.out.println("@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%");
			System.out.println("Faroo::: Topic: "+topicwp+", time taken: "+((eight-seven)/1000)+", result size: "+ftopicwpList.size());
			System.out.println("@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%");
			size = 0;
						
			List ftopicblogspotList = WebSiteSearch.farooSearch(topiclist);
			long nine = System.currentTimeMillis();
			if(ftopicblogspotList!=null&& ftopicblogspotList.size()>0){
				for (int j = 0; j < ftopicblogspotList.size(); j++) {
					FarooResultSet frs = (FarooResultSet) ftopicblogspotList.get(j);
					List results = frs.getResults();
					size = size+results.size();
				}
			}
			System.out.println("@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%");
			System.out.println("Faroo::: Topic: "+topicblogspot+", time taken: "+((nine-eight)/1000)+", result size: "+ftopicblogspotList.size());
			System.out.println("@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%");
			size = 0;
						
			List fbloggerList = WebSiteSearch.farooSearch(topiclist);
			long ten = System.currentTimeMillis();
			if(fbloggerList!=null&& fbloggerList.size()>0){
				for (int j = 0; j < fbloggerList.size(); j++) {
					FarooResultSet frs = (FarooResultSet) fbloggerList.get(j);
					List results = frs.getResults();
					size = size+results.size();
				}
			}
			System.out.println("@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%");
			System.out.println("Faroo::: Topic: "+topicblogger+", time taken: "+((ten-nine)/1000)+", result size: "+fbloggerList.size());
			System.out.println("@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%");
			size = 0;
			
			//YANDEX
			List ytopicList = new ArrayList();
			try {
				ytopicList = WebSiteSearch.yandexSearch(topiclist);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println(e.getMessage());
			}
			long eleven = System.currentTimeMillis();
			System.out.println("@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%");
			System.out.println("Yandex::: Topic: "+topic+", time taken: "+((eleven-ten)/1000)+", result size: "+ytopicList.size());
			System.out.println("@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%");
			
			List ytopicblogList = new ArrayList();
			try {
				ytopicblogList = WebSiteSearch.yandexSearch(topiclist);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println(e.getMessage());
			}
			long twelve = System.currentTimeMillis();
			System.out.println("@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%");
			System.out.println("Yandex::: Topic: "+topicblog+", time taken: "+((twelve-eleven)/1000)+", result size: "+ytopicblogList.size());
			System.out.println("@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%");
			
			List ytopicwpList = new ArrayList();
			try {
				ytopicwpList = WebSiteSearch.yandexSearch(topiclist);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println(e.getMessage());
			}
			long thirteen = System.currentTimeMillis();
			System.out.println("@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%");
			System.out.println("Yandex::: Topic: "+topicwp+", time taken: "+((thirteen-twelve)/1000)+", result size: "+ytopicwpList.size());
			System.out.println("@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%");
			
			List ytopicblogspotList = new ArrayList();
			try {
				ytopicblogspotList = WebSiteSearch.yandexSearch(topiclist);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println(e.getMessage());
			}
			long fourteen = System.currentTimeMillis();
			System.out.println("@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%");
			System.out.println("Yandex::: Topic: "+topicblogspot+", time taken: "+((fourteen-thirteen)/1000)+", result size: "+ytopicblogspotList.size());
			System.out.println("@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%");
			
			List ybloggerList = new ArrayList();
			try {
				ybloggerList = WebSiteSearch.yandexSearch(topiclist);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println(e.getMessage());
			}
			long fifteen = System.currentTimeMillis();
			System.out.println("@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%");
			System.out.println("Yandex::: Topic: "+topicblogger+", time taken: "+((fifteen-fourteen)/1000)+", result size: "+ybloggerList.size());
			System.out.println("@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%");
		}
		
		
		return new ModelAndView(returnView);
	}
	private static void run(){
		List terms = new ArrayList();
		terms.add("Sachin Tendulkar");
	//	terms.add("Tikarpada");
	//	terms.add("The book of Eli");
	//	terms.add("Arnab Goswami");
	//	terms.add("Black holes");
	//	terms.add("Trivago");
	//	terms.add("Audi A5");
	//	terms.add("Nepal earthquake");
	//	terms.add("Toit Bangalore");
	//	terms.add("Doremon");
		
		for (int i=0;i<terms.size();i++){
			String topic = (String)terms.get(i);
			String topicblog = topic+" blog";
			String topicwp = topic+" wordpress";
			String topicblogspot = topic+" blogspot";
			String topicblogger = topic+" blogger";
			
			List topiclist = new ArrayList();
			topiclist.add(topic);
			
			List topicbloglist = new ArrayList();
			topicbloglist.add(topicblog);
			
			List topicwplist = new ArrayList();
			topicwplist.add(topicwp);
			
			List topicblogspotlist = new ArrayList();
			topicblogspotlist.add(topicblogspot);
			
			List topicbloggerlist = new ArrayList();
			topicbloggerlist.add(topicblogger);
			
			long zero = System.currentTimeMillis();
			
			//GOOOGLE
			List gtopicList = WebSiteSearch.googleSearch(topiclist);
			long one = System.currentTimeMillis();
			//System.out.println("@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%");
			System.out.println("Google::: Topic: "+topic+", time taken: "+((one-zero)/1000)+", result size: "+gtopicList.size());
			//System.out.println("@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%");
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			one = System.currentTimeMillis();
			List gtopicblogList = WebSiteSearch.googleSearch(topicbloglist);
			long two = System.currentTimeMillis();
		//	System.out.println("@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%");
			System.out.println("Google::: Topic: "+topicblog+", time taken: "+((two-one)/1000)+", result size: "+gtopicblogList.size());
		//	System.out.println("@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%");
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			two = System.currentTimeMillis();
			List gtopicwpList = WebSiteSearch.googleSearch(topicwplist);
			
			long three = System.currentTimeMillis();
		//	System.out.println("@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%");
			System.out.println("Google::: Topic: "+topicwp+", time taken: "+((three-two)/1000)+", result size: "+gtopicwpList.size());
		//	System.out.println("@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%");
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			three = System.currentTimeMillis();
			List gtopicblogspotList = WebSiteSearch.googleSearch(topicblogspotlist);
			
			long four = System.currentTimeMillis();
		//	System.out.println("@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%");
			System.out.println("Google::: Topic: "+topicblogspot+", time taken: "+((four-three)/1000)+", result size: "+gtopicblogspotList.size());
		//	System.out.println("@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%");
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			four = System.currentTimeMillis();
			List gtopicbloggerList = WebSiteSearch.googleSearch(topicbloggerlist);
			long five = System.currentTimeMillis();
		//	System.out.println("@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%");
			System.out.println("Google::: Topic: "+topicblogger+", time taken: "+((five-four)/1000)+", result size: "+gtopicbloggerList.size());
		//	System.out.println("@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%");
			
			
			//FAROO
			five = System.currentTimeMillis();
			List ftopicList = new ArrayList();
			try {
				ftopicList = WebSiteSearch.farooSearch(topiclist);
			} catch (Exception e2) {
				// TODO Auto-generated catch block
				System.out.println(e2.getMessage());
			}
			
			long six = System.currentTimeMillis();
			int size = 0;
			if(ftopicList!=null&& ftopicList.size()>0){
				for (int j = 0; j < ftopicList.size(); j++) {
					FarooResultSet frs = (FarooResultSet) ftopicList.get(j);
					List results = frs.getResults();
					size = size+results.size();
				}
			}
		//	System.out.println("@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%");
			System.out.println("Faroo::: Topic: "+topic+", time taken: "+((six-five)/1000)+", result size: "+size);
		//	System.out.println("@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%");
			
			size = 0;
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			six = System.currentTimeMillis();
			List ftopicblogList = new ArrayList();
			try {
				ftopicblogList = WebSiteSearch.farooSearch(topicbloglist);
			} catch (Exception e2) {
				// TODO Auto-generated catch block
				System.out.println(e2.getMessage());
			}
			
			long seven = System.currentTimeMillis();
			if(ftopicblogList!=null&& ftopicblogList.size()>0){
				for (int j = 0; j < ftopicblogList.size(); j++) {
					FarooResultSet frs = (FarooResultSet) ftopicblogList.get(j);
					List results = frs.getResults();
					size = size+results.size();
				}
			}
		//	System.out.println("@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%");
			System.out.println("Faroo::: Topic: "+topicblog+", time taken: "+((seven-six)/1000)+", result size: "+ftopicblogList.size());
		//	System.out.println("@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%");
			size = 0;
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			seven = System.currentTimeMillis();
			List ftopicwpList = new ArrayList();
			try {
				ftopicwpList = WebSiteSearch.farooSearch(topicwplist);
			} catch (Exception e2) {
				// TODO Auto-generated catch block
				System.out.println(e2.getMessage());
			}
			
			long eight = System.currentTimeMillis();
			if(ftopicwpList!=null&& ftopicwpList.size()>0){
				for (int j = 0; j < ftopicwpList.size(); j++) {
					FarooResultSet frs = (FarooResultSet) ftopicwpList.get(j);
					List results = frs.getResults();
					size = size+results.size();
				}
			}
		//	System.out.println("@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%");
			System.out.println("Faroo::: Topic: "+topicwp+", time taken: "+((eight-seven)/1000)+", result size: "+ftopicwpList.size());
		//	System.out.println("@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%");
			size = 0;
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			eight = System.currentTimeMillis();	
			List ftopicblogspotList = new ArrayList();
			try {
				ftopicblogspotList = WebSiteSearch.farooSearch(topicblogspotlist);
			} catch (RuntimeException e2) {
				// TODO Auto-generated catch block
				System.out.println(e2.getMessage());
			}
			
			long nine = System.currentTimeMillis();
			if(ftopicblogspotList!=null&& ftopicblogspotList.size()>0){
				for (int j = 0; j < ftopicblogspotList.size(); j++) {
					FarooResultSet frs = (FarooResultSet) ftopicblogspotList.get(j);
					List results = frs.getResults();
					size = size+results.size();
				}
			}
		//	System.out.println("@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%");
			System.out.println("Faroo::: Topic: "+topicblogspot+", time taken: "+((nine-eight)/1000)+", result size: "+ftopicblogspotList.size());
		//	System.out.println("@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%");
			size = 0;
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			nine = System.currentTimeMillis();		
			List fbloggerList = new ArrayList();
			try {
				fbloggerList = WebSiteSearch.farooSearch(topicbloggerlist);
			} catch (RuntimeException e2) {
				// TODO Auto-generated catch block
				System.out.println(e2.getMessage());
			}
			
			long ten = System.currentTimeMillis();
			if(fbloggerList!=null&& fbloggerList.size()>0){
				for (int j = 0; j < fbloggerList.size(); j++) {
					FarooResultSet frs = (FarooResultSet) fbloggerList.get(j);
					List results = frs.getResults();
					size = size+results.size();
				}
			}
		//	System.out.println("@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%");
			System.out.println("Faroo::: Topic: "+topicblogger+", time taken: "+((ten-nine)/1000)+", result size: "+fbloggerList.size());
		//	System.out.println("@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%");
			size = 0;
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			//YANDEX
			List ytopicList = new ArrayList();
			ten = System.currentTimeMillis();
			try {
				ytopicList = WebSiteSearch.yandexSearch(topiclist);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println(e.getMessage());
			}
			
			long eleven = System.currentTimeMillis();
		//	System.out.println("@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%");
			System.out.println("Yandex::: Topic: "+topic+", time taken: "+((eleven-ten)/1000)+", result size: "+ytopicList.size());
		//	System.out.println("@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%");
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			List ytopicblogList = new ArrayList();
			eleven = System.currentTimeMillis();
			try {
				ytopicblogList = WebSiteSearch.yandexSearch(topicbloglist);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println(e.getMessage());
			}
			
			long twelve = System.currentTimeMillis();
		//	System.out.println("@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%");
			System.out.println("Yandex::: Topic: "+topicblog+", time taken: "+((twelve-eleven)/1000)+", result size: "+ytopicblogList.size());
		//	System.out.println("@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%");
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			List ytopicwpList = new ArrayList();
			twelve = System.currentTimeMillis();
			try {
				ytopicwpList = WebSiteSearch.yandexSearch(topicwplist);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println(e.getMessage());
			}
			
			long thirteen = System.currentTimeMillis();
		//	System.out.println("@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%");
			System.out.println("Yandex::: Topic: "+topicwp+", time taken: "+((thirteen-twelve)/1000)+", result size: "+ytopicwpList.size());
		//	System.out.println("@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%");
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			List ytopicblogspotList = new ArrayList();
			thirteen = System.currentTimeMillis();
			try {
				ytopicblogspotList = WebSiteSearch.yandexSearch(topicblogspotlist);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println(e.getMessage());
			}
			
			long fourteen = System.currentTimeMillis();
		//	System.out.println("@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%");
			System.out.println("Yandex::: Topic: "+topicblogspot+", time taken: "+((fourteen-thirteen)/1000)+", result size: "+ytopicblogspotList.size());
		//	System.out.println("@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%");
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			List ybloggerList = new ArrayList();
			fourteen = System.currentTimeMillis();
			try {
				ybloggerList = WebSiteSearch.yandexSearch(topicbloggerlist);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println(e.getMessage());
			}
			
			long fifteen = System.currentTimeMillis();
			//System.out.println("@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%");
			System.out.println("Yandex::: Topic: "+topicblogger+", time taken: "+((fifteen-fourteen)/1000)+", result size: "+ybloggerList.size());
			//System.out.println("@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%@#$%");
			
		}
	}
	
	public static void main(String [] args){
		run();
	}
}
