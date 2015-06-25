/**
 * 
 */
package com.crowd.streetbuzzalgo.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.apache.commons.codec.binary.Hex;


/**
 * @author Atrijit
 * 
 */
public class ByteUtils {

	/**
	 * @param args
	 */
	public static byte[] serialize(Object obj) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ObjectOutputStream os = new ObjectOutputStream(out);
		os.writeObject(obj);
		return out.toByteArray();
	}

	public static Object deserialize(byte[] data) throws IOException,
			ClassNotFoundException {
		ByteArrayInputStream in = new ByteArrayInputStream(data);
		ObjectInputStream is = new ObjectInputStream(in);
		return is.readObject();
	}

	public static String getHex(byte[] bytes) {
		return Hex.encodeHexString(bytes);
	}

	public static byte[] hexStringToByteArray(String hex) {
		int k = 0;
		byte[] results = new byte[hex.length() / 2];
		for (int i = 0; i < hex.length();) {
			results[k] = (byte) (Character.digit(hex.charAt(i++), 16) << 4);
			results[k] += (byte) (Character.digit(hex.charAt(i++), 16));
			k++;
		}
		return results;
	}

	public static void main(String[] args) {
		String hex = ".0rSU5LtMgxV3eTWFJPLJYd9f8xLp_o40YqdIFe9fNkApCBjVbFTkKuXlSkIzs76vPVSUWR51N39O_1RHlZZ9Q--";
		byte[] bytes =  hexStringToByteArray(hex);
		String str="";
		try {
			str = (String)deserialize(bytes);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(str);

	}

}
