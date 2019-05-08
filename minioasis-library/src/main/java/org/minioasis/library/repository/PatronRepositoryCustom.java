package org.minioasis.library.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.jooq.Record1;
import org.jooq.Record3;
import org.jooq.Record4;
import org.jooq.Result;
import org.minioasis.library.domain.Group;
import org.minioasis.library.domain.Patron;
import org.minioasis.library.domain.search.PatronCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PatronRepositoryCustom {

	int bulkUpdateGroup(List<Long> ids, Group group, LocalDateTime now);
	
	Page<Patron> findByCriteria(PatronCriteria criteria, Pageable pageable);
	
	Result<Record4<Integer, Integer, String, Integer>> CountPatronsByTypes();
	
	Result<Record3<Integer, String, Integer>> CountPatronsByTypes3(int year);
	
	Result<Record1<Integer>> getAllPatronsStartedYears();
	
}
