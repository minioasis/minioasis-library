package org.minioasis.library.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Filter;
import org.hibernate.validator.constraints.Length;
import org.minioasis.library.exception.LibraryException;
import org.minioasis.library.service.HolidayCalculationStrategy;
import org.minioasis.validation.Notification;

@Entity
@Table(name = "patron")
public class Patron implements Serializable {

	private static final long serialVersionUID = -8094767852594052970L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Long id;

	@NotNull(message = "{notnull}")
	@Column(name = "active", nullable = false, columnDefinition = "CHAR(1) default 'Y' ")
	@Enumerated(EnumType.STRING)
	private YesNo active;

	@NotNull(message = "{notnull}")
	@Length(max = 64)
	private String name;

	@Length(max = 64)
	private String name2;

	@NotNull(message = "{notnull}")
	@Temporal(TemporalType.DATE)
	@Column(name = "start_date", nullable = false)
	private Date startDate;

	@NotNull(message = "{notnull}")
	@Temporal(TemporalType.DATE)
	@Column(name = "end_date", nullable = false)
	private Date endDate;

	@NotNull(message = "{notnull}")
	@Length(max = 16)
	@Column(name = "card_key", unique = true, nullable = false, columnDefinition = "CHAR(16)")
	private String cardKey;

	@NotNull(message = "{notnull}")
	@Length(max = 16)
	@Column(name = "entangled", unique = true, nullable = false, columnDefinition = "CHAR(16)")
	private String entangled;

	@Length(max = 20)
	private String gender;

