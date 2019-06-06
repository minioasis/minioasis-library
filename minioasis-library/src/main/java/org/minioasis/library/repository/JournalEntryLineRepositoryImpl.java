package org.minioasis.library.repository;

import static org.minioasis.library.jooq.tables.Account.ACCOUNT;
import static org.minioasis.library.jooq.tables.JournalEntry.JOURNAL_ENTRY;
import static org.minioasis.library.jooq.tables.JournalEntryLine.JOURNAL_ENTRY_LINE;

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
import org.minioasis.library.domain.JournalEntryLine;
import org.minioasis.library.domain.search.JournalEntryLineCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

public class JournalEntryLineRepositoryImpl implements JournalEntryLineRepositoryCustom {

	@PersistenceContext
	private EntityManager em;
	
	@Autowired
	private DSLContext dsl;
	
	private org.minioasis.library.jooq.tables.JournalEntry j = JOURNAL_ENTRY.as("j");
	private org.minioasis.library.jooq.tables.JournalEntryLine jl = JOURNAL_ENTRY_LINE.as("jl");
	private org.minioasis.library.jooq.tables.Account a = ACCOUNT.as("a");
	private org.minioasis.library.jooq.tables.Account b = ACCOUNT.as("b");
	
	public Page<JournalEntryLine> findByCriteria(JournalEntryLineCriteria criteria, Pageable pageable){
		
		Table<?> table = createTable();
		
		org.jooq.Query jooqQuery = dsl.select()
									.from(table)
									.where(condition(criteria))
									.limit(pageable.getPageSize())
									.offset((int)pageable.getOffset());
		
		Query q = em.createNativeQuery(jooqQuery.getSQL(), JournalEntryLine.class);
		setBindParameterValues(q, jooqQuery);
		
		List<JournalEntryLine> lines = q.getResultList();
		
		long total = findCountByCriteriaLikeExpression(criteria);
		
		return new PageImpl<>(lines, pageable, total);
	}
	
	private long findCountByCriteriaLikeExpression(JournalEntryLineCriteria criteria) {

		Table<?> table = createTable();
		
        long total = dsl.fetchCount(
        						dsl.select(jl.ID)
        						.from(table)
        						.where(condition(criteria))
        );

        return total;
    }
	
	private Table<?> createTable() {
		
		Table<?> table = jl;
		
		table = table.innerJoin(j).on(jl.JOURNALENTRY_ID.eq(j.ID))
					 .innerJoin(a).on(jl.ACCOUNT_ID.eq(a.ID))
				 	 .innerJoin(b).on(jl.ACCOUNT_ID.eq(b.ID));
		
		return table;
		
	}
	
	private static void setBindParameterValues(Query hibernateQuery, org.jooq.Query jooqQuery) {
	    List<Object> values = jooqQuery.getBindValues();
	    for (int i = 0; i < values.size(); i++) {
	        hibernateQuery.setParameter(i + 1, values.get(i));
	    }
	}
	
	private Condition condition(JournalEntryLineCriteria criteria) {
		
	    Condition condition = DSL.trueCondition();
	    
		final String description = criteria.getDescription();
		final String code1 = criteria.getCode1();
		final Set<AccountType> types1 = criteria.getAccountTypes();
		final String code2 = criteria.getCode2();
		final LocalDate txnDateFrom = criteria.getTxnDateFrom();
		final LocalDate txnDateTo = criteria.getTxnDateTo();
		
		if(description != null) {
			condition = condition.and(jl.DESCRIPTION.likeIgnoreCase("%" + description + "%"));
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
