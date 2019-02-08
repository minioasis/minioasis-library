package org.minioasis.library.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "block")
public class Block implements Serializable {

	private static final long serialVersionUID = 6909064930996625896L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", unique = true, nullable = false)
	private Long id;
	
	@NotNull
	@Length(max = 64)
	@Column(name = "name", unique = true , nullable = false)
	private String name;
	
	@NotNull
	@Temporal(TemporalType.DATE)
	@Column(name = "start_date", nullable = false)
	private Date startDate;
	
	@NotNull
	@Temporal(TemporalType.DATE)
	@Column(name = "expiry_date", nullable = false)
	private Date expiryDate;

	@Column(name = "description" , columnDefinition = "TEXT")
	private String description;
	
    @NotNull
	@Enumerated(EnumType.STRING)
	@Column(name = "state" , nullable = false , columnDefinition = "VARCHAR(20)")
	private BlockState state;
	
    @NotNull
    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="patron_id" , nullable = false , updatable = true , foreignKey = @ForeignKey(name = "fk_block_patron"))
	private Patron patron;

	public Block() {
	}

	public Block(String name, Date startDate, Date expiryDate,
			BlockState state, Patron patron) {
		this.name = name;
		this.startDate = startDate;
		this.expiryDate = expiryDate;
		this.state = state;
		this.patron = patron;
	}

	public Block(String name, Date startDate, Date expiryDate,
			String description, BlockState state,
			Patron patron) {
		this.name = name;
		this.startDate = startDate;
		this.expiryDate = expiryDate;
		this.description = description;
		this.state = state;
		this.patron = patron;
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

	public Date getStartDate() {
		return this.startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getExpiryDate() {
		return this.expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public BlockState getState() {
		return state;
	}

	public void setState(BlockState state) {
		this.state = state;
	}

	public Patron getPatron() {
		return patron;
	}

	public void setPatron(Patron patron) {
		this.patron = patron;
	}

    @Override
	public boolean equals(Object other) {

		if(this == other)
			return true;
		if(other == null)
			return false;
		if(!(other instanceof Block))
			return false;
		final Block that = (Block)other;
		return Objects.equals(name, that.getName());

	}

    @Override
	public int hashCode() {
		return Objects.hashCode(name);
	}

}
