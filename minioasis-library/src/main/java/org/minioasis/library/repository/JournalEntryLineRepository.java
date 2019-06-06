package org.minioasis.library.repository;

import org.minioasis.library.domain.JournalEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JournalEntryLineRepository extends JpaRepository<JournalEntry, Long>, JournalEntryLineRepositoryCustom {

}
