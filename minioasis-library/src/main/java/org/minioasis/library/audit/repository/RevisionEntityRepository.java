package org.minioasis.library.audit.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.minioasis.library.audit.AuditRevisionEntity;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public class RevisionEntityRepository {

	@PersistenceContext
	private EntityManager entityManager;
	
	public AuditRevisionEntity getRevisionEntity(Integer rev) {
		
        AuditReader auditReader = AuditReaderFactory.get(entityManager);
        return auditReader.findRevision(AuditRevisionEntity.class, rev);
    }
}
