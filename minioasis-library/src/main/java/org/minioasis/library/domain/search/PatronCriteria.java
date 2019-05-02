package org.minioasis.library.domain.search;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.NotNull;

import org.minioasis.library.domain.YesNo;

public class PatronCriteria {

	private String keyword;
	private String cardKey;
	private String note;
	private LocalDateTime createdFrom;
	private LocalDateTime createdTo;
	private LocalDate startDateFrom;
	private LocalDate startDateTo;
	private LocalDate endDateFrom;
	private LocalDate endDateTo;
	private Set<YesNo> actives = new HashSet<YesNo>();
	private Set<Long> patronTypes = new HashSet<Long>();
	private Set<Long> groups = new HashSet<Long>();
	@NotNull
	private Long[] ids = null;
	@NotNull
	private Long id = null;
	
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public String getCardKey() {
		return cardKey;
	}
	public void setCardKey(String cardKey) {
		this.cardKey = cardKey;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public LocalDateTime getCreatedFrom() {
		return createdFrom;
	}
	public void setCreatedFrom(LocalDateTime createdFrom) {
		this.createdFrom = createdFrom;
	}
	public LocalDateTime getCreatedTo() {
		return createdTo;
	}
	public void setCreatedTo(LocalDateTime createdTo) {
		this.createdTo = createdTo;
	}
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
	public Long[] getIds() {
		return ids;
	}
	public void setIds(Long[] ids) {
		this.ids = ids;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
}
