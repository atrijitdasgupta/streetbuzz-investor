/**
 * MyClient.java
 * 
 * Example client that uses textalytics.com Social Media analysis service
 * 
 * @version 1.0
 * @author zdepablo
 * @contact    -- http://www.textalytics.com (http://www.daedalus.es)
 * @copyright  -- Copyright (c) 2013, DAEDALUS S.A. All rights reserved.
 */
package es.daedalus.textalytics.sma;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpResponseException;

import es.daedalus.textalytics.sma.domain.Result;

public class MyClient {
	
	static String key;
	static String txt;
	static String filename;
	static String what;

	/**
	 * key : textalytics.com SMA key. 
	 * txt : text to analyze
	 * filename: a file with text to analyze
	 * what: teh fields to extract 
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		fillArguments(args);
		
		System.out.println("key= " + key);
		System.out.println("txt= " + txt);
		System.out.println("filename= " + filename);
		System.out.println("what= " + what);
		
		SmaClient smaclient = new SmaClient(key);
		smaclient.setFields(what);
		try {
			Result result = smaclient.analyze(txt);

			System.out.println("----------------------------------------------");		
			System.out.println(result.toString());
			System.out.println("----------------------------------------------");

			// API request errors are returned as HTTP error codes
		} catch (HttpResponseException e) {
			System.err.println(e.getStatusCode() + " " + e.getLocalizedMessage());

		} catch (ClientProtocolException e) {
			System.err.println(e.getLocalizedMessage());
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println(e.getLocalizedMessage());
			e.printStackTrace();
		}		


	}


	/*
	 * Parse, validate and fill arguments or exit 	
	 */
	private static void fillArguments(String[] args) {

		String newline = System.getProperty("line.separator");
		
		// Read arguments 
		if (args.length < 2) usage("Missing arguments");			
	
		key = parseArg("-key=", args[0]);
		txt = parseArg("-txt=", args[1]);
		filename = parseArg("-file=", args[1]);
		
		what = null;
		if (args.length == 3 ) what= parseArg("-what=", args[2]);		
	
		if (key == null) usage("Missing service key");
		if ((txt == null) && (filename == null)) usage("Provide text (txt) or a file with text to analyze (filename)");			
		
		// If file, read contents
		if (filename != null) {
			BufferedReader br = null;
			String line = null;
			StringBuffer sb = new StringBuffer();
			try {
				br = new BufferedReader(new FileReader(new File(filename)));
				while ( (line = br.readLine()) != null) {
					sb.append(line).append(newline);
				}
				txt = sb.toString();
			} catch (IOException e) {
				usage("Check file " + filename);
			} finally {
					try {
						if (br != null ) br.close();
					} catch (IOException e) { e.printStackTrace(); }
			}
		}

		if (txt == null || txt.isEmpty()) usage("There is no text to analyze");

	}

	
	/*
	 * Helper method to report errors and usage
	 */
	private static void usage(String message) {
		String usage = "Usage: java " + MyClient.class + " -key=<key> (-txt=<txt>|-file=<filename>) [-what=<fields>]" +
				"\n-key=<key> --> textalytics service key" + 
				"\n-txt=<text> or -file=<filename>  --> the text to analyze" +
               	"\n-what=<elements>  --> the elements to extract, separated by '|'. " + 
				"\nThe value of elements can be: sentiment, categories, entities, concepts, timeExpressions, moneyExpressions, uris and phoneExpressions or any combination of them. " +
				"\nIf the parameter is omited all the elements are obtained." +
                "\nMore info in the Documentation section of Media Analysis API at textalytics.com";


		System.err.println(message);
		System.err.println();
		System.err.println(usage);
		System.exit(-1);
	}

	/*
	 * Helper method to parse a single argument 
	 */
	private static String parseArg(String name, String argument) {
		if (!argument.startsWith(name)) return null;
		return argument.substring(name.length());
	}

	
		
}
