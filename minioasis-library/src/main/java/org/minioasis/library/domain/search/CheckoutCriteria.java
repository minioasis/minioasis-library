package org.minioasis.library.domain.search;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import org.minioasis.library.domain.CheckoutState;
import org.minioasis.library.domain.YesNo;

public class CheckoutCriteria implements Serializable {

	private static final long serialVersionUID = -7908482252906419894L;

	private String cardkey;
	private String barcode;
	private String title;
	private LocalDate checkoutFrom;
	private LocalDate checkoutTo;
	private LocalDate dueDateFrom;
	private LocalDate dueDateTo;
	private LocalDate doneFrom;
	private LocalDate doneTo;
	
	private Set<CheckoutState> states = new HashSet<CheckoutState>();
	private Set<YesNo> actives = new HashSet<YesNo>();
	private Set<Long> patronTypes = new HashSet<Long>();
	private Set<Long> groups = new HashSet<Long>();

	public String getCardkey() {
		return cardkey;
	}

	public void setCardkey(String cardkey) {
		this.cardkey = cardkey;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public LocalDate getCheckoutFrom() {
		return checkoutFrom;
	}

	public void setCheckoutFrom(LocalDate checkoutFrom) {
		this.checkoutFrom = checkoutFrom;
	}

	public LocalDate getCheckoutTo() {
		return checkoutTo;
	}

	public void setCheckoutTo(LocalDate checkoutTo) {
		this.checkoutTo = checkoutTo;
	}

	public LocalDate getDueDateFrom() {
		return dueDateFrom;
	}

	public void setDueDateFrom(LocalDate dueDateFrom) {
		this.dueDateFrom = dueDateFrom;
	}

	public LocalDate getDueDateTo() {
		return dueDateTo;
	}

	public void setDueDateTo(LocalDate dueDateTo) {
		this.dueDateTo = dueDateTo;
	}

	public LocalDate getDoneFrom() {
		return doneFrom;
	}

	public void setDoneFrom(LocalDate doneFrom) {
		this.doneFrom = doneFrom;
	}

	public LocalDate getDoneTo() {
		return doneTo;
	}

	public void setDoneTo(LocalDate doneTo) {
		this.doneTo = doneTo;
	}

	public Set<CheckoutState> getStates() {
		return states;
	}

	public void setStates(Set<CheckoutState> states) {
		this.states = states;
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

}
