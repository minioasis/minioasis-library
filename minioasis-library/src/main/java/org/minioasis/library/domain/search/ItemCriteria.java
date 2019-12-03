package org.minioasis.library.domain.search;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import org.minioasis.library.domain.YesNo;

public class ItemCriteria implements Serializable {
	
	private static final long serialVersionUID = 7017446345857953691L;
	
	private String keyword;
	private String biblio;
	private LocalDate firstCheckinFrom;
	private LocalDate firstCheckinTo;
	private LocalDate lastCheckinFrom;
	private LocalDate lastCheckinTo;
	private LocalDate expiredFrom;
	private LocalDate expiredTo;

	private Set<Long> itemStatuz = new HashSet<Long>();
	private Set<Long> locations = new HashSet<Long>();
	private Set<String> itemStates =  new HashSet<String>();
	private Set<YesNo> actives = new HashSet<YesNo>();
	private Set<YesNo> checkeds = new HashSet<YesNo>();
	
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
	public LocalDate getExpiredFrom() {
		return expiredFrom;
	}
	public void setExpiredFrom(LocalDate expiredFrom) {
		this.expiredFrom = expiredFrom;
	}
	public LocalDate getExpiredTo() {
		return expiredTo;
	}
	public void setExpiredTo(LocalDate expiredTo) {
		this.expiredTo = expiredTo;
	}
	public String getBiblio() {
		return biblio;
	}
	public void setBiblio(String biblio) {
		this.biblio = biblio;
	}
	public Set<Long> getItemStatuz() {
		return itemStatuz;
	}
	public void setItemStatuz(Set<Long> itemStatuz) {
		this.itemStatuz = itemStatuz;
	}
	public Set<Long> getLocations() {
		return locations;
	}
	public void setLocations(Set<Long> locations) {
		this.locations = locations;
	}
	public Set<String> getItemStates() {
		return itemStates;
	}
	public void setItemStates(Set<String> itemStates) {
		this.itemStates = itemStates;
	}
	public Set<YesNo> getActives() {
		return actives;
	}
	public void setActives(Set<YesNo> actives) {
		this.actives = actives;
	}
	public Set<YesNo> getCheckeds() {
		return checkeds;
	}
	public void setCheckeds(Set<YesNo> checkeds) {
		this.checkeds = checkeds;
	}
	
}
