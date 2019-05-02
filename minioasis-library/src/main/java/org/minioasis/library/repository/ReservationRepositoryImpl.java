package org.minioasis.library.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.hibernate.CacheMode;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Table;
import org.jooq.impl.DSL;
import org.minioasis.library.domain.Biblio;
import org.minioasis.library.domain.Patron;
import org.minioasis.library.domain.Reservation;
import org.minioasis.library.domain.ReservationState;
import org.minioasis.library.domain.search.ReservationCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import static org.minioasis.library.jooq.tables.Reservation.RESERVATION;
import static org.minioasis.library.jooq.tables.Patron.PATRON;
import static org.minioasis.library.jooq.tables.Biblio.BIBLIO;

public class ReservationRepositoryImpl implements ReservationRepositoryCustom {

	@PersistenceContext
	private EntityManager em;
	
	@Autowired
	private DSLContext dsl;
	
	private org.minioasis.library.jooq.tables.Patron p = PATRON.as("p");
	private org.minioasis.library.jooq.tables.Reservation r = RESERVATION.as("r");
	private org.minioasis.library.jooq.tables.Biblio b = BIBLIO.as("b");

	public Page<Reservation> findByCriteria(ReservationCriteria criteria, Pageable pageable){

		Table<?> table = createTable(criteria);
	
		org.jooq.Query jooqQuery = dsl.select()
									.from(table)
									.where(condition(criteria))
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
	    
		final LocalDateTime reservationDateFrom = criteria.getReservationDateFrom();
		final LocalDateTime reservationDateTo = criteria.getReservationDateTo();
		final LocalDate availableDateFrom = criteria.getAvailableDateFrom();
		final LocalDate availableDateTo = criteria.getAvailableDateTo();
		final LocalDate notificationDateFrom = criteria.getNotificationDateFrom();
		final LocalDate notificationDateTo = criteria.getNotificationDateTo();
		final Set<ReservationState> states = criteria.getStates();
		
		if(reservationDateFrom != null && reservationDateTo != null){
			condition = condition.and(r.RESERVATION_DATE.ge(java.sql.Timestamp.valueOf(reservationDateFrom)))
							.and(r.RESERVATION_DATE.le(java.sql.Timestamp.valueOf(reservationDateTo)));
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
	
	@SuppressWarnings("unchecked")
	private List<Reservation> getExpiredBiblioReservations() {

		Session session = em.unwrap(Session.class);

		List<Reservation> result = session
				.createQuery("from Reservation r " 
							+ "left join fetch r.biblio b "
							+ "left join fetch b.reservations rs " 
							+ "where b is not null " 
							+ "and r.state = :state "
							+ "and rs.state in ('RESERVE','AVAILABLE','NOTIFIED')")
				.setParameter("state", ReservationState.NOTIFIED).getResultList();

		session.close();
		
		return result;

	}
	
	public void refreshReservationStates() {
		
		//System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  refreshReservationStates");
		LocalDate now = LocalDate.now();
		
		clearExpiredReservations(now);
		clearUncollectedReservations(now);
		clearPunishedReservations(now);
		//add clearReportLostAndPaidReservation(aGivenDate) if possible
		
	}
	
	private void clearExpiredReservations(LocalDate given) {
		
		//System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  clearExpiredReservations");
		Session session = em.unwrap(Session.class);
		ScrollableResults reservationCursor = session
				.createQuery("from Reservation r where r.state = :state")
				.setParameter("state", ReservationState.RESERVE)
				.setCacheMode(CacheMode.IGNORE)
				.scroll(ScrollMode.FORWARD_ONLY);
		
		int count = 0;

		while (reservationCursor.next()) {
			Reservation reservation = (Reservation) reservationCursor.get(0);

			if (reservation.getExpiryDate().isBefore(given)){
				reservation.setState(ReservationState.RESERVATION_EXPIRED);
			}
				
			// if transactionInterceptor's transactionAttributes does not set to PROPAGATION_REQUIRED
			// for this method(clearExpiredReservations) , flushing will not be done !
			if (++count % 1 == 0) {
				// flush a batch of updates and release memory:
				session.flush();
				session.clear();
			}
			
		}

	}
	
	private void clearUncollectedReservations(LocalDate given){
		
		//System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  clearUncollectedReservations");
		Session session = em.unwrap(Session.class);
		
		int count = 0;
		
		// biblio
		List<Reservation> biblioReservations = getExpiredBiblioReservations();

		Iterator<Reservation> itb = biblioReservations.iterator();

		while (itb.hasNext()) {
			
			Reservation reservation = (Reservation) itb.next();

			// TODO : LUT use old or new ? old now !
			long maxCollectablePeriod = reservation.getPatronType().getMaxCollectablePeriod().longValue();
			LocalDate notificationDate = reservation.getNotificationDate();
			LocalDate bufferDate = notificationDate.plusDays(maxCollectablePeriod);

			Patron patron = reservation.getPatron();
			short unCollectedNo = patron.getUnCollectedNo();
			short plusOne = (short) (unCollectedNo + 1);

			if(reservation.getBiblio() != null){

				if (bufferDate.isBefore(given)) {

					reservation.setState(ReservationState.COLLECTION_PERIOD_EXPIRED);
					reservation.setUnCollectedDate(bufferDate);
					patron.setUnCollectedNo(plusOne);

					Biblio biblio = reservation.getBiblio();
					Reservation r = biblio.findFirstReservationInReservedState();

					if (r != null) {
						r.setAvailableDate(given);
						r.setState(ReservationState.AVAILABLE);
					}
					
				}
				
			}

			if (++count % 1 == 0) {
				// flush a batch of updates and release memory:
				session.flush();
				session.clear();
			}

		}

	}
	
	private void clearPunishedReservations(LocalDate given) {

		//System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  clearPunishedReservations");
		Session session = em.unwrap(Session.class);

		ScrollableResults patronCursor = session
				.createQuery("from Patron p " 
							+ "join p.reservations r " 
							+ "where r.state = :state "
							+ "order by r.unCollectedDate")
				.setParameter("state", ReservationState.NOTIFIED)
				.setCacheMode(CacheMode.IGNORE)
				.scroll(ScrollMode.FORWARD_ONLY);

		int count = 0;

		while (patronCursor.next()) {

			Patron patron = (Patron) patronCursor.get(0);

			int maxUnCollectedNo = patron.getPatronType().getMaxUncollectedNo();
			int unCollectedNo = patron.getUnCollectedNo().intValue();

			if (unCollectedNo >= maxUnCollectedNo) {

				Iterator<Reservation> it = patron.getReservations().iterator();

				for (int i = 0; i < maxUnCollectedNo; i++) {
					Reservation r = (Reservation) it.next();
					r.setState(ReservationState.PUNISHED);
				}

				short unCollectedNoAfterPunish = (short) (unCollectedNo - maxUnCollectedNo);

				patron.setUnCollectedNo(unCollectedNoAfterPunish);

			}

			if (++count % 1 == 0) {
				// flush a batch of updates and release memory:
				session.flush();
				session.clear();
			}

		}

	}
	
}
