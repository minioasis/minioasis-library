package org.minioasis.library.domain.search;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import org.minioasis.library.domain.AccountType;

public class AccountCriteria implements Serializable {

	private static final long serialVersionUID = 8548576385592295069L;
	
	private String code;
	private String name;
	private Set<AccountType> types = new HashSet<AccountType>();
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Set<AccountType> getTypes() {
		return types;
	}
	public void setTypes(Set<AccountType> types) {
		this.types = types;
	}

}
