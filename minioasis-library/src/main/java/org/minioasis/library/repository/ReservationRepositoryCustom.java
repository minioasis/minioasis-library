package org.minioasis.library.repository;

import org.minioasis.library.domain.Reservation;
import org.minioasis.library.domain.search.ReservationCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReservationRepositoryCustom {

	void refreshReservationStates();
	
	Page<Reservation> findByCriteria(ReservationCriteria criteria, Pageable pageable);
	
}
