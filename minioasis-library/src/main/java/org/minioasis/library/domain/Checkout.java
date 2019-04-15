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
import java.util.ArrayList;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import org.minioasis.library.service.HolidayCalculationStrategy;

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
	@Temporal(TemporalType.DATE)
	@Column(name = "checkout_date", nullable = false)
	private Date checkoutDate;
	
	@NotNull
	@Temporal(TemporalType.DATE)
	@Column(name = "due_date", nullable = false)
	private Date dueDate;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "done")
	private Date done;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "fine_paid_date")
	private Date finePaidDate;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "lost_or_damage_paid_date")
	private Date lostOrDamagePaidDate;
	
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
    @JoinColumn(name="patrontype_id" , nullable = false , updatable = false , foreignKey = @ForeignKey(name = "fk_checkout_patrontype"))
	private PatronType patronType;
    
    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="item_id", nullable = false , foreignKey = @ForeignKey(name = "fk_checkout_item"))
	private Item item;
	
    @OneToMany(mappedBy = "checkout" , cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @Filter(name = "attachmentCheckoutStateFilter")
	private List<AttachmentCheckout> attachmentCheckouts = new ArrayList<AttachmentCheckout>(0);
	
	@Transient private boolean overDue = false;
	@Transient private boolean fine = false;
	@Transient private boolean reachMinRenewableDate = false;
	@Transient private int daysOfOverDue = -1;
	@Transient private BigDecimal fineAmount = new BigDecimal(0);
	
    // global variable
	@Transient long oneDay = 86400000;

	public Checkout() {
	}

	public Checkout(Date checkoutDate, Date newDueDate,Integer renewedNo,
			CheckoutState state, Patron patron, Item item) {
		this.checkoutDate = checkoutDate;
		this.dueDate =  newDueDate;
		this.renewedNo = renewedNo;
		this.state = state;
		this.patron = patron;
		this.patronType = patron.getPatronType();
		this.item = item;
	}

	public Checkout(Date checkoutDate, Date newDueDate, Date done,
			Date finePaidDate, Date lostOrDamagePaidDate,
			BigDecimal finePaidAmount, BigDecimal lostOrDamageFineAmount,
			Integer renewedNo, CheckoutState state, Patron patron,
			PatronType patronType, Item item) {
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
		this.patronType = patron.getPatronType();
		this.item = item;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getDaysOfOverDue() {
		return daysOfOverDue;
	}

	public void setDaysOfOverDue(int daysOfOverDue) {
		this.daysOfOverDue = daysOfOverDue;
	}

	public boolean isFine() {
		return fine;
	}

	public void setFine(boolean fine) {
		this.fine = fine;
	}

	public BigDecimal getFineAmount() {
		return fineAmount;
	}

	public void setFineAmount(BigDecimal fineAmount) {
		this.fineAmount = fineAmount;
	}

	public boolean isReachMinRenewableDate() {
		return reachMinRenewableDate;
	}

	public void setReachMinRenewableDate(boolean reachMinRenewableDate) {
		this.reachMinRenewableDate = reachMinRenewableDate;
	}

	public boolean isOverDue() {
		return overDue;
	}

	public void setOverDue(boolean overDue) {
		this.overDue = overDue;
	}

	public Date getCheckoutDate() {
		return this.checkoutDate;
	}

	public void setCheckoutDate(Date checkoutDate) {
		this.checkoutDate = checkoutDate;
	}

	public Date getDueDate() {
		return this.dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	public Date getDone() {
		return this.done;
	}

	public void setDone(Date done) {
		this.done = done;
	}

	public Date getFinePaidDate() {
		return this.finePaidDate;
	}

	public void setFinePaidDate(Date finePaidDate) {
		this.finePaidDate = finePaidDate;
	}

	public Date getLostOrDamagePaidDate() {
		return this.lostOrDamagePaidDate;
	}

	public void setLostOrDamagePaidDate(Date lostOrDamagePaidDate) {
		this.lostOrDamagePaidDate = lostOrDamagePaidDate;
	}

	public BigDecimal getFinePaidAmount() {
		return this.finePaidAmount;
	}

	public void setFinePaidAmount(BigDecimal finePaidAmount) {
		this.finePaidAmount = finePaidAmount;
	}

	public BigDecimal getLostOrDamageFineAmount() {
		return this.lostOrDamageFineAmount;
	}

	public void setLostOrDamageFineAmount(BigDecimal lostOrDamageFineAmount) {
		this.lostOrDamageFineAmount = lostOrDamageFineAmount;
	}

	public Integer getRenewedNo() {
		return this.renewedNo;
	}

	public void setRenewedNo(Integer renewedNo) {
		this.renewedNo = renewedNo;
	}

	public CheckoutState getState() {
		return this.state;
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

	public PatronType getPatronType() {
		return this.patronType;
	}

	public void setPatronType(PatronType patronType) {
		this.patronType = patronType;
	}

	public Item getItem() {
		return this.item;
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
	
	// *********************************  Domain Logic  **************************************

	public boolean isInLostOrDamageState() {
		if (this.state.equals(CheckoutState.REPORTLOST) 
				|| this.state.equals(CheckoutState.REPORTLOST_WITH_FINE)
				|| this.state.equals(CheckoutState.RETURN_WITH_DAMAGE)
				|| this.state.equals(CheckoutState.RETURN_WITH_DAMAGE_AND_FINE))
			return true;
		return false;
	}

	public Date calculateDueDate(){
		
		long duration = this.patronType.getDuration().longValue();
		long itemDuration = item.getItemDuration().getValue().longValue();
		
		long total = duration + itemDuration;
		
		Date dueDate =  new Date(checkoutDate.getTime()+(total*oneDay));
		
		return dueDate;
		
	}
	
	public boolean reachMinRenewableDate(Date given) {

		long duration = this.patronType.getMinRenewablePeriod();

		Date minDate = new Date(this.checkoutDate.getTime()	+ (duration * oneDay));

		if (given.after(minDate))
			return true;

		return false;

	}

    public boolean isOverDue(Date given) {

    	if(state.equals(CheckoutState.RETURN_WITH_FINE) ||
				state.equals(CheckoutState.RETURN_WITH_DAMAGE_AND_FINE) ||
				state.equals(CheckoutState.REPORTLOST_WITH_FINE)){
    		return true;
    	}
    	
    	if(state.equals(CheckoutState.CHECKOUT) ||
				state.equals(CheckoutState.RENEW)){
    		if(given.after(dueDate))
    			return true;
    	}
    	
       	return false;

    }
	
	public int calculateDaysOfOverDue(Date given, HolidayCalculationStrategy strategy) {
	
		int dues = 0;
		int holidays = 0;

		if(state.equals(CheckoutState.RETURN_WITH_FINE) ||
				state.equals(CheckoutState.RETURN_WITH_DAMAGE_AND_FINE) ||
				state.equals(CheckoutState.REPORTLOST_WITH_FINE)){
			dues = (int)Math.round((this.done.getTime() - this.dueDate.getTime())/oneDay);
			holidays = strategy.getNoOfHolidaysBetween(dueDate, done);
		}
			
		if(state.equals(CheckoutState.CHECKOUT) ||
				state.equals(CheckoutState.RENEW)){
			dues = (int)Math.round((given.getTime() - this.dueDate.getTime())/oneDay);
			holidays = strategy.getNoOfHolidaysBetween(dueDate, given);
		}
		
		return dues - holidays;
	}
	
	public BigDecimal calculateFineAmount() {
		
		BigDecimal totalFine = new BigDecimal(0);
		
		double fineRate = this.patronType.getFineRate().doubleValue();
		double fineAmount = daysOfOverDue * fineRate;
			
		totalFine =  new BigDecimal(round(fineAmount,1)).setScale(1,BigDecimal.ROUND_HALF_UP);
		
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
	
	public int calDaysOfUnpaidFine(Date given) {
		
		return (int)Math.round((given.getTime() - this.dueDate.getTime())/oneDay);

	}
	
	public void calculateAllStates(Date given, HolidayCalculationStrategy strategy) {
		
		this.overDue = isOverDue(given);
		
		if(overDue){

			this.daysOfOverDue = calculateDaysOfOverDue(given, strategy);
			this.fineAmount = calculateFineAmount();
			
			if(fineAmount.doubleValue() > 0){
				this.fine = true;
			}else{
				this.fine = false;
			}			
		}

		this.reachMinRenewableDate = reachMinRenewableDate(given);

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
