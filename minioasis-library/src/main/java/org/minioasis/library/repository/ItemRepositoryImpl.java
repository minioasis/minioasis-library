package org.minioasis.library.repository;

import static org.minioasis.library.jooq.tables.Biblio.BIBLIO;
import static org.minioasis.library.jooq.tables.Item.ITEM;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Table;
import org.jooq.impl.DSL;
import org.minioasis.library.domain.Biblio;
import org.minioasis.library.domain.Item;
import org.minioasis.library.domain.YesNo;
import org.minioasis.library.domain.search.ItemCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

// if you use the class name as ItemRepositoryCustomImpl, this will cause PropertyReferenceException error !!
// org.springframework.data.mapping.PropertyReferenceException: No property..
// by default, the name should be ItemRepositoryImpl instead of ItemRepositoryCustomImpl
public class ItemRepositoryImpl implements ItemRepositoryCustom {

	@PersistenceContext
	private EntityManager em;

	@Autowired
	private DSLContext dsl;
	
	private org.minioasis.library.jooq.tables.Biblio b = BIBLIO.as("b");
	private org.minioasis.library.jooq.tables.Item i = ITEM.as("i");
	
	public Page<Item> findByCriteria(ItemCriteria criteria, Pageable pageable){

		Table<?> table = createTable(criteria);
	
		org.jooq.Query jooqQuery = dsl.select()
									.from(table)
									.where(condition(criteria))
									.limit(pageable.getPageSize())
									.offset((int)pageable.getOffset());
		
		Query q = em.createNativeQuery(jooqQuery.getSQL(), Item.class);
		setBindParameterValues(q, jooqQuery);
		
		List<Item> items = q.getResultList();
		
		long total = findCountByCriteriaLikeExpression(criteria);
		
		return new PageImpl<>(items, pageable, total);
	}
	
