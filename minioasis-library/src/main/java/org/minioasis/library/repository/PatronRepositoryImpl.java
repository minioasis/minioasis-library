package org.minioasis.library.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
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
import org.jooq.Record4;
import org.jooq.Result;
import org.jooq.SortField;
import org.jooq.TableField;
import org.minioasis.library.domain.Group;
import org.minioasis.library.domain.Patron;
import org.minioasis.library.domain.YesNo;
import org.minioasis.library.domain.search.PatronCriteria;
import org.minioasis.report.chart.ChartData;
import org.minioasis.report.chart.Series;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import static org.minioasis.library.jooq.tables.Groups.GROUPS;
import static org.minioasis.library.jooq.tables.Patron.PATRON;
import static org.minioasis.library.jooq.tables.PatronType.PATRON_TYPE;

public class PatronRepositoryImpl implements PatronRepositoryCustom {

	private static final Logger LOGGER = LoggerFactory.getLogger(PatronRepositoryImpl.class);
	
	@PersistenceContext
	private EntityManager em;
	
	@Autowired
	private DSLContext dsl;
	
	private org.minioasis.library.jooq.tables.Patron p = PATRON.as("p");
	private org.minioasis.library.jooq.tables.PatronType pt = PATRON_TYPE.as("pt");
	private org.minioasis.library.jooq.tables.Groups g = GROUPS.as("g");

