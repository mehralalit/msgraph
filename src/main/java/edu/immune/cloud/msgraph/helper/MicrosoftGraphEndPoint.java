package edu.immune.cloud.msgraph.helper;

import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Provides Microsoft end-point specific information. It contains all the necessary URIs required for this application to run
 * @author Lalit Mehra
 *
 */
@Component
public class MicrosoftGraphEndPoint {

	@Autowired
	public Properties properties;

	private static final String COMMON = "common";
	private static final String LOGIN_DOMAIN = "https://login.microsoftonline.com/";
	private static final String GRAPH_DOMAIN = "https://graph.microsoft.com/v1.0/me/";
	private static final String OAUTH_VERSION = "/oauth2/v2.0";
	
	private static final String TENANT_ID_PROPKEY = "tenant.id";
	
	private String getTenant() {
		String tenantId = properties.getProperty(TENANT_ID_PROPKEY);
		tenantId = StringUtils.isBlank(tenantId)?COMMON:tenantId;
		
		return tenantId;
	}
	
	public String getAuthorizationUrl() {
		return LOGIN_DOMAIN + getTenant() + OAUTH_VERSION + "/authorize?";
	}
	
	public String getTokenUrl() {
		return LOGIN_DOMAIN + getTenant() + OAUTH_VERSION + "/token";
	}
	
	public String getCalendarUrl() {
		return GRAPH_DOMAIN + "calendar/events";
	}
}
