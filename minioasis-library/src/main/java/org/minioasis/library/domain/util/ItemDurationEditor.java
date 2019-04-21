package org.minioasis.library.domain.util;

import java.beans.PropertyEditorSupport;

import org.minioasis.library.domain.ItemDuration;
import org.minioasis.library.service.LibraryService;

public class ItemDurationEditor extends PropertyEditorSupport {

	private LibraryService service;
	
	public ItemDurationEditor(LibraryService service) {
		super();
		this.service = service;
	}

	public void setService(LibraryService service) {
		this.service = service;
	}

	public void setAsText(String text) throws IllegalArgumentException {
		int id = Integer.parseInt(text);
		ItemDuration itemDuration = service.getItemDuration(id);
		setValue(itemDuration);
	}
	
}
