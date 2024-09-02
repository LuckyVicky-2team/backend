package com.boardgo.schedule.service;

import static com.boardgo.schedule.service.enums.ScheduleJobs.COMPLETED_MEETING;

import com.boardgo.schedule.job.CompletedMeetingStateJob;
import com.boardgo.schedule.service.enums.ScheduleJobs;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.quartz.JobDetail;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CompletedMeetingStateService {

    private final Scheduler scheduler;
    private final TriggerService triggerService;
    private final JobDetailService jobDetailService;

    @PostConstruct
    private void jobProgress() throws SchedulerException {
        updateCompleteMeetingState();
    }

    public void updateCompleteMeetingState() {
        ScheduleJobs job = COMPLETED_MEETING;
        JobKey jobKey = JobKey.jobKey(job.name(), job.getJobGroup());
        final int intervalInMinutes = 1;

        JobDetail jobDetail =
                jobDetailService.jobDetailBuilder(jobKey, CompletedMeetingStateJob.class);
        Trigger simpleTrigger = triggerService.simpleTrigger(jobKey, intervalInMinutes);
        schedule(jobDetail, simpleTrigger);
    }

    private void schedule(JobDetail jobDetail, Trigger lastTrigger) {
        try {
            scheduler.start();
            scheduler.scheduleJob(jobDetail, lastTrigger);
        } catch (SchedulerException e) {
            JobExecutionException jobExecutionException = new JobExecutionException(e);
            jobExecutionException.setRefireImmediately(true);
        }
    }
}
