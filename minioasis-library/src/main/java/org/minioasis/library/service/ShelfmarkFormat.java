package org.minioasis.library.service;

import java.util.Set;

import org.minioasis.library.domain.search.Shelfmark;

public interface ShelfmarkFormat {
	
	Set<Shelfmark> format(Set<Shelfmark> shelfmarks);
}
