package org.minioasis.library.domain.search;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.minioasis.library.domain.CheckoutState;

public class CheckoutCriteria implements Serializable {

	private static final long serialVersionUID = -7908482252906419894L;

	private String cardkey;
	private String barcode;
	private String title;
	private Date checkoutFrom;
	private Date checkoutTo;
	private Date dueDateFrom;
	private Date dueDateTo;
	private Date doneFrom;
	private Date doneTo;
	
	Set<CheckoutState> states = new HashSet<CheckoutState>();

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

	public Date getCheckoutFrom() {
		return checkoutFrom;
	}

	public void setCheckoutFrom(Date checkoutFrom) {
		this.checkoutFrom = checkoutFrom;
	}

	public Date getCheckoutTo() {
		return checkoutTo;
	}

	public void setCheckoutTo(Date checkoutTo) {
		this.checkoutTo = checkoutTo;
	}

	public Date getDueDateFrom() {
		return dueDateFrom;
	}

	public void setDueDateFrom(Date dueDateFrom) {
		this.dueDateFrom = dueDateFrom;
	}

	public Date getDueDateTo() {
		return dueDateTo;
	}

	public void setDueDateTo(Date dueDateTo) {
		this.dueDateTo = dueDateTo;
	}

	public Date getDoneFrom() {
		return doneFrom;
	}

	public void setDoneFrom(Date doneFrom) {
		this.doneFrom = doneFrom;
	}

	public Date getDoneTo() {
		return doneTo;
	}

	public void setDoneTo(Date doneTo) {
		this.doneTo = doneTo;
	}

	public Set<CheckoutState> getStates() {
		return states;
	}

	public void setStates(Set<CheckoutState> states) {
		this.states = states;
	}

}
