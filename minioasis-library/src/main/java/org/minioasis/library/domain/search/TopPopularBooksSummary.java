package org.minioasis.library.domain.search;

import java.io.Serializable;
import java.time.LocalDate;

public class TopPopularBooksSummary implements Serializable {

	private static final long serialVersionUID = 434681452802841059L;

	private String title;
	private String isbn;
	private LocalDate firstCheckin;
	private String active;
	private String patronType;
	private String group;
	private Integer total;

	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getIsbn() {
		return isbn;
	}
	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}
	public LocalDate getFirstCheckin() {
		return firstCheckin;
	}
	public void setFirstCheckin(LocalDate firstCheckin) {
		this.firstCheckin = firstCheckin;
	}
	public String getActive() {
		return active;
	}
	public void setActive(String active) {
		this.active = active;
	}
	public String getPatronType() {
		return patronType;
	}
	public void setPatronType(String patronType) {
		this.patronType = patronType;
	}
	public String getGroup() {
		return group;
	}
	public void setGroup(String group) {
		this.group = group;
	}
	public Integer getTotal() {
		return total;
	}
	public void setTotal(Integer total) {
		this.total = total;
	}
	
}
