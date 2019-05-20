package org.minioasis.library.service;

import java.time.LocalDate;

import org.minioasis.library.domain.Holiday;
import org.minioasis.library.repository.HolidayRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HolidayCalculationStrategyImpl implements HolidayCalculationStrategy {
	
	@Autowired
	private HolidayRepository holidayRepository;
	
	public LocalDate getNewDueDateAfterHolidays(LocalDate dueDate){
		
		LocalDate newDueDate = dueDate;

		Holiday holiday = this.holidayRepository.getHolidayByDueDate(dueDate);

		if(holiday != null)
			newDueDate = holiday.getEndDate().plusDays(1);

		return newDueDate;
		
	}

}
