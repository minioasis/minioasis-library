package org.minioasis.library.domain;

import java.io.Serializable;
import java.util.Date;

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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;

@Entity
@FilterDef(
		name = "attachmentCheckoutStateFilter",
		defaultCondition= "state in (:effectiveAttachmentCheckoutState)" ,
		parameters = {
			@ParamDef(name = "effectiveAttachmentCheckoutState", type = "string")
		}
	)
@Table(name = "attachment_checkout")
public class AttachmentCheckout implements Serializable {

	private static final long serialVersionUID = -3097500441699675560L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Long id;
	
	@NotNull
	@Temporal(TemporalType.DATE)
	@Column(name = "checkout_date", nullable = false)
	private Date checkoutDate;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "done")
	private Date done;
	
    @NotNull
	@Enumerated(EnumType.STRING)
	@Column(name = "state" , nullable = false , columnDefinition = "CHAR(20)")
	private AttachmentCheckoutState state;
    
    @ManyToOne
    @JoinColumn(name="patron_id" , nullable = false , updatable = false , foreignKey = @ForeignKey(name = "fk_attachmentcheckout_patron"))
	private Patron patron;
    
    @ManyToOne(fetch = FetchType.EAGER , cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinColumn(name="attachment_id" , nullable = false , foreignKey = @ForeignKey(name = "fk_attachmentcheckout_attachment"))
	private Attachment attachment;
	
    @ManyToOne
    @JoinColumn(name="checkout_id" , nullable = false , foreignKey = @ForeignKey(name = "fk_attachmentcheckout_checkout"))
	private Checkout checkout;
	
	public AttachmentCheckout() {}

	public AttachmentCheckout(Date checkoutDate, Date done, AttachmentCheckoutState state, 
			Patron patron, Attachment attachment, Checkout checkout) {
		this.checkoutDate = checkoutDate;
		this.done = done;
		this.state = state;
		this.patron = patron;
		this.attachment = attachment;
		this.checkout = checkout;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getCheckoutDate() {
		return checkoutDate;
	}

	public void setCheckoutDate(Date checkoutDate) {
		this.checkoutDate = checkoutDate;
	}

	public Date getDone() {
		return done;
	}

	public void setDone(Date done) {
		this.done = done;
	}

	public AttachmentCheckoutState getState() {
		return state;
	}

	public void setState(AttachmentCheckoutState state) {
		this.state = state;
	}

	public Patron getPatron() {
		return patron;
	}

	public void setPatron(Patron patron) {
		this.patron = patron;
	}

	public Attachment getAttachment() {
		return attachment;
	}

	public void setAttachment(Attachment attachment) {
		this.attachment = attachment;
	}

	public Checkout getCheckout() {
		return checkout;
	}

	public void setCheckout(Checkout checkout) {
		this.checkout = checkout;
	}

    @Override
	public boolean equals(Object other) {
		
		if(this == other)
			return true;
		if(other == null)
			return false;
		if(!(other instanceof AttachmentCheckout))
			return false;
		final AttachmentCheckout that = (AttachmentCheckout) other;
		return id != null && id.equals(that.getId());
		
	}

    @Override
    public int hashCode() {
        return 37;
    }
	
}
