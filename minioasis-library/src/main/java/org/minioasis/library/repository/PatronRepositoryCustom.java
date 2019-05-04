package org.minioasis.library.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.jooq.Record3;
import org.jooq.Result;
import org.minioasis.library.domain.Group;
import org.minioasis.library.domain.Patron;
import org.minioasis.library.domain.search.PatronCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PatronRepositoryCustom {

	int bulkUpdateGroup(List<Long> ids, Group group, LocalDateTime now);
	
	Page<Patron> findByCriteria(PatronCriteria criteria, Pageable pageable);
	
	Result<Record3<Integer, String, Integer>> CountPatronsByTypes();
	
}
