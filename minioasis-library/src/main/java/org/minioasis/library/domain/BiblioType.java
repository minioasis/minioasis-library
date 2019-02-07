package org.minioasis.library.domain;

public enum BiblioType {

	BOOK("Book"), JOURNAL("Journal");

	private final String value;

	private BiblioType(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

}