	@Length(max = 20)
	private String ic;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "group_id", foreignKey = @ForeignKey(name = "fk_patron_group") )
	private Group group;

	@NotNull(message = "{notnull}")
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "patrontype_id", nullable = false, foreignKey = @ForeignKey(name = "fk_patron_patrontype") )
	private PatronType patronType;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "photo_id", unique= true, nullable=true, insertable=true, updatable=true)
	private Photo photo;

	@Valid
	private Contact contact;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created")
	private Date created = new Date();

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "updated")
	private Date updated = new Date();

	@Column(name = "uncollected_no", nullable = false)
	private Short unCollectedNo = 0;

	@Column(name = "order_no")
	private Short orderNo;
	
	@Column(columnDefinition = "TEXT")
	private String note;

	@OneToMany(mappedBy = "patron", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@OrderBy("checkoutDate ASC")
	private List<Checkout> checkouts = new ArrayList<Checkout>(0);

	@OneToMany(mappedBy = "patron", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@OrderBy("reservationDate ASC")
	private List<Reservation> reservations = new ArrayList<Reservation>(0);

	@OneToMany(mappedBy = "patron", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@Filter(name = "attachmentCheckoutStateFilter")
	private List<AttachmentCheckout> attachmentCheckouts = new ArrayList<AttachmentCheckout>(0);

	// global variable
	@Transient
	private long oneDay = 86400000;
	@Transient
	private boolean dateExpired = false;
	@Transient
	private int noOfOverdueBiblios = 0;
	@Transient
	private BigDecimal totalAmountOfFines = new BigDecimal(0);

	public Patron() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public YesNo getActive() {
		return active;
	}

	public void setActive(YesNo active) {
		this.active = active;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName2() {
		return name2;
	}

	public void setName2(String name2) {
		this.name2 = name2;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getCardKey() {
		return cardKey;
	}

	public void setCardKey(String cardKey) {
		this.cardKey = cardKey;
	}

	public String getEntangled() {
		return entangled;
	}

	public void setEntangled(String entangled) {
		this.entangled = entangled;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getIc() {
		return ic;
	}

	public void setIc(String ic) {
		this.ic = ic;
	}

	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}

	public PatronType getPatronType() {
		return patronType;
	}

	public void setPatronType(PatronType patronType) {
		this.patronType = patronType;
	}

	public Photo getPhoto() {
		return photo;
	}

	public void setPhoto(Photo photo) {
		this.photo = photo;
	}

	public Contact getContact() {
		return contact;
	}

	public void setContact(Contact contact) {
		this.contact = contact;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Date getUpdated() {
		return updated;
	}

	public void setUpdated(Date updated) {
		this.updated = updated;
	}

	public Short getUnCollectedNo() {
		return unCollectedNo;
	}

	public void setUnCollectedNo(Short unCollectedNo) {
		this.unCollectedNo = unCollectedNo;
	}

	public Short getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(Short orderNo) {
		this.orderNo = orderNo;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public List<Checkout> getCheckouts() {
		return checkouts;
	}

	public void setCheckouts(List<Checkout> checkouts) {
		this.checkouts = checkouts;
	}

	public List<Reservation> getReservations() {
		return reservations;
	}

	public void setReservations(List<Reservation> reservations) {
		this.reservations = reservations;
	}

	public List<AttachmentCheckout> getAttachmentCheckouts() {
		return attachmentCheckouts;
	}

	public void setAttachmentCheckouts(List<AttachmentCheckout> attachmentCheckouts) {
		this.attachmentCheckouts = attachmentCheckouts;
	}

	public long getOneDay() {
		return oneDay;
	}

	public void setOneDay(long oneDay) {
		this.oneDay = oneDay;
	}

	public boolean isDateExpired() {
		return dateExpired;
	}

	public void setDateExpired(boolean dateExpired) {
		this.dateExpired = dateExpired;
	}

	public int getNoOfOverdueBiblios() {
		return noOfOverdueBiblios;
	}

	public void setNoOfOverdueBiblios(int noOfOverdueBiblios) {
		this.noOfOverdueBiblios = noOfOverdueBiblios;
	}

	public BigDecimal getTotalAmountOfFines() {
		return totalAmountOfFines;
	}

	public void setTotalAmountOfFines(BigDecimal totalAmountOfFines) {
		this.totalAmountOfFines = totalAmountOfFines;
	}

	public void addCheckout(Checkout checkout) {
		if (checkout == null)
			throw new IllegalArgumentException("Can't add a null checkout.");

		this.checkouts.add(checkout);
	}

	public void removeCheckout(Checkout checkout) {
		if (checkout == null)
			throw new IllegalArgumentException("Can't remove a null checkout.");

		this.checkouts.remove(checkout);
	}

	public void addAttachmentCheckout(AttachmentCheckout attachmentCheckout) {
		if (attachmentCheckout == null)
			throw new IllegalArgumentException("Can't add a null attachmentCheckout.");

		this.attachmentCheckouts.add(attachmentCheckout);
	}

	public void removeAttachmentCheckout(AttachmentCheckout attachmentCheckout) {
		if (attachmentCheckout == null)
			throw new IllegalArgumentException("Can't remove a null attachmentCheckout.");

		this.attachmentCheckouts.remove(attachmentCheckout);
	}

	public void addReservation(Reservation reservation) {
		if (reservation == null)
			throw new IllegalArgumentException("Can't add a null reservation.");

		this.reservations.add(reservation);
	}

	public void removeReservation(Reservation reservation) {
		if (reservation == null)
			throw new IllegalArgumentException("Can't remove a null reservation.");

		this.reservations.remove(reservation);
	}

	public Date calculateDueDate(Date checkoutDate, Item item) {

		long duration = this.patronType.getDuration().longValue();
		long itemDuration = item.getItemDuration().getValue().longValue();

		long total = duration + itemDuration;

		Date dueDate = new Date(checkoutDate.getTime() + (total * oneDay));

		return dueDate;
	}

	// ******************** checkout *********************************

	public void checkout(Item item, Date given, Date newDueDate, int holidays, HolidayCalculationStrategy strategy) {

		Notification notification = checkoutValidation(item, given, holidays, strategy);

		if (notification.hasErrors())
			throw new LibraryException(notification.getAllMessages());

		Checkout b = new Checkout(given, null, new Integer(0), CheckoutState.CHECKOUT, this, item);

		b.setDueDate(whichDueDate(b.calculateDueDate(), newDueDate));

		addCheckout(b);

		item.setState(ItemState.CHECKOUT);

		// B
		// clear reservation if any
		stateChangeToCompleted(item,given);

	}

	// ********************* checkout's helping methods ***************

	private Date whichDueDate(Date one, Date two) {
		if (one.equals(two))
			return one;

		return two;
	}

	// A
	private Notification checkoutValidation(Item item, Date given, int holidays, HolidayCalculationStrategy strategy) {

		Notification notification = new Notification();

		// 1. validation - given date
		givenDateValidation(given, notification);

		// 2. validation - item
		itemValidation(item, given, notification);

		// 3. validation - patronType
		patronTypeValidation(given, holidays, strategy, notification);

		return notification;
	}

	// B
	private void stateChangeToCompleted(Item item, Date given) {

		Biblio b = item.getBiblio();

		Long biblioId = b.getId();

		for (Reservation r : reservations) {

			if (r.getState().equals(ReservationState.NOTIFIED) || r.getState().equals(ReservationState.AVAILABLE)) {

				if (biblioId.equals(r.getBiblio().getId())) {
					r.setState(ReservationState.COLLECTED);
					r.setCollectedDate(given);
				}
				
				reservations.remove(r);
				
				return;

			}
		}

	}

	// A.1
	private void givenDateValidation(Date given, Notification notification) {

		if (startDate.after(given) || endDate.before(given)) {
			notification.addError(CirculationCode.INVALID_GIVENDATE_PATRON);
		}

		if (patronType.getStartDate().after(given) || patronType.getExpiryDate().before(given)) {
			notification.addError(CirculationCode.INVALID_GIVENDATE_PATRONTYPE);
		}

	}

	// A.3
	private void itemValidation(Item item, Date given, Notification notification) {

		// check item's borrowable
		if (!item.isItemStateBorrowable())
			notification.addError(CirculationCode.ITEMSTATE_ARE_NOT_BORROWABLE);

		if (!item.isItemStatusBorrowable())
			notification.addError(CirculationCode.ITEMSTATUS_ARE_NOT_BORROWABLE);

		// A.3.1
		// not allow to checkout two same book
		if (anySameBiblioInCheckouts(item)) {
			notification.addError(CirculationCode.SAME_BIBLIO_ARE_NOT_ALLOWED);
		}

		// check has other reservations (exclude myself)
		// if yes , am l the pickup candidate
		// because of this checking , item's reservations have to be fetch
		// correctly when retrieve item !
		if (item.hasReservations()) {
			
			if (!item.amlThePickupCandidate(this))
				notification.addError(CirculationCode.HAS_RESERVATIONS);
			
			// A.3.2
			// item not reserved by this user
			if (!isItemReservedByThisUser(item)) {
				notification.addError(CirculationCode.ITEM_NOT_RESERVED_BY_THIS_USER);
			}
			
			// A.3.3
			// item in reservable state
			if (!itemInReservableState(item))
				notification.addError(CirculationCode.ITEM_NOT_IN_RESERVABLE_STATE);
		}

	}

	// A.3.3.1
	private boolean resumeCheckoutPeriod(Item item, Date given) {

		long oneDay = 86400000;
		long duration = getPatronType().getResumeBorrowablePeriod();
		Date calResumeCheckoutDate = new Date(item.getLastCheckin().getTime() + (duration * oneDay));
		if (given.after(calResumeCheckoutDate))
			return true;

		return false;

	}

	// A.3.1
	private boolean anySameBiblioInCheckouts(Item item) {

		Biblio biblio = item.getBiblio();

		Long bid = biblio.getId();

		for (Checkout c : checkouts) {
			Biblio b = c.getItem().getBiblio();

			if (inCheckoutState(c) && b != null && bid.equals(b.getId()))
				return true;

		}

		return false;

	}

	private boolean inCheckoutState(Checkout checkout) {
		return (checkout.getState().equals(CheckoutState.CHECKOUT) || checkout.getState().equals(CheckoutState.RENEW));
	}

	// A.3.2.1
	private boolean isItemReservedByThisUser(Item item) {

		Biblio b = item.getBiblio();

		Long biblioId = b.getId();
		if (this.reservations.size() > 0) {
			for (Reservation r : reservations) {
				if (biblioId == r.getBiblio().getId()) {
					return true;
				}
			}
		}

		return false;

	}

	// A.3.2.2
	private boolean itemInReservableState(Item item) {

		Biblio b = item.getBiblio();

		Long biblioId = b.getId();

		for (Reservation r : reservations) {
			if (biblioId == r.getBiblio().getId()) {
				if (r.getState().equals(ReservationState.NOTIFIED) 
						|| r.getState().equals(ReservationState.AVAILABLE))
					return true;
			}
		}

		return false;

	}

	// A.4 the states must be ACTIVE , otherwise the code bellow is not
	// true !!
	private void patronTypeValidation(Date given, int holidays, HolidayCalculationStrategy strategy, Notification notifications) {

		// A.4.1
		if (isBiblioLimitOver(patronType))
			notifications.addError(CirculationCode.REACHED_BOOKLIMIT);

	}

	// A.4.1
	private boolean isBiblioLimitOver(PatronType patronType) {

		int noOfBiblioLend = this.checkouts.size();
		int biblioLimit = patronType.getBiblioLimit().intValue();

		// we use >= here because we have to count the current checkout !
		return (noOfBiblioLend >= biblioLimit);
	}

	// ******************** checkout attachment **********************

	public void checkout(Attachment attachment, Date given) {

		Checkout checkout = findCheckout(attachment);

		Notification notification = checkoutValidation(attachment, checkout, given);

		if (notification.hasErrors())
			throw new LibraryException(notification.getAllMessages());

		// add attachmentCheckout
		AttachmentCheckout ac = new AttachmentCheckout(given, null, AttachmentCheckoutState.CHECKOUT, this, attachment,
				checkout);
		attachment.setState(AttachmentState.CHECKOUT);
		addAttachmentCheckout(ac);
	}

	private Checkout findCheckout(Attachment attachment) {

		Item item = attachment.getItem();
		List<Checkout> checkouts = item.getCheckouts();

		for (Checkout c : checkouts) {
			if (c.getItem().equals(item)) {
				return c;
			}
		}

		return null;
	}

	private Notification checkoutValidation(Attachment attachment, Checkout checkout, Date given) {

		Notification notification = new Notification();

		if (checkout == null) {
			notification.addError(CirculationCode.CHECKOUT_NOT_FOUND);
			return notification;
		}

		if (!(checkout.getState().equals(CheckoutState.CHECKOUT) || checkout.getState().equals(CheckoutState.RENEW))) {
			notification.addError(CirculationCode.WRONG_CHECKOUTSTATES);
			return notification;
		}

		if (!attachment.getState().equals(AttachmentState.IN_LIBRARY) || attachment.getBorrowable().equals(YesNo.N)) {
			notification.addError(CirculationCode.WRONG_ATTACHMENTSTATES);
			return notification;
		}

		// validation - given date
		givenDateValidation(given, notification);

		return notification;
	}

	// ********************* checkin *********************************

	public CheckoutResult checkin(Item item, Date given, boolean damage, boolean renew, HolidayCalculationStrategy strategy) {

		CheckoutResult result = new CheckoutResult();

		if (item.getState().equals(ItemState.CHECKOUT) || item.getState().equals(ItemState.REPORTLOST)) {

			Checkout checkin = findCheckout(item);

			Notification notification = checkinValidation(item, checkin, given, renew);

			if (notification.hasErrors())
				throw new LibraryException(notification.getAllMessages());

			// prepare
			checkin.calculateAllStates(given,strategy);

			// check returnDate , it cannot < checkoutDate

			/*
			 * fine |damage | state
			 * 	1 	| 	1 	| RETURN_WITH_DAMAGE_AND_FINE
			 * 	1 	| 	0 	| RETURN_WITH_FINE
			 * 	0 	| 	1 	| RETURN_WITH_DAMAGE
			 * 	0 	| 	0 	| RETURN
			 */

			// set checkout state
			if (checkin.isFine()) {
				if (damage) {
					checkin.setState(CheckoutState.RETURN_WITH_DAMAGE_AND_FINE);
				} else {
					checkin.setState(CheckoutState.RETURN_WITH_FINE);
				}
			} else {
				if (damage) {
					checkin.setState(CheckoutState.RETURN_WITH_DAMAGE);
				} else {
					checkin.setState(CheckoutState.RETURN);
					removeCheckout(checkin);
				}
			}

			checkin.setDone(given);
			checkin.getItem().setLastCheckin(given);
			checkin.getItem().setState(ItemState.IN_LIBRARY);
			// set checkin into result, otherwise renew will cause
			// CHECKOUT_NOT_FOUND
			result.setCheckout(checkin);

			// check reservation
			// if has reservation , change
			// 1st reservation to state AVAILABLE
			// item to state RESERVED_IN_LIBRARY
			Biblio biblio = item.getBiblio();

			if (biblio.isReserved()) {

				item.setState(ItemState.RESERVED_IN_LIBRARY);

				Reservation r = biblio.findFirstReservationInReservedState();
				r.setAvailableDate(given);
				r.setState(ReservationState.AVAILABLE);

				result.setReservation(r);

			}

			return result;

		}

		if (item.getState().equals(ItemState.IN_LIBRARY)) {
			throw new LibraryException(CirculationCode.ITEM_IN_LIBRARY);
		}

		if (item.getState().equals(ItemState.RESERVED_IN_LIBRARY)) {
			throw new LibraryException(CirculationCode.ITEM_RESERVED_IN_LIBRARY);
		}

		if (item.getState().equals(ItemState.REPORTLOST_AND_PAID)) {
			throw new LibraryException(CirculationCode.ITEM_LOST_AND_PAID);
		}

		return result;

	}

	// ************* checkin's helping methods *******************************

	private Checkout findCheckout(Item item) {
		for (Checkout c : checkouts) {
			if (c.getItem().equals(item))
				return c;
		}
		return null;
	}

	private Notification checkinValidation(Item item, Checkout checkin, Date given, boolean renew) {

		Notification notification = new Notification();

		if (checkin == null) {
			throw new LibraryException(CirculationCode.CHECKOUT_NOT_FOUND);
		}

		if (!(checkin.getState().equals(CheckoutState.CHECKOUT) || checkin.getState().equals(CheckoutState.RENEW)
				|| checkin.getState().equals(CheckoutState.REPORTLOST))) {

			throw new LibraryException(CirculationCode.WRONG_CHECKOUTSTATES);

		}

		// check return date validation
		if (given.before(checkin.getCheckoutDate())) {
			notification.addError(CirculationCode.INVALID_GIVENDATE);
		}

		// this part is different from item's checkin(..) , but is OK for both
		if (!renew) {
			// force user to return the attachments before returning item.
			for (AttachmentCheckout e : attachmentCheckouts) {
				if (e.getCheckout().getItem().equals(item)) {
					notification.addError(CirculationCode.RETURN_ATTACHMENT_FIRST);
					break;
				}
			}
		}

		// if this item has reservation, renew will failed !
		if (renew) {
			Biblio biblio = item.getBiblio();
			if (biblio.isReserved()) {
				notification.addError(CirculationCode.HAS_RESERVATIONS);
			}
		}

		return notification;
	}

	// ******************* checkin attachment ***************************************

	public void checkin(Attachment attachment, Date given, boolean damageBadly) {

		AttachmentState state = attachment.getState();
		
		int stateNo = 0;
		
		if(state.equals(AttachmentState.IN_LIBRARY)) {
			stateNo = 1;
		} else if (state.equals(AttachmentState.DAMAGE)){
			stateNo = 2;
		} else if (state.equals(AttachmentState.CHECKOUT) || 
					state.equals(AttachmentState.LOST)){
			stateNo = 3;
		}
		
		switch (stateNo) {

			case 1 :
				throw new LibraryException(CirculationCode.ITEM_IN_LIBRARY);
			case 2 :
				throw new LibraryException(CirculationCode.ITEM_DAMAGED);
			case 3 :
				AttachmentCheckout ac = findAttachmentCheckout(attachment);

				Notification notification = checkinValidation(ac, given);
				if (notification.hasErrors())
					throw new LibraryException(notification.getAllMessages());

				ac.setDone(given);
				attachment.setLastCheckin(given);

				if (damageBadly) {
					ac.setState(AttachmentCheckoutState.DAMAGE);
					attachment.setState(AttachmentState.DAMAGE);
				} else {
					// if this is a LOST item, then return
					ac.setState(AttachmentCheckoutState.RETURN);
					attachment.setState(AttachmentState.IN_LIBRARY);

				}

				removeAttachmentCheckout(ac);
				break;
		}

	}

	private AttachmentCheckout findAttachmentCheckout(Attachment attachment) {

		for (AttachmentCheckout ac : attachmentCheckouts) {
			if (ac.getAttachment().equals(attachment))
				return ac;
		}

		return null;
	}

	private Notification checkinValidation(AttachmentCheckout ac, Date given) {

		Notification notification = new Notification();

		if (ac == null) {
			throw new LibraryException(CirculationCode.ATTACHMENTCHECKOUT_NOT_FOUND);
		}

		if (given.before(ac.getCheckoutDate())) {
			notification.addError(CirculationCode.INVALID_GIVENDATE);
			return notification;
		}

		return notification;
	}

	// ******************* renew (item) **********************************

	public boolean isThisInCheckouts(Item item){
		for(Checkout c : this.checkouts){
			if(c.getItem().equals(item)){
				return true;
			}
		}
		return false;		
	}
	
	public void renew(Item item, Date given, Date newDueDate, int holidays, HolidayCalculationStrategy strategy) {

		boolean aRenew = true;
		
		if(!isThisInCheckouts(item)){
			throw new LibraryException(CirculationCode.ITEM_NOT_BELONG_TO_THIS_USER);
		}
		
		// check resume borrowable period
		String lastfullRenewPerson = item.getLastFullRenewPerson();

		if (lastfullRenewPerson != null && lastfullRenewPerson.equals(this.entangled)) {
			// A.3.3.1
			if (!resumeCheckoutPeriod(item, given))
				throw new LibraryException(CirculationCode.NOT_REACHED_RESUME_CHECKOUT_PERIOD);
		}

		// checkin
		CheckoutResult result = checkin(item, given, false, aRenew, strategy);
		Checkout renew = result.getCheckout();

		// renew validation
		Notification renewError = renewValidation(renew, given);
		if (renewError.hasErrors())
			throw new LibraryException(renewError.getAllMessages());

		// checkout validation
		Notification checkoutError = checkoutValidation(item, given, holidays,strategy);
		if (checkoutError.hasErrors())
			throw new LibraryException(checkoutError.getAllMessages());

		// start checking out
		Checkout b = new Checkout(given, null, renew.getRenewedNo() + 1, CheckoutState.RENEW, this, item);

		b.setDueDate(whichDueDate(b.calculateDueDate(), newDueDate));

		addCheckout(b);

		item.setState(ItemState.CHECKOUT);

		// set lastFullRenewPerson
		if (b.getRenewedNo().equals(patronType.getMaxNoOfRenew())) {
			item.setLastFullRenewPerson(this.entangled);
		}

		// there is no clear reservation code here , because the patron
		// simply cannot have reservation to the book he lend.

		// renew attachmentcheckout automatically for this item
		for (AttachmentCheckout e : attachmentCheckouts) {
			if (e.getCheckout().getItem().equals(item))
				e.setCheckoutDate(given);
		}

	}

	private Notification renewValidation(Checkout renew, Date given) {

		Notification notification = new Notification();
		
		// this validation is almost impossible to happened because when
		// the patron renew book, no checkbox "damage" for them, so
		// the only CheckoutState state will be RETURN, l still put the
		// validation here to avoid somebody else changing the GUI accidentally
		// without notice this.
		if (!(renew.getState().equals(CheckoutState.RETURN))) {
			throw new LibraryException(CirculationCode.WRONG_CHECKOUTSTATES);
		}

		if (!renew.reachMinRenewableDate(given)) {
			notification.addError(CirculationCode.CANNOT_RENEW_SO_EARLIER);
		}

		if (renew.getRenewedNo().intValue() >= patronType.getMaxNoOfRenew().intValue()) {
			notification.addError(CirculationCode.REACHED_MAX_RENEWNO);
		}

		return notification;
	}

	// ******************* reportlost (item) *****************************

	public void reportLost(Item item, Date given) {

		Checkout checkout = findCheckout(item);

		if (checkout == null) {
			throw new LibraryException(CirculationCode.CHECKOUT_NOT_FOUND);
		}

		CheckoutState state = checkout.getState();

		if (!(state.equals(CheckoutState.CHECKOUT) || state.equals(CheckoutState.RENEW))) {
			throw new LibraryException(CirculationCode.WRONG_CHECKOUTSTATES);
		}

		// set reportlost date , state
		// set item state
		if (given.after(checkout.getDueDate())) {

			checkout.setDone(given);
			checkout.setState(CheckoutState.REPORTLOST_WITH_FINE);
			checkout.getItem().setState(ItemState.REPORTLOST);

		} else {

			checkout.setDone(given);
			checkout.setState(CheckoutState.REPORTLOST);
			checkout.getItem().setState(ItemState.REPORTLOST);

		}

	}

	// ******************* reportlost (attachment) ******************

	public void reportLost(Attachment attachment, Date given) {

		AttachmentCheckout ac = findAttachmentCheckout(attachment);

		if (ac == null) {
			throw new LibraryException(CirculationCode.ATTACHMENTCHECKOUT_NOT_FOUND);
		}

		ac.setDone(given);
		ac.setState(AttachmentCheckoutState.LOST);
		attachment.setState(AttachmentState.LOST);

	}

	// ******************** reserve *******************************

	public ReservationResult reserve(Biblio biblio, Date given, Date expiryDate) {

		ReservationResult result = new ReservationResult();
		Notification notification = result.getNotification();

		Reservation r = null;

		isBiblioReservable(biblio, given, notification);

		if (notification.hasErrors())
			throw new LibraryException(notification.getAllMessages());
		
		r = new Reservation(given, expiryDate, ReservationState.RESERVE, patronType, biblio, this);

		addReservation(r);

		result.setReservation(r);

		return result;

	}

	public Reservation cancelReservation(Date given, Long reservationId) {

		Long id = new Long(reservationId);

		for (Reservation r : reservations) {

			if (r.getId().equals(id)) {

				r.cancel(given);
				removeReservation(r);
				return r;

			}

		}

		return null;

	}

	public boolean isBiblioReservable(Biblio biblio, Date given, Notification notification) {

		givenDateValidation(given, notification);

		checkWrongActionForReservation(biblio, notification);

		checkReserveLutConstraint(biblio, notification);

		return true;

	}

	private void checkWrongActionForReservation(Biblio biblio, Notification notification) {

		List<Item> items = new ArrayList<Item>(biblio.getItems());

		if (items.size() == 0){
			notification.addError(CirculationCode.NO_ITEM_FOUND);
		}
			
		if (!isReservableStatus(items)){
			notification.addError(CirculationCode.ALL_ITEMS_NOT_RESERVABLE);
		}
			
		if (isCheckoutByTheSameUser(biblio)){
			notification.addError(CirculationCode.YOU_HAD_THIS_BIBLIO);
		}
		
	}

	private void checkReserveLutConstraint(Biblio biblio, Notification notification) {

		isReserveSameBiblio(biblio, notification);
		int reservationNo = 1 + reservations.size();

		if (reservationNo > patronType.getMaxNoOfReservations().intValue()) {
			notification.addError(CirculationCode.REACHED_MAX_RESERVATION_NO);
		}

		if (this.getUnCollectedNo() > patronType.getMaxUncollectedNo().intValue()) {
			notification.addError(CirculationCode.REACHED_MAX_UNCOLLECTEDNO);
		}

	}

	private boolean isReserveSameBiblio(Biblio biblio, Notification notification) {

		for (Reservation r : reservations) {

			Biblio b = r.getBiblio();

			if (b != null && b.getId().equals(biblio.getId())) {
				notification.addError(CirculationCode.CANNOT_RESERVE_SAME_BIBLIO);
				return true;
			}

		}

		return false;

	}

	private boolean isCheckoutByTheSameUser(Biblio biblio) {

		for (Checkout c : checkouts) {
			if (c.getItem().getBiblio().getId().equals(biblio.getId()))
				return true;
		}

		return false;

	}

	public boolean isReservableStatus(List<Item> items) {

		boolean isReservable = false;

		for (Item i : items) {

			ItemState itemState = i.getState();
			ItemStatus itemStatus = i.getItemStatus();

			if (itemStatus.getBorrowable().booleanValue() && itemStatus.getReservable().booleanValue()) {

				// cannot be reserved if it is in library
				if (itemState.equals(ItemState.IN_LIBRARY)) {

					return false;
				}

				// check whether it is borrowed by himself
				if (itemState.equals(ItemState.RESERVED_IN_LIBRARY)) {
					for (Reservation r : this.reservations) {
						if (r.getBiblio().equals(i.getBiblio())) {
							return false;
						}
					}
				}

				if (itemState.equals(ItemState.CHECKOUT)) {
					isReservable = true;
				}

			}

		}
		
		return isReservable;
	}

	// ******************* pay fine *******************************

	public void payFine(Long[] ids, BigDecimal payAmount, Date given,  HolidayCalculationStrategy strategy) {

		BigDecimal fineAmount = new BigDecimal(0);

		if (payAmount == null)
			throw new LibraryException(CirculationCode.INVALID_PAIDAMOUNT);

		if (payAmount.longValue() < 0)
			throw new LibraryException(CirculationCode.INVALID_PAIDAMOUNT);

		List<Checkout> fineCheckouts = new ArrayList<Checkout>();

		for (int i = 0; i < ids.length; i++) {

			Long selected = ids[i];

			// make fineAmount and checkoutIds
			for (Checkout c : checkouts) {

				Date actionDate = c.getDone();

				if (actionDate != null && actionDate.after(given)) {
					throw new LibraryException(CirculationCode.INVALID_GIVENDATE);
				}

				c.calculateAllStates(given, strategy);
				Long cid = c.getId();
				CheckoutState cstate = c.getState();

				if (selected.equals(cid)) {

					// must be in fine checkoutState
					if (isNotInFineCheckoutState(c)) {
						throw new LibraryException(CirculationCode.WRONG_CHECKOUTSTATES);	
					}

					// RF
					if (cstate.equals(CheckoutState.RETURN_WITH_FINE))
						fineAmount = fineAmount.add(c.getFineAmount());

					// RD || L
					if (cstate.equals(CheckoutState.RETURN_WITH_DAMAGE) || cstate.equals(CheckoutState.REPORTLOST)) {

						if (c.getLostOrDamageFineAmount() == null) {
							throw new LibraryException(CirculationCode.LOST_OR_DAMAGE_FINEAMOUNT_NOT_SET);	
						}

						fineAmount = fineAmount.add(c.getLostOrDamageFineAmount());
					}

					// RDF || LF
					if (cstate.equals(CheckoutState.RETURN_WITH_DAMAGE_AND_FINE)
							|| cstate.equals(CheckoutState.REPORTLOST_WITH_FINE)) {

						if (c.getLostOrDamageFineAmount() == null) {
							throw new LibraryException(CirculationCode.LOST_OR_DAMAGE_FINEAMOUNT_NOT_SET);
						}

						fineAmount = (fineAmount.add(c.getFineAmount())).add(c.getLostOrDamageFineAmount());

					}

					fineCheckouts.add(c);

				}

			}

		}
		
		if (payAmount.compareTo(fineAmount) != 0){
			throw new LibraryException(CirculationCode.FINE_AND_PAIDAMOUNT_DIFFERENT);
		}

		for (Checkout c : fineCheckouts) {

			// set states and credit
			CheckoutState bs = c.getState();

			if (bs.equals(CheckoutState.RETURN_WITH_FINE)) {

				c.setState(CheckoutState.FINE_PAID);
				c.setFinePaidDate(given);
				c.setFinePaidAmount(c.getFineAmount());
				removeCheckout(c);

			}

			if (bs.equals(CheckoutState.RETURN_WITH_DAMAGE)) {

				c.setState(CheckoutState.DAMAGE_PAID);
				c.setLostOrDamagePaidDate(given);
				c.setLostOrDamageFineAmount(c.getLostOrDamageFineAmount());
				removeCheckout(c);

			}

			if (bs.equals(CheckoutState.RETURN_WITH_DAMAGE_AND_FINE)) {

				c.setState(CheckoutState.DAMAGE_AND_FINEPAID);
				c.setFinePaidDate(given);
				c.setFinePaidAmount(c.getFineAmount());
				c.setLostOrDamagePaidDate(given);
				c.setLostOrDamageFineAmount(c.getLostOrDamageFineAmount());
				removeCheckout(c);

			}

			if (bs.equals(CheckoutState.REPORTLOST)) {

				c.setState(CheckoutState.REPORTLOST_PAID);
				c.setLostOrDamagePaidDate(given);
				c.setLostOrDamageFineAmount(c.getLostOrDamageFineAmount());
				removeCheckout(c);

			}

			if (bs.equals(CheckoutState.REPORTLOST_WITH_FINE)) {

				c.setState(CheckoutState.REPORTLOST_AND_FINEPAID);
				c.setFinePaidDate(given);
				c.setFinePaidAmount(c.getFineAmount());
				c.setLostOrDamagePaidDate(given);
				c.setLostOrDamageFineAmount(c.getLostOrDamageFineAmount());
				removeCheckout(c);

			}

		}

	}

	private boolean isNotInFineCheckoutState(Checkout b) {

		CheckoutState bs = b.getState();

		if (bs.equals(CheckoutState.RETURN_WITH_FINE) || bs.equals(CheckoutState.RETURN_WITH_DAMAGE)
				|| bs.equals(CheckoutState.RETURN_WITH_DAMAGE_AND_FINE) || bs.equals(CheckoutState.REPORTLOST)
				|| bs.equals(CheckoutState.REPORTLOST_WITH_FINE)) {
			return false;
		} else {
			return true;
		}

	}

	// ******************* states calculation **************************
	/**
	 * the order can not be change !
	 * 
	 * @param given
	 */
	public void calculateAllStates(Date given, HolidayCalculationStrategy strategy) {

		// TODO : dateExpired has never been used in the code !!!
		this.dateExpired = this.getEndDate().before(given);
		// TODO: cannot do the following code now !
		// currentLut.calculateState(given);
		calculateCheckoutsState(given,strategy);
		this.noOfOverdueBiblios = calculateNoOfBioblioOverDue();
		this.totalAmountOfFines = calculateFine();
	}

	private void calculateCheckoutsState(Date given,  HolidayCalculationStrategy strategy) {
		for (Checkout c : checkouts) {
			c.calculateAllStates(given, strategy);
		}
	}

	private int calculateNoOfBioblioOverDue() {

		int count = 0;
		for (Checkout c : checkouts) {
			if (c.isOverDue()) {
				count = count + 1;
			}
		}

		return count;
	}

	private BigDecimal calculateFine() {

		double totalFine = 0;

		for (Checkout c : checkouts) {
			if (c.getFineAmount() != null) {
				double fineAmount = c.getFineAmount().doubleValue();
				totalFine = totalFine + fineAmount;
			}
		}

		return new BigDecimal(totalFine);
	}

    @Override
	public boolean equals(Object other) {

		if (this == other)
			return true;
		if (!(other instanceof Patron))
			return false;
		final Patron that = (Patron) other;
		return Objects.equals(cardKey, that.getCardKey());

	}

    @Override
	public int hashCode() {
		return Objects.hashCode(cardKey);
	}

}
