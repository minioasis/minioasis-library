package org.minioasis.library.repository;

import static org.minioasis.library.jooq.tables.Biblio.BIBLIO;
import static org.minioasis.library.jooq.tables.Checkout.CHECKOUT;
import static org.minioasis.library.jooq.tables.Groups.GROUPS;
import static org.minioasis.library.jooq.tables.Item.ITEM;
import static org.minioasis.library.jooq.tables.Patron.PATRON;
import static org.minioasis.library.jooq.tables.PatronType.PATRON_TYPE;

import java.sql.Date;
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
import org.jooq.Field;
import org.jooq.Record7;
import org.jooq.Record9;
import org.jooq.SortField;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.impl.DSL;
import org.minioasis.library.domain.Checkout;
import org.minioasis.library.domain.CheckoutState;
import org.minioasis.library.domain.YesNo;
import org.minioasis.library.domain.search.CheckoutCriteria;
import org.minioasis.library.domain.search.CheckoutPatronCriteria;
import org.minioasis.library.domain.search.TopCheckoutPatronsSummary;
import org.minioasis.library.domain.search.TopPopularBooksCriteria;
import org.minioasis.library.domain.search.TopPopularBooksSummary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class CheckoutRepositoryImpl implements CheckoutRepositoryCustom {

	private static final Logger LOGGER = LoggerFactory.getLogger(CheckoutRepositoryImpl.class);
	
	@PersistenceContext
	private EntityManager em;
	
	@Autowired
	private DSLContext dsl;
	
	private org.minioasis.library.jooq.tables.Checkout c = CHECKOUT.as("c");
	private org.minioasis.library.jooq.tables.Patron p = PATRON.as("p");
	private org.minioasis.library.jooq.tables.PatronType pt = PATRON_TYPE.as("pt");
	private org.minioasis.library.jooq.tables.Item i = ITEM.as("i");
	private org.minioasis.library.jooq.tables.Biblio b = BIBLIO.as("b");
	private org.minioasis.library.jooq.tables.Groups g = GROUPS.as("g");
	
	public List<Checkout> findAllActiveCheckoutsByCardKey(String cardKey){
		
		Table<?> table = c;
		
		table = table.innerJoin(p).on(c.PATRON_ID.eq(p.ID))
						.join(i).on(c.ITEM_ID.eq(i.ID))
						.join(b).on(i.BIBLIO_ID.eq(b.ID));
		
		org.jooq.Query jooqQuery = dsl.select()
									.from(table)
									.where(activeStatesCondition(cardKey))
									.orderBy(c.DUE_DATE.asc());
		
		Query q = em.createNativeQuery(jooqQuery.getSQL(), Checkout.class);
		setBindParameterValues(q, jooqQuery);
		
		List<Checkout> checkouts = q.getResultList();
		
		return checkouts;

	}
	
	private Condition activeStatesCondition(String cardKey) {
		
	    Condition condition = DSL.trueCondition();
		condition = condition.and(p.CARD_KEY.eq(cardKey))
							 .and(c.STATE.in(CheckoutState.getActives()));
	    return condition;
	}
	
	// ************************************************************************
	
	public List<Checkout> patronOverDues(String cardKey, LocalDate given){
		
		Table<?> table = c;
		table = table.innerJoin(p).on(c.PATRON_ID.eq(p.ID))
					 .innerJoin(i).on(c.ITEM_ID.eq(i.ID))
					 .innerJoin(b).on(i.BIBLIO_ID.eq(b.ID));
		
		org.jooq.Query jooqQuery = dsl.select()
				.from(table)
				.where(patronOvedueCondition(cardKey,given))
				.orderBy(c.DUE_DATE.asc());
		
		Query q = em.createNativeQuery(jooqQuery.getSQL(), Checkout.class);
		setBindParameterValues(q, jooqQuery);
		
		List<Checkout> checkouts = q.getResultList();
		
		return checkouts;
		
	}

	private Condition patronOvedueCondition(String cardKey, LocalDate given) {
		
	    Condition condition = DSL.trueCondition();
	    condition = condition.and(p.CARD_KEY.eq(cardKey))
	    						.and(c.STATE.in(CheckoutState.getActives()))
	    						.and(c.DUE_DATE.le(java.sql.Date.valueOf(given)));
		
	    return condition;
	}
	
	// ************************************************************************
	public List<String> allOverDuePatrons(LocalDate given){
		
		Table<?> table = c;
		table = table.innerJoin(p).on(c.PATRON_ID.eq(p.ID));

		List<String> result = dsl.select().from(table)
											.where(ovedueCondition(given))
											.fetch(p.CARD_KEY, String.class);
		
		return result;
	}
	
	private Condition ovedueCondition(LocalDate given) {
		
	    Condition condition = DSL.trueCondition();
	    condition = condition.and(c.STATE.in(CheckoutState.getActives()))
	    						.and(c.DUE_DATE.le(java.sql.Date.valueOf(given)));
		
	    return condition;
	}

	// ************************************************************************
	
	public List<TopPopularBooksSummary> topPopularBooks(TopPopularBooksCriteria criteria){
		
		Table<?> table = createTable2();
		
		Table<Record7<String, String, Date, String, String, String, Integer>> view =
				dsl.select(b.TITLE, b.ISBN, i.FIRST_CHECKIN, p.ACTIVE,pt.NAME.as("patronType"), g.CODE.as("group"), DSL.count().as("total"))
					.from(table)
					.where(topPopularBooksCondition(criteria))
					.groupBy(b.ID).asTable("view");

		
		return dsl.select(view.fields())
				.from(view)
				.orderBy(view.field("total").desc())
				.fetchInto(TopPopularBooksSummary.class);

	}
	
	private Table<?> createTable2() {

		Table<?> table = c;
		
		table = table.innerJoin(p).on(c.PATRON_ID.eq(p.ID))
						.join(pt).on(p.PATRONTYPE_ID.eq(pt.ID))
						.join(g).on(p.GROUP_ID.eq(g.ID))
						.join(i).on(c.ITEM_ID.eq(i.ID))
						.join(b).on(i.BIBLIO_ID.eq(b.ID));

		return table;
	}
	
	private Condition topPopularBooksCondition(TopPopularBooksCriteria criteria) {
		
	    Condition condition = DSL.trueCondition();
	    
		final LocalDate firstCheckinFrom = criteria.getFirstCheckinFrom();
		final LocalDate firstCheckinTo = criteria.getFirstCheckinTo();
		final Set<YesNo> actives = criteria.getActives();
		final Set<Long> patronTypes = criteria.getPatronTypes();
		final Set<Long> groups = criteria.getGroups();
		final Set<CheckoutState> states = criteria.getStates();		
		
		if(firstCheckinFrom != null && firstCheckinTo != null){
			condition = condition.and(i.FIRST_CHECKIN.ge(java.sql.Date.valueOf(firstCheckinFrom))
							.and(i.FIRST_CHECKIN.le(java.sql.Date.valueOf(firstCheckinTo))));
		}
		if(patronTypes != null && patronTypes.size() > 0){
			condition = condition.and(p.PATRONTYPE_ID.in(patronTypes));
		}
		if(groups != null && groups.size() > 0){
			condition = condition.and(p.GROUP_ID.in(groups));
		}
		if(actives != null && actives.size() > 0){
			condition = condition.and(p.ACTIVE.in(actives));
		}
		if(states != null && states.size() > 0){
			condition = condition.and(c.STATE.in(states));
		}
		
	    return condition;
	}

	// ************************************************************************
	
	public List<TopCheckoutPatronsSummary> topListPatronsForCheckouts(CheckoutPatronCriteria criteria){	

		Table<Record9<String, String, String, String, String, String, Date, Date, Integer>> view =
				dsl.select(p.CARD_KEY,p.ACTIVE, p.NAME, p.NAME2, pt.NAME.as("patronType"), g.CODE.as("group"), p.START_DATE, p.END_DATE, DSL.count().as("total"))
					.from(c)
					.join(p).on(c.PATRON_ID.eq(p.ID))
					.join(pt).on(p.PATRONTYPE_ID.eq(pt.ID))
					.join(g).on(p.GROUP_ID.eq(g.ID))
					.where(topListPatronsCondition(criteria))
					.groupBy(p.ID).asTable("view");

		return dsl.select(view.fields())
					.from(view)
					.orderBy(view.field("total").desc())
					.fetchInto(TopCheckoutPatronsSummary.class);

	}
	
	private Condition topListPatronsCondition(CheckoutPatronCriteria criteria) {
		
	    Condition condition = DSL.trueCondition();
	    
		final LocalDate startDateFrom = criteria.getStartDateFrom();
		final LocalDate startDateTo = criteria.getStartDateTo();
		final LocalDate endDateFrom = criteria.getEndDateFrom();
		final LocalDate endDateTo = criteria.getEndDateTo();
		final Set<YesNo> actives = criteria.getActives();
		final Set<Long> patronTypes = criteria.getPatronTypes();
		final Set<Long> groups = criteria.getGroups();
		final Set<CheckoutState> states = criteria.getStates();		
		
		if(startDateFrom != null && startDateTo != null){
			condition = condition.and(p.START_DATE.ge(java.sql.Date.valueOf(startDateFrom))
							.and(p.END_DATE.le(java.sql.Date.valueOf(startDateTo))));
		}
		if(endDateFrom != null && endDateTo != null){
			condition = condition.and(p.END_DATE.ge(java.sql.Date.valueOf(endDateFrom))
							.and(p.END_DATE.le(java.sql.Date.valueOf(endDateTo))));
		}
		if(patronTypes != null && patronTypes.size() > 0){
			condition = condition.and(p.PATRONTYPE_ID.in(patronTypes));
		}
		if(groups != null && groups.size() > 0){
			condition = condition.and(p.GROUP_ID.in(groups));
		}
		if(actives != null && actives.size() > 0){
			condition = condition.and(p.ACTIVE.in(actives));
		}
		if(states != null && states.size() > 0){
			condition = condition.and(c.STATE.in(states));
		}
		
	    return condition;
	}
	
	public String topListPatronsForCheckouts_JSON(){
		
		String results = dsl.select(p.CARD_KEY, p.NAME, p.NAME2, p.START_DATE, DSL.count())
																			.from(c)
																			.join(p).on(c.PATRON_ID.eq(p.ID))
																			.groupBy(c.ID)
																			.orderBy(DSL.count(),p.START_DATE)
																			.fetch().formatJSON();
				
		return results;
	}

	public Page<Checkout> findByCriteria(CheckoutCriteria criteria, Pageable pageable){

		Table<?> table = createTable(criteria);
	
		org.jooq.Query jooqQuery = dsl.select()
									.from(table)
									.where(condition(criteria))
									 .orderBy(getSortFields(pageable.getSort()))
									.limit(pageable.getPageSize())
									.offset((int)pageable.getOffset());
		
		Query q = em.createNativeQuery(jooqQuery.getSQL(), Checkout.class);
		setBindParameterValues(q, jooqQuery);
		
		List<Checkout> checkouts = q.getResultList();
		
		long total = findCountByCriteriaLikeExpression(criteria);
		
		return new PageImpl<>(checkouts, pageable, total);
	}
	
	private long findCountByCriteriaLikeExpression(CheckoutCriteria criteria) {

		Table<?> table = createTable(criteria);
		
        long total = dsl.fetchCount(
        						dsl.select(c.ID)
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

	private Table<?> createTable(CheckoutCriteria criteria) {

		final String cardkey = criteria.getCardkey();
		final String barcode = criteria.getBarcode();
		final String title = criteria.getTitle();
		
		Table<?> table = c;
		
		if(cardkey == null) {
			table = table.join(p).on(c.PATRON_ID.eq(p.ID))
						.join(pt).on(pt.ID.eq(p.PATRONTYPE_ID))
						.join(g).on(g.ID.eq(p.GROUP_ID));
		}else {
			table = table.join(p).on(c.PATRON_ID.eq(p.ID)).and(p.CARD_KEY.eq(cardkey))
						.join(pt).on(pt.ID.eq(p.PATRONTYPE_ID))
						.join(g).on(g.ID.eq(p.GROUP_ID));
		}

		if(title != null) {
			if(barcode != null) {
				table = table.join(i).on(c.ITEM_ID.eq(i.ID)).and(i.BARCODE.eq(barcode))
								.join(b).on(i.BIBLIO_ID.eq(b.ID)).and(b.TITLE.likeIgnoreCase("%" + title + "%"));
			}else {
				table = table.join(i).on(c.ITEM_ID.eq(i.ID))
						.join(b).on(i.BIBLIO_ID.eq(b.ID)).and(b.TITLE.likeIgnoreCase("%" + title + "%"));
			}
		}else {
			if(barcode != null) {
				table = table.join(i).on(c.ITEM_ID.eq(i.ID)).and(i.BARCODE.eq(barcode))
						.join(b).on(i.BIBLIO_ID.eq(b.ID));
			}else {
				table = table.join(i).on(c.ITEM_ID.eq(i.ID))
						.join(b).on(i.BIBLIO_ID.eq(b.ID));
			}
		}
		
		return table;
	}

	private Condition condition(CheckoutCriteria criteria) {
		
	    Condition condition = DSL.trueCondition();
	    
		final LocalDate checkoutFrom = criteria.getCheckoutFrom();
		final LocalDate checkoutTo = criteria.getCheckoutTo();
		final LocalDate dueDateFrom = criteria.getDueDateFrom();
		final LocalDate dueDateTo = criteria.getDueDateTo();
		final LocalDate doneFrom = criteria.getDoneFrom();
		final LocalDate doneTo = criteria.getDoneTo();
		final Set<CheckoutState> states = criteria.getStates();	
		final Set<YesNo> actives = criteria.getActives();
		final Set<Long> patronTypes = criteria.getPatronTypes();
		final Set<Long> groups = criteria.getGroups();
		
		if(checkoutFrom != null && checkoutTo != null){
			condition = condition.and(c.CHECKOUT_DATE.ge(java.sql.Date.valueOf(checkoutFrom))
							.and(c.CHECKOUT_DATE.le(java.sql.Date.valueOf(checkoutTo))));
		}
		if(dueDateFrom != null && dueDateTo != null){
			condition = condition.and(c.DUE_DATE.ge(java.sql.Date.valueOf(dueDateFrom))
							.and(c.DUE_DATE.le(java.sql.Date.valueOf(dueDateTo))));
		}
		if(doneFrom != null && doneTo != null){
			condition = condition.and(c.DONE.ge(java.sql.Date.valueOf(doneFrom))
							.and(c.DONE.le(java.sql.Date.valueOf(doneTo))));
		}
		if(states != null && states.size() > 0){
			condition = condition.and(c.STATE.in(states));
		}
		if(patronTypes != null && patronTypes.size() > 0){
			condition = condition.and(p.PATRONTYPE_ID.in(patronTypes));
		}
		if(groups != null && groups.size() > 0){
			condition = condition.and(p.GROUP_ID.in(groups));
		}
		if(actives != null && actives.size() > 0){
			condition = condition.and(p.ACTIVE.in(actives));
		}
		
	    return condition;
	}
	
	@SuppressWarnings("unused")
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
            java.lang.reflect.Field tableField = CHECKOUT.getClass().getField(sortFieldName);
            sortField = (TableField<?, ?>) tableField.get(CHECKOUT);
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
