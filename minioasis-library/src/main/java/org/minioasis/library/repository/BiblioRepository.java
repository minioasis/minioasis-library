package org.minioasis.library.repository;

import org.minioasis.library.domain.Biblio;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BiblioRepository extends JpaRepository<Biblio, Long>, BiblioRepositoryCustom {

	@Query("SELECT b FROM Biblio b WHERE b.id = ?1")
	public Biblio getBiblioDetails(long id);
	
	@Query("SELECT b FROM Biblio b LEFT JOIN FETCH b.items WHERE b.id = ?1")
	public Biblio getBiblioFetchItems(long id);
	
	public Biblio findByIsbn(String isbn);
	
	@Query("SELECT b FROM Biblio b WHERE title LIKE %?1% OR isbn LIKE %?1% OR note LIKE %?1%")
	public Page<Biblio> findByTitleAndIsbnAndNoteContaining(String title, Pageable pageable); 
	
	@Query("SELECT b FROM Biblio b LEFT JOIN b.items i WHERE i.biblio is NULL")
	public Page<Biblio> findAllUncompleteBiblios(Pageable pageable);
	
}
