package org.minioasis.library.audit.service;

import java.util.Optional;

import org.minioasis.library.audit.repository.BiblioRevisionRepository;
import org.minioasis.library.audit.repository.PatronRevisionRepository;
import org.minioasis.library.domain.Biblio;
import org.minioasis.library.domain.Patron;
import org.minioasis.library.repository.BiblioRepository;
import org.minioasis.library.repository.PatronRepository;
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
	private PatronRepository patronRepository;
	
	@Autowired
	BiblioRevisionRepository biblioRevisionRepository;
	
	@Autowired
	PatronRevisionRepository patronRevisionRepository;
	
	public Revisions<Integer, Biblio> findBiblioRevisions(Long id) {
		return this.biblioRepository.findRevisions(id);
	}
	
	public Page<Revision<Integer, Biblio>> findBiblioRevisions(Long id, Pageable pageable) {
		return this.biblioRepository.findRevisions(id, pageable);
	}
	
	public Optional<Revision<Integer, Biblio>> findBiblioRevisions(Long id, Integer revisionNumber) {
		return this.biblioRepository.findRevision(id, revisionNumber);
	}
	
	public Optional<Revision<Integer, Biblio>> findBiblioLastChangeRevision(Long id) {
		return this.biblioRepository.findLastChangeRevision(id);
	}
	
	public Page<Object[]> listDeletedBibliosIn(String title, int days, Pageable pageable) {
		return this.biblioRevisionRepository.listDeletedBibliosIn(title, days, pageable);
	}
	
	public Revisions<Integer, Patron> findPatronRevisions(Long id) {
		return this.patronRepository.findRevisions(id);
	}
	
	public Page<Revision<Integer, Patron>> findPatronRevisions(Long id, Pageable pageable) {
		return this.patronRepository.findRevisions(id, pageable);
	}
	
	public Optional<Revision<Integer, Patron>> findPatronRevisions(Long id, Integer revisionNumber) {
		return this.patronRepository.findRevision(id, revisionNumber);
	}
	
	public Optional<Revision<Integer, Patron>> findPatronLastChangeRevision(Long id) {
		return this.patronRepository.findLastChangeRevision(id);
	}
	
	public Page<Object[]> listDeletedPatronsIn(String title, int days, Pageable pageable) {
		return this.patronRevisionRepository.listDeletedPatronsIn(title, days, pageable);
	}
}
