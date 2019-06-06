package org.minioasis.library.repository;

import org.minioasis.library.domain.Txn;
import org.minioasis.library.domain.search.TxnCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TxnRepositoryCustom {

	Page<Txn> findByCriteria(TxnCriteria criteria, Pageable pageable);
}
