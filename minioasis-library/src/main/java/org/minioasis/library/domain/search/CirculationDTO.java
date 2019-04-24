package org.minioasis.library.domain.search;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import org.minioasis.library.domain.Patron;
import org.minioasis.library.domain.Reservation;

public class CirculationDTO implements Serializable {

	private static final long serialVersionUID = -693285528500118818L;
	
	private Patron patron;
	private String cardKey;
	private String barcode;
	private Date given;
	private boolean damage = false;
	private String msg = null;
	private BigDecimal payAmount;
	private Long[] ids = null;
	private Reservation reservation = null;
	
	public Patron getPatron() {
		return patron;
	}
	public void setPatron(Patron patron) {
		this.patron = patron;
	}
	public String getCardKey() {
		return cardKey;
	}
	public void setCardKey(String cardKey) {
		this.cardKey = cardKey;
	}
	public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	public Date getGiven() {
		return given;
	}
	public void setGiven(Date given) {
		this.given = given;
	}
	public boolean isDamage() {
		return damage;
	}
	public void setDamage(boolean damage) {
		this.damage = damage;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public BigDecimal getPayAmount() {
		return payAmount;
	}
	public void setPayAmount(BigDecimal payAmount) {
		this.payAmount = payAmount;
	}
	public Long[] getIds() {
		return ids;
	}
	public void setIds(Long[] ids) {
		this.ids = ids;
	}
	public Reservation getReservation() {
		return reservation;
	}
	public void setReservation(Reservation reservation) {
		this.reservation = reservation;
	}
	public void clearHistory(){
		setBarcode(null);
		setDamage(false);

		setMsg(null);
		setPayAmount(null);
		setIds(null);
		
		//setReservation(null);
	}
		
}