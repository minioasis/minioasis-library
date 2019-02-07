package org.minioasis.library.repository;

import org.minioasis.library.domain.PublicationType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PublicationTypeRepository extends JpaRepository<PublicationType, Long> {

	public Page<PublicationType> findAllByNameContaining(String type, Pageable pageable);
}
