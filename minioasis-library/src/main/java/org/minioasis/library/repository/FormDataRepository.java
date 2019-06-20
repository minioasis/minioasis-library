package org.minioasis.library.repository;

import java.util.List;

import org.minioasis.library.domain.DataType;
import org.minioasis.library.domain.FormData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface FormDataRepository extends JpaRepository<FormData, Long>{

	@Query("SELECT d FROM FormData d WHERE d.data LIKE %?1%  AND d.type = ?2")
	List<FormData> findByDataContainingAndType(String data, DataType type);
	
	@Query("SELECT d FROM FormData d WHERE d.data LIKE %?1%  AND d.type = ?2")
	Page<FormData> findByDataContainingAndType(String data, DataType type, Pageable pageable);
}
