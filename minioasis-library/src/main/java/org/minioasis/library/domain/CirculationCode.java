package org.minioasis.library.domain;

import org.minioasis.validation.ErrorCode;

public enum CirculationCode implements ErrorCode {

	ATTACHMENTCHECKOUT_NOT_FOUND(5,"attachmentcheckout.not.found"),
	SAME_BIBLIO_ARE_NOT_ALLOWED(10,"same.biblio.are.not.allowed"),
	CHECKOUT_NOT_FOUND(15,"checkout.not.found"),
	INVALID_GIVENDATE(25,"invalid.given.date"),
	INVALID_GIVENDATE_PATRON(30,"invalid.given.date.patron"),
	INVALID_GIVENDATE_PATRONTYPE(35,"invalid.given.date.patrontype"),
	ITEM_NOT_BELONG_TO_THIS_USER(37, "item.not.belong.to.this.user"),
	ITEM_NOT_FOUND(40,"item.not.found"),	
	ITEM_NOT_CHECKED(45,"item.not.checked"),	
	ITEM_DAMAGED(50,"item.damaged"),
	ITEM_IN_LIBRARY(55,"item.in.library"),
	ITEM_LOST_AND_PAID(60,"item.lost.and.paid"),
	ITEM_RESERVED_IN_LIBRARY(65,"item.reserved.in.library"),
	ITEMSTATE_ARE_NOT_BORROWABLE(70,"itemstate.are.not.borrowable"),
	ITEMSTATUS_ARE_NOT_BORROWABLE(75,"itemstatus.are.not.borrowable"),
	NO_ITEM_FOUND(80,"no.item.found"),
	RETURN_ATTACHMENT_FIRST(85,"return.attachments.first"),
	WRONG_ATTACHMENTSTATES(90,"wrong.attachmentstates"),
	WRONG_CHECKOUTSTATES(95,"wrong.checkoutstates"),
	
	// patron type
	REACHED_BOOKLIMIT(100,"reached.book.limit"),	
	NOT_REACHED_RESUME_CHECKOUT_PERIOD(125,"not.reached.resume.checkout.period"),
	
	// pay fines
	LOST_OR_DAMAGE_FINEAMOUNT_NOT_SET(130,"lost.or.damage.fineamount.not.set"),
	FINE_AND_PAIDAMOUNT_DIFFERENT(135,"fine.and.paidamount.different"),
	INVALID_PAIDAMOUNT(140,"invalid.pay.amount"),
	
	// renew
	CANNOT_RENEW_SO_EARLIER(145,"cannot.renew.so.earlier"),	
	REACHED_MAX_RENEWNO(146,"reached.max.renew.no"),
	HAS_FINES(147,"has.fines"),
	
	// reservation
	THIS_BIBLIO_IS_AVAILABLE(150,"this.biblio.is.available"),
	CANNOT_RESERVE_SAME_BIBLIO(155,"cannot.reserve.same.biblio"),
	HAS_RESERVATIONS(165,"has.reservations"),
	ITEM_NOT_IN_RESERVABLE_STATE(167,"item.not.in.reservable.state"),
	ITEM_NOT_RESERVED_BY_THIS_USER(168,"item.not.reserved.by.this.user"),
	REACHED_MAX_RESERVATION_NO(170,"reached.max.reservation.no"),
	REACHED_MAX_UNCOLLECTEDNO(175,"reached.max.uncollected.no"),
	YOU_HAD_THIS_BIBLIO(180,"you.had.this.biblio");

	private final int number;
	private final String code;
	
	private CirculationCode(int number, String code) {
	    this.number = number;
	    this.code = code;
	 }

	public int getNumber() {
		return number;
	}

	public String getCode() {
		return code;
	}
	
}
