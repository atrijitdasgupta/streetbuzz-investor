// Decompiled by DJ v3.12.12.98 Copyright 2014 Atanas Neshkov  Date: 27-09-2014 22:11:39
// Home Page:  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   StrUtil.java

package com.crowd.streetbuzzalgo.utils;

import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.BreakIterator;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.codec.digest.DigestUtils;
import org.jsoup.Jsoup;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.crowd.streetbuzz.common.Constants;
import com.crowd.streetbuzz.json.JSONdisambiguationtype;
import com.crowd.streetbuzz.json.ResultReadyJSONResponse;
import com.crowd.streetbuzz.stopword.SingleStopper;
import com.crowd.streetbuzzalgo.taxonomytree.TraverseTaxonomy;
import com.google.gson.Gson;
import com.jaunt.Element;
import com.jaunt.Elements;
import com.jaunt.NotFound;
import com.jaunt.UserAgent;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.CoreMap;

public class StrUtil implements Constants {

	public StrUtil() {
	}

	public static String[] nonNull(String str[]) {
		return str != null ? str : new String[0];
	}

	public static String nonNull(String s) {
		return s != null ? s : "";
	}

	public static String nonSelect(String s) {
		if ("Select".equalsIgnoreCase(s))
			s = "";
		return s;
	}

	public static List breakPara(String source) {
		List list = new ArrayList();
		BreakIterator bi = BreakIterator.getSentenceInstance(Locale.US);
		bi.setText(source);

		int lastIndex = bi.first();
		while (lastIndex != BreakIterator.DONE) {
			int firstIndex = lastIndex;
			lastIndex = bi.next();

			if (lastIndex != BreakIterator.DONE) {
				String sentence = source.substring(firstIndex, lastIndex);
				list.add(sentence);

			}
		}
		return list;
	}

	public static List sentenceify(String para) {
		Pattern re = Pattern
				.compile(
						"[^.!?\\s][^.!?]*(?:[.!?](?!['\"]?\\s|$)[^.!?]*)*[.!?]?['\"]?(?=\\s|$)",
						Pattern.MULTILINE | Pattern.COMMENTS);
		Matcher reMatcher = re.matcher(para);
		List list = new ArrayList();
		int count = reMatcher.groupCount();
		System.out.println(count);
		while (reMatcher.find()) {
			String sentence = reMatcher.group();
			list.add(sentence);
		}
		return list;
	}

	private static void testSentiment() {
		/*
		 * Properties props = new Properties(); props.setProperty("annotators",
		 * "tokenize, ssplit, parse, sentiment"); StanfordCoreNLP pipeline = new
		 * StanfordCoreNLP(props);
		 */

		String one = "The Khaimaquam of Khanaquin is versed in every kind of sin.";
		String two = "Nothing to do but work to keep from getting bored";
		String three = "Black and white spots on  brown coach makes it look good";

		List list = new ArrayList();
		list.add(one);
		list.add(two);
		list.add(three);

		for (int i = 0; i < list.size(); i++) {
			Properties props = new Properties();
			props.setProperty("annotators",
					"tokenize, ssplit, parse, sentiment");
			StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
			String line = (String) list.get(i);
			Annotation annotation = pipeline.process(line);
			for (CoreMap sentence : annotation
					.get(CoreAnnotations.SentencesAnnotation.class)) {
				Tree tree = sentence
						.get(SentimentCoreAnnotations.AnnotatedTree.class);
				int sentimentscore = RNNCoreAnnotations.getPredictedClass(tree);
				String sentimentStr = sentence
						.get(SentimentCoreAnnotations.ClassName.class);
				System.out.println("Twitter Input: " + line
						+ ", sentiment numeric value: " + sentimentscore
						+ ", sentiment absolute value:" + sentimentStr);
			}
		}
	}

	public static int getCheckSum(String str) throws Exception {
		byte bytes[] = str.getBytes();
		return bytes.length;
	}

	public static List listYears() {
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");
		String yrnow = dateFormat.format(date);
		Integer yrInt = new Integer(yrnow);
		int yr = yrInt.intValue();
		List yrList = new ArrayList();
		for (int j = 0; j < 101; j++) {
			yrList.add(new Integer(yr));
			yr--;
		}

		return yrList;
	}

	public static List ListMonths() {
		List monthList = new ArrayList();
		monthList.add("January");
		monthList.add("February");
		monthList.add("March");
		monthList.add("April");
		monthList.add("May");
		monthList.add("June");
		monthList.add("July");
		monthList.add("August");
		monthList.add("September");
		monthList.add("October");
		monthList.add("November");
		monthList.add("December");
		return monthList;
	}

	public static List ListDays() {
		List daysList = new ArrayList();
		daysList.add("1");
		daysList.add("2");
		daysList.add("3");
		daysList.add("4");
		daysList.add("5");
		daysList.add("6");
		daysList.add("7");
		daysList.add("8");
		daysList.add("9");
		daysList.add("10");
		daysList.add("11");
		daysList.add("12");
		daysList.add("13");
		daysList.add("14");
		daysList.add("15");
		daysList.add("16");
		daysList.add("17");
		daysList.add("18");
		daysList.add("19");
		daysList.add("20");
		daysList.add("21");
		daysList.add("22");
		daysList.add("23");
		daysList.add("24");
		daysList.add("25");
		daysList.add("26");
		daysList.add("27");
		daysList.add("28");
		daysList.add("29");
		daysList.add("30");
		daysList.add("31");
		return daysList;
	}

	public static String extractNodeValue(String body, String startingNode,
			String closingNode) {
		String temp = "";
		try {
			temp = body.substring(
					body.indexOf(startingNode) + startingNode.length(),
					body.indexOf(closingNode)).trim();
		} catch (RuntimeException e) {
			System.out.println(e.getMessage());
			temp = "Friday, January 9, 2009";
		}
		return temp;
	}

	public static String putBackSingleQuotes(String sign, String feed) {
		feed = feed.replaceAll(sign, "'");
		return feed;
	}

	public static String removeTrail(String str) {
		if (str.indexOf("_") > 0)
			str = str.substring(0, str.indexOf("_"));
		str = str.trim();
		return str;
	}

	public static String newLineHash(String str) {
		if (str.indexOf("#") > -1)
			str = str.replaceAll("#", "\n");
		return str;
	}

