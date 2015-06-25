/**
 * Resource.java
 * 
 * @contact    http://www.textalytics.com (http://www.daedalus.es)
 * @copyright  Copyright (c) 2014, DAEDALUS S.A. All rights reserved.
 */
package es.daedalus.textalytics.sempub.domain;

import es.daedalus.textalytics.sempub.TextalyticsClientException;

/**
 * Resource is the interface that all user resources must implement
 * in the User Resources Management service.
 * 
 * @author Textalytics
 * @version 1.0 02/2014
 */
public interface Resource {

	String toJsonString() throws TextalyticsClientException;
	
	String getObjectIdentifier();
}
