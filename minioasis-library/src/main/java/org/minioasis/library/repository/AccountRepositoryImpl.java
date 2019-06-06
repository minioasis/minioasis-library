package org.minioasis.library.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.minioasis.library.domain.Account;
import org.minioasis.library.domain.AccountType;
import org.minioasis.library.domain.AttachmentCheckout;
import org.minioasis.library.domain.search.AccountCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Set;

import static org.minioasis.library.jooq.tables.Account.ACCOUNT;

public class AccountRepositoryImpl implements AccountRepositoryCustom{

	@PersistenceContext
	private EntityManager em;
	
	@Autowired
	private DSLContext dsl;
	
	private org.minioasis.library.jooq.tables.Account a = ACCOUNT.as("a");
	
	public Page<Account> findByCriteria(AccountCriteria criteria, Pageable pageable){
	
		org.jooq.Query jooqQuery = dsl.select()
									.from(a)
									.where(condition(criteria))
									.limit(pageable.getPageSize())
									.offset((int)pageable.getOffset());
		
		Query q = em.createNativeQuery(jooqQuery.getSQL(), AttachmentCheckout.class);
		setBindParameterValues(q, jooqQuery);
		
		List<Account> acs = q.getResultList();
		
		long total = findCountByCriteriaLikeExpression(criteria);
		
		return new PageImpl<>(acs, pageable, total);
	}
	
	private long findCountByCriteriaLikeExpression(AccountCriteria criteria) {
		
        long total = dsl.fetchCount(
        						dsl.select(a.ID)
        						.from(a)
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
	
	private Condition condition(AccountCriteria criteria) {
		
	    Condition condition = DSL.trueCondition();
	    
	    final String code = criteria.getCode();
	    final String name = criteria.getName();
		final Set<AccountType> types = criteria.getTypes();		
		
		if (code != null) {
	    	condition = condition.and(a.CODE.eq(code));				
	    }
	    if (name != null) {
	    	condition = condition.and(a.NAME.likeIgnoreCase("%" + name + "%"));				
	    }  
		if(types != null && types.size() > 0){
			condition = condition.and(a.TYPE.in(types));
		}
		
	    return condition;
	}
}
