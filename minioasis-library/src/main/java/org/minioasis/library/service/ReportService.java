package org.minioasis.library.service;

import java.time.LocalDate;
import java.util.List;

import org.jooq.Record1;
import org.jooq.Record3;
import org.jooq.Record4;
import org.jooq.Result;
import org.minioasis.library.domain.Checkout;
import org.minioasis.library.domain.CheckoutState;
import org.minioasis.library.domain.search.CheckoutPatronCriteria;
import org.minioasis.library.domain.search.TopCheckoutPatronsSummary;
import org.minioasis.library.domain.search.TopPopularBooksCriteria;
import org.minioasis.library.domain.search.TopPopularBooksSummary;
import org.minioasis.report.chart.ChartData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReportService {

	Result<Record4<Integer,Integer, String, Integer>>  CountPatronsByTypes();
	
	Result<Record3<Integer, String, Integer>> CountPatronsByTypes3(int year);
	
	Result<Record1<Integer>> getAllPatronsStartedYears();
	
	List<TopCheckoutPatronsSummary> topListPatronsForCheckouts(CheckoutPatronCriteria criteria);
	
	List<TopPopularBooksSummary> topPopularBooks(TopPopularBooksCriteria criteria);
	
	String topListPatronsForCheckouts_JSON();
	
	List<ChartData> CountPatronsByTypes(int from, int to);
	
	Page<Checkout> findAllOverDueOrderByDueDateCardKey(List<CheckoutState> cStates, LocalDate given, Pageable pageable);
	
	Page<Checkout> findAllOverDueOrderByGroupPatronTypeDueDateCardKey(List<CheckoutState> cStates, LocalDate given, Pageable pageable);
}
