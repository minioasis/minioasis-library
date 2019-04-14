package org.minioasis.library.service;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.minioasis.library.domain.Attachment;
import org.minioasis.library.domain.AttachmentCheckout;
import org.minioasis.library.domain.AttachmentCheckoutState;
import org.minioasis.library.domain.Biblio;
import org.minioasis.library.domain.Checkout;
import org.minioasis.library.domain.CheckoutResult;
import org.minioasis.library.domain.CheckoutState;
import org.minioasis.library.domain.Group;
import org.minioasis.library.domain.Holiday;
import org.minioasis.library.domain.Item;
import org.minioasis.library.domain.ItemDuration;
import org.minioasis.library.domain.ItemStatus;
import org.minioasis.library.domain.Block;
import org.minioasis.library.domain.Patron;
import org.minioasis.library.domain.PatronType;
import org.minioasis.library.domain.Location;
import org.minioasis.library.domain.Publisher;
import org.minioasis.library.domain.Reservation;
import org.minioasis.library.domain.ReservationResult;
import org.minioasis.library.domain.Series;
import org.minioasis.library.domain.search.HolidayCriteria;
import org.minioasis.library.domain.search.PatronCriteria;
import org.minioasis.library.exception.LibraryException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public interface LibraryService {

	// Business Logic
	// Checkout
	void checkout(Patron patron, Item item, Date given) throws LibraryException;
	void checkoutAttachment(Patron patron, Attachment attachment, Date given) throws LibraryException;
	
	// Checkin
	CheckoutResult checkin(Patron patron, Item item, Date given, boolean damage, boolean renew) throws LibraryException;
	void checkinAttachment(Patron patron, Attachment attachment, Date given, boolean damageBadly) throws LibraryException;
	
	// Renew
	void renew(Patron patron, Item item, Date given) throws LibraryException;
	
	// ReportLost
	void reportlost(Patron patron, Item item, Date given) throws LibraryException;
	void reportlost(Patron patron, Attachment attachment, Date given) throws LibraryException;
	
	// PayFine
	void payFine(Patron patron, Long[] ids, BigDecimal payAmount, Date given) throws LibraryException;
	
	// Return
	CheckoutResult returnItem(Item item, Date given, boolean damage) throws LibraryException;
	CheckoutResult returnAttachment(Attachment attachment, Date given, boolean damage) throws LibraryException;
	
	// Reserve
	ReservationResult reserve(Patron patron, Biblio biblio, Date given, Date expiryDate) throws LibraryException;
	void cancelReservation(Patron patron, long reservationId, Date cancelDate) throws LibraryException;
	
	
	// Attachment
	void save(Attachment entity);
	void delete(Attachment entity);
	void deleteAttachment(long id);
	Attachment getAttachment(long id);
	Attachment getAttachment(String barcode);
	Attachment getAttachmentForCheckout(String barcode);
	Attachment getAttachmentForCheckin(String barcode);
	List<Attachment> findAllAttachments(Sort sort);
	Page<Attachment> findAllAttachments(Pageable pageable);
	Page<Attachment> findByBarcode(String barcode, Pageable pageable);
	Page<Attachment> findByDescriptionContaining(String desp, Pageable pageable);

	// AttachmentCheckout
	void save(AttachmentCheckout entity);
	void delete(AttachmentCheckout entity);
	void deleteAttachmentCheckout(long id);
	AttachmentCheckout getAttachmentCheckout(long id);
	List<AttachmentCheckout> findAllAttachmentCheckouts(Sort sort);
	Page<AttachmentCheckout> findAllAttachmentCheckouts(Pageable pageable);
	
	// Biblio	
	void save(Biblio entity);
	void delete(Biblio entity);
	void deleteBiblio(long id);
	boolean upload(Biblio biblio);
	Biblio getBiblio(long id);
	Biblio getBiblioDetails(long id);
	Biblio getBiblioFetchItems(long id);
	List<Biblio> findAllBiblios(Sort sort);
	Page<Biblio> findAllBiblios(Pageable pageable);
	Page<Biblio> findAllBibliosWithoutItem(Pageable pageable);
	Page<Biblio> findByTitleAndIsbnAndNoteContaining(String title, Pageable pageable);
	
	// Block	
	void save(Block entity);
	void delete(Block entity);
	void deleteBlock(long id);
	Block getBlock(long id);
	Block getBlockFetchUser(long id);
	List<Block> findAllBlocks(Sort sort);
	List<Block> getFilterBlocksByCardKey(String cardKey);
	Page<Block> findAllBlocks(Pageable pageable);
	Page<Block> findAllBlocksByName(String name, Pageable pageable);
	
	void refreshBlockStates();
	
	// Checkout
	void save(Checkout entity);
	void update(Checkout entity);
	void delete(Checkout entity);
	void deleteCheckout(long id);
	Checkout getCheckout(long id);
	List<Checkout> findAllCheckouts(Sort sort);
	List<Checkout> findByCardKeyFetchItemBiblio(String cardKey);
	List<Checkout> findByBarcodeAndFilterByStates(String barcode, List<CheckoutState> cStates, List<AttachmentCheckoutState> acStates);
	Page<Checkout> findAllCheckouts(Pageable pageable);
	Page<Checkout> findAllOverDue(List<CheckoutState> cStates, Date given, Pageable pageable);
	Page<Checkout> findAllCheckouts(String username, List<CheckoutState> cStates, Pageable pageable);
	//Page<Checkout> findAllCheckoutsByName(String name, Pageable pageable);

	// Group
	void save(Group entity);
	void delete(Group entity);
	void deleteGroup(long id);
	Group getGroup(long id);
	List<Group> findAllGroups();
	List<Group> findAllGroups(Sort sort);
	Page<Group> findAllGroups(Pageable pageable);
	Page<Group> findByCodeOrNameContaining(String keyword, Pageable pageable);
	
	// Holiday
	void save(Holiday entity);
	void delete(Holiday entity);
	void deleteHoliday(long id);
	Holiday getHoliday(long id);
	Holiday getHolidayByDueDate(Date dueDate);
	Holiday getHolidayByStartAndEndDate(Date start, Date end);
	List<Holiday> findByInBetween(Date start , Date end);
	List<Holiday> findByInBetweenWithFines(Date start, Date end, Boolean fine);
	List<Holiday> findByExcluded(Date start , Date end);
	List<Holiday> findAllHolidays(Sort sort);
	List<Holiday> findAllHolidaysByGivenDate(Date given);
	Page<Holiday> findAllHolidays(Pageable pageable);
	Page<Holiday> findByCriteria(HolidayCriteria criteria, Pageable pageable);
	
	// Item	
	void save(Item entity);
	void delete(Item entity);
	void deleteItem(long id);
	Item getItem(long id);
	Item getItemFetchBiblio(String barcode);
	Item getItemFetchItemRelated(long id);
	Item getItemFetchItemStatus(String barcode);
	Item getItemFetchRelatedBiblio(String barcode);
	Item findByBarcode(String barcode);
	List<Item> findAllItems(Sort sort);
	List<Item> findAllItemsOrderByBarcode(long id);
	Page<Item> findAllItems(Pageable pageable);
	Page<Item> findAllItemsByKeyword(String keyword, Pageable pageable);
	
	Item getItemForCheckout(String barcode);
	Item getItemForCheckin(String barcode);
	
	void stockCheck();
	
	//Map findStockCheckItemsByCriteria(ItemCmd command);

	// ItemDuration	
	void save(ItemDuration entity);
	void delete(ItemDuration entity);
	void deleteItemDuration(long id);
	ItemDuration getItemDuration(long id);
	List<ItemDuration> findAllItemDurations();
	List<ItemDuration> findAllItemDurations(Sort sort);
	Page<ItemDuration> findAllItemDurationsByName(String name, Pageable pageable);
	
	// ItemStatus
	void save(ItemStatus entity);
	void delete(ItemStatus entity);
	void deleteItemStatus(long id);
	ItemStatus getItemStatus(long id);
	List<ItemStatus> findAllItemStatus();
	List<ItemStatus> findAllItemStatus(Sort sort);
	Page<ItemStatus> findAllItemStatusByName(String name, Pageable pageable);
	
	// Location
	void save(Location entity);
	void delete(Location entity);
	void deleteLocation(long id);
	Location getLocation(long id);
	List<Location> findAllLocations();
	List<Location> findAllLocations(Sort sort);
	Page<Location> findAllLocationsByName(String name, Pageable pageable);

	// Patron
	void save(Patron entity);
	void edit(Patron entity);
	int bulkUpdateGroup(List<Long> ids, Group group, Date now);
	void delete(Patron entity);
	void deletePatron(long id);
	Patron getPatron(long id);
	boolean upload(Patron patron);
	Patron findByCardKey(String name);
	Patron findByEntangled(String name);
	List<Patron> findAllPatrons(Sort sort);
	List<Patron> findByCardKeyContaining(String name);
	List<Patron> findByEntangledContaining(String name);
	List<Patron> findByCardKeyAndEntangledContaining(String keyword);
	List<Patron> findByNameOrCardKeyContaining(String key,Pageable pageable);
	List<Patron> findByIdIn(Collection<Long> ids);
	List<Patron> findByGroupAndUpdatedOrderByUpdatedDesc(Group group, Date updated);
	Page<Patron> findAllPatrons(Pageable pageable);
	Page<Patron> findByCriteria(PatronCriteria criteria, Pageable pageable);
	
	Patron getPatronByCardKeyForCirculation(String cardKey,Date given);
	
	// PatronType	
	void save(PatronType entity);
	void delete(PatronType entity);
	void deletePatronType(long id);
	PatronType getPatronType(long id);
	List<PatronType> findAllPatronTypes(Sort sort);
	List<PatronType> findByNameContaining(String name);
	
	// Publisher
	void save(Publisher entity);
	void deletePublisher(long id);
	void delete(Publisher entity);
	Publisher getPublisher(long id);
	Publisher findPublisherByName(String name);
	List<Publisher> findAllPublishers(Sort sort);
	List<Publisher> findPublishersyNameContaining(String name);
	Page<Publisher> findAllPublishers(Pageable pageable);
	Page<Publisher> findPublishersByNameContaining(String name, Pageable pageable);

	// Reservation
	void save(Reservation entity);
	void delete(Reservation entity);
	void deleteReservation(long id);
	Reservation getReservation(long id);
	List<Reservation> findAllReservations(Sort sort);
	List<Reservation> findFilteredReservationsByCardKeyFetchBiblio(String cardKey);
	
	List<Reservation> findFilteredReservationsByCardKeyFetchBiblioReservations(String cardKey);
	
	List<Reservation> findReservationsByBiblioIdAndActiveStates(long id);
	Page<Reservation> findAllReservations(Pageable pageable);
	
	void refreshReservationStates();
	//void clearExpiredReservations(Date given);
	//void clearUncollectedReservations(Date given);
	//void clearPunishedReservations(Date given);
	
	// Series
	void save(Series entity);
	void delete(Series entity);
	void deleteSeries(long id);
	Series getSeries(long id);
	Series findSeriesByName(String name);
	List<Series> findAllSeries(Sort sort);
	List<Series> findSeriesByNameContaining(String name);
	Page<Series> findAllSeries(Pageable pageable);
	Page<Series> findSeriesByNameContaining(String name, Pageable pageable);
	
}