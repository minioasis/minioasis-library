/*
* Licensed to the Apache Software Foundation (ASF) under one or more
* contributor license agreements.  The ASF licenses this file to You
* under the Apache License, Version 2.0 (the "License"); you may not
* use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.  For additional information regarding
* copyright in this work, please see the NOTICE file in the top level
* directory of this distribution.
*/

package org.minioasis.library.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.JoinColumn;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Filter;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.hibernate.envers.RelationTargetAuditMode;
import org.hibernate.validator.constraints.Length;
import org.minioasis.library.exception.LibraryException;
import org.minioasis.validation.Notification;

@Entity
@Audited
@Table(name = "item")
public class Item implements Serializable {

	private static final long serialVersionUID = 9071545881473164206L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Long id;
	
	@NotNull
	@Length(max = 32)
	@Column(name = "barcode", unique = true , nullable = false)
	private String barcode;
	
	@Length(max = 30)
	@Column(name = "shelf_mark")
	private String shelfMark;
	
	@Length(max = 16)
	@Column(name = "last_full_renew_person")
	private String lastFullRenewPerson;

	@NotNull
	@Column(name = "first_checkin" , nullable = false)
	private LocalDate firstCheckin;

	@NotNull
	@Column(name = "created" , nullable = false)
	private LocalDate created = LocalDate.now();
	
	@Column(name = "last_checkin")
	private LocalDateTime lastCheckin = LocalDateTime.now();
	
	@NotNull
	@Column(name = "price" , columnDefinition = "DECIMAL(12,2)" , nullable = false)
	private BigDecimal price;

	@Length(max = 128)
	@Column(length = 128)
	private String source;

	@NotNull
	@Column(name = "checked" , nullable = false , columnDefinition = "CHAR(1)")
	@Enumerated(EnumType.STRING)
	private YesNo checked;

	@NotNull
	@Column(name = "active", nullable = false , columnDefinition = "CHAR(1) default 'Y' ")
	@Enumerated(EnumType.STRING)
	private YesNo active;
	
	@Column(name = "expired")
	private LocalDateTime expired;
	
	@Valid
	private ItemState state = ItemState.IN_LIBRARY;

	@NotAudited
	@OneToMany(mappedBy="item")
    @OrderBy("checkoutDate ASC")
    @Filter(name = "checkoutStateFilter")
	private List<Checkout> checkouts = new ArrayList<Checkout>(0);
    
	@NotAudited
    @OneToMany(mappedBy="item")
	private Set<Attachment> attachments = new HashSet<Attachment>(0);
    
