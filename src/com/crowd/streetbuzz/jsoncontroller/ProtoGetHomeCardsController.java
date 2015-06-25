/**
 * 
 */
package com.crowd.streetbuzz.jsoncontroller;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.crowd.streetbuzz.common.Constants;
import com.crowd.streetbuzz.dao.implementation.ProtoSearchCardDAO;
import com.crowd.streetbuzz.model.ProtoSearchCard;
import com.crowd.streetbuzzalgo.utils.StrUtil;
import com.google.gson.Gson;

/**
 * @author Atrijit
 * 
 */
public class ProtoGetHomeCardsController implements Controller, Constants {
	private ProtoSearchCardDAO protoSearchCardDAO;

	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		System.out
				.println("Inside ProtoGetHomeCardsController ready to spotate");
		List searchList = protoSearchCardDAO.getAllReadyRecords();
		List finalList = new ArrayList();
		for (int i = 0; i < searchList.size(); i++) {
			ProtoSearchCard psc = (ProtoSearchCard) searchList.get(i);
			Date date = psc.getCreationdate();
			int daysago = StrUtil.getDaysAgo(date);
			psc.setDaysago(daysago);
			finalList.add(psc);

		}

		Gson gson = new Gson();
		response.addHeader("Access-Control-Allow-Origin", "*");
		response.setStatus(200);
		PrintWriter writer = response.getWriter();
		String retStr = gson.toJson(finalList);
		System.out.println("GetHomeCardsController: " + retStr);
		writer.write(retStr);
		writer.close();
		return null;
	}

	public ProtoSearchCardDAO getProtoSearchCardDAO() {
		return protoSearchCardDAO;
	}

	public void setProtoSearchCardDAO(ProtoSearchCardDAO protoSearchCardDAO) {
		this.protoSearchCardDAO = protoSearchCardDAO;
	}
}
