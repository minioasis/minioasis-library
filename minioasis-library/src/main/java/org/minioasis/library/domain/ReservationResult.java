package org.minioasis.library.domain;

import org.minioasis.validation.Notification;

public class ReservationResult {

	private Reservation reservation = null;
	private Notification notification = new Notification();
	
	public ReservationResult(){ }
	
	public ReservationResult(Reservation reservation, Notification notification) {
		super();
		this.reservation = reservation;
		this.notification = notification;
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
