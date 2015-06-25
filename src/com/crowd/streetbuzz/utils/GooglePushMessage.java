/**
 * 
 */
package com.crowd.streetbuzz.utils;

import java.io.IOException;

import com.crowd.streetbuzz.json.JsonGetCard;
import com.crowd.streetbuzzalgo.constants.SystemConstants;
import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;
import com.google.gson.Gson;

/**
 * @author Atrijit
 *
 */
public class GooglePushMessage implements SystemConstants{
	
	/**
	 * 
	 */
	public GooglePushMessage() {
		// TODO Auto-generated constructor stub
	}
	
	public static void sendPushMessage(String deviceregid, String userMessage){
		Sender sender = new Sender(GCM_SERVER_KEY);
		Message message = new Message.Builder().timeToLive(30)
		.delayWhileIdle(true).addData(MESSAGE_KEY, "StreetBuzz: New Notification!").addData(DATA_KEY, userMessage).build();
		/*Message message = new Message.Builder().timeToLive(30)
		.delayWhileIdle(true).addData(APPNAME_KEY, "StreetBuzz").addData(MESSAGE_KEY, "New Notification!").addData(DATA_KEY, userMessage).build();*/
		Result result = null;
		String resultMsg = "";
		try {
			result = sender.send(message, deviceregid, 1);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(result!=null){
			resultMsg = result.toString();
		}
		System.out.println("pushStatus: "+resultMsg);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String deviceregid = "APA91bF8iQEHIZaNLEO0Gm5lEiGc4N7ith0BtQDmAg3ZkgpJ59GCRsL5gCIyd7CL6RJ9BCAGhkU8Otc3iU8i_9RtGIvac2nl5LIVtSYBayCRZvyRz1SJqbcZ8U1kz1UXUwjWqXvf37Cjei9jCkICBo2qpanC4KRrgQ";
		JsonGetCard jgc = new JsonGetCard();
		jgc.setCardid("745");
		jgc.setUniqueid("b65d7f5b-76b5-499c-8fe3-dbf01d829526-1428994372693");
		//"id":61,"uniqueid":"65d76619-45c8-4b6d-8e6e-308985662c09-1420456687217"//"id":8,"uniqueid":"81a8c24f-1912-4fb4-9cec-068a2513673e-1420457026686"
		Gson gson = new Gson();
		String userMessage = gson.toJson(jgc);
		System.out.println(userMessage);
		sendPushMessage(deviceregid, userMessage);
	}

}
