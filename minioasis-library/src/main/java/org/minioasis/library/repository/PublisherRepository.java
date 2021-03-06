package org.minioasis.library.repository;

import java.util.List;

import org.minioasis.library.domain.Publisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PublisherRepository extends JpaRepository<Publisher, Long> {

	Publisher findByName(String name);
	
	List<Publisher> findFirst10PublisherByNameContainingIgnoreCase(String name, Sort sort);
	
	Page<Publisher> findPublishersByNameContaining(String name, Pageable pageable);
}
