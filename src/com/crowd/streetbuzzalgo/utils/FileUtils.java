/**
 * 
 */
package com.crowd.streetbuzzalgo.utils;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Copy;
import org.apache.tools.ant.types.FileSet;

import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

/**
 * @author Atrijit Dasgupta
 * 
 */
public class FileUtils {
	public static boolean writeBytestoFile(byte[] filebyte, String writePath) {
		boolean retBool = true;
		FileOutputStream fout = null;

		try {
			fout = new FileOutputStream(writePath);
			fout.write(filebyte);
		} catch (Exception e) {
			e.printStackTrace();
			retBool = false;

			File file = new File(writePath);

			if (file.exists()) {
				file.delete();
			}
		} finally {
			if (fout != null) {
				try {
					fout.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		return retBool;
	}

	public static void deleteFile(String path) {
		File file = new File(path);
		file.delete();
	}

	public static void deleteDir(String path) {
		if (path.endsWith(File.separator))
			path = path.substring(0, path.length() - 1);
		File dir = new File(path);
		String lists[] = dir.list();
		for (int i = 0; lists != null && i < lists.length; i++) {
			File subfile = new File(path + File.separator + lists[i]);
			if (subfile.isDirectory())
				deleteDir(path + File.separator + lists[i]);
			else
				deleteFile(path + File.separator + lists[i]);
		}

		dir.delete();
	}

	public static boolean copyDirectory(File srcDir, File distDir) {
		if (!distDir.isDirectory()) {
			distDir.mkdir();
		}
		// use ANt copy task to copy the directory
		Project project = new Project();
		Copy copydir = new Copy();
		copydir.setProject(project);
		copydir.setTodir(distDir);
		FileSet srcFileSet = new FileSet();
		srcFileSet.setDir(srcDir);
		copydir.addFileset(srcFileSet);
		copydir.execute();
		return true;
	}

	public static void copyFile(File srcFile, File destinationFile)
			throws IOException {
		// use ANt copy task to copy the file
		Project project = new Project();
		Copy copydir = new Copy();
		copydir.setProject(project);
		copydir.setTofile(destinationFile);
		copydir.setFile(srcFile);
		copydir.execute();

	}

	public static void copyFile(String srcFilePath, String destinationFilePath)
			throws IOException {
		File inFile = new File(srcFilePath);
		File outFile = new File(destinationFilePath);
		copyFile(inFile, outFile);
	}

	public static void download(String address, String localFileName) {

		OutputStream out = null;
		URLConnection conn = null;
		InputStream in = null;
		try {
			URL url = new URL(address);
			out = new BufferedOutputStream(new FileOutputStream(localFileName));
			conn = url.openConnection();
			in = conn.getInputStream();
			byte[] buffer = new byte[1024];
			int numRead;
			long numWritten = 0;
			while ((numRead = in.read(buffer)) != -1) {
				out.write(buffer, 0, numRead);
				numWritten += numRead;
			}
			System.out.println(localFileName + "\t" + numWritten);
		} catch (Exception exception) {
			exception.printStackTrace();
		} finally {
			try {
				if (in != null) {
					in.close();
				}
				if (out != null) {
					out.close();
				}
			} catch (IOException ioe) {
			}
		}
	}

	public static byte[] getBytedataofFile(File f) {
		byte buffer[];
		buffer = new byte[0];
		try {
			FileInputStream in = null;
			int len = 0;
			try {
				len = (int) f.length();

				buffer = new byte[len];
				in = new FileInputStream(f);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			int numbytesread = 0;
			while (numbytesread < len) {
				numbytesread += in.read(buffer, numbytesread, len
						- numbytesread);
			}
			in.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
		return buffer;
	}

	public static List<File> getFileListing(File aStartingDir)
			throws FileNotFoundException {
		validateDirectory(aStartingDir);
		List<File> result = getFileListingNoSort(aStartingDir);
		Collections.sort(result);
		return result;
	}

	private static void validateDirectory(File aDirectory)
			throws FileNotFoundException {
		if (aDirectory == null) {
			throw new IllegalArgumentException("Directory should not be null.");
		}
		if (!aDirectory.exists()) {
			throw new FileNotFoundException("Directory does not exist: "
					+ aDirectory);
		}
		if (!aDirectory.isDirectory()) {
			throw new IllegalArgumentException("Is not a directory: "
					+ aDirectory);
		}
		if (!aDirectory.canRead()) {
			throw new IllegalArgumentException("Directory cannot be read: "
					+ aDirectory);
		}
	}

	public static void writeToFile(InputStream is, String filePath, String name) {

		OutputStream out = null;

		try {
			File file = new File(filePath);
			if (!file.exists()) {
				file.mkdirs();
			}
			if (file.exists() && file.isDirectory()) {
				file = new File(filePath + File.separator + name);
				out = new FileOutputStream(file);
				byte buf[] = new byte[1024];
				int len;
				while ((len = is.read(buf)) > 0) {
					out.write(buf, 0, len);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private static List<File> getFileListingNoSort(File aStartingDir)
			throws FileNotFoundException {
		List<File> result = new ArrayList<File>();
		File[] filesAndDirs = aStartingDir.listFiles();
		List<File> filesDirs = Arrays.asList(filesAndDirs);
		for (File file : filesDirs) {
			result.add(file); // always add, even if directory
			if (!file.isFile()) {
				// must be a directory
				// recursive call!
				List<File> deeperList = getFileListingNoSort(file);
				result.addAll(deeperList);
			}
		}
		return result;
	}
	
	private static void updatePdf()throws Exception{
		String filename = "D:/ack.pdf";
		PdfReader pdfReader = new PdfReader(filename);
		PdfStamper pdfStamper = new PdfStamper(pdfReader, new FileOutputStream("PDF_Hello_World_Modified_Example.pdf"));
		PdfContentByte underContent = pdfStamper.getUnderContent(1);
		underContent.beginText();
		BaseFont bf = BaseFont.createFont(BaseFont.COURIER, BaseFont.WINANSI, BaseFont.EMBEDDED);
        underContent.setFontAndSize(bf, 12);
        underContent.showTextAligned(Element.ALIGN_TOP, "This is footer hmm hmm hmm", 0, 2, 0);
        underContent.endText();
        pdfStamper.close();
	}
	
	public static String getExtension(String filepath){
		String extn = "";
		if(filepath.indexOf(".")>0){
			extn = filepath.substring(filepath.indexOf("."), filepath.length());
		}
		return extn;
	}
	
	public static String getClearExtension(String extn){
		if(extn.startsWith(".")){
			extn =  extn.substring((extn.indexOf(".")+1), extn.length());
		}
		extn = extn.trim();
		return extn;
	}
	public static List readFile(File file){
		List list = new ArrayList();
		BufferedReader in = null;
		try {
			in = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			while (in.ready()) {
				  String s = in.readLine();
				  s = s.trim();
				 // s = s.toLowerCase();
				  if(!list.contains(s)){
					  list.add(s);
				  }
				 
				}
				in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	public static void readLines(File file){
		BufferedReader in = null;
		try {
			in = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		StringBuffer sbfr = new StringBuffer();
		try {
			while (in.ready()) {
				  String s = in.readLine();
				  s = s.trim();
				  s = s.toLowerCase();
				  sbfr.append(s+",");
				}
				in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(sbfr.toString());
	}
	public static void main(String[] args) {
		readLines(new File("C:/Mywork/StreetBuzz/morenegatives.txt"));
	}

}
