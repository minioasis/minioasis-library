package org.minioasis.library.tasksheduler;

import java.time.LocalDate;

import javax.annotation.PostConstruct;

import org.minioasis.library.service.LibraryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ReservationTaskScheduler {

	private static final Logger logger = LoggerFactory.getLogger(ReservationTaskScheduler.class);

	@Autowired
	private LibraryService service;

	/*
	 * @Scheduled(cron = "[Seconds] [Minutes] [Hours] [Day of month] [Month] [Day of week] [Year]")
	 * 
	 * Fires at 12 PM every day : 						@Scheduled(cron = "0 0 12 * * ?") 
	 * Fires at 10:15AM every day in the year 2005 : 	@Scheduled(cron = "0 15 10 * * ? 2005") 
	 * Fires every 20 seconds : 						@Scheduled(cron = "0/20 * * * * ?") 
	 */

//	@Scheduled(fixedRate = 5000)
//	public void reportCurrentTime() {
//		logger.info("Reservation Scheduler LOG : Testing : " + LocalDate.now());
//	}

	@Scheduled(cron = "0 0 0 * * ?")
	public void refreshReservationStatesMidnight() {

		logger.info("Reservation Scheduler LOG : Fire Midnight : " + LocalDate.now());
		this.service.refreshReservationStates();

	}

	@PostConstruct
	public void onStartup() {
		logger.info("Reservation Scheduler LOG : Fire on start up : " + LocalDate.now());
		this.service.refreshReservationStates();
	}

}
