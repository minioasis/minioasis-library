package org.minioasis.library.audit.repository;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.RevisionType;
import org.hibernate.envers.query.AuditEntity;
import org.hibernate.envers.query.AuditQuery;
import org.hibernate.envers.query.criteria.MatchMode;
import org.minioasis.library.domain.Patron;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public class PatronRevisionRepository {

	@PersistenceContext
	private EntityManager entityManager;
	
	@SuppressWarnings("unchecked")
	public Page<Object[]> listDeletedPatronsIn(String username, int days, Pageable pageable) {
		
		int page = pageable.getPageNumber();
		int pageSize = pageable.getPageSize();
		
		Instant before = Instant.now().minusSeconds(60*60*24*days);	
		long from = before.toEpochMilli();
		
        AuditReader auditReader = AuditReaderFactory.get(entityManager);

        AuditQuery query = auditReader.createQuery()
					        		.forRevisionsOfEntity(Patron.class, false, true)
					        		.add(AuditEntity.revisionProperty("timestamp").gt(from))
					        		.add(AuditEntity.revisionType().eq(RevisionType.DEL));
        
        if(username != null && ! username.isEmpty()) {
        	query.add(AuditEntity.revisionProperty("username").ilike(username, MatchMode.ANYWHERE));
        }

        List<Object[]> results = query.setFirstResult(page * pageSize)
        							.setMaxResults(pageSize)
        							.getResultList();
        
        long total = totalDeletedPatronsIn(username, days);

        return new PageImpl<Object[]>(results, pageable, total);
    }
	
	private long totalDeletedPatronsIn(String username, int days) {
		
		Instant before = Instant.now().minusSeconds(60*60*24*days);	
		long from = before.toEpochMilli();

        AuditReader auditReader = AuditReaderFactory.get(entityManager);

        AuditQuery query = auditReader.createQuery()
					        		.forRevisionsOfEntity(Patron.class, false, true)
					        		.addProjection(AuditEntity.id().count())
					        		.add(AuditEntity.revisionProperty("timestamp").gt(from))
					        		.add(AuditEntity.revisionType().eq(RevisionType.DEL));

        if(username != null && ! username.isEmpty()) {
        	query.add(AuditEntity.revisionProperty("username").ilike(username, MatchMode.ANYWHERE));
        }
        
        return (Long)query.getSingleResult();
	}
	
	public static LocalDateTime millsToLocalDateTime(long millis) {
		Instant instant = Instant.ofEpochMilli(millis);
		LocalDateTime date = instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
		return date;
	}
	
}
