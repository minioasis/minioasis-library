package org.minioasis.library.repository;

import java.util.Date;
import java.util.List;

import org.minioasis.library.domain.Group;

public interface PatronRepositoryCustom {

	int bulkUpdateGroup(List<Long> ids, Group group, Date now);
	
}
