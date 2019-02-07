package org.minioasis.library.service;

import java.util.Date;

public interface HolidayCalculationStrategy {
	
	// this is for checkout
	Date getNewDueDateAfterHolidays(Date dueDate);

	// this is for fine
	int getNoOfHolidaysBetween(Date start , Date end);

}
