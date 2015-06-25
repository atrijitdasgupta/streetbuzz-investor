/**
 * 
 */
package com.amazon.advertising.api.sample;

import com.crowd.streetbuzzalgo.constants.SystemConstants;

/**
 * @author Atrijit
 *
 */
public class AWSIFrameGrabber implements SystemConstants{
	private static final String ENDPOINT = "ecs.amazonaws.com";
	/**
	 * 
	 */
	public AWSIFrameGrabber() {
		// TODO Auto-generated constructor stub
	}
	
	public void grab(String itemId){
		 SignedRequestsHelper helper;
	        try {
	            helper = SignedRequestsHelper.getInstance(ENDPOINT, amazonkeyid, amazonsecretaccesskey);
	        } catch (Exception e) {
	            e.printStackTrace();
	            return;
	        }
	      String requesturl = "http://webservices.amazon.com/onca/xml?Service=AWSECommerceService&" +
	      		"AWSAccessKeyId="+amazonkeyid+"&Operation=ItemLookup&ItemId="+itemId+
	      		"&ResponseGroup=Reviews&TruncateReviewsAt='256'&IncludeReviewsSummary='False'" +
	      		"&Version=2015-05-23&&Timestamp=2015-05-23T20:21:00Z&&Signature=[Request Signature]";
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
