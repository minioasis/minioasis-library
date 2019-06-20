package org.minioasis.library.repository;

import org.minioasis.library.domain.JournalEntry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JournalEntryRepository extends JpaRepository<JournalEntry, Long>, JournalEntryRepositoryCustom {

}
