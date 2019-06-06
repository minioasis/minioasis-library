package org.minioasis.library.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

@Entity
@Table(name = "journal_entry")
public class JournalEntry implements Serializable {

	private static final long serialVersionUID = 862965136161416526L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Long id;
	
	@NotNull
	@Length(max = 128)
	@Column(name = "description", nullable = false)
	private String description;
	
	@Column(name = "txn_date")
    private LocalDate txnDate;
	
	@NotNull
	@Column(name = "total" , columnDefinition = "DECIMAL(12,2)" , nullable = false)
    private BigDecimal total = BigDecimal.ZERO;
	
	@OneToMany(mappedBy="journalEntry" , fetch = FetchType.LAZY)
	private List<JournalEntryLine> lines = new ArrayList<JournalEntryLine>();
    
	@Column(name = "created_by")
    private String createdBy;

	@CreatedDate
	@Column(name = "created")
    private LocalDateTime created = LocalDateTime.now();

	@Column(name = "updated_by")
    private String updatedBy;
	
	@LastModifiedDate
	@Column(name = "updated")
    private LocalDateTime updated;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public LocalDate getTxnDate() {
		return txnDate;
	}

	public void setTxnDate(LocalDate txnDate) {
		this.txnDate = txnDate;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public List<JournalEntryLine> getLines() {
		return lines;
	}

	public void setLines(List<JournalEntryLine> lines) {
		this.lines = lines;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public LocalDateTime getCreated() {
		return created;
	}

	public void setCreated(LocalDateTime created) {
		this.created = created;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public LocalDateTime getUpdated() {
		return updated;
	}

	public void setUpdated(LocalDateTime updated) {
		this.updated = updated;
	}
	
}