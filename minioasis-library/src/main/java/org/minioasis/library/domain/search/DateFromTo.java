package org.minioasis.library.domain.search;

import java.io.Serializable;
import java.time.LocalDate;

public class DateFromTo implements Serializable {

	private static final long serialVersionUID = 3260168897015078309L;

	private LocalDate from = LocalDate.now();

	private LocalDate to = LocalDate.now();
	
	public LocalDate getFrom() {
		return from;
	}
	public void setFrom(LocalDate from) {
		this.from = from;
	}
	public LocalDate getTo() {
		return to;
	}
	public void setTo(LocalDate to) {
		this.to = to;
	}
	
}
