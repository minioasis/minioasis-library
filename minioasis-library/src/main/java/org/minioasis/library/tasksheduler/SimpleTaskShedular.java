package org.minioasis.library.tasksheduler;

import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class SimpleTaskShedular {

	//private static final Logger logger = LoggerFactory.getLogger(SimpleTaskShedular.class);
	
	/*
	 * @Scheduled(cron =
	 * "[Seconds] [Minutes] [Hours] [Day of month] [Month] [Day of week] [Year]")
	 * 
	 * Fires at 12 PM every day : @Scheduled(cron = "0 0 12 * * ?") Fires at 10:15
	 * AM every day in the year 2005 : @Scheduled(cron = "0 15 10 * * ? 2005") Fires
	 * every 20 seconds : @Scheduled(cron = "0/20 * * * * ?")
	 * 
	 */

//	@Scheduled(fixedRate = 5000)
//	public void reportCurrentTime() {
//		logger.info("Simple Task Scheduler LOG --- NOW : " + LocalDate.now());
//	}
}
