package org.minioasis.library.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.envers.Audited;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

@Entity
@Audited
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
	@Column(name = "debit" , columnDefinition = "DECIMAL(12,2)" , nullable = false)
    private BigDecimal debit = BigDecimal.ZERO;
	
	@NotNull
	@Column(name = "credit" , columnDefinition = "DECIMAL(12,2)" , nullable = false)
    private BigDecimal credit = BigDecimal.ZERO;
	
	@OneToMany(mappedBy="journalEntry", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
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

	public List<JournalEntryLine> getLines() {
		return lines;
	}

	public void setLines(List<JournalEntryLine> lines) {
		this.lines = lines;
		calculateCreditAndDebit();
	}
	
	public BigDecimal getDebit() {
		return debit;
	}

	public BigDecimal getCredit() {
		return credit;
	}

	public void addLine(JournalEntryLine line) {
		line.setJournalEntry(this);
		this.lines.add(line);
		calculateCreditAndDebit();
	}
	
	public void removeLines() {
		this.lines = new ArrayList<JournalEntryLine>();
		calculateCreditAndDebit();
	}
	
	public void remove(JournalEntryLine line) {
		this.lines.remove(line);
		calculateCreditAndDebit();
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
	
	public boolean isBalance() {
		if(credit.subtract(debit).equals(BigDecimal.ZERO))
			return true;
		
		return false;
	}
	
	private void calculateCreditAndDebit() {

		BigDecimal c = BigDecimal.ZERO;
		BigDecimal d = BigDecimal.ZERO;
		
		if(lines.size() > 0) {

			for(JournalEntryLine l : this.lines) {
				c = c.add(l.getCredit());
				d = d.add(l.getDebit());
			}
		}
		
		this.credit = c;
		this.debit = d;

	}
	
    @Override
	public boolean equals(Object other) {
		
		if(this == other) 
			return true;
		if(other == null)	
			return false;
		if(!(other instanceof JournalEntry))	
			return false;
		final JournalEntry that = (JournalEntry) other;
		return id != null && id.equals(that.getId());
		
	}

    @Override
    public int hashCode() {
        return 45;
    }
	
}
