package org.minioasis.library.domain;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

@Embeddable
public class Address {

	@Length(max = 64)
	@NotNull(message = "{notnull}")
	private String address1;
	@Length(max = 64)
	private String address2;
	@Length(max = 64)
	private String address3;
	@Length(max = 64)
	private String city;
	@Length(max = 64)
	private String state;
	@Length(max = 20)
	private String postcode;
	@Length(max = 128)
	private String country;
	
	public Address(){}
	
	public String getAddress1() {
		return address1;
	}
	public void setAddress1(String address1) {
		this.address1 = address1;
	}
	public String getAddress2() {
		return address2;
	}
	public void setAddress2(String address2) {
		this.address2 = address2;
	}
	public String getAddress3() {
		return address3;
	}
	public void setAddress3(String address3) {
		this.address3 = address3;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getPostcode() {
		return postcode;
	}
	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	
}
