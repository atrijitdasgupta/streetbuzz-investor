/**
 * 
 */
package com.crowd.streetbuzzalgo.imagecompression;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;

import org.imgscalr.Scalr;
import org.imgscalr.Scalr.Mode;

import com.crowd.streetbuzz.common.Constants;
import com.crowd.streetbuzzalgo.utils.StrUtil;

/**
 * @author Atrijit
 * 
 */
public class ImageCompress implements Constants {

	/**
	 * 
	 */
	public ImageCompress() {
		// TODO Auto-generated constructor stub
	}

	public static String compress(String imageUrl) {
		String tempfilename = StrUtil.getUniqueId();
		String tempfileextn = "";
		try {
			tempfileextn = imageUrl.substring(imageUrl.lastIndexOf("."),
					imageUrl.length());
		} catch (Exception e1) {
			System.out.println(e1.getMessage());
		}
		System.out.println(tempfileextn);
		String tempfile = tempfilename + tempfileextn;
		String tempfilepath = BASESBSTORAGEPATH + tempfile;
		boolean saveimage = saveImage(imageUrl, tempfilepath);
		if (!saveimage) {
			return "false";
		}
		BufferedImage img = null;
		try {
			System.out.println(tempfilepath);
			img = ImageIO.read(new File(tempfilepath));
		} catch (IOException e) {
			return "false";
		}
		int type = img.getType() == 0? BufferedImage.TYPE_INT_ARGB : img.getType();
		
		//BufferedImage scaledImg = Scalr.resize(img, Mode.AUTOMATIC, 320, 240);
		BufferedImage scaledImg = resizeImage(img,type,320,240);
		// System.out.println(scaledImg);
		String destFileName = StrUtil.getUniqueId();
		String destFilePath = BASESBSTORAGEPATH + destFileName + ".gif";
		File destFile = new File(destFilePath);
		try {
			ImageIO.write(scaledImg, tempfileextn, destFile);
		} catch (IOException e) {
			return "false";
		}
		// FileUtils.deleteFile(tempfilepath);
		return destFileName;
	}

	private static BufferedImage resizeImage(BufferedImage originalImage,
			int type,int width, int height) {
		BufferedImage resizedImage = new BufferedImage(width, height, type);
		Graphics2D g = resizedImage.createGraphics();
		g.drawImage(originalImage, 0, 0, width, height, null);
		g.dispose();

		return resizedImage;
	}

	public static boolean saveImage(String imageUrl, String destinationFile) {
		URL url = null;
		try {
			url = new URL(imageUrl);
		} catch (MalformedURLException e) {
			return false;
		}
		InputStream is = null;
		try {
			is = url.openStream();
		} catch (IOException e) {
			return false;
		}
		OutputStream os = null;
		try {
			os = new FileOutputStream(destinationFile);
		} catch (FileNotFoundException e) {
			return false;
		}

		byte[] b = new byte[2048];
		int length;

		try {
			while ((length = is.read(b)) != -1) {
				os.write(b, 0, length);
			}

			is.close();
			os.close();
		} catch (IOException e) {
			return false;
		}
		return true;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out
				.println(saveImage("http://www.starkrfid.com/img/nfcc-graphic.jpg",("C:/Mywork/StreetBuzz/zeroimage/"+System.currentTimeMillis()+".jpg")));
	}

}
