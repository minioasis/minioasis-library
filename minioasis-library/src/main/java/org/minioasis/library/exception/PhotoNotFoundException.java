package org.minioasis.library.exception;

public class PhotoNotFoundException extends RuntimeException {

	private static final long serialVersionUID = -5783944524595903629L;

	PhotoNotFoundException(String id) {
	    super("Could not find photo " + id);
	  }
	
}
