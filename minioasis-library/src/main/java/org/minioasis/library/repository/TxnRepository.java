package org.minioasis.library.repository;

import org.minioasis.library.domain.Txn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TxnRepository extends JpaRepository<Txn, Long>, TxnRepositoryCustom {

}