	@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="biblio_id" , foreignKey = @ForeignKey(name = "fk_item_biblio"))
	private Biblio biblio;
    
	@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="item_status_id" , nullable = false , foreignKey = @ForeignKey(name = "fk_item_itemstatus"))
	private ItemStatus itemStatus;
    
	@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="location_id" , nullable = false , foreignKey = @ForeignKey(name = "fk_item_location"))
	private Location location;

	public Item() { }
	
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getBarcode() {
		return this.barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public String getShelfMark() {
		return this.shelfMark;
	}

	public void setShelfMark(String shelfMark) {
		this.shelfMark = shelfMark;
	}

	public String getLastFullRenewPerson() {
		return this.lastFullRenewPerson;
	}

	public void setLastFullRenewPerson(String lastFullRenewPerson) {
		this.lastFullRenewPerson = lastFullRenewPerson;
	}

	public LocalDateTime getLastCheckin() {
		return lastCheckin;
	}

	public void setLastCheckin(LocalDateTime lastCheckin) {
		this.lastCheckin = lastCheckin;
	}

	public LocalDate getFirstCheckin() {
		return firstCheckin;
	}

	public void setFirstCheckin(LocalDate firstCheckin) {
		this.firstCheckin = firstCheckin;
	}

	public LocalDate getCreated() {
		return created;
	}

	public void setCreated(LocalDate created) {
		this.created = created;
	}
	
	public LocalDateTime getExpired() {
		return expired;
	}

	public void setExpired(LocalDateTime expired) {
		this.expired = expired;
	}

	public BigDecimal getPrice() {
		return this.price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public YesNo getChecked() {
		return checked;
	}

	public void setChecked(YesNo checkStock) {
		this.checked = checkStock;
	}

	public YesNo getActive() {
		return active;
	}

	public void setActive(YesNo active) {
		this.active = active;
	}

	public ItemState getState() {
		return this.state;
	}

	public void setState(ItemState state) {
		this.state = state;
	}

	public Biblio getBiblio() {
		return this.biblio;
	}

	public void setBiblio(Biblio biblio) {
		this.biblio = biblio;
	}

	public ItemStatus getItemStatus() {
		return this.itemStatus;
	}

	public void setItemStatus(ItemStatus itemStatus) {
		this.itemStatus = itemStatus;
	}

	public Location getLocation() {
		return this.location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

    public List<Checkout> getCheckouts() {
		return checkouts;
	}

	public void setCheckouts(List<Checkout> checkouts) {
		this.checkouts = checkouts;
	}

	public Set<Attachment> getAttachments() {
		return attachments;
	}

	public void setAttachments(Set<Attachment> attachments) {
		this.attachments = attachments;
	}
	
    // ***************************** Domain Logic ******************************
 
	public CheckoutResult checkIn(LocalDate given, boolean damage) {

		CheckoutResult result = new CheckoutResult();
		
		Checkout checkin = this.checkouts.get(0);		
		
		if (this.state.equals(ItemState.IN_LIBRARY)) {
			throw new LibraryException(CirculationCode.ITEM_IN_LIBRARY);
		}

		if (this.state.equals(ItemState.RESERVED_IN_LIBRARY)) {
			throw new LibraryException(CirculationCode.ITEM_RESERVED_IN_LIBRARY);
		}

		if (this.state.equals(ItemState.REPORTLOST_AND_PAID)) {
			throw new LibraryException(CirculationCode.ITEM_LOST_AND_PAID);
		}

		if (this.state.equals(ItemState.CHECKOUT) || this.state.equals(ItemState.REPORTLOST)) {

			Notification notification = checkinValidation(checkin, given);
			if(notification.hasErrors())
				throw new LibraryException(notification.getAllMessages());

			// prepare
			checkin.preparingCheckoutOn(given);
					
			/*
				  fine | damage | state
					1  |   1    | RETURN_WITH_DAMAGE_AND_FINE
					1  |   0    | RETURN_WITH_FINE
				  	0  |   1    | RETURN_WITH_DAMAGE
					0  |   0    | RETURN
			*/
						
			// set checkout state
			if (checkin.isOverDue(given)) {
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
				}

			}

			checkin.setDone(given);
			checkin.getItem().setLastCheckin(given.atTime(00, 00, 00));
			checkin.getItem().setState(ItemState.IN_LIBRARY);

			result.setCheckout(checkin);

		}
		
		return result;

	}
	
	private Notification checkinValidation(Checkout checkin, LocalDate given){
		
		Notification notification = new Notification();
		
		// check return date validation
		if (given.isBefore(checkin.getCheckoutDate())) {
			notification.addError(CirculationCode.INVALID_GIVENDATE);
		}

		if (checkin.getAttachmentCheckouts().size() > 0){
			notification.addError(CirculationCode.RETURN_ATTACHMENT_FIRST);
		}
		
		return notification;
	}
	
	public boolean hasReservations() {
    	
    	boolean hasReservations = false; 
   		hasReservations = biblio.hasReservation();
    	
    	return hasReservations;
    	
    }
    
    public boolean amlThePickupCandidate(Patron patron){
    	
    	boolean result = false; 
   		result = biblio.amlThePickupCandidate(patron);
    	
    	return result;
    }
    
    public boolean isBorrowableState(Checkout checkout){
		return(checkout.getState().equals(CheckoutState.CHECKOUT) || 
				checkout.getState().equals(CheckoutState.RENEW));
	}
    
    public boolean isItemStateBorrowable(){
    	// equivalent to ItemState.IN_LIBRARY || ItemState.RESERVED_IN_LIBRARY
    	return this.getState().isBorrowable();
    }
    
    public boolean isItemStatusBorrowable(){
    	return this.getItemStatus().getBorrowable();
    }
 
    @Override
	public boolean equals(Object other) {

		if(this == other)
			return true;
		if(other == null)
			return false;
		if (!(other instanceof Item))
			return false;
		final Item that = (Item) other;
		return Objects.equals(barcode, that.getBarcode());

	}

    @Override
	public int hashCode() {
    	return Objects.hashCode(barcode);
	}

}
