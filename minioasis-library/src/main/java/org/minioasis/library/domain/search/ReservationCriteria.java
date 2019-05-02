package org.minioasis.library.domain.search;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.minioasis.library.domain.ReservationState;

public class ReservationCriteria implements Serializable {

	private static final long serialVersionUID = 1891212596560171728L;

	private String cardKey;
	private String isbn;
	private LocalDateTime reservationDateFrom;
	private LocalDateTime reservationDateTo;
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

	public LocalDateTime getReservationDateFrom() {
		return reservationDateFrom;
	}

	public void setReservationDateFrom(LocalDateTime reservationDateFrom) {
		this.reservationDateFrom = reservationDateFrom;
	}

	public LocalDateTime getReservationDateTo() {
		return reservationDateTo;
	}

	public void setReservationDateTo(LocalDateTime reservationDateTo) {
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
