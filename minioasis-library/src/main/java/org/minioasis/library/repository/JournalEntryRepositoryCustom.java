package org.minioasis.library.repository;

import org.minioasis.library.domain.JournalEntry;
import org.minioasis.library.domain.search.JournalEntryCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface JournalEntryRepositoryCustom {

	Page<JournalEntry> findByCriteria(JournalEntryCriteria criteria, Pageable pageable);
}
