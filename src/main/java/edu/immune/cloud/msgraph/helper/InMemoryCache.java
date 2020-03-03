package edu.immune.cloud.msgraph.helper;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import edu.immune.cloud.msgraph.model.IdentityInfomation;

/**
 * Cache class to store information received from Microsoft Graph.<br> 
 * *** It stores *** <br> 
 * 1. 'code' received in response to authorize call <br>
 * 2. 'access_token' received in response to token call <br>
 * 3. 'refresh_token' received in response to token call. This is used to re-query for an access_token once they are invalidated <br><br>
 * This is an InMemory Cache and is short lived, information is only stored till the application is running.<br>
 * @author Lalit Mehra
 *
 */
@Component
public class InMemoryCache {
	
	private Map<String, IdentityInfomation> cache;
	
	public InMemoryCache() {
		this.cache = new HashMap<String, IdentityInfomation>();
	}

	public void setCode(String key, String code) {
		IdentityInfomation obj = get(key);
		if(null == obj) {
			obj = new IdentityInfomation();
			obj.setCode(code);
			cache.put(key, obj);
		} else {
			obj.setCode(code);
		}
	}
	
	public void add(String key, IdentityInfomation value) {
		cache.put(key, value);
	}
	
	public IdentityInfomation get(String key) {
		return cache.get(key);
	}
}
