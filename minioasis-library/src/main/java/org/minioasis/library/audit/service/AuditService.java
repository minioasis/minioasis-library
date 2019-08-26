package org.minioasis.library.audit.service;

import java.util.Optional;

import org.minioasis.library.domain.Biblio;
import org.minioasis.library.domain.Patron;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.history.Revision;
import org.springframework.data.history.Revisions;

public interface AuditService {

	Revisions<Integer, Biblio> findBiblioRevisions(Long id);
	
	Page<Revision<Integer, Biblio>> findBiblioRevisions(Long id, Pageable pageable);
	
	Optional<Revision<Integer, Biblio>> findBiblioRevisions(Long id, Integer revisionNumber);
	
	Optional<Revision<Integer, Biblio>> findBiblioLastChangeRevision(Long id);
	
	Page<Object[]> listDeletedBibliosIn(String title, int days, Pageable pageable);
	
	Revisions<Integer, Patron> findPatronRevisions(Long id);
	
	Page<Revision<Integer, Patron>> findPatronRevisions(Long id, Pageable pageable);
	
	Optional<Revision<Integer, Patron>> findPatronRevisions(Long id, Integer revisionNumber);
	
	Optional<Revision<Integer, Patron>> findPatronLastChangeRevision(Long id);
	
	Page<Object[]> listDeletedPatronsIn(String title, int days, Pageable pageable);
	
}
