/**
 * 
 */
package com.crowd.streetbuzz.webservicecontroller;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.crowd.streetbuzz.common.Constants;
import com.crowd.streetbuzz.dao.implementation.InterestReferenceModelDAO;
import com.crowd.streetbuzz.dao.implementation.InterestSearchTermsDAO;
import com.crowd.streetbuzz.model.InterestReferenceModel;
import com.crowd.streetbuzz.model.InterestSearchTerms;
import com.crowd.streetbuzzalgo.googlesearch.vo.Result;
import com.crowd.streetbuzzalgo.scrapeandpost.FunnyCrawler;
import com.crowd.streetbuzzalgo.utils.FileUtils;
import com.crowd.streetbuzzalgo.utils.StrUtil;

/**
 * @author Atrijit
 *
 */
public class InterestTermsLoaderController implements Controller, Constants{
	private InterestSearchTermsDAO interestSearchTermsDAO;
	private InterestReferenceModelDAO interestReferenceModelDAO;
	
	public InterestReferenceModelDAO getInterestReferenceModelDAO() {
		return interestReferenceModelDAO;
	}
	public void setInterestReferenceModelDAO(
			InterestReferenceModelDAO interestReferenceModelDAO) {
		this.interestReferenceModelDAO = interestReferenceModelDAO;
	}
	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		long start = System.currentTimeMillis();
		List list = interestSearchTermsDAO.getAllRecordsPending();
		System.out.println("Total size "+list.size());
		for (int i=0;i<list.size();i++){
			InterestSearchTerms ist = (InterestSearchTerms)list.get(i);
			String name = StrUtil.nonNull(ist.getName());
			if(!"".equalsIgnoreCase(name)){
				/*List srchlist = new ArrayList();
				srchlist.add(name);*/
				List googleResultList =  new ArrayList();
				List tlist = new ArrayList();
				try {
					//googleResultList = WebSiteSearch.googleSearch(srchlist);
					googleResultList = new FunnyCrawler().getDataFromGoogle(name);
					if(googleResultList.size()>10){
						tlist = googleResultList.subList(0, 10);
					}else{
						tlist = googleResultList;
					}
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
				if(tlist.size()>0){
					for (int j=0;j<tlist.size();j++){
						/*Result result = (Result) tlist.get(j);
						String title = StrUtil.nonNull(result.getTitle());
						String url = result.getUrl();*/
						String url = (String)tlist.get(j);
						System.out.println("URL: "+url);
						Document doc = null;
						try {
							doc = Jsoup.connect(url).get();
						} catch (Exception e1) {
							System.out.println(e1.getMessage());
						}
						String text = "";
						String title = "";
						if(doc!=null){
							try {
								text = doc.body().text();
								title = doc.title();
							} catch (Exception e) {
								System.out.println(e.getMessage());
							}
						}
						
						InterestReferenceModel irm = new InterestReferenceModel();
						irm.setBody(text);
						irm.setInterestid(ist.getInterestid());
						irm.setParentinterestid(ist.getParentinterestid());
						irm.setKeywords("");
						irm.setSearchurl(url);
						irm.setTitle(title);
						irm.setName(name);
						
						try {
							interestReferenceModelDAO.addOrUpdateRecord(irm);
						} catch (Exception e) {
							System.out.println(e.getMessage());
						}
						ist.setStatus("DONE");
						interestSearchTermsDAO.addOrUpdateRecord(ist);
						
						
					}
				}
			}
			//System.out.println("Done "+(i+1));
			long end = System.currentTimeMillis();
			System.out.println("time :"+((end-start)/1000) +"secs");
		//	Thread.sleep(5000);
		}
		response.setStatus(200);
		response.addHeader("Access-Control-Allow-Origin", "*");
		PrintWriter writer = response.getWriter();
		writer.write("");
		writer.close();
		return null;
	}
	public ModelAndView handleRequest_Old(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String sourcepath = "/home/streetbuzz/catefeed.txt";
		List list = FileUtils.readFile(new File(sourcepath));
		for (int i=0;i<list.size();i++){
			String temp = (String)list.get(i);
			String[] temparray = temp.split(",");
			/*1,0,Business
			1,0,Economy
			2,0,Culture
			2,0,Entertainment*/
			String interestidStr = temparray[0];
			interestidStr = interestidStr.trim();
			String parentidStr = temparray[1];
			parentidStr = parentidStr.trim();
			String name = temparray[2];
			name = name.trim();
			Long interestid = new Long(interestidStr);
			Long parentinterestid = new Long(parentidStr);
			InterestSearchTerms ist = new InterestSearchTerms();
			ist.setInterestid(interestid);
			ist.setParentinterestid(parentinterestid);
			ist.setName(name);
			interestSearchTermsDAO.addOrUpdateRecord(ist);
			
		}
		System.out.println("Done");
		response.setStatus(200);
		response.addHeader("Access-Control-Allow-Origin", "*");
		PrintWriter writer = response.getWriter();
		writer.write("");
		writer.close();
		return null;
	}
	public InterestSearchTermsDAO getInterestSearchTermsDAO() {
		return interestSearchTermsDAO;
	}
	public void setInterestSearchTermsDAO(
			InterestSearchTermsDAO interestSearchTermsDAO) {
		this.interestSearchTermsDAO = interestSearchTermsDAO;
	}
}
