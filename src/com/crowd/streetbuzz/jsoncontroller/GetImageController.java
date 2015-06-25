/**
 * 
 */
package com.crowd.streetbuzz.jsoncontroller;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
public class GetImageController implements Controller, Constants{
private String retStr = "";
private MediaFilesDAO mediaFilesDAO;

public MediaFilesDAO getMediaFilesDAO() {
	return mediaFilesDAO;
}

public void setMediaFilesDAO(MediaFilesDAO mediaFilesDAO) {
	this.mediaFilesDAO = mediaFilesDAO;
}

public ModelAndView handleRequest(HttpServletRequest request,
		HttpServletResponse response) throws Exception {
	String mediaid = StrUtil.nonNull(request.getParameter("mediaid"));
	MediaFiles mf = new MediaFiles();
	if(!"".equalsIgnoreCase(mediaid)){
		mf = (MediaFiles)mediaFilesDAO.getObjectByUniqueId(mediaid);
	}
	String filepath = mf.getFilepath();
	String extn = FileUtils.getExtension(filepath);
	String clearExtn = FileUtils.getClearExtension(extn);
	String mimetype = "image/"+clearExtn;
	String filetype = mf.getType();
	if(VIDEO.equalsIgnoreCase(filetype)){
		mimetype = "video/"+clearExtn;
	}
	
	System.out.println("mimetype: "+mimetype);
	
	
	File file = new File(filepath);
	FileInputStream inf = new FileInputStream(file);
    byte b[] = new byte[(int)file.length()];
    response.setContentType((new StringBuilder()).append(mimetype).toString());
    response.setContentLength(b.length);
    OutputStream out = response.getOutputStream();
    inf.read(b);
    out.write(b);
    inf.close();
    out.close();
	
    return new ModelAndView("");
}
}
