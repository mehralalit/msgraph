package edu.immune.cloud.msgraph.model;

import edu.immune.cloud.msgraph.helper.InMemoryCache;

/**
 * Forms the Value part of the InMemory<K,V> cache
 * 
 * @author Lalit Mehra
 * @see InMemoryCache
 *
 */
public class IdentityInfomation {

	/**
	 * code received in response to authorize call
	 */
	private String code;
	private String accessToken;
	private String refreshToken;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("IdentityInfomation [code=");
		builder.append(code);
		builder.append(", accessToken=");
		builder.append(accessToken);
		builder.append(", refreshToken=");
		builder.append(refreshToken);
		builder.append("]");
		return builder.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((code == null) ? 0 : code.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		IdentityInfomation other = (IdentityInfomation) obj;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		return true;
	}

}
