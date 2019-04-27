package org.minioasis.library.repository;

import java.util.List;

import org.minioasis.library.domain.AttachmentCheckout;
import org.minioasis.library.domain.AttachmentCheckoutState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AttachmentCheckoutRepository extends JpaRepository<AttachmentCheckout, Long>,  AttachmentCheckoutRepositoryCustom {

	@Query("SELECT ac FROM AttachmentCheckout ac WHERE ac.checkout.id = ?1 AND ac.state = ?2")
	List<AttachmentCheckout> findByCheckoutId(Long id,AttachmentCheckoutState state);
	
	@Query("SELECT ac FROM AttachmentCheckout ac WHERE ac.patron.cardKey = ?1 AND ac.state = ?2")
	List<AttachmentCheckout> findByCardKeyAndFilterByStates(String cardKey, AttachmentCheckoutState state);

	@Query("SELECT ac FROM AttachmentCheckout ac"
			+ " LEFT JOIN FETCH ac.attachment a"
			+ " WHERE a.barcode = ?1 AND ac.state = ?2")
	List<AttachmentCheckout> findByBarcodeAndFilterByStates(String barcode, AttachmentCheckoutState state);
}
