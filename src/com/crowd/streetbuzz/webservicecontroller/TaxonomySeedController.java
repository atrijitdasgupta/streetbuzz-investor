/**
 * 
 */
package com.crowd.streetbuzz.webservicecontroller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.crowd.streetbuzz.common.Constants;
import com.crowd.streetbuzz.dao.implementation.TaxonomyDAO;
import com.crowd.streetbuzzalgo.taxonomytree.TaxonomyPopulate;
import com.crowd.streetbuzzalgo.taxonomytree.TaxonomyTree;
import com.crowd.streetbuzzalgo.utils.StrUtil;

/**
 * @author Atrijit
 * 
 */
public class TaxonomySeedController implements Controller, Constants {
	private TaxonomyDAO taxonomyDAO;

	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		System.out.println("Starting seeding");
		String mode = StrUtil.nonNull(request.getParameter("mode"));
		if("seed".equalsIgnoreCase(mode)){
			TaxonomyTree.seed(taxonomyDAO);
		}
		else if("populate".equalsIgnoreCase(mode)){
			TaxonomyPopulate.populate(taxonomyDAO);
		}else{
			TaxonomyTree.seed(taxonomyDAO);
			TaxonomyPopulate.populate(taxonomyDAO);
		}
		
		
		System.out.println("Done seeding");
		return null;
	}

	public TaxonomyDAO getTaxonomyDAO() {
		return taxonomyDAO;
	}

	public void setTaxonomyDAO(TaxonomyDAO taxonomyDAO) {
		this.taxonomyDAO = taxonomyDAO;
	}
}
