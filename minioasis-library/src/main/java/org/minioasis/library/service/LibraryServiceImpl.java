package org.minioasis.library.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
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
import org.minioasis.library.domain.Image;
import org.minioasis.library.domain.Photo;
import org.minioasis.library.domain.Item;
import org.minioasis.library.domain.ItemDuration;
import org.minioasis.library.domain.ItemState;
import org.minioasis.library.domain.ItemStatus;
import org.minioasis.library.domain.Patron;
import org.minioasis.library.domain.PatronType;
import org.minioasis.library.domain.Location;
import org.minioasis.library.domain.Publisher;
import org.minioasis.library.domain.Reservation;
import org.minioasis.library.domain.ReservationResult;
import org.minioasis.library.domain.ReservationState;
import org.minioasis.library.domain.Series;
import org.minioasis.library.domain.search.AttachmentCheckoutCriteria;
import org.minioasis.library.domain.search.AttachmentCriteria;
import org.minioasis.library.domain.search.BiblioCriteria;
import org.minioasis.library.domain.search.CheckoutCriteria;
import org.minioasis.library.domain.search.HolidayCriteria;
import org.minioasis.library.domain.search.ItemCriteria;
import org.minioasis.library.domain.search.PatronCriteria;
import org.minioasis.library.domain.search.ReservationCriteria;
import org.minioasis.library.exception.LibraryException;
import org.minioasis.library.repository.AttachmentCheckoutRepository;
import org.minioasis.library.repository.AttachmentRepository;
import org.minioasis.library.repository.BiblioRepository;
import org.minioasis.library.repository.CheckoutRepository;
import org.minioasis.library.repository.GroupRepository;
import org.minioasis.library.repository.HolidayRepository;
import org.minioasis.library.repository.ItemDurationRepository;
import org.minioasis.library.repository.ItemRepository;
import org.minioasis.library.repository.ItemStatusRepository;
import org.minioasis.library.repository.PatronRepository;
import org.minioasis.library.repository.PatronTypeRepository;
import org.minioasis.library.repository.LocationRepository;
import org.minioasis.library.repository.PublisherRepository;
import org.minioasis.library.repository.ReservationRepository;
import org.minioasis.library.repository.SeriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
public class LibraryServiceImpl implements LibraryService {
	
	@Autowired
	private AttachmentRepository attachmentRepository;
	@Autowired
	private AttachmentCheckoutRepository attachmentCheckoutRepository;
	@Autowired
	private BiblioRepository biblioRepository;
	@Autowired
	private CheckoutRepository checkoutRepository;
	@Autowired
	private GroupRepository groupRepository;	
	@Autowired
	private HolidayRepository holidayRepository;
	@Autowired
	private ItemRepository itemRepository;
	@Autowired
	private ItemDurationRepository itemDurationRepository;
	@Autowired
	private ItemStatusRepository itemStatusRepository;
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
	private SeriesRepository seriesRepository;
	@Autowired
	private HolidayCalculationStrategy holidayStrategy;

	/****************************************  Business Logic **************************************/	
	
	private LocalDate calculateDueDate(Patron patron, Item item, LocalDate given) {
		
		long duration = patron.getPatronType().getDuration().longValue();
		long itemDuration = item.getItemDuration().getValue().longValue();
		
		return given.plusDays(duration + itemDuration);
	}
	
	public void checkout(Patron patron, Item item, LocalDate given) throws LibraryException {
	
		LocalDate dueDate = calculateDueDate(patron, item, given);
		LocalDate newDueDate = holidayStrategy.getNewDueDateAfterHolidays(dueDate);
		
		patron.preparingCheckoutsOn(given);
		patron.checkout(item, given, newDueDate);

		this.patronRepository.save(patron);

	}

	public void renew(Patron patron, Item item, LocalDate given) throws LibraryException {
		
		LocalDate dueDate = calculateDueDate(patron,item,given);
		LocalDate newDueDate = holidayStrategy.getNewDueDateAfterHolidays(dueDate);
		
		patron.preparingCheckoutsOn(given);
		patron.renew(item, given, newDueDate);
		
		this.patronRepository.save(patron);
		
	}
	
	public void checkoutAttachment(Patron patron, Attachment attachment, LocalDate given){

		patron.checkout(attachment, given);
		this.patronRepository.save(patron);
		
	}
	
