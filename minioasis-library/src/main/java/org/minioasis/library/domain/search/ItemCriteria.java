package org.minioasis.library.domain.search;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.minioasis.library.domain.YesNo;

public class ItemCriteria implements Serializable {
	
	private static final long serialVersionUID = 7017446345857953691L;
	
	private String keyword;
	private String biblio;
	private Date firstCheckinFrom;
	private Date firstCheckinTo;
	private Date lastCheckinFrom;
	private Date lastCheckinTo;
	private Date expiredFrom;
	private Date expiredTo;

	
	private Set<Long> itemDurations = new HashSet<Long>();
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
	public String getBiblio() {
		return biblio;
	}
	public void setBiblio(String biblio) {
		this.biblio = biblio;
	}
	public Set<Long> getItemDurations() {
		return itemDurations;
	}
	public void setItemDurations(Set<Long> itemDurations) {
		this.itemDurations = itemDurations;
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
