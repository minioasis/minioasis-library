package org.minioasis.library.repository;

import org.minioasis.library.domain.Account;
import org.minioasis.library.domain.search.AccountCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AccountRepositoryCustom {

	Page<Account> findByCriteria(AccountCriteria criteria, Pageable pageable);
}
