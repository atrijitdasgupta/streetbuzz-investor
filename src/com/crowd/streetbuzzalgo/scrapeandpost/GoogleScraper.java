/**
 * 
 */
package com.crowd.streetbuzzalgo.scrapeandpost;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * @author Atrijit
 * 
 */
public class GoogleScraper {

	/**
	 * 
	 */
	public GoogleScraper() {

	}

	public static List jsoupScrape(String term) {
		Document doc = null;
		try {
			term = URLEncoder.encode(term, "utf-8");
		} catch (UnsupportedEncodingException e) {

		}
		// https://www.google.co.in/search?q=pudding&oq=pudding&aqs=chrome..69i57j0l5.1321j0j7&sourceid=chrome&es_sm=93&ie=UTF-8
		try {
			doc = Jsoup
					.connect(
							"https://www.google.co.in/search?q="
									+ term
									+ "&oq="
									+ term
									+ "&aqs=chrome..69i57j0l5.1321j0j7&sourceid=chrome&es_sm=93&ie=UTF-8")
					.userAgent("Mozilla").ignoreHttpErrors(true).timeout(0)
					.get();
			Elements links = null;
			try {
				links = doc.select("li[class=g]");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (links != null) {
				for (Element link : links) {
					Elements titles = link.select("h3[class=r]");
					String title = titles.text();

					Elements bodies = link.select("span[class=st]");
					String body = bodies.text();

					System.out.println("Title: " + title);
					System.out.println("Body: " + body + "\n");
				}
			}

		} catch (IOException e) {

		}
		Elements links = doc.select("li[class=g]");
		return null;
	}

	// DOES NOT WORK ANYMORE - TAKEN FROM JAUNT'S SITE
	/*
	 * public static List scrape(String term){ UserAgent userAgent = new
	 * UserAgent(); userAgent.settings.autoSaveAsHTML = true; try {
	 * userAgent.visit("http://google.com"); } catch (ResponseException e) { //
	 * TODO Auto-generated catch block e.printStackTrace(); } try {
	 * userAgent.doc.apply(term); } catch (JauntException e) { // TODO
	 * Auto-generated catch block e.printStackTrace(); } try {
	 * userAgent.doc.submit("Google Search"); } catch (SearchException e) { //
	 * TODO Auto-generated catch block e.printStackTrace(); } catch
	 * (ResponseException e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); } Elements links = null; try { links =
	 * userAgent.doc.findEvery("<h3 class=r>").findEvery("<a>"); } catch
	 * (Exception e) { // TODO Auto-generated catch block e.printStackTrace(); }
	 * for(Element link : links) { try { System.out.println(link.getAt("href")); }
	 * catch (NotFound e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); } } return null;
	 *  }
	 */

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		jsoupScrape("Butterfly");

	}

}
