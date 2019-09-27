package org.minioasis.library.domain;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.hibernate.validator.constraints.Length;

@Embeddable
public class Journal implements Serializable {

	private static final long serialVersionUID = 3862993347942371358L;
	
	@Length(max = 15)
	@Column(name = "issn")
	private String issn;
	
	@Length(max = 16)
	private String coden;
	
	@Column(name = "publishing_date")
	private LocalDate publishingDate;
	
	@Length(max = 64)
	@Column(name = "volume_no" , length = 64)
	private String volumeNo;
	
	public Journal(){}

	public Journal(@Length(max = 15) String issn, @Length(max = 16) String coden, LocalDate publishingDate,
			@Length(max = 32) String volumeNo) {
		super();
		this.issn = issn;
		this.coden = coden;
		this.publishingDate = publishingDate;
		this.volumeNo = volumeNo;
	}

	public String getIssn() {
		return issn;
	}

	public void setIssn(String issn) {
		this.issn = issn;
	}

	public String getCoden() {
		return coden;
	}

	public void setCoden(String coden) {
		this.coden = coden;
	}

	public LocalDate getPublishingDate() {
		return publishingDate;
	}

	public void setPublishingDate(LocalDate publishingDate) {
		this.publishingDate = publishingDate;
	}

	public String getVolumeNo() {
		return volumeNo;
	}

	public void setVolumeNo(String volumeNo) {
		this.volumeNo = volumeNo;
	}

}
