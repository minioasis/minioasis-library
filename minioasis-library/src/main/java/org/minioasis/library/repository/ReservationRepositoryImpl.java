package org.minioasis.library.repository;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.CacheMode;
import org.hibernate.Query;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.minioasis.library.domain.Biblio;
import org.minioasis.library.domain.Patron;
import org.minioasis.library.domain.Reservation;
import org.minioasis.library.domain.ReservationState;

public class ReservationRepositoryImpl implements ReservationRepositoryCustom {

	@PersistenceContext
	private EntityManager em;

	@SuppressWarnings("unchecked")
	private List<Reservation> getExpiredBiblioReservations() {

		Session session = em.unwrap(Session.class);

		Query query = session
				.createQuery("from Reservation r " 
							+ "left join fetch r.biblio b "
							+ "left join fetch b.reservations rs " 
							+ "where b is not null " 
							+ "and r.state = :state "
							+ "and rs.state in ('RESERVE','AVAILABLE','NOTIFIED')")
				.setParameter("state", ReservationState.NOTIFIED);

		return query.list();

	}
	
	public void refreshReservationStates() {
		
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  refreshReservationStates");
		Date now = new Date();
		
		clearExpiredReservations(now);
		clearUncollectedReservations(now);
		clearPunishedReservations(now);
		//add clearReportLostAndPaidReservation(aGivenDate) if possible
		
	}
	
	private void clearExpiredReservations(Date given) {
		
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

			if (reservation.getExpiryDate().before(given)){
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
	
	private void clearUncollectedReservations(Date given){
		
		//System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  clearUncollectedReservations");
		Session session = em.unwrap(Session.class);
		
		int count = 0;
		long oneDay = 86400000;
		
		// biblio
		List<Reservation> biblioReservations = getExpiredBiblioReservations();

		Iterator<Reservation> itb = biblioReservations.iterator();

		while (itb.hasNext()) {
			
			Reservation reservation = (Reservation) itb.next();

			// TODO : LUT use old or new ? old now !
			int maxCollectablePeriod = reservation.getPatronType().getMaxCollectablePeriod().intValue();
			long notificationTime = reservation.getNotificationDate().getTime();
			long bufferTime = notificationTime + maxCollectablePeriod * oneDay;
			Date bufferDate = new Date(bufferTime);

			Patron patron = reservation.getPatron();
			short unCollectedNo = patron.getUnCollectedNo();
			short plusOne = (short) (unCollectedNo + 1);

			if(reservation.getBiblio() != null){

				if (bufferDate.before(given)) {

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
	
	private void clearPunishedReservations(Date given) {

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
