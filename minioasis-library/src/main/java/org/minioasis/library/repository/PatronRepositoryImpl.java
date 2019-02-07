package org.minioasis.library.repository;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Root;

import org.minioasis.library.domain.Group;
import org.minioasis.library.domain.Patron;

public class PatronRepositoryImpl implements PatronRepositoryCustom {

	@PersistenceContext
	private EntityManager em;
	
	public int bulkUpdateGroup(List<Long> ids, Group group, Date now){
		
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
