package org.minioasis.library.repository;

import org.minioasis.library.domain.Attachment;
import org.minioasis.library.domain.search.AttachmentCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AttachmentRepositoryCustom {

	Page<Attachment> findByCriteria(AttachmentCriteria criteria, Pageable pageable);
}
