package org.minioasis.library.repository;

import org.minioasis.library.domain.Checkout;
import org.minioasis.library.domain.search.CheckoutCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CheckoutRepositoryCustom {

	Page<Checkout> findByCriteria(CheckoutCriteria criteria, Pageable pageable);
}
