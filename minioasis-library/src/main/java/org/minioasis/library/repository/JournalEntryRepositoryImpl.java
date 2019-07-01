package org.minioasis.library.repository;

import static org.minioasis.library.jooq.tables.JournalEntry.JOURNAL_ENTRY;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Table;
import org.jooq.impl.DSL;
import org.minioasis.library.domain.JournalEntry;
import org.minioasis.library.domain.JournalEntryLine;
import org.minioasis.library.domain.search.JournalEntryCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

public class JournalEntryRepositoryImpl implements JournalEntryRepositoryCustom{

	@PersistenceContext
	private EntityManager em;
	
	@Autowired
	private DSLContext dsl;
	
	private org.minioasis.library.jooq.tables.JournalEntry j = JOURNAL_ENTRY.as("j");
	
	public Page<JournalEntry> findByCriteria(JournalEntryCriteria criteria, Pageable pageable){
		
		Table<?> table = j;
		
		org.jooq.Query jooqQuery = dsl.select()
									.from(table)
									.where(condition(criteria))
									.limit(pageable.getPageSize())
									.offset((int)pageable.getOffset());
		
		Query q = em.createNativeQuery(jooqQuery.getSQL(), JournalEntryLine.class);
		setBindParameterValues(q, jooqQuery);
		
		List<JournalEntry> list = q.getResultList();
		
		long total = findCountByCriteriaLikeExpression(criteria);
		
		return new PageImpl<>(list, pageable, total);
	}
	
	private long findCountByCriteriaLikeExpression(JournalEntryCriteria criteria) {

		Table<?> table = j;
		
        long total = dsl.fetchCount(
        						dsl.select(j.ID)
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
	
	private Condition condition(JournalEntryCriteria criteria) {
		
	    Condition condition = DSL.trueCondition();
	    
		final String description = criteria.getDescription();
		final String createdBy = criteria.getCreatedBy();
		final String updatedBy = criteria.getUpdatedBy();
		final LocalDate txnDateFrom = criteria.getTxnDateFrom();
		final LocalDate txnDateTo = criteria.getTxnDateTo();
		final BigDecimal fromTotal = criteria.getFromTotal();
		final BigDecimal toTotal = criteria.getToTotal();
		
		if(description != null) {
			condition = condition.and(j.DESCRIPTION.likeIgnoreCase("%" + description + "%"));
		}
		if(createdBy != null) {
			condition = condition.and(j.CREATED_BY.eq(createdBy));
		}
		if(updatedBy != null) {
			condition = condition.and(j.UPDATED_BY.eq(updatedBy));
		}
		if(txnDateFrom != null && txnDateTo != null){
			condition = condition.and(j.TXN_DATE.ge(java.sql.Date.valueOf(txnDateFrom))
								 .and(j.TXN_DATE.le(java.sql.Date.valueOf(txnDateTo))));
		}
		if(fromTotal != null && toTotal != null){
			condition = condition.and(j.CREDIT.ge(fromTotal)
								 .and(j.CREDIT.le(toTotal)));
		}
		
	    return condition;
	}
}
