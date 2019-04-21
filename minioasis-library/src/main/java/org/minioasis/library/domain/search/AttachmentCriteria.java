package org.minioasis.library.domain.search;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.minioasis.library.domain.AttachmentState;
import org.minioasis.library.domain.YesNo;

public class AttachmentCriteria implements Serializable {

	private static final long serialVersionUID = -6905279065623607531L;

	private String itemBarcode;
	private String keyword;
	private Date firstCheckinFrom;
	private Date firstCheckinTo;
	private Date lastCheckinFrom;
	private Date lastCheckinTo;
	private Date expiredFrom;
	private Date expiredTo;
	
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
	public Date getFirstCheckinFrom() {
		return firstCheckinFrom;
	}
	public void setFirstCheckinFrom(Date firstCheckinFrom) {
		this.firstCheckinFrom = firstCheckinFrom;
	}
	public Date getFirstCheckinTo() {
		return firstCheckinTo;
	}
	public void setFirstCheckinTo(Date firstCheckinTo) {
		this.firstCheckinTo = firstCheckinTo;
	}
	public Date getLastCheckinFrom() {
		return lastCheckinFrom;
	}
	public void setLastCheckinFrom(Date lastCheckinFrom) {
		this.lastCheckinFrom = lastCheckinFrom;
	}
	public Date getLastCheckinTo() {
		return lastCheckinTo;
	}
	public void setLastCheckinTo(Date lastCheckinTo) {
		this.lastCheckinTo = lastCheckinTo;
	}
	public Date getExpiredFrom() {
		return expiredFrom;
	}
	public void setExpiredFrom(Date expiredFrom) {
		this.expiredFrom = expiredFrom;
	}
	public Date getExpiredTo() {
		return expiredTo;
	}
	public void setExpiredTo(Date expiredTo) {
		this.expiredTo = expiredTo;
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

