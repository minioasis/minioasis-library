package org.minioasis.library.audit.service;

import java.util.Optional;

import org.minioasis.library.audit.AuditRevisionEntity;
import org.minioasis.library.audit.repository.AttachmentRevisionRepository;
import org.minioasis.library.audit.repository.BiblioRevisionRepository;
import org.minioasis.library.audit.repository.ItemRevisionRepository;
import org.minioasis.library.audit.repository.PatronRevisionRepository;
import org.minioasis.library.audit.repository.ReservationRevisionRepository;
import org.minioasis.library.audit.repository.RevisionEntityRepository;
import org.minioasis.library.audit.repository.TelegramUserRevisionRepository;
import org.minioasis.library.domain.Attachment;
import org.minioasis.library.domain.Biblio;
import org.minioasis.library.domain.Item;
import org.minioasis.library.domain.Patron;
import org.minioasis.library.domain.Reservation;
import org.minioasis.library.domain.TelegramUser;
import org.minioasis.library.repository.AttachmentRepository;
import org.minioasis.library.repository.BiblioRepository;
import org.minioasis.library.repository.ItemRepository;
import org.minioasis.library.repository.PatronRepository;
import org.minioasis.library.repository.ReservationRepository;
import org.minioasis.library.repository.TelegramUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.history.Revision;
import org.springframework.data.history.Revisions;
import org.springframework.stereotype.Service;

@Service
public class AuditServiceImpl implements AuditService {

	@Autowired
	private AttachmentRepository attachmentRepository;
	@Autowired
	private AttachmentRevisionRepository attachmentRevisionRepository;
	
	@Autowired
	private BiblioRepository biblioRepository;
	@Autowired
	private BiblioRevisionRepository biblioRevisionRepository;
	
	@Autowired
	private ItemRepository itemRepository;
	@Autowired
	private ItemRevisionRepository itemRevisionRepository;
	
	@Autowired
	private PatronRepository patronRepository;
	@Autowired
	private PatronRevisionRepository patronRevisionRepository;
	
	@Autowired
	private RevisionEntityRepository revisionEntityRepository;
	
	@Autowired
	private ReservationRepository reservationRepository;
	@Autowired
	private ReservationRevisionRepository reservationRevisionRepository;
	
	@Autowired
	private TelegramUserRepository telegramUserRepository;
	@Autowired
	private TelegramUserRevisionRepository telegramUserRevisionRepository;

	// Attachment
	public Revisions<Integer, Attachment> findAttachmentRevisions(Long id) {
		return this.attachmentRepository.findRevisions(id);
	}
	public Page<Revision<Integer, Attachment>> findAttachmentRevisions(Long id, Pageable pageable) {
		return this.attachmentRepository.findRevisions(id, pageable);
	}
	public Optional<Revision<Integer, Attachment>> findAttachmentRevisions(Long id, Integer revisionNumber) {
		return this.attachmentRepository.findRevision(id, revisionNumber);
	}
	public Optional<Revision<Integer, Attachment>> findAttachmentLastChangeRevision(Long id) {
		return this.attachmentRepository.findLastChangeRevision(id);
	}
	public Page<Object[]> listDeletedAttachmentsIn(String title, int days, Pageable pageable) {
		return this.attachmentRevisionRepository.listDeletedAttachmentsIn(title, days, pageable);
	}
	
	// Biblio
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

	// Item
	public Revisions<Integer, Item> findItemRevisions(Long id) {
		return this.itemRepository.findRevisions(id);
	}	
	public Page<Revision<Integer, Item>> findItemRevisions(Long id, Pageable pageable) {
		return this.itemRepository.findRevisions(id, pageable);
	}
	public Optional<Revision<Integer, Item>> findItemRevisions(Long id, Integer revisionNumber) {
		return this.itemRepository.findRevision(id, revisionNumber);
	}
	public Optional<Revision<Integer, Item>> findItemLastChangeRevision(Long id) {
		return this.itemRepository.findLastChangeRevision(id);
	}
	public Page<Object[]> listDeletedItemsIn(String title, int days, Pageable pageable) {
		return this.itemRevisionRepository.listDeletedItemsIn(title, days, pageable);
	}
	
	// Patron
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
	
	// RevisionEntity
	public AuditRevisionEntity getRevisionEntity(Integer rev) {
		return this.revisionEntityRepository.getRevisionEntity(rev);
	}
	
	// Reservation
	public Revisions<Integer, Reservation> findReservationRevisions(Long id) {
		return this.reservationRepository.findRevisions(id);
	}
	public Page<Revision<Integer, Reservation>> findReservationRevisions(Long id, Pageable pageable) {
		return this.reservationRepository.findRevisions(id, pageable);
	}
	public Optional<Revision<Integer, Reservation>> findReservationRevisions(Long id, Integer revisionNumber) {
		return this.reservationRepository.findRevision(id, revisionNumber);
	}
	public Optional<Revision<Integer, Reservation>> findReservationLastChangeRevision(Long id) {
		return this.reservationRepository.findLastChangeRevision(id);
	}
	public Page<Object[]> listDeletedReservationsIn(String title, int days, Pageable pageable) {
		return this.reservationRevisionRepository.listDeletedReservationsIn(title, days, pageable);
	}
	
	// TelegramUser
	public Revisions<Integer, TelegramUser> findTelegramUserRevisions(Long id) {
		return this.telegramUserRepository.findRevisions(id);
	}
	public Page<Revision<Integer, TelegramUser>> findTelegramUserRevisions(Long id, Pageable pageable) {
		return this.telegramUserRepository.findRevisions(id, pageable);
	}
	public Optional<Revision<Integer, TelegramUser>> findTelegramUserRevisions(Long id, Integer revisionNumber) {
		return this.telegramUserRepository.findRevision(id, revisionNumber);
	}
	public Optional<Revision<Integer, TelegramUser>> findTelegramUserLastChangeRevision(Long id) {
		return this.telegramUserRepository.findLastChangeRevision(id);
	}
	public Page<Object[]> listDeletedTelegramUsersIn(String title, int days, Pageable pageable) {
		return this.telegramUserRevisionRepository.listDeletedTelegramUsersIn(title, days, pageable);
	}
}