	private Condition condition(PatronCriteria criteria) {
		
	    Condition condition = DSL.trueCondition();
	    
		final String cardkey = criteria.getCardKey();
		final String keyword = criteria.getKeyword();
		final String note = criteria.getNote();
		final LocalDate createdFrom = criteria.getCreatedFrom();
		final LocalDate createdTo = criteria.getCreatedTo();
		final LocalDate startDateFrom = criteria.getStartDateFrom();
		final LocalDate startDateTo = criteria.getStartDateTo();
		final LocalDate endDateFrom = criteria.getEndDateFrom();
		final LocalDate endDateTo = criteria.getEndDateTo();
		final Set<YesNo> actives = criteria.getActives();
		final Set<Long> patronTypes = criteria.getPatronTypes();
		final Set<Long> groups = criteria.getGroups();		
	    
	    if (cardkey != null) {
	    	condition = condition.and(p.CARD_KEY.likeIgnoreCase("%" + cardkey + "%"));
	    }
	        
	    if (keyword != null) {
	    	condition = condition.and(p.NAME.likeIgnoreCase("%" + keyword + "%")
    						.or(p.NAME2.likeIgnoreCase("%" + keyword + "%")
    						.or(p.MOBILE.likeIgnoreCase("%" + keyword + "%")
    						.or(p.IC.likeIgnoreCase("%" + keyword + "%")))));
	    }

		if(note != null) {
			if(note.equals("isNotEmpty()")) {
				condition = condition.and(p.NOTE.isNotNull());
			}else {
				condition = condition.and(p.NOTE.likeIgnoreCase("%" + note + "%"));
			}			
		}
	    
	    if(createdFrom != null && createdTo != null) {
	    	condition = condition.and(p.CREATED.between(java.sql.Timestamp.valueOf(createdFrom.atStartOfDay()), java.sql.Timestamp.valueOf(createdTo.atStartOfDay())));
	    }
		if(startDateFrom != null && startDateTo != null){
			condition = condition.and(p.START_DATE.between(java.sql.Date.valueOf(startDateFrom),java.sql.Date.valueOf(startDateTo)));
		}
		if(endDateFrom != null && endDateTo != null){
			condition = condition.and(p.END_DATE.between(java.sql.Date.valueOf(endDateFrom), java.sql.Date.valueOf(endDateTo)));
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

	
	public Result<Record4<Integer, Integer, String, Integer>> CountPatronsByTypes() {
		
		Field<Integer> year = DSL.extract(p.START_DATE, DatePart.YEAR);
		Field<Integer> month = DSL.extract(p.START_DATE, DatePart.MONTH);
/*		In MYSQL :
		select extract(year from p.START_DATE) yr, extract(month from p.START_DATE) m, pt.NAME, count(*) 
		from PATRON p join PATRON_TYPE pt on pt.ID = p.PATRONTYPE_ID 
		group by pt.NAME, yr, m 
		order by yr, m, pt.NAME

*/
		Result<Record4<Integer,Integer, String, Integer>> records = dsl.select(year, month, pt.NAME, DSL.count())
									.from(p)
									.join(pt).on(pt.ID.eq(p.PATRONTYPE_ID))
									.groupBy(pt.CODE, year, month)
									.orderBy(year, month, pt.CODE)
									.fetch();
		
		return records;
	}

/*	
	select extract(month from p.START_DATE) m, pt.NAME, count(*) 
	from PATRON p join PATRON_TYPE pt on pt.ID = p.PATRONTYPE_ID 
	where p.START_DATE between date '2019-01-01' and date '2019-12-31' 
	group by pt.CODE, m 
	order by pt.CODE
*/

	public List<ChartData> CountPatronsByTypes(int from, int to){
		
		int period = 1;
		period = to - from + period;

		Result<Record3<Integer, String, Integer>> result = null;

		List<ChartData> list = new LinkedList<ChartData>();

		for (int i = 0; i < period; i++) {

			result = CountPatronsByTypes3(from);

			if (result.size() > 0) {
				
				ChartData chartData = new ChartData();
				// set title
				chartData.setTitle(new Integer(from).toString());

				List<Series> series = chartData.getSeries();

				Series s = new Series();
				String lastName = "";
				
				for (Record3<Integer, String, Integer> r : result) {

					Integer month = r.value1();
					String name = r.value2();
					Integer count = r.value3();

					String state = "";

					// set state
					if (s.getName() == null) {
						state = "NEW_SERIES";		
					}else {
						if(name.equals(lastName)) {
							state = "IN_THE_SERIES";	
						}else {
							state = "ANOTHER_SERIES";
							
						}
					}
					
					switch(state) {
						case "NEW_SERIES":		
							lastName = name;
							s.setName(name);
							s.getData()[month - 1] = count;		
							break;
							
						case "IN_THE_SERIES":	
							s.getData()[month - 1] = count;	
							break;
							
						case "ANOTHER_SERIES":
							lastName = name;
							
							series.add(s);
							s = new Series();			
							s.setName(name);
							s.getData()[month - 1] = count;
							break;				
					}

				}
				
				series.add(s);
				chartData.setSeries(series);
				list.add(chartData);
			}
			
			from++;
		}
		
		return list;
	}
	
	public Result<Record3<Integer, String, Integer>> CountPatronsByTypes3(int year) {
		
		Field<Integer> month = DSL.extract(p.START_DATE, DatePart.MONTH);

		Result<Record3<Integer, String, Integer>> records = dsl.select(month, pt.NAME, DSL.count())
									.from(p)
									.join(pt).on(pt.ID.eq(p.PATRONTYPE_ID))
									.where(p.START_DATE.between(java.sql.Date.valueOf(LocalDate.of(year, 1, 1)), java.sql.Date.valueOf(LocalDate.of(year, 12, 31))))
									.groupBy(pt.CODE, month)
									.orderBy(pt.CODE)
									.fetch();
		
		return records;
	}
	
	public List<Integer> getAllPatronsStartedYears(){
		
		Field<Integer> year = DSL.extract(p.START_DATE, DatePart.YEAR);
		
		List<Integer> records = dsl.selectDistinct(year)
				.from(p)
				.orderBy(year)
				.fetch(year, Integer.class);
		
		return records;
	}
	
	public Page<Patron> findByCriteria(PatronCriteria criteria, Pageable pageable){
		
		org.jooq.Query jooqQuery = dsl.select()
									.from(p)
									.join(pt).on(pt.ID.eq(p.PATRONTYPE_ID))
									.join(g).on(g.ID.eq(p.GROUP_ID))
									.where(condition(criteria))
									.orderBy(getSortFields(pageable.getSort()))
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

        	java.lang.reflect.Field tableField = p.getClass().getField(sortFieldName.toUpperCase());
            sortField = (TableField<?, ?>) tableField.get(p);
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
