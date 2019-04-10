package org.minioasis.library.repository;

import java.util.Date;
import java.util.List;

import org.minioasis.library.domain.Holiday;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface HolidayRepository extends JpaRepository<Holiday, Long>, HolidayRepositoryCustom {

	@Query("SELECT h FROM Holiday h WHERE h.startDate <= ?1 AND ?1 <= h.endDate")
	Holiday getHolidayByDueDate(Date dueDate);

	@Query("SELECT h FROM Holiday h WHERE h.startDate = ?1 AND h.endDate = ?2")
	Holiday getHolidayByStartAndEndDate(Date start, Date end);
	
	@Query("SELECT h FROM Holiday h WHERE ?1 < h.startDate AND h.endDate < ?2")
	List<Holiday> findByInBetween(Date start, Date end);
	
	@Query("SELECT h FROM Holiday h WHERE ?1 < h.startDate AND h.endDate < ?2 AND h.fine = ?3")
	List<Holiday> findByInBetweenWithFines(Date start, Date end, Boolean fine);
	
	@Query("SELECT h FROM Holiday h WHERE NOT ((?2 < h.startDate AND ?2 < h.endDate) OR (h.startDate < ?1 AND h.endDate < ?1))")
	List<Holiday> findByExcluded(Date start , Date end);
	
	@Query("SELECT h FROM Holiday h WHERE h.startDate <= ?1 AND ?1 <= h.endDate")
	List<Holiday> findAllHolidaysByGivenDate(Date given);
	
	Page<Holiday> findByNameContaining(String name , Pageable pageable);
	
}
