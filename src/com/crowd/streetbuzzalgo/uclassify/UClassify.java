/**
 * 
 */
package com.crowd.streetbuzzalgo.uclassify;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.onesun.textmining.uclassify.ResultHandler;
import org.onesun.textmining.uclassify.ServiceType;
import org.onesun.textmining.uclassify.UClassifyService;

import com.crowd.streetbuzz.common.Constants;

/**
 * @author Atrijit
 * 
 */
public class UClassify implements Constants {
	

	
	public UClassify() {
		// TODO Auto-generated constructor stub
	}
	
	

	public Map run(String text) {
		final Map rmap = new HashMap();
		// Map map = new HashMap();
		for (ServiceType service : ServiceType.values()) {
			UClassifyService.setUClassifyReadAccessKey(uClassifyReadKey);
			UClassifyService uClassifyService = new UClassifyService(text,
					service, new ResultHandler() {

						// @Override
						public void process(ServiceType serviceType,
								Map<String, Double> results) {
							/*System.out
									.println("---------------------------------------------------------------------\n"
											+ serviceType.getUrl()
											+ " <<<>>> "
											+ serviceType.getClassifier()
											+ "\n"
											+ "---------------------------------------------------------------------\n");*/
							String topic = serviceType.getClassifier();
							boolean add = false;
							/*if ("Topics".equalsIgnoreCase(topic)||"Tonality".equalsIgnoreCase(topic)) {
								System.out.println(topic);
							}*/
							
							for (String key : results.keySet()) {
								Double result = results.get(key);

								// interested in match >= 25%
								/*if (result >= 50)
									System.out.format("%1$-50s %2$10.2f\n",
											key, result);*/

								if (result > 50.0) {

									rmap.put(key, result);
								}

							}
							
						}
					});
			try {
				uClassifyService.process();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return rmap;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Map map = new UClassify().run("Narendra Modi");
		Set set = map.keySet();
		Iterator it = set.iterator();
		while (it.hasNext()){
			String key = (String)it.next();
			Double value = (Double)map.get(key);
			System.out.println(key+"::"+value);
		}

	}

}
