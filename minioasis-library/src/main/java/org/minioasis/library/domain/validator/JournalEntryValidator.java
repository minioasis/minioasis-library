package org.minioasis.library.domain.validator;

import java.time.LocalDate;

import org.minioasis.library.domain.JournalEntry;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class JournalEntryValidator implements Validator {
	
	@SuppressWarnings("rawtypes")
	public boolean supports(Class clazz) {
		return JournalEntry.class.isAssignableFrom(clazz);
	}

	// Validate the compulsory data
	public void validate(Object obj, Errors e) {
		
		JournalEntry je = (JournalEntry)obj;

		LocalDate txnDate = je.getTxnDate();
		String desp = je.getDescription();
		boolean balance = je.isBalance();
		
		if(txnDate == null) {
			e.rejectValue("je.txnDate", "notnull");
		}
		if(desp == null || desp.equals("")) {
			e.rejectValue("je.description", "notnull");
		}
		if(!balance) {
			e.rejectValue("je.credit", "not.balance");
		}

	}

}
