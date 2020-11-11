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
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

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
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;

@Entity
@FilterDef(
		name = "checkoutStateFilter",
		defaultCondition= "state in (:effectiveCheckoutState)" ,
		parameters = {
			@ParamDef(name = "effectiveCheckoutState", type = "string")
		}
	)
@Table(name = "checkout")
public class Checkout implements Serializable {

	private static final long serialVersionUID = -1325747475488445105L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Long id;
	
	@NotNull
	@Column(name = "checkout_date", nullable = false)
	private LocalDate checkoutDate;
	
	@NotNull
	@Column(name = "due_date", nullable = false)
	private LocalDate dueDate;
	
	@Column(name = "done")
	private LocalDate done;
	
	@Column(name = "fine_paid_date")
	private LocalDate finePaidDate;
	
	@Column(name = "lost_or_damage_paid_date")
	private LocalDate lostOrDamagePaidDate;
	
	@Column(name = "fine_paid_amount" , columnDefinition = "DECIMAL(12,2)")
	private BigDecimal finePaidAmount;
	
	@Column(name = "lost_and_damage_fine_amount" , columnDefinition = "DECIMAL(12,2)")
	private BigDecimal lostOrDamageFineAmount;
	
	@Column(name = "renewed_no" , columnDefinition="TINYINT(3) UNSIGNED" , nullable = false)
	private Integer renewedNo;
	
