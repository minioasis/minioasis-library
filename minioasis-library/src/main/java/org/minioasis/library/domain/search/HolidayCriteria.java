package org.minioasis.library.domain.search;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class HolidayCriteria implements Serializable {

	private static final long serialVersionUID = 8142411660001005134L;
	
	private String name;
	private Date StartDateFrom;
	private Date StartDateTo;
	private Date endDateFrom;
	private Date endDateTo;
	private Set<Boolean> fines = new HashSet<Boolean>();
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getStartDateFrom() {
		return StartDateFrom;
	}
	public void setStartDateFrom(Date startDateFrom) {
		StartDateFrom = startDateFrom;
	}
	public Date getStartDateTo() {
		return StartDateTo;
	}
	public void setStartDateTo(Date startDateTo) {
		StartDateTo = startDateTo;
	}
	public Date getEndDateFrom() {
		return endDateFrom;
	}
	public void setEndDateFrom(Date endDateFrom) {
		this.endDateFrom = endDateFrom;
	}
	public Date getEndDateTo() {
		return endDateTo;
	}
	public void setEndDateTo(Date endDateTo) {
		this.endDateTo = endDateTo;
	}
	public Set<Boolean> getFines() {
		return fines;
	}
	public void setFines(Set<Boolean> fines) {
		this.fines = fines;
	}
	
}
