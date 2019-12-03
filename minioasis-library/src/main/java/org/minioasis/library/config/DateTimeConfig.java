package org.minioasis.library.config;

import java.time.format.DateTimeFormatter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar;
import org.springframework.format.number.NumberFormatAnnotationFormatterFactory;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.format.support.FormattingConversionService;

@Configuration
public class DateTimeConfig {
 
    @Bean
    public FormattingConversionService conversionService() {
    	
        DefaultFormattingConversionService conversionService = new DefaultFormattingConversionService(false);
        
     // Ensure @NumberFormat is still supported
        conversionService.addFormatterForFieldAnnotation(new NumberFormatAnnotationFormatterFactory());
 
        DateTimeFormatterRegistrar registrar = new DateTimeFormatterRegistrar();

        registrar.setDateFormatter(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        registrar.setDateTimeFormatter(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        registrar.registerFormatters(conversionService);
 
        return conversionService;
    }
}
