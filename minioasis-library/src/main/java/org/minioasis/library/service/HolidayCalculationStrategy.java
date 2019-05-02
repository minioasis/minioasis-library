package org.minioasis.library.service;

import java.time.LocalDate;

public interface HolidayCalculationStrategy {
	
	// this is for checkout
	LocalDate getNewDueDateAfterHolidays(LocalDate dueDate);

	// this is for fine
	int getNoOfHolidaysBetween(LocalDate start , LocalDate end);

}
