package com.boardgo.schedule.job;

import com.boardgo.domain.notification.service.facade.NotificationCommandFacade;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

@DisallowConcurrentExecution
public class SendPushJob implements Job {

    @Autowired private NotificationCommandFacade notificationCommandFacade;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        notificationCommandFacade.sendNotificationPush();
    }
}
