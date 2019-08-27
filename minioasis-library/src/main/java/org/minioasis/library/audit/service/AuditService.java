package org.minioasis.library.audit.service;

import java.util.Optional;

import org.minioasis.library.domain.Attachment;
import org.minioasis.library.domain.Biblio;
import org.minioasis.library.domain.Item;
import org.minioasis.library.domain.Patron;
import org.minioasis.library.domain.Reservation;
import org.minioasis.library.domain.TelegramUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.history.Revision;
import org.springframework.data.history.Revisions;

public interface AuditService {

	// Attachment
	Revisions<Integer, Attachment> findAttachmentRevisions(Long id);
	Page<Revision<Integer, Attachment>> findAttachmentRevisions(Long id, Pageable pageable);
	Optional<Revision<Integer, Attachment>> findAttachmentRevisions(Long id, Integer revisionNumber);
	Optional<Revision<Integer, Attachment>> findAttachmentLastChangeRevision(Long id);
	Page<Object[]> listDeletedAttachmentsIn(String title, int days, Pageable pageable);
	
	// Biblio
	Revisions<Integer, Biblio> findBiblioRevisions(Long id);
	Page<Revision<Integer, Biblio>> findBiblioRevisions(Long id, Pageable pageable);
	Optional<Revision<Integer, Biblio>> findBiblioRevisions(Long id, Integer revisionNumber);
	Optional<Revision<Integer, Biblio>> findBiblioLastChangeRevision(Long id);
	Page<Object[]> listDeletedBibliosIn(String title, int days, Pageable pageable);

	// Item
	Revisions<Integer, Item> findItemRevisions(Long id);
	Page<Revision<Integer, Item>> findItemRevisions(Long id, Pageable pageable);
	Optional<Revision<Integer, Item>> findItemRevisions(Long id, Integer revisionNumber);
	Optional<Revision<Integer, Item>> findItemLastChangeRevision(Long id);
	Page<Object[]> listDeletedItemsIn(String title, int days, Pageable pageable);
	
	// Patron
	Revisions<Integer, Patron> findPatronRevisions(Long id);
	Page<Revision<Integer, Patron>> findPatronRevisions(Long id, Pageable pageable);
	Optional<Revision<Integer, Patron>> findPatronRevisions(Long id, Integer revisionNumber);
	Optional<Revision<Integer, Patron>> findPatronLastChangeRevision(Long id);
	Page<Object[]> listDeletedPatronsIn(String title, int days, Pageable pageable);
	
	// Reservation
	Revisions<Integer, Reservation> findReservationRevisions(Long id);
	Page<Revision<Integer, Reservation>> findReservationRevisions(Long id, Pageable pageable);
	Optional<Revision<Integer, Reservation>> findReservationRevisions(Long id, Integer revisionNumber);
	Optional<Revision<Integer, Reservation>> findReservationLastChangeRevision(Long id);
	Page<Object[]> listDeletedReservationsIn(String title, int days, Pageable pageable);
	
	// TelegramUser
	Revisions<Integer, TelegramUser> findTelegramUserRevisions(Long id);
	Page<Revision<Integer, TelegramUser>> findTelegramUserRevisions(Long id, Pageable pageable);
	Optional<Revision<Integer, TelegramUser>> findTelegramUserRevisions(Long id, Integer revisionNumber);
	Optional<Revision<Integer, TelegramUser>> findTelegramUserLastChangeRevision(Long id);
	Page<Object[]> listDeletedTelegramUsersIn(String title, int days, Pageable pageable);
}
