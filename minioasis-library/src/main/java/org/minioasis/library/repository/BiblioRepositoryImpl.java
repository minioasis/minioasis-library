package org.minioasis.library.repository;

import static org.minioasis.library.jooq.tables.Biblio.BIBLIO;
import static org.minioasis.library.jooq.tables.Publisher.PUBLISHER;
import static org.minioasis.library.jooq.tables.Series.SERIES;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Table;
import org.jooq.impl.DSL;
import org.minioasis.library.domain.Biblio;
import org.minioasis.library.domain.BiblioType;
import org.minioasis.library.domain.Binding;
import org.minioasis.library.domain.Language;
import org.minioasis.library.domain.YesNo;
import org.minioasis.library.domain.search.BiblioCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

public class BiblioRepositoryImpl implements BiblioRepositoryCustom {

	@PersistenceContext
	private EntityManager em;
	
	@Autowired
	private DSLContext dsl;
	
	private org.minioasis.library.jooq.tables.Biblio b = BIBLIO.as("b");
	private org.minioasis.library.jooq.tables.Publisher p = PUBLISHER.as("p");
	private org.minioasis.library.jooq.tables.Series s = SERIES.as("s");
	
	public Page<Biblio> findByCriteria(BiblioCriteria criteria, Pageable pageable){

		Table<?> table = createTable(criteria);
	
		org.jooq.Query jooqQuery = dsl.select()
									.from(table)
									.where(condition(criteria))
									.limit(pageable.getPageSize())
									.offset((int)pageable.getOffset());
		
		Query q = em.createNativeQuery(jooqQuery.getSQL(), Biblio.class);
		setBindParameterValues(q, jooqQuery);
		
		List<Biblio> biblios = q.getResultList();
		
		long total = findCountByCriteriaLikeExpression(criteria);
		
		return new PageImpl<>(biblios, pageable, total);
	}
	
	private long findCountByCriteriaLikeExpression(BiblioCriteria criteria) {

		Table<?> table = createTable(criteria);
		
        long total = dsl.fetchCount(
        						dsl.select(b.ID)
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
	
	private Table<?> createTable(BiblioCriteria criteria) {
		
		final String publisher = criteria.getPublisher();
		final String series = criteria.getSeries();
		
		Table<?> table = b;
		
		if(publisher != null) {
			table = table.innerJoin(p).on(b.PUBLISHER_ID.eq(p.ID)).and(p.NAME.likeIgnoreCase("%" + publisher + "%"));
		}
		if(series != null) {
			table = table.innerJoin(s).on(b.SERIES_ID.eq(s.ID)).and(s.NAME.likeIgnoreCase("%" + series + "%"));
		}
		
		return b;
	}

	private Condition condition(BiblioCriteria criteria) {
		
	    Condition condition = DSL.trueCondition();
	    
		final String keyword1 = criteria.getKeyword1();
		final String keyword2 = criteria.getKeyword2();
		final LocalDateTime updatedFrom = criteria.getUpdatedFrom();
		final LocalDateTime updatedTo = criteria.getUpdatedTo();
		final String note = criteria.getNote();
		final Set<YesNo> actives = criteria.getActives();
		final Set<Binding> bindings = criteria.getBindings();		
		final Set<Language> languages = criteria.getLanguages();
		final Set<BiblioType> bibliotypes = criteria.getBibliotypes();
		
	    if (keyword1 != null) {
	    	condition = condition.and(b.TITLE.likeIgnoreCase("%" + keyword1 + "%"))
	    					.or(b.AUTHOR.likeIgnoreCase("%" + keyword1 + "%"))
	    					.or(b.ISBN.likeIgnoreCase("%" + keyword1 + "%"));				
	    }

	    if (keyword2 != null) {
	    	condition = condition.and(b.TOPIC.likeIgnoreCase("%" + keyword2 + "%"))
	    					.or(b.CLASS_MARK.likeIgnoreCase("%" + keyword2 + "%"))
	    					.or(b.ISSN.likeIgnoreCase("%" + keyword2 + "%"))
	    					.or(b.CODEN.likeIgnoreCase("%" + keyword2 + "%"))
	    					.or(b.DESCRIPTION.likeIgnoreCase("%" + keyword2 + "%"));				
	    }
	    if(note != null) {
	    	condition = condition.and(b.NOTE.likeIgnoreCase("%" + note + "%"));
	    }
		if(updatedFrom != null && updatedTo != null){
			condition = condition.and(b.UPDATED.ge(java.sql.Timestamp.valueOf(updatedFrom))
							.and(b.UPDATED.le(java.sql.Timestamp.valueOf(updatedTo))));
		}
		if(actives != null && actives.size() > 0){
			condition = condition.and(b.ACTIVE.in(actives));
		}
		if(bindings != null && bindings.size() > 0){
			condition = condition.and(b.BINDING.in(bindings));
		}
		if(languages != null && languages.size() > 0){
			condition = condition.and(b.LANGUAGE.in(languages));
		}
		if(bibliotypes != null && bibliotypes.size() > 0){
			condition = condition.and(b.BIBLIO_TYPE.in(bibliotypes));
		}
		
	    return condition;
	}
	
}
