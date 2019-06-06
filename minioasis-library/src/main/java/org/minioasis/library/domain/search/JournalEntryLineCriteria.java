package org.minioasis.library.domain.search;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import org.minioasis.library.domain.AccountType;

public class JournalEntryLineCriteria implements Serializable {

	private static final long serialVersionUID = 390473093715431619L;

	private String description;
    private LocalDate txnDateFrom;
    private LocalDate txnDateTo;
    private BigDecimal debit;
    private BigDecimal credit;
    private String code1;
    private Set<AccountType> accountTypes = new HashSet<AccountType>();
    private String code2;
    private String recordBy;
    private String updatedBy;
    
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public LocalDate getTxnDateFrom() {
		return txnDateFrom;
	}
	public void setTxnDateFrom(LocalDate txnDateFrom) {
		this.txnDateFrom = txnDateFrom;
	}
	public LocalDate getTxnDateTo() {
		return txnDateTo;
	}
	public void setTxnDateTo(LocalDate txnDateTo) {
		this.txnDateTo = txnDateTo;
	}
	public BigDecimal getDebit() {
		return debit;
	}
	public void setDebit(BigDecimal debit) {
		this.debit = debit;
	}
	public BigDecimal getCredit() {
		return credit;
	}
	public void setCredit(BigDecimal credit) {
		this.credit = credit;
	}
	public String getCode1() {
		return code1;
	}
	public void setCode1(String code1) {
		this.code1 = code1;
	}
	public Set<AccountType> getAccountTypes() {
		return accountTypes;
	}
	public void setAccountTypes(Set<AccountType> accountTypes) {
		this.accountTypes = accountTypes;
	}
	public String getCode2() {
		return code2;
	}
	public void setCode2(String code2) {
		this.code2 = code2;
	}
	public String getRecordBy() {
		return recordBy;
	}
	public void setRecordBy(String recordBy) {
		this.recordBy = recordBy;
	}
	public String getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}
    
}
