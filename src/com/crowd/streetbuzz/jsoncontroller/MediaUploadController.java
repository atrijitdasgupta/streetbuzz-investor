/**
 * 
 */
package com.crowd.streetbuzz.jsoncontroller;

import java.io.InputStream;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.crowd.streetbuzz.common.Constants;
import com.crowd.streetbuzz.dao.implementation.MediaFilesDAO;
import com.crowd.streetbuzz.model.MediaFiles;
import com.crowd.streetbuzzalgo.utils.FileUtils;
import com.crowd.streetbuzzalgo.utils.StrUtil;

/**
 * @author Atrijit
 *
 */
public class MediaUploadController implements Controller, Constants{
	String savedfilePath = "";
	private MediaFilesDAO mediaFilesDAO;
	String extension = "";
	
	
	public MediaFilesDAO getMediaFilesDAO() {
		return mediaFilesDAO;
	}

	public void setMediaFilesDAO(MediaFilesDAO mediaFilesDAO) {
		this.mediaFilesDAO = mediaFilesDAO;
	}

	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		boolean noerror = true;
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		if (isMultipart) {
			ServletFileUpload upload = new ServletFileUpload();
			try {
				FileItemIterator iter = upload.getItemIterator(request);
				while (iter.hasNext()) {
					FileItemStream item = iter.next();
					String name = item.getFieldName();
					System.out.println("item.getFieldName()==>" + name);
					InputStream stream = item.openStream();
					if (item.isFormField()) {
						String temp = Streams.asString(stream);
						System.out.println(name+": "+temp);
						extension = FileUtils.getExtension(temp);
					}else {
						if (name.equals("file")) {
							try {
								savedfilePath = createFile(item.getName(),
										stream, name);
							} catch (Exception e) {
								noerror = false;
								String errMsg = e.getMessage();
								PrintWriter writer = response.getWriter();
								writer.write(STANDARERRORRESPONSE+errMsg);
								writer.close();
								return null;
							}
							
							
						}
					}
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
		 PrintWriter writer = response.getWriter();
	        writer.write(savedfilePath);
		return null;
	}
	private String getType(String extn){
		String type = VIDEO;
		if(".jpg".equalsIgnoreCase(extn)){
			type = IMAGE;
		}if(".jpeg".equalsIgnoreCase(extn)){
			type = IMAGE;
		}if(".png".equalsIgnoreCase(extn)){
			type = IMAGE;
		}if(".gif".equalsIgnoreCase(extn)){
			type = IMAGE;
		}if(extn.endsWith(".jpg")||extn.endsWith(".JPG")){
			type = IMAGE;
		}if(extn.endsWith(".jpeg")||extn.endsWith(".JPEG")){
			type = IMAGE;
		}if(extn.endsWith(".png")||extn.endsWith(".PNG")){
			type = IMAGE;
		}if(extn.endsWith(".gif")||extn.endsWith(".GIF")){
			type = IMAGE;
		}
		return type;
	}
	private String createFile(String filename, InputStream stream,
			String fieldname) throws Exception{
		//String extn = FileUtils.getExtension(filename);
		String extn = "";
		if(!"".equalsIgnoreCase(extension)){
			extn = extension;
		}else{
			extn = FileUtils.getExtension(filename);
		}
		String filetype = getType(extn);
		String filePath = "";
		String rand_1 = StrUtil.getUniqueId();
		String rand_2 = new Long(System.currentTimeMillis()).toString();
		
		String retStr = rand_1+rand_2;
		if (filename != null) {
			if (!"".equalsIgnoreCase(filename)) {
				
					filePath = BASESBSTORAGEPATH+ retStr;
					System.out.println("filePath: "+filePath);
				
			}
		}
		if (!"".equalsIgnoreCase(filePath)) {
			filename = retStr+extn;
			//Create the file
			FileUtils.writeToFile(stream, filePath, filename);
			
			//Create the DB entry
			MediaFiles mediaFiles = new MediaFiles();
			String fullfilepath = filePath+"/"+filename;
			System.out.println("fullfilepath: "+fullfilepath);
			mediaFiles.setFilepath(fullfilepath);
			mediaFiles.setType(filetype);
			mediaFiles.setUniqueid(retStr);
			String mediaurl = uploadedimageurlprefix
			+ "getimage.htm?mediaid=" + retStr;
			if(IMAGE.equalsIgnoreCase(filetype)){
				mediaFiles.setMediaurl(mediaurl);
				mediaFiles.setVideourl("");
			}
			if(VIDEO.equalsIgnoreCase(filetype)){
				mediaFiles.setMediaurl("");
				mediaFiles.setVideourl(mediaurl);
			}
			
			mediaFilesDAO.addOrUpdateRecord(mediaFiles);
		}
		if (!"".equalsIgnoreCase(filePath)) {
			return retStr;
		} else {
			return "";
		}

	}
	
	
}