    @NotNull
	@Enumerated(EnumType.STRING)
	@Column(name = "state" , nullable = false , columnDefinition = "CHAR(40)")
	private CheckoutState state;
	
    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="patron_id" , nullable = false , updatable = false , foreignKey = @ForeignKey(name = "fk_checkout_patron"))
	private Patron patron;
    
    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="item_id", nullable = false , foreignKey = @ForeignKey(name = "fk_checkout_item"))
	private Item item;
	
    @OneToMany(mappedBy = "checkout" , cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @Filter(name = "attachmentCheckoutStateFilter")
	private List<AttachmentCheckout> attachmentCheckouts = new ArrayList<AttachmentCheckout>(0);

	@Transient
	private List<Holiday> holidays = new ArrayList<Holiday>(0);
	
	@Transient private int daysOfOverDue = 0;
	@Transient private BigDecimal fineAmount = new BigDecimal(0);
	
    // global variable
	@Transient long oneDay = 86400000;

	public Checkout() {
	}

	public Checkout(LocalDate checkoutDate, LocalDate newDueDate,Integer renewedNo,
			CheckoutState state, Patron patron, Item item) {
		this.checkoutDate = checkoutDate;
		this.dueDate =  newDueDate;
		this.renewedNo = renewedNo;
		this.state = state;
		this.patron = patron;
		this.item = item;
	}

	public Checkout(LocalDate checkoutDate, LocalDate newDueDate, LocalDate done,
			LocalDate finePaidDate, LocalDate lostOrDamagePaidDate,
			BigDecimal finePaidAmount, BigDecimal lostOrDamageFineAmount,
			Integer renewedNo, CheckoutState state, Patron patron, Item item) {
		this.checkoutDate = checkoutDate;
		this.dueDate = newDueDate;
		this.done = done;
		this.finePaidDate = finePaidDate;
		this.lostOrDamagePaidDate = lostOrDamagePaidDate;
		this.finePaidAmount = finePaidAmount;
		this.lostOrDamageFineAmount = lostOrDamageFineAmount;
		this.renewedNo = renewedNo;
		this.state = state;
		this.patron = patron;
		this.item = item;
	}
	
	// *********************************  Domain Logic  **************************************

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDate getCheckoutDate() {
		return checkoutDate;
	}

	public void setCheckoutDate(LocalDate checkoutDate) {
		this.checkoutDate = checkoutDate;
	}

	public LocalDate getDueDate() {
		return dueDate;
	}

	public void setDueDate(LocalDate dueDate) {
		this.dueDate = dueDate;
	}

	public LocalDate getDone() {
		return done;
	}

	public void setDone(LocalDate done) {
		this.done = done;
	}

	public LocalDate getFinePaidDate() {
		return finePaidDate;
	}

	public void setFinePaidDate(LocalDate finePaidDate) {
		this.finePaidDate = finePaidDate;
	}

	public LocalDate getLostOrDamagePaidDate() {
		return lostOrDamagePaidDate;
	}

	public void setLostOrDamagePaidDate(LocalDate lostOrDamagePaidDate) {
		this.lostOrDamagePaidDate = lostOrDamagePaidDate;
	}

	public BigDecimal getFinePaidAmount() {
		return finePaidAmount;
	}

	public void setFinePaidAmount(BigDecimal finePaidAmount) {
		this.finePaidAmount = finePaidAmount;
	}

	public BigDecimal getLostOrDamageFineAmount() {
		return lostOrDamageFineAmount;
	}

	public void setLostOrDamageFineAmount(BigDecimal lostOrDamageFineAmount) {
		this.lostOrDamageFineAmount = lostOrDamageFineAmount;
	}

	public Integer getRenewedNo() {
		return renewedNo;
	}

	public void setRenewedNo(Integer renewedNo) {
		this.renewedNo = renewedNo;
	}

	public CheckoutState getState() {
		return state;
	}

	public void setState(CheckoutState state) {
		this.state = state;
	}

	public Patron getPatron() {
		return patron;
	}

	public void setPatron(Patron patron) {
		this.patron = patron;
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

	public List<Holiday> getHolidays() {
		return holidays;
	}

	public void setHolidays(List<Holiday> holidays) {
		this.holidays = holidays;
	}

	public int getDaysOfOverDue() {
		return daysOfOverDue;
	}

	public void setDaysOfOverDue(int daysOfOverDue) {
		this.daysOfOverDue = daysOfOverDue;
	}

	public BigDecimal getFineAmount() {
		return fineAmount;
	}

	public void setFineAmount(BigDecimal fineAmount) {
		this.fineAmount = fineAmount;
	}

	public long getOneDay() {
		return oneDay;
	}

	public void setOneDay(long oneDay) {
		this.oneDay = oneDay;
	}

	public boolean isInLostOrDamageState() {
		if (this.state.equals(CheckoutState.REPORTLOST) 
				|| this.state.equals(CheckoutState.REPORTLOST_WITH_FINE)
				|| this.state.equals(CheckoutState.RETURN_WITH_DAMAGE)
				|| this.state.equals(CheckoutState.RETURN_WITH_DAMAGE_AND_FINE))
			return true;
		return false;
	}
	
	public boolean reachMinRenewableDate(LocalDate given) {

		long duration = this.patron.getPatronType().getMinRenewablePeriod();
		
		LocalDate minDate = this.checkoutDate.plusDays(duration);

		if (given.isAfter(minDate))
			return true;

		return false;
	}

    public boolean isOverDue(LocalDate given) {

    	if(state.equals(CheckoutState.RETURN_WITH_FINE) ||
				state.equals(CheckoutState.RETURN_WITH_DAMAGE_AND_FINE) ||
				state.equals(CheckoutState.REPORTLOST_WITH_FINE)){
    		return true;
    	}
    	
    	if(state.equals(CheckoutState.CHECKOUT) ||
				state.equals(CheckoutState.RENEW)){
    		if(given.isAfter(dueDate))
    			return true;
    	}
    	
       	return false;

    }
	
	public int getOverDueDays(LocalDate given) {
	
		int dueDays = 0;
		int due = 0;
		int noFineDays = 0;
		
		if(state.equals(CheckoutState.RETURN_WITH_FINE) ||
				state.equals(CheckoutState.RETURN_WITH_DAMAGE_AND_FINE) ||
				state.equals(CheckoutState.REPORTLOST_WITH_FINE)){
			
			due = (int)ChronoUnit.DAYS.between(dueDate, done);
			noFineDays = getNoOfNoFineDaysBetween(dueDate, done);
			
			dueDays = due - noFineDays;
			
			if(dueDays < 0)	return 0;
		}
			
		if(state.equals(CheckoutState.CHECKOUT) ||
				state.equals(CheckoutState.RENEW)){
			
			due = (int)ChronoUnit.DAYS.between(given, dueDate);
			
			// due
			if(due < 0) {
				noFineDays = getNoOfNoFineDaysBetween(dueDate, given);
				dueDays = - due - noFineDays;
				
				return dueDays;
			}
			
		}
		
		return dueDays;
	}
	
	private int getNoOfNoFineDaysBetween(LocalDate due, LocalDate given) {
		
		int days = 0;
		
		for(Holiday h : holidays) {
			if(!h.getStartDate().isBefore(due) && !h.getEndDate().isAfter(given)) {	
				if(h.getFine() == false) {
					days = days + h.getHolidays();
				}			
			}
		}

		return days;
	}
	
	public BigDecimal calculateFineAmount() {
		
		BigDecimal totalFine = new BigDecimal(0);
		
		double fineRate = this.patron.getPatronType().getFineRate().doubleValue();
		System.out.println("*****1*****" + fineRate);
		System.out.println("*****2*****" + daysOfOverDue);
		double fineAmount = daysOfOverDue * fineRate;
	
		totalFine =  new BigDecimal(round(fineAmount,1)).setScale(1,BigDecimal.ROUND_HALF_UP);
		System.out.println("*****totalFine*****" + totalFine);	
		return totalFine;
	}
	
    private double round(double v, int scale) {

		if (scale < 0) {
			throw new IllegalArgumentException(
					"The scale must be a positive integer or zero");
		}

		BigDecimal b = new BigDecimal(v);
		BigDecimal one = new BigDecimal("1");

		return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();

	}
	
	public int calDaysOfUnpaidFine(LocalDate given) {
		
		return (int) ChronoUnit.DAYS.between(given, this.dueDate);

	}
	
	public void preparingCheckoutOn(LocalDate given) {
		
		if(isOverDue(given)){
			this.daysOfOverDue = getOverDueDays(given);
			this.fineAmount = calculateFineAmount();			
		}

	}

    @Override
	public boolean equals(Object other) {
		
		if(this == other)
			return true;
		if(other == null)
			return false;
		if(!(other instanceof Checkout))
			return false;
		final Checkout that = (Checkout)other;
		return id != null && id.equals(that.getId());
		
	}

    @Override
    public int hashCode() {
        return 35;
    }

}
