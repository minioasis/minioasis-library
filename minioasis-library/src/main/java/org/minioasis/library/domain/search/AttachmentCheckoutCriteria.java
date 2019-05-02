package org.minioasis.library.domain.search;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import org.minioasis.library.domain.AttachmentCheckoutState;

public class AttachmentCheckoutCriteria implements Serializable {

	private static final long serialVersionUID = -5123573089881018415L;

	private String barcode;
	private String cardkey;
	private String keyword;
	private LocalDate checkoutFrom;
	private LocalDate checkoutTo;
	private LocalDate doneFrom;
	private LocalDate doneTo;

	Set<AttachmentCheckoutState> states = new HashSet<AttachmentCheckoutState>();

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public String getCardkey() {
		return cardkey;
	}

	public void setCardkey(String cardkey) {
		this.cardkey = cardkey;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
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

	public Set<AttachmentCheckoutState> getStates() {
		return states;
	}

	public void setStates(Set<AttachmentCheckoutState> states) {
		this.states = states;
	}
	
}
