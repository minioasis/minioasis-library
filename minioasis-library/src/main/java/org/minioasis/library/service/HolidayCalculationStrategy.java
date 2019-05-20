package org.minioasis.library.service;

import java.time.LocalDate;

public interface HolidayCalculationStrategy {
	
	LocalDate getNewDueDateAfterHolidays(LocalDate dueDate);

}
