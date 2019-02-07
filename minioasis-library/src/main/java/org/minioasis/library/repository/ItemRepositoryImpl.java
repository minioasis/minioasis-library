package org.minioasis.library.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.minioasis.library.domain.Biblio;
import org.minioasis.library.domain.Item;
import org.minioasis.library.domain.YesNo;

// if you use the class name as ItemRepositoryCustomImpl, this will cause PropertyReferenceException error !!
// org.springframework.data.mapping.PropertyReferenceException: No property..
// by default, the name should be ItemRepositoryImpl instead of ItemRepositoryCustomImpl
public class ItemRepositoryImpl implements ItemRepositoryCustom {

	@PersistenceContext
	private EntityManager em;

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
