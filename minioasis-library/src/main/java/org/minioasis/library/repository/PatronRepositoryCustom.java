package org.minioasis.library.repository;

import java.util.Date;
import java.util.List;

import org.minioasis.library.domain.Group;
import org.minioasis.library.domain.Patron;
import org.minioasis.library.domain.search.PatronCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PatronRepositoryCustom {

	int bulkUpdateGroup(List<Long> ids, Group group, Date now);
	
	Page<Patron> findByCriteria(PatronCriteria criteria, Pageable pageable);
	
}
