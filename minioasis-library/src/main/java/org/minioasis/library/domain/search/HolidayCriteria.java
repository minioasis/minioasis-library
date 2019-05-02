package org.minioasis.library.domain.search;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class HolidayCriteria implements Serializable {

	private static final long serialVersionUID = 8142411660001005134L;
	
	private String name;
	private LocalDate StartDateFrom;
	private LocalDate StartDateTo;
	private LocalDate endDateFrom;
	private LocalDate endDateTo;
	private Set<Boolean> fines = new HashSet<Boolean>();
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public LocalDate getStartDateFrom() {
		return StartDateFrom;
	}
	public void setStartDateFrom(LocalDate startDateFrom) {
		StartDateFrom = startDateFrom;
	}
	public LocalDate getStartDateTo() {
		return StartDateTo;
	}
	public void setStartDateTo(LocalDate startDateTo) {
		StartDateTo = startDateTo;
	}
	public LocalDate getEndDateFrom() {
		return endDateFrom;
	}
	public void setEndDateFrom(LocalDate endDateFrom) {
		this.endDateFrom = endDateFrom;
	}
	public LocalDate getEndDateTo() {
		return endDateTo;
	}
	public void setEndDateTo(LocalDate endDateTo) {
		this.endDateTo = endDateTo;
	}
	public Set<Boolean> getFines() {
		return fines;
	}
	public void setFines(Set<Boolean> fines) {
		this.fines = fines;
	}
	
}
