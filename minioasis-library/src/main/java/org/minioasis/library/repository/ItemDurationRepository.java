package org.minioasis.library.repository;

import org.minioasis.library.domain.ItemDuration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemDurationRepository extends JpaRepository<ItemDuration, Long> {

	Page<ItemDuration> findAllByName(String name, Pageable pageable);
}
