package org.minioasis.library.repository;

import org.minioasis.library.domain.Group;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface GroupRepository extends JpaRepository<Group, Long> {

	@Query("SELECT g FROM Group g WHERE LOWER(g.code) LIKE %?1% OR LOWER(g.name) LIKE %?1%")
	public Page<Group> findByCodeOrNameContaining(String keyword, Pageable pageable);

}
