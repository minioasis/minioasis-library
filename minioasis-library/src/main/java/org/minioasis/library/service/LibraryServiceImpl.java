package org.minioasis.library.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

import org.minioasis.library.domain.Account;
import org.minioasis.library.domain.Attachment;
import org.minioasis.library.domain.AttachmentCheckout;
import org.minioasis.library.domain.AttachmentCheckoutState;
import org.minioasis.library.domain.Biblio;
import org.minioasis.library.domain.Checkout;
import org.minioasis.library.domain.CheckoutResult;
import org.minioasis.library.domain.CheckoutState;
import org.minioasis.library.domain.DataType;
import org.minioasis.library.domain.FormData;
import org.minioasis.library.domain.Group;
import org.minioasis.library.domain.Holiday;
import org.minioasis.library.domain.Item;
import org.minioasis.library.domain.ItemState;
import org.minioasis.library.domain.ItemStatus;
import org.minioasis.library.domain.JournalEntry;
import org.minioasis.library.domain.JournalEntryLine;
import org.minioasis.library.domain.Patron;
import org.minioasis.library.domain.PatronType;
import org.minioasis.library.domain.Location;
import org.minioasis.library.domain.Publisher;
import org.minioasis.library.domain.Reservation;
import org.minioasis.library.domain.ReservationResult;
import org.minioasis.library.domain.ReservationState;
import org.minioasis.library.domain.Role;
import org.minioasis.library.domain.Series;
import org.minioasis.library.domain.search.AccountCriteria;
import org.minioasis.library.domain.search.AttachmentCheckoutCriteria;
import org.minioasis.library.domain.search.AttachmentCriteria;
import org.minioasis.library.domain.search.BiblioCriteria;
import org.minioasis.library.domain.search.CheckoutCriteria;
import org.minioasis.library.domain.search.HolidayCriteria;
import org.minioasis.library.domain.search.ItemCriteria;
import org.minioasis.library.domain.search.JournalEntryCriteria;
import org.minioasis.library.domain.search.JournalEntryLineCriteria;
import org.minioasis.library.domain.search.PatronCriteria;
import org.minioasis.library.domain.search.ReservationCriteria;
import org.minioasis.library.domain.util.ReservationComparator;
import org.minioasis.library.exception.LibraryException;
import org.minioasis.library.repository.AccountRepository;
import org.minioasis.library.repository.AttachmentCheckoutRepository;
import org.minioasis.library.repository.AttachmentRepository;
import org.minioasis.library.repository.BiblioRepository;
import org.minioasis.library.repository.CheckoutRepository;
import org.minioasis.library.repository.FormDataRepository;
import org.minioasis.library.repository.GroupRepository;
import org.minioasis.library.repository.HolidayRepository;
import org.minioasis.library.repository.ItemRepository;
import org.minioasis.library.repository.ItemStatusRepository;
import org.minioasis.library.repository.JournalEntryLineRepository;
import org.minioasis.library.repository.JournalEntryRepository;
import org.minioasis.library.repository.PatronRepository;
import org.minioasis.library.repository.PatronTypeRepository;
import org.minioasis.library.repository.LocationRepository;
import org.minioasis.library.repository.PublisherRepository;
import org.minioasis.library.repository.ReservationRepository;
import org.minioasis.library.repository.RoleRepository;
import org.minioasis.library.repository.SeriesRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
public class LibraryServiceImpl implements LibraryService {

	private static final Logger logger = LoggerFactory.getLogger(LibraryServiceImpl.class);
	
	@Autowired
	private AccountRepository accountRepository;
	@Autowired
	private AttachmentRepository attachmentRepository;
	@Autowired
	private AttachmentCheckoutRepository attachmentCheckoutRepository;
	@Autowired
	private BiblioRepository biblioRepository;
	@Autowired
	private CheckoutRepository checkoutRepository;
	@Autowired
	private FormDataRepository formDataRepository;
	@Autowired
	private GroupRepository groupRepository;	
	@Autowired
	private HolidayRepository holidayRepository;
	@Autowired
	private ItemRepository itemRepository;
	@Autowired
	private ItemStatusRepository itemStatusRepository;
	@Autowired
	private JournalEntryRepository journalEntryRepository;
	@Autowired
	private JournalEntryLineRepository journalEntryLineRepository;
	@Autowired
	private LocationRepository locationRepository;
	@Autowired
	private PatronRepository patronRepository;
	@Autowired
	private PatronTypeRepository patronTypeRepository;
	@Autowired
	private PublisherRepository publisherRepository;
	@Autowired
	private ReservationRepository reservationRepository;
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private SeriesRepository seriesRepository;
	@Autowired
	private HolidayCalculationStrategy holidayStrategy;

	/****************************************  Bug Cleaner **************************************/
	
	public void fixBug() {
		patronRepository.fixBug();
	}
	
	/****************************************  Business Logic **************************************/	
	
	private LocalDate calculateDueDate(Patron patron, LocalDate given) {
		long duration = patron.getPatronType().getDuration().longValue();	
		return given.plusDays(duration);
	}
	
