package org.minioasis.library.domain.search;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.minioasis.library.domain.AttachmentCheckoutState;

public class AttachmentCheckoutCriteria implements Serializable {

	private static final long serialVersionUID = -5123573089881018415L;

	private String barcode;
	private String cardkey;
	private String keyword;
	private Date checkoutFrom;
	private Date checkoutTo;
	private Date doneFrom;
	private Date doneTo;

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

	public Set<AttachmentCheckoutState> getStates() {
		return states;
	}

	public void setStates(Set<AttachmentCheckoutState> states) {
		this.states = states;
	}
	
}
