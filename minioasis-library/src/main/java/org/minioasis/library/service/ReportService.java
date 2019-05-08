package org.minioasis.library.service;

import org.jooq.Record1;
import org.jooq.Record3;
import org.jooq.Record4;
import org.jooq.Result;

public interface ReportService {

	Result<Record4<Integer,Integer, String, Integer>>  CountPatronsByTypes();
	
	Result<Record3<Integer, String, Integer>> CountPatronsByTypes3(int year);
	
	Result<Record1<Integer>> getAllPatronsStartedYears();
}
