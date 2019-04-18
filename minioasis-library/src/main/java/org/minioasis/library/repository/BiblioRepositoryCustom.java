package org.minioasis.library.repository;

import org.minioasis.library.domain.Biblio;
import org.minioasis.library.domain.search.BiblioCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BiblioRepositoryCustom {

	Page<Biblio> findByCriteria(BiblioCriteria criteria, Pageable pageable);
}
