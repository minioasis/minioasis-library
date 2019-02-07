package org.minioasis.library.domain;

import java.beans.PropertyEditorSupport;

import org.minioasis.library.service.LibraryService;

public class GroupEditor  extends PropertyEditorSupport {

	private LibraryService service;
	
	public GroupEditor(LibraryService service) {
		super();
		this.service = service;
	}

	public void setService(LibraryService service) {
		this.service = service;
	}

	public void setAsText(String text) throws IllegalArgumentException {
		int id = Integer.parseInt(text);
		Group group = service.getGroup(id);
		setValue(group);
	}
}
