package org.minioasis.library.domain.search;

import java.io.Serializable;
import java.time.LocalDate;

public class CheckoutSummary implements Serializable {

	private static final long serialVersionUID = -3300139513283797042L;

	private String cardKey;
	private String name;
	private String name2;
	private LocalDate startDate;
	private Integer total;

	public CheckoutSummary() {}
			
	public CheckoutSummary(Integer total, String cardKey) {
		this.total = total;
		this.cardKey = cardKey;
	}
	
	public String getCardKey() {
		return cardKey;
	}
	public void setCardKey(String cardKey) {
		this.cardKey = cardKey;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getName2() {
		return name2;
	}
	public void setName2(String name2) {
		this.name2 = name2;
	}
	public LocalDate getStartDate() {
		return startDate;
	}
	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}
	public Integer getTotal() {
		return total;
	}
	public void setTotal(Integer total) {
		this.total = total;
	}

}
