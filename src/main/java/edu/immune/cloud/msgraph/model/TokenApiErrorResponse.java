package edu.immune.cloud.msgraph.model;

import java.util.List;

/**
 * Java Object equivalent of the json error response of graph token api
 * @author Lalit Mehra
 *
 */
public class TokenApiErrorResponse {

	private String error;
	private String error_description;
	private List<Integer> error_codes;
	private String timestamp;
	private String trace_id;
	private String correlation_id;
	private String error_uri;

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getError_description() {
		return error_description;
	}

	public void setError_description(String error_description) {
		this.error_description = error_description;
	}

	public List<Integer> getError_codes() {
		return error_codes;
	}

	public void setError_codes(List<Integer> error_codes) {
		this.error_codes = error_codes;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getTrace_id() {
		return trace_id;
	}

	public void setTrace_id(String trace_id) {
		this.trace_id = trace_id;
	}

	public String getCorrelation_id() {
		return correlation_id;
	}

	public void setCorrelation_id(String correlation_id) {
		this.correlation_id = correlation_id;
	}

	public String getError_uri() {
		return error_uri;
	}

	public void setError_uri(String error_uri) {
		this.error_uri = error_uri;
	}

}