	public void checkout(Patron patron, Item item, LocalDate given) throws LibraryException {
	
		LocalDate dueDate = calculateDueDate(patron, given);
		LocalDate newDueDate = holidayStrategy.getNewDueDateAfterHolidays(dueDate);
		
		patron.preparingCheckoutsOn(given);
		patron.checkout(item, given, newDueDate);

		this.patronRepository.save(patron);

	}

	public void renew(Patron patron, Item item, LocalDate given) throws LibraryException {
		
		LocalDate dueDate = calculateDueDate(patron,given);
		LocalDate newDueDate = holidayStrategy.getNewDueDateAfterHolidays(dueDate);
		
		patron.preparingCheckoutsOn(given);
		patron.renew(item, given, newDueDate);
		
		this.patronRepository.save(patron);
		
	}
	
	public List<Checkout> renewAll(Patron patron, LocalDate given) throws LibraryException {
		
		LocalDate dueDate = calculateDueDate(patron,given);
		LocalDate newDueDate = holidayStrategy.getNewDueDateAfterHolidays(dueDate);
		
		patron.preparingCheckoutsOn(given);
		List<Checkout> sucessRenews = patron.renewAll(given, newDueDate);
		
		this.patronRepository.save(patron);
		
		return sucessRenews;
		
	}
	
	public void checkoutAttachment(Patron patron, Attachment attachment, LocalDate given){

		patron.checkout(attachment, given);
		this.patronRepository.save(patron);
		
	}
	
	private Reservation setReservationPriorityAndItemState(Item item, LocalDate given) {
		
		List<Reservation> reservations = this.reservationRepository.findByBiblioIdAndStates(item.getBiblio().getId(),
				new ArrayList<ReservationState>(EnumSet.of(ReservationState.RESERVE)));
		
		if(reservations.size() > 0) {
			
			Collections.sort(reservations,new ReservationComparator());
			Reservation candidate = reservations.get(0);
			candidate.setAvailableDate(given);
			candidate.setState(ReservationState.AVAILABLE);

			this.reservationRepository.save(candidate);	
			item.setState(ItemState.RESERVED_IN_LIBRARY);

			return candidate;
		}
		
		return null;
	}

	public CheckoutResult checkin(Patron patron, Item item, LocalDate given, boolean damage, boolean renew) throws LibraryException {
		
		CheckoutResult result = patron.checkin(item, given, damage, renew, holidayStrategy);
		
		Reservation r = setReservationPriorityAndItemState(item, given);
		if(r != null) {
			result.setReservation(r);
		}

		this.itemRepository.save(item);
		this.patronRepository.save(patron);

		return result;

	}
	
	// why the return is void which is different from item checkin ?
	// because this method no need to return "AttachmentCheckout" and "Reservation" !
	public void checkinAttachment(Patron patron, Attachment attachment, LocalDate given, boolean damageBadly) throws LibraryException {
		
		patron.checkin(attachment, given, damageBadly);
		this.patronRepository.save(patron);

	}
	
	public void reportlost(Patron patron, Item item, LocalDate given) throws LibraryException {
		
		patron.reportLost(item, given);
		this.patronRepository.save(patron);
		
	}

	public void reportlost(Patron patron, Attachment attachment, LocalDate given) throws LibraryException {
		
		patron.reportLost(attachment, given);
		this.patronRepository.save(patron);

	}

	public void payFine(Patron patron, Long[] ids, BigDecimal payAmount, LocalDate given) throws LibraryException {
		
		patron.payFine(ids, payAmount, given, holidayStrategy);
		this.patronRepository.save(patron);
		
	}
	
	public CheckoutResult returnItem(Item item, LocalDate given, boolean damage) throws LibraryException {
		
		CheckoutResult result = item.checkIn(given, damage);
		
		Reservation r = setReservationPriorityAndItemState(item, given);
		if(r != null) {
			result.setReservation(r);
		}
		
		this.itemRepository.save(item);
		
		return result;
		
	}

	public CheckoutResult returnAttachment(Attachment attachment, LocalDate given, boolean damage) throws LibraryException {
		
		CheckoutResult result = attachment.checkin(given, damage);
		this.attachmentRepository.save(attachment);
		
		return result;
		
	}
	
	public ReservationResult reserve(Patron patron, Biblio biblio, LocalDateTime given, LocalDate expiryDate) throws LibraryException {

		ReservationResult result = patron.reserve(biblio, given, expiryDate);
		this.patronRepository.save(patron);
		
		return result;
		
	}
	
	private Reservation createNewReservation(Biblio biblio, LocalDate given) {
		
		Reservation candidate = null;
		
		List<Reservation> reservations = this.reservationRepository.findByBiblioIdAndStates(biblio.getId(),
				new ArrayList<ReservationState>(EnumSet.of(ReservationState.RESERVE)));
		
		if(reservations.size() > 0) {
			
			Collections.sort(reservations,new ReservationComparator());
			
			candidate = reservations.get(0);
			candidate.setAvailableDate(given);
			candidate.setState(ReservationState.AVAILABLE);
		}
		
		return candidate;
	}
	
	public void cancelReservation(Patron patron, long reservationId, LocalDate cancelDate) throws LibraryException {
		
		Reservation cancel = patron.cancelReservation(cancelDate, reservationId);		
		Biblio b = cancel.getBiblio();
		
		Reservation candidate = createNewReservation(b, cancelDate);
		if(candidate != null) {
			this.reservationRepository.save(candidate);
		}
		
		this.patronRepository.save(patron);
	}
	
