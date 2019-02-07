package org.minioasis.library.repository;

import org.minioasis.library.domain.Location;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {

	Page<Location> findAllByName(String name, Pageable pageable);
}
