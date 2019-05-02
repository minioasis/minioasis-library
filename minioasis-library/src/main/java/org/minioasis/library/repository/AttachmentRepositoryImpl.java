package org.minioasis.library.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Table;
import org.jooq.impl.DSL;
import org.minioasis.library.domain.Attachment;
import org.minioasis.library.domain.AttachmentState;
import org.minioasis.library.domain.YesNo;
import org.minioasis.library.domain.search.AttachmentCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import static org.minioasis.library.jooq.tables.Item.ITEM;
import static org.minioasis.library.jooq.tables.Attachment.ATTACHMENT;

public class AttachmentRepositoryImpl implements AttachmentRepositoryCustom {
	
	@PersistenceContext
	private EntityManager em;

	@Autowired
	private DSLContext dsl;
	
	private org.minioasis.library.jooq.tables.Item i = ITEM.as("i");
	private org.minioasis.library.jooq.tables.Attachment a = ATTACHMENT.as("a");

	public Page<Attachment> findByCriteria(AttachmentCriteria criteria, Pageable pageable){

		Table<?> table = createTable(criteria);
	
		org.jooq.Query jooqQuery = dsl.select()
									.from(table)
									.where(condition(criteria))
									.limit(pageable.getPageSize())
									.offset((int)pageable.getOffset());
		
		Query q = em.createNativeQuery(jooqQuery.getSQL(), Attachment.class);
		setBindParameterValues(q, jooqQuery);
		
		List<Attachment> attachments = q.getResultList();
		
		long total = findCountByCriteriaLikeExpression(criteria);
		
		return new PageImpl<>(attachments, pageable, total);
	}
	
	private long findCountByCriteriaLikeExpression(AttachmentCriteria criteria) {

		Table<?> table = createTable(criteria);
		
        long total = dsl.fetchCount(
        						dsl.select(a.ID)
        						.from(table)
        						.where(condition(criteria))
        );

        return total;
    }
	
	private static void setBindParameterValues(Query hibernateQuery, org.jooq.Query jooqQuery) {
	    List<Object> values = jooqQuery.getBindValues();
	    for (int i = 0; i < values.size(); i++) {
	        hibernateQuery.setParameter(i + 1, values.get(i));
	    }
	}
	
	private Table<?> createTable(AttachmentCriteria criteria) {
		
		final String itemBarcode = criteria.getItemBarcode();
		
		Table<?> table = a;
		
		if(itemBarcode != null) {
			table = table.innerJoin(i).on(a.ITEM_ID.eq(i.ID)).and(i.BARCODE.eq(itemBarcode));
		}
		
		return table;
	}
	
	private Condition condition(AttachmentCriteria criteria) {
		
	    Condition condition = DSL.trueCondition();
	    
		final String keyword = criteria.getKeyword();
		final LocalDate firstFrom = criteria.getFirstCheckinFrom();
		final LocalDate firstTo = criteria.getFirstCheckinTo();
		final LocalDate lastFrom = criteria.getLastCheckinFrom();
		final LocalDate lastTo = criteria.getLastCheckinTo();
		
		final Set<AttachmentState> states = criteria.getStates();
		final Set<YesNo> borrowables = criteria.getBorrowables();
		
	    if (keyword != null) {
	    	condition = condition.and(a.BARCODE.likeIgnoreCase("%" + keyword + "%"))
	    					.or(a.CALL_NO.likeIgnoreCase("%" + keyword + "%"))
	    					.or(a.DESCRIPTION.likeIgnoreCase("%" + keyword + "%"));				
	    }
		if(firstFrom != null && firstTo != null){
			condition = condition.and(a.FIRST_CHECKIN.ge(java.sql.Date.valueOf(firstFrom))
							.and(a.FIRST_CHECKIN.le(java.sql.Date.valueOf(firstTo))));
		}
		if(lastFrom != null && lastTo != null){
			condition = condition.and(a.LAST_CHECKIN.ge(java.sql.Date.valueOf(lastFrom))
							.and(a.LAST_CHECKIN.le(java.sql.Date.valueOf(lastTo))));
		}
		if(states != null && states.size() > 0){
			condition = condition.and(a.STATE.in(states));
		}
		if(borrowables != null && borrowables.size() > 0){
			condition = condition.and(a.BORROWABLE.in(borrowables));
		}
		
	    return condition;
	}
}
