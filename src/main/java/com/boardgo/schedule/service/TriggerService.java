package com.boardgo.schedule.service;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobKey;
import org.quartz.TriggerBuilder;
import org.springframework.stereotype.Service;

@Service
public class TriggerService {
    public TriggerBuilder<CronTrigger> cronTrigger(
            JobKey jobKey, CronScheduleBuilder cronScheduleBuilder) {
        return TriggerBuilder.newTrigger()
                .forJob(jobKey)
                .withIdentity(jobKey.getName())
                .withSchedule(cronScheduleBuilder);
    }
}
