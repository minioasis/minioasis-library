package org.minioasis.library.repository;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.SortField;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.impl.DSL;
import org.minioasis.library.domain.CheckoutState;
import org.minioasis.library.domain.Patron;
import org.minioasis.library.domain.Reservation;
import org.minioasis.library.domain.ReservationState;
import org.minioasis.library.domain.search.ReservationCriteria;
import org.minioasis.library.domain.util.ReservationComparator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import static org.minioasis.library.jooq.tables.Reservation.RESERVATION;
import static org.minioasis.library.jooq.tables.Patron.PATRON;
import static org.minioasis.library.jooq.tables.Biblio.BIBLIO;

public class ReservationRepositoryImpl implements ReservationRepositoryCustom {

	private static final Logger LOGGER = LoggerFactory.getLogger(ReservationRepositoryImpl.class);
	
	@PersistenceContext
	private EntityManager em;
	
	@Autowired
	private DSLContext dsl;
	
	private org.minioasis.library.jooq.tables.Patron p = PATRON.as("p");
	private org.minioasis.library.jooq.tables.Reservation r = RESERVATION.as("r");
	private org.minioasis.library.jooq.tables.Biblio b = BIBLIO.as("b");
	
	public List<Reservation> findByBiblioIdAndStates(long id, List<ReservationState> states){
		
		Table<?> table = r;
		table = table.innerJoin(b).on(r.BIBLIO_ID.eq(b.ID)).and(b.ID.eq(id));
		
		Condition condition = DSL.trueCondition();
		
		if(states != null && states.size() > 0){
			condition = condition.and(r.STATE.in(states));
		}
		
		org.jooq.Query jooqQuery = dsl.select()
				.from(table)
				.where(condition);
		
		Query q = em.createNativeQuery(jooqQuery.getSQL(), Reservation.class);
		setBindParameterValues(q, jooqQuery);
		
		List<Reservation> list = q.getResultList();
				
		return list;
	}
	
	public Page<Reservation> findByCriteria(ReservationCriteria criteria, Pageable pageable){

		Table<?> table = createTable(criteria);
	
		org.jooq.Query jooqQuery = dsl.select()
									.from(table)
									.where(condition(criteria))
									.orderBy(getSortFields(pageable.getSort()))
									.limit(pageable.getPageSize())
									.offset((int)pageable.getOffset());
		
		Query q = em.createNativeQuery(jooqQuery.getSQL(), Reservation.class);
		setBindParameterValues(q, jooqQuery);
		
		List<Reservation> list = q.getResultList();
		
		long total = findCountByCriteriaLikeExpression(criteria);
		
		return new PageImpl<>(list, pageable, total);
	}
	
