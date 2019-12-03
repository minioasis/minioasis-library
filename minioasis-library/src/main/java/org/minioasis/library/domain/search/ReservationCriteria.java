package org.minioasis.library.domain.search;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import org.minioasis.library.domain.ReservationState;

public class ReservationCriteria implements Serializable {

	private static final long serialVersionUID = 1891212596560171728L;

	private String cardKey;
	private String isbn;
	private LocalDate reservationDateFrom;
	private LocalDate reservationDateTo;
	private LocalDate availableDateFrom;
	private LocalDate availableDateTo;
	private LocalDate notificationDateFrom;
	private LocalDate notificationDateTo;
	
	Set<ReservationState> states = new HashSet<ReservationState>();

	public String getCardKey() {
		return cardKey;
	}
	public void setCardKey(String cardKey) {
		this.cardKey = cardKey;
	}
	public String getIsbn() {
		return isbn;
	}
	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}
	public LocalDate getReservationDateFrom() {
		return reservationDateFrom;
	}
	public void setReservationDateFrom(LocalDate reservationDateFrom) {
		this.reservationDateFrom = reservationDateFrom;
	}
	public LocalDate getReservationDateTo() {
		return reservationDateTo;
	}
	public void setReservationDateTo(LocalDate reservationDateTo) {
		this.reservationDateTo = reservationDateTo;
	}
	public LocalDate getAvailableDateFrom() {
		return availableDateFrom;
	}
	public void setAvailableDateFrom(LocalDate availableDateFrom) {
		this.availableDateFrom = availableDateFrom;
	}
	public LocalDate getAvailableDateTo() {
		return availableDateTo;
	}
	public void setAvailableDateTo(LocalDate availableDateTo) {
		this.availableDateTo = availableDateTo;
	}
	public LocalDate getNotificationDateFrom() {
		return notificationDateFrom;
	}
	public void setNotificationDateFrom(LocalDate notificationDateFrom) {
		this.notificationDateFrom = notificationDateFrom;
	}
	public LocalDate getNotificationDateTo() {
		return notificationDateTo;
	}
	public void setNotificationDateTo(LocalDate notificationDateTo) {
		this.notificationDateTo = notificationDateTo;
	}
	public Set<ReservationState> getStates() {
		return states;
	}
	public void setStates(Set<ReservationState> states) {
		this.states = states;
	}
	
}
