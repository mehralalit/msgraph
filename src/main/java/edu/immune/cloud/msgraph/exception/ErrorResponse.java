package edu.immune.cloud.msgraph.exception;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * @author Lalit Mehra
 *
 * @param <T>
 * @see CustomExceptionHandler
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
public class ErrorResponse<T> {
	private List<T> errors = new ArrayList<>(1);

	public ErrorResponse(List<T> errors) {
		this.errors = errors;
	}

	public List<T> getErrors() {
		return errors;
	}
}