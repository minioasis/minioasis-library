package org.minioasis.library.domain.search;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.minioasis.library.domain.AttachmentState;
import org.minioasis.library.domain.YesNo;

public class AttachmentCriteria implements Serializable {

	private static final long serialVersionUID = -6905279065623607531L;

	private String itemBarcode;
	private String keyword;
	private LocalDate firstCheckinFrom;
	private LocalDate firstCheckinTo;
	private LocalDate lastCheckinFrom;
	private LocalDate lastCheckinTo;
	
	Set<YesNo> borrowables = new HashSet<YesNo>();
	Set<AttachmentState> states = new HashSet<AttachmentState>();

	public String getItemBarcode() {
		return itemBarcode;
	}
	public void setItemBarcode(String itemBarcode) {
		this.itemBarcode = itemBarcode;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public LocalDate getFirstCheckinFrom() {
		return firstCheckinFrom;
	}
	public void setFirstCheckinFrom(LocalDate firstCheckinFrom) {
		this.firstCheckinFrom = firstCheckinFrom;
	}
	public LocalDate getFirstCheckinTo() {
		return firstCheckinTo;
	}
	public void setFirstCheckinTo(LocalDate firstCheckinTo) {
		this.firstCheckinTo = firstCheckinTo;
	}
	public LocalDate getLastCheckinFrom() {
		return lastCheckinFrom;
	}
	public void setLastCheckinFrom(LocalDate lastCheckinFrom) {
		this.lastCheckinFrom = lastCheckinFrom;
	}
	public LocalDate getLastCheckinTo() {
		return lastCheckinTo;
	}
	public void setLastCheckinTo(LocalDate lastCheckinTo) {
		this.lastCheckinTo = lastCheckinTo;
	}
	public Set<YesNo> getBorrowables() {
		return borrowables;
	}
	public void setBorrowables(Set<YesNo> borrowables) {
		this.borrowables = borrowables;
	}
	public Set<AttachmentState> getStates() {
		return states;
	}
	public void setStates(Set<AttachmentState> states) {
		this.states = states;
	}
		
}

