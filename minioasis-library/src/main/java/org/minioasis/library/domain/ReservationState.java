package org.minioasis.library.domain;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public enum ReservationState {

	// active
	RESERVE("Reserve"), 
	AVAILABLE("Available"),
	NOTIFIED("Notified"),
	// completed
	CANCEL("Cancel"),
	RESERVATION_EXPIRED("Reservation Expired"),
	COLLECTION_PERIOD_EXPIRED("Collection Period Expired"), 
	COLLECTED("Collected"),
	PUNISHED("Punished");
	
	private final String name;

	private ReservationState(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
	public static List<ReservationState> getActives(){
		return new ArrayList<ReservationState>(EnumSet.range(RESERVE, NOTIFIED));
	}

}