	private long findCountByCriteriaLikeExpression(ItemCriteria criteria) {

		Table<?> table = createTable(criteria);
		
        long total = dsl.fetchCount(
        						dsl.select(i.ID)
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
	
	private Table<?> createTable(ItemCriteria criteria) {
		
		final String biblio = criteria.getBiblio();
		
		Table<?> table = i;
		
		if(biblio != null) {
			table = table.innerJoin(b).on(i.BIBLIO_ID.eq(b.ID)).and(b.ISBN.likeIgnoreCase("%" + biblio + "%"));
		}
		
		return table;
	}
	
	private Condition condition(ItemCriteria criteria) {
		
	    Condition condition = DSL.trueCondition();
	    
		final String keyword = criteria.getKeyword();
		final Date firstFrom = criteria.getFirstCheckinFrom();
		final Date firstTo = criteria.getFirstCheckinTo();
		final Date lastFrom = criteria.getLastCheckinFrom();
		final Date lastTo = criteria.getLastCheckinTo();
		final Date expiredFrom = criteria.getExpiredFrom();
		final Date expiredTo = criteria.getExpiredTo();
		
		final Set<YesNo> actives = criteria.getActives();
		final Set<Long> itemdurations = criteria.getItemDurations();
		final Set<Long> itemstatuz = criteria.getItemStatuz();
		final Set<Long> locations = criteria.getLocations();
		final Set<String> itemStates = criteria.getItemStates();
		final Set<YesNo> checkStocks = criteria.getCheckeds();
		
	    if (keyword != null) {
	    	condition = condition.and(i.BARCODE.likeIgnoreCase("%" + keyword + "%"))
	    					.or(i.SHELF_MARK.likeIgnoreCase("%" + keyword + "%"))
	    					.or(i.SOURCE.likeIgnoreCase("%" + keyword + "%"));				
	    }
		if(firstFrom != null && firstTo != null){
			condition = condition.and(i.FIRST_CHECKIN.ge(new java.sql.Timestamp(firstFrom.getTime()))
							.and(i.FIRST_CHECKIN.le(new java.sql.Timestamp(firstTo.getTime()))));
		}
		if(lastFrom != null && lastTo != null){
			condition = condition.and(i.LAST_CHECKIN.ge(new java.sql.Timestamp(lastFrom.getTime()))
							.and(i.LAST_CHECKIN.le(new java.sql.Timestamp(lastTo.getTime()))));
		}
		if(expiredFrom != null && expiredTo != null){
			condition = condition.and(i.EXPIRED.ge(new java.sql.Date(expiredFrom.getTime()))
							.and(i.EXPIRED.le(new java.sql.Date(expiredTo.getTime()))));
		}
		if(actives != null && actives.size() > 0){
			condition = condition.and(i.ACTIVE.in(actives));
		}
		if(checkStocks != null && checkStocks.size() > 0){
			condition = condition.and(i.CHECKED.in(checkStocks));
		}
		if(itemdurations != null && itemdurations.size() > 0){
			condition = condition.and(i.ITEM_DURATION_ID.in(itemdurations));
		}
		if(itemStates != null && itemStates.size() > 0){
			condition = condition.and(i.STATE.in(itemStates));
		}
		if(itemstatuz != null && itemstatuz.size() > 0){
			condition = condition.and(i.ITEM_STATUS_ID.in(itemstatuz));
		}
		if(locations != null && locations.size() > 0){
			condition = condition.and(i.LOCATION_ID.in(locations));
		}

		
	    return condition;
	}
	
	public void stockCheck(){
		
		
 		// ******************** stock check ********************

 		Session session = em.unwrap(Session.class);
		ScrollableResults itemCursor = session.createQuery("from Item")
				.scroll();
		
		int count = 0;
		while (itemCursor.next()) {
			Item item = (Item) itemCursor.get(0);
			item.setChecked(YesNo.N);
			if (++count % 100 == 0) {
				session.flush();
				session.clear();
			}
			
		}

		
/*		
		// **************** Concatenating Authors ****************
		Session session = em.unwrap(Session.class);
		
		List<Biblio> biblios = getAllBibliosConcatenatingAuthors();

		session.flush();
		session.clear();
						
		int count = 0;		
		for (int i = 0 ; i < biblios.size() ; i++) {

			Biblio b = biblios.get(i);
			Long id = b.getId();
			
			Biblio biblio = (Biblio)session.load(Biblio.class, id);
			biblio.setAuthor(b.getAuthor());
			session.update(biblio);
			
			// System.out.println(biblio.getId() + " : " + biblio.getTitle() + "  : " + biblio.getAuthor());
			
			if (++count % 100 == 0) {
				session.flush();
				session.clear();
			}

		}
*/			

/*	
		// **************** Concatenating Subjects ****************
		Session session = em.unwrap(Session.class);
		
		List<Biblio> biblios = getAllBibliosConcatenatingSubjects();

		session.flush();
		session.clear();
		
		int count = 0;		
		for (int i = 0 ; i < biblios.size() ; i++) {

			Biblio b = biblios.get(i);
			Long id = b.getId();
			
			Biblio biblio = (Biblio)session.load(Biblio.class, id);
			biblio.setSubject(b.getSubject());
			session.update(biblio);
			
			// System.out.println(biblio.getId() + " : " + biblio.getTitle() + "  : " + biblio.getAuthor());
			
			if (++count % 100 == 0) {
				session.flush();
				session.clear();
			}

		}*/

		
	}
	
	@SuppressWarnings("unused")
	private List<Biblio> getAllBibliosConcatenatingAuthors() {
		
		Query q = em.createNativeQuery("SELECT b.id, active, biblio_type, binding, "
				+ "class_mark, description, edition, isbn, coden, issn, language,"
				+ "note, pages, publication_place, publishing_year, title, updated,"
				+ "image_id, publication_type_id, publisher_id, series_id, GROUP_CONCAT(a.name ORDER BY a.name) author, topic FROM biblio b INNER JOIN biblio_author ba ON b.id = ba.biblio_id INNER JOIN author a ON ba.author_id = a.id GROUP BY b.id, b.title", Biblio.class);
		
		@SuppressWarnings("unchecked")
		List<Biblio> biblios = q.getResultList();
		
		return biblios;
		
	}
	
	@SuppressWarnings("unused")
	private List<Biblio> getAllBibliosConcatenatingSubjects() {
		
		Query q = em.createNativeQuery("SELECT b.id, active, biblio_type, binding, "
				+ "class_mark, description, edition, isbn, coden, issn, language,"
				+ "note, pages, publication_place, publishing_year, title, updated,"
				+ "image_id, publication_type_id, publisher_id, series_id, author, GROUP_CONCAT(s.name ORDER BY s.name) topic FROM biblio b INNER JOIN biblio_subject bs ON b.id = bs.biblio_id INNER JOIN subject s ON bs.subject_id = s.id GROUP BY b.id, b.title", Biblio.class);
		
		@SuppressWarnings("unchecked")
		List<Biblio> biblios = q.getResultList();
		
		return biblios;
		
	}

}
