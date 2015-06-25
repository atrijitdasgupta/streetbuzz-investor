/**
 * 
 */
package com.crowd.streetbuzzalgo.taxonomytree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import com.crowd.streetbuzz.dao.implementation.TaxonomyDAO;
import com.crowd.streetbuzz.model.Taxonomy;
import com.crowd.streetbuzzalgo.taxonomy.freebase.Freebase;
import com.crowd.streetbuzzalgo.taxonomy.googlesuggest.GoogleSuggest;

/**
 * @author Atrijit
 * 
 */

/*
 * CREATE TABLE SB_TAXONOMY( ID BIGINT not null auto_increment, ROOTWORD
 * VARCHAR(400), LINKEDWORD VARCHAR(400), TYPE VARCHAR(20), SCORE BIGINT,
 * CATEGORYID BIGINT, RUNNUM BIGINT, HASHVALUE VARCHAR(200), PRIMARY KEY (ID))
 * ENGINE=InnoDB;
 */
public class TraverseTaxonomy {
	public static boolean ASC = true;

	public static boolean DESC = false;

	/**
	 * 
	 */
	public TraverseTaxonomy() {
		// TODO Auto-generated constructor stub
	}

	public static List traverse(String phrase, TaxonomyDAO taxonomyDAO,
			Long categoryid) {
		return scourTree(phrase, taxonomyDAO, categoryid);
	}

	public static List getCategory(String phrase, TaxonomyDAO taxonomyDAO) {
		return determineCategoryRank(phrase, taxonomyDAO);
	}

