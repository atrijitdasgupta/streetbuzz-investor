/**
 * 
 */
package com.crowd.streetbuzzalgo.utils;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Atrijit
 * 
 */
public class CollectionsUtils {

	public static <String, Integer extends Comparable<? super Integer>> Map<String, Integer> sortByValue(
			Map<String, Integer> map) {
		List<Map.Entry<String, Integer>> list = new LinkedList<Map.Entry<String, Integer>>(
				map.entrySet());
		Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
			public int compare(Map.Entry<String, Integer> o1,
					Map.Entry<String, Integer> o2) {
				return (o1.getValue()).compareTo(o2.getValue());
			}
		});

		Map<String, Integer> result = new LinkedHashMap<String, Integer>();
		for (Map.Entry<String, Integer> entry : list) {
			result.put(entry.getKey(), entry.getValue());
		}
		return result;
	}
	
	public static <String, Long extends Comparable<? super Long>> Map<String, Long> sortByLongValue(
			Map<String, Long> map) {
		List<Map.Entry<String, Long>> list = new LinkedList<Map.Entry<String, Long>>(
				map.entrySet());
		Collections.sort(list, new Comparator<Map.Entry<String, Long>>() {
			public int compare(Map.Entry<String, Long> o1,
					Map.Entry<String, Long> o2) {
				return (o1.getValue()).compareTo(o2.getValue());
			}
		});

		Map<String, Long> result = new LinkedHashMap<String, Long>();
		for (Map.Entry<String, Long> entry : list) {
			result.put(entry.getKey(), entry.getValue());
		}
		return result;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Map map = new HashMap();
		map.put("ab", new Integer(22));
		map.put("xyz", new Integer(22));
		map.put("min", new Integer(49));
		map.put("pqr", new Integer(12));
		map = sortByValue(map);

		Set set = map.keySet();
		Iterator it = set.iterator();
		while (it.hasNext()) {
			String key = (String) it.next();
			Integer value = (Integer) map.get(key);
			System.out.println(key + "::" + value.toString());

		}

	}

}
