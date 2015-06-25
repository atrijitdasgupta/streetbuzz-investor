/**
 * 
 */
package com.crowd.streetbuzzalgo.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import com.crowd.streetbuzz.common.Constants;
import com.crowd.streetbuzz.dao.implementation.ConversationCardDAO;
import com.crowd.streetbuzz.dao.implementation.MediaFilesDAO;
import com.crowd.streetbuzz.dao.implementation.ProtoSearchCardDAO;
import com.crowd.streetbuzz.dao.implementation.ProtoVoicesDAO;
import com.crowd.streetbuzz.dao.implementation.ProtoVoicesDetailsDAO;
import com.crowd.streetbuzz.dao.implementation.VoicesDAO;
import com.crowd.streetbuzz.dao.implementation.VoicesDetailsDAO;
import com.crowd.streetbuzz.dao.implementation.WordCloudModelDAO;
import com.crowd.streetbuzz.dao.implementation.WordCloudStoreDAO;
import com.crowd.streetbuzz.json.GraphJson;
import com.crowd.streetbuzz.json.JsonBarChartNegative;
import com.crowd.streetbuzz.json.JsonBarChartNeutral;
import com.crowd.streetbuzz.json.JsonBarChartPositive;
import com.crowd.streetbuzz.json.JsonLinechart;
import com.crowd.streetbuzz.json.JsonSentiment;
import com.crowd.streetbuzz.json.JsonWordCloud;
import com.crowd.streetbuzz.model.ConversationCard;
import com.crowd.streetbuzz.model.ProtoSearchCard;
import com.crowd.streetbuzz.model.ProtoVoices;
import com.crowd.streetbuzz.model.ProtoVoicesDetails;
import com.crowd.streetbuzz.model.Voices;
import com.crowd.streetbuzz.model.VoicesDetails;
import com.crowd.streetbuzzalgo.jfreechart.JFreeChartGen;
import com.crowd.streetbuzzalgo.wordcloud.WordCloudGen;
import com.google.gson.Gson;

/**
 * @author Atrijit
 * 
 */
public class GraphDataUtils implements Constants {
	private static final String POSITIVEPLUS = "positiveplus";

	private static final String POSITIVE = "pos";

	private static final String NEUTRAL = "neu";

	private static final String NEGATIVEPLUS = "neg";

	private static final String NEGATIVE = "negativeplus";
	/**
	 * 
	 */
	public GraphDataUtils() {
		// TODO Auto-generated constructor stub
	}

	// Method to generate graph during card prcessing - this is used finally

