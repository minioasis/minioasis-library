package org.minioasis.library.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
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
	
	@CreatedDate
	@Column(name = "created")
    private LocalDateTime created = LocalDateTime.now();
	
	@LastModifiedDate
	@Column(name = "updated")
    private LocalDateTime updated;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="account_id" , nullable = false, foreignKey = @ForeignKey(name = "fk_txn_account"))
    private Account account;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="to_account_id" , foreignKey = @ForeignKey(name = "fk_txn_toaccount"))
    private Account toAccount;
    
	@NotNull
	@Column(name = "debit" , columnDefinition = "DECIMAL(12,2)" , nullable = false)
    private BigDecimal debit = BigDecimal.ZERO;
	
	@NotNull
	@Column(name = "credit" , columnDefinition = "DECIMAL(12,2)" , nullable = false)
    private BigDecimal credit = BigDecimal.ZERO;
    
	@Column(name = "record_by")
    private String recordBy;
    
	@Column(name = "updated_by")
    private String updatedBy;
    
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
	public LocalDateTime getCreated() {
		return created;
	}
	public void setCreated(LocalDateTime created) {
		this.created = created;
	}
	public LocalDateTime getUpdated() {
		return updated;
	}
	public void setUpdated(LocalDateTime updated) {
		this.updated = updated;
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
	public Account getAccount() {
		return account;
	}
	public void setAccount(Account account) {
		this.account = account;
	}
	public Account getToAccount() {
		return toAccount;
	}
	public void setToAccount(Account toAccount) {
		this.toAccount = toAccount;
	}
	public String getRecordBy() {
		return recordBy;
	}
	public void setRecordBy(String recordBy) {
		this.recordBy = recordBy;
	}
	public String getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}
	
}
