package com.boardgo.schedule.service;

import static com.boardgo.common.constant.TimeConstant.MINUTE;
import static com.boardgo.schedule.service.enums.ScheduleJobs.FINISHED_MEETING;

import com.boardgo.schedule.JobRunner;
import com.boardgo.schedule.job.FinishedMeetingStateJob;
import com.boardgo.schedule.service.enums.ScheduleJobs;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class FinishedMeetingStateService extends JobRunner {

    private final Scheduler scheduler;
    private final TriggerService triggerService;
    private final JobDetailService jobDetailService;

    @Override
    protected void doRun() {
        updateFinishMeetingState();
    }

    public void updateFinishMeetingState() {
        ScheduleJobs job = FINISHED_MEETING;
        JobKey jobKey = JobKey.jobKey(job.name(), job.getJobGroup());
        final int intervalInMinutes = 30;

        JobDetail jobDetail =
                jobDetailService.jobDetailBuilder(jobKey, FinishedMeetingStateJob.class);
        Trigger simpleTrigger = triggerService.simpleTrigger(jobKey, intervalInMinutes, MINUTE);
        schedule(jobDetail, simpleTrigger);
    }

    private void schedule(JobDetail jobDetail, Trigger lastTrigger) {
        try {
            scheduler.start();
            scheduler.scheduleJob(jobDetail, lastTrigger);
        } catch (SchedulerException e) {
            log.warn(
                    "Fail scheduler job :: {} , Error :: {}",
                    jobDetail.getKey().getName(),
                    e.getMessage());
        }
    }
}
