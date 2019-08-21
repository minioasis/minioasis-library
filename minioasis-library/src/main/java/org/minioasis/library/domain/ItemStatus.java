package org.minioasis.library.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.envers.Audited;
import org.hibernate.validator.constraints.Length;

@Entity
@Audited
@Table(name = "item_status")
public class ItemStatus implements Serializable {

	private static final long serialVersionUID = 5961019929667652636L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", unique = true, nullable = false)
	private Long id;
	
	@NotNull
	@Length(max = 64)
	@Column(name = "name", unique = true , nullable = false)
	private String name;
	
	@NotNull
	@Column(nullable = false)
	private Boolean borrowable;
	
	@NotNull
	@Column(nullable = false)
	private Boolean reservable;
	
    @OneToMany(mappedBy="itemStatus")
	private Set<Item> items = new HashSet<Item>(0);

	public ItemStatus() {
	}

	public ItemStatus(String name, Boolean borrowable,
			Boolean reservable) {
		this.name = name;
		this.borrowable = borrowable;
		this.reservable = reservable;
	}

	public ItemStatus(String name, Boolean borrowable,
			Boolean reservable, Set<Item> items) {
		this.name = name;
		this.borrowable = borrowable;
		this.reservable = reservable;
		this.items = items;
	}

	public Boolean getBorrowable() {
		return borrowable;
	}

	public void setBorrowable(Boolean borrowable) {
		this.borrowable = borrowable;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Set<Item> getItems() {
		return items;
	}

	public void setItems(Set<Item> items) {
		this.items = items;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean getReservable() {
		return reservable;
	}

	public void setReservable(Boolean reservable) {
		this.reservable = reservable;
	}

    @Override
	public boolean equals(Object other) {
		
		if(this == other)
			return true;
		if(other == null)
			return false;
		if(!(other instanceof ItemStatus))
			return false;
		final ItemStatus that = (ItemStatus) other;
		return Objects.equals(name, that.getName());

	}

    @Override
	public int hashCode() {
    	return Objects.hashCode(name);
	}

}
