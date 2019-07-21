package org.minioasis.library.repository;

import java.util.List;

import org.minioasis.library.domain.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long>, ItemRepositoryCustom {
	
	@Query("SELECT i FROM Item i"
			+ " LEFT JOIN FETCH i.biblio b"
			+ " WHERE i.barcode = ?1")
	Item getItemFetchBiblio(String barcode);
	
	@Query("SELECT i FROM Item i"
			+ " JOIN FETCH i.itemStatus"
			+ " JOIN FETCH i.location"
			+ " WHERE i.id = ?1")
	Item getItemFetchItemRelated(long id);
	
	@Query("SELECT i FROM Item i"
			+ " LEFT JOIN FETCH i.itemStatus"
			+ " WHERE i.barcode = ?1")
	Item getItemFetchItemStatus(String barcode);
	
	@Query("SELECT i FROM Item i"
			+ " LEFT JOIN FETCH i.itemStatus"
			+ " LEFT JOIN FETCH i.location"
			+ " LEFT JOIN FETCH i.biblio"
			+ " WHERE i.barcode = ?1")
	Item getItemFetchRelatedBiblio(String barcode);
	
	@Query("SELECT i FROM Item i"
			+ " LEFT JOIN FETCH i.itemStatus"
			+ " LEFT JOIN FETCH i.location"
			+ " LEFT JOIN FETCH i.biblio"
			+ " WHERE i.barcode = ?1 AND i.state.state in ?2")
	Item getItemForCheckin(String barcode, String[] states);
	
	@Query("SELECT i FROM Item i"
			+ " LEFT JOIN FETCH i.itemStatus"
			+ " LEFT JOIN FETCH i.biblio b"
			+ " WHERE b.isbn = ?1 AND i.state.state in ?2")
	List<Item> findItemsByIsbnAndStates(String isbn, String[] states);
	
	@Query("SELECT i FROM Item i"
			+ " LEFT JOIN FETCH i.itemStatus"
			+ " LEFT JOIN FETCH i.biblio b"
			+ " WHERE b.isbn = ?1")
	List<Item> findItemsByIsbn(String isbn);
	
	@Query("SELECT i FROM Item i"
			+ " LEFT JOIN FETCH i.biblio b"
			+ " WHERE b.id = ?1")	
	List<Item> findAllItemsOrderByBarcode(long id);
	
	@Query("SELECT i FROM Item i Where i.biblio.title LIKE %?1% OR i.barcode LIKE %?1% OR i.shelfMark LIKE %?1% OR i.source LIKE %?1%")
	Page<Item> findAllByKeyword(String keyword, Pageable pageable);
	
}
