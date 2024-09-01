package com.boardgo.schedule.config;

import java.io.IOException;
import java.util.Properties;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

@Configuration
@RequiredArgsConstructor
public class QuartzConfig {

    private final JobsListener jobsListener;
    private final TriggersListener triggersListener;
    private final ApplicationContext applicationContext;

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean() {
        SchedulerFactoryBean schedulerFactory = new SchedulerFactoryBean();
        // application start > scheduler auto start after 30s
        schedulerFactory.setAutoStartup(true);
        schedulerFactory.setStartupDelay(3);

        schedulerFactory.setQuartzProperties(quartzProperties());
        schedulerFactory.setGlobalJobListeners(jobsListener);
        schedulerFactory.setGlobalTriggerListeners(triggersListener);
        schedulerFactory.setOverwriteExistingJobs(true);
        schedulerFactory.setWaitForJobsToCompleteOnShutdown(true);

        // custom job factory of spring with DI support for @Autowired
        AutowiringSpringBeanJobFactory jobFactory = new AutowiringSpringBeanJobFactory();
        jobFactory.setApplicationContext(applicationContext);
        schedulerFactory.setJobFactory(jobFactory);

        return schedulerFactory;
    }

    private Properties quartzProperties() {
        PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
        propertiesFactoryBean.setLocation(new ClassPathResource("/quartz.properties"));
        Properties properties;
        try {
            propertiesFactoryBean.afterPropertiesSet();
            properties = propertiesFactoryBean.getObject();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return properties;
    }
}
