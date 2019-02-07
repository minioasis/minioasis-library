package org.minioasis.library.domain;

public enum Language {

	cn("Chinese"),en("English"),my("Malay"),ot("Other");

	private final String value;

	private Language(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
