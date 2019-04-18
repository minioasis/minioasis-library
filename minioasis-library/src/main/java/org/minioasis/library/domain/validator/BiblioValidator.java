package org.minioasis.library.domain.validator;

import org.minioasis.library.domain.Biblio;
import org.minioasis.library.domain.Publisher;
import org.minioasis.library.domain.Series;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class BiblioValidator implements Validator {

	@SuppressWarnings("rawtypes")
	public boolean supports(Class clazz) {
		return Biblio.class.isAssignableFrom(clazz);
	}

	// Validate the compulsory data
	public void validate(Object obj, Errors e) {

		Biblio biblio = (Biblio) obj;
		Publisher p = biblio.getPublisher();
		Series s = biblio.getSeries();

		// publisher name
		if(p != null) {
			String pname = p.getName();
			if (pname != null && !pname.isEmpty() && pname.length() > 128) {
				e.rejectValue("publisher.name", "", null, "< 129");
			}
		}

		// series name
		if(s != null) {
			String sname = s.getName();
			if (sname != null && !sname.isEmpty() && sname.length() > 128) {
				e.rejectValue("series.name", "", null, "< 129");
			}
		}
		
	}

}
