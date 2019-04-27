package org.minioasis.library.domain.search;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.minioasis.library.domain.ReservationState;

public class ReservationCriteria implements Serializable {

	private static final long serialVersionUID = 1891212596560171728L;

	private String cardKey;
	private String isbn;
	private Date reservationDateFrom;
	private Date reservationDateTo;
	private Date availableDateFrom;
	private Date availableDateTo;
	private Date notificationDateFrom;
	private Date notificationDateTo;
	
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

	public Date getReservationDateFrom() {
		return reservationDateFrom;
	}

	public void setReservationDateFrom(Date reservationDateFrom) {
		this.reservationDateFrom = reservationDateFrom;
	}

	public Date getReservationDateTo() {
		return reservationDateTo;
	}

	public void setReservationDateTo(Date reservationDateTo) {
		this.reservationDateTo = reservationDateTo;
	}

	public Date getAvailableDateFrom() {
		return availableDateFrom;
	}

	public void setAvailableDateFrom(Date availableDateFrom) {
		this.availableDateFrom = availableDateFrom;
	}

	public Date getAvailableDateTo() {
		return availableDateTo;
	}

	public void setAvailableDateTo(Date availableDateTo) {
		this.availableDateTo = availableDateTo;
	}

	public Date getNotificationDateFrom() {
		return notificationDateFrom;
	}

	public void setNotificationDateFrom(Date notificationDateFrom) {
		this.notificationDateFrom = notificationDateFrom;
	}

	public Date getNotificationDateTo() {
		return notificationDateTo;
	}

	public void setNotificationDateTo(Date notificationDateTo) {
		this.notificationDateTo = notificationDateTo;
	}

	public Set<ReservationState> getStates() {
		return states;
	}

	public void setStates(Set<ReservationState> states) {
		this.states = states;
	}
	
}
