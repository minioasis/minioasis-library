package org.minioasis.library.service;

import java.util.List;

import org.jooq.Record1;
import org.jooq.Record3;
import org.jooq.Record4;
import org.jooq.Result;
import org.minioasis.library.domain.search.CheckoutPatronCriteria;
import org.minioasis.library.domain.search.TopCheckoutPatronsSummary;
import org.minioasis.library.domain.search.TopPopularBooksCriteria;
import org.minioasis.library.domain.search.TopPopularBooksSummary;
import org.minioasis.report.chart.ChartData;

public interface ReportService {

	Result<Record4<Integer,Integer, String, Integer>>  CountPatronsByTypes();
	
	Result<Record3<Integer, String, Integer>> CountPatronsByTypes3(int year);
	
	Result<Record1<Integer>> getAllPatronsStartedYears();
	
	List<TopCheckoutPatronsSummary> topListPatronsForCheckouts(CheckoutPatronCriteria criteria);
	
	List<TopPopularBooksSummary> topPopularBooks(TopPopularBooksCriteria criteria);
	
	String topListPatronsForCheckouts_JSON();
	
	List<ChartData> CountPatronsByTypes(int from, int to);
}
