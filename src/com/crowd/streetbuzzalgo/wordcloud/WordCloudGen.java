/**
 * 
 */
package com.crowd.streetbuzzalgo.wordcloud;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import wordcloud.CollisionMode;
import wordcloud.WordCloud;
import wordcloud.WordFrequency;
import wordcloud.bg.RectangleBackground;
import wordcloud.font.scale.LinearFontScalar;
import wordcloud.palette.ColorPalette;

import com.crowd.streetbuzz.common.Constants;
import com.crowd.streetbuzz.dao.implementation.MediaFilesDAO;
import com.crowd.streetbuzz.model.MediaFiles;
import com.crowd.streetbuzz.model.WordCloudModel;
import com.crowd.streetbuzz.model.WordCloudStore;
import com.crowd.streetbuzzalgo.utils.StrUtil;

/**
 * @author Atrijit
 * 
 */
public class WordCloudGen implements Constants {

	private static final Random RANDOM = new Random();

	/**
	 * 
	 */
	public WordCloudGen() {
		// TODO Auto-generated constructor stub
	}

	private static List<WordFrequency> buildWordFrequences(List list)
	throws IOException {
		final List wordFrequencies = new ArrayList();
		for (int i=0;i<list.size();i++){
			WordCloudStore wct = (WordCloudStore)list.get(i);
			String word = wct.getWord();
			Long freq = wct.getCount();
			wordFrequencies.add(new WordFrequency(word, freq.intValue()));
			
		}
		return wordFrequencies;
	}

	public static String createCloud(List  list, MediaFilesDAO mediaFilesDAO,String width, String height)
			throws Exception {
		int widthc=400;
		int heightc=400;
		/*if(!"".equalsIgnoreCase(width)){
			widthc = new Integer(width).intValue();
		}
		if(!"".equalsIgnoreCase(height)){
			heightc = new Integer(height).intValue();
		}*/
		long start = System.currentTimeMillis();
		final List<WordFrequency> wordFrequencies = buildWordFrequences(list);
		long one = System.currentTimeMillis();
		System.out.println("****************DATA PREPN WORD CLOUD*****************" +((one  - start)/1000)+"secs");

		final WordCloud wordCloud = new WordCloud(widthc, heightc,
				CollisionMode.RECTANGLE);
		wordCloud.setPadding(0);
		wordCloud.setBackground(new RectangleBackground(widthc, heightc));
		wordCloud.setColorPalette(buildRandomColorPallete(20));
		wordCloud.setFontScalar(new LinearFontScalar(10, 40));
		wordCloud.build(wordFrequencies);
		String filename = StrUtil.getUniqueId();
		String filepath = BASESBSTORAGEPATH + filename + ".gif";
		wordCloud.writeToFile(filepath);
		String mediaurl = "http://203.123.190.50/streetbuzz/getimage.htm?mediaid="
			+ filename;
		MediaFiles mf = new MediaFiles();
		mf.setFilepath(filepath);
		mf.setUniqueid(filename);
		mf.setType(IMAGE);
		
		mf.setMediaurl(mediaurl);
		mediaFilesDAO.addOrUpdateRecord(mf);
		long two = System.currentTimeMillis();
		System.out.println("****************GEN WORD CLOUD*****************" +((two  - one)/1000)+"secs");
		return mediaurl;

	}

	private static ColorPalette buildRandomColorPallete(int n) {
		final Color[] colors = new Color[n];
		for (int i = 0; i < colors.length; i++) {
			colors[i] = new Color(RANDOM.nextInt(230) + 25,
					RANDOM.nextInt(230) + 25, RANDOM.nextInt(230) + 25);
		}
		return new ColorPalette(colors);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		List list = new ArrayList();
		WordCloudModel w = new WordCloudModel();
		
		w.setFrequency(new Long(12));
		w.setWords("Modi");
		list.add(w);
		
		WordCloudModel w2 = new WordCloudModel();
		w2.setFrequency(new Long(5));
		w2.setWords("Seeman");
		list.add(w2);
		
		WordCloudModel w3 = new WordCloudModel();
		w3.setFrequency(new Long(3));
		w3.setWords("Sushma");
		list.add(w3);
		
		WordCloudModel w4 = new WordCloudModel();
		w4.setFrequency(new Long(44));
		w4.setWords("Obama");
		list.add(w4);
		
		WordCloudModel w5 = new WordCloudModel();
		w5.setFrequency(new Long(2));
		w5.setWords("Rahul");
		list.add(w5);
		try {
			System.out.println(createCloud(list,null,"",""));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
