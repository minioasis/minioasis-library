package org.minioasis.library.repository;

import org.minioasis.library.domain.ItemStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemStatusRepository extends JpaRepository<ItemStatus, Long> {

	Page<ItemStatus> findAllByName(String name, Pageable pageable);
}
