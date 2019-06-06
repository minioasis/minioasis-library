package org.minioasis.library.repository;

import static org.minioasis.library.jooq.tables.Account.ACCOUNT;
import static org.minioasis.library.jooq.tables.Txn.TXN;

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
import org.minioasis.library.domain.Txn;
import org.minioasis.library.domain.search.TxnCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

public class TxnRepositoryImpl implements TxnRepositoryCustom {

	@PersistenceContext
	private EntityManager em;
	
	@Autowired
	private DSLContext dsl;
	
	private org.minioasis.library.jooq.tables.Txn t = TXN.as("t");
	private org.minioasis.library.jooq.tables.Account a = ACCOUNT.as("a");
	private org.minioasis.library.jooq.tables.Account b = ACCOUNT.as("b");
	
	public Page<Txn> findByCriteria(TxnCriteria criteria, Pageable pageable){
		
		Table<?> table = createTable();
		
		org.jooq.Query jooqQuery = dsl.select()
									.from(table)
									.where(condition(criteria))
									.limit(pageable.getPageSize())
									.offset((int)pageable.getOffset());
		
		Query q = em.createNativeQuery(jooqQuery.getSQL(), Txn.class);
		setBindParameterValues(q, jooqQuery);
		
		List<Txn> txns = q.getResultList();
		
		long total = findCountByCriteriaLikeExpression(criteria);
		
		return new PageImpl<>(txns, pageable, total);
	}
	
	private long findCountByCriteriaLikeExpression(TxnCriteria criteria) {

		Table<?> table = createTable();
		
        long total = dsl.fetchCount(
        						dsl.select(t.ID)
        						.from(table)
        						.where(condition(criteria))
        );

        return total;
    }
	
	private Table<?> createTable() {
		
		Table<?> table = t;
		
		table = table.innerJoin(a).on(t.ACCOUNT_ID.eq(a.ID))
				 .innerJoin(b).on(t.ACCOUNT_ID.eq(b.ID));
		
		return table;
		
	}
	
	private static void setBindParameterValues(Query hibernateQuery, org.jooq.Query jooqQuery) {
	    List<Object> values = jooqQuery.getBindValues();
	    for (int i = 0; i < values.size(); i++) {
	        hibernateQuery.setParameter(i + 1, values.get(i));
	    }
	}
	
	private Condition condition(TxnCriteria criteria) {
		
	    Condition condition = DSL.trueCondition();
	    
		final String description = criteria.getDescription();
		final String code1 = criteria.getCode1();
		final Set<AccountType> types1 = criteria.getAccountTypes();
		final String code2 = criteria.getCode2();
		final LocalDate txnDateFrom = criteria.getTxnDateFrom();
		final LocalDate txnDateTo = criteria.getTxnDateTo();
		
		if(description != null) {
			condition = condition.and(t.DESCRIPTION.likeIgnoreCase("%" + description + "%"));
		}
		if(code1 != null) {
			condition = condition.and(a.CODE.eq(code1));
		}
		if(code2 != null) {
			condition = condition.and(b.CODE.eq(code2));
		}
		if(txnDateFrom != null && txnDateTo != null){
			condition = condition.and(t.TXN_DATE.ge(java.sql.Date.valueOf(txnDateFrom))
								 .and(t.TXN_DATE.le(java.sql.Date.valueOf(txnDateTo))));
		}
		if(types1 != null && types1.size() > 0){
			condition = condition.and(a.TYPE.in(types1));
		}
		
	    return condition;
	}

}
