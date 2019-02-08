package org.minioasis.library.domain;

import java.io.Serializable;
import java.util.Date;

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
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;

@Entity
@FilterDef(
		name = "reservationStateFilter",
		defaultCondition= "state in (:effectiveReservationState)" ,
		parameters = {
			@ParamDef(name = "effectiveReservationState", type = "string")
		}
	)
@Table(name = "reservation")
public class Reservation implements Serializable {

	private static final long serialVersionUID = -2106656926264523126L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", unique = true, nullable = false)
	private Long id;
	
	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "reservation_date", nullable = false)
	private Date reservationDate;
	
	@NotNull
	@Temporal(TemporalType.DATE)
	@Column(name = "expiry_date", nullable = false)
	private Date expiryDate;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "available_date")
	private Date availableDate;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "notification_date")
	private Date notificationDate;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "collected_date")
	private Date collectedDate;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "cancel_date")
	private Date cancelDate;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "unCollected_date")
	private Date unCollectedDate;
	
    @NotNull
	@Enumerated(EnumType.STRING)
	@Column(name = "state" , nullable = false , columnDefinition = "CHAR(20)")
	private ReservationState state;
	
	@NotNull
    @ManyToOne
    @JoinColumn(name="patrontype_id" , nullable = false , foreignKey = @ForeignKey(name = "fk_reservation_patrontype"))
	private PatronType patronType;
	
    @ManyToOne
    @JoinColumn(name="biblio_id" , nullable = true , updatable = false , foreignKey = @ForeignKey(name = "fk_reservation_biblio"))
	private Biblio biblio;
	
    @NotNull
    @ManyToOne
    @JoinColumn(name="patron_id" , nullable = false , updatable = false , foreignKey = @ForeignKey(name = "fk_reservation_patron"))
	private Patron patron;

	public Reservation(Date collectedDate) {
		super();
		this.collectedDate = collectedDate;
	}

	public Reservation() {
	}

	public Reservation(Date reservationDate, Date expiryDate,
			ReservationState state, PatronType patronType,
			Biblio biblio, Patron patron) {
		this.reservationDate = reservationDate;
		this.expiryDate = expiryDate;
		this.state = state;
		this.patronType = patronType;
		this.biblio = biblio;
		this.patron = patron;
	}

	public Reservation(Date reservationDate, Date expiryDate,
			Date availableDate, Date notificationDate, Date collectedDate,
			Date cancelDate,Date unCollectedDate, ReservationState state,
			PatronType patronType, Biblio biblio,Patron patron) {
		this.reservationDate = reservationDate;
		this.expiryDate = expiryDate;
		this.availableDate = availableDate;
		this.notificationDate = notificationDate;
		this.collectedDate = collectedDate;
		this.cancelDate = cancelDate;
		this.unCollectedDate = unCollectedDate;
		this.state = state;
		this.patronType = patronType;
		this.biblio = biblio;
		this.patron = patron;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getReservationDate() {
		return this.reservationDate;
	}

	public void setReservationDate(Date reservationDate) {
		this.reservationDate = reservationDate;
	}

	public Date getExpiryDate() {
		return this.expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	public Date getAvailableDate() {
		return this.availableDate;
	}

	public void setAvailableDate(Date availableDate) {
		this.availableDate = availableDate;
	}

	public Date getNotificationDate() {
		return this.notificationDate;
	}

	public void setNotificationDate(Date notificationDate) {
		this.notificationDate = notificationDate;
	}


	public Date getCollectedDate() {
		return collectedDate;
	}

	public void setCollectedDate(Date collectedDate) {
		this.collectedDate = collectedDate;
	}

	public Date getCancelDate() {
		return this.cancelDate;
	}

	public void setCancelDate(Date cancelDate) {
		this.cancelDate = cancelDate;
	}

	public Date getUnCollectedDate() {
		return unCollectedDate;
	}

	public void setUnCollectedDate(Date unCollectedDate) {
		this.unCollectedDate = unCollectedDate;
	}

	public ReservationState getState() {
		return this.state;
	}

	public void setState(ReservationState state) {
		this.state = state;
	}

	public PatronType getPatronType() {
		return this.patronType;
	}

	public void setPatronType(PatronType patronType) {
		this.patronType = patronType;
	}

	public Biblio getBiblio() {
		return this.biblio;
	}

	public void setBiblio(Biblio biblio) {
		this.biblio = biblio;
	}

	public Patron getPatron() {
		return patron;
	}

	public void setPatron(Patron patron) {
		this.patron = patron;
	}

	public void cancel(Date given) {

		if (state.equals(ReservationState.RESERVE)) {
			setCancelDate(given);
			setState(ReservationState.CANCEL);
		}

		if (state.equals(ReservationState.AVAILABLE) || state.equals(ReservationState.NOTIFIED)) {

			setCancelDate(given);
			setState(ReservationState.CANCEL);

			Reservation candidate = biblio.findFirstReservationInReservedState();
			if (candidate != null)
				candidate.setState(ReservationState.AVAILABLE);

		}

	}
	
	public void notification(Date given) {
		if(given == null)
			throw new IllegalArgumentException("Must have a given date.");
		
		if(state.equals(ReservationState.AVAILABLE)){
			setNotificationDate(given);
			setState(ReservationState.NOTIFIED);
		}
	}
	
    @Override
	public boolean equals(Object other) {
		
		if(this == other)
			return true;
		if(other == null)
			return false;
		if(!(other instanceof Reservation))
			return false;
		final Reservation that = (Reservation)other;
		return id != null && id.equals(that.getId());
		
	}
    
    @Override
    public int hashCode() {
        return 39;
    }
}
