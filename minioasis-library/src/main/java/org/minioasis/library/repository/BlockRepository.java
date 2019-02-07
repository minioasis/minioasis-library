package org.minioasis.library.repository;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.minioasis.library.domain.Block;
import org.minioasis.library.domain.BlockState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BlockRepository extends JpaRepository<Block, Long>, BlockRepositoryCustom {

	@Query("SELECT b FROM Block b LEFT JOIN FETCH b.patron WHERE b.id = ?1")
	Block getBlockFetchUser(long id);
	
	@Query("SELECT b FROM Block b WHERE b.patron.cardKey = ?1")
	List<Block> getFilterBlocksByCardKey(String cardKey);
	
	@Query("SELECT b FROM Block b WHERE b.patron.cardKey = ?1 AND b.startDate <= ?2 AND ?2 <= b.expiryDate AND b.state in ?3")
	Set<Block> findByCardKeyAndFilterByStates(String cardKey, Date given, List<BlockState> states);
	
	Page<Block> findAllByName(String name, Pageable pageable);
	
}
