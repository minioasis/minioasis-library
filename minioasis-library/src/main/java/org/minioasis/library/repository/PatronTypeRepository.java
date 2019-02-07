package org.minioasis.library.repository;

import java.util.List;

import org.minioasis.library.domain.PatronType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PatronTypeRepository extends JpaRepository<PatronType, Long> {
	
	@Query("SELECT t FROM PatronType t WHERE t.name LIKE %?1%")
	List<PatronType> findByNameContaining(String name);
}
