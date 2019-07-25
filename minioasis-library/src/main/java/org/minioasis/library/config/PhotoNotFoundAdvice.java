package org.minioasis.library.config;

import org.minioasis.library.exception.PhotoNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ControllerAdvice
public class PhotoNotFoundAdvice {

	@ResponseBody
	@ExceptionHandler(PhotoNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	String photoNotFoundHandler(PhotoNotFoundException ex) {
		return ex.getMessage();
	}
}
