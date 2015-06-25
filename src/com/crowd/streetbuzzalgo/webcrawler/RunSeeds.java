/**
 * 
 */
package com.crowd.streetbuzzalgo.webcrawler;

import java.io.File;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.crowd.streetbuzzalgo.utils.FileUtils;
import com.crowd.streetbuzzalgo.vo.ThreadObjectReturn;

/**
 * @author Atrijit
 *
 */
public class RunSeeds {
	private static final int MYTHREADS = 10;
	/**
	 * 
	 */
	public RunSeeds() {
		// TODO Auto-generated constructor stub
	}
	
	private static List readFile(){
		String seedfile = "C:/Mywork/StreetBuzz/crawler/seedfile/seedfile.txt";
		File file = new File(seedfile);
		List list = FileUtils.readFile(file);
		return list;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ExecutorService executor = Executors.newFixedThreadPool(MYTHREADS);
		List list = readFile();
		for (int i=0;i<list.size();i++){
			String url = (String)list.get(i);
			Callable worker = new RunnableCrawler(url,i);
			Future <ThreadObjectReturn> future = executor.submit(worker);
			while (!future.isDone()) {
				System.out.println("Task is not completed yet....");
	            try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		executor.shutdown();
		
		System.out.println("\nFinished all threads");

	}

}
