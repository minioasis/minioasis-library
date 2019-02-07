package org.minioasis.library.domain;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;

@Embeddable
public class Contact implements Serializable {

	private static final long serialVersionUID = -8992287444437744200L;

	@Length(max = 20)
	private String tel;
	
	@NotNull
	@Length(max = 20)
	private String mobile;
	
	@Length(max = 64)
	//@Email
	private String email;

	@Valid
	private Address address;
		
	public Contact() {
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Address getAddress() {
		return address;
	}
	public void setAddress(Address address) {
		this.address = address;
	}
	
}
