package com.boardgo.schedule.service;

import com.boardgo.common.exception.CustomIllegalArgumentException;
import com.boardgo.domain.meeting.service.MeetingBatchServiceV1;
import com.boardgo.schedule.job.FinishedMeetingStateJob;
import jakarta.annotation.PostConstruct;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FinishedMeetingStateService {

    private final Scheduler scheduler;
    private final TriggerService triggerService;

    private final MeetingBatchServiceV1 meetingBatchServiceV1;

    @PostConstruct
    private void jobProgress() throws SchedulerException {
        updateFinishMeetingState();

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.schedule(this::resetSchedule, 30, TimeUnit.MINUTES);
    }

    public void updateFinishMeetingState() {
        String description = "[모임 종료] 상태 변경 Job";
        JobKey jobKey = JobKey.jobKey("finishedMeeting", "MeetingState");

        JobDetail jobDetail = finishedMeetingStateBuild(jobKey, description);
        CronTrigger cronTrigger =
                triggerService
                        .cronTrigger(jobKey, CronScheduleBuilder.cronSchedule("0 0/30 * ? * *"))
                        .withDescription(description)
                        .startNow()
                        .build();
        schedule(jobDetail, cronTrigger);
    }

    private JobDetail finishedMeetingStateBuild(JobKey jobKey, String description) {
        return JobBuilder.newJob(FinishedMeetingStateJob.class)
                .withIdentity(jobKey.getName(), jobKey.getGroup())
                .withDescription(description)
                .build();
    }

    private void schedule(JobDetail jobDetail, Trigger lastTrigger) {
        try {
            scheduler.getContext().put("meetingBatchServiceV1", meetingBatchServiceV1);
            scheduler.start();
            scheduler.scheduleJob(jobDetail, lastTrigger);
        } catch (SchedulerException e) {
            JobExecutionException jobExecutionException = new JobExecutionException(e);
            jobExecutionException.setRefireImmediately(true);
        }
    }

    private void resetSchedule() {
        try {
            scheduler.deleteJob(JobKey.jobKey("finishedMeeting", "MeetingState"));
        } catch (SchedulerException se) {
            throw new CustomIllegalArgumentException("deleteSchedule");
        }
        updateFinishMeetingState();
    }
}
