package org.minioasis.library.domain;

import org.minioasis.validation.Notification;

public class AttachmentCheckoutResult {

	private AttachmentCheckout attachmentCheckout = null;
	private Reservation reservation = null;
	private Notification notification = new Notification();
	
	public AttachmentCheckoutResult() { }
	
	public AttachmentCheckoutResult(AttachmentCheckout attachmentCheckout,
			Reservation reservation, Notification notification) {
		this.attachmentCheckout = attachmentCheckout;
		this.reservation = reservation;
		this.notification = notification;
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

	public Notification getNotification() {
		return notification;
	}

	public void setNotification(Notification notification) {
		this.notification = notification;
	}

}