	public void extendReservation(Patron patron, long reservationId, long extendDays) throws LibraryException {
		
		Reservation extend = patron.extendReservation(extendDays, reservationId);
		if(extend != null) {
			this.reservationRepository.save(extend);
		}
	}
	
	// Account
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public void save(Account entity) {
		this.accountRepository.save(entity);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public void edit(Account entity) {
		this.accountRepository.save(entity);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public void delete(Account entity) {
		this.accountRepository.delete(entity);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public void deleteAccount(long id) {
		this.accountRepository.deleteById(id);
	}
	public Account getAccount(long id) {
		return this.accountRepository.getOne(id);
	}
	public Account findByCode(String code) {
		return this.accountRepository.findByCode(code);
	}
	public List<Account> findByCodeContaining(String code) {
		return this.accountRepository.findByCodeContaining(code);
	}
	public List<Account> findAllAccount(Sort sort){
		return this.accountRepository.findAll(sort);
	}
	public Page<Account> findAllAccount(Pageable pageable){
		return this.accountRepository.findAll(pageable);
	}
	public Page<Account> findByCriteria(AccountCriteria criteria, Pageable pageable) {
		return this.accountRepository.findByCriteria(criteria, pageable);
	}
	
	// Attachment
	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_LIBRARIAN')")
	public void save(Attachment entity){
		entity.setLastCheckin(entity.getFirstCheckin());
		this.attachmentRepository.save(entity);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_LIBRARIAN')")
	public void edit(Attachment entity){
		this.attachmentRepository.save(entity);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_LIBRARIAN')")
	public void delete(Attachment entity){
		this.attachmentRepository.delete(entity);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_LIBRARIAN')")
	public void deleteAttachment(long id){
		this.attachmentRepository.deleteById(id);
	}
	public Attachment getAttachment(long id){
		return this.attachmentRepository.getOne(id);
	}
	public Attachment getAttachment(String barcode){
		return this.attachmentRepository.findByBarcode(barcode);
	}
/*	public Attachment loadAttachment(long id){
		return this.em.getReference(Attachment.class, id);
	}*/
	public List<Attachment> findAllAttachments(Sort sort){
		return this.attachmentRepository.findAll(sort);
	}
	public Page<Attachment> findAllAttachments(Pageable pageable){
		return this.attachmentRepository.findAll(pageable);
	}
	public Page<Attachment> findByBarcode(String barcode, Pageable pageable){
		return this.attachmentRepository.findByBarcode(barcode, pageable);
	}
	public Page<Attachment> findByDescriptionContaining(String desp, Pageable pageable){
		return this.attachmentRepository.findByDescriptionContaining(desp, pageable);
	}
	public Page<Attachment> findByCriteria(AttachmentCriteria criteria, Pageable pageable){
		return this.attachmentRepository.findByCriteria(criteria, pageable);
	}
	
	public Attachment getAttachmentForCheckout(String barcode){
		
		Attachment attachment = this.attachmentRepository.findByBarcode(barcode);
		if(attachment == null)
			return null;
		
		Item item = attachment.getItem();
		String itemBarcode = item.getBarcode();
		
		CheckoutState[] cStates = {
				CheckoutState.CHECKOUT
		};
		
		List<Checkout> checkouts = this.checkoutRepository.findByBarcodeAndFilterByStates(itemBarcode, Arrays.asList(cStates));
		item.setCheckouts(checkouts);

		return attachment;
	}
	
	public Attachment getAttachmentForCheckin(String barcode){
		
		Attachment attachment = this.attachmentRepository.findByBarcode(barcode);
		if(attachment == null)
			return null;
		
		List<AttachmentCheckout> attachmentCheckouts = this.attachmentCheckoutRepository.findByBarcodeAndFilterByStates(barcode,AttachmentCheckoutState.CHECKOUT);

		attachment.setAttachmentCheckouts(attachmentCheckouts);

		return attachment;
	}
	
	/****************************************  AttachmentCheckout  *******************************/
	
	public void save(AttachmentCheckout entity){
		this.attachmentCheckoutRepository.save(entity);
	}
	public void delete(AttachmentCheckout entity){
		this.attachmentCheckoutRepository.delete(entity);
	}
	public void deleteAttachmentCheckout(long id){
		this.attachmentCheckoutRepository.deleteById(id);
	}
	public AttachmentCheckout getAttachmentCheckout(long id){
		return this.attachmentCheckoutRepository.getOne(id);
	}
	public List<AttachmentCheckout> findAllAttachmentCheckouts(Sort sort){
		return this.attachmentCheckoutRepository.findAll(sort);
	}
	public Page<AttachmentCheckout> findAllAttachmentCheckouts(Pageable pageable){
		return this.attachmentCheckoutRepository.findAll(pageable);
	}
	public Page<AttachmentCheckout> findByCriteria(AttachmentCheckoutCriteria criteria, Pageable pageable){
		return this.attachmentCheckoutRepository.findByCriteria(criteria, pageable);
	}
	
	/****************************************  Biblio  *****************************************/
	
	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_LIBRARIAN')")
	public void save(Biblio entity){
		
		Publisher p = entity.getPublisher();
		
		if(p != null) {
			
			String pname = p.getName();
			
			if(pname != null) {
				Publisher publisher = this.publisherRepository.findByName(pname);
				
				if(publisher != null) {
					entity.setPublisher(publisher);
				}else{
					this.publisherRepository.save(p);
					entity.setPublisher(p);
				}
			}else {
				entity.setPublisher(null);
			}
			
		}
		
		Series s = entity.getSeries();
		
		if(s != null) {
			
			String sname = s.getName();	
			
			if(sname != null) {
				Series series = this.seriesRepository.findByName(sname);
				
				if(series != null) {
					entity.setSeries(series);
				}else {
					this.seriesRepository.save(s);
					entity.setSeries(s);
				}
			}else{
				entity.setSeries(null);
			}
		}
		
		this.biblioRepository.save(entity);
		
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_LIBRARIAN')")
	public void edit(Biblio entity){
		this.biblioRepository.save(entity);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_LIBRARIAN')")
	public void delete(Biblio entity){
		this.biblioRepository.delete(entity);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_LIBRARIAN')")
	public void deleteBiblio(long id){
		this.biblioRepository.deleteById(id);
	}
	public Biblio getBiblio(long id){
		return this.biblioRepository.getOne(id);
	}
	public Biblio findByBiblioId(long id){
		return this.biblioRepository.findById(id);
	}
	public Biblio getBiblioFetchItems(long id){
		return this.biblioRepository.getBiblioFetchItems(id);
	}
	public Biblio findByIsbn(String isbn) {
		return this.biblioRepository.findByIsbn(isbn);
	}
	public List<Biblio> findAllBiblios(Sort sort){
		return this.biblioRepository.findAll(sort);
	}
	public Page<Biblio> findAllBiblios(Pageable pageable){
		return this.biblioRepository.findAll(pageable);
	}
	public Page<Biblio> findAllUncompleteBiblios(Pageable pageable){
		return this.biblioRepository.findAllUncompleteBiblios(pageable);
	}
	public Page<Biblio> findByTitleAndIsbnAndNoteContaining(String title, Pageable pageable){
		return this.biblioRepository.findByTitleAndIsbnAndNoteContaining(title, pageable);
	}
	public Page<Biblio> findByCriteria(BiblioCriteria criteria, Pageable pageable){
		return this.biblioRepository.findByCriteria(criteria, pageable);
	}
	public Page<Biblio> findByOrCriteria(String  keyword, Pageable pageable){
		return this.biblioRepository.findByOrCriteria(keyword, pageable);
	}
	/****************************************  Checkout  ******************************************/
	
	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_LIBRARIAN')")
	public void save(Checkout entity){
		this.checkoutRepository.save(entity);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_LIBRARIAN')")
	public void update(Checkout entity){
		this.checkoutRepository.save(entity);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_LIBRARIAN')")
	public void delete(Checkout entity){
		this.checkoutRepository.delete(entity);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_LIBRARIAN')")
	public void deleteCheckout(long id){
		this.checkoutRepository.deleteById(id);
	}
	public Checkout getCheckout(long id){
		return this.checkoutRepository.getOne(id);
	}
	public List<Checkout> findAllCheckouts(Sort sort){
		return this.checkoutRepository.findAll(sort);
	}
	public List<Checkout> findByCardKeyFetchItemBiblio(String cardKey){
		return this.checkoutRepository.findByCardKeyFetchItemBiblio(cardKey);
	}
	public List<Checkout> findByBarcodeAndFilterByStates(String barcode, List<CheckoutState> cStates, List<AttachmentCheckoutState> acStates){
		return this.checkoutRepository.findByBarcodeAndFilterByStates(barcode, cStates, acStates);
	}
	public List<Checkout> findAllActiveCheckoutsByCardKey(String cardKey){
		return this.checkoutRepository.findAllActiveCheckoutsByCardKey(cardKey);
	}
	public Page<Checkout> findAllCheckouts(Pageable pageable){
		
		final LocalDate now = LocalDate.now();

		Page<Checkout> checkoutsPage = this.checkoutRepository.findAll(pageable);
		
		for(Checkout c : checkoutsPage.getContent()){
			c.preparingCheckoutOn(now);
		}
		
		return checkoutsPage;
	}
	public Page<Checkout> findAllCheckouts(String username, List<CheckoutState> cStates, Pageable pageable){		
		return this.checkoutRepository.findAllCheckouts(username, cStates, pageable);
	}
	
	public Page<Checkout> findByCriteria(CheckoutCriteria criteria, Pageable pageable){
		
		final LocalDate now = LocalDate.now();
		
		Page<Checkout> checkoutsPage = this.checkoutRepository.findByCriteria(criteria, pageable);
		
		for(Checkout c : checkoutsPage.getContent()){
			c.preparingCheckoutOn(now);
		}
		
		return checkoutsPage;
	}
	
	public List<String> allOverDuePatrons(LocalDate given, int reminderDays){
		return this.checkoutRepository.allOverDuePatrons(given, reminderDays);
	}
	
	public List<Checkout> patronOverDues(String cardKey, LocalDate given, int reminderDays){
		return this.checkoutRepository.patronOverDues(cardKey, given, reminderDays);
	}

	/************************************  FormData  *************************************/
	
	public void save(FormData entity) {
		this.formDataRepository.save(entity);
	}
	public void delete(FormData entity){
		this.formDataRepository.delete(entity);
	}
	public void deleteFormData(long id){
		this.formDataRepository.deleteById(id);
	}
	public FormData getFormData(long id){
		return this.formDataRepository.getOne(id);
	}
	public List<FormData> findAllFormDatas(){
		return this.formDataRepository.findAll();
	}
	public List<FormData> findAllFormDatas(Sort sort){
		return this.formDataRepository.findAll(sort);
	}
	public Page<FormData> findAllFormDatas(Pageable pageable){
		return this.formDataRepository.findAll(pageable);
	}
	public Page<FormData> findByDataContainingAndType(String data, DataType type, Pageable pageable){
		return this.formDataRepository.findByDataContainingAndType(data, type, pageable);
	}
	
	/************************************  Group  *************************************/
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public void save(Group entity){
		this.groupRepository.save(entity);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public void delete(Group entity){
		this.groupRepository.delete(entity);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public void deleteGroup(long id){
		this.groupRepository.deleteById(id);
	}
	public Group getGroup(long id){
		return this.groupRepository.getOne(id);
	}
	public List<Group> findAllGroups(){
		return this.groupRepository.findAll();
	}
	public List<Group> findAllGroups(Sort sort){
		return this.groupRepository.findAll(sort);
	}
	public Page<Group> findAllGroups(Pageable pageable){
		return this.groupRepository.findAll(pageable);
	}
	public Page<Group> findByCodeOrNameContaining(String keyword, Pageable pageable){
		return this.groupRepository.findByCodeOrNameContaining(keyword, pageable);
	}
	
	/****************************************  Holiday  *****************************************/
	
	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_LIBRARIAN')")
	public void save(Holiday entity){
		this.holidayRepository.save(entity);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_LIBRARIAN')")
	public void delete(Holiday entity){
		this.holidayRepository.delete(entity);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_LIBRARIAN')")
	public void deleteHoliday(long id){
		this.holidayRepository.deleteById(id);
	}
	public Holiday getHoliday(long id){
		return this.holidayRepository.getOne(id);
	}
	public Holiday getHolidayByDueDate(LocalDate dueDate){
		return this.holidayRepository.getHolidayByDueDate(dueDate);
	}
	public Holiday getHolidayByStartAndEndDate(LocalDate start, LocalDate end){
		return this.holidayRepository.getHolidayByStartAndEndDate(start, end);
	}
	public List<Holiday> findByInBetween(LocalDate start , LocalDate end){
		return this.holidayRepository.findByInBetween(start, end);
	}
	public List<Holiday> findByStartDateAfterAndFine(LocalDate dueDate, Boolean fine){
		return this.holidayRepository.findByStartDateAfterAndFine(dueDate, fine);
	}
	public List<Holiday> findByInBetweenAndFine(LocalDate start, LocalDate end, Boolean fine){
		return this.holidayRepository.findByInBetweenAndFine(start, end, fine);
	}
	public List<Holiday> findByExcluded(LocalDate start , LocalDate end){
		return this.holidayRepository.findByExcluded(start, end);
	}
	public List<Holiday> findAllHolidays(Sort sort){
		return this.holidayRepository.findAll(sort);
	}
	public Page<Holiday> findAllHolidays(Pageable pageable){
		return this.holidayRepository.findAll(pageable);
	}
	public Page<Holiday> findByCriteria(HolidayCriteria criteria, Pageable pageable){
		return this.holidayRepository.findByCriteria(criteria, pageable);
	}

	/****************************************  Item  ********************************************/
	
	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_LIBRARIAN')")
	public void save(Item entity){
		entity.setState(ItemState.IN_LIBRARY);
		this.itemRepository.save(entity);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_LIBRARIAN')")
	public void edit(Item entity){
		
		Item origin = this.itemRepository.getOne(entity.getId());
		ItemState state = origin.getState();
		entity.setState(state);
		
		this.itemRepository.save(entity);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_LIBRARIAN')")
	public void delete(Item entity){
		this.itemRepository.delete(entity);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_LIBRARIAN')")
	public void deleteItem(long id){
		this.itemRepository.deleteById(id);
	}
	public Item getItem(long id){
		return this.itemRepository.getOne(id);
	}
	public Item getItemFetchBiblio(String barcode){
		return this.itemRepository.getItemFetchBiblio(barcode);
	}
	public Item getItemFetchItemRelated(long id){
		return this.itemRepository.getItemFetchItemRelated(id);
	}
	public Item getItemFetchItemStatus(String barcode){
		return this.itemRepository.getItemFetchItemStatus(barcode);
	}
	public Item getItemFetchRelatedBiblio(String barcode){
		return this.itemRepository.getItemFetchRelatedBiblio(barcode);
	}
	public Item findByBarcode(String barcode) {
		return this.itemRepository.findByBarcode(barcode);
	}
	public List<Item> findItemsByBiblioId(Long id){
		return this.itemRepository.findItemsByBiblioId(id);
	}
	public List<Item> findItemsByIsbnAndStates(String isbn, String[] states) {
		return this.itemRepository.findItemsByIsbnAndStates(isbn, states);
	}
	public List<Item> findAllItems(Sort sort){
		return this.itemRepository.findAll(sort);
	}
	public List<Item> findAllItemsOrderByBarcode(long id){
		return this.itemRepository.findAllItemsOrderByBarcode(id);
	}
	public Page<Item> findAllItems(Pageable pageable){
		return this.itemRepository.findAll(pageable);
	}
	public Page<Item> findAllItemsByKeyword(String keyword, Pageable pageable){
		return this.itemRepository.findAllByKeyword(keyword, pageable);
	}
	public Page<Item> findByCriteria(ItemCriteria criteria, Pageable pageable){
		return this.itemRepository.findByCriteria(criteria, pageable);
	}
	
	public Item getItemForCheckout(String barcode){
		
		Item item = this.itemRepository.getItemFetchRelatedBiblio(barcode);
		if(item == null)
			return null;
		
		List<Reservation> brs = this.findByBiblioIdAndStates(item.getBiblio().getId());
		item.getBiblio().setReservations(brs);
		
		return item;
	}
	
	public Item getItemForCheckin(String barcode){
		
		String[] checkinStates = {
				ItemState.CHECKOUT.toString(),
				ItemState.REPORTLOST.toString()
		};
		
		Item item = this.itemRepository.getItemForCheckin(barcode, checkinStates);
		if(item == null)
			return null;
		
		CheckoutState[] cStates = {
				CheckoutState.CHECKOUT,
				CheckoutState.RENEW,
				CheckoutState.REPORTLOST
		};
		
		// only ONE checkout can be found in the states above !
		List<Checkout> checkouts = this.checkoutRepository.findByBarcodeAndFilterByStates(barcode, Arrays.asList(cStates));
		Checkout c = checkouts.get(0);
		Long id = c.getId();
		
		List<AttachmentCheckout> acs = this.attachmentCheckoutRepository.findByCheckoutId(id, AttachmentCheckoutState.CHECKOUT);
		c.setAttachmentCheckouts(acs);

		item.setCheckouts(checkouts);
		
		List<Reservation> brs = this.findByBiblioIdAndStates(item.getBiblio().getId());
		item.getBiblio().setReservations(brs);
	
		return item;
	}
	
	public void stockCheck() {
		this.itemRepository.stockCheck();
	}
	
	/****************************************  ItemStatus  **************************************/
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public void save(ItemStatus entity){
		this.itemStatusRepository.save(entity);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public void delete(ItemStatus entity){
		this.itemStatusRepository.delete(entity);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public void deleteItemStatus(long id){
		this.itemStatusRepository.deleteById(id);
	}
	public ItemStatus getItemStatus(long id){
		return this.itemStatusRepository.getOne(id);
	}
	public List<ItemStatus> findAllItemStatus(){
		return this.itemStatusRepository.findAll();
	}
	public List<ItemStatus> findAllItemStatus(Sort sort){
		return this.itemStatusRepository.findAll(sort);
	}
	public Page<ItemStatus> findAllItemStatusByName(String name, Pageable pageable){
		return this.itemStatusRepository.findAllByName(name, pageable);
	}

	/*************************************  JournalEntry  ************************************/
	
	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_LIBRARIAN')")
	public void save(JournalEntry entity) {
		this.journalEntryRepository.save(entity);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_LIBRARIAN')")
	public void delete(JournalEntry entity){
		this.journalEntryRepository.delete(entity);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_LIBRARIAN')")
	public void deleteJournalEntry(long id){
		this.journalEntryRepository.deleteById(id);
	}
	public JournalEntry getJournalEntry(long id){
		return this.journalEntryRepository.getOne(id);
	}
	public List<JournalEntry> findAllJournalEntries(){
		return this.journalEntryRepository.findAll();
	}
	public List<JournalEntry> findAllJournalEntries(Sort sort){
		return this.journalEntryRepository.findAll(sort);
	}
	public Page<JournalEntry> findAllJournalEntries(Pageable pageable){
		return this.journalEntryRepository.findAll(pageable);
	}
	public Page<JournalEntry> findByCriteria(JournalEntryCriteria criteria, Pageable pageable){
		return this.journalEntryRepository.findByCriteria(criteria, pageable);
	}
	
	/*************************************  JournalEntryLine  ************************************/
	
	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_LIBRARIAN')")
	public void save(JournalEntryLine entity) {
		this.journalEntryLineRepository.save(entity);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_LIBRARIAN')")
	public void delete(JournalEntryLine entity){
		this.journalEntryLineRepository.delete(entity);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_LIBRARIAN')")
	public void deleteJournalEntryLine(long id){
		this.journalEntryLineRepository.deleteById(id);
	}
	public JournalEntryLine getJournalEntryLine(long id){
		return this.journalEntryLineRepository.getOne(id);
	}
	public List<JournalEntryLine> findAllJournalEntryLines(){
		return this.journalEntryLineRepository.findAll();
	}
	public List<JournalEntryLine> findAllJournalEntryLines(Sort sort){
		return this.journalEntryLineRepository.findAll(sort);
	}
	public Page<JournalEntryLine> findAllJournalEntryLines(Pageable pageable){
		return this.journalEntryLineRepository.findAll(pageable);
	}
	public Page<JournalEntryLine> findByCriteria(JournalEntryLineCriteria criteria, Pageable pageable){
		return this.journalEntryLineRepository.findByCriteria(criteria, pageable);
	}
	
	/****************************************  Location  ****************************************/
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public void save(Location entity){
		this.locationRepository.save(entity);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public void delete(Location entity){
		this.locationRepository.delete(entity);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public void deleteLocation(long id){
		this.locationRepository.deleteById(id);
	}
	public Location getLocation(long id){
		return this.locationRepository.getOne(id);
	}
	public List<Location> findAllLocations(){
		return this.locationRepository.findAll();
	}
	public List<Location> findAllLocations(Sort sort){
		return this.locationRepository.findAll(sort);
	}
	public Page<Location> findAllLocationsByName(String name, Pageable pageable){
		return this.locationRepository.findAllByName(name, pageable);
	}

	/****************************************  Patron  *********************************/

	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_LIBRARIAN')")
	public void save(Patron entity){
		this.patronRepository.save(entity);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_LIBRARIAN')")
	public void edit(Patron entity){
		this.patronRepository.save(entity);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_LIBRARIAN')")
	public void delete(Patron entity){
		this.patronRepository.delete(entity);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_LIBRARIAN')")
	public void deletePatron(long id){
		this.patronRepository.deleteById(id);
	}
	public Patron getPatron(long id){
		return this.patronRepository.getOne(id);
	}
	public Patron findByCardKey(String cardKey){
		return this.patronRepository.findByCardKey(cardKey);
	}
	public Patron findByEntangled(String entangled){
		return this.patronRepository.findByEntangled(entangled);
	}
	public List<Patron> findAllPatrons(Sort sort){
		return this.patronRepository.findAll(sort);
	}
	public List<Patron> findByCardKeyContaining(String cardKey){
		return this.patronRepository.findByCardKeyContaining(cardKey);
	}
	public List<Patron> findByEntangledContaining(String entangled){
		return this.patronRepository.findByEntangledContaining(entangled);
	}
	public List<Patron> findByCardKeyAndEntangledContaining(String keyword){
		return this.patronRepository.findByCardKeyAndEntangledContaining(keyword);
	}
	public List<Patron> findByNameOrCardKeyContaining(String key, Pageable pageable){
		return this.patronRepository.findByNameOrCardKeyContaining(key,pageable);
	}
	public List<Patron> findByIdIn(Collection<Long> ids){
		return this.patronRepository.findByIdIn(ids);
	}
	public List<Patron> findByGroupAndUpdatedOrderByUpdatedDesc(Group group, LocalDateTime updated){
		return this.patronRepository.findByGroupAndUpdatedOrderByUpdatedDesc(group, updated);
	}
	public List<Patron> expiringMembershipPatrons(LocalDate given, int firstRemind, int secondRemind){
		return this.patronRepository.expiringMembershipPatrons(given, firstRemind, secondRemind);
	}
	public Page<Patron> findAllPatrons(Pageable pageable){
		return this.patronRepository.findAll(pageable);
	}
	public Page<Patron> findByCriteria(PatronCriteria criteria, Pageable pageable){
		return this.patronRepository.findByCriteria(criteria, pageable);
	}
	
	public Patron preparingPatronForCirculation(String cardKey, LocalDate given){

		Patron patron = this.patronRepository.findByCardKeyFetchPatronType(cardKey);	
		if(patron == null) return null;
		
		List<Checkout> checkouts = this.checkoutRepository.findByCardKeyAndFilterByStates(cardKey, CheckoutState.getActives());
		List<Holiday> holidays = this.holidayRepository.findByStartDateAfterAndFine(findEarliestDueDate(checkouts), Boolean.FALSE);

		for(Checkout c : checkouts) {
			c.setHolidays(holidays);
		}
		patron.setCheckouts(checkouts);
	
		List<Reservation> reservations = this.reservationRepository.findByCardKeyAndStates(cardKey, ReservationState.getActives());
		patron.setReservations(reservations);
		
		List<AttachmentCheckout> attachmentCheckouts = this.attachmentCheckoutRepository.findByCardKeyAndFilterByStates(cardKey, AttachmentCheckoutState.CHECKOUT);
		patron.setAttachmentCheckouts(attachmentCheckouts);
		
		patron.preparingCheckoutsOn(given);
		
		return patron;
	}
	
	public boolean match(String cardKey, String mobile) {
		return this.patronRepository.match(cardKey, mobile);
	}
	
	private LocalDate findEarliestDueDate(List<Checkout> checkouts) {
		
		LocalDate dueDate = LocalDate.MAX;
		
		for(Checkout c : checkouts) {
			if(c.getDueDate().isBefore(dueDate)) {
				dueDate = c.getDueDate();
			}	
		}
		return dueDate;
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public int bulkUpdateGroup(List<Long> ids , Group group, LocalDateTime now){
		return this.patronRepository.bulkUpdateGroup(ids, group, now);
	}
	
	/****************************************  PatronType  *********************************/
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public void save(PatronType entity){
		this.patronTypeRepository.save(entity);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public void delete(PatronType entity){
		this.patronTypeRepository.delete(entity);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public void deletePatronType(long id){
		this.patronTypeRepository.deleteById(id);
	}
	public PatronType getPatronType(long id){
		return this.patronTypeRepository.getOne(id);
	}
	public List<PatronType> findAllPatronTypes(Sort sort){
		return this.patronTypeRepository.findAll(sort);
	}
	public List<PatronType> findByNameContaining(String name){
		return this.patronTypeRepository.findByNameContaining(name);
	}
	
	/****************************************  Publisher  ***************************************/ 
	
	public void save(Publisher entity){
		this.publisherRepository.save(entity);
	}
	public void delete(Publisher entity){
		this.publisherRepository.delete(entity);
	}
	public void deletePublisher(long id){
		this.publisherRepository.deleteById(id);
	}
	public Publisher getPublisher(long id){
		return this.publisherRepository.getOne(id);
	}
	public Publisher findPublisherByName(String name) {
		return this.publisherRepository.findByName(name);
	}
	public List<Publisher> findAllPublishers(Sort sort){
		return this.publisherRepository.findAll(sort);
	}
	public List<Publisher> findFirst10PublisherByNameContaining(String name, Sort sort){
		return this.publisherRepository.findFirst10PublisherByNameContainingIgnoreCase(name, sort);
	}
	public Page<Publisher> findAllPublishers(Pageable pageable){
		return this.publisherRepository.findAll(pageable);
	}
	public Page<Publisher> findPublishersByNameContaining(String name, Pageable pageable){
		return this.publisherRepository.findPublishersByNameContaining(name, pageable);
	}

	/****************************************  Reservation  **************************************/ 
	
	public void save(Reservation entity){
		this.reservationRepository.save(entity);
	}
	public void delete(Reservation entity){
		this.reservationRepository.delete(entity);
	}
	public void deleteReservation(long id){
		this.reservationRepository.deleteById(id);
	}
	public Reservation getReservation(long id){
		return this.reservationRepository.getOne(id);
	}
	public List<Reservation> findAllReservations(Sort sort){
		return this.reservationRepository.findAll(sort);
	}
	public List<Reservation> findReservationsByCardKey(String cardKey){
		return this.reservationRepository.findReservationsByCardKey(cardKey);
	}
	public List<Reservation> findByCardKeyAndStates(String cardKey, List<ReservationState> states){
		return this.reservationRepository.findByCardKeyAndStates(cardKey, states);
	}
	public List<Reservation> findReservationsByCardKeyFetchBiblioReservations(String cardKey){
		return this.reservationRepository.findReservationsByCardKeyFetchBiblioReservations(cardKey);
	}
	
	public List<Reservation> findByBiblioIdAndStates(long id){
		
		ReservationState[] activeStates = {
				ReservationState.RESERVE,
				ReservationState.AVAILABLE,
				ReservationState.NOTIFIED
		};
		
		return this.reservationRepository.findByBiblioIdAndStates(id,Arrays.asList(activeStates));
	}
	
	public Page<Reservation> findAllReservations(Pageable pageable){
		return this.reservationRepository.findAll(pageable);
	}
	
	public Page<Reservation> findByCriteria(ReservationCriteria criteria, Pageable pageable){
		return this.reservationRepository.findByCriteria(criteria, pageable);
	}
	
	public List<Reservation> findAvailableReservations(){
		return this.reservationRepository.findAvailableReservations();
	}
	
	public void refreshReservationStates(){
		logger.info("*** REFRESHING RESERVATIONS' STATE *** : " + LocalDate.now());
		this.reservationRepository.refreshReservationStates();
	}
	
	/****************************************  Role  **************************************/ 
	
	public void save(Role entity){
		this.roleRepository.save(entity);
	}
	public void delete(Role entity){
		this.roleRepository.delete(entity);
	}
	public void deleteRole(long id){
		this.roleRepository.deleteById(id);
	}
	public Role findRoleByName(String name){
		return this.roleRepository.findByName(name);
	}
	
	/****************************************  Series  *******************************************/
	
	public void save(Series entity){
		this.seriesRepository.save(entity);
	}
	public void delete(Series entity){
		this.seriesRepository.delete(entity);
	}
	public void deleteSeries(long id){
		this.seriesRepository.deleteById(id);
	}
	public Series getSeries(long id){
		return this.seriesRepository.getOne(id);
	}
	public Series findSeriesByName(String name) {
		return this.seriesRepository.findByName(name);
	}
	public List<Series> findAllSeries(Sort sort){
		return this.seriesRepository.findAll(sort);
	}
	public List<Series> findFirst10SeriesByNameContaining(String name, Sort sort){
		return this.seriesRepository.findFirst10ByNameContainingIgnoreCase(name,sort);
	}
	public Page<Series> findAllSeries(Pageable pageable){
		return this.seriesRepository.findAll(pageable);
	}
	public Page<Series> findSeriesByNameContaining(String name, Pageable pageable){
		return this.seriesRepository.findByNameContainingIgnoreCase(name, pageable);
	}
	
}