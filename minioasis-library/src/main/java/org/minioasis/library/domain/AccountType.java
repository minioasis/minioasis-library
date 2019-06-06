package org.minioasis.library.domain;

public enum AccountType {

	// Sale
	SALE("Sale"), OTHER_INCOME("Other Income"), 
	// Asset
	PATRON("Patron"), CASH("Cash"), BANK("Bank"), UNPAID_DEPOSIT("Unpaid Deposit"), 
	// Liability	
	DEPOSIT("Deposit");

	private final String value;

	private AccountType(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
	
}
