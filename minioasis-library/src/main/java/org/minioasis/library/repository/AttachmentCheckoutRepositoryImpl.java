package org.minioasis.library.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Table;
import org.jooq.impl.DSL;
import org.minioasis.library.domain.AttachmentCheckout;
import org.minioasis.library.domain.AttachmentCheckoutState;
import org.minioasis.library.domain.search.AttachmentCheckoutCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import static org.minioasis.library.jooq.tables.AttachmentCheckout.ATTACHMENT_CHECKOUT;
import static org.minioasis.library.jooq.tables.Patron.PATRON;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.minioasis.library.jooq.tables.Attachment.ATTACHMENT;

public class AttachmentCheckoutRepositoryImpl implements AttachmentCheckoutRepositoryCustom {

	@PersistenceContext
	private EntityManager em;
	
	@Autowired
	private DSLContext dsl;
	
	private org.minioasis.library.jooq.tables.AttachmentCheckout ac = ATTACHMENT_CHECKOUT.as("ac");
	private org.minioasis.library.jooq.tables.Patron p = PATRON.as("p");
	private org.minioasis.library.jooq.tables.Attachment a = ATTACHMENT.as("a");
	
	public Page<AttachmentCheckout> findByCriteria(AttachmentCheckoutCriteria criteria, Pageable pageable){

		Table<?> table = createTable(criteria);
	
		org.jooq.Query jooqQuery = dsl.select()
									.from(table)
									.where(condition(criteria))
									.limit(pageable.getPageSize())
									.offset((int)pageable.getOffset());
		
		Query q = em.createNativeQuery(jooqQuery.getSQL(), AttachmentCheckout.class);
		setBindParameterValues(q, jooqQuery);
		
		List<AttachmentCheckout> acs = q.getResultList();
		
		long total = findCountByCriteriaLikeExpression(criteria);
		
		return new PageImpl<>(acs, pageable, total);
	}
	
	private long findCountByCriteriaLikeExpression(AttachmentCheckoutCriteria criteria) {

		Table<?> table = createTable(criteria);
		
        long total = dsl.fetchCount(
        						dsl.select(ac.ID)
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
	
	private Table<?> createTable(AttachmentCheckoutCriteria criteria) {

		final String barcode = criteria.getBarcode();
		final String cardkey = criteria.getCardkey();
		final String description = criteria.getKeyword();
		
		Table<?> table = ac;

		if(cardkey != null) {
			table = table.innerJoin(p).on(ac.PATRON_ID.eq(p.ID)).and(p.CARD_KEY.eq(cardkey));
		}	
		if(barcode != null || description != null) {
			table = table.innerJoin(a).on(ac.ATTACHMENT_ID.eq(a.ID)).and(onCondition(criteria));
		}
		
		return table;
	}

	private Condition onCondition(AttachmentCheckoutCriteria criteria) {
		
		Condition condition = DSL.trueCondition();
		
		final String barcode = criteria.getBarcode();
		final String description = criteria.getKeyword();
		
		if(barcode != null) {		
			condition = condition.and(a.BARCODE.eq(barcode));
		}	
		if(description != null) {
			condition = condition.and(a.DESCRIPTION.likeIgnoreCase("%" + description + "%"));
		}
		
		return condition;
	}

	    
	private Condition condition(AttachmentCheckoutCriteria criteria) {
		
	    Condition condition = DSL.trueCondition();
	    
		final LocalDate checkoutFrom = criteria.getCheckoutFrom();
		final LocalDate checkoutTo = criteria.getCheckoutTo();
		final LocalDate doneFrom = criteria.getDoneFrom();
		final LocalDate doneTo = criteria.getDoneTo();
		final Set<AttachmentCheckoutState> states = criteria.getStates();		
		
		if(checkoutFrom != null && checkoutTo != null){
			condition = condition.and(ac.CHECKOUT_DATE.ge(java.sql.Date.valueOf(checkoutFrom))
							.and(ac.CHECKOUT_DATE.le(java.sql.Date.valueOf(checkoutTo))));
		}
		if(doneFrom != null && doneTo != null){
			condition = condition.and(ac.DONE.ge(java.sql.Date.valueOf(doneFrom))
							.and(ac.DONE.le(java.sql.Date.valueOf(doneTo))));
		}
		if(states != null && states.size() > 0){
			condition = condition.and(ac.STATE.in(states));
		}
		
	    return condition;
	}
}
