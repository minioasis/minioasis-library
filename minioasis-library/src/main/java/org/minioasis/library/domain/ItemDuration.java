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

import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "item_duration")
public class ItemDuration implements Serializable {

	private static final long serialVersionUID = 2198938334309107778L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Long id;
	
	@NotNull
	@Length(max = 64)
	@Column(name = "name", unique = true , nullable = false)
	private String name;
	
	@NotNull
	@Column(name = "item_duration_value" , nullable = false)
	private Integer value;
	
    @OneToMany(mappedBy="itemDuration")
	private Set<Item> items = new HashSet<Item>(0);

	public ItemDuration() {
	}

	public ItemDuration(String name, Integer value) {
		this.name = name;
		this.value = value;
	}

	public ItemDuration(String name, Integer value,
			Set<Item> items) {
		this.name = name;
		this.value = value;
		this.items = items;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

	public Set<Item> getItems() {
		return items;
	}

	public void setItems(Set<Item> items) {
		this.items = items;
	}

    @Override
	public boolean equals(Object other) {

		if(this == other)
			return true;
		if(other == null)
			return false;
		if(!(other instanceof ItemDuration))
			return false;
		final ItemDuration that = (ItemDuration) other;
		return Objects.equals(name, that.getName());

	}

    @Override
	public int hashCode() {
    	return Objects.hashCode(name);
	}

}
