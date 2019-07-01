package org.minioasis.library.domain.validator;

import java.math.BigDecimal;

import org.minioasis.library.domain.Account;
import org.minioasis.library.domain.JournalEntryLine;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class JournalEntryLineValidator implements Validator {

	@SuppressWarnings("rawtypes")
	public boolean supports(Class clazz) {
		return JournalEntryLine.class.isAssignableFrom(clazz);
	}

	// Validate the compulsory data
	public void validate(Object obj, Errors e) {
		
		JournalEntryLine line = (JournalEntryLine)obj;
		Account account = line.getAccount();
		String desp = line.getDescription();
		BigDecimal credit = line.getCredit();
		BigDecimal debit = line.getDebit();
		
		if(account == null) {
			e.rejectValue("line.account.code", "notnull");
		}
		if(desp == null || desp.equals("")) {
			e.rejectValue("line.description", "notnull");
		}
		if(debit.equals(BigDecimal.ZERO) && credit.equals(BigDecimal.ZERO)) {
			e.rejectValue("line.debit", "debit.credit.cannot.be.both.zero");
		}
		if(!(debit.equals(BigDecimal.ZERO) && credit.doubleValue() > 0) && 
				!(debit.doubleValue() > 0 && credit.equals(BigDecimal.ZERO))) {
			e.rejectValue("line.debit", "at.least.one.must.be.zero");
		}
		if(debit.doubleValue() < 0 || credit.doubleValue() < 0) {
			e.rejectValue("line.debit", "not.negative");
		}

	}
}
