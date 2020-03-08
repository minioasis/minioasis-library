package org.minioasis.library.domain;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

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
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

@Entity
@FilterDef(
		name = "reservationStateFilter",
		defaultCondition= "state in (:effectiveReservationState)" ,
		parameters = {
			@ParamDef(name = "effectiveReservationState", type = "string")
		}
	)
@Audited
@Table(name = "reservation")
public class Reservation implements Serializable {

	private static final long serialVersionUID = -2106656926264523126L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Long id;
	
	@NotNull
	@Column(name = "reservation_date", nullable = false)
	private LocalDateTime reservationDate;
	
	@NotNull
	@Column(name = "expiry_date", nullable = false)
	private LocalDate expiryDate;
	
	@Column(name = "available_date")
	private LocalDate availableDate;
	
	@Column(name = "notification_date")
	private LocalDate notificationDate;
	
	@Column(name = "collected_date")
	private LocalDate collectedDate;
	
	@Column(name = "cancel_date")
	private LocalDate cancelDate;
	
	@Column(name = "uncollected_date")
	private LocalDate unCollectedDate;
	
    @NotNull
	@Enumerated(EnumType.STRING)
	@Column(name = "state" , nullable = false , columnDefinition = "CHAR(30)")
	private ReservationState state;
	
    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="biblio_id" , nullable = true , updatable = false , foreignKey = @ForeignKey(name = "fk_reservation_biblio"))
	private Biblio biblio;
	
    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="patron_id" , nullable = false , updatable = false , foreignKey = @ForeignKey(name = "fk_reservation_patron"))
	private Patron patron;

	public Reservation(LocalDate collectedDate) {
		super();
		this.collectedDate = collectedDate;
	}

	public Reservation() {
	}

	public Reservation(LocalDateTime reservationDate, LocalDate expiryDate,
			ReservationState state, Biblio biblio, Patron patron) {
		this.reservationDate = reservationDate;
		this.expiryDate = expiryDate;
		this.state = state;
		this.biblio = biblio;
		this.patron = patron;
	}

	public Reservation(LocalDateTime reservationDate, LocalDate expiryDate,
			LocalDate availableDate, LocalDate notificationDate, LocalDate collectedDate,
			LocalDate cancelDate, LocalDate unCollectedDate, ReservationState state, Biblio biblio,Patron patron) {
		this.reservationDate = reservationDate;
		this.expiryDate = expiryDate;
		this.availableDate = availableDate;
		this.notificationDate = notificationDate;
		this.collectedDate = collectedDate;
		this.cancelDate = cancelDate;
		this.unCollectedDate = unCollectedDate;
		this.state = state;
		this.biblio = biblio;
		this.patron = patron;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDateTime getReservationDate() {
		return reservationDate;
	}

	public void setReservationDate(LocalDateTime reservationDate) {
		this.reservationDate = reservationDate;
	}

	public LocalDate getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(LocalDate expiryDate) {
		this.expiryDate = expiryDate;
	}

	public LocalDate getAvailableDate() {
		return availableDate;
	}

	public void setAvailableDate(LocalDate availableDate) {
		this.availableDate = availableDate;
	}

	public LocalDate getNotificationDate() {
		return notificationDate;
	}

	public void setNotificationDate(LocalDate notificationDate) {
		this.notificationDate = notificationDate;
	}

	public LocalDate getCollectedDate() {
		return collectedDate;
	}

	public void setCollectedDate(LocalDate collectedDate) {
		this.collectedDate = collectedDate;
	}

	public LocalDate getCancelDate() {
		return cancelDate;
	}

	public void setCancelDate(LocalDate cancelDate) {
		this.cancelDate = cancelDate;
	}

	public LocalDate getUnCollectedDate() {
		return unCollectedDate;
	}

	public void setUnCollectedDate(LocalDate unCollectedDate) {
		this.unCollectedDate = unCollectedDate;
	}

	public ReservationState getState() {
		return this.state;
	}

	public void setState(ReservationState state) {
		this.state = state;
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

	public void cancel(LocalDate given) {
		setCancelDate(given);
		setState(ReservationState.CANCEL);
	}
	
	public void extend(long days) {
		if (state.equals(ReservationState.RESERVE)) {
			setExpiryDate(this.expiryDate.plusDays(days));
		}
	}
	
	public void notification(LocalDate given) {
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
