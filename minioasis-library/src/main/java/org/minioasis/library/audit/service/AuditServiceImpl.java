package org.minioasis.library.audit.service;

import java.util.Optional;

import org.minioasis.library.audit.DeletedAuditEntity;
import org.minioasis.library.domain.Biblio;
import org.minioasis.library.repository.BiblioRepository;
import org.minioasis.library.repository.BiblioRevisionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.history.Revision;
import org.springframework.data.history.Revisions;
import org.springframework.stereotype.Service;

@Service
public class AuditServiceImpl implements AuditService {

	@Autowired
	private BiblioRepository biblioRepository;
	
	@Autowired
	BiblioRevisionRepository biblioRevisionRepository;
	
	public Revisions<Integer, Biblio> findRevisions(Long id) {
		return this.biblioRepository.findRevisions(id);
	}
	
	public Page<Revision<Integer, Biblio>> findRevisions(Long id, Pageable pageable) {
		return this.biblioRepository.findRevisions(id, pageable);
	}
	
	public Optional<Revision<Integer, Biblio>> findRevisions(Long id, Integer revisionNumber) {
		return this.biblioRepository.findRevision(id, revisionNumber);
	}
	
	public Optional<Revision<Integer, Biblio>> findLastChangeRevision(Long id) {
		return this.biblioRepository.findLastChangeRevision(id);
	}
	
	public Page<DeletedAuditEntity> listDeletedBibliosIn(String title, int days, Pageable pageable) {
		return this.biblioRevisionRepository.listDeletedBibliosIn(title, days, pageable);
	}
}
