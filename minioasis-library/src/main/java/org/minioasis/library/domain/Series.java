package org.minioasis.library.domain;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "series")
public class Series implements Serializable {

	private static final long serialVersionUID = 7596615629985266021L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", unique = true, nullable = false)
	private Long id;
	
	@NotNull
	@Length(max = 128)
	@Column(name = "name", unique = true , nullable = false)
	private String name;

	public Series() {
	}

	public Series(String name) {
		this.name = name;
	}

	public Long getId() {
		return this.id;
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
	
    @Override
	public boolean equals(Object other) {
		
		if(this == other)
			return true;
		if(other == null)
			return false;
		if(!(other instanceof Series))
			return false;
		final Series that = (Series)other;
		return Objects.equals(name, that.getName());
		
	}

    @Override
	public int hashCode() {
    	return Objects.hashCode(name);
	}

}
