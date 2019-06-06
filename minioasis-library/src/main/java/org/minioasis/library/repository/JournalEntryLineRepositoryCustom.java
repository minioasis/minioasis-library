package org.minioasis.library.repository;

import org.minioasis.library.domain.JournalEntryLine;
import org.minioasis.library.domain.search.JournalEntryLineCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface JournalEntryLineRepositoryCustom {

	Page<JournalEntryLine> findByCriteria(JournalEntryLineCriteria criteria, Pageable pageable);
}