	private static List determineCategoryRank(String phrase,
			TaxonomyDAO taxonomyDAO) {
		List rootList = taxonomyDAO.getAllRecordsByRoot(phrase);
		List linkedList = taxonomyDAO.getAllRecordsByLink(phrase);
		Map map = new HashMap();
		// Root lists
		for (int i = 0; i < rootList.size(); i++) {
			Taxonomy tax = (Taxonomy) rootList.get(i);
			Long categoryId = tax.getCategoryid();
			if (map.containsKey(categoryId)) {
				Integer value = (Integer) map.get(categoryId);
				int num = value.intValue();
				num = num + 1;
				value = new Integer(num);
				map.put(categoryId, value);
			} else {
				map.put(categoryId, new Integer(1));
			}

		}

		// Linked Lists
		for (int i = 0; i < linkedList.size(); i++) {
			Taxonomy tax = (Taxonomy) linkedList.get(i);
			Long categoryId = tax.getCategoryid();
			if (map.containsKey(categoryId)) {
				Integer value = (Integer) map.get(categoryId);
				int num = value.intValue();
				num = num + 1;
				value = new Integer(num);
				map.put(categoryId, value);
			} else {
				map.put(categoryId, new Integer(1));
			}
		}
		Map sortedMap = new HashMap();
		if (map.size() > 0) {
			sortedMap = sortByIntegerComparator(map, DESC);
			List catlist = new ArrayList();
			Set keySet = sortedMap.keySet();
			Iterator it = keySet.iterator();
			while (it.hasNext()) {
				Integer key = (Integer) it.next();
				catlist.add(key);
				return catlist;
			}
		} else {
			// Get Freebase list for phrase
			List freebaseList = new ArrayList();
			try {
				freebaseList = Freebase.getList(phrase);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			List googleSuggestList = new ArrayList();
			try {
				googleSuggestList = GoogleSuggest.googleSuggest(phrase);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			List rootExternalList = new ArrayList();
			List linkedExternalList = new ArrayList();

			if (freebaseList.size() > 0) {
				for (int i = 0; i < freebaseList.size(); i++) {
					String word = (String) freebaseList.get(i);
					List rootlist = taxonomyDAO.getAllRecordsByRoot(word);
					List linkedlist = taxonomyDAO.getAllRecordsByLink(word);
					rootExternalList.addAll(rootlist);
					linkedExternalList.addAll(linkedlist);
				}
			}
			if (googleSuggestList.size() > 0) {
				for (int i = 0; i < googleSuggestList.size(); i++) {
					String word = (String) googleSuggestList.get(i);
					List rootlist = taxonomyDAO.getAllRecordsByRoot(word);
					List linkedlist = taxonomyDAO.getAllRecordsByLink(word);
					rootExternalList.addAll(rootlist);
					linkedExternalList.addAll(linkedlist);
				}
			}

			for (int i = 0; i < rootExternalList.size(); i++) {
				Taxonomy tax = (Taxonomy) rootExternalList.get(i);
				Long categoryId = tax.getCategoryid();
				if (map.containsKey(categoryId)) {
					Integer value = (Integer) map.get(categoryId);
					int num = value.intValue();
					num = num + 1;
					value = new Integer(num);
					map.put(categoryId, value);
				} else {
					map.put(categoryId, new Integer(1));
				}

			}

			for (int i = 0; i < linkedExternalList.size(); i++) {
				Taxonomy tax = (Taxonomy) linkedExternalList.get(i);
				Long categoryId = tax.getCategoryid();
				if (map.containsKey(categoryId)) {
					Integer value = (Integer) map.get(categoryId);
					int num = value.intValue();
					num = num + 1;
					value = new Integer(num);
					map.put(categoryId, value);
				} else {
					map.put(categoryId, new Integer(1));
				}
			}

			if (map.size() > 0) {
				sortedMap = sortByIntegerComparator(map, DESC);
				List catlist = new ArrayList();
				Set keySet = sortedMap.keySet();
				Iterator it = keySet.iterator();
				while (it.hasNext()) {
					Integer key = (Integer) it.next();
					catlist.add(key);
					return catlist;
				}
			}

		}
		return new ArrayList();
	}

	private static List scourTree(String phrase, TaxonomyDAO taxonomyDAO,
			Long categoryid) {
		List rootList = taxonomyDAO.getAllRecordsByRootCategory(phrase,
				categoryid);
		List linkedList = taxonomyDAO.getAllRecordsByLinkCategory(phrase,
				categoryid);

		// Get Freebase list for phrase
		List freebaseList = new ArrayList();
		try {
			freebaseList = Freebase.getList(phrase);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List googleSuggestList = new ArrayList();
		try {
			googleSuggestList = GoogleSuggest.googleSuggest(phrase);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		List rootExternalList = new ArrayList();
		List linkedExternalList = new ArrayList();

		if (freebaseList.size() > 0) {
			for (int i = 0; i < freebaseList.size(); i++) {
				String word = (String) freebaseList.get(i);
				List rootlist = taxonomyDAO.getAllRecordsByRootCategory(word,
						categoryid);
				List linkedlist = taxonomyDAO.getAllRecordsByLinkCategory(word,
						categoryid);
				rootExternalList.addAll(rootlist);
				linkedExternalList.addAll(linkedlist);
			}
		}
		if (googleSuggestList.size() > 0) {
			for (int i = 0; i < googleSuggestList.size(); i++) {
				String word = (String) googleSuggestList.get(i);
				List rootlist = taxonomyDAO.getAllRecordsByRootCategory(word,
						categoryid);
				List linkedlist = taxonomyDAO.getAllRecordsByLinkCategory(word,
						categoryid);
				rootExternalList.addAll(rootlist);
				linkedExternalList.addAll(linkedlist);
			}
		}
		Map map = new HashMap();
		// Root lists
		for (int i = 0; i < rootList.size(); i++) {
			Taxonomy tax = (Taxonomy) rootList.get(i);
			String linkedword = tax.getLinkedword();
			if (map.containsKey(linkedword)) {
				Integer value = (Integer) map.get(linkedword);
				int num = value.intValue();
				num = num + 1;
				value = new Integer(num);
				map.put(linkedword, value);
			} else {
				map.put(linkedword, new Integer(1));
			}

		}
		for (int i = 0; i < rootExternalList.size(); i++) {
			Taxonomy tax = (Taxonomy) rootExternalList.get(i);
			String linkedword = tax.getLinkedword();
			if (map.containsKey(linkedword)) {
				Integer value = (Integer) map.get(linkedword);
				int num = value.intValue();
				num = num + 1;
				value = new Integer(num);
				map.put(linkedword, value);
			} else {
				map.put(linkedword, new Integer(1));
			}

		}
		// Linked Lists
		for (int i = 0; i < linkedList.size(); i++) {
			Taxonomy tax = (Taxonomy) linkedList.get(i);
			String rootword = tax.getRootword();
			if (map.containsKey(rootword)) {
				Integer value = (Integer) map.get(rootword);
				int num = value.intValue();
				num = num + 1;
				value = new Integer(num);
				map.put(rootword, value);
			} else {
				map.put(rootword, new Integer(1));
			}
		}

		for (int i = 0; i < linkedExternalList.size(); i++) {
			Taxonomy tax = (Taxonomy) linkedExternalList.get(i);
			String rootword = tax.getRootword();
			if (map.containsKey(rootword)) {
				Integer value = (Integer) map.get(rootword);
				int num = value.intValue();
				num = num + 1;
				value = new Integer(num);
				map.put(rootword, value);
			} else {
				map.put(rootword, new Integer(1));
			}
		}
		Map sortedMap = sortByComparator(map, DESC);
		Set keyset = sortedMap.keySet();
		Iterator it = keyset.iterator();
		List list = new ArrayList();
		while (it.hasNext()) {
			String key = (String) it.next();
			list.add(key);
		}

		return list;
	}

	public static Map<String, Integer> sortByComparator(
			Map<String, Integer> unsortMap, final boolean order) {

		List<Entry<String, Integer>> list = new LinkedList<Entry<String, Integer>>(
				unsortMap.entrySet());

		// Sorting the list based on values
		Collections.sort(list, new Comparator<Entry<String, Integer>>() {
			public int compare(Entry<String, Integer> o1,
					Entry<String, Integer> o2) {
				if (order) {
					return o1.getValue().compareTo(o2.getValue());
				} else {
					return o2.getValue().compareTo(o1.getValue());

				}
			}
		});

		// Maintaining insertion order with the help of LinkedList
		Map<String, Integer> sortedMap = new LinkedHashMap<String, Integer>();
		for (Entry<String, Integer> entry : list) {
			sortedMap.put(entry.getKey(), entry.getValue());
		}

		return sortedMap;
	}

	private static Map<Integer, Integer> sortByIntegerComparator(
			Map<Integer, Integer> unsortMap, final boolean order) {

		List<Entry<Integer, Integer>> list = new LinkedList<Entry<Integer, Integer>>(
				unsortMap.entrySet());

		// Sorting the list based on values
		Collections.sort(list, new Comparator<Entry<Integer, Integer>>() {
			public int compare(Entry<Integer, Integer> o1,
					Entry<Integer, Integer> o2) {
				if (order) {
					return o1.getValue().compareTo(o2.getValue());
				} else {
					return o2.getValue().compareTo(o1.getValue());

				}
			}
		});

		// Maintaining insertion order with the help of LinkedList
		Map<Integer, Integer> sortedMap = new LinkedHashMap<Integer, Integer>();
		for (Entry<Integer, Integer> entry : list) {
			sortedMap.put(entry.getKey(), entry.getValue());
		}

		return sortedMap;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		HashMap map = new HashMap();

		/*
		 * map.put("ewd", new Integer(14)); map.put("abc", new Integer(44));
		 * map.put("pqr", new Integer(11)); map.put("xyz", new Integer(23));
		 */

		map.put(new Integer(2), new Integer(14));
		map.put(new Integer(4), new Integer(44));
		map.put(new Integer(6), new Integer(11));
		map.put(new Integer(8), new Integer(23));

		Map sortedMap = sortByIntegerComparator(map, DESC);
		Set keyset = sortedMap.keySet();
		Iterator it = keyset.iterator();
		List list = new ArrayList();
		while (it.hasNext()) {
			Integer key = (Integer) it.next();
			Integer val = (Integer) sortedMap.get(key);
			System.out.println(key + "::" + val.toString());
			list.add(key);
		}
		for (int i = 0; i < list.size(); i++) {
			Integer word = (Integer) list.get(i);
			System.out.println(word.toString());
		}

	}

}
