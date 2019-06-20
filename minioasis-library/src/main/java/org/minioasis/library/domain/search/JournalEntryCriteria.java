package org.minioasis.library.domain.search;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

public class JournalEntryCriteria implements Serializable {

	private static final long serialVersionUID = 8952561181377909912L;
	
	private String description;
	private String createdBy;
	private String updatedBy;
    private LocalDate txnDateFrom;
    private LocalDate txnDateTo;
    private BigDecimal fromTotal;
    private BigDecimal toTotal;
    
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public String getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
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
	public BigDecimal getFromTotal() {
		return fromTotal;
	}
	public void setFromTotal(BigDecimal fromTotal) {
		this.fromTotal = fromTotal;
	}
	public BigDecimal getToTotal() {
		return toTotal;
	}
	public void setToTotal(BigDecimal toTotal) {
		this.toTotal = toTotal;
	}
    
}
