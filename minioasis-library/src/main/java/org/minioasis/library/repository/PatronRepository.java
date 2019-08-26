package org.minioasis.library.repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import org.minioasis.library.domain.Group;
import org.minioasis.library.domain.Patron;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.history.RevisionRepository;

public interface PatronRepository extends RevisionRepository<Patron, Long, Integer>, JpaRepository<Patron, Long>, PatronRepositoryCustom {

	Patron findByCardKey(String key);
	
	@Query("SELECT u FROM Patron u LEFT JOIN FETCH u.patronType WHERE u.cardKey = ?1")
	Patron findByCardKeyFetchPatronType(String key);
	
	Patron findByEntangled(String key);
	List<Patron> findByCardKeyContaining(String key);
	List<Patron> findByEntangledContaining(String key);

	@Query("SELECT u FROM Patron u WHERE LOWER(u.name) LIKE %?1% OR LOWER(u.name2) LIKE %?1% OR LOWER(u.cardKey) LIKE %?1%")
	List<Patron> findByNameOrCardKeyContaining(String key , Pageable pageable);
	
	@Query("SELECT u FROM Patron u WHERE LOWER(u.cardKey) LIKE %?1% OR LOWER(u.entangled) LIKE %?1%")
	List<Patron> findByCardKeyAndEntangledContaining(String keyword); 

	List<Patron> findByIdIn(Collection<Long> ids);
	
	List<Patron> findByGroupAndUpdatedOrderByUpdatedDesc(Group group, LocalDateTime updated);

	@Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM Patron u WHERE u.cardKey = ?1 AND u.contact.mobile = ?2")
	boolean match(String cardKey, String mobile);
	
}
