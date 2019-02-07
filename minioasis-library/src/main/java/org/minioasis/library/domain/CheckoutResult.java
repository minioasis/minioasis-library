package org.minioasis.library.domain;

public class CheckoutResult {
	
	// this field is used by renew and reportlost
	private Checkout checkout = null;
	private AttachmentCheckout attachmentCheckout = null;
	private Reservation reservation = null;
	
	public CheckoutResult(){ }

	public Checkout getCheckout() {
		return checkout;
	}

	public void setCheckout(Checkout checkout) {
		this.checkout = checkout;
	}

	public AttachmentCheckout getAttachmentCheckout() {
		return attachmentCheckout;
	}

	public void setAttachmentCheckout(AttachmentCheckout attachmentCheckout) {
		this.attachmentCheckout = attachmentCheckout;
	}

	public Reservation getReservation() {
		return reservation;
	}

	public void setReservation(Reservation reservation) {
		this.reservation = reservation;
	}
	
}
