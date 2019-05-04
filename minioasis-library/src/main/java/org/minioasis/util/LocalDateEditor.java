package org.minioasis.util;

import java.beans.PropertyEditorSupport;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LocalDateEditor extends PropertyEditorSupport {
	
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public void setAsText(String text) throws IllegalArgumentException{
        setValue(LocalDate.parse(text, DATE_FORMATTER));
    }

    @Override
    public String getAsText() throws IllegalArgumentException {
        LocalDate value = (LocalDate) getValue();
        if(value != null)
        	return value.format(DATE_FORMATTER);
        return "";
    }
}