	private long findCountByCriteriaLikeExpression(ReservationCriteria criteria) {

		Table<?> table = createTable(criteria);
		
        long total = dsl.fetchCount(
        						dsl.select(r.ID)
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
	
	private Table<?> createTable(ReservationCriteria criteria) {

		final String cardkey = criteria.getCardKey();
		final String isbn = criteria.getIsbn();
		
		Table<?> table = r;
		
		if(cardkey != null) {
			table = table.innerJoin(p).on(r.PATRON_ID.eq(p.ID)).and(p.CARD_KEY.eq(cardkey));
		}
		if(isbn != null) {
			table = table.innerJoin(b).on(r.BIBLIO_ID.eq(b.ID)).and(b.ISBN.eq(isbn));
		}
		
		return table;
	}

	private Condition condition(ReservationCriteria criteria) {
		
	    Condition condition = DSL.trueCondition();
	    
		final LocalDate reservationDateFrom = criteria.getReservationDateFrom();
		final LocalDate reservationDateTo = criteria.getReservationDateTo();
		final LocalDate availableDateFrom = criteria.getAvailableDateFrom();
		final LocalDate availableDateTo = criteria.getAvailableDateTo();
		final LocalDate notificationDateFrom = criteria.getNotificationDateFrom();
		final LocalDate notificationDateTo = criteria.getNotificationDateTo();
		final Set<ReservationState> states = criteria.getStates();
		
		if(reservationDateFrom != null && reservationDateTo != null){
			condition = condition.and(r.RESERVATION_DATE.ge(java.sql.Timestamp.valueOf(reservationDateFrom.atStartOfDay())))
							.and(r.RESERVATION_DATE.le(java.sql.Timestamp.valueOf(reservationDateTo.atStartOfDay())));
		}
		if(availableDateFrom != null && availableDateTo != null){
			condition = condition.and(r.AVAILABLE_DATE.ge(java.sql.Date.valueOf(availableDateFrom))
							.and(r.AVAILABLE_DATE.le(java.sql.Date.valueOf(availableDateTo))));
		}
		if(notificationDateFrom != null && notificationDateTo != null){
			condition = condition.and(r.NOTIFICATION_DATE.ge(java.sql.Date.valueOf(notificationDateFrom))
							.and(r.NOTIFICATION_DATE.le(java.sql.Date.valueOf(notificationDateTo))));
		}
		if(states != null && states.size() > 0){
			condition = condition.and(r.STATE.in(states));
		}
		
	    return condition;
	}
	
	public void refreshReservationStates() {
		
		LOGGER.info("Entering refreshReservationStates()..");
		LocalDate now = LocalDate.now();
		
		clearExpiredReservations(now);
		clearUncollectedReservations(now);
		//add clearReportLostAndPaidReservation(aGivenDate) if possible
		
	}
	
	public void clearExpiredReservations(LocalDate given) {
		
		LOGGER.info("*** CLEAR EXPIRED RESERVATIONS *** : " + LocalDate.now());
		
		TypedQuery<Reservation> reservationQuery = em.createQuery("select r from Reservation r where r.state = :state", Reservation.class)
													  .setParameter("state", ReservationState.RESERVE);
		
		List<Reservation> reservations = reservationQuery.getResultList();
		
	    for (Reservation r : reservations) {
	    	if (r.getExpiryDate().isBefore(given)){
				r.setState(ReservationState.RESERVATION_EXPIRED);
			}
	    }
	}
	
	public void clearUncollectedReservations(LocalDate given){
		
		LOGGER.info("*** CLEAR UNCOLLECTED RESERVATIONS *** : " + LocalDate.now());
		
		List<Reservation> reservations = em.createQuery("select r from Reservation r where r.state = :state ", Reservation.class)
												.setParameter("state", ReservationState.NOTIFIED)
												.getResultList();

		for(Reservation r : reservations) {

			long maxCollectablePeriod = r.getPatron().getPatronType().getMaxCollectablePeriod().longValue();
			int maxUncollectedNo = r.getPatron().getPatronType().getMaxUncollectedNo();
			int maxCantReservePeriod = r.getPatron().getPatronType().getMaxCantReservePeriod();
			
			LocalDate notificationDate = r.getNotificationDate();
			LocalDate lastDayForCollection = notificationDate.plusDays(maxCollectablePeriod);

			Patron patron = r.getPatron();
			short unCollectedNo = patron.getUnCollectedNo();
			short plusOne = (short) (unCollectedNo + 1);

			if (lastDayForCollection.isBefore(given)) {

				r.setState(ReservationState.COLLECTION_PERIOD_EXPIRED);
				r.setUnCollectedDate(lastDayForCollection);
				patron.setUnCollectedNo(plusOne);
				
				if(plusOne >= maxUncollectedNo) {
					patron.setReservableDate(given.plusDays(maxCantReservePeriod));
					patron.setUnCollectedNo((short)0);
				}

				List<Reservation> rs = findByBiblioIdAndStates(r.getBiblio().getId(), new ArrayList<ReservationState>(EnumSet.of(ReservationState.RESERVE)));

				if(rs.size() > 0) {
					
					Collections.sort(rs,new ReservationComparator());
					Reservation candidate = rs.get(0);
					candidate.setAvailableDate(given);
					candidate.setState(ReservationState.AVAILABLE);

				}
			}
		}
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

            Field tableField = r.getClass().getField(sortFieldName.toUpperCase());
            sortField = (TableField<?, ?>) tableField.get(r);
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
