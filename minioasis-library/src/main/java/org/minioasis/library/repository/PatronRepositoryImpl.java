package org.minioasis.library.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Root;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.jooq.DatePart;
import org.jooq.Field;
import org.jooq.Record3;
import org.jooq.Result;
import org.minioasis.library.domain.Group;
import org.minioasis.library.domain.Patron;
import org.minioasis.library.domain.YesNo;
import org.minioasis.library.domain.search.PatronCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import static org.minioasis.library.jooq.tables.Groups.GROUPS;
import static org.minioasis.library.jooq.tables.Patron.PATRON;
import static org.minioasis.library.jooq.tables.PatronType.PATRON_TYPE;

public class PatronRepositoryImpl implements PatronRepositoryCustom {

	@PersistenceContext
	private EntityManager em;
	
	@Autowired
	private DSLContext dsl;
	
	private org.minioasis.library.jooq.tables.Patron p = PATRON.as("p");
	private org.minioasis.library.jooq.tables.PatronType pt = PATRON_TYPE.as("pt");
	private org.minioasis.library.jooq.tables.Groups g = GROUPS.as("g");

	private Condition condition(PatronCriteria criteria) {
		
	    Condition result = DSL.trueCondition();
	    
		final String cardkey = criteria.getCardKey();
		final String keyword = criteria.getKeyword();
		final String note = criteria.getNote();
		final LocalDateTime createdFrom = criteria.getCreatedFrom();
		final LocalDateTime createdTo = criteria.getCreatedTo();
		final LocalDate startDateFrom = criteria.getStartDateFrom();
		final LocalDate startDateTo = criteria.getStartDateTo();
		final LocalDate endDateFrom = criteria.getEndDateFrom();
		final LocalDate endDateTo = criteria.getEndDateTo();
		final Set<YesNo> actives = criteria.getActives();
		final Set<Long> patronTypes = criteria.getPatronTypes();
		final Set<Long> groups = criteria.getGroups();		
	    
	    if (cardkey != null) {
	    	result = result.and(p.CARD_KEY.eq(cardkey));
	    }
	        
	    if (keyword != null) {
	    	result = result.and(p.NAME.likeIgnoreCase("%" + keyword + "%")
    						.or(p.NAME2.likeIgnoreCase("%" + keyword + "%")
    						.or(p.MOBILE.likeIgnoreCase("%" + keyword + "%")
    						.or(p.IC.likeIgnoreCase("%" + keyword + "%")))));
	    }

		if(note != null) {
			if(note.equals("isNotEmpty()")) {
				result = result.and(p.NOTE.isNotNull());
			}else {
				result = result.and(p.NOTE.likeIgnoreCase("%" + note + "%"));
			}			
		}
	    
	    if(createdFrom != null || createdTo != null) {
	    	result = result.and(p.CREATED.between(java.sql.Timestamp.valueOf(createdFrom), java.sql.Timestamp.valueOf(createdTo)));
	    }
		if(startDateFrom != null || startDateTo != null){
			result = result.and(p.START_DATE.between(java.sql.Date.valueOf(startDateFrom),java.sql.Date.valueOf(startDateTo)));
		}
		if(endDateFrom != null || endDateTo != null){
			result = result.and(p.START_DATE.between(java.sql.Date.valueOf(endDateFrom), java.sql.Date.valueOf(endDateTo)));
		}
		if(patronTypes != null && patronTypes.size() > 0){
			result = result.and(p.PATRONTYPE_ID.in(patronTypes));
		}
		if(groups != null && groups.size() > 0){
			result = result.and(p.GROUP_ID.in(groups));
		}
		if(actives != null && actives.size() > 0){
			result = result.and(p.ACTIVE.in(actives));
		}
	    
	    return result;
	}

	
	public Result<Record3<Integer, String, Integer>> CountPatronsByTypes() {
		
		Field<Integer> month = p.START_DATE.extract(DatePart.MONTH);

		Result<Record3<Integer, String, Integer>> records = dsl.select(month, pt.NAME, DSL.count())
									.from(p)
									.join(pt).on(pt.ID.eq(p.PATRONTYPE_ID))
									.groupBy(pt.NAME, month)
									.orderBy(month, pt.NAME)
									.fetch();
		
		return records;
	}
	
	public Page<Patron> findByCriteria(PatronCriteria criteria, Pageable pageable){
		
		org.jooq.Query jooqQuery = dsl.select()
									.from(p)
									.join(pt).on(pt.ID.eq(p.PATRONTYPE_ID))
									.join(g).on(g.ID.eq(p.GROUP_ID))
									.where(condition(criteria))
									.limit(pageable.getPageSize())
									.offset((int)pageable.getOffset());
		
		//Session session = em.unwrap(Session.class);
		//Query q = session.createNativeQuery(jooqQuery.getSQL(), Patron.class);
		
		Query q = em.createNativeQuery(jooqQuery.getSQL(), Patron.class);
		setBindParameterValues(q, jooqQuery);
		
		List<Patron> patrons = q.getResultList();
		
		long total = findCountByCriteriaLikeExpression(criteria);
		
		return new PageImpl<>(patrons, pageable, total);
	}
	
	private static void setBindParameterValues(Query hibernateQuery, org.jooq.Query jooqQuery) {
	    List<Object> values = jooqQuery.getBindValues();
	    for (int i = 0; i < values.size(); i++) {
	        hibernateQuery.setParameter(i + 1, values.get(i));
	    }
	}
	
	private long findCountByCriteriaLikeExpression(PatronCriteria criteria) {

        long total = dsl.fetchCount(
        						dsl.select(p.ID)
        						.from(p)
        						.where(condition(criteria))
        );

        return total;
    }


	public int bulkUpdateGroup(List<Long> ids, Group group, LocalDateTime now){
		
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaUpdate<Patron> query = cb.createCriteriaUpdate(Patron.class);
		
		Root<Patron> root = query.from(Patron.class);
		
		query.set(root.get("group"), group)
				.set(root.get("updated"), now)
				.where(root.get("id").in(ids));

		int result = em.createQuery(query).executeUpdate();
		
		return result;
		
	}
	
}
