package org.minioasis.library.repository;

import org.minioasis.library.domain.Attachment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttachmentRepository extends JpaRepository<Attachment, Long>, AttachmentRepositoryCustom {

	Attachment findByBarcode(String barcode);
	
	Page<Attachment> findByBarcode(String barcode, Pageable pageable);
	
	Page<Attachment> findByDescriptionContaining(String desp, Pageable pageable);
	
}
