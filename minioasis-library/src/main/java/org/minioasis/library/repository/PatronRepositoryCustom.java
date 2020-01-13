package org.minioasis.library.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.jooq.Record3;
import org.jooq.Record4;
import org.jooq.Result;
import org.minioasis.library.domain.Group;
import org.minioasis.library.domain.Patron;
import org.minioasis.library.domain.search.PatronCriteria;
import org.minioasis.report.chart.ChartData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PatronRepositoryCustom {

	int bulkUpdateGroup(List<Long> ids, Group group, LocalDateTime now);
	
	Page<Patron> findByCriteria(PatronCriteria criteria, Pageable pageable);
	
	Result<Record4<Integer, Integer, String, Integer>> CountPatronsByTypes();
	
	Result<Record3<Integer, String, Integer>> CountPatronsByTypes3(int year);
	
	List<Integer> getAllPatronsStartedYears();
	
	List<ChartData> CountPatronsByTypes(int from, int to);
	
	List<Patron> expiringMembershipPatrons(LocalDate given, int firstRemind, int secondRemind);
	
}
