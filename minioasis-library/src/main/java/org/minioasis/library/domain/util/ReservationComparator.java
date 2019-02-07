package org.minioasis.library.domain.util;

import java.util.Comparator;

import org.minioasis.library.domain.Reservation;

public class ReservationComparator implements Comparator<Reservation>{
	
	public int compare(Reservation o1, Reservation o2) {
		
		Reservation r1 = (Reservation)o1;
		Reservation r2 = (Reservation)o2;

		return r1.getReservationDate().compareTo(r2.getReservationDate());
	  }

}
