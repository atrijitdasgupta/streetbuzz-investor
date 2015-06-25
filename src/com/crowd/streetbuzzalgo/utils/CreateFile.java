/**
 * 
 */
package com.crowd.streetbuzzalgo.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author Atrijit
 *
 */
public class CreateFile {

	/**
	 * 
	 */
	public CreateFile() {
		// TODO Auto-generated constructor stub
	}
	public static void write(String content, String mode){
		String filename = mode+System.currentTimeMillis()+".html";
		String location = "";
		if ("blogspot".equalsIgnoreCase(mode)){
			location = "blogspot";
		}else if ("wordpress".equalsIgnoreCase(mode)){
			location = "wordpress";
		}else if ("youtube".equalsIgnoreCase(mode)){
			location = "youtube";
		}else{
			location = "generic";
		}
		filename = "C:/Mywork/StreetBuzz/WebScraping/files/"+location+"/"+filename;
		File file = new File(filename);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		try {
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(content);
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
