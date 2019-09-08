package org.minioasis.library.domain;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;
import org.hibernate.validator.constraints.Length;

@Entity
@Audited
@Table(name = "journal_entry_line")
public class JournalEntryLine implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name = "id", unique = true, nullable = false)
	private Long id;
	
	@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="journalentry_id" , nullable = false, foreignKey = @ForeignKey(name = "fk_journalentryline_journalentry"))
	private JournalEntry journalEntry;
    
	@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="account_id" , nullable = false, foreignKey = @ForeignKey(name = "fk_journalentryline_account"))
    private Account account;
    
	@NotNull
	@Length(max = 128)
	@Column(name = "description", nullable = false)
	private String description;
	
	@Length(max = 256)
	@Column(name = "reference")
	private String reference;
	
	@NotNull
	@Column(name = "debit" , columnDefinition = "DECIMAL(12,2)" , nullable = false)
    private BigDecimal debit = BigDecimal.ZERO;
	
	@NotNull
	@Column(name = "credit" , columnDefinition = "DECIMAL(12,2)" , nullable = false)
    private BigDecimal credit = BigDecimal.ZERO;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public JournalEntry getJournalEntry() {
		return journalEntry;
	}

	public void setJournalEntry(JournalEntry journalEntry) {
		this.journalEntry = journalEntry;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public BigDecimal getDebit() {
		return debit;
	}

	public void setDebit(BigDecimal debit) {
		this.debit = debit;
	}

	public BigDecimal getCredit() {
		return credit;
	}

	public void setCredit(BigDecimal credit) {
		this.credit = credit;
	}
	
	public BigDecimal sum() {
		return credit.subtract(debit);
	}
	
    @Override
	public boolean equals(Object other) {
		
		if(this == other) 
			return true;
		if(other == null)	
			return false;
		if(!(other instanceof JournalEntryLine))	
			return false;
		final JournalEntryLine that = (JournalEntryLine) other;
		return id != null && id.equals(that.getId());
		
	}

    @Override
    public int hashCode() {
        return 45;
    }
	
}
