package org.minioasis.library.repository;

import java.util.List;

import org.minioasis.library.domain.Checkout;
import org.minioasis.library.domain.search.CheckoutCriteria;
import org.minioasis.library.domain.search.CheckoutPatronCriteria;
import org.minioasis.library.domain.search.CheckoutSummary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CheckoutRepositoryCustom {

	Page<Checkout> findByCriteria(CheckoutCriteria criteria, Pageable pageable);
	
	List<CheckoutSummary> topListPatronsForCheckouts(CheckoutPatronCriteria criteria);
	
	String topListPatronsForCheckouts_JSON();

}
