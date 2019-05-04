package org.minioasis.library.service;

import org.jooq.Record3;
import org.jooq.Result;
import org.minioasis.library.repository.PatronRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReportServiceImpl implements ReportService {

	@Autowired
	private PatronRepository patronRepository;
	
	public Result<Record3<Integer, String, Integer>> CountPatronsByTypes(){
		return this.patronRepository.CountPatronsByTypes();
	}
}
