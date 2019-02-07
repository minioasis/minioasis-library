package org.minioasis.library.service;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.minioasis.library.domain.Holiday;
import org.minioasis.library.repository.HolidayRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HolidayCalculationStrategyImpl implements HolidayCalculationStrategy {
	
	private final static int ONE_DAY = 1;
	@Autowired
	private HolidayRepository holidayRepository;
	
	private int calculateDifference(Date a, Date b) {
		
	    int tempDifference = 0;
	    int difference = 0;
	    Calendar earlier = Calendar.getInstance();
	    Calendar later = Calendar.getInstance();
	 
	    if (a.compareTo(b) < 0){
	    	
	        earlier.setTime(a);
	        later.setTime(b);
	        
	    } else {
	    	
	        earlier.setTime(b);
	        later.setTime(a);
	        
	    }
	 
	    while (earlier.get(Calendar.YEAR) != later.get(Calendar.YEAR)) {
	    	
	        tempDifference = 365 * (later.get(Calendar.YEAR) - earlier.get(Calendar.YEAR));
	        difference += tempDifference;
	 
	        earlier.add(Calendar.DAY_OF_YEAR, tempDifference);
	    }
	 
	    if (earlier.get(Calendar.DAY_OF_YEAR) != later.get(Calendar.DAY_OF_YEAR)) {
	    	
	        tempDifference = later.get(Calendar.DAY_OF_YEAR) - earlier.get(Calendar.DAY_OF_YEAR);
	        difference += tempDifference;
	 
	        earlier.add(Calendar.DAY_OF_YEAR, tempDifference);
	    }
	 
	    return difference+1;
	    
	}

	private Date tomorrow(Date given){
		
		GregorianCalendar nextcheckoutDateCalendar = new GregorianCalendar();
		nextcheckoutDateCalendar.setTime(given);
		nextcheckoutDateCalendar.add(Calendar.DATE,ONE_DAY);
		
		return nextcheckoutDateCalendar.getTime();
		
	}
	
	public int getNoOfHolidaysBetween(Date start, Date end){
		
		int days = 0;
		
		List<Holiday> holidays = this.holidayRepository.findByInBetweenWithFines(start, end, Boolean.FALSE);

		for(Holiday h : holidays){
			
			int different = 0;
			
			Date startDate = h.getStartDate();
			Date endDate = h.getEndDate();
			
			if(end.after(startDate) && (end.after(endDate) || end.compareTo(endDate) == 0)){
				
				different = calculateDifference(startDate,endDate);
			}
			
			if(end.compareTo(startDate) == 0){
					
					different = 1;
			}

			days = days + different;			
		}
		
		return days;
	}
	
	public Date getNewDueDateAfterHolidays(Date dueDate){
		
		Date newDueDate = dueDate;

		Holiday holiday = this.holidayRepository.getHolidayByDueDate(dueDate);

		if(holiday != null)
			newDueDate = tomorrow(holiday.getEndDate());

		return newDueDate;
		
	}

}
