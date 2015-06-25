/**
 * 
 */
package com.crowd.streetbuzzalgo.utils;

/**
 * @author Atrijit
 *
 */
public class TemporaryCategoryUtils {

	/**
	 * 
	 */
	public TemporaryCategoryUtils() {
		// TODO Auto-generated constructor stub
	}
	
	public static String getCategoryStr(String category){
		String categoryStr = "";
		if("1".equalsIgnoreCase(category)){
			categoryStr = "Business & Technology";
		}else if("2".equalsIgnoreCase(category)){
			categoryStr = "Culture & Entertainment";
		}else if("3".equalsIgnoreCase(category)){
			categoryStr = "Education & Work";
		}else if("4".equalsIgnoreCase(category)){
			categoryStr = "Family & Living";
		}else if("5".equalsIgnoreCase(category)){
			categoryStr = "Fashion & Beauty";
		}else if("6".equalsIgnoreCase(category)){
			categoryStr = "Food & Drinks";
		}else if("7".equalsIgnoreCase(category)){
			categoryStr = "Gadgets & Appliances";
		}else if("8".equalsIgnoreCase(category)){
			categoryStr = "Health & Fitness";
		}else if("9".equalsIgnoreCase(category)){
			categoryStr = "Home & Garden";
		}else if("10".equalsIgnoreCase(category)){
			categoryStr = "Motors & Vehicles";
		}else if("11".equalsIgnoreCase(category)){
			categoryStr = "Nature & Climate";
		}else if("12".equalsIgnoreCase(category)){
			categoryStr = "Politics & Economy";
		}else if("13".equalsIgnoreCase(category)){
			categoryStr = "Religion & Philosophy";
		}else if("14".equalsIgnoreCase(category)){
			categoryStr = "Sports & Hobbies";
		}else if("15".equalsIgnoreCase(category)){
			categoryStr = "Travel & Tourism";
		}else if("16".equalsIgnoreCase(category)){
			categoryStr = "Science & Mathematics";
		}
		return categoryStr;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
