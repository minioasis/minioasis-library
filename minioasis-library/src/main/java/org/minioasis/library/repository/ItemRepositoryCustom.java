package org.minioasis.library.repository;

import org.minioasis.library.domain.Item;
import org.minioasis.library.domain.search.ItemCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ItemRepositoryCustom {

	 void stockCheck();
	 
	 Page<Item> findByCriteria(ItemCriteria criteria, Pageable pageable);
	 
}
