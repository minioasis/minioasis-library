package org.minioasis.library.domain;

public enum BiblioType {

	BOOK("Book"), JOURNAL("Journal"), EBOOK("Ebook"), ART_MEDIA("Art Media"), VIDEO("Video"), AUDIO("Audio");

	private final String value;

	private BiblioType(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

}