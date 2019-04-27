package org.minioasis.library.repository;

import org.minioasis.library.domain.AttachmentCheckout;
import org.minioasis.library.domain.search.AttachmentCheckoutCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AttachmentCheckoutRepositoryCustom {

	Page<AttachmentCheckout> findByCriteria(AttachmentCheckoutCriteria criteria, Pageable pageable);
}
