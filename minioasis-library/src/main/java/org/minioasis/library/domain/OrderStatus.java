package org.minioasis.library.domain;

public enum OrderStatus {

	IN_PROGRESS("In Progress"), CANCELLED("Cancelled"), MAILED("Mailed"), PARTIALLY_SHIPPED("Partially Shipped"), COMPLETED("Completed");

	private final String value;

	private OrderStatus(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
	
}
