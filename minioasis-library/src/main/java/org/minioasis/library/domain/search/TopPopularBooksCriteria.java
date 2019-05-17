package org.minioasis.library.domain.search;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import org.minioasis.library.domain.CheckoutState;
import org.minioasis.library.domain.YesNo;

public class TopPopularBooksCriteria {

	private LocalDate firstCheckinFrom;
	private LocalDate firstCheckinTo;
	private Set<YesNo> actives = new HashSet<YesNo>();
	private Set<Long> patronTypes = new HashSet<Long>();
	private Set<Long> groups = new HashSet<Long>();
	private Set<CheckoutState> states = new HashSet<CheckoutState>();
	
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
