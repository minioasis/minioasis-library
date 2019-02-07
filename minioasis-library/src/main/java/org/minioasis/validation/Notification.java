package org.minioasis.validation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/*
 * This is a Notification Pattern (Martin Flower)
 * "Replacing Throwing Exceptions with Notification in Validations"
 */
public class Notification implements Serializable {

	private static final long serialVersionUID = 8041565169621087219L;

	private List<Error> errors = new ArrayList<Error>();

	public Notification() {
	}

	public Notification(Error error){
		addError(error);
	}
	
	public Notification(ErrorCode error){
		addError(error);
	}
	
	public void addError(String error) {
		this.errors.add(new Error(error, null));
	}
	
	public void addError(ErrorCode error) {
		this.errors.add(new Error(error.getCode(), null));
	}
	
	public void addError(Error error) {
		this.errors.add(error);
	}
	
	public void addError(String message, Exception e) {
		this.errors.add(new Error(message, e));
	}
	
	public Error getFirstError() {
		return this.errors.get(0);
	}

	public boolean hasErrors() {
		return !this.errors.isEmpty();
	}

	public String getMessage() {
		return this.errors.stream()
				.map(e -> e.message)
				.collect(Collectors.joining(", "));
	}
	
	public List<Error> getAllErrors() {
		return this.errors;
	}
	
	public List<String> getAllMessages() {
		
		List<String> list = new ArrayList<String>();
		for(Error e : errors){
			list.add(e.message);
		}
		return list;
	}
	
	private static class Error {
		String message;
		@SuppressWarnings("unused")
		Exception cause;

		private Error(String message, Exception cause) {
			this.message = message;
			this.cause = cause;
		}
	}

}
