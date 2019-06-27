package org.minioasis.library.repository;

import java.util.List;

import org.minioasis.library.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long>, AccountRepositoryCustom {
	
	public Account findByCode(String code);
	
	@Query("SELECT a FROM Account a WHERE lower(a.code) LIKE %?1% OR lower(a.name) LIKE %?1%")
	public List<Account> findByCodeContaining(String code);
	
}
