package org.minioasis.library.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
@ComponentScan({"org.minioasis.library.tasksheduler","org.minioasis.library.telegram"})
public class SchedulingConfig {

}
