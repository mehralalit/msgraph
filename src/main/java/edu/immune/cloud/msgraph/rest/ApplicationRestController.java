package edu.immune.cloud.msgraph.rest;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.immune.cloud.msgraph.exception.ApiException;
import edu.immune.cloud.msgraph.helper.InMemoryCache;
import edu.immune.cloud.msgraph.helper.MicrosoftGraphEndPoint;
import edu.immune.cloud.msgraph.model.IdentityInfomation;
import edu.immune.cloud.msgraph.model.TokenApiResponse;

/**
 * End point for all the rest calls
 * 
 * @author Lalit Mehra
 * 
 */
@RestController
public class ApplicationRestController {

	@Autowired
	private Properties properties;

	@Autowired
	private InMemoryCache cache;

	@Autowired
	private MicrosoftGraphEndPoint msEndPoint;

	private static ObjectMapper mapper = new ObjectMapper();

	/**
	 * maps json response to java object
	 * 
	 * @param response
	 * @return
	 */
	private TokenApiResponse mapResponseToObject(String response) {
		TokenApiResponse obj = null;
		try {
			obj = mapper.readValue(response, TokenApiResponse.class);
		} catch (JsonProcessingException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}

		return obj;
	}

	/**
	 * Fetches access token passing in the 'code' value received in response to the authorize call 
	 * 
	 * @param user
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping("token")
	public String token(@RequestParam(name = "user", required = true) String user) throws UnsupportedEncodingException {
		IdentityInfomation identity = cache.get(user);
		String code = identity.getCode();

		HttpPost httppost = new HttpPost(msEndPoint.getTokenUrl());
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("client_id", properties.getProperty("client.id")));
		list.add(new BasicNameValuePair("scope", "User.Read Calendars.Read Calendars.ReadWrite"));
		list.add(new BasicNameValuePair("code", code));
		list.add(new BasicNameValuePair("client_secret", properties.getProperty("client.secret")));
		list.add(new BasicNameValuePair("redirect_uri", properties.getProperty("redirect_uri")));
		list.add(new BasicNameValuePair("grant_type", "authorization_code"));

		HttpEntity entity = new UrlEncodedFormEntity(list);
		httppost.setEntity(entity);

		String result = StringUtils.EMPTY;
		try (CloseableHttpClient httpClient = HttpClients.createDefault();
				CloseableHttpResponse response = httpClient.execute(httppost)) {

			result = EntityUtils.toString(response.getEntity());
		} catch (IOException e) {
			result = e.getMessage();
			e.printStackTrace();
		}

		TokenApiResponse response = mapResponseToObject(result);
		identity.setAccessToken(response.getAccess_token());
		identity.setRefreshToken(response.getRefresh_token());

		return response.getAccess_token();
	}
	
	/**
	 * Fetches a new access token if the existing token is invalidated
	 * 
	 * @param user
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping("refresh")
	public String refresh(@RequestParam(name = "user", required = true) String user) throws UnsupportedEncodingException {
		IdentityInfomation identity = cache.get(user);
		String refreshToken = identity.getRefreshToken();

		HttpPost httppost = new HttpPost(msEndPoint.getTokenUrl());
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("client_id", properties.getProperty("client.id")));
		list.add(new BasicNameValuePair("scope", "User.Read Calendars.Read Calendars.ReadWrite"));
		list.add(new BasicNameValuePair("refresh_token", refreshToken));
		list.add(new BasicNameValuePair("client_secret", properties.getProperty("client.secret")));
		list.add(new BasicNameValuePair("redirect_uri", properties.getProperty("redirect_uri")));
		list.add(new BasicNameValuePair("grant_type", "refresh_token"));

		HttpEntity entity = new UrlEncodedFormEntity(list);
		httppost.setEntity(entity);

		String result = StringUtils.EMPTY;
		try (CloseableHttpClient httpClient = HttpClients.createDefault();
				CloseableHttpResponse response = httpClient.execute(httppost)) {

			result = EntityUtils.toString(response.getEntity());
		} catch (IOException e) {
			result = e.getMessage();
			e.printStackTrace();
		}

		TokenApiResponse response = mapResponseToObject(result);
		identity.setAccessToken(response.getAccess_token());
		identity.setRefreshToken(response.getRefresh_token());

		return response.getAccess_token();
	}
	
	/**
	 * Fetches the permitted information from Aure AD via MS Graph API.<br>
	 * 
	 * @param user
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping("fetch_events")
	public String fetchEvents(@RequestParam(name = "user", required = true) String user) throws UnsupportedEncodingException {
		IdentityInfomation identity = cache.get(user);
		String accessToken = identity.getAccessToken();
		
		HttpGet httpget = new HttpGet(msEndPoint.getCalendarUrl());
		
		Header header = new BasicHeader("Authorization", accessToken);
		httpget.setHeader(header);
		
		String result = StringUtils.EMPTY;
		try (CloseableHttpClient httpClient = HttpClients.createDefault();
				CloseableHttpResponse response = httpClient.execute(httpget)) {

			result = EntityUtils.toString(response.getEntity());
			
			if(result.contains("InvalidAuthenticationToken")) {
				throw new ApiException("InvalidAuthenticationToken");
			}
		} catch (IOException e) {
			result = e.getMessage();
			e.printStackTrace();
		}
		
		return result;
	}

	/**
	 * Fetches the permitted information from Aure AD via MS Graph API.<br>
	 * If the access token is invalidated a new token is fetched using the refresh() call<br>
	 * Makes call to data() and refresh()
	 * 
	 * @param user
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping("fetch")
	public String fetch(@RequestParam(name = "user", required = true) String user) throws UnsupportedEncodingException {
		
		String response = null;
		try {
			response = fetchEvents(user);
		} catch (ApiException e) {
			refresh(user);
			response = fetchEvents(user);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return response;
	}
	
	
	
}
