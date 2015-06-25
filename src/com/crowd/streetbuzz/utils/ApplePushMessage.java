/**
 * 
 */
package com.crowd.streetbuzz.utils;

import com.crowd.streetbuzzalgo.utils.StrUtil;
import com.notnoop.apns.APNS;
import com.notnoop.apns.ApnsService;

/**
 * @author Atrijit
 *
 */
public class ApplePushMessage {

	/**
	 * 
	 */
	public ApplePushMessage() {
		// TODO Auto-generated constructor stub
	}
	public static void sendPushMessage(String deviceregid, String userMessage){
		loadCert();
		push(deviceregid, userMessage);
	}
	public static void sendAPNPushMessage(String deviceregid, String userMessage){
		ApnsService service =
		    APNS.newService()
		    .withCert("C:/Mywork/StreetBuzz/applecert/SBPushDev.p12", "MyCertPassword")
		    .withSandboxDestination()
		    .build();
		String payload = APNS.newPayload().alertBody("New Notification").build();
		String token = userMessage;
		service.push(token, payload);
	}
	private static void loadCert(){
		System.out.println("Located APN Certificate at /home/streetbuzz/apncert/SBPushDev.p12");
	}
	private static void push(String deviceregid, String userMessage){
		System.out.println("pushStatus:" +StrUtil.getUniqueId());
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		sendPushMessage("","Hi");

	}

}
