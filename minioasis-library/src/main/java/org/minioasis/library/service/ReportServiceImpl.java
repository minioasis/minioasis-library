package org.minioasis.library.service;

import java.time.LocalDate;
import java.util.List;

import org.jooq.Record3;
import org.jooq.Record4;
import org.jooq.Result;
import org.minioasis.library.domain.Checkout;
import org.minioasis.library.domain.CheckoutState;
import org.minioasis.library.domain.search.CheckoutPatronCriteria;
import org.minioasis.library.domain.search.TopCheckoutPatronsSummary;
import org.minioasis.library.domain.search.TopPopularBooksCriteria;
import org.minioasis.library.domain.search.TopPopularBooksSummary;
import org.minioasis.library.repository.CheckoutRepository;
import org.minioasis.library.repository.PatronRepository;
import org.minioasis.report.chart.ChartData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_LIBRARIAN')")
public class ReportServiceImpl implements ReportService {

	@Autowired
	private PatronRepository patronRepository;
	@Autowired
	private CheckoutRepository checkoutRepository;
	
	public Result<Record4<Integer,Integer, String, Integer>> CountPatronsByTypes(){
		return this.patronRepository.CountPatronsByTypes();
	}
	
	public Result<Record3<Integer, String, Integer>> CountPatronsByTypes3(int year){
		return patronRepository.CountPatronsByTypes3(year);
	}
	
	public List<Integer> getAllPatronsStartedYears(){
		return patronRepository.getAllPatronsStartedYears();
	}
	
	public List<TopCheckoutPatronsSummary> topListPatronsForCheckouts(CheckoutPatronCriteria criteria){
		return checkoutRepository.topListPatronsForCheckouts(criteria);
	}
	
	public List<TopPopularBooksSummary> topPopularBooks(TopPopularBooksCriteria criteria){
		return checkoutRepository.topPopularBooks(criteria);
	}
	
	public String topListPatronsForCheckouts_JSON() {
		return checkoutRepository.topListPatronsForCheckouts_JSON();
	}
	
	public List<ChartData> CountPatronsByTypes(int from, int to){
		return patronRepository.CountPatronsByTypes(from, to);
	}
	
	public 	Page<Checkout> findAllOverDueOrderByDueDateCardKey(List<CheckoutState> cStates, LocalDate given, Pageable pageable){
		
		Page<Checkout> page = checkoutRepository.findAllOverDueOrderByDueDateCardKey(cStates, given, pageable);
		
		List<Checkout> checkouts = page.getContent();
		for(Checkout c : checkouts) {
			c.preparingCheckoutOn(given);
		}
		
		return page;
	}
	
	public Page<Checkout> findAllOverDueOrderByGroupPatronTypeDueDateCardKey(List<CheckoutState> cStates, LocalDate given, Pageable pageable){
		
		Page<Checkout> page = checkoutRepository.findAllOverDueOrderByGroupPatronTypeDueDateCardKey(cStates, given, pageable);
		
		List<Checkout> checkouts = page.getContent();
		for(Checkout c : checkouts) {
			c.preparingCheckoutOn(given);
		}
		
		return page;
	}
}
