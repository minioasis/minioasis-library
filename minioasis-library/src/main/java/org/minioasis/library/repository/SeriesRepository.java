package org.minioasis.library.repository;

import java.util.List;

import org.minioasis.library.domain.Series;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SeriesRepository extends JpaRepository<Series, Long> {

	Series findByName(String name);
	
	List<Series> findByNameContainingIgnoreCase(String name);
	
	Page<Series> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
