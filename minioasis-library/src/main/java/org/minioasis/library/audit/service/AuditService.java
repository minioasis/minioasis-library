package org.minioasis.library.audit.service;

import java.util.Optional;

import org.minioasis.library.audit.DeletedAuditEntity;
import org.minioasis.library.domain.Biblio;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.history.Revision;
import org.springframework.data.history.Revisions;

public interface AuditService {

	Revisions<Integer, Biblio> findRevisions(Long id);
	
	Page<Revision<Integer, Biblio>> findRevisions(Long id, Pageable pageable);
	
	Optional<Revision<Integer, Biblio>> findRevisions(Long id, Integer revisionNumber);
	
	Optional<Revision<Integer, Biblio>> findLastChangeRevision(Long id);
	
	Page<DeletedAuditEntity> listDeletedBibliosIn(String title, int days, Pageable pageable);
	
}
