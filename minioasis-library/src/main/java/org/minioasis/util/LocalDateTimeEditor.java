package org.minioasis.util;

import java.beans.PropertyEditorSupport;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeEditor extends PropertyEditorSupport {

	private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public void setAsText(String text) throws IllegalArgumentException{
    	if(text != null && !text.equals("")) {
    		setValue(LocalDate.parse(text, DATETIME_FORMATTER));
    	}else {
    		setValue(null);
    	}  
    }

    @Override
    public String getAsText() throws IllegalArgumentException {
    	LocalDateTime value = (LocalDateTime) getValue();
        if(value != null)
        	return value.format(DATETIME_FORMATTER);
        return "";
    }
    
}
