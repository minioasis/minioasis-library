package org.minioasis.library.domain.search;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.NotNull;

import org.minioasis.library.domain.YesNo;

public class PatronCriteria {

	private String keyword;
	private String cardKey;
	private String note;
	private Date createdFrom;
	private Date createdTo;
	private Date startDateFrom;
	private Date startDateTo;
	private Date endDateFrom;
	private Date endDateTo;
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
	public Date getCreatedFrom() {
		return createdFrom;
	}
	public void setCreatedFrom(Date createdFrom) {
		this.createdFrom = createdFrom;
	}
	public Date getCreatedTo() {
		return createdTo;
	}
	public void setCreatedTo(Date createdTo) {
		this.createdTo = createdTo;
	}
	public Date getStartDateFrom() {
		return startDateFrom;
	}
	public void setStartDateFrom(Date startDateFrom) {
		this.startDateFrom = startDateFrom;
	}
	public Date getStartDateTo() {
		return startDateTo;
	}
	public void setStartDateTo(Date startDateTo) {
		this.startDateTo = startDateTo;
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
