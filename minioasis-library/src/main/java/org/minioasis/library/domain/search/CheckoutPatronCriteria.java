package org.minioasis.library.domain.search;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import org.minioasis.library.domain.CheckoutState;
import org.minioasis.library.domain.YesNo;

public class CheckoutPatronCriteria {

	private LocalDate startDateFrom;
	private LocalDate startDateTo;
	private LocalDate endDateFrom;
	private LocalDate endDateTo;
	private Set<YesNo> actives = new HashSet<YesNo>();
	private Set<Long> patronTypes = new HashSet<Long>();
	private Set<Long> groups = new HashSet<Long>();
	private Set<CheckoutState> states = new HashSet<CheckoutState>();
	
	public LocalDate getStartDateFrom() {
		return startDateFrom;
	}
	public void setStartDateFrom(LocalDate startDateFrom) {
		this.startDateFrom = startDateFrom;
	}
	public LocalDate getStartDateTo() {
		return startDateTo;
	}
	public void setStartDateTo(LocalDate startDateTo) {
		this.startDateTo = startDateTo;
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
	public Set<YesNo> getActives() {
		return actives;
	}
	public void setActives(Set<YesNo> actives) {
		this.actives = actives;
	}
	public Set<Long> getPatronTypes() {
		return patronTypes;
	}
	public void setPatronTypes(Set<Long> patronTypes) {
		this.patronTypes = patronTypes;
	}
	public Set<Long> getGroups() {
		return groups;
	}
	public void setGroups(Set<Long> groups) {
		this.groups = groups;
	}
	public Set<CheckoutState> getStates() {
		return states;
	}
	public void setStates(Set<CheckoutState> states) {
		this.states = states;
	}
	
}
