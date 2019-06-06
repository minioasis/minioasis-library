package org.minioasis.library.repository;

import static org.minioasis.library.jooq.tables.Account.ACCOUNT;
import static org.minioasis.library.jooq.tables.JournalEntry.JOURNAL_ENTRY;

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
import org.minioasis.library.domain.AccountType;
import org.minioasis.library.domain.JournalEntry;
import org.minioasis.library.domain.search.JournalEntryCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

public class JournalEntryRepositoryImpl implements JournalEntryRepositoryCustom {

	@PersistenceContext
	private EntityManager em;
	
	@Autowired
	private DSLContext dsl;
	
	private org.minioasis.library.jooq.tables.JournalEntry j = JOURNAL_ENTRY.as("j");
	private org.minioasis.library.jooq.tables.Account a = ACCOUNT.as("a");
	private org.minioasis.library.jooq.tables.Account b = ACCOUNT.as("b");
	
	public Page<JournalEntry> findByCriteria(JournalEntryCriteria criteria, Pageable pageable){
		
		Table<?> table = createTable();
		
		org.jooq.Query jooqQuery = dsl.select()
									.from(table)
									.where(condition(criteria))
									.limit(pageable.getPageSize())
									.offset((int)pageable.getOffset());
		
		Query q = em.createNativeQuery(jooqQuery.getSQL(), JournalEntry.class);
		setBindParameterValues(q, jooqQuery);
		
		List<JournalEntry> txns = q.getResultList();
		
		long total = findCountByCriteriaLikeExpression(criteria);
		
		return new PageImpl<>(txns, pageable, total);
	}
	
	private long findCountByCriteriaLikeExpression(JournalEntryCriteria criteria) {

		Table<?> table = createTable();
		
        long total = dsl.fetchCount(
        						dsl.select(j.ID)
        						.from(table)
        						.where(condition(criteria))
        );

        return total;
    }
	
	private Table<?> createTable() {
		
		Table<?> table = j;
		
		table = table.innerJoin(a).on(j.ACCOUNT_ID.eq(a.ID))
				 .innerJoin(b).on(j.ACCOUNT_ID.eq(b.ID));
		
		return table;
		
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
		final String code1 = criteria.getCode1();
		final Set<AccountType> types1 = criteria.getAccountTypes();
		final String code2 = criteria.getCode2();
		final LocalDate txnDateFrom = criteria.getTxnDateFrom();
		final LocalDate txnDateTo = criteria.getTxnDateTo();
		
		if(description != null) {
			condition = condition.and(j.DESCRIPTION.likeIgnoreCase("%" + description + "%"));
		}
		if(code1 != null) {
			condition = condition.and(a.CODE.eq(code1));
		}
		if(code2 != null) {
			condition = condition.and(b.CODE.eq(code2));
		}
		if(txnDateFrom != null && txnDateTo != null){
			condition = condition.and(j.TXN_DATE.ge(java.sql.Date.valueOf(txnDateFrom))
								 .and(j.TXN_DATE.le(java.sql.Date.valueOf(txnDateTo))));
		}
		if(types1 != null && types1.size() > 0){
			condition = condition.and(a.TYPE.in(types1));
		}
		
	    return condition;
	}

}
