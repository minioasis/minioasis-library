package org.minioasis.library.repository;

import java.util.List;

import org.minioasis.library.domain.Reservation;
import org.minioasis.library.domain.ReservationState;
import org.minioasis.library.domain.search.ReservationCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReservationRepositoryCustom {

	void refreshReservationStates();
	
	List<Reservation> findByBiblioIdAndStates(long id, List<ReservationState> states);
	
	Page<Reservation> findByCriteria(ReservationCriteria criteria, Pageable pageable);
	
}
