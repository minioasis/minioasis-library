package org.minioasis.library.repository;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.SortField;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.impl.DSL;
import org.minioasis.library.domain.Attachment;
import org.minioasis.library.domain.AttachmentState;
import org.minioasis.library.domain.YesNo;
import org.minioasis.library.domain.search.AttachmentCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import static org.minioasis.library.jooq.tables.Item.ITEM;
import static org.minioasis.library.jooq.tables.Attachment.ATTACHMENT;

public class AttachmentRepositoryImpl implements AttachmentRepositoryCustom {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AttachmentRepositoryImpl.class);
	
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
									.orderBy(getSortFields(pageable.getSort()))
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
	
	private Collection<SortField<?>> getSortFields(Sort sortSpecification) {

        LOGGER.debug("Getting sort fields from sort specification: {}", sortSpecification);
        Collection<SortField<?>> querySortFields = new ArrayList<>();

        if (sortSpecification == null) {
            LOGGER.debug("No sort specification found. Returning empty collection -> no sorting is done.");
            return querySortFields;
        }

        Iterator<Sort.Order> specifiedFields = sortSpecification.iterator();

        while (specifiedFields.hasNext()) {
            Sort.Order specifiedField = specifiedFields.next();

            String sortFieldName = specifiedField.getProperty();

            Sort.Direction sortDirection = specifiedField.getDirection();
            LOGGER.debug("Getting sort field with name: {} and direction: {}", sortFieldName, sortDirection);

            TableField<?, ?> tableField = getTableField(sortFieldName);
            SortField<?> querySortField = convertTableFieldToSortField(tableField, sortDirection);
            querySortFields.add(querySortField);
        }

        return querySortFields;
    }

    private TableField<?, ?> getTableField(String sortFieldName) {
        TableField<?, ?> sortField = null;
        try {

            Field tableField = a.getClass().getField(sortFieldName.toUpperCase());
            sortField = (TableField<?, ?>) tableField.get(a);
        } catch (NoSuchFieldException | IllegalAccessException ex) {
            String errorMessage = String.format("Could not find table field: {}", sortFieldName);
            throw new InvalidDataAccessApiUsageException(errorMessage, ex);
        }

        return sortField;
    }

    private SortField<?> convertTableFieldToSortField(TableField<?, ?> tableField, Sort.Direction sortDirection) {
        if (sortDirection == Sort.Direction.ASC) {
            return tableField.asc();
        }
        else {
            return tableField.desc();
        }
    }
}
