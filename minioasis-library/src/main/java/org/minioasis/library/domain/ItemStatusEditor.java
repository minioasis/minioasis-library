package org.minioasis.library.domain;

import java.beans.PropertyEditorSupport;

import org.minioasis.library.service.LibraryService;

public class ItemStatusEditor extends PropertyEditorSupport {

	private LibraryService service;
	
	public ItemStatusEditor(LibraryService service) {
		super();
		this.service = service;
	}

	public void setService(LibraryService service) {
		this.service = service;
	}

	public void setAsText(String text) throws IllegalArgumentException {
		int id = Integer.parseInt(text);
		ItemStatus status = service.getItemStatus(id);
		setValue(status);
	}
	
}
