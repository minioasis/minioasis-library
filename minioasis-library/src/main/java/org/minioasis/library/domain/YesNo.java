package org.minioasis.library.domain;

public enum YesNo {

	Y("yes"), N("no");

	private final String value;

	private YesNo(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}

