package org.minioasis.library.exception;

import java.util.ArrayList;
import java.util.List;

import org.minioasis.validation.ErrorCode;

public class LibraryException extends RuntimeException {

	private static final long serialVersionUID = -7048848983336971613L;

	private List<String> errors = new ArrayList<String>();
	
	public LibraryException() {}

    public LibraryException(String message) {
        super(message);
    }
 
    public LibraryException(ErrorCode errorCode){
    	this.errors.add(errorCode.getCode());
    }
    public LibraryException(List<String> messages) {
        this.errors.addAll(messages);
    }
    
    public LibraryException(String message, Throwable cause) {
    	super(message,cause);
    }

	public List<String> getAllErrors() {
		return errors;
	}
	
}
