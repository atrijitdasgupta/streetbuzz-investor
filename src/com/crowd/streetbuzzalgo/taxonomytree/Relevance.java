/**
 * 
 */
package com.crowd.streetbuzzalgo.taxonomytree;

import java.util.ArrayList;
import java.util.List;

import com.crowd.streetbuzz.dao.implementation.TaxonomyDAO;
import com.crowd.streetbuzz.model.Taxonomy;

/**
 * @author Atrijit
 * 
 */
public class Relevance {

	/**
	 * 
	 */
	public Relevance() {
		// TODO Auto-generated constructor stub
	}

	public static void getRelevance(TaxonomyDAO taxonomyDAO) {
		// Get list of Taxonomy objects within each category
		List tList = taxonomyDAO.getAllRecordsbyCategory(new Long(3));
		process(tList, taxonomyDAO);

		tList = taxonomyDAO.getAllRecordsbyCategory(new Long(4));
		process(tList, taxonomyDAO);
	}

	private static void process(List tList, TaxonomyDAO taxonomyDAO) {
		//Let's get the unique hash values
		List hashList = new ArrayList();
		for (int i=0;i<tList.size();i++){
			Taxonomy t = (Taxonomy)tList.get(i);
			String hash = t.getHash();
			if(!hashList.contains(hash)){
				hashList.add(hash);
			}
		}
		
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
