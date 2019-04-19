package org.minioasis.library.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.minioasis.library.domain.Holiday;
import org.minioasis.library.domain.search.HolidayCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import static org.minioasis.library.jooq.tables.Holiday.HOLIDAY;

import java.util.Date;
import java.util.List;
import java.util.Set;

public class HolidayRepositoryImpl implements HolidayRepositoryCustom {

	@PersistenceContext
	private EntityManager em;
	
	@Autowired
	private DSLContext dsl;
	
	private org.minioasis.library.jooq.tables.Holiday h = HOLIDAY.as("h");
	
	private Condition condition(HolidayCriteria criteria) {
		
	    Condition result = DSL.trueCondition();
	    
		final String name = criteria.getName();
		final Date startDateFrom = criteria.getStartDateFrom();
		final Date endDateTo = criteria.getEndDateTo();
		final Set<Boolean> fines = criteria.getFines();		
	    
	    if (name != null) {
	    	result = result.and(h.NAME.likeIgnoreCase("%" + name + "%"));
	    }

		if(startDateFrom != null && endDateTo != null){
			result = result.and(h.START_DATE.ge(new java.sql.Date(startDateFrom.getTime()))
							.and(h.END_DATE.le(new java.sql.Date(endDateTo.getTime()))));
		}
		if(fines != null && fines.size() > 0){
			result = result.and(h.FINE.in(fines));
		}
	    
	    return result;
	}
	
	public Page<Holiday> findByCriteria(HolidayCriteria criteria, Pageable pageable){
		
		org.jooq.Query jooqQuery = dsl.select()
									.from(h)
									.where(condition(criteria))
									.limit(pageable.getPageSize())
									.offset((int)pageable.getOffset());
		
		Query q = em.createNativeQuery(jooqQuery.getSQL(), Holiday.class);
		setBindParameterValues(q, jooqQuery);
		
		List<Holiday> holidays = q.getResultList();
		
		long total = findCountByCriteriaLikeExpression(criteria);
		
		return new PageImpl<>(holidays, pageable, total);
	}
	
	private static void setBindParameterValues(Query hibernateQuery, org.jooq.Query jooqQuery) {
	    List<Object> values = jooqQuery.getBindValues();
	    for (int i = 0; i < values.size(); i++) {
	        hibernateQuery.setParameter(i + 1, values.get(i));
	    }
	}
	
	private long findCountByCriteriaLikeExpression(HolidayCriteria criteria) {

        long total = dsl.fetchCount(
        						dsl.select(h.ID)
        						.from(h)
        						.where(condition(criteria))
        );

        return total;
    }

}
