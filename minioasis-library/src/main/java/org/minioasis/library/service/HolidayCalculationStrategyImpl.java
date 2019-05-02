package org.minioasis.library.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.minioasis.library.domain.Holiday;
import org.minioasis.library.repository.HolidayRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HolidayCalculationStrategyImpl implements HolidayCalculationStrategy {
	
	@Autowired
	private HolidayRepository holidayRepository;
	
	public int getNoOfHolidaysBetween(LocalDate start, LocalDate end){
		
		int days = 0;
		
		List<Holiday> holidays = this.holidayRepository.findByInBetweenWithFines(start, end, Boolean.FALSE);

		for(Holiday h : holidays){
			
			int different = 0;
			
			LocalDate startDate = h.getStartDate();
			LocalDate endDate = h.getEndDate();
			
			if(end.isAfter(startDate) && (end.isAfter(endDate) || end.compareTo(endDate) == 0)){
				
				different = (int)ChronoUnit.DAYS.between(startDate, endDate);
			}
			
			if(end.compareTo(startDate) == 0){
					
					different = 1;
			}

			days = days + different;			
		}
		
		return days;
	}
	
	public LocalDate getNewDueDateAfterHolidays(LocalDate dueDate){
		
		LocalDate newDueDate = dueDate;

		Holiday holiday = this.holidayRepository.getHolidayByDueDate(dueDate);

		if(holiday != null)
			newDueDate = holiday.getEndDate().plusDays(1);

		return newDueDate;
		
	}

}
