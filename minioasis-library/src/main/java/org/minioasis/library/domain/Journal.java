package org.minioasis.library.domain;

import java.io.Serializable;

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
	
	public Journal(){}
	
	public Journal(String issn, String coden) {
		this.issn = issn;
		this.coden = coden;
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

}
