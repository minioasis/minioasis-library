package org.minioasis.library.repository;

import org.minioasis.library.domain.Holiday;
import org.minioasis.library.domain.search.HolidayCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface HolidayRepositoryCustom {
	
	Page<Holiday> findByCriteria(HolidayCriteria criteria, Pageable pageable);

}