	public static ConversationCard getCardData(VoicesDAO voicesDAO,
			VoicesDetailsDAO voicesDetailsDAO, Long cardid,
			MediaFilesDAO mediaFilesDAO, 
			WordCloudStoreDAO wordCloudStoreDAO, ConversationCard cc) {
		String barcharturl = "";
		String linemapurl = "";
		String wordcloudurl = "";
		long start = System.currentTimeMillis();
			Long positivecount = new Long(0);
			Long negativecount = new Long(0);
			Long neutralcount = new Long(0);
			if (cc != null) {
				positivecount = cc.getPositivevoices();
				negativecount = cc.getNegativevoices();
				neutralcount = cc.getNeutralvoices();
			}
			float  a = positivecount.floatValue();
			float b = negativecount.floatValue();
			float c = neutralcount.floatValue();
			float  sum = a+b+c;
			float  ax = (a/sum)*100;
			float  bx = (b/sum)*100;
			float  cx = (c/sum)*100;
			int k = new Float(ax).intValue();
			int m = new Float(bx).intValue();
			int p = new Float(cx).intValue();
			Long positivecountpercent = new Long(k);
			Long negativecountpercent = new Long(m);
			Long neutralcountpercent = new Long(p);
			
			Map map = new HashMap();

			map.put("neutral", neutralcountpercent);
			map.put("negative", negativecountpercent);
			map.put("positive", positivecountpercent);
			long one = System.currentTimeMillis();
			System.out.println("****************DATA PREPN BAR GRAPH*****************" +((one  - start)/1000)+"secs");

			try {
				barcharturl = JFreeChartGen.createBarChart(map, mediaFilesDAO,
						"320", "240");
			} catch (Exception e2) {
				System.out.println(e2.getMessage());
			}
			long two = System.currentTimeMillis();
			System.out.println("****************GEN BAR GRAPH*****************" +((two  - one)/1000)+"secs");
			
			List voicesList = voicesDAO.getAllNNRecordsbyCardId(cardid);
			List voicesDetailsList = voicesDetailsDAO.getAllNNRecordsbyCardid(cardid);
			//Map linemap = getLineChartDetails(voicesList, voicesDetailsDAO);
			Map linemap = getLineChartDetails(voicesList, voicesDetailsList);

			TreeMap posmap = (TreeMap) linemap.get(POSITIVE);
			TreeMap negmap = (TreeMap) linemap.get(NEGATIVE);
			long three = System.currentTimeMillis();
			System.out.println("****************DATA PREPN LINE GRAPH*****************" +((three  - two)/1000)+"secs");

			try {
				linemapurl = JFreeChartGen.createLineChart(posmap,negmap,mediaFilesDAO);
			} catch (Exception e1) {
				System.out.println(e1.getMessage());
			}
			long four = System.currentTimeMillis();
			System.out.println("****************GEN LINE GRAPH*****************" +((four  - three)/1000)+"secs");
			
			List wclist = wordCloudStoreDAO.getAllRecordsbyCardId(cardid);
			try {
				wordcloudurl = WordCloudGen.createCloud(wclist, mediaFilesDAO,
						"320", "240");
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
			cc.setBarcharturl(barcharturl);
			cc.setLinemapurl(linemapurl);
			cc.setWordcloudurl(wordcloudurl);
		return cc;
	}

	// Method to generate graph on client call
	public static String getCardData(VoicesDAO voicesDAO,
			VoicesDetailsDAO voicesDetailsDAO, Long cardid, String channel,
			ConversationCardDAO conversationCardDAO,
			MediaFilesDAO mediaFilesDAO, WordCloudModelDAO wordCloudModelDAO,
			WordCloudStoreDAO wordCloudStoreDAO, String height, String width) {
		// Bar Graph
		ConversationCard cc = (ConversationCard) conversationCardDAO
				.getObjectById(cardid);

		String barcharturl = StrUtil.nonNull(cc.getBarcharturl());
		String linemapurl = StrUtil.nonNull(cc.getLinemapurl());
		String wordcloudurl = StrUtil.nonNull(cc.getWordcloudurl());
		boolean updatecard = false;

		if ("".equalsIgnoreCase(barcharturl)) {
			updatecard = true;
			Long positivecount = new Long(0);
			Long negativecount = new Long(0);
			Long neutralcount = new Long(0);
			if (cc != null) {
				positivecount = cc.getPositivevoices();
				negativecount = cc.getNegativevoices();
				neutralcount = cc.getNeutralvoices();
			}
			Map map = new HashMap();

			map.put("neutral", neutralcount);
			map.put("negative", negativecount);
			map.put("positive", positivecount);

			try {
				barcharturl = JFreeChartGen.createBarChart(map, mediaFilesDAO,
						width, height);
			} catch (Exception e2) {
				System.out.println(e2.getMessage());
			}
		}

		if ("".equalsIgnoreCase(linemapurl)) {
			updatecard = true;
			// Line Graph
			List voicesList = voicesDAO.getAllRecordsbyCardId(cardid);
			List voicesDetailsList = voicesDetailsDAO.getAllNNRecordsbyCardid(cardid);
			Map linemap = getLineChartDetails(voicesList, voicesDetailsDAO);
			//Map linemap = getLineChartDetails(voicesList, voicesDetailsList);

			TreeMap posmap = (TreeMap) linemap.get("positive");
			TreeMap negmap = (TreeMap) linemap.get("negative");

			try {
				linemapurl = JFreeChartGen.createXYChart(posmap, negmap,
						mediaFilesDAO, width, height);
			} catch (Exception e1) {
				System.out.println(e1.getMessage());
			}
		}

		if ("".equalsIgnoreCase(wordcloudurl)) {
			updatecard = true;
			List wclist = wordCloudStoreDAO.getAllRecordsbyCardId(cardid);
			try {
				wordcloudurl = WordCloudGen.createCloud(wclist, mediaFilesDAO,
						width, height);
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
		// Word Cloud

		GraphJson graphjson = new GraphJson();
		graphjson.setOverallbuzzurl(barcharturl);
		graphjson.setBuzzacrosstimeurl(linemapurl);
		graphjson.setBuzzcloudurl(wordcloudurl);
		String barchartshareurl = BARCHARTSHAREURL+cardid.toString();
		String linemapshareurl = LINEMAPSHAREURL+cardid.toString();
		String wordcloudshareurl = WORDCLOUDSHAREURL+cardid.toString();
		graphjson.setBarchartshareurl(barchartshareurl);
		graphjson.setLinemapshareurl(linemapshareurl);
		graphjson.setWordcloudshareurl(wordcloudshareurl);
		

		if (updatecard) {
			cc.setWordcloudurl(wordcloudurl);
			cc.setLinemapurl(linemapurl);
			cc.setBarcharturl(barcharturl);
			conversationCardDAO.addOrUpdateRecord(cc);
		}

		Gson gson = new Gson();
		String retStr = gson.toJson(graphjson);
		return retStr;
	}

	public static String getCardDataOld(VoicesDAO voicesDAO,
			VoicesDetailsDAO voicesDetailsDAO, Long cardid, String channel,
			ConversationCardDAO conversationCardDAO) {
		ConversationCard cc = (ConversationCard) conversationCardDAO
				.getObjectById(cardid);
		Long positivecount = new Long(0);
		Long negativecount = new Long(0);
		Long neutralcount = new Long(0);
		if (cc != null) {
			positivecount = cc.getPositivevoices();
			negativecount = cc.getNegativevoices();
			neutralcount = cc.getNeutralvoices();
		}

		List linechartList = new ArrayList();

		List voicesList = voicesDAO.getAllRecordsbyCardId(cardid);

		Map map = new HashMap();
		// map.put("linechart", linechartList);
		List newlinechartlist = getLineChartDetailsOld(voicesList,
				voicesDetailsDAO);
		map.put("linechart", newlinechartlist);
		List wordcloudList = new ArrayList();
		try {
			wordcloudList = getWordCloudList(voicesList, cc, voicesDetailsDAO);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
		}
		map.put("wordcloud", wordcloudList);

		JsonSentiment js = new JsonSentiment();
		js.setNegativevoices(negativecount);
		js.setNeutralvoices(neutralcount);
		js.setPositivevoices(positivecount);

		Gson gson = new Gson();
		StringBuffer sbfr = new StringBuffer();

		Set set = map.keySet();
		Iterator it = set.iterator();
		List tempList = new ArrayList();

		String temp1 = "";
		String temp2 = "";
		while (it.hasNext()) {
			String key = (String) it.next();
			temp1 = gson.toJson(key);

			tempList = (List) map.get(key);
			temp2 = gson.toJson(tempList);

			sbfr.append(temp1 + ":");
			sbfr.append(temp2 + ",");

		}
		String k = gson.toJson("sentiment");
		String m = gson.toJson(js);

		sbfr.append(k).append(":" + m);

		String kk = sbfr.toString();
		if (!kk.startsWith("{")) {
			kk = "{" + kk;
		}
		if (kk.endsWith("}")) {
			kk = kk + "}";
		}
		return kk;

	}
	
	private static Map getLineChartDetails(List voicesList,
			List vdList) {
		long start = System.currentTimeMillis();
		Map retMap = new HashMap();
		SortedMap posmap = new TreeMap();
		SortedMap negmap = new TreeMap();
		for (int i = 0; i < voicesList.size(); i++) {
			Voices v = (Voices) voicesList.get(i);
			Date date = v.getVoicesdate();
			if (date == null) {
				date = new Date();
			}
			String sentimentstr = StrUtil.nonNull(v.getSentimentrating());
			
			if (POSITIVE.matches(sentimentstr)) {
				if (posmap.containsKey(date)) {
					Long val = (Long) posmap.get(date);
					int valint = val.intValue() + 1;
					val = new Long(valint);
					posmap.put(date, val);
				} else {
					posmap.put(date, new Long(1));
				}
			}
			
			if (NEGATIVE.matches(sentimentstr)) {
				if (negmap.containsKey(date)) {
					Long val = (Long) negmap.get(date);
					int valint = val.intValue() + 1;
					val = new Long(valint);
					negmap.put(date, val);
				} else {
					negmap.put(date, new Long(1));
				}
			}
			
		}
		
		for (int i = 0; i < vdList.size(); i++) {
			VoicesDetails vd = (VoicesDetails) vdList.get(i);
			Date vddate = vd.getVoicedate();
			if (vddate == null) {
				vddate = new Date();
			}
			String sentimentstrvd = StrUtil.nonNull(vd.getSentimentrating());

			
				if (POSITIVE.matches(sentimentstrvd)) {
					if (posmap.containsKey(vddate)) {
						Long val = (Long) posmap.get(vddate);
						int valint = val.intValue() + 1;
						val = new Long(valint);
						posmap.put(vddate, val);
					} else {
						posmap.put(vddate, new Long(1));
					}
				}
				
				if (NEGATIVE.matches(sentimentstrvd)) {
					if (negmap.containsKey(vddate)) {
						Long val = (Long) negmap.get(vddate);
						int valint = val.intValue() + 1;
						val = new Long(valint);
						negmap.put(vddate, val);
					} else {
						negmap.put(vddate, new Long(1));
					}
				}
		}
		long end = System.currentTimeMillis();
		System.out.println("****************FOR VOICE LINE GRPH*****************" +((end  - start)/1000)+"secs");
		retMap.put(POSITIVE, posmap);
		retMap.put(NEGATIVE, negmap);
		return retMap;
	}

	private static Map getLineChartDetails(List voicesList,
			VoicesDetailsDAO voicesDetailsDAO) {
		Map retMap = new HashMap();
		SortedMap posmap = new TreeMap();
		SortedMap negmap = new TreeMap();
		for (int i = 0; i < voicesList.size(); i++) {
			long start = System.currentTimeMillis();
			Voices v = (Voices) voicesList.get(i);
			Date date = v.getVoicesdate();
			if (date == null) {
				date = new Date();
			}
			String sentimentstr = StrUtil.nonNull(v.getSentimentrating());
			//if (POSITIVE.equalsIgnoreCase(sentimentstr)) {
			if (POSITIVE.matches(sentimentstr)) {
				if (posmap.containsKey(date)) {
					Long val = (Long) posmap.get(date);
					int valint = val.intValue() + 1;
					val = new Long(valint);
					posmap.put(date, val);
				} else {
					posmap.put(date, new Long(1));
				}
			}
			//if (NEGATIVE.equalsIgnoreCase(sentimentstr)) {
			if (NEGATIVE.matches(sentimentstr)) {
				if (negmap.containsKey(date)) {
					Long val = (Long) negmap.get(date);
					int valint = val.intValue() + 1;
					val = new Long(valint);
					negmap.put(date, val);
				} else {
					negmap.put(date, new Long(1));
				}
			}
			Long vid = v.getId();
			List vdlist = voicesDetailsDAO.getAllNNRecordsbyVoices(vid);
			for (int j = 0; j < vdlist.size(); j++) {
				VoicesDetails vd = (VoicesDetails) vdlist.get(j);
				Date vddate = vd.getVoicedate();
				if (vddate == null) {
					vddate = new Date();
				}
				String sentimentstrvd = StrUtil.nonNull(vd.getSentimentrating());

			//	if (POSITIVE.equalsIgnoreCase(sentimentstrvd)) {
				if (POSITIVE.matches(sentimentstrvd)) {
					if (posmap.containsKey(date)) {
						Long val = (Long) posmap.get(date);
						int valint = val.intValue() + 1;
						val = new Long(valint);
						posmap.put(date, val);
					} else {
						posmap.put(date, new Long(1));
					}
				}
			//	if (NEGATIVE.equalsIgnoreCase(sentimentstrvd)) {
				if (NEGATIVE.matches(sentimentstrvd)) {
					if (negmap.containsKey(date)) {
						Long val = (Long) negmap.get(date);
						int valint = val.intValue() + 1;
						val = new Long(valint);
						negmap.put(date, val);
					} else {
						negmap.put(date, new Long(1));
					}
				}

			}
			long end = System.currentTimeMillis();
			System.out.println("****************FOR EACH VOICE LINE GRPH*****************" +((end  - start)/1000)+"secs");
		}
		retMap.put(POSITIVE, posmap);
		retMap.put(NEGATIVE, negmap);
		return retMap;
	}

	private static List getLineChartDetailsOld(List voicesList,
			VoicesDetailsDAO voicesDetailsDAO) {
		List retList = new ArrayList();
		SortedMap posmap = new TreeMap();
		SortedMap negmap = new TreeMap();
		for (int i = 0; i < voicesList.size(); i++) {
			Voices v = (Voices) voicesList.get(i);
			Date date = v.getVoicesdate();
			if (date == null) {
				date = new Date();
			}
			String sentimentstr = v.getSentimentrating();
			if ("positive".equalsIgnoreCase(sentimentstr)
					|| "positiveplus".equalsIgnoreCase(sentimentstr)) {
				if (posmap.containsKey(date)) {
					Long val = (Long) posmap.get(date);
					int valint = val.intValue() + 1;
					val = new Long(valint);
					posmap.put(date, val);
				} else {
					posmap.put(date, new Long(1));
				}
			}
			if ("negative".equalsIgnoreCase(sentimentstr)
					|| "negativeplus".equalsIgnoreCase(sentimentstr)) {
				if (negmap.containsKey(date)) {
					Long val = (Long) negmap.get(date);
					int valint = val.intValue() + 1;
					val = new Long(valint);
					negmap.put(date, val);
				} else {
					negmap.put(date, new Long(1));
				}
			}
			Long vid = v.getId();
			List vdlist = voicesDetailsDAO.getAllRecordsbyVoices(vid);
			for (int j = 0; j < vdlist.size(); j++) {
				VoicesDetails vd = (VoicesDetails) vdlist.get(j);
				Date vddate = vd.getVoicedate();
				if (vddate == null) {
					vddate = new Date();
				}
				String sentimentstrvd = vd.getSentimentrating();

				if ("positive".equalsIgnoreCase(sentimentstrvd)
						|| "positiveplus".equalsIgnoreCase(sentimentstrvd)) {
					if (posmap.containsKey(date)) {
						Long val = (Long) posmap.get(date);
						int valint = val.intValue() + 1;
						val = new Long(valint);
						posmap.put(date, val);
					} else {
						posmap.put(date, new Long(1));
					}
				}
				if ("negative".equalsIgnoreCase(sentimentstrvd)
						|| "negativeplus".equalsIgnoreCase(sentimentstrvd)) {
					if (negmap.containsKey(date)) {
						Long val = (Long) negmap.get(date);
						int valint = val.intValue() + 1;
						val = new Long(valint);
						negmap.put(date, val);
					} else {
						negmap.put(date, new Long(1));
					}
				}

			}

		}

		SortedSet poskeyset = new TreeSet(posmap.keySet());
		SortedSet negkeyset = new TreeSet(negmap.keySet());

		Iterator posit = poskeyset.iterator();
		Iterator negit = negkeyset.iterator();

		while (posit.hasNext()) {
			Date key = (Date) posit.next();
			Long val = (Long) posmap.get(key);
			JsonLinechart jlc = new JsonLinechart();
			jlc.setCount(val);
			jlc.setDate(StrUtil.getDateString(key, dateformat2));
			jlc.setSentiment("positive");
			jlc.setSentimentrating(val);
			retList.add(jlc);
		}

		while (negit.hasNext()) {
			Date key = (Date) negit.next();
			Long val = (Long) negmap.get(key);
			JsonLinechart jlc = new JsonLinechart();
			jlc.setCount(val);
			jlc.setDate(StrUtil.getDateString(key, dateformat2));
			jlc.setSentiment("negative");
			jlc.setSentimentrating(val);
			retList.add(jlc);
		}

		return retList;
	}

	private static List getWordCloudList(List voicesList, ConversationCard cc,
			VoicesDetailsDAO voicesDetailsDAO) {
		Long cid = cc.getId();
		List jcdlist = new ArrayList();
		Map posmap = new HashMap();
		Map negmap = new HashMap();
		for (int i = 0; i < voicesList.size(); i++) {
			Voices voices = (Voices) voicesList.get(i);
			String pos = StrUtil.nonNull(voices.getPositivephrase());
			String neg = StrUtil.nonNull(voices.getNegativephrase());
			if (!"".equalsIgnoreCase(pos)) {
				if (posmap.containsKey(pos)) {
					Integer count = (Integer) posmap.get(pos);
					int countint = count.intValue();
					countint = countint + 1;
					posmap.put(pos, new Integer(countint));
				} else {
					posmap.put(pos, new Integer(1));
				}
			}
			if (!"".equalsIgnoreCase(neg)) {
				if (negmap.containsKey(neg)) {
					Integer count = (Integer) negmap.get(neg);
					int countint = count.intValue();
					countint = countint + 1;
					negmap.put(neg, new Integer(countint));
				} else {
					negmap.put(neg, new Integer(1));
				}
			}
			List voicesdetailslist = voicesDetailsDAO
					.getAllRecordsbyVoices(voices.getId());
			for (int j = 0; j < voicesdetailslist.size(); j++) {
				VoicesDetails vd = (VoicesDetails) voicesdetailslist.get(j);
				String posvd = StrUtil.nonNull(vd.getPositivephrase());
				String negvd = StrUtil.nonNull(vd.getNegativephrase());
				if (!"".equalsIgnoreCase(posvd)) {
					if (posmap.containsKey(posvd)) {
						Integer count = (Integer) posmap.get(posvd);
						int countint = count.intValue();
						countint = countint + 1;
						posmap.put(posvd, new Integer(countint));
					} else {
						posmap.put(posvd, new Integer(1));
					}
				}
				if (!"".equalsIgnoreCase(negvd)) {
					if (negmap.containsKey(negvd)) {
						Integer count = (Integer) negmap.get(negvd);
						int countint = count.intValue();
						countint = countint + 1;
						negmap.put(negvd, new Integer(countint));
					} else {
						negmap.put(negvd, new Integer(1));
					}
				}
			}
		}

		Map sortedposMap = CollectionsUtils.sortByValue(posmap);
		Map sortednegMap = CollectionsUtils.sortByValue(negmap);

		ArrayList<String> poskeys = new ArrayList<String>(sortedposMap.keySet());
		ArrayList<String> negkeys = new ArrayList<String>(sortednegMap.keySet());

		for (int i = poskeys.size() - 1; i >= 0; i--) {
			String posword = (String) poskeys.get(i);
			Integer posvalue = (Integer) sortedposMap.get(posword);
			JsonWordCloud jc = new JsonWordCloud();
			jc.setCount(posvalue.intValue());
			jc.setType("positive");
			jc.setWord(posword);
			jcdlist.add(jc);
			if (jcdlist.size() > 5) {
				break;
			}
		}

		for (int i = negkeys.size() - 1; i >= 0; i--) {
			String negword = (String) negkeys.get(i);
			Integer negvalue = (Integer) sortednegMap.get(negword);
			JsonWordCloud jc = new JsonWordCloud();
			jc.setCount(negvalue.intValue());
			jc.setType("negative");
			jc.setWord(negword);
			jcdlist.add(jc);
			if (jcdlist.size() > 10) {
				break;
			}
		}

		return jcdlist;
	}

	private static List getWordCloudListOld(List voicesList, ConversationCard cc) {
		Long cid = cc.getId();
		int cidInt = cid.intValue();
		List jcdlist = new ArrayList();
		if (cid == 2) {
			JsonWordCloud jc1 = new JsonWordCloud();
			jc1.setCount(2);
			jc1.setWord("Top");
			jc1.setType("Positive");
			jcdlist.add(jc1);

			JsonWordCloud jc2 = new JsonWordCloud();
			jc2.setCount(1);
			jc2.setWord("Homage");
			jc2.setType("Positive");
			jcdlist.add(jc2);

			JsonWordCloud jc3 = new JsonWordCloud();
			jc3.setCount(1);
			jc3.setType("Negative");
			jc3.setWord("Buyback");
			jcdlist.add(jc3);

			JsonWordCloud jc4 = new JsonWordCloud();
			jc4.setCount(1);
			jc3.setType("Negative");
			jc3.setWord("Hits");
			jcdlist.add(jc4);
			return jcdlist;
		} else if (cid == 3) {
			return jcdlist;
		} else if (cid == 4) {
			JsonWordCloud jc1 = new JsonWordCloud();
			jc1.setCount(1);
			jc1.setWord("Interesting");
			jc1.setType("Positive");
			jcdlist.add(jc1);

			JsonWordCloud jc2 = new JsonWordCloud();
			jc2.setCount(1);
			jc2.setWord("Future");
			jc2.setType("Positive");
			jcdlist.add(jc2);
			return jcdlist;
		} else if (cid == 5) {
			JsonWordCloud jc1 = new JsonWordCloud();
			jc1.setCount(2);
			jc1.setWord("Love");
			jc1.setType("Positive");
			jcdlist.add(jc1);

			JsonWordCloud jc2 = new JsonWordCloud();
			jc2.setCount(1);
			jc2.setWord("Stopped");
			jc2.setType("Negative");
			jcdlist.add(jc2);
			return jcdlist;
		} else if (cid == 6) {
			JsonWordCloud jc1 = new JsonWordCloud();
			jc1.setCount(1);
			jc1.setWord("Patient");
			jc1.setType("Positive");
			jcdlist.add(jc1);

			JsonWordCloud jc2 = new JsonWordCloud();
			jc2.setCount(1);
			jc2.setWord("Deadly");
			jc2.setType("Negative");
			jcdlist.add(jc2);

			JsonWordCloud jc3 = new JsonWordCloud();
			jc3.setCount(1);
			jc3.setWord("Hope");
			jc3.setType("Positive");
			jcdlist.add(jc3);

			JsonWordCloud jc4 = new JsonWordCloud();
			jc4.setCount(1);
			jc4.setWord("Killing");
			jc4.setType("Negative");
			jcdlist.add(jc4);

			JsonWordCloud jc5 = new JsonWordCloud();
			jc5.setCount(1);
			jc5.setWord("Crisis");
			jc5.setType("Negative");
			jcdlist.add(jc5);
			return jcdlist;
		} else if (cid == 7) {
			JsonWordCloud jc1 = new JsonWordCloud();
			jc1.setCount(1);
			jc1.setWord("Hope");
			jc1.setType("Positive");
			jcdlist.add(jc1);

			JsonWordCloud jc2 = new JsonWordCloud();
			jc2.setCount(2);
			jc2.setWord("risk");
			jc2.setType("Negative");
			jcdlist.add(jc2);

			JsonWordCloud jc3 = new JsonWordCloud();
			jc3.setCount(1);
			jc3.setWord("Deplorable");
			jc3.setType("Negative");
			jcdlist.add(jc3);

			return jcdlist;
		} else if (cid == 8) {
			JsonWordCloud jc1 = new JsonWordCloud();
			jc1.setCount(2);
			jc1.setWord("Hope");
			jc1.setType("Positive");
			jcdlist.add(jc1);
			return jcdlist;
		} else if (cid == 9) {
			JsonWordCloud jc1 = new JsonWordCloud();
			jc1.setCount(2);
			jc1.setWord("Powerful");
			jc1.setType("Positive");
			jcdlist.add(jc1);

			JsonWordCloud jc2 = new JsonWordCloud();
			jc2.setCount(1);
			jc2.setWord("Hackers");
			jc2.setType("Negative");
			jcdlist.add(jc2);
			return jcdlist;
		}
		return new ArrayList();
	}

	private static List getWordCloudList(List voicesList, ProtoSearchCard psc) {
		Long cid = psc.getId();
		int cidInt = cid.intValue();
		List jcdlist = new ArrayList();
		if (cid == 2) {
			JsonWordCloud jc1 = new JsonWordCloud();
			jc1.setCount(2);
			jc1.setWord("Top");
			jc1.setType("Positive");
			jcdlist.add(jc1);

			JsonWordCloud jc2 = new JsonWordCloud();
			jc2.setCount(1);
			jc2.setWord("Homage");
			jc2.setType("Positive");
			jcdlist.add(jc2);

			JsonWordCloud jc3 = new JsonWordCloud();
			jc3.setCount(1);
			jc3.setType("Negative");
			jc3.setWord("Buyback");
			jcdlist.add(jc3);

			JsonWordCloud jc4 = new JsonWordCloud();
			jc4.setCount(1);
			jc3.setType("Negative");
			jc3.setWord("Hits");
			jcdlist.add(jc4);
			return jcdlist;
		} else if (cid == 3) {
			return jcdlist;
		} else if (cid == 4) {
			JsonWordCloud jc1 = new JsonWordCloud();
			jc1.setCount(1);
			jc1.setWord("Interesting");
			jc1.setType("Positive");
			jcdlist.add(jc1);

			JsonWordCloud jc2 = new JsonWordCloud();
			jc2.setCount(1);
			jc2.setWord("Future");
			jc2.setType("Positive");
			jcdlist.add(jc2);
			return jcdlist;
		} else if (cid == 5) {
			JsonWordCloud jc1 = new JsonWordCloud();
			jc1.setCount(2);
			jc1.setWord("Love");
			jc1.setType("Positive");
			jcdlist.add(jc1);

			JsonWordCloud jc2 = new JsonWordCloud();
			jc2.setCount(1);
			jc2.setWord("Stopped");
			jc2.setType("Negative");
			jcdlist.add(jc2);
			return jcdlist;
		} else if (cid == 6) {
			JsonWordCloud jc1 = new JsonWordCloud();
			jc1.setCount(1);
			jc1.setWord("Patient");
			jc1.setType("Positive");
			jcdlist.add(jc1);

			JsonWordCloud jc2 = new JsonWordCloud();
			jc2.setCount(1);
			jc2.setWord("Deadly");
			jc2.setType("Negative");
			jcdlist.add(jc2);

			JsonWordCloud jc3 = new JsonWordCloud();
			jc3.setCount(1);
			jc3.setWord("Hope");
			jc3.setType("Positive");
			jcdlist.add(jc3);

			JsonWordCloud jc4 = new JsonWordCloud();
			jc4.setCount(1);
			jc4.setWord("Killing");
			jc4.setType("Negative");
			jcdlist.add(jc4);

			JsonWordCloud jc5 = new JsonWordCloud();
			jc5.setCount(1);
			jc5.setWord("Crisis");
			jc5.setType("Negative");
			jcdlist.add(jc5);
			return jcdlist;
		} else if (cid == 7) {
			JsonWordCloud jc1 = new JsonWordCloud();
			jc1.setCount(1);
			jc1.setWord("Hope");
			jc1.setType("Positive");
			jcdlist.add(jc1);

			JsonWordCloud jc2 = new JsonWordCloud();
			jc2.setCount(2);
			jc2.setWord("risk");
			jc2.setType("Negative");
			jcdlist.add(jc2);

			JsonWordCloud jc3 = new JsonWordCloud();
			jc3.setCount(1);
			jc3.setWord("Deplorable");
			jc3.setType("Negative");
			jcdlist.add(jc3);

			return jcdlist;
		} else if (cid == 8) {
			JsonWordCloud jc1 = new JsonWordCloud();
			jc1.setCount(2);
			jc1.setWord("Hope");
			jc1.setType("Positive");
			jcdlist.add(jc1);
			return jcdlist;
		} else if (cid == 9) {
			JsonWordCloud jc1 = new JsonWordCloud();
			jc1.setCount(2);
			jc1.setWord("Powerful");
			jc1.setType("Positive");
			jcdlist.add(jc1);

			JsonWordCloud jc2 = new JsonWordCloud();
			jc2.setCount(1);
			jc2.setWord("Hackers");
			jc2.setType("Negative");
			jcdlist.add(jc2);
			return jcdlist;
		}
		return null;
	}

	public static String getCardDataAlt(ProtoVoicesDAO protoVoicesDAO,
			ProtoVoicesDetailsDAO protoVoicesDetailsDAO, String carduniqueid,
			String channel, ProtoSearchCardDAO protoSearchCardDAO) {
		ProtoSearchCard psc = (ProtoSearchCard) protoSearchCardDAO
				.getObjectByUniqueId(carduniqueid);
		Long positivecount = psc.getPositivevoices();
		Long negativecount = psc.getNegativevoices();
		Long neutralcount = psc.getNeutralvoices();
		List linechartList = new ArrayList();

		List voicesList = protoVoicesDAO
				.getAllRecordsbyCardUniqueId(carduniqueid);
		for (int i = 0; i < voicesList.size(); i++) {
			ProtoVoices voices = (ProtoVoices) voicesList.get(i);
			JsonLinechart jlc = new JsonLinechart();

			jlc.setCount(new Long(i + 1));
			String sentimentStr = StrUtil.nonNull(voices.getSentimentrating());
			Long sentimentLong = voices.getSentimentscore();
			if (sentimentLong == null) {
				sentimentLong = new Long(0);
			}
			jlc.setSentiment(sentimentStr);
			jlc.setSentimentrating(sentimentLong);
			Date vdt = voices.getVoicesdate();
			if (vdt != null) {
				jlc.setDate(StrUtil.getDateString(vdt, dateformat2));
			} else {
				jlc.setDate(StrUtil.getDateString(new Date(), dateformat2));
			}
			if (!"".equalsIgnoreCase(sentimentStr)) {
				linechartList.add(jlc);
			}

			Long voicesid = voices.getId();
			List vdlist = protoVoicesDetailsDAO.getAllRecordsbyVoices(voicesid);
			for (int j = 0; j < vdlist.size(); j++) {
				JsonLinechart jlc1 = new JsonLinechart();
				ProtoVoicesDetails vd = (ProtoVoicesDetails) vdlist.get(j);

				jlc1.setCount(new Long(j + 1));
				Date dt = vd.getVoicedate();

				if (dt != null) {
					jlc1.setDate(StrUtil.getDateString(dt, dateformat2));
				} else {
					jlc1
							.setDate(StrUtil.getDateString(new Date(),
									dateformat2));
				}
				String sentiment = StrUtil.nonNull(vd.getSentimentrating());

				jlc1.setSentiment(sentiment);

				Long sentimentscore = vd.getSentimentscore();
				if (sentimentscore == null) {
					sentimentscore = new Long(0);
				}
				jlc1.setSentimentrating(sentimentscore);
				if (!"".equalsIgnoreCase(sentiment)) {
					linechartList.add(jlc1);
				}

			}
		}
		Map map = new HashMap();
		map.put("linechart", linechartList);
		List wordcloudlist = getWordCloudList(voicesList, psc);
		map.put("wordcloud", wordcloudlist);

		JsonSentiment js = new JsonSentiment();
		js.setNegativevoices(negativecount);
		js.setNeutralvoices(neutralcount);
		js.setPositivevoices(positivecount);

		Gson gson = new Gson();
		StringBuffer sbfr = new StringBuffer();

		Set set = map.keySet();
		Iterator it = set.iterator();
		List tempList = new ArrayList();

		String temp1 = "";
		String temp2 = "";
		while (it.hasNext()) {
			String key = (String) it.next();
			temp1 = gson.toJson(key);

			tempList = (List) map.get(key);
			temp2 = gson.toJson(tempList);

			sbfr.append(temp1 + ":");
			sbfr.append(temp2 + ",");

		}
		String k = gson.toJson("sentiment");
		String m = gson.toJson(js);

		sbfr.append(k).append(":" + m);

		String kk = sbfr.toString();
		if (!kk.startsWith("{")) {
			kk = "{" + kk;
		}
		if (kk.endsWith("}")) {
			kk = kk + "}";
		}
		return kk;

	}

	public static String getCardData(ProtoVoicesDAO protoVoicesDAO,
			ProtoVoicesDetailsDAO protoVoicesDetailsDAO, String carduniqueid,
			String source) {
		List linechartList = new ArrayList();

		List positiveList = new ArrayList();
		List negativeList = new ArrayList();
		List neutralList = new ArrayList();

		JsonSentiment js = new JsonSentiment();

		int poscount = 0;
		int negcount = 0;
		int neucount = 0;

		List voicesList = protoVoicesDAO
				.getAllRecordsbyCardUniqueId(carduniqueid);
		for (int i = 0; i < voicesList.size(); i++) {
			ProtoVoices voices = (ProtoVoices) voicesList.get(i);

			JsonLinechart jlc = new JsonLinechart();
			JsonBarChartPositive jbcp = new JsonBarChartPositive();
			JsonBarChartNegative jbcn = new JsonBarChartNegative();
			JsonBarChartNeutral jbcneu = new JsonBarChartNeutral();

			List posList = new ArrayList();
			List negList = new ArrayList();

			jlc.setCount(new Long(1));
			String sentimentStr = StrUtil.nonNull(voices.getSentimentrating());

			if ("positive".equalsIgnoreCase(sentimentStr)) {
				poscount = poscount + 1;
			}
			if ("negative".equalsIgnoreCase(sentimentStr)) {
				negcount = negcount + 1;
			}
			if ("neutral".equalsIgnoreCase(sentimentStr)) {
				neucount = neucount + 1;
			}

			Long sentimentLong = voices.getSentimentscore();
			if (sentimentLong == null) {
				sentimentLong = new Long(0);
			}
			jlc.setSentiment(sentimentStr);
			jlc.setSentimentrating(sentimentLong);
			Date vdt = voices.getVoicesdate();
			if (vdt != null) {
				jlc.setDate(StrUtil.getDateString(vdt, dateformat2));
			} else {
				jlc.setDate(StrUtil.getDateString(new Date(), dateformat2));
			}
			linechartList.add(jlc);

			String positiveterm = StrUtil.nonNull(voices.getPositivephrase());
			String negativeterm = StrUtil.nonNull(voices.getNegativephrase());
			String neutralterm = StrUtil.nonNull(voices.getNeutralphrase());

			if (!"".equalsIgnoreCase(positiveterm)) {
				if (posList.contains(positiveterm)) {
					jbcp.setCount(new Long(3));
					jbcp.setType("positive");
					jbcp.setWord(positiveterm);
					positiveList.add(jbcp);
					posList.add(positiveterm);
				} else {
					jbcp.setCount(new Long(1));
					jbcp.setType("positive");
					jbcp.setWord(positiveterm);
					positiveList.add(jbcp);
					posList.add(positiveterm);
				}

				// poscount = poscount + 1;
			}
			if (!"".equalsIgnoreCase(negativeterm)) {
				if (negList.contains(negativeterm)) {
					jbcn.setCount(new Long(3));
					jbcn.setType("negative");
					jbcn.setWord(negativeterm);
					negativeList.add(jbcn);
					negList.add(negativeterm);
				} else {
					jbcn.setCount(new Long(1));
					jbcn.setType("negative");
					jbcn.setWord(negativeterm);
					negativeList.add(jbcn);
					negList.add(negativeterm);
				}

				// negcount = negcount + 1;
			}
			if (!"".equalsIgnoreCase(neutralterm)) {
				jbcneu.setCount(new Long(1));
				jbcneu.setType("neutral");
				jbcneu.setWord(neutralterm);
				neutralList.add(jbcneu);
				// neucount = neucount + 1;
			}
			Long voicesid = voices.getId();
			List vdlist = protoVoicesDetailsDAO.getAllRecordsbyVoices(voicesid);

			for (int j = 0; j < vdlist.size(); j++) {
				JsonSentiment js1 = new JsonSentiment();
				JsonLinechart jlc1 = new JsonLinechart();
				JsonBarChartPositive jbcp1 = new JsonBarChartPositive();
				JsonBarChartNegative jbcn1 = new JsonBarChartNegative();
				JsonBarChartNeutral jbcneu1 = new JsonBarChartNeutral();

				ProtoVoicesDetails vd = (ProtoVoicesDetails) vdlist.get(j);

				jlc1.setCount(new Long(1));
				Date dt = vd.getVoicedate();

				if (dt != null) {
					jlc1.setDate(StrUtil.getDateString(dt, dateformat2));
				} else {
					jlc1
							.setDate(StrUtil.getDateString(new Date(),
									dateformat2));
				}
				String sentiment = StrUtil.nonNull(vd.getSentimentrating());

				if ("positive".equalsIgnoreCase(sentiment)) {
					poscount = poscount + 1;
				}
				if ("negative".equalsIgnoreCase(sentiment)) {
					negcount = negcount + 1;
				}
				if ("neutral".equalsIgnoreCase(sentiment)) {
					neucount = neucount + 1;
				}

				jlc1.setSentiment(sentiment);

				Long sentimentscore = vd.getSentimentscore();
				if (sentimentscore == null) {
					sentimentscore = new Long(0);
				}
				jlc1.setSentimentrating(sentimentscore);
				linechartList.add(jlc1);

				String positive = StrUtil.nonNull(vd.getPositivephrase());
				String negative = StrUtil.nonNull(vd.getNegativephrase());
				String neutral = StrUtil.nonNull(vd.getNeutralphrase());

				if (!"".equalsIgnoreCase(positive)) {
					jbcp1.setCount(new Long(1));
					jbcp1.setType("positive");
					jbcp1.setWord(positive);
					positiveList.add(jbcp1);
					// poscount = poscount + 1;
				}
				if (!"".equalsIgnoreCase(negative)) {
					jbcn1.setCount(new Long(1));
					jbcn1.setType("negative");
					jbcn1.setWord(negative);
					negativeList.add(jbcn1);
					// negcount = negcount + 1;
				}
				if (!"".equalsIgnoreCase(neutral)) {
					jbcneu1.setCount(new Long(1));
					jbcneu1.setType("neutral");
					jbcneu1.setWord(neutral);
					neutralList.add(jbcneu1);
					// neucount = neucount + 1;
				}

			}

		}

		js.setNegativevoices(new Long(negcount));
		js.setNeutralvoices(new Long(neucount));
		js.setPositivevoices(new Long(poscount));

		Gson gson = new Gson();
		StringBuffer sbfr = new StringBuffer();

		Map map = new HashMap();
		map.put("linechart", linechartList);
		map.put("positive", positiveList);
		map.put("negative", negativeList);
		map.put("neutral", neutralList);

		Set set = map.keySet();
		Iterator it = set.iterator();
		List tempList = new ArrayList();

		String temp1 = "";
		String temp2 = "";
		while (it.hasNext()) {
			String key = (String) it.next();
			temp1 = gson.toJson(key);

			tempList = (List) map.get(key);
			temp2 = gson.toJson(tempList);

			sbfr.append(temp1 + ":");
			sbfr.append(temp2 + ",");

		}
		String k = gson.toJson("sentiment");
		String m = gson.toJson(js);

		sbfr.append(k).append(":" + m);

		String kk = sbfr.toString();
		if (!kk.startsWith("{")) {
			kk = "{" + kk;
		}
		if (kk.endsWith("}")) {
			kk = kk + "}";
		}
		return kk;
	}

	public static String getVoicesData(VoicesDAO voicesDAO,
			VoicesDetailsDAO voicesDetailsDAO, Long voicesid) {
		Voices voices = (Voices) voicesDAO.getObjectById(voicesid);

		List linechartList = new ArrayList();
		List positiveList = new ArrayList();
		List negativeList = new ArrayList();
		List neutralList = new ArrayList();

		JsonSentiment js = new JsonSentiment();
		JsonLinechart jlc = new JsonLinechart();
		JsonBarChartPositive jbcp = new JsonBarChartPositive();
		JsonBarChartNegative jbcn = new JsonBarChartNegative();
		JsonBarChartNeutral jbcneu = new JsonBarChartNeutral();

		int poscount = 0;
		int negcount = 0;
		int neucount = 0;

		jlc.setCount(new Long(1));
		String sentimentStr = StrUtil.nonNull(voices.getSentimentrating());

		if ("positive".equalsIgnoreCase(sentimentStr)) {
			poscount = poscount + 1;
		}
		if ("negative".equalsIgnoreCase(sentimentStr)) {
			negcount = negcount + 1;
		}
		if ("neutral".equalsIgnoreCase(sentimentStr)) {
			neucount = neucount + 1;
		}
		Long sentimentLong = voices.getSentimentscore();
		if (sentimentLong == null) {
			sentimentLong = new Long(0);
		}
		jlc.setSentiment(sentimentStr);
		jlc.setSentimentrating(sentimentLong);
		Date vdt = voices.getVoicesdate();
		if (vdt != null) {
			jlc.setDate(StrUtil.getDateString(vdt, dateformat2));
		} else {
			jlc.setDate(StrUtil.getDateString(new Date(), dateformat2));
		}
		linechartList.add(jlc);

		String positiveterm = StrUtil.nonNull(voices.getPositivephrase());
		String negativeterm = StrUtil.nonNull(voices.getNegativephrase());
		String neutralterm = StrUtil.nonNull(voices.getNeutralphrase());

		if (!"".equalsIgnoreCase(positiveterm)) {
			jbcp.setCount(new Long(1));
			jbcp.setType("positive");
			jbcp.setWord(positiveterm);
			positiveList.add(jbcp);
			// poscount = poscount + 1;
		}
		if (!"".equalsIgnoreCase(negativeterm)) {
			jbcn.setCount(new Long(1));
			jbcn.setType("negative");
			jbcn.setWord(negativeterm);
			negativeList.add(jbcn);
			// negcount = negcount + 1;
		}
		if (!"".equalsIgnoreCase(neutralterm)) {
			jbcneu.setCount(new Long(1));
			jbcneu.setType("neutral");
			jbcneu.setWord(neutralterm);
			neutralList.add(jbcneu);
			// neucount = neucount + 1;
		}

		List vdlist = voicesDetailsDAO.getAllRecordsbyVoices(voicesid);

		for (int i = 0; i < vdlist.size(); i++) {
			JsonSentiment js1 = new JsonSentiment();
			JsonLinechart jlc1 = new JsonLinechart();
			JsonBarChartPositive jbcp1 = new JsonBarChartPositive();
			JsonBarChartNegative jbcn1 = new JsonBarChartNegative();
			JsonBarChartNeutral jbcneu1 = new JsonBarChartNeutral();

			VoicesDetails vd = (VoicesDetails) vdlist.get(i);

			jlc1.setCount(new Long(1));
			Date dt = vd.getVoicedate();

			if (dt != null) {
				jlc1.setDate(StrUtil.getDateString(dt, dateformat2));
			} else {
				jlc1.setDate(StrUtil.getDateString(new Date(), dateformat2));
			}
			String sentiment = StrUtil.nonNull(vd.getSentimentrating());

			if ("positive".equalsIgnoreCase(sentiment)) {
				poscount = poscount + 1;
			}
			if ("negative".equalsIgnoreCase(sentiment)) {
				negcount = negcount + 1;
			}
			if ("neutral".equalsIgnoreCase(sentiment)) {
				neucount = neucount + 1;
			}

			jlc1.setSentiment(sentiment);

			Long sentimentscore = vd.getSentimentscore();
			if (sentimentscore == null) {
				sentimentscore = new Long(0);
			}
			jlc1.setSentimentrating(sentimentscore);
			linechartList.add(jlc1);

			String positive = StrUtil.nonNull(vd.getPositivephrase());
			String negative = StrUtil.nonNull(vd.getNegativephrase());
			String neutral = StrUtil.nonNull(vd.getNeutralphrase());

			if (!"".equalsIgnoreCase(positive)) {
				jbcp1.setCount(new Long(1));
				jbcp1.setType("positive");
				jbcp1.setWord(positive);
				positiveList.add(jbcp1);
				// poscount = poscount + 1;
			}
			if (!"".equalsIgnoreCase(negative)) {
				jbcn1.setCount(new Long(1));
				jbcn1.setType("negative");
				jbcn1.setWord(negative);
				negativeList.add(jbcn1);
				// negcount = negcount + 1;
			}
			if (!"".equalsIgnoreCase(neutral)) {
				jbcneu1.setCount(new Long(1));
				jbcneu1.setType("neutral");
				jbcneu1.setWord(neutral);
				neutralList.add(jbcneu1);
				// neucount = neucount + 1;
			}

		}
		int vdcount = vdlist.size();
		int[] rand = StrUtil.divide(vdcount, 3);
		int a = rand[0];
		int b = rand[1];
		int c = rand[2];
		Long pos = new Long(a);
		Long neg = new Long(b);
		Long neu = new Long(c);
		js.setNegativevoices(neg);
		js.setNeutralvoices(neu);
		js.setPositivevoices(pos);

		Gson gson = new Gson();
		StringBuffer sbfr = new StringBuffer();

		Map map = new HashMap();
		map.put("linechart", linechartList);
		map.put("positive", positiveList);
		map.put("negative", negativeList);
		map.put("neutral", neutralList);

		Set set = map.keySet();
		Iterator it = set.iterator();
		List tempList = new ArrayList();

		String temp1 = "";
		String temp2 = "";
		while (it.hasNext()) {
			String key = (String) it.next();
			temp1 = gson.toJson(key);

			tempList = (List) map.get(key);
			temp2 = gson.toJson(tempList);

			sbfr.append(temp1 + ":");
			sbfr.append(temp2 + ",");

		}
		String k = gson.toJson("sentiment");
		String m = gson.toJson(js);

		sbfr.append(k).append(":" + m);

		String kk = sbfr.toString();
		if (!kk.startsWith("{")) {
			kk = "{" + kk;
		}
		if (kk.endsWith("}")) {
			kk = kk + "}";
		}
		return kk;
	}

	public static String getVoicesData(ProtoVoicesDAO protoVoicesDAO,
			ProtoVoicesDetailsDAO protoVoicesDetailsDAO, Long voicesid) {
		ProtoVoices voices = (ProtoVoices) protoVoicesDAO
				.getObjectById(voicesid);

		List linechartList = new ArrayList();
		List positiveList = new ArrayList();
		List negativeList = new ArrayList();
		List neutralList = new ArrayList();

		JsonSentiment js = new JsonSentiment();
		JsonLinechart jlc = new JsonLinechart();
		JsonBarChartPositive jbcp = new JsonBarChartPositive();
		JsonBarChartNegative jbcn = new JsonBarChartNegative();
		JsonBarChartNeutral jbcneu = new JsonBarChartNeutral();

		int poscount = 0;
		int negcount = 0;
		int neucount = 0;

		jlc.setCount(new Long(1));
		String sentimentStr = StrUtil.nonNull(voices.getSentimentrating());

		if ("positive".equalsIgnoreCase(sentimentStr)) {
			poscount = poscount + 1;
		}
		if ("negative".equalsIgnoreCase(sentimentStr)) {
			negcount = negcount + 1;
		}
		if ("neutral".equalsIgnoreCase(sentimentStr)) {
			neucount = neucount + 1;
		}
		Long sentimentLong = voices.getSentimentscore();
		if (sentimentLong == null) {
			sentimentLong = new Long(0);
		}
		jlc.setSentiment(sentimentStr);
		jlc.setSentimentrating(sentimentLong);
		Date vdt = voices.getVoicesdate();
		if (vdt != null) {
			jlc.setDate(StrUtil.getDateString(vdt, dateformat2));
		} else {
			jlc.setDate(StrUtil.getDateString(new Date(), dateformat2));
		}
		linechartList.add(jlc);

		String positiveterm = StrUtil.nonNull(voices.getPositivephrase());
		String negativeterm = StrUtil.nonNull(voices.getNegativephrase());
		String neutralterm = StrUtil.nonNull(voices.getNeutralphrase());

		if (!"".equalsIgnoreCase(positiveterm)) {
			jbcp.setCount(new Long(1));
			jbcp.setType("positive");
			jbcp.setWord(positiveterm);
			positiveList.add(jbcp);
			// poscount = poscount + 1;
		}
		if (!"".equalsIgnoreCase(negativeterm)) {
			jbcn.setCount(new Long(1));
			jbcn.setType("negative");
			jbcn.setWord(negativeterm);
			negativeList.add(jbcn);
			// negcount = negcount + 1;
		}
		if (!"".equalsIgnoreCase(neutralterm)) {
			jbcneu.setCount(new Long(1));
			jbcneu.setType("neutral");
			jbcneu.setWord(neutralterm);
			neutralList.add(jbcneu);
			// neucount = neucount + 1;
		}

		List vdlist = protoVoicesDetailsDAO.getAllRecordsbyVoices(voicesid);

		for (int i = 0; i < vdlist.size(); i++) {
			JsonSentiment js1 = new JsonSentiment();
			JsonLinechart jlc1 = new JsonLinechart();
			JsonBarChartPositive jbcp1 = new JsonBarChartPositive();
			JsonBarChartNegative jbcn1 = new JsonBarChartNegative();
			JsonBarChartNeutral jbcneu1 = new JsonBarChartNeutral();

			ProtoVoicesDetails vd = (ProtoVoicesDetails) vdlist.get(i);

			jlc1.setCount(new Long(1));
			Date dt = vd.getVoicedate();

			if (dt != null) {
				jlc1.setDate(StrUtil.getDateString(dt, dateformat2));
			} else {
				jlc1.setDate(StrUtil.getDateString(new Date(), dateformat2));
			}
			String sentiment = StrUtil.nonNull(vd.getSentimentrating());

			if ("positive".equalsIgnoreCase(sentiment)) {
				poscount = poscount + 1;
			}
			if ("negative".equalsIgnoreCase(sentiment)) {
				negcount = negcount + 1;
			}
			if ("neutral".equalsIgnoreCase(sentiment)) {
				neucount = neucount + 1;
			}

			jlc1.setSentiment(sentiment);

			Long sentimentscore = vd.getSentimentscore();
			if (sentimentscore == null) {
				sentimentscore = new Long(0);
			}
			jlc1.setSentimentrating(sentimentscore);
			linechartList.add(jlc1);

			String positive = StrUtil.nonNull(vd.getPositivephrase());
			String negative = StrUtil.nonNull(vd.getNegativephrase());
			String neutral = StrUtil.nonNull(vd.getNeutralphrase());

			if (!"".equalsIgnoreCase(positive)) {
				jbcp1.setCount(new Long(1));
				jbcp1.setType("positive");
				jbcp1.setWord(positive);
				positiveList.add(jbcp1);
				// poscount = poscount + 1;
			}
			if (!"".equalsIgnoreCase(negative)) {
				jbcn1.setCount(new Long(1));
				jbcn1.setType("negative");
				jbcn1.setWord(negative);
				negativeList.add(jbcn1);
				// negcount = negcount + 1;
			}
			if (!"".equalsIgnoreCase(neutral)) {
				jbcneu1.setCount(new Long(1));
				jbcneu1.setType("neutral");
				jbcneu1.setWord(neutral);
				neutralList.add(jbcneu1);
				// neucount = neucount + 1;
			}

		}
		int vdcount = vdlist.size();
		int[] rand = StrUtil.divide(vdcount, 3);
		int a = rand[0];
		int b = rand[1];
		int c = rand[2];
		Long pos = new Long(a);
		Long neg = new Long(b);
		Long neu = new Long(c);
		js.setNegativevoices(neg);
		js.setNeutralvoices(neu);
		js.setPositivevoices(pos);

		Gson gson = new Gson();
		StringBuffer sbfr = new StringBuffer();

		Map map = new HashMap();
		map.put("linechart", linechartList);
		map.put("positive", positiveList);
		map.put("negative", negativeList);
		map.put("neutral", neutralList);

		Set set = map.keySet();
		Iterator it = set.iterator();
		List tempList = new ArrayList();

		String temp1 = "";
		String temp2 = "";
		while (it.hasNext()) {
			String key = (String) it.next();
			temp1 = gson.toJson(key);

			tempList = (List) map.get(key);
			temp2 = gson.toJson(tempList);

			sbfr.append(temp1 + ":");
			sbfr.append(temp2 + ",");

		}
		String k = gson.toJson("sentiment");
		String m = gson.toJson(js);

		sbfr.append(k).append(":" + m);

		String kk = sbfr.toString();
		if (!kk.startsWith("{")) {
			kk = "{" + kk;
		}
		if (kk.endsWith("}")) {
			kk = kk + "}";
		}
		return kk;
	}

	public static String sendDummyData() {
		List linechartList = new ArrayList();

		List positiveList = new ArrayList();
		List negativeList = new ArrayList();
		List neutralList = new ArrayList();

		JsonSentiment js = new JsonSentiment();
		js.setNegativevoices(new Long(22));
		js.setNeutralvoices(new Long(33));
		js.setPositivevoices(new Long(56));

		for (int i = 0; i < 3; i++) {
			JsonLinechart jlc = new JsonLinechart();
			jlc.setCount(new Long(44));
			String dateStr = StrUtil.getDateString(new Date(), dateformat2);
			jlc.setDate((i + 4) + "-09-2014");
			jlc.setSentiment("positive");
			jlc.setSentimentrating(new Long(8));
			linechartList.add(jlc);

			JsonBarChartPositive jbcp = new JsonBarChartPositive();
			jbcp.setCount(new Long(1));
			jbcp.setType("positive");
			jbcp.setWord("Brilliant");
			positiveList.add(jbcp);

			JsonBarChartNegative jbcn = new JsonBarChartNegative();
			JsonBarChartNeutral jbcneu = new JsonBarChartNeutral();

			for (int j = 0; j < 10; j++) {
				JsonLinechart jlci = new JsonLinechart();
				jlci.setCount(new Long(44));
				dateStr = StrUtil.getDateString(new Date(), dateformat2);
				jlci.setDate((j + 5) + "-10-2014");
				jlci.setSentiment("positive");
				jlci.setSentimentrating(new Long(8));
				linechartList.add(jlci);

				JsonBarChartPositive jbcpi = new JsonBarChartPositive();
				jbcpi.setCount(new Long(1));
				jbcpi.setType("positive");
				jbcpi.setWord("Brilliant");
				positiveList.add(jbcpi);

				JsonBarChartNegative jbcni = new JsonBarChartNegative();
				jbcni.setCount(new Long(1));
				jbcni.setType("negative");
				jbcni.setWord("Bad");
				negativeList.add(jbcni);

				JsonBarChartNeutral jbcneui = new JsonBarChartNeutral();
				jbcneui.setCount(new Long(1));
				jbcneui.setType("neutral");
				jbcneui.setWord("So so");
				neutralList.add(jbcneui);

			}
		}

		Gson gson = new Gson();
		StringBuffer sbfr = new StringBuffer();

		Map map = new HashMap();
		map.put("linechart", linechartList);
		map.put("positive", positiveList);
		map.put("negative", negativeList);
		map.put("neutral", neutralList);

		Set set = map.keySet();
		Iterator it = set.iterator();
		List tempList = new ArrayList();

		String temp1 = "";
		String temp2 = "";
		while (it.hasNext()) {
			String key = (String) it.next();
			temp1 = gson.toJson(key);

			tempList = (List) map.get(key);
			temp2 = gson.toJson(tempList);

			sbfr.append(temp1 + ":");
			sbfr.append(temp2 + ",");

		}
		String k = gson.toJson("sentiment");
		String m = gson.toJson(js);

		sbfr.append(k).append(":" + m);

		String kk = sbfr.toString();
		if (!kk.startsWith("{")) {
			kk = "{" + kk;
		}
		if (kk.endsWith("}")) {
			kk = kk + "}";
		}
		return kk;
	}

	public static String sendVoiceDummyData() {
		List linechartList = new ArrayList();

		JsonSentiment js = new JsonSentiment();
		js.setNegativevoices(new Long(22));
		js.setNeutralvoices(new Long(33));
		js.setPositivevoices(new Long(56));

		List positiveList = new ArrayList();
		List negativeList = new ArrayList();
		List neutralList = new ArrayList();

		JsonLinechart jlc = new JsonLinechart();
		jlc.setCount(new Long(44));
		String dateStr = StrUtil.getDateString(new Date(), dateformat2);
		jlc.setDate("22-11-2014");
		jlc.setSentiment("positive");
		jlc.setSentimentrating(new Long(8));
		linechartList.add(jlc);

		JsonBarChartPositive jbcp = new JsonBarChartPositive();
		jbcp.setCount(new Long(1));
		jbcp.setType("positive");
		jbcp.setWord("Brilliant");
		positiveList.add(jbcp);

		JsonBarChartNegative jbcn = new JsonBarChartNegative();
		JsonBarChartNeutral jbcneu = new JsonBarChartNeutral();

		for (int i = 0; i < 10; i++) {
			JsonLinechart jlci = new JsonLinechart();
			jlci.setCount(new Long(44));
			dateStr = StrUtil.getDateString(new Date(), dateformat2);
			jlci.setDate((i + 2) + "-12-2014");
			jlci.setSentiment("positive");
			jlci.setSentimentrating(new Long(8));
			linechartList.add(jlci);

			JsonBarChartPositive jbcpi = new JsonBarChartPositive();
			jbcpi.setCount(new Long(1));
			jbcpi.setType("positive");
			jbcpi.setWord("Brilliant");
			positiveList.add(jbcpi);

			JsonBarChartNegative jbcni = new JsonBarChartNegative();
			jbcni.setCount(new Long(1));
			jbcni.setType("negative");
			jbcni.setWord("Disgusting");
			negativeList.add(jbcni);

			JsonBarChartNeutral jbcneui = new JsonBarChartNeutral();
			jbcneui.setCount(new Long(1));
			jbcneui.setType("neutral");
			jbcneui.setWord("So so");
			neutralList.add(jbcneui);

		}

		Gson gson = new Gson();
		StringBuffer sbfr = new StringBuffer();

		Map map = new HashMap();
		map.put("linechart", linechartList);
		map.put("positive", positiveList);
		map.put("negative", negativeList);
		map.put("neutral", neutralList);

		Set set = map.keySet();
		Iterator it = set.iterator();
		List tempList = new ArrayList();

		String temp1 = "";
		String temp2 = "";
		while (it.hasNext()) {
			String key = (String) it.next();
			temp1 = gson.toJson(key);

			tempList = (List) map.get(key);
			temp2 = gson.toJson(tempList);

			sbfr.append(temp1 + ":");
			sbfr.append(temp2 + ",");

		}
		String k = gson.toJson("sentiment");
		String m = gson.toJson(js);

		sbfr.append(k).append(":" + m);

		String kk = sbfr.toString();
		if (!kk.startsWith("{")) {
			kk = "{" + kk;
		}
		if (kk.endsWith("}")) {
			kk = kk + "}";
		}
		return kk;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Date dt = new Date();
		String temp = StrUtil.getDateString(dt, dateformat2);
		System.out.println(temp);
	}

}
