package org.minioasis.library.domain;

public enum Binding {
	
	S("Soft Cover"), H("Hard Cover");

	private final String value;

	private Binding(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

}
