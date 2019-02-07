package org.minioasis.library.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Filter;
import org.hibernate.validator.constraints.Length;
import org.minioasis.library.exception.LibraryException;
import org.minioasis.validation.Notification;

@Entity
@Table(name = "attachment")
public class Attachment implements Serializable {

	private static final long serialVersionUID = 256405420843100507L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", unique = true, nullable = false)
	private Long id;

	@NotNull
	@Column(nullable = false)
	private String description;
	
	@NotNull
	@Length(max = 32)
	@Column(name = "barcode", unique = true , nullable = false)
	private String barcode;
	
	@Length(max = 20)
	@Column(name = "call_no" , nullable = false)
	private String callNo;
	
	@NotNull
	@Temporal(TemporalType.DATE)
	@Column(name = "first_checkin", nullable = false)
	private Date firstCheckin;

	@Temporal(TemporalType.DATE)
	@Column(name = "last_checkin")
	private Date lastCheckin;
	
	@NotNull
	@Column(nullable = false , columnDefinition = "CHAR(1)")
	@Enumerated(EnumType.STRING)
	private YesNo borrowable;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "state" , nullable = false , columnDefinition = "CHAR(20)" )
    @Filter(name = "attachmentStateFilter")
	private AttachmentState state;
    
    @ManyToOne
    @JoinColumn(name="item_id", nullable = false , foreignKey = @ForeignKey(name = "fk_attachment_item"))
	private Item item;
    
    @OneToMany(mappedBy="attachment")
    @OrderBy("checkoutDate ASC")
    @Filter(name = "attachmentCheckoutStateFilter")
	private List<AttachmentCheckout> attachmentCheckouts = new ArrayList<AttachmentCheckout>(0);
	
	public Attachment() {}

	public Attachment(String description, String barcode, String callNo,Date firstCheckin,
			Date lastCheckin,YesNo borrowable,AttachmentState state, Item item) {
		this.description = description;
		this.barcode = barcode;
		this.callNo = callNo;
		this.firstCheckin = firstCheckin;
		this.lastCheckin = lastCheckin;
		this.borrowable = borrowable;
		this.state = state;
		this.item = item;
	}

	public Attachment(String description, String barcode, String callNo, Date firstCheckin, 
			Date lastCheckin,Item item, YesNo borrowable, AttachmentState state) {
		this.description = description;
		this.barcode = barcode;
		this.callNo = callNo;
		this.borrowable = borrowable;
		this.item = item;
		this.state = state;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public String getCallNo() {
		return callNo;
	}

	public void setCallNo(String callNo) {
		this.callNo = callNo;
	}

	public Date getFirstCheckin() {
		return firstCheckin;
	}

	public void setFirstCheckin(Date firstCheckin) {
		this.firstCheckin = firstCheckin;
	}

	public Date getLastCheckin() {
		return lastCheckin;
	}

	public void setLastCheckin(Date lastCheckin) {
		this.lastCheckin = lastCheckin;
	}

	public YesNo getBorrowable() {
		return borrowable;
	}

	public void setBorrowable(YesNo borrowable) {
		this.borrowable = borrowable;
	}

	public AttachmentState getState() {
		return state;
	}

	public void setState(AttachmentState state) {
		this.state = state;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}
	
	public List<AttachmentCheckout> getAttachmentCheckouts() {
		return attachmentCheckouts;
	}

	public void setAttachmentCheckouts(List<AttachmentCheckout> attachmentCheckouts) {
		this.attachmentCheckouts = attachmentCheckouts;
	}
	
	// ****************************************** Domain Logic *****************************************

	public CheckoutResult checkin(Date given, boolean damageBadly) {

		CheckoutResult result = new CheckoutResult();
		
		AttachmentCheckout ac = findAttachmentCheckout();
		
		Notification notification = checkoutValidation(ac, given);
		if(notification.hasErrors())
			throw new LibraryException(notification.getAllMessages());
	
		if (this.state.equals(AttachmentState.CHECKOUT)
				|| this.state.equals(AttachmentState.LOST)) {
		
			ac.setCompleted(given);
			setLastCheckin(given);

			if (damageBadly) {
				ac.setState(AttachmentCheckoutState.DAMAGE);
				setState(AttachmentState.DAMAGE);
			} else {
				ac.setState(AttachmentCheckoutState.RETURN);
				setState(AttachmentState.IN_LIBRARY);

			}
			
			result.setAttachmentCheckout(ac);

			return result;	

		}
		
		if(this.state.equals(AttachmentState.IN_LIBRARY)){
			throw new LibraryException(CirculationCode.ITEM_IN_LIBRARY);
		}
		
		if(this.state.equals(AttachmentState.DAMAGE)){
			throw new LibraryException(CirculationCode.ITEM_DAMAGED);
		}
		
		return result;

	}

	private AttachmentCheckout findAttachmentCheckout() {

		for (AttachmentCheckout ac : attachmentCheckouts) {
			if (ac.getAttachment().equals(this))
				return ac;
		}
		
		return null;
	}
	
	private Notification checkoutValidation(AttachmentCheckout ac, Date given){
		
		Notification notification = new Notification();
		
		if(ac == null){
			throw new LibraryException(CirculationCode.ATTACHMENTCHECKOUT_NOT_FOUND);
		}
		
		if (given.before(ac.getCheckoutDate())){
			notification.addError(CirculationCode.INVALID_GIVENDATE);
		}
		
		return notification;
	}
	
	public boolean equals(Object other) {
		
		if (this == other) return true;
		if ( !(other instanceof Attachment) ) return false;
		final Attachment that = (Attachment) other;

		return this.id.equals( that.getBarcode() );
		
	}
	public int hashCode() {
		return id == null ? System.identityHashCode(this) : id.hashCode();
	}
	
}

