/**
 * 
 */
package com.crowd.streetbuzzalgo.languagedetection;

import java.util.List;

import com.detectlanguage.DetectLanguage;
import com.detectlanguage.Result;
import com.detectlanguage.errors.APIError;

/**
 * @author Atrijit
 *
 */
public class LanguageDetect {

	/**
	 * 
	 */
	public LanguageDetect() {
		// TODO Auto-generated constructor stub
	}
	public static boolean isEnglish(String text){
		String results = "";
		try {
			results = DetectLanguage.simpleDetect(text);
		} catch (APIError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(results!=null){
			System.out.println(results);
		}
		return false;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println(isEnglish("buens noches senor"));

	}

}
