package com.example.scheduling_tasks.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class ScheduledTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduledTask.class);

    private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("HH:mm:ss");

    @Scheduled(fixedRate = 5000) // Define cuando se ejecuta un metodo determinado
    public void reportCurrentTime() {
        LOGGER.info("The time is now {}", SIMPLE_DATE_FORMAT.format(new Date()));
    }
}