	public static String[] splitHash(String str) {
		if (str != null && str.indexOf("#") > 0) {
			String strSplit[] = str.split("#");
			return strSplit;
		} else {
			return null;
		}
	}

	public static String getDateString(Date dt, String format) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		String wtf = dateFormat.format(dt);
		return wtf;
	}

	public static String getSha(String value, String salt) {
		return DigestUtils.sha512Hex((new StringBuilder()).append(value)
				.append(salt).toString());
	}

	private static void checkXml() {
		String RCPTNUMBER_START_TAG = "<receiptNumber>";
		String RCPTNUMBER_END_TAG = "</receiptNumber>";
		String CHKOUT_START_TAG = "<checkedOut>";
		String CHKOUT_END_TAG = "</checkedOut>";
		String ERRORMSG_START_TAG = "<error_message>";
		String ERRORMSG_END_TAG = "</error_message>";
		String xmlResponse = "<?xml version='1.0' encoding='UTF-8' standalone='yes'?><donation><amount>10.00</amount><chargedAmount>10.00</chargedAmount><currencyCode>USD</currencyCode><datetime>2013-01-05T21:18:31.745-05:00</datetime><email>prashanth@thoughtfocus.com</email><project><funding>34255.00</funding><goal>100000.00</goal><id>3754</id><numberOfDonations>317</numberOfDonations><progressReportLink>http://www.globalgiving.org/projects/empowering-vulnerable-youngpeople-with-life-skills/updates/</progressReportLink><projectLink>http://www.globalgiving.org/projects/empowering-vulnerable-youngpeople-with-life-skills/</projectLink><remaining>65745.00</remaining></project><refcode>1357438710717Prashanth1357438710717</refcode><signupForCharityNewsletter>false</signupForCharityNewsletter><signupForGGNewsletter>false</signupForGGNewsletter><transactionId>Prashanth1357438710717</transactionId><checkedOut>true</checkedOut><payment_detail><address>12941 W Wyndridge Dr</address><address2></address2><city>New Berlin</city><creditCardNumber>5178-XXXX-XXXX-0431</creditCardNumber><creditCardType>mastercard</creditCardType><expiryDateMonth>10</expiryDateMonth><expiryDateYear>2013</expiryDateYear><firstname>Prashanth</firstname><iso3166CountryCode>US</iso3166CountryCode><lastname>Sharma</lastname><phone></phone><securityCode>XXX</securityCode><state>WI</state><zip>53151</zip></payment_detail><receipt><currencyCode>USD</currencyCode><receiptNumber>R657635-GC2324816-CBcb01</receiptNumber><taxDeductibleContributionAmount>10.00</taxDeductibleContributionAmount><totalAmountBilled>10.00</totalAmountBilled></receipt></donation>";
		boolean paymentSuccess = true;
		String errorMsg = "";
		String checkOutVal = "";
		String receiptNumberVal = "";
		if (xmlResponse.indexOf(ERRORMSG_START_TAG) > -1
				&& xmlResponse.indexOf(ERRORMSG_END_TAG) > -1) {
			paymentSuccess = false;
			errorMsg = nonNull(xmlResponse.substring(xmlResponse
					.indexOf(ERRORMSG_START_TAG)
					+ ERRORMSG_START_TAG.length(), xmlResponse
					.indexOf(ERRORMSG_END_TAG)));
			errorMsg = errorMsg.trim();
		}
		if (xmlResponse.indexOf(CHKOUT_START_TAG) > -1
				&& xmlResponse.indexOf(CHKOUT_END_TAG) > -1) {
			checkOutVal = nonNull(xmlResponse.substring(xmlResponse
					.indexOf(CHKOUT_START_TAG)
					+ CHKOUT_START_TAG.length(), xmlResponse
					.indexOf(CHKOUT_END_TAG)));
			checkOutVal = checkOutVal.trim();
		}
		if (xmlResponse.indexOf(RCPTNUMBER_START_TAG) > -1
				&& xmlResponse.indexOf(RCPTNUMBER_END_TAG) > -1) {
			receiptNumberVal = nonNull(xmlResponse.substring(xmlResponse
					.indexOf(RCPTNUMBER_START_TAG)
					+ RCPTNUMBER_START_TAG.length(), xmlResponse
					.indexOf(RCPTNUMBER_END_TAG)));
			receiptNumberVal = receiptNumberVal.trim();
			System.out.println((new StringBuilder()).append(
					"1. receiptNumberVal: ").append(receiptNumberVal)
					.toString());
		}
		if ("".equalsIgnoreCase(checkOutVal)
				|| "".equalsIgnoreCase(receiptNumberVal))
			System.out.println("blank checkOutVal OR receiptNumberVal");
		if ("false".equalsIgnoreCase(checkOutVal)) {
			paymentSuccess = false;
		} else {
			paymentSuccess = true;
			System.out.println((new StringBuilder()).append("paymentSuccess: ")
					.append(paymentSuccess).toString());
			System.out.println((new StringBuilder()).append(
					"2. receiptNumberVal: ").append(receiptNumberVal)
					.toString());
		}
	}

	public static String getExtnType(String filename) {
		return filename.substring(filename.indexOf(".") + 1, filename.length());
	}

	public static Date getDate(String datestr, String format) {
		Date dt = null;
		try {
			dt = (new SimpleDateFormat(format)).parse(datestr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return dt;
	}

	public static String putLineBreak(String temp) {
		String temprr[] = temp.split(" ");
		StringBuffer sbfr = new StringBuffer();
		for (int i = 0; i < temprr.length; i++) {
			System.out.println(temprr[i]);
			sbfr.append((new StringBuilder()).append(temprr[i]).append(" ")
					.toString());
			String xtemp = sbfr.toString();
			if (xtemp.indexOf("<br/>") < 0 && xtemp.length() > 30)
				sbfr.append("<br/>");
		}

		return sbfr.toString();
	}

	public static void splitSum(String sum) {
		String splitstr[] = sum.split("");
		for (int i = 0; i < splitstr.length; i++) {
			String temp = splitstr[i];
			System.out.println(temp);
		}

	}

	public static boolean onlyNumbers(String data) {
		return data.matches("[0-9]+");
	}

	private static void parseJson() {
		String jsonstr = "{'http':200,'message':'success','terms':[{'term':'Bicycle','canonical':1,'oskill':0},"
				+ "{'term':'Bicycles','canonical':0,'oskill':0},"
				+ "{'term':'Bycicle','canonical':0,'oskill':0},{'term':'Pedal cycle','canonical':0,'oskill':0},"
				+ "{'term':'Pedal bicycle','canonical':0,'oskill':0},{'term':'Pedal bike','canonical':0,'oskill':0},"
				+ "{'term':'Tall Bikes','canonical':0,'oskill':0},{'term':'Push-bike','canonical':0,'oskill':0},"
				+ "{'term':'Push bike','canonical':0,'oskill':0},{'term':'Pushbike','canonical':0,'oskill':0},"
				+ "{'term':'Bikes','canonical':0,'oskill':0},{'term':'Dutch bicycles','canonical':0,'oskill':0},"
				+ "{'term':'Bicicletta','canonical':0,'oskill':0},{'term':'Pedalcycle','canonical':0,'oskill':0},"
				+ "{'term':'','canonical':0,'oskill':0},{'term':'Bycycle','canonical':0,'oskill':0}]}";

		String[] arr = jsonstr.split(",");
		String val = "";
		List aList = new ArrayList();
		for (int i = 0; i < arr.length; i++) {
			String temp = arr[i];

			if (temp.indexOf("term") > 0) {
				val = temp.substring(temp.indexOf("term") + "term':'".length(),
						temp.length() - 1);

			}
			if (val.indexOf("temp") < 0) {
				if (!aList.contains(val)) {
					val = DataClean.clean(val);
					val = StrUtil.nonNull(val);
					if (!"".equalsIgnoreCase(val)) {
						aList.add(val);

					}

				}
			}

		}
		for (int i = 0; i < aList.size(); i++) {
			String prnt = (String) aList.get(i);
			System.out.println(prnt);
		}
	}

	private static void checkAmpersandSplit() {
		String temp = "A & B";
		String[] arr = temp.split("&");
		System.out.println(arr[0] + "<->" + arr[1]);

	}

	private static void getKeywords() throws Exception {
		String temp = "<?xml version='1.0' encoding='UTF-8'?><results><status>OK</status>"
				+ "<usage>By accessing RP or using information generated by RP, "
				+ "you are agreeing to be bound by the RP Terms of Use: "
				+ "http://www.alchemyapi.com/company/terms.html</usage><url></url>"
				+ "<language>english</language>"
				+ "<keywords><keyword><text>Best Chinese Restaurant</text><relevance>0.916599</relevance></keyword>"
				+ "<keyword><text>Bangalore</text><relevance>0.788314</relevance></keyword></keywords></results>";
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		InputSource is = new InputSource(new StringReader(temp));
		Document document = builder.parse(is);
		NodeList nodeList = document.getDocumentElement().getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node node = nodeList.item(i);
			if (node.getNodeName().equalsIgnoreCase("keywords")) {
				NodeList cnodeList = node.getChildNodes();
				for (int j = 0; j < cnodeList.getLength(); j++) {
					Node cnode = cnodeList.item(j);
					if (cnode.getNodeName().equalsIgnoreCase("keyword")) {
						NodeList gcnodelist = cnode.getChildNodes();
						for (int k = 0; k < gcnodelist.getLength(); k++) {
							Node gcnode = gcnodelist.item(k);
							String nname = gcnode.getNodeName();
							if ("text".equalsIgnoreCase(nname)) {
								System.out.println(gcnode.getTextContent());
							}
							if ("relevance".equalsIgnoreCase(nname)) {
								System.out.println(gcnode.getTextContent());
							}
							/*
							 * Element elem = (Element) gcnode; String text =
							 * elem.getElementsByTagName("text").item(0).getNodeValue();
							 * String relevance =
							 * elem.getElementsByTagName("relevance").item(0).getNodeValue();
							 */

							// System.out.println(text+" "+relevance);
						}
					}
				}
			}
		}

	}

	public static boolean containsOld(String sentence, Pattern p) {

		System.out.println(p.toString());
		System.out.println(sentence);
		Matcher m = p.matcher(sentence);

		if (m.find()) {
			return true;
		}

		return false;
	}

	public static boolean containsOrNot(String sentence, List synonyms) {
		List statusList = new ArrayList();
		sentence = sentence.toLowerCase().trim();
		// System.out.println("containsOrNot sentence: "+sentence);
		for (int i = 0; i < synonyms.size(); i++) {
			String temp = (String) synonyms.get(i);
			temp = temp.toLowerCase().trim();
			// System.out.println("containsOrNot temp: "+temp);
			if (sentence.contains(temp)) {
				// return true;
				statusList.add("true");
			} else {
				statusList.add("false");
			}
		}
		if (statusList.contains("false")) {
			return false;
		}
		return true;
	}

	public static boolean justContains(String sentence, List synonyms) {

		sentence = sentence.toLowerCase().trim();
		// System.out.println("containsOrNot sentence: "+sentence);
		for (int i = 0; i < synonyms.size(); i++) {
			String temp = (String) synonyms.get(i);
			temp = temp.toLowerCase().trim();
			// System.out.println("containsOrNot temp: "+temp);
			if (sentence.contains(temp)) {
				return true;

			}
		}
		return false;

	}

	private static String stripVerb(String source, String verb) {

		if (source.endsWith(verb)) {
			source = source.substring(0, source.indexOf(verb));
		}
		return source;
	}

	public static String removeDuplicates(String target, String verb) {
		String[] arr = target.split(" ");
		StringBuffer sbfr = new StringBuffer();
		boolean removed = false;
		for (int i = 0; i < arr.length; i++) {
			String temp = arr[i];
			temp = stripVerb(temp, verb);
			if (!temp.equalsIgnoreCase(verb)) {
				sbfr.append(temp + " ");
			}
		}
		String finalStr = sbfr.toString();
		finalStr = finalStr.trim();
		if (finalStr.indexOf(verb) < 0) {
			finalStr = finalStr + " " + verb;
			finalStr = finalStr.trim();
		}
		return finalStr;
	}

	public static boolean isSingle(String text) {
		String[] array = text.split(" ");
		if (array.length < 2) {
			return true;
		}
		return false;
	}

	public static String getUniqueId(String userid) {
		String temp = userid + new Long(System.currentTimeMillis()).toString();
		byte[] tbytes = temp.getBytes();
		String hex = ByteUtils.getHex(tbytes);
		return hex;
	}

	public static String getUniqueId() {
		UUID idOne = UUID.randomUUID();
		String time = new Long(System.currentTimeMillis()).toString();
		String temp = String.valueOf(idOne) + "-" + time;
		return temp;
	}

	public static String getUniqueGoogleId() {
		UUID idOne = UUID.randomUUID();
		String temp = String.valueOf(idOne);
		if (temp.length() > 40) {
			temp = temp.substring(0, 39);
		}
		return temp;
	}

	public static int[] divideUniformlyRandomly(int number, int parts) {
		Random random = new Random();
		int[] randoms = new int[parts];
		for (int i = 0; i < parts; i++) {
			randoms[i] = random.nextInt(number);
		}
		return randoms;
	}

	public static int[] divide(int number, int parts) {
		int[] randoms = new int[parts];
		Arrays.fill(randoms, 1); // At least one
		int remainder = number - parts;
		Random random = new Random();
		for (int i = 0; i < parts - 1 && remainder > 0; ++i) {
			int diff = random.nextInt(remainder);
			randoms[i] += diff;
			remainder -= diff;
		}
		randoms[parts - 1] += remainder;
		Arrays.sort(randoms);

		// Reverse (for getting a descending array):
		for (int i = 0, j = parts - 1; i < j; ++i, --j) {
			int temp = randoms[i];
			randoms[i] = randoms[j];
			randoms[j] = temp;
		}
		return randoms;
	}

	public static List getCatsForSearch(String cat) {
		if (cat.indexOf("&") > 0) {
			cat = cat.replaceAll("&", "");
		}
		// System.out.println(cat);
		List catTermsList = new ArrayList();
		String[] splitArr = cat.split(" ");
		for (int i = 0; i < splitArr.length; i++) {
			String temp = splitArr[i];
			temp = temp.trim();
			if (!"".equalsIgnoreCase(temp)) {
				catTermsList.add(splitArr[i]);
			}

		}
		if (!catTermsList.contains(cat)) {

			catTermsList.add(cat);
		}
		/*
		 * for (int j=0;j<catTermsList.size();j++){ String temp =
		 * (String)catTermsList.get(j); System.out.println(temp); }
		 */

		return catTermsList;
	}

	public static Date getYandexDate(String yandexmodtime) {
		Date dt = new Date();
		if (yandexmodtime.indexOf("T") > 0) {
			String[] split = yandexmodtime.split("T");
			String ymd = StrUtil.nonNull(split[0]);
			if (!"".equalsIgnoreCase(ymd)) {
				dt = StrUtil.getDate(ymd, yandexdateformat);
			}
		}

		return dt;
	}

	public static Date getFarooDate(String farooDtStr) {
		Date dt = new Date();
		if (!"".equalsIgnoreCase(farooDtStr)) {
			Long flong = new Long(farooDtStr);
			dt = new Date(flong);
		}

		return dt;
	}

	public static Date getTumblrDate(String tumblrDtStr) {
		Date dt = new Date();
		if (!"".equalsIgnoreCase(tumblrDtStr)) {
			tumblrDtStr = tumblrDtStr.replaceAll(" GMT", "");
			dt = StrUtil.getDate(tumblrDtStr, tumblrdateformat);
		}
		// System.out.println(dt.toString());

		return dt;
	}
	//2015-06-13T14:01:00.000+03:00
	public static Date getWebhoseDate(String whdate){
		Date dt = new Date();
		if(!"".equalsIgnoreCase(whdate)){
			whdate = whdate.substring(0, whdate.indexOf("T"));
			dt = StrUtil.getDate(whdate, webhosedateformat);
		}
		
		return dt;
	}

	public static int getRandInt() {
		Random rand = new Random();
		Long seed = System.currentTimeMillis();
		rand.setSeed(seed);
		int num = rand.nextInt(5) + 1;
		return num;
	}

	public static Integer getDaysAgo(Date date) {
		Date currentdate = new Date();
		long curr = currentdate.getTime();
		if (date == null) {
			return 1;
		}
		long passed = date.getTime();
		long diff = curr - passed;
		long diffdays = diff / (24 * 60 * 60 * 1000);
		System.out.println("diffdays: " + diffdays);
		Integer diffInt = new Integer(new Long(diffdays).toString());
		System.out.println("diffInt: " + diffInt.intValue());
		return diffInt;
	}

	public static String removeColon(String input) {
		if (input.indexOf(":") > -1) {
			input = input.substring((input.indexOf(":") + 1), input.length());
			input = input.trim();
		}
		return input;
	}

	public static void writeDown() {
		String list = "January,February,March,April,May,June,July,August,September,October,November,December,s,th,ish,start,starts,period,periods,a,an,and,will,would,can,could,shall,should,are,as,at,although,be,but,by,can,for,from,generally,if,in,into,is,it,no,not,of,on,or,such,that,the,their,then,there,these,they,this,to,was,will,with,not,also,very,often,however,too,usually,really,early,never,always,sometimes,together,likely,simply,generally,instead,actually,again,rather,almost,especial,ever,quickly,probably,already,below,directly,therefore,else,thus,easily,eventually,exactly,certainly,normally,currently,extremely,finally,constantly,properly,soon,specifically,ahead,daily,highly,immediately,relatively,slowly,fairly,primarily,completely,ultimately,widely,recently,seriously,frequently,fully,mostly,naturally,nearly,occasionally,carefully,clearly,essentially,possibly,slightly,somewhat,equally,greatly,necessarily,personally,rarely,regularly,similarly,basically,closely,effectively,initially,literally,mainly,merely,gently,hopefully,originally,roughly,significantl,totally,twice,elsewhere,everywhere,perfectly,physically,suddenly,truly,virtually,altogether,anyway,automaticall,deeply,definitely,deliberately,hardly,readily,terribly,unfortunatel,forth,briefly,moreover,strongly,honestly,previously,as,there,when,how,so,up,out,no,only,well,then,first,where,why,now,around,once,down,off,here,tonight,away,today,far,quite,later,above,yet,maybe,otherwise,near,forward,somewhere,anywhere,please,forever,somehow,absolutely,abroad,yeah,nowhere,tomorrow,yesterday,the,to,in,on,by,more,about,such,through,new,just,any,each,much,before,between,free,right,best,since,both,sure,without,back,better,enough,lot,small,though,less,little,under,next,hard,real,left,least,short,last,within,along,lower,true,bad,across,clear,easy,full,close,late,proper,fast,wide,item,wrong,ago,behind,quick,straight,direct,extra,morning,pretty,overall,alone,bright,flat,whatever,slow,clean,fresh,whenever,cheap,thin,cool,fair,fine,smooth,false,thick,collect,nearby,wild,apart,none,strange,tourist,aside,loud,super,tight,gross,ill,downtown,honest,ok,pray,weekly,,Accidentally,Always,Angrilya,Anxiously,Awkwardly,Badlybad,Blindly,Boastfully,Boldlybo,Bravelyb,Brightly,Cheerfully,Coyly,Crazily,Defiantly,Deftlyde,Deliberately,Devotedly,Doubtfully,Dramatically,Dutifully,Eagerly,Elegantly,Enormously,Evenly,Eventually,Exactly,Faithfully,Finally,Foolishly,Fortunately,Frantically,Frequently,Gleefully,Gracefully,Happily,Hastily,Honestly,Hopelessly,Hourly,Hungrily,Innocently,Inquisitively,Irritably,Jealously,Justlyju,Kindly,The,Lazily,Loosely,The,Madly,Merrily,Mortally,Mysteriously,Nervously,Nevernev,Obediently,Obnoxiously,Occasionally,Oftenoft,Only The on,Perfectly,Politely,Poorly,Powerfully,Promptly He,Quicklyq,Rapidlyr,Rarelyra,Really The,Regularly,Rudely,Safely,Seldomse,Selfishly,Seriously,Shakily,Sharply,Silently,Slowlysl,Solemnly,Sometimes,Speedily,Steadily,Sternlys,Technically,Tediously,Tenderly,Terrifically,Tightly,Totally,Tremendously,Unexpectedly,Usuallyu,Victoriously,Vivaciously,Warmly,Wearily,Weekly,Wildly,Yearly,used,important,every,large,available,popular,able,basic,known,various,difficult,several,united,historical,hot,useful,mental,scared,additional,emotional,old,political,similar,healthy,financial,medical,traditional,federal,entire,strong,actual,significant,successful,electrical,expensive,pregnant,intelligent,interesting,poor,happy,responsible,cute,helpful,recent,willing,nice,wonderful,impossible,serious,huge,rare,technical,typical,competitive,critical,electronic,immediate,aware,educational,environmenta,global,legal,relevant,accurate,capable,dangerous,dramatic,efficient,powerful,foreign,hungry,practical,psychological,severe,suitable,numerous,sufficient,unusual,consistent,cultural,existing,famous,pure,afraid,obvious,careful,latter,obviously,unhappy,acceptable,aggressive,boring,distinct,eastern,logical,reasonable,strict,successfully,administrati,automatic,civil,former,massive,southern,unfair,visible,alive,angry,desperate,exciting,friendly,lucky,realistic,sorry,ugly,unlikely,anxious,comprehensive,curious,impressive,informal,inner,pleasant,sexual,sudden,terrible,unable,weak,wooden,asleep,confident,conscious,decent,embarrassed,guilty,lonely,mad,nervous,odd,remarkable,substantial,suspicious,tall,tiny,more,some,one,all,many,most,other,such,even,new,just,good,any,each,much,own,great,another,same,few,free,right,still,best,public,human,both,local,sure,better,general,specific,enough,long,small,less,high,certain,little,common,next,simple,hard,past,big,possible,particular,real,major,personal,current,left,national,least,natural,physical,short,last,single,individual,main,potential,professional,internationa,lower,open,according,alternative,special,working,true,whole,clear,dry,easy,cold,commercial,full,low,primary,worth,necessary,positive,present,close,creative,green,late,fit,glad,proper,complex,content,due,effective,middle,regular,fast,independent,original,wide,beautiful,complete,active,negative,safe,visual,wrong,ago,quick,ready,straight,white,direct,excellent,extra,junior,pretty,unique,classic,final,overall,private,separate,western,alone,familiar,official,perfect,bright,broad,comfortable,flat,rich,warm,young,heavy,valuable,correct,leading,slow,clean,fresh,normal,secret,tough,brown,cheap,deep,objective,secure,thin,chemical,cool,extreme,exact,fair,fine,formal,opposite,remote,total,vast,lost,smooth,dark,double,equal,firm,frequent,internal,sensitive,constant,minor,previous,raw,soft,solid,weird,amazing,annual,busy,dead,false,round,sharp,thick,wise,equivalent,initial,narrow,nearby,proud,spiritual,wild,adult,apart,brief,crazy,prior,rough,sad,sick,strange,external,illegal,loud,mobile,nasty,ordinary,royal,senior,super,tight,upper,yellow,dependent,funny,gross,ill,spare,sweet,upstairs,usual,brave,calm,dirty,downtown,grand,honest,loose,male,quiet,brilliant,dear,drunk,empty,female,inevitable,neat,ok,representati,silly,slight,smart,stupid,temporary,weekly,that,this,what,which,time,these,work,no,only,then,first,money,over,business,his,game,think,after,life,day,home,economy,away,either,fat,key,training,top,level,far,fun,kind,future,action,live,period,subject,mean,stock,chance,beginning,upset,head,material,car,appropriate,inside,outside,standard,medium,choice,north,square,born,capital,shot,front,living,plastic,express,feeling,otherwise,plus,savings,animal,budget,minute,character,maximum,novel,plenty,select,background,forward,glass,joint,master,red,vegetable,ideal,kitchen,mother,party,relative,signal,street,connect,minimum,sea,south,status,daughter,hour,trick,afternoon,gold,mission,agent,corner,east,neither,parking,routine,swimming,winter,airline,designer,dress,emergency,evening,extension,holiday,horror,mountain,patient,proof,west,wine,expert,native,opening,silver,waste,plane,leather,purple,specialist,bitter,incident,motor,pretend,prize,resident";
		String[] listArr = list.split(",");
		for (int i = 0; i < listArr.length; i++) {
			String temp = listArr[i];
			temp = temp.toLowerCase();
			System.out.println(temp);
		}
	}

	public static String getHash(String input) {
		int hash = input.hashCode();
		String hashcode = new Integer(hash).toString();
		return hashcode;
	}

	public static String getHost(String url) {
		URL u = null;
		try {
			u = new URL(url);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String host = StrUtil.nonNull(u.getHost());
		if (host.indexOf(BLOGSPOTC) > -1) {
			host = BLOGSPOTC;
		} else if (host.indexOf(WORDPRESSC) > -1) {
			host = WORDPRESSC;
		} else if (host.indexOf(BLOGGERC) > -1) {
			host = BLOGGERC;
		} else if (host.indexOf(TUMBLRC) > -1) {
			host = TUMBLRC;
		} else if (host.indexOf(TYPEPADC) > -1) {
			host = TYPEPADC;
		} else if (host.indexOf(PINTERESTC) > -1) {
			host = PINTERESTC;
		} else if (host.indexOf(INSTAGRAMC) > -1) {
			host = INSTAGRAMC;
		} else if (host.indexOf(VKC) > -1) {
			host = VKC;
		} else if (host.indexOf(FLICKRC) > -1) {
			host = FLICKRC;
		} else if (host.indexOf(VINEC) > -1) {
			host = VINEC;
		} else if (host.indexOf(AMAZONC) > -1) {
			host = AMAZONC;
		} else if (host.indexOf(EPINIONSC) > -1) {
			host = EPINIONSC;
		} else if (host.indexOf(TRIPADVISORC) > -1) {
			host = TRIPADVISORC;
		} else if (host.indexOf(YELPC) > -1) {
			host = YELPC;
		} else if (host.indexOf(MOUTHSHUTC) > -1) {
			host = MOUTHSHUTC;
		} else if (host.indexOf(EBAYC) > -1) {
			host = EBAYC;
		} else if (host.indexOf(WALMARTC) > -1) {
			host = WALMARTC;
		} else if (host.indexOf(BESTBUYC) > -1) {
			host = BESTBUYC;
		} else if (host.indexOf(HOMEDEPOTC) > -1) {
			host = HOMEDEPOTC;
		} else if (host.indexOf(SEARSC) > -1) {
			host = SEARSC;
		} else if (host.indexOf(QUORAC) > -1) {
			host = QUORAC;
		} else {
			host = host;
		}
		return host;
	}

	public static String getChannel(String url) {
		URL u = null;
		try {
			u = new URL(url);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String host = StrUtil.nonNull(u.getHost());
		if (host.indexOf(BLOGSPOTC) > -1) {
			host = BLOGCH;
		} else if (host.indexOf(WORDPRESSC) > -1) {
			host = BLOGCH;
		} else if (host.indexOf(BLOGGERC) > -1) {
			host = BLOGCH;
		} else if (host.indexOf(TUMBLRC) > -1) {
			host = BLOGCH;
		} else if (host.indexOf(TYPEPADC) > -1) {
			host = BLOGCH;
		} else if (host.indexOf(PINTERESTC) > -1) {
			host = IMAGECH;
		} else if (host.indexOf(INSTAGRAMC) > -1) {
			host = IMAGECH;
		} else if (host.indexOf(VKC) > -1) {
			host = FORUMCH;
		} else if (host.indexOf(FLICKRC) > -1) {
			host = IMAGECH;
		} else if (host.indexOf(VINEC) > -1) {
			host = VIDEOCH;
		} else if (host.indexOf(AMAZONC) > -1) {
			host = REVIEWCH;
		} else if (host.indexOf(EPINIONSC) > -1) {
			host = REVIEWCH;
		} else if (host.indexOf(TRIPADVISORC) > -1) {
			host = REVIEWCH;
		} else if (host.indexOf(YELPC) > -1) {
			host = REVIEWCH;
		} else if (host.indexOf(MOUTHSHUTC) > -1) {
			host = REVIEWCH;
		} else if (host.indexOf(EBAYC) > -1) {
			host = REVIEWCH;
		} else if (host.indexOf(WALMARTC) > -1) {
			host = REVIEWCH;
		} else if (host.indexOf(BESTBUYC) > -1) {
			host = REVIEWCH;
		} else if (host.indexOf(HOMEDEPOTC) > -1) {
			host = REVIEWCH;
		} else if (host.indexOf(SEARSC) > -1) {
			host = REVIEWCH;
		} else if (host.indexOf(QUORAC) > -1) {
			host = FORUMCH;
		} else {
			host = GOOGLESEARCHC;
		}
		return host;
	}

	public static Long getuserrating() {
		Random rand = new Random();
		rand.setSeed(System.currentTimeMillis());
		int num = rand.nextInt(3);
		Long val = new Long(num);
		// System.out.println(val);
		return val;
	}

	public static String getTitleAlt(String webUrl) throws Exception {
		String title = "";
		if (webUrl == null) {
			return title;
		}
		if ("".equalsIgnoreCase(webUrl)) {
			return title;
		}
		if (webUrl.indexOf(" ") > 0) {
			return title;
		}
		UserAgent userAgent = new UserAgent();
		try {
			userAgent.visit(webUrl);
		} catch (Exception e) {
			System.out.println("TweetWithSentiments JAUNT parse error "
					+ e.getMessage());
		}
		Element titleelem = userAgent.doc.findFirst("<title>");
		if (titleelem != null) {
			title = titleelem.getText();
		}
		try {
			userAgent.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
		}
		return title;
	}

	public static String getMeta(String webUrl) {
		String meta = "";
		if (webUrl == null) {
			return meta;
		}
		if ("".equalsIgnoreCase(webUrl)) {
			return meta;
		}

		UserAgent userAgent = new UserAgent();
		try {
			userAgent.visit(webUrl);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		try {
			Element metatitleelem = userAgent.doc
					.findFirst("<meta property=og:description>");
			if (metatitleelem != null) {
				String m = metatitleelem.getAt("content");
				System.out.println("m : " + m);
			}

		} catch (NotFound e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {

			Element keywordelem = userAgent.doc
					.findFirst("<meta name=keywords>");
			if (keywordelem != null) {
				String k = keywordelem.getAt("content");
				System.out.println("k : " + k);
			}
		} catch (NotFound e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return meta;

	}
	public static String getThumb(String webUrl)throws Exception{
		org.jsoup.nodes.Document doc = null;
		try {
			doc = Jsoup.connect(webUrl).get();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		String imageUrl = null;
		org.jsoup.select.Elements metaOgImage = null;
		if(doc!=null){
			metaOgImage = doc.select("meta[property=og:image]");
		    if (metaOgImage!=null) {
		        imageUrl = StrUtil.nonNull(metaOgImage.attr("content"));
		        if(!"".equalsIgnoreCase(imageUrl)){
		        	System.out.println("1");
		        	return imageUrl;
		        }
		    }
		}
		org.jsoup.select.Elements elements = null;
		try {
			elements = doc.getElementsByTag("IMG");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		String retStr = "";
		if(elements!=null){
			
			if(elements.size()>1){
				System.out.println("2");
				retStr = elements.get(1).attr("src");
				return retStr;
			}else{
				System.out.println("3");
				retStr = elements.get(0).attr("src");
				return retStr;
			}
		}
		
		return "";
	}

	public static String getThumbOld(String webUrl) throws Exception {
		String webpreviewimage = "";
		if (webUrl == null) {
			return webpreviewimage;
		}
		if ("".equalsIgnoreCase(webUrl)) {
			return webpreviewimage;
		}
		if (webUrl.indexOf(" ") > 0) {
			return webpreviewimage;
		}
		UserAgent userAgent = new UserAgent();
		try {
			userAgent.visit(webUrl);
		} catch (Exception e) {
			System.out.println("TweetWithSentiments JAUNT parse error "
					+ e.getMessage());
		}
		Elements metalot = null;
		try {
			metalot = userAgent.doc.findFirst("<head>").findEvery("<meta>");
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			System.out.println(e1.getMessage());
		}
		List tempList = new ArrayList();
		if (metalot != null) {
			tempList = metalot.toList();
		}
		List holdList = new ArrayList();

		for (int i = 0; i < tempList.size(); i++) {
			Element meta = (Element) tempList.get(i);

			List attNames = meta.getAttributeNames();

			for (int j = 0; j < attNames.size(); j++) {
				String att = (String) attNames.get(j);

				String kk = meta.getAttx("content");
				if (kk.startsWith("http")) {
					if (!holdList.contains(kk)) {
						if (kk.indexOf(" ") < 0 && kk.indexOf("%20") < 0) {
							holdList.add(kk);
						}

					}

				}
			}

		}

		for (int i = 0; i < holdList.size(); i++) {
			String val = (String) holdList.get(i);
			String temp = val.toLowerCase();

			if (temp.endsWith(".jpg") || temp.endsWith(".png")
					|| temp.endsWith(".jpeg") || temp.endsWith(".gif")
					|| temp.indexOf("gravatar") > 0) {
				if (temp.indexOf("favicon") < 0) {
					return val;
				}

			} else {
				holdList.remove(val);
			}

		}
		if (holdList.size() > 0) {
			webpreviewimage = (String) holdList.get(0);
		}
		try {
			userAgent.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
		}
		return webpreviewimage;
	}

	public static String prepareforSentinet(String term) {
		StringBuffer sbfr = new StringBuffer();
		if (term.indexOf(" ") < 1) {
			return term;
		}

		if (term.indexOf(" ") > 0) {
			String termarr[] = term.split(" ");
			for (int i = 0; i < termarr.length; i++) {
				String temp = termarr[i];
				temp = temp.trim();
				if (!"".equalsIgnoreCase(temp)) {
					sbfr.append(temp + "_");
				}

			}
		}
		term = sbfr.toString();
		if (term.endsWith("_")) {
			term = term.substring(0, (term.length() - 1));
		}
		// System.out.println(term);
		return term;
	}

	public static void testresultjson() {
		ResultReadyJSONResponse rrjr = new ResultReadyJSONResponse();
		rrjr.setType(disambiguationtype);
		JSONdisambiguationtype jd = new JSONdisambiguationtype();
		jd.setId(new Long(12));
		List interestlist = new ArrayList();
		interestlist.add(new Long(22));
		interestlist.add(new Long(34));
		jd.setInterests(interestlist);
		rrjr.setData(jd);
		Gson gson = new Gson();
		String resp = gson.toJson(rrjr);
		System.out.println(resp);
	}

	private static void checkExtn() {
		String temp = "/home/sss/29283394/image.mp4";
		String a = (FileUtils.getExtension(temp));
		System.out.println(a);
		if (a.startsWith(".")) {
			a = a.substring((a.indexOf(".") + 1), a.length());
		}
		// a = a.replaceAll(".", "");
		a = a.trim();
		System.out.println(a);
	}

	private static void checkIds() {
		int a = 0;
		int b = 1;
		if (b != 0 && a == 0) {
			System.out.println("match");
		} else {
			System.out.println("Fail");
		}

		List list = new ArrayList();
		list.add("");
		list.add("");
		System.out.println(list.size());
		list.clear();
		System.out.println(list.size());
	}

	private static void checkDouble() {
		int pos = 22;
		int posplus = 5;
		int neu = 13;
		int neg = 7;
		int negplus = 14;

		double finalpos = 0;
		double finalneg = 0;

		finalpos = pos + 1.5 * posplus;
		finalneg = neg + 1.5 * negplus;
		Double finalposd = new Double(finalpos);
		Double finalnegd = new Double(finalneg);

		Long flpos = new Long(finalposd.longValue());
		Long flneg = new Long(finalnegd.longValue());
		Long flneu = new Long(neu);

		System.out.println("flpos: " + flpos.toString());
		System.out.println("flneg: " + flneg.toString());
		System.out.println("flneu: " + flneu.toString());
	}

	public static Long getRCount() {
		Random rand = new Random();
		rand.setSeed(System.currentTimeMillis());
		int num = rand.nextInt(5);
		if (num == 0) {
			num = 1;
		}
		System.out.println(num);
		return new Long(num);
	}

	public static String getDomain(String url) {
		if (!url.startsWith("http") && !url.startsWith("https")) {
			url = "http://" + url;
		}
		String domain = "";
		try {
			URL u = new URL(url);
			domain = u.getHost();
			if (domain.startsWith("www")) {
				domain = domain.substring("www".length() + 1);
				if (domain.endsWith(".com") || domain.endsWith(".co.in")
						|| domain.endsWith(".net") || domain.endsWith(".co")
						|| domain.endsWith(".in") || domain.endsWith(".ru")
						|| domain.endsWith(".gov")) {
					domain = domain.substring(0, domain.indexOf("."));
				}
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(domain.indexOf(".")>0){
			domain = domain.substring(0,domain.indexOf("."));
		}
		return domain;

	}

	public static String getTitle(String url) throws Exception {
		org.jsoup.nodes.Document doc = Jsoup.connect(url).get();
		String text = doc.body().text();
		String title = doc.title();
		return title;

	}

	private static void testFunny() {
		String str = "http://pngimg.com/img/insects/butterfly&sa=U&ei=Vk50VdfIHYbZmgWUh4DgDg&ved=0CBUQ9QEwAA&usg=AFQjCNFY3vWR1_-GRwJ9ParO7Zod72cKZw";
		str = str.substring(0, str.indexOf("&sa"));
		System.out.println(str);
	}

	public static Map process(List list) {
		SingleStopper ss = SingleStopper.getInstance();
		List tlist = new ArrayList();
		for (int i = 0; i < list.size(); i++) {
			String temp = (String) list.get(i);
			/*
			 * System.out.println(temp); try { Thread.sleep(1000); } catch
			 * (InterruptedException e) { // TODO Auto-generated catch block
			 * e.printStackTrace(); }
			 */
			String result = ss.cullPhrase(temp);
			tlist.add(result);
		}
		Map map = new HashMap();
		for (int i = 0; i < tlist.size(); i++) {
			String tmp = (String) tlist.get(i);
			// System.out.println(tmp);
			String[] tmparr = tmp.split(" ");
			for (int j = 0; j < tmparr.length; j++) {
				String xyz = StrUtil.nonNull(tmparr[j]);
				if(!"".equalsIgnoreCase(xyz)){
					// System.out.println(xyz);
					 xyz = DataClean.clean(xyz);
					
					if (map.containsKey(xyz)) {
						Integer value = (Integer) map.get(xyz);
						int val = value.intValue();
						val = val + 1;
						value = new Integer(val);
						map.put(xyz, value);

					} else {
						map.put(xyz, new Integer(1));
					}
				}
			
			}
		}
		Map smap = TraverseTaxonomy.sortByComparator(map, false);

		return smap;

	}

	private static void distribute() {
		String path = "C:/Mywork/StreetBuzz/Lingpipe/tmp/";
		File file = new File(path);
		String[] tmp = file.list();
		for (int i = 1; i < 150; i++) {
			File newdir = new File(path + i);
			newdir.mkdir();
		}

	}

	private static void fetch(String url) {
		org.jsoup.nodes.Document doc = null;
		try {
			doc = Jsoup.connect(url).get();
		} catch (Exception e1) {
			System.out.println(e1.getMessage());
		}
		String text = "";
		String title = "";
		if (doc != null) {
			try {
				text = doc.body().text();
				title = doc.title();
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
		System.out.println(title);
	}
	
	public static Character isAlpha(String name) {
		char blank = " ".charAt(0);
	    char[] chars = name.toCharArray();

	    for (char c : chars) {
	        if(Character.isLetter(c)) {
	            return new Character(c);
	        }
	    }

	    return new Character(blank);
	}
	
	public static String getFirstletter(String entry){
		entry = entry.trim();
		entry = entry.toLowerCase();
		Character character = isAlpha(entry);
		String input = character.toString();
		if(input.startsWith("a")){
			return "a";
		}
		if(input.startsWith("b")){
			return "b";
		}
		if(input.startsWith("c")){
			return "c";
		}
		if(input.startsWith("d")){
			return "d";
		}
		if(input.startsWith("e")){
			return "e";
		}
		if(input.startsWith("f")){
			return "f";
		}
		if(input.startsWith("g")){
			return "g";
		}
		if(input.startsWith("h")){
			return "h";
		}
		if(input.startsWith("i")){
			return "i";
		}
		if(input.startsWith("j")){
			return "j";
		}
		if(input.startsWith("k")){
			return "a";
		}
		if(input.startsWith("a")){
			return "k";
		}
		if(input.startsWith("l")){
			return "l";
		}
		if(input.startsWith("m")){
			return "m";
		}
		if(input.startsWith("n")){
			return "n";
		}
		if(input.startsWith("o")){
			return "o";
		}
		if(input.startsWith("p")){
			return "p";
		}
		if(input.startsWith("q")){
			return "q";
		}
		if(input.startsWith("r")){
			return "r";
		}
		if(input.startsWith("s")){
			return "s";
		}
		if(input.startsWith("t")){
			return "t";
		}
		if(input.startsWith("u")){
			return "u";
		}
		if(input.startsWith("v")){
			return "v";
		}
		if(input.startsWith("w")){
			return "w";
		}
		if(input.startsWith("x")){
			return "x";
		}
		if(input.startsWith("y")){
			return "y";
		}
		if(input.startsWith("z")){
			return "z";
		}
		return "";
	}
	
	public static boolean isAlphabet(String name) {
	    char[] chars = name.toCharArray();

	    for (char c : chars) {
	        if(!Character.isLetter(c)) {
	            return false;
	        }
	    }

	    return true;
	}
	
	public static void getTweet()throws Exception{
		File file = new File("C:/Mywork/StreetBuzz/UrviTweets/Tweets.txt");
		List list = FileUtils.readFile(file);
		System.setOut(new PrintStream(new BufferedOutputStream(
				new FileOutputStream("C:/Mywork/StreetBuzz/UrviTweets/"+"neg.txt"))));
		for (int i=0;i<list.size();i++){
			String temp = StrUtil.nonNull((String)list.get(i));
			temp = temp.trim();
			if(!"".equalsIgnoreCase(temp)){
				if(!temp.startsWith("+1 ***")&&(temp.indexOf("1 ***"))>-1)
				System.out.println(temp);
			}
			
		}
	}
	private static void percent(){
		float  a = 77;
		float  b = 23;
		float  c = 0;
		float  sum = a+b+c;
		System.out.println("sum: "+sum);
		float  ax = (a/sum)*100;
		float  bx = (b/sum)*100;
		float  cx = (c/sum)*100;
		
		int k = new Float(ax).intValue();
		int m = new Float(bx).intValue();
		int p = new Float(cx).intValue();
		System.out.println(k+", "+m+", "+p);
	}
	
	public static boolean imageExtnCheck(String imageurl){
		
		String imageurllower = imageurl.toLowerCase();
		if(imageurllower.endsWith(".jpg")){
			return true;
		}
		if(imageurllower.endsWith(".gif")){
			return true;
		}
		if(imageurllower.endsWith(".png")){
			return true;
		}
		if(imageurllower.endsWith(".jpeg")){
			return true;
		}
		return false;
	}
	
	public static void main(String args1[]) {
		try {
			fetch("http://www.edmunds.com/jaguar/f-type/2016/road-test.html");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
