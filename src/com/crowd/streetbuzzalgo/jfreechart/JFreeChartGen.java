/**
 * 
 */
package com.crowd.streetbuzzalgo.jfreechart;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import com.crowd.streetbuzz.common.Constants;
import com.crowd.streetbuzz.dao.implementation.MediaFilesDAO;
import com.crowd.streetbuzz.model.MediaFiles;
import com.crowd.streetbuzzalgo.utils.StrUtil;

/**
 * @author Atrijit
 * 
 */
public class JFreeChartGen implements Constants {

	/**
	 * 
	 */
	public JFreeChartGen() {
		// TODO Auto-generated constructor stub
	}

	public static String createBarChart(Map map, MediaFilesDAO mediaFilesDAO, String width, String height) {
		
		Set set = map.keySet();
		Iterator it = set.iterator();
		final DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		while (it.hasNext()) {
			String key = (String) it.next();
			Long val = (Long) map.get(key);
			dataset.addValue(val, key, "");
		}
		JFreeChart barChart = ChartFactory.createBarChart("Buzz Meter",
				"Sentiments", "Score", dataset, PlotOrientation.VERTICAL,
				false, true, false);
		
		/*final CategoryPlot plot = barChart.getCategoryPlot();
		CategoryItemRenderer renderer = new CustomRenderer(); 
		plot.setRenderer(renderer);*/
		
		int widthc = 533; /* Width of the image */
		int heightc = 400; /* Height of the image */
		String filename = StrUtil.getUniqueId();
		String filepath = BASESBSTORAGEPATH + filename + ".jpg";
		File BarChart = new File(filepath);
		try {
			ChartUtilities.saveChartAsJPEG(BarChart, barChart, widthc, heightc);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		MediaFiles mf = new MediaFiles();
		mf.setFilepath(filepath);
		mf.setUniqueid(filename);
		mf.setType(IMAGE);
		String mediaurl = "http://203.123.190.50/streetbuzz/getimage.htm?mediaid="
				+ filename;
		mf.setMediaurl(mediaurl);
		mediaFilesDAO.addOrUpdateRecord(mf);
		return mediaurl;
	}

	public static String createXYChart(TreeMap posmap, TreeMap negmap,
			MediaFilesDAO mediaFilesDAO,String width, String height) {

		final XYSeries positive = new XYSeries("Positive");
		Set posset = posmap.keySet();
		Iterator itpos = posset.iterator();
		while (itpos.hasNext()) {
			Date dt = (Date) itpos.next();
			String dtstr = StrUtil.getDateString(dt, "yyyy");
			System.out.println("pos dtstr: "+dtstr);
			Double dtdbl = new Double(dtstr);
			Long val = (Long) posmap.get(dt);
			System.out.println("pos val: "+val.toString());
			positive.add(dtdbl, val);
		}

		final XYSeries negative = new XYSeries("Negative");

		Set negset = negmap.keySet();
		Iterator itneg = negset.iterator();
		while (itneg.hasNext()) {
			Date dt = (Date) itneg.next();
			String dtstr = StrUtil.getDateString(dt, "yyyy");
			System.out.println("ned dtstr: "+dtstr);
			Double dtdbl = new Double(dtstr);
			Long val = (Long) negmap.get(dt);
			System.out.println("neg val: "+val.toString());
			negative.add(dtdbl, val);
		}
		final XYSeriesCollection dataset = new XYSeriesCollection();
		dataset.addSeries(positive);

		dataset.addSeries(negative);

		JFreeChart xylineChart = ChartFactory.createXYLineChart(
				"Year vs Sentiment", "Year", "Sentiment", dataset,
				PlotOrientation.VERTICAL, true, true, false);

		int widthc = 533; /* Width of the image */
		int heightc = 400; /* Height of the image */
		
		String filename = StrUtil.getUniqueId();
		String filepath = "/home/streetbuzz/" + filename + ".jpg";
		File XYChart = new File(filepath);
		try {
			ChartUtilities.saveChartAsJPEG(XYChart, xylineChart, widthc, heightc);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		MediaFiles mf = new MediaFiles();
		mf.setFilepath(filepath);
		mf.setUniqueid(filename);
		mf.setType(IMAGE);
		String mediaurl = "http://203.123.190.50/streetbuzz/getimage.htm?mediaid="
				+ filename;
		mf.setMediaurl(mediaurl);
		mediaFilesDAO.addOrUpdateRecord(mf);
		return mediaurl;
	}
	
	public static String createLineChart(TreeMap posmap, TreeMap negmap,
			MediaFilesDAO mediaFilesDAO) {
		DefaultCategoryDataset line_chart_dataset = new DefaultCategoryDataset();
		Set posset = posmap.keySet();
		Iterator itpos = posset.iterator();
		while (itpos.hasNext()) {
			Date dt = (Date) itpos.next();
			String dtstr = StrUtil.getDateString(dt, dateformat2);
			Long val = (Long) posmap.get(dt);
			double d = val.doubleValue();
			String year = StrUtil.getDateString(dt, "yyyy");
			line_chart_dataset.addValue(d, "Positive", year);
		}
		Set negset = negmap.keySet();
		Iterator itneg = negset.iterator();
		while (itneg.hasNext()) {
			Date dt = (Date) itneg.next();
			String dtstr = StrUtil.getDateString(dt, dateformat2);
			Long val = (Long) negmap.get(dt);
			double d = val.doubleValue();
			String year = StrUtil.getDateString(dt, "yyyy");
			line_chart_dataset.addValue(d, "Negative", year);
		}
		JFreeChart lineChartObject = ChartFactory
		.createLineChart("", "Time", "Sentiment",
				line_chart_dataset, PlotOrientation.VERTICAL, true,
				true, false);
		int widthc = 533; /* Width of the image */
		int heightc = 400; /* Height of the image */
		
		String filename = StrUtil.getUniqueId();
		String filepath = "/home/streetbuzz/" + filename + ".jpg";
		File lineChart = new File(filepath);
		try {
			ChartUtilities.saveChartAsJPEG(lineChart, lineChartObject, widthc,
					heightc);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		MediaFiles mf = new MediaFiles();
		mf.setFilepath(filepath);
		mf.setUniqueid(filename);
		mf.setType(IMAGE);
		String mediaurl = "http://203.123.190.50/streetbuzz/getimage.htm?mediaid="
				+ filename;
		mf.setMediaurl(mediaurl);
		mediaFilesDAO.addOrUpdateRecord(mf);
		return mediaurl;
	}
	
	

	private static void createChart() {
		long start = System.currentTimeMillis();
		DefaultCategoryDataset line_chart_dataset = new DefaultCategoryDataset();
		
		line_chart_dataset.addValue(25, "negative", "1");
		line_chart_dataset.addValue(25, "negative", "2");
		line_chart_dataset.addValue(34, "negative", "3");
		line_chart_dataset.addValue(144, "negative", "4");
		line_chart_dataset.addValue(287, "negative", "5");
		line_chart_dataset.addValue(345, "negative", "6");
		line_chart_dataset.addValue(125, "negative", "7");
		line_chart_dataset.addValue(215, "negative", "8");
		line_chart_dataset.addValue(314, "negative", "9");
		line_chart_dataset.addValue(1414, "negative", "10");
		line_chart_dataset.addValue(2187, "negative", "11");
		line_chart_dataset.addValue(3145, "negative", "12");
		line_chart_dataset.addValue(251, "negative", "13");
		line_chart_dataset.addValue(2115, "negative", "14");
		line_chart_dataset.addValue(324, "negative", "15");
		
		
		line_chart_dataset.addValue(28, "positive", "1" );
		line_chart_dataset.addValue(30, "positive", "2");
		line_chart_dataset.addValue(60, "positive", "3");
		line_chart_dataset.addValue(210, "positive", "4");
		line_chart_dataset.addValue(334, "positive", "5");
		line_chart_dataset.addValue(450, "positive", "6");
		line_chart_dataset.addValue(228, "positive", "7" );
		line_chart_dataset.addValue(3022, "positive", "8");
		line_chart_dataset.addValue(620, "positive", "9");
		line_chart_dataset.addValue(2510, "positive", "10");
		line_chart_dataset.addValue(3364, "positive", "11");
		line_chart_dataset.addValue(4520, "positive", "12");
		line_chart_dataset.addValue(228, "positive", "13" );
		line_chart_dataset.addValue(3110, "positive", "14");
		line_chart_dataset.addValue(620, "positive", "15");
		
		JFreeChart lineChartObject = ChartFactory
				.createLineChart("", "Time", "Sentiment",
						line_chart_dataset, PlotOrientation.VERTICAL, true,
						true, false);

		int width = 640; /* Width of the image */
		int height = 480; /* Height of the image */
		File lineChart = new File("C:/Mywork/StreetBuzz/genchart/"+System.currentTimeMillis()+".jpg");
		try {
			ChartUtilities.saveChartAsJPEG(lineChart, lineChartObject, width,
					height);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		long end = System.currentTimeMillis();
		long time = (end-start)/1000;
		System.out.println("time:"+time+" secs");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		createChart();
	}

}
