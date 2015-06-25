/**
 * 
 */
package com.crowd.streetbuzzalgo.webcrawler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Pattern;

import com.crowd.streetbuzzalgo.utils.StrUtil;
import com.jaunt.Document;
import com.jaunt.ResponseException;
import com.jaunt.UserAgent;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

/**
 * @author Atrijit
 * 
 */
/*
 * Refer https://github.com/yasserg/crawler4j for the base code
 */
public class Crawler extends WebCrawler {
	private final static Pattern FILTERS = Pattern
			.compile(".*(\\.(css|js|pdf|gif|jpg" + "|png|mp3|mp3|zip|gz))$");

	/**
	 * 
	 */
	public Crawler() {
		// TODO Auto-generated constructor stub
	}

	public boolean shouldVisit(Page referringPage, WebURL url) {
		String href = url.getURL().toLowerCase();
		/*
		 * return !FILTERS.matcher(href).matches() &&
		 * href.startsWith("http://www.imdb.com/");
		 */
		return (!FILTERS.matcher(href).matches());
	}
	public void visit(Page page) {
		String url = page.getWebURL().getURL();
		if (!shouldVisit(page, page.getWebURL())) {
			return;
		}
		if (page.getParseData() instanceof HtmlParseData) {
			HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
			String title = htmlParseData.getTitle();
			System.out.println("Title: " + title + ", URL: " + url);
		}
	}

	public void visitdb(Page page) {
		String url = page.getWebURL().getURL();
		if (!shouldVisit(page, page.getWebURL())) {
			return;
		}
		if (page.getParseData() instanceof HtmlParseData) {
			HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
			String title = htmlParseData.getTitle();
			List links = htmlParseData.getOutgoingUrls();
			byte[] contentbytes = page.getContentData();
			String contentstr = new String(contentbytes);
			String webpreviewimage = "";
			String firstsentence = "";
			System.out.println("Title: " + title + ", URL: " + url);
			UserAgent ua = new UserAgent();
			Document doc = null;
			try {
				doc = ua.openContent(contentstr);
			} catch (ResponseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String plaintext = doc.getText();

			List sentences = StrUtil.breakPara(plaintext);
			String temp = "";
			if (sentences != null) {
				if (sentences.size() > 1) {
					temp = (String) sentences.get(0) + " " + sentences.get(1);
				} else if (sentences.size() == 1) {
					temp = (String) sentences.get(0);
				}
			}
			firstsentence = temp;
			String disqusshortname = "";
			boolean disqustypeone = false;
			boolean disqustypetwo = false;
			if (contentstr.indexOf("disqus_shortname") > 0) {
				disqustypeone = true;
			}
			if (disqustypeone) {
				int num = contentstr.indexOf("disqus_shortname");
				int length = "disqus_shortname".length();
				String tmp = contentstr.substring(num + length, num + length
						+ 50);
				disqusshortname = tmp.substring((tmp.indexOf("'") + 1), tmp
						.indexOf("';"));
			}
			if (contentstr.indexOf("dsq.src") > 0 && !disqustypeone) {
				disqustypetwo = true;
			}
			if (disqustypetwo) {
				int num = contentstr.indexOf("dsq.src");
				int length = "dsq.src".length();
				String tmp = contentstr.substring(num + length, num + length
						+ 50);
				disqusshortname = tmp.substring((tmp.indexOf("'") + 1), tmp
						.indexOf("';"));
			}
			saveCrawl(title, url, webpreviewimage, firstsentence,
					disqusshortname);

		}
	}

	private void saveCrawl(String title, String url, String webpreviewimage,
			String firstsentence, String disqusshortname) {

		String dbdriver = "com.mysql.jdbc.Driver";
		String dburl = "jdbc:mysql://localhost:3306/onlineadmissions";
		Connection conn = null;
		try {
			Class.forName(dbdriver);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		try {
			conn = DriverManager.getConnection(dburl, "on_lineadms",
					"on_lineadms#@!");
		} catch (SQLException e) { // TODO Auto-generated catch block
			e.printStackTrace();
		}

		//Connection conn = BonePool.getConnection();
		Calendar calendar = Calendar.getInstance();
		java.sql.Date startDate = new java.sql.Date(calendar.getTime()
				.getTime());
		String query = "INSERT INTO SB_CRAWLER_MODEL (URL,AUTHOR,FIRSTSENTENCE,PAGEDATE,"
				+ "DISQUSSHORTNAME,LIVEFYREKEY,IMAGEURL,META, OGMETA) VALUES(?,?,?,?,?,?,?,?,?)";
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, url);
			pstmt.setString(2, "");
			pstmt.setString(3, firstsentence);
			pstmt.setDate(4, startDate);
			pstmt.setString(5, disqusshortname);
			pstmt.setString(6, "");
			pstmt.setString(7, "");
			pstmt.setString(8, "");
			pstmt.setString(9, "");
			pstmt.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (pstmt != null) {
			try {
				pstmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		BonePool.closeConnection(conn);

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
