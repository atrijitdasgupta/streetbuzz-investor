/**
 * 
 */
package com.crowd.streetbuzzalgo.parser;

import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URLEncoder;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.crowd.streetbuzzalgo.constants.SystemConstants;

/**
 * @author Atrijit
 *
 */
public class MahoutCaller implements SystemConstants{
	private static final String POSITIVEPLUS = "P+";

	private static final String POSITIVE = "P";

	private static final String NEUTRAL = "Nu";

	private static final String NEGATIVEPLUS = "N+";

	private static final String NEGATIVE = "N";

	private static final String POSITIVEPLUSVAL = "positiveplus";

	private static final String POSITIVEVAL = "positive";

	private static final String NEUTRALVAL = "neutral";

	private static final String NEGATIVEVAL = "negative";

	private static final String NEGATIVEPLUSVAL = "negativeplus";
	/**
	 * 
	 */
	public MahoutCaller() {
		// TODO Auto-generated constructor stub
	}
	
	public static int getSentimentScore(String sentiment) {
		if (POSITIVEPLUSVAL.equalsIgnoreCase(sentiment)) {
			return 5;
		} else if (POSITIVEVAL.equalsIgnoreCase(sentiment)) {
			return 4;
		} else if (NEUTRALVAL.equalsIgnoreCase(sentiment)) {
			return 3;
		} else if (NEGATIVEVAL.equalsIgnoreCase(sentiment)) {
			return 2;
		} else if (NEGATIVEPLUSVAL.equalsIgnoreCase(sentiment)) {
			return 1;
		} else {
			return 3;
		}
	}
	
	public static String getSentiments(String phrase) throws Exception {
		
		phrase = URLEncoder.encode(phrase, "UTF-8");
		String sentimentval = "";
		StringBuffer params = new StringBuffer();
		params.append("?txt=" + phrase);
		params.append("&key=" + MAHOUT_KEY);
		params.append("&of=xml");
		params.append("&model=en-general");
		String url = MAHOUT_SENTIMENT_URL + params.toString();
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPost httppost = new HttpPost(url);
		CloseableHttpResponse response = httpclient.execute(httppost);
		HttpEntity entity = response.getEntity();
		if (entity != null) {
			Reader reader = new InputStreamReader(entity.getContent());
			int i;
			char c;
			StringBuffer sbfr = new StringBuffer();
			while ((i = reader.read()) != -1) {
				// int to character
				c = (char) i;
				sbfr.append(c);
				// print char
				// System.out.println("Character Read: "+c);
			}
			// System.out.println(sbfr.toString());
			String result = sbfr.toString();
			String begin = "<score_tag>";
			String end = "</score_tag>";
			int beginIndex = 0;
			if (result.indexOf(begin) > 0) {
				beginIndex = result.indexOf(begin);
			}
			int addIndex = begin.length();
			int endIndex = 0;
			if (result.indexOf(end) > 0) {
				endIndex = result.indexOf(end);
			}
			String sentiment = "Nu";
			try {
				sentiment = result.substring((beginIndex + addIndex), endIndex);
			} catch (RuntimeException e) {
				sentiment = "Nu";
			}
			sentiment = sentiment.trim();
			// System.out.println("Sentiment: " + sentiment);
			if (POSITIVEPLUS.equals(sentiment)) {
				sentimentval = POSITIVEPLUSVAL;
			} else if (POSITIVE.equals(sentiment)) {
				sentimentval = POSITIVEVAL;
			} else if (NEGATIVEPLUS.equals(sentiment)) {
				sentimentval = NEGATIVEPLUSVAL;
			} else if (NEGATIVE.equals(sentiment)) {
				sentimentval = NEGATIVEVAL;
			} else if (NEUTRAL.equals(sentiment)) {
				sentimentval = NEUTRALVAL;
			} else {
				sentimentval = NEUTRALVAL;
			}

		}
		httpclient.close();
		response.close();
		// System.out.println("Sentimentval: " + sentimentval);
		return sentimentval;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
