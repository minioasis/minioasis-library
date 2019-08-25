package org.minioasis.library.repository;

import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.RevisionType;
import org.hibernate.envers.query.AuditEntity;
import org.hibernate.envers.query.AuditQuery;
import org.hibernate.envers.query.criteria.MatchMode;
import org.minioasis.library.audit.DeletedAuditEntity;
import org.minioasis.library.domain.Biblio;
import org.minioasis.library.audit.AuditRevisionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Repository
@Transactional
public class BiblioRevisionRepository {

	@PersistenceContext
	private EntityManager entityManager;
	
	@SuppressWarnings("unchecked")
	public Page<DeletedAuditEntity> listDeletedBibliosIn(String title, int days, Pageable pageable) {
		
		int page = pageable.getPageNumber();
		int pageSize = pageable.getPageSize();
		
		long timestamp = System.currentTimeMillis() - (60*60*1000*24*days);
		
        AuditReader auditReader = AuditReaderFactory.get(entityManager);

        AuditQuery query = auditReader.createQuery()
					        		.forRevisionsOfEntity(Biblio.class, false, true)
					        		//.add(AuditEntity.revisionProperty("timestamp").gt(timestamp))
					        		.add(AuditEntity.revisionType().eq(RevisionType.DEL));
        
		if(title != null && !title.isEmpty()) {
			query.add(AuditEntity.property("title").ilike(title, MatchMode.ANYWHERE));
		}
					        		

        List<Object[]> results = query.setFirstResult(page * pageSize)
        							.setMaxResults(pageSize)
        							.getResultList();
        
        List<DeletedAuditEntity> deletedEntities = new ArrayList<DeletedAuditEntity>();
        
        for(Object[] r : results) {
   
        	Biblio b = (Biblio)r[0];
        	AuditRevisionEntity e = (AuditRevisionEntity)r[1];

        	DeletedAuditEntity deleted = new DeletedAuditEntity();
        	
        	deleted.setRev(e.getId());
        	deleted.setTimestamp(millsToLocalDateTime(e.getTimestamp()));
        	deleted.setUsername(e.getUsername());
        	deleted.setEntityId(b.getId());
        	
        	deletedEntities.add(deleted);
        }
        
        long total = totalDeletedBibliosIn(title, days);

        return new PageImpl<DeletedAuditEntity>(deletedEntities, pageable, total);
    }
	
	private long totalDeletedBibliosIn(String title, int days) {
		
		long timestamp = System.currentTimeMillis() - (60*60*1000*24*days);
		
        AuditReader auditReader = AuditReaderFactory.get(entityManager);

        AuditQuery query = auditReader.createQuery()
					        		.forRevisionsOfEntity(Biblio.class, false, true)
					        		.addProjection(AuditEntity.id().count())
					        		//.add(AuditEntity.revisionProperty("timestamp").gt(timestamp))
					        		.add(AuditEntity.revisionType().eq(RevisionType.DEL));
		
		if(title != null && !title.isEmpty()) {
			query.add(AuditEntity.property("title").ilike(title, MatchMode.ANYWHERE));
		}

        return (Long)query.getSingleResult();
	}
	
	public static LocalDateTime millsToLocalDateTime(long millis) {
	      Instant instant = Instant.ofEpochMilli(millis);
	      LocalDateTime date = instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
	      return date;
	  }

}
