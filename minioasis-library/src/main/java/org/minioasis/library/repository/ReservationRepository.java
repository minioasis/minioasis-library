package org.minioasis.library.repository;

import java.util.List;

import org.minioasis.library.domain.Reservation;
import org.minioasis.library.domain.ReservationState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long>, ReservationRepositoryCustom {

	@Query("SELECT r FROM Reservation r"
			+ " JOIN r.biblio"
			+ " WHERE r.patron.cardKey = ?1 AND r.state in ?2")
	List<Reservation> findByCardKeyAndStates(String cardKey, List<ReservationState> states);
	
	@Query("SELECT r FROM Reservation r"
			+ " JOIN r.biblio"
			+ " WHERE r.patron.cardKey = ?1")
	List<Reservation> findReservationsByCardKey(String cardKey);
	
	@Query("SELECT r FROM Reservation r"
			+ " LEFT JOIN FETCH r.biblio b"
			+ " LEFT JOIN FETCH b.reservations"
			+ " WHERE b IS NOT NULL"
			+ " AND r.patron.cardKey = ?1")
	List<Reservation> findReservationsByCardKeyFetchBiblioReservations(String cardKey);

}
