/**
 * 
 */
package com.crowd.streetbuzz.stopword;

/**
 * @author Atrijit
 *
 */
public class Test {

	/**
	 * 
	 */
	public Test() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SingleStopper ss = SingleStopper.getInstance();
		String result = ss.cullPhrase("Narendra Modi corruption");
		System.out.println(result);
		if("".equalsIgnoreCase(result)){
			System.out.println("Empty");
		}
		

	}

}