	public CheckoutResult checkin(Patron patron, Item item, LocalDate given, boolean damage, boolean renew) throws LibraryException {
		
		CheckoutResult result = patron.checkin(item, given, damage, renew, holidayStrategy);
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
	
	public void cancelReservation(Patron patron, long reservationId, LocalDate cancelDate) throws LibraryException {
		patron.cancelReservation(cancelDate, reservationId);
	}
	
	// Attachment
	public void save(Attachment entity){
		entity.setLastCheckin(entity.getFirstCheckin());
		this.attachmentRepository.save(entity);
	}
	public void edit(Attachment entity){
		this.attachmentRepository.save(entity);
	}
	public void delete(Attachment entity){
		this.attachmentRepository.delete(entity);
	}
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
	
	public void save(Biblio entity){
		this.biblioRepository.save(entity);
	}
	public void edit(Biblio entity){
		long id = entity.getId();
		Biblio existingbiblio = this.biblioRepository.getOne(id);
		Image image = existingbiblio.getImage();
		entity.setImage(image);
		this.biblioRepository.save(entity);
	}
	public void delete(Biblio entity){
		this.biblioRepository.delete(entity);
	}
	public void deleteBiblio(long id){
		this.biblioRepository.deleteById(id);
	}
	public boolean upload(Biblio biblio){

		Image imagePost = biblio.getImage();
		byte[] img = imagePost.getImg();
		int size = 0;
		
		if (img != null){
			 size = img.length;
		}		
		
		try{
			
			Biblio existingBiblio = this.biblioRepository.getOne(biblio.getId());
			Image imageFromDb = existingBiblio.getImage();
			
			if(imageFromDb != null){
				if(imagePost != null){
					imagePost.setId(imageFromDb.getId());
				}		
			}
			
			imagePost.setSize(size);
			existingBiblio.setImage(imagePost);
			
			this.biblioRepository.save(existingBiblio);
			return true;
			
		} catch (Exception e){
			// TODO
		}
		
		return false;
	}
	public Biblio getBiblio(long id){
		return this.biblioRepository.getOne(id);
	}
	public Biblio getBiblioDetails(long id){
		return this.biblioRepository.getBiblioDetails(id);
	}
	public Biblio getBiblioFetchItems(long id){
		return this.biblioRepository.getBiblioFetchItems(id);
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
	
	/****************************************  Checkout  ******************************************/
	
	public void save(Checkout entity){
		this.checkoutRepository.save(entity);
	}
	public void update(Checkout entity){
		this.checkoutRepository.save(entity);
	}
	public void delete(Checkout entity){
		this.checkoutRepository.delete(entity);
	}
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
	// Report
	public Page<Checkout> findAllOverDue(List<CheckoutState> cStates, LocalDate given, Pageable pageable){
		
		Page<Checkout> page = this.checkoutRepository.findAllOverDue(cStates, given, pageable);
		return page;
	}
	
	public Page<Checkout> findByCriteria(CheckoutCriteria criteria, Pageable pageable){
		return this.checkoutRepository.findByCriteria(criteria, pageable);
	}
	
	/****************************************  Group  *****************************************/
	
	public void save(Group entity){
		this.groupRepository.save(entity);
	}
	public void delete(Group entity){
		this.groupRepository.delete(entity);
	}
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
	
	public void save(Holiday entity){
		this.holidayRepository.save(entity);
	}
	public void delete(Holiday entity){
		this.holidayRepository.delete(entity);
	}
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
	
	public void save(Item entity){
		entity.setState(ItemState.IN_LIBRARY);
		this.itemRepository.save(entity);
	}
	public void edit(Item entity){
		
		Item origin = this.itemRepository.getOne(entity.getId());
		ItemState state = origin.getState();
		entity.setState(state);
		
		this.itemRepository.save(entity);
	}
	public void delete(Item entity){
		this.itemRepository.delete(entity);
	}
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
		
		List<Reservation> brs = this.findReservationsByBiblioIdAndActiveStates(item.getBiblio().getId());
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
		
		List<Reservation> brs = this.findReservationsByBiblioIdAndActiveStates(item.getBiblio().getId());
		item.getBiblio().setReservations(brs);
	
		return item;
	}
	
	public void stockCheck() {
		this.itemRepository.stockCheck();
	}
	
	/****************************************  ItemDuration  ************************************/
	
	public void save(ItemDuration entity){
		this.itemDurationRepository.save(entity);
	}
	public void delete(ItemDuration entity){
		this.itemDurationRepository.delete(entity);
	}
	public void deleteItemDuration(long id){
		this.itemDurationRepository.deleteById(id);
	}
	public ItemDuration getItemDuration(long id){
		return this.itemDurationRepository.getOne(id);
	}
	public List<ItemDuration> findAllItemDurations(){
		return this.itemDurationRepository.findAll();
	}
	public List<ItemDuration> findAllItemDurations(Sort sort){
		return this.itemDurationRepository.findAll(sort);
	}
	public Page<ItemDuration> findAllItemDurationsByName(String name, Pageable pageable){
		return this.itemDurationRepository.findAllByName(name, pageable);
	}
	
	/****************************************  ItemStatus  **************************************/
	
	public void save(ItemStatus entity){
		this.itemStatusRepository.save(entity);
	}
	public void delete(ItemStatus entity){
		this.itemStatusRepository.delete(entity);
	}
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
	
	/****************************************  Location  ****************************************/
	
	public void save(Location entity){
		this.locationRepository.save(entity);
	}
	public void delete(Location entity){
		this.locationRepository.delete(entity);
	}
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

	public void save(Patron entity){
		this.patronRepository.save(entity);
	}
	public void edit(Patron entity){
		long id = entity.getId();
		Patron existingPatron = this.patronRepository.getOne(id);
		Photo photo = existingPatron.getPhoto();
		entity.setPhoto(photo);
		this.patronRepository.save(entity);
	}
	public void delete(Patron entity){
		this.patronRepository.delete(entity);
	}
	public void deletePatron(long id){
		this.patronRepository.deleteById(id);
	}
	public Patron getPatron(long id){
		return this.patronRepository.getOne(id);
	}
	public boolean upload(Patron patron) {
		
		Photo photoPost = patron.getPhoto();
		byte[] img = photoPost.getImg();
		int size = 0;
		
		if (img != null){
			 size = img.length;
		}
		
		try{
			
			Patron existingPatron = this.patronRepository.getOne(patron.getId());
			Photo photoFromDB = existingPatron.getPhoto();
			
			if (photoFromDB != null) {
				if (photoPost != null) {
					photoPost.setId(photoFromDB.getId());
				}
			}

			photoPost.setSize(size);
			existingPatron.setPhoto(photoPost);
			
			this.patronRepository.save(existingPatron);
			return true;
			
		}catch (Exception e){
			// TODO
		}
		
		return false;
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
	
		List<Reservation> reservations = this.reservationRepository.findByCardKeyAndFilterByStates(cardKey, ReservationState.getActives());
		patron.setReservations(reservations);
		
		List<AttachmentCheckout> attachmentCheckouts = this.attachmentCheckoutRepository.findByCardKeyAndFilterByStates(cardKey, AttachmentCheckoutState.CHECKOUT);
		patron.setAttachmentCheckouts(attachmentCheckouts);
		
		patron.preparingCheckoutsOn(given);
		
		return patron;
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
	
	public int bulkUpdateGroup(List<Long> ids , Group group, LocalDateTime now){
		return this.patronRepository.bulkUpdateGroup(ids, group, now);
	}
	
	/****************************************  PatronType  *********************************/
	
	public void save(PatronType entity){
		this.patronTypeRepository.save(entity);
	}
	public void delete(PatronType entity){
		this.patronTypeRepository.delete(entity);
	}
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
	public Page<Publisher> findAllPublishers(Pageable pageable){
		return this.publisherRepository.findAll(pageable);
	}
	public List<Publisher> findPublishersyNameContaining(String name){
		return this.publisherRepository.findByNameContainingIgnoreCase(name);
	}
	public Page<Publisher> findPublishersByNameContaining(String name, Pageable pageable){
		return this.publisherRepository.findByNameContainingIgnoreCase(name, pageable);
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
	public List<Reservation> findFilteredReservationsByCardKeyFetchBiblio(String cardKey){
		return this.reservationRepository.findFilteredReservationsByCardKeyFetchBiblio(cardKey);
	}
	
	public List<Reservation> findFilteredReservationsByCardKeyFetchBiblioReservations(String cardKey){
		return this.reservationRepository.findFilteredReservationsByCardKeyFetchBiblioReservations(cardKey);
	}
	
	public List<Reservation> findReservationsByBiblioIdAndActiveStates(long id){
		
		ReservationState[] activeStates = {
				ReservationState.RESERVE,
				ReservationState.AVAILABLE,
				ReservationState.NOTIFIED
		};
		
		return this.reservationRepository.findReservationsByBiblioIdAndStates(id,Arrays.asList(activeStates));
	}
	
	public Page<Reservation> findAllReservations(Pageable pageable){
		return this.reservationRepository.findAll(pageable);
	}
	
	public Page<Reservation> findByCriteria(ReservationCriteria criteria, Pageable pageable){
		return this.reservationRepository.findByCriteria(criteria, pageable);
	}
	
	//Map findReservationsByCriteria(ReservationCmd command);
	//Collection findPageReservationsByCriteria(ReservationCmd command);
	//Long findTotalReservationsByCriteria(ReservationCmd command);
	
	//Map findAvailableBiblioReservationsByBarcode(KeywordCmd keywordCmd);
	//Collection findPageAvailableBiblioReservationsByBarcode(KeywordCmd keywordCmd);
	//Long findTotalAvailableBiblioReservationsByBarcode(KeywordCmd keywordCmd);
	
	//Map findAvailableVolumeReservationsByBarcode(KeywordCmd keywordCmd);
	//Collection findPageAvailableVolumeReservationsByBarcode(KeywordCmd keywordCmd);
	//Long findTotalAvailableVolumeReservationsByBarcode(KeywordCmd keywordCmd);
	
	public void refreshReservationStates(){
		this.reservationRepository.refreshReservationStates();
	}
/*	public void clearExpiredReservations(Date given){
		this.reservationRepository.clearExpiredReservations(given);
	}
	public void clearUncollectedReservations(Date given){
		this.reservationRepository.clearUncollectedReservations(given);
	}
	public void clearPunishedReservations(Date given){
		this.reservationRepository.clearPunishedReservations(given);
	}*/
	
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
	public List<Series> findSeriesByNameContaining(String name){
		return this.seriesRepository.findByNameContainingIgnoreCase(name);
	}
	public Page<Series> findAllSeries(Pageable pageable){
		return this.seriesRepository.findAll(pageable);
	}
	public Page<Series> findSeriesByNameContaining(String name, Pageable pageable){
		return this.seriesRepository.findByNameContainingIgnoreCase(name, pageable);
	}
	